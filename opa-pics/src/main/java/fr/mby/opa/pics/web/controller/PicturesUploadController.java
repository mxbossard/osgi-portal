/**
 * Copyright 2013 Maxime Bossard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.mby.opa.pics.web.controller;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IPicsDao;
import fr.mby.opa.pics.web.form.PicturesUploadForm;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Controller
@RequestMapping("/upload")
public class PicturesUploadController {

	public static final String THUMBNAIL_FORMAT = "jpg";

	public static final int THUMBNAIL_MAX_WIDTH = 200;

	public static final int THUMBNAIL_MAX_HEIGHT = 200;

	private static final boolean USE_RESIZE_HINT = true;

	@Autowired
	private IPicsDao picsDao;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String displayForm() {
		return "file_upload_form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("uploadForm") final PicturesUploadForm uploadForm, final Model map)
			throws Exception {

		final Collection<MultipartFile> pictures = uploadForm.getPictures();

		final Collection<String> picturesNames = new ArrayList<String>();

		if (null != pictures && pictures.size() > 0) {
			for (final MultipartFile multipartPicture : pictures) {

				if (!multipartPicture.isEmpty()) {
					final String fileName = multipartPicture.getOriginalFilename();
					picturesNames.add(fileName);

					final InputStream imageInput = multipartPicture.getInputStream();
					final BufferedImage originalImage = ImageIO.read(imageInput);
					final byte[] contents = multipartPicture.getBytes();

					final Picture pic = new Picture();
					pic.setPicture(contents);
					pic.setCreationTime(new DateTime());
					pic.setName(fileName);

					final String uniqueHash = this.generateHash(contents);
					pic.setUniqueHash(uniqueHash);

					final byte[] thumbnail = this.generateThumbnail(originalImage,
							PicturesUploadController.THUMBNAIL_FORMAT).toByteArray();
					pic.setThumbnail(thumbnail);

					this.picsDao.savePicture(pic);
				}
			}
		}

		map.addAttribute("picturesNames", picturesNames);
		return "file_upload_success";
	}

	/**
	 * @param contents
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected String generateHash(final byte[] contents) throws NoSuchAlgorithmException {
		final String myHash = "MD5"; // or "SHA-1" or "SHA-256"
		final MessageDigest hasher = MessageDigest.getInstance(myHash);

		return new String(hasher.digest(contents));
	}

	protected ByteArrayOutputStream generateThumbnail(final BufferedImage originalImage, final String formatName)
			throws IOException {

		final BufferedImage resizedImage = this.resizeImage(originalImage,
				PicturesUploadController.THUMBNAIL_MAX_WIDTH, PicturesUploadController.THUMBNAIL_MAX_HEIGHT, true,
				PicturesUploadController.USE_RESIZE_HINT);

		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(resizedImage, formatName, output);

		return output;
	}

	protected BufferedImage resizeImage(final BufferedImage image, final int width, final int height,
			final boolean keepScale, final boolean withHint) {
		final int oldWidth = image.getWidth();
		final int oldHeight = image.getHeight();

		final boolean isWidthLonger = oldWidth > oldHeight;

		int newWidth = -1;
		int newHeight = -1;

		if (keepScale) {
			if (isWidthLonger) {
				newWidth = width;
				final double ratio = (double) (oldHeight) / oldWidth;
				newHeight = (int) (width * ratio);
			} else {
				newHeight = height;
				final double ratio = oldWidth / oldHeight;
				newHeight = (int) (height * ratio);
			}
		} else {
			newWidth = width;
			newHeight = height;
		}

		final int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();

		return withHint ? this.resizeImageWithHint(image, newWidth, newHeight, type) : this.resizeImageWithoutHint(
				image, newWidth, newHeight, type);
	}

	protected BufferedImage resizeImageWithoutHint(final Image originalImage, final int width, final int height,
			final int type) {
		final BufferedImage resizedImage = new BufferedImage(width, height, type);

		final Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, width, height, null);
		graphics2D.dispose();

		return resizedImage;
	}

	protected BufferedImage resizeImageWithHint(final Image originalImage, final int width, final int height,
			final int type) {
		final BufferedImage resizedImage = new BufferedImage(width, height, type);

		final Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.setComposite(AlphaComposite.Src);
		// below three lines are for RenderingHints for better image quality at cost of higher processing time
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(originalImage, 0, 0, width, height, null);
		graphics2D.dispose();

		return resizedImage;
	}

}
