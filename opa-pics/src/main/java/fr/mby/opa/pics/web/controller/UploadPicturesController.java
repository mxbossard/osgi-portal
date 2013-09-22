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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Iterables;

import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IAlbumDao;
import fr.mby.opa.pics.service.IPictureDao;
import fr.mby.opa.pics.service.IPictureFactory;
import fr.mby.opa.pics.service.PictureAlreadyExistsException;
import fr.mby.opa.pics.service.UnsupportedPictureTypeException;
import fr.mby.opa.pics.web.form.PicturesUploadForm;
import fr.mby.opa.pics.web.jquery.FileMeta;
import fr.mby.opa.pics.web.jquery.FileMetaList;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Controller
@RequestMapping("upload")
public class UploadPicturesController {

	/** ExecutorService used to build Picture. */
	private static final ExecutorService BUILD_PICTURE_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime()
			.availableProcessors() * 3);

	private static final String PICTURE_ALREADY_EXISTS_MSG = "Picture already exists in the database : ";

	private static final String UNSUPPORTED_PICTURE_TYPE_MSG = "Unsupported picture type : ";

	private static final String ZIP_CONTENT_TYPE_REGEX = "application/(zip|x-zip-compressed)";

	private static final Pattern ZIP_CONTENT_TYPE_PATTERN = Pattern
			.compile(UploadPicturesController.ZIP_CONTENT_TYPE_REGEX);

	private static final String IMG_CONTENT_TYPE_REGEX = "image/(jpe?g|gif|png)";

	private static final Pattern IMG_CONTENT_TYPE_PATTERN = Pattern
			.compile(UploadPicturesController.IMG_CONTENT_TYPE_REGEX);

	@Autowired
	private IPictureFactory pictureFactory;

	@Autowired
	private IAlbumDao albumDao;

	@Autowired
	private IPictureDao pictureDao;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String displayForm() {
		return "file_upload_form";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("uploadForm") final PicturesUploadForm uploadForm, final Model map)
			throws Exception {
		final Collection<String> uploadedPicturesNames = new ArrayList<String>();
		final Collection<String> alreayExistingPicturesNames = new ArrayList<String>();

		final Collection<MultipartFile> pictures = uploadForm.getPictures();

		if (null != pictures && pictures.size() > 0) {
			final Collection<Future<Picture>> futures = new ArrayList<>(pictures.size());

			final Iterator<MultipartFile> multipartFileIt = pictures.iterator();
			while (multipartFileIt.hasNext()) {
				final MultipartFile multipartFile = multipartFileIt.next();

				final Future<Picture> future = UploadPicturesController.BUILD_PICTURE_EXECUTOR
						.submit(new Callable<Picture>() {

							@Override
							public Picture call() throws Exception {
								Picture picture = null;
								try {
									picture = UploadPicturesController.this.createPicture(
											multipartFile.getOriginalFilename(), multipartFile.getBytes());

									// Free memory: we don't need blobs
									picture.setThumbnail(null);
									picture.setImage(null);
								} catch (final PictureAlreadyExistsException e) {
									alreayExistingPicturesNames.add(e.getFilename());
								}

								return picture;
							}
						});

				futures.add(future);
			}

			final Iterator<Future<Picture>> futureIt = futures.iterator();
			while (futureIt.hasNext()) {
				final Future<Picture> future = futureIt.next();

				final Picture picture = future.get();
				if (picture != null) {
					uploadedPicturesNames.add(picture.getFilename());
				}
			}

		}

		map.addAttribute("picturesNames", uploadedPicturesNames);
		return "file_upload_success";
	}

	/***************************************************
	 * URL: /upload/jqueryUpload upload(): receives files
	 * 
	 * @param request
	 *            : MultipartHttpServletRequest auto passed
	 * @param response
	 *            : HttpServletResponse auto passed
	 * @return LinkedList<FileMeta> as json format
	 ****************************************************/
	@ResponseBody
	@RequestMapping(value = "/jqueryUpload", method = RequestMethod.POST)
	public FileMetaList jqueryUpload(final MultipartHttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		final FileMetaList files = new FileMetaList();
		FileMeta fileMeta = null;

		// 1. build an iterator
		final Iterator<String> itr = request.getFileNames();

		// 2. get each file
		while (itr.hasNext()) {

			// 2.1 get next MultipartFile
			final MultipartFile mpf = request.getFile(itr.next());

			// Here the file is uploaded

			final String originalFilename = mpf.getOriginalFilename();
			final byte[] fileContents = mpf.getBytes();
			final String contentType = mpf.getContentType();

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(originalFilename);
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());

			fileMeta.setBytes(fileContents);

			final Matcher zipMatcher = UploadPicturesController.ZIP_CONTENT_TYPE_PATTERN.matcher(contentType);
			if (zipMatcher.find()) {
				final List<Path> picturesPaths = this.processArchive(mpf);
				for (final Path picturePath : picturesPaths) {
					final String pictureFileName = picturePath.getName(picturePath.getNameCount() - 1).toString();
					final byte[] pictureContents = Files.readAllBytes(picturePath);

					final FileMeta pictureMeta = new FileMeta();
					try {
						final Picture picture = this.createPicture(pictureFileName.toString(), pictureContents);
						final Long imageId = picture.getImage().getId();
						final Long thumbnailId = picture.getThumbnail().getId();

						pictureMeta.setFileName(pictureFileName);
						pictureMeta.setFileSize(pictureContents.length / 1024 + " Kb");
						pictureMeta.setFileType(Files.probeContentType(picturePath));
						pictureMeta.setUrl(response.encodeURL(PicsController.GET_IMAGE_PATH + imageId));
						pictureMeta.setThumbnailUrl(response.encodeURL(PicsController.GET_IMAGE_PATH + thumbnailId));
					} catch (final PictureAlreadyExistsException e) {
						// Picture already exists !
						pictureMeta.setError(UploadPicturesController.PICTURE_ALREADY_EXISTS_MSG + e.getFilename());
					} catch (final UnsupportedPictureTypeException e) {
						// Picture already exists !
						pictureMeta.setError(UploadPicturesController.UNSUPPORTED_PICTURE_TYPE_MSG + e.getFilename());
					}

					files.add(pictureMeta);
				}
			}

			final Matcher imgMatcher = UploadPicturesController.IMG_CONTENT_TYPE_PATTERN.matcher(contentType);
			if (imgMatcher.find()) {
				try {
					final Picture picture = this.createPicture(originalFilename, fileContents);

					final Long imageId = picture.getImage().getId();
					final Long thumbnailId = picture.getThumbnail().getId();

					fileMeta.setUrl(response.encodeURL(PicsController.GET_IMAGE_PATH + imageId));
					fileMeta.setThumbnailUrl(response.encodeURL(PicsController.GET_IMAGE_PATH + thumbnailId));
				} catch (final PictureAlreadyExistsException e) {
					// Picture already exists !
					fileMeta.setError(UploadPicturesController.PICTURE_ALREADY_EXISTS_MSG);
				}
			}

			// 2.4 add to files
			files.add(fileMeta);
		}

		// result will be like this
		// {files:[{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]}
		return files;
	}

	protected Picture createPicture(final String filename, final byte[] contents) throws PictureAlreadyExistsException,
			IOException, UnsupportedPictureTypeException {
		final Picture picture = this.pictureFactory.build(filename, contents);

		if (picture != null) {
			UploadPicturesController.this.pictureDao.createPicture(picture, UploadPicturesController.this.initAlbum());
		}

		return picture;
	}

	protected Album initAlbum() {
		final Collection<Album> albums = this.albumDao.findAllAlbums();
		Album firstAlbum = Iterables.getFirst(albums, null);

		if (firstAlbum == null) {
			final Album newAlbum = new Album();
			newAlbum.setName("myFirstAlbum");
			newAlbum.setCreationTime(new DateTime());

			firstAlbum = this.albumDao.createAlbum(newAlbum);
		}

		return firstAlbum;
	}

	protected List<Path> processArchive(final MultipartFile multipartFile) throws IOException {
		final List<Path> archivePictures = new ArrayList<>(128);

		// We copy the archive in a tmp file
		final File tmpFile = File.createTempFile(multipartFile.getName(), ".tmp");
		multipartFile.transferTo(tmpFile);

		final Path tmpFilePath = tmpFile.toPath();
		final FileSystem archiveFs = FileSystems.newFileSystem(tmpFilePath, null);

		final Iterable<Path> rootDirs = archiveFs.getRootDirectories();
		for (final Path rootDir : rootDirs) {
			Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) throws IOException {
					final boolean isDirectory = Files.isDirectory(path);

					if (!isDirectory) {
						final String contentType = Files.probeContentType(path);
						if (contentType != null && contentType.startsWith("image/")) {
							archivePictures.add(path);
						}
					}

					return super.visitFile(path, attrs);
				}

			});
		}

		return archivePictures;
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

	/**
	 * Getter of picsDao.
	 * 
	 * @return the picsDao
	 */
	public IPictureDao getPicsDao() {
		return this.pictureDao;
	}

	/**
	 * Setter of picsDao.
	 * 
	 * @param picsDao
	 *            the picsDao to set
	 */
	public void setPicsDao(final IPictureDao picsDao) {
		this.pictureDao = picsDao;
	}

}
