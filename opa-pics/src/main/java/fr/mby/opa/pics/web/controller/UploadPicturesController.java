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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IAlbumDao;
import fr.mby.opa.pics.service.IPictureDao;
import fr.mby.opa.pics.service.IPictureService;
import fr.mby.opa.pics.service.PictureAlreadyExistsException;
import fr.mby.opa.pics.service.UnsupportedPictureTypeException;
import fr.mby.opa.pics.web.jquery.FileMeta;
import fr.mby.opa.pics.web.jquery.FileMetaList;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Controller
@RequestMapping(UploadPicturesController.UPLOAD_CONTROLLER_PATH)
public class UploadPicturesController {

	public static final String UPLOAD_CONTROLLER_PATH = "/upload";

	private static final String PICTURE_ALREADY_EXISTS_MSG = "Picture already exists in the database : ";

	private static final String UNSUPPORTED_PICTURE_TYPE_MSG = "Unsupported picture type : ";

	private static final String ZIP_CONTENT_TYPE_REGEX = "application/(zip|x-zip-compressed)";

	private static final Pattern ZIP_CONTENT_TYPE_PATTERN = Pattern
			.compile(UploadPicturesController.ZIP_CONTENT_TYPE_REGEX);

	private static final String IMG_CONTENT_TYPE_REGEX = "image/(jpe?g|gif|png)";

	private static final Pattern IMG_CONTENT_TYPE_PATTERN = Pattern
			.compile(UploadPicturesController.IMG_CONTENT_TYPE_REGEX);

	@Autowired
	private IPictureService pictureService;

	@Autowired
	private IAlbumDao albumDao;

	@Autowired
	private IPictureDao pictureDao;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String displayForm() {
		return "file_upload_form";
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
	public FileMetaList jqueryUpload(@RequestParam final Long albumId, final MultipartHttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		Assert.notNull(albumId, "No Album Id supplied !");

		final FileMetaList files = new FileMetaList();

		// 1. build an iterator
		final Iterator<String> itr = request.getFileNames();

		// 2. get each file
		while (itr.hasNext()) {

			// 2.1 get next MultipartFile
			final MultipartFile mpf = request.getFile(itr.next());

			// Here the file is uploaded

			final String originalFilename = mpf.getOriginalFilename();
			final String contentType = mpf.getContentType();

			final Matcher zipMatcher = UploadPicturesController.ZIP_CONTENT_TYPE_PATTERN.matcher(contentType);
			if (zipMatcher.find()) {

				// 2.3 create new fileMeta
				final FileMeta zipMeta = new FileMeta();
				zipMeta.setFileName(originalFilename);
				zipMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
				zipMeta.setFileType(mpf.getContentType());

				final List<Path> picturesPaths = this.processArchive(mpf);

				final Collection<Future<Void>> futures = new ArrayList<>(picturesPaths.size());
				final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
						.availableProcessors());

				for (final Path picturePath : picturesPaths) {
					final Future<Void> future = executorService.submit(new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							final String pictureFileName = picturePath.getName(picturePath.getNameCount() - 1)
									.toString();
							final byte[] pictureContents = Files.readAllBytes(picturePath);

							final FileMeta pictureMeta = new FileMeta();
							try {
								final Picture picture = UploadPicturesController.this.createPicture(albumId,
										pictureFileName.toString(), pictureContents);
								final Long imageId = picture.getImage().getId();
								final Long thumbnailId = picture.getThumbnail().getId();

								pictureMeta.setFileName(pictureFileName);
								pictureMeta.setFileSize(pictureContents.length / 1024 + " Kb");
								pictureMeta.setFileType(Files.probeContentType(picturePath));
								pictureMeta.setUrl(response.encodeURL(ImageController.IMAGE_CONTROLLER_PATH + "/"
										+ imageId));
								pictureMeta.setThumbnailUrl(response.encodeURL(ImageController.IMAGE_CONTROLLER_PATH
										+ "/" + thumbnailId));
							} catch (final PictureAlreadyExistsException e) {
								// Picture already exists !
								pictureMeta.setError(UploadPicturesController.PICTURE_ALREADY_EXISTS_MSG
										+ e.getFilename());
							} catch (final UnsupportedPictureTypeException e) {
								// Picture already exists !
								pictureMeta.setError(UploadPicturesController.UNSUPPORTED_PICTURE_TYPE_MSG
										+ e.getFilename());
							}

							files.add(pictureMeta);

							return null;
						}
					});
					futures.add(future);
				}

				for (final Future<Void> future : futures) {
					future.get();
				}

				files.add(zipMeta);
			}

			final Matcher imgMatcher = UploadPicturesController.IMG_CONTENT_TYPE_PATTERN.matcher(contentType);
			if (imgMatcher.find()) {
				// 2.3 create new fileMeta
				final FileMeta fileMeta = new FileMeta();
				try {
					final byte[] fileContents = mpf.getBytes();
					final Picture picture = this.createPicture(albumId, originalFilename, fileContents);

					final Long imageId = picture.getImage().getId();
					final Long thumbnailId = picture.getThumbnail().getId();

					fileMeta.setFileName(originalFilename);
					fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
					fileMeta.setFileType(mpf.getContentType());
					fileMeta.setBytes(fileContents);
					fileMeta.setUrl(response.encodeURL(ImageController.IMAGE_CONTROLLER_PATH + "/" + imageId));
					fileMeta.setThumbnailUrl(response.encodeURL(ImageController.IMAGE_CONTROLLER_PATH + "/"
							+ thumbnailId));

					// 2.4 add to files
					files.add(fileMeta);
				} catch (final PictureAlreadyExistsException e) {
					// Picture already exists !
					fileMeta.setError(UploadPicturesController.PICTURE_ALREADY_EXISTS_MSG);
				}
			}

		}

		// result will be like this
		// {files:[{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]}
		return files;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(final Exception e) {
		return "return error object instead";
	}

	protected Picture createPicture(final Long albumId, final String filename, final byte[] contents)
			throws PictureAlreadyExistsException, IOException, UnsupportedPictureTypeException {
		Picture picture = null;

		final Album album = this.albumDao.findAlbumById(albumId);
		if (album != null) {
			picture = this.pictureService.createPicture(filename, contents);

			if (picture != null) {
				UploadPicturesController.this.pictureDao.createPicture(picture, album);
			}
		}

		return picture;
	}

	protected List<Path> processArchive(final MultipartFile multipartFile) throws IOException {
		final List<Path> archivePictures = new ArrayList<>(128);

		// We copy the archive in a tmp file
		final File tmpFile = File.createTempFile(multipartFile.getName(), ".tmp");
		multipartFile.transferTo(tmpFile);
		// final InputStream archiveInputStream = multipartFile.getInputStream();
		// Streams.copy(archiveInputStream, new FileOutputStream(tmpFile), true);
		// archiveInputStream.close();

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
	 * Getter of pictureService.
	 * 
	 * @return the pictureService
	 */
	public IPictureService getPictureService() {
		return this.pictureService;
	}

	/**
	 * Setter of pictureService.
	 * 
	 * @param pictureService
	 *            the pictureService to set
	 */
	public void setPictureService(final IPictureService pictureService) {
		this.pictureService = pictureService;
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
