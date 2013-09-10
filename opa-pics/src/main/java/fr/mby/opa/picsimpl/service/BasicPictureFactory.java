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

package fr.mby.opa.picsimpl.service;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONStringer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IPictureFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicPictureFactory implements IPictureFactory {

	/** Logger. */
	private static final Logger LOG = LogManager.getLogger(BasicPictureFactory.class);

	public static final String THUMBNAIL_FORMAT = "jpg";

	public static final int THUMBNAIL_MAX_WIDTH = 300;

	public static final int THUMBNAIL_MAX_HEIGHT = 230;

	private static final boolean USE_RESIZE_HINT = true;

	/** "MD5" or "SHA-1" or "SHA-256" */
	private static final String HASH_ALGORITHM = "SHA-256";

	private static final String DEFAULT_PICTURE_FORMAT = "jpg";

	@Override
	public Picture build(final MultipartFile multipartFile) throws IOException {
		Picture pic = null;
		if (!multipartFile.isEmpty()) {
			pic = new Picture();

			final String fileName = multipartFile.getOriginalFilename();
			pic.setFilename(fileName);
			pic.setName(fileName);

			final byte[] contents = multipartFile.getBytes();
			final BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(contents),
					contents.length);
			bufferedStream.mark(contents.length);

			this.loadPicture(pic, contents, bufferedStream);

			this.loadMetadata(pic, bufferedStream);

			bufferedStream.close();
		}

		return pic;
	}

	protected void loadPicture(final Picture picture, final byte[] contents, final BufferedInputStream stream)
			throws IOException {

		// Generate picture hash
		final byte[] uniqueHash = this.generateHash(contents);
		picture.setUniqueHash(new String(uniqueHash));

		// Load BufferedImage
		stream.reset();
		final BufferedImage originalImage = ImageIO.read(stream);
		final int width = originalImage.getWidth();
		final int height = originalImage.getHeight();
		picture.setWidth(width);
		picture.setHeight(height);
		picture.setSize(contents.length);

		// Load thumbnail
		this.loadThumbnail(picture, originalImage);

		// Search for picture format
		String format = null;
		// with Java image readers
		stream.reset();
		final ImageInputStream imageStream = ImageIO.createImageInputStream(stream);
		final Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageStream);
		if (imageReaders.hasNext()) {
			format = imageReaders.next().getFormatName();
		}
		// In picture filename extension
		if (format == null) {
			format = FilenameUtils.getExtension(picture.getFilename());
		}
		// Default format
		if (format == null) {
			format = BasicPictureFactory.DEFAULT_PICTURE_FORMAT;
		}
		picture.setFormat(format.toLowerCase());

		// Build Contents
		final BinaryImage image = new BinaryImage();
		image.setData(contents);
		image.setFilename(picture.getFilename());
		image.setFormat(format);
		image.setWidth(width);
		image.setHeight(height);
		picture.setImage(image);
	}

	protected void loadThumbnail(final Picture picture, final BufferedImage originalImage) throws IOException {
		final String thumbnailFormat = BasicPictureFactory.THUMBNAIL_FORMAT;

		final BufferedImage resizedImage = this.resizeImage(originalImage, BasicPictureFactory.THUMBNAIL_MAX_WIDTH,
				BasicPictureFactory.THUMBNAIL_MAX_HEIGHT, true, BasicPictureFactory.USE_RESIZE_HINT);

		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(resizedImage, thumbnailFormat, output);

		final byte[] thumbnailData = output.toByteArray();

		final int width = resizedImage.getWidth();
		final int height = resizedImage.getHeight();
		picture.setThumbnailWidth(width);
		picture.setThumbnailHeigth(height);
		picture.setThumbnailSize(thumbnailData.length);
		picture.setThumbnailFormat(thumbnailFormat);

		// Build Thumbnail
		final BinaryImage thumbnail = new BinaryImage();
		thumbnail.setData(thumbnailData);
		thumbnail.setFilename(picture.getFilename());
		thumbnail.setFormat(thumbnailFormat);
		thumbnail.setWidth(width);
		thumbnail.setHeight(height);
		picture.setThumbnail(thumbnail);
	}

	protected void loadMetadata(final Picture picture, final BufferedInputStream stream) throws IOException {
		try {
			stream.reset();
			final Metadata metadata = ImageMetadataReader.readMetadata(stream, false);

			// Process all metadatas
			final JSONStringer jsonStringer = new JSONStringer();
			// Start main object
			jsonStringer.object();

			final Iterator<Directory> dirIt = metadata.getDirectories().iterator();
			while (dirIt.hasNext()) {
				final Directory dir = dirIt.next();
				// Start Directory
				jsonStringer.key(dir.getName()).object();
				final Collection<Tag> dirTags = dir.getTags();
				for (final Tag tag : dirTags) {
					final String tagName = tag.getTagName();
					final String tagDesc = tag.getDescription();
					// Add Tag
					jsonStringer.key(tagName).value(tagDesc);
				}

				// End Directory
				jsonStringer.endObject();
			}

			// End main object
			jsonStringer.endObject();

			final String jsonMetadata = jsonStringer.toString();
			picture.setJsonMetadata(jsonMetadata);

			// Process specific metadata

			// Creation time
			DateTime creationTime = null;
			final ExifIFD0Directory exifDir = metadata.getDirectory(ExifIFD0Directory.class);
			Date date = exifDir.getDate(ExifIFD0Directory.TAG_DATETIME);

			final ExifSubIFDDirectory subExifDir = metadata.getDirectory(ExifSubIFDDirectory.class);
			if (date == null) {
				date = subExifDir.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			}

			if (date == null) {
				date = subExifDir.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
			}

			if (date != null) {
				creationTime = new DateTime(date);
			} else {
				creationTime = new DateTime();
			}

			picture.setCreationTime(creationTime);

		} catch (final ImageProcessingException e) {
			// Unable to process metadata
			BasicPictureFactory.LOG.warn("Unable to read metadata !", e);
		} catch (final JSONException e) {
			// Technical problem
			BasicPictureFactory.LOG.warn("Problem while converting Metadata to JSON !", e);
			throw new RuntimeException("Problem while converting Metadata to JSON !", e);
		}
	}

	/**
	 * @param contents
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	protected byte[] generateHash(final byte[] contents) {
		MessageDigest hasher;

		try {
			hasher = MessageDigest.getInstance(BasicPictureFactory.HASH_ALGORITHM);
		} catch (final NoSuchAlgorithmException e) {
			throw new RuntimeException("Hash algorithm missing !", e);
		}

		return hasher.digest(contents);
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

	protected BufferedImage resizeImageWithoutHint(final java.awt.Image originalImage, final int width,
			final int height, final int type) {
		final BufferedImage resizedImage = new BufferedImage(width, height, type);

		final Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, width, height, null);
		graphics2D.dispose();

		return resizedImage;
	}

	protected BufferedImage resizeImageWithHint(final java.awt.Image originalImage, final int width, final int height,
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
