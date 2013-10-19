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
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import fr.mby.opa.pics.exception.PictureAlreadyExistsException;
import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IPictureFactory;
import fr.mby.opa.pics.service.IPictureService;
import fr.mby.opa.pics.service.UnsupportedPictureTypeException;
import fr.mby.utils.common.image.ImageHelper;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicPictureFactory implements IPictureFactory {

	/** Logger. */
	private static final Logger LOG = LogManager.getLogger(BasicPictureFactory.class);

	private static final String DEFAULT_PICTURE_FORMAT = "jpeg";

	@Autowired
	private IPictureService pictureService;

	@Override
	public Picture build(final String filename, final byte[] contents) throws IOException,
			PictureAlreadyExistsException, UnsupportedPictureTypeException {
		Assert.hasText(filename, "No filename supplied !");
		Assert.notNull(contents, "No contents supplied !");

		Picture picture = null;
		if (contents.length > 0) {
			picture = new Picture();

			picture.setFilename(filename);
			picture.setName(filename);

			final BufferedInputStream bufferedStream = new BufferedInputStream(new ByteArrayInputStream(contents),
					contents.length);
			bufferedStream.mark(contents.length + 1);

			this.loadPicture(picture, contents, bufferedStream);

			this.loadMetadata(picture, bufferedStream);

			bufferedStream.close();
		}

		return picture;
	}

	@Override
	public BinaryImage buildThumbnail(final BufferedImage originalImage, final String filename, final Integer width,
			final Integer height, final String format) throws IOException {

		final BufferedImage resizedImage = ImageHelper.resize(originalImage, width, height, true, true);

		final byte[] thumbnailData = ImageHelper.toByteArray(resizedImage, format);

		// Build Thumbnail
		final BinaryImage thumbnail = new BinaryImage();
		thumbnail.setData(thumbnailData);
		thumbnail.setFilename(filename);
		thumbnail.setFormat(format);
		thumbnail.setWidth(resizedImage.getWidth());
		thumbnail.setHeight(resizedImage.getHeight());

		return thumbnail;
	}

	protected void loadPicture(final Picture picture, final byte[] contents, final BufferedInputStream stream)
			throws IOException, UnsupportedPictureTypeException {

		// Load BufferedImage
		stream.reset();
		final BufferedImage originalImage = ImageIO.read(stream);
		if (originalImage == null) {
			throw new UnsupportedPictureTypeException(picture.getFilename());
		}

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
		final String thumbnailFormat = BasicPictureService.THUMBNAIL_FORMAT;

		// Build Thumbnail
		final BinaryImage thumbnail = this.buildThumbnail(originalImage, "thumb" + picture.getFilename(),
				BasicPictureService.THUMBNAIL_MAX_WIDTH, BasicPictureService.THUMBNAIL_MAX_HEIGHT, thumbnailFormat);

		picture.setThumbnailWidth(thumbnail.getWidth());
		picture.setThumbnailHeight(thumbnail.getHeight());
		picture.setThumbnailSize(thumbnail.getData().length);
		picture.setThumbnailFormat(thumbnail.getFormat());

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
					final int tagType = tag.getTagType();
					final String tagName = tag.getTagName();
					final String tagDesc = tag.getDescription();
					// Add Tag
					jsonStringer.key(tagName + "[" + tagType + "]").value(tagDesc);
				}

				// End Directory
				jsonStringer.endObject();
			}

			// End main object
			jsonStringer.endObject();

			final String jsonMetadata = jsonStringer.toString();
			picture.setJsonMetadata(jsonMetadata);

			// Process specific metadata

			// Times
			final Timestamp originalTime = this.findOriginalTime(metadata);
			picture.setOriginalTime(originalTime);

			picture.setCreationTime(new Timestamp(System.currentTimeMillis()));

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
	 * Search metadata to find original time.
	 * 
	 * @param metadata
	 * @return
	 */
	protected Timestamp findOriginalTime(final Metadata metadata) {
		Date date = null;

		final ExifSubIFDDirectory subExifDir = metadata.getDirectory(ExifSubIFDDirectory.class);
		if (date == null) {
			date = subExifDir.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		}

		if (date == null) {
			date = subExifDir.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
		}

		if (date == null) {
			final ExifIFD0Directory exifDir = metadata.getDirectory(ExifIFD0Directory.class);
			date = exifDir.getDate(ExifIFD0Directory.TAG_DATETIME);
		}

		Timestamp creationTime = null;

		if (date != null) {
			creationTime = new Timestamp(date.getTime());
		} else {
			creationTime = new Timestamp(System.currentTimeMillis());
		}
		return creationTime;
	}

}
