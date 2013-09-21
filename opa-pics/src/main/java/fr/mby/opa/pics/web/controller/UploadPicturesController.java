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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
				// Free memory
				multipartFileIt.remove();

				final Future<Picture> future = UploadPicturesController.BUILD_PICTURE_EXECUTOR
						.submit(new Callable<Picture>() {

							@Override
							public Picture call() throws Exception {
								Picture picture = null;
								try {
									picture = UploadPicturesController.this.createPicture(multipartFile);

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
				// Free memory
				futureIt.remove();

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

			// Free memory
			itr.remove();

			// 2.3 create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(mpf.getOriginalFilename());
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());

			fileMeta.setBytes(mpf.getBytes());

			try {
				final Picture picture = this.createPicture(mpf);

				final Long imageId = picture.getImage().getId();
				final Long thumbnailId = picture.getThumbnail().getId();

				fileMeta.setUrl(response.encodeURL(PicsController.GET_IMAGE_PATH + imageId));
				fileMeta.setThumbnailUrl(response.encodeURL(PicsController.GET_IMAGE_PATH + thumbnailId));

				// 2.4 add to files
				files.add(fileMeta);
			} catch (final PictureAlreadyExistsException e) {
				// Picture already exists !
			}

		}

		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		return files;
	}

	protected Picture createPicture(final MultipartFile multipartFile) throws PictureAlreadyExistsException,
			IOException {
		final Picture picture = this.pictureFactory.build(multipartFile);

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
