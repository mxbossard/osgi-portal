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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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

		final Collection<MultipartFile> pictures = uploadForm.getPictures();

		final Collection<String> picturesNames = new ArrayList<String>();

		if (null != pictures && pictures.size() > 0) {
			final Collection<Future<Picture>> futures = new ArrayList<>(pictures.size());

			for (final MultipartFile multipartFile : pictures) {

				final Future<Picture> future = UploadPicturesController.BUILD_PICTURE_EXECUTOR
						.submit(new Callable<Picture>() {

							@Override
							public Picture call() throws Exception {
								Picture picture = null;
								try {
									picture = UploadPicturesController.this.createPicture(multipartFile);
								} catch (final PictureAlreadyExistsException e) {
									// Nothing to do
								}

								return picture;
							}

						});
				futures.add(future);
			}

			for (final Future<Picture> future : futures) {
				final Picture picture = future.get();
				if (picture != null) {
					picturesNames.add(picture.getFilename());
				}
			}

		}

		map.addAttribute("picturesNames", picturesNames);
		return "file_upload_success";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String testUploadFile(final MultipartHttpServletRequest request, final HttpServletResponse response)
			throws IOException {

		// 1. build an iterator
		// final Iterator<String> itr = request.getFileNames();
		final List<MultipartFile> files = request.getFiles("pictures");
		final Iterator<MultipartFile> itr = files.iterator();

		MultipartFile mpf = null;

		// 2. get each file
		while (itr.hasNext()) {

			mpf = itr.next();
			// 2.1 get next MultipartFile
			// mpf = request.getFile(itr.next());

			final InputStream stream = mpf.getInputStream();

		}

		return "file_upload_success";
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
