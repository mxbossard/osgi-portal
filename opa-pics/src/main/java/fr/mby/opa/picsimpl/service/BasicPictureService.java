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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IPictureDao;
import fr.mby.opa.pics.service.IPictureFactory;
import fr.mby.opa.pics.service.IPictureService;
import fr.mby.opa.pics.service.PictureAlreadyExistsException;
import fr.mby.opa.pics.service.UnsupportedPictureTypeException;
import fr.mby.utils.common.image.ImageHelper;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicPictureService implements IPictureService {

	public static final String THUMBNAIL_FORMAT = "jpeg";

	public static final int THUMBNAIL_MAX_WIDTH = 1000;

	public static final int THUMBNAIL_MAX_HEIGHT = 200;

	/** "MD5" or "SHA-1" or "SHA-256" */
	private static final HashFunction HASH_FUNCTION = Hashing.sha256();

	@Autowired
	private IPictureDao pictureDao;

	@Autowired
	private IPictureFactory pictureFactory;

	@Override
	public Picture createPicture(final String filename, final byte[] contents) throws PictureAlreadyExistsException,
			IOException, UnsupportedPictureTypeException {
		Assert.hasText(filename, "No filename supplied !");
		Assert.notNull(contents, "No contents supplied !");

		Picture picture = null;
		if (contents.length > 0) {
			// Generate picture hash
			final String uniqueHash = this.generateHash(contents);

			final Long alreadyExistingPictureId = this.pictureDao.findPictureIdByHash(uniqueHash);
			if (alreadyExistingPictureId != null) {
				throw new PictureAlreadyExistsException(filename);
			}

			picture = this.pictureFactory.build(filename, contents);
			picture.setHash(uniqueHash);
		}

		return picture;
	}

	@Override
	public BinaryImage generateThumbnail(final Picture picture, final int width, final int height,
			final boolean keepScale, final String format) throws IOException {

		final BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(picture.getImage().getData()));

		return this.pictureFactory.buildThumbnail(originalImage, picture.getFilename(), width, height, format);
	}

	@Override
	public Picture rotatePicture(final Long pictureId, final Integer angle) throws IOException {
		Assert.notNull(pictureId, "Picture Id not supplied !");
		Assert.notNull(angle, "Rotating angle not supplied !");

		Picture rotatedPicture = null;

		final Picture picture = this.pictureDao.loadFullPictureById(pictureId);
		if (picture != null) {
			final String filename = picture.getThumbnail().getFilename();
			final byte[] contents = picture.getImage().getData();
			try (ByteArrayInputStream bais = new ByteArrayInputStream(contents)) {
				final BufferedImage bufferedImage = ImageIO.read(bais);
				final BufferedImage rotatedImage = ImageHelper.toBufferedImage(ImageHelper.rotateWithHint(
						bufferedImage, angle));

				final BinaryImage thumbnail = this.pictureFactory.buildThumbnail(rotatedImage, filename,
						BasicPictureService.THUMBNAIL_MAX_WIDTH, BasicPictureService.THUMBNAIL_MAX_HEIGHT,
						BasicPictureService.THUMBNAIL_FORMAT);

				final byte[] rotatedData = ImageHelper.toByteArray(rotatedImage, picture.getFormat());

				picture.getImage().setData(rotatedData);
				picture.setHeight(rotatedImage.getHeight());
				picture.setWidth(rotatedImage.getWidth());
				picture.setSize(rotatedData.length);

				picture.setThumbnail(thumbnail);
				picture.setThumbnailFormat(thumbnail.getFormat());
				picture.setThumbnailHeight(thumbnail.getHeight());
				picture.setThumbnailWidth(thumbnail.getWidth());
				picture.setThumbnailSize(thumbnail.getData().length);
			}

			rotatedPicture = this.pictureDao.updatePicture(picture);
		}

		return rotatedPicture;
	}

	@Override
	public String generateHash(final byte[] contents) {
		final Hasher hasher = BasicPictureService.HASH_FUNCTION.newHasher();

		final HashCode hashCode = hasher.putBytes(contents).hash();

		return hashCode.toString();
	}

	/**
	 * Getter of pictureDao.
	 * 
	 * @return the pictureDao
	 */
	public IPictureDao getPictureDao() {
		return this.pictureDao;
	}

	/**
	 * Setter of pictureDao.
	 * 
	 * @param pictureDao
	 *            the pictureDao to set
	 */
	public void setPictureDao(final IPictureDao pictureDao) {
		this.pictureDao = pictureDao;
	}

	/**
	 * Getter of pictureFactory.
	 * 
	 * @return the pictureFactory
	 */
	public IPictureFactory getPictureFactory() {
		return this.pictureFactory;
	}

	/**
	 * Setter of pictureFactory.
	 * 
	 * @param pictureFactory
	 *            the pictureFactory to set
	 */
	public void setPictureFactory(final IPictureFactory pictureFactory) {
		this.pictureFactory = pictureFactory;
	}

}
