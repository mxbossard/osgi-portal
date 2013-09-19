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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IAlbumDao;
import fr.mby.opa.pics.service.IPictureDao;
import fr.mby.opa.pics.service.IPictureFactory;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IPortalApp;
import fr.mby.portal.api.message.IActionMessage;
import fr.mby.portal.api.message.IActionReply;
import fr.mby.portal.api.message.IRenderMessage;
import fr.mby.portal.api.message.IRenderReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping("")
public class PicsController implements IPortalApp {

	@Autowired
	private IAlbumDao albumDao;

	@Autowired
	private IPictureDao pictureDao;

	@Autowired
	private IPictureFactory pictureFactory;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final ModelAndView mv = new ModelAndView("index");

		final Collection<Picture> allPictures = this.pictureDao.findAllPictures();

		mv.addObject("pictures", allPictures);

		return mv;
	}

	@RequestMapping(value = "album", method = RequestMethod.POST)
	@ResponseBody
	public Album createAlbum(@Valid final Album album, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		this.albumDao.createAlbum(album);

		return album;
	}

	@RequestMapping(value = "album", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Album> findAllAlbumsJson(final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		final Collection<Album> albums = this.albumDao.findAllAlbums();

		return albums;
	}

	@RequestMapping(value = "album/{albumId}/pictures", method = RequestMethod.GET)
	@ResponseBody
	public List<Picture> findAlbumPictures(final Long albumId, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		final Album album = this.albumDao.loadAlbumById(albumId);

		return album.getPictures();
	}

	@RequestMapping(value = "image/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@PathVariable final Long id, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		ResponseEntity<byte[]> responseEntity = null;

		if (id != null) {

			final BinaryImage image = this.pictureDao.findImageById(id);
			if (image != null) {
				final byte[] thumbnailData = image.getData();

				final HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.setContentType(MediaType.parseMediaType("image/" + image.getFormat()));
				responseHeaders.setContentLength(thumbnailData.length);
				responseHeaders.set("Content-Disposition", "filename=\"" + image.getFilename() + '\"');

				responseEntity = new ResponseEntity<byte[]>(thumbnailData, responseHeaders, HttpStatus.OK);
			}
		}

		if (responseEntity == null) {
			responseEntity = new ResponseEntity<byte[]>(null, null, HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}

	@RequestMapping(value = "rebuildThumbnails/{width}/{height}/{format}", method = RequestMethod.GET)
	public String rebuildThumbnails(@PathVariable final Integer width, @PathVariable final Integer height,
			@PathVariable final String format, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		final Integer selectedWidth = (width != null) ? width : 800;
		final Integer selectedHeight = (height != null) ? height : 200;
		final String selectedFormat = (format != null) ? format : "jpg";

		final ExecutorService rebuildThumbnailsExecutor = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors());

		final Collection<Picture> pictures = this.pictureDao.findAllPictures();
		final Collection<Future<Void>> futures = new ArrayList<>(pictures.size());

		for (final Picture picture : pictures) {
			final Future<Void> future = rebuildThumbnailsExecutor.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					final BinaryImage generated = PicsController.this.pictureFactory.generateThumbnail(picture,
							selectedWidth, selectedHeight, true, selectedFormat);
					// Set the old Id to update
					final BinaryImage thumbnailToUpdate = picture.getThumbnail();
					thumbnailToUpdate.setData(generated.getData());
					thumbnailToUpdate.setFormat(generated.getFormat());
					thumbnailToUpdate.setWidth(generated.getWidth());
					thumbnailToUpdate.setHeight(generated.getHeight());

					picture.setThumbnail(generated);
					picture.setThumbnailWidth(generated.getWidth());
					picture.setThumbnailHeigth(generated.getHeight());
					picture.setThumbnailFormat(generated.getFormat());
					picture.setThumbnailSize(generated.getData().length);

					PicsController.this.pictureDao.updatePicture(picture);

					return null;
				}
			});

			futures.add(future);
		}

		for (final Future<Void> future : futures) {
			future.get();
		}

		return "index";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IPortalApp#destory()
	 */
	@Override
	public void destory() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IPortalApp#init(fr.mby.portal.api.app.IAppConfig)
	 */
	@Override
	public void init(final IAppConfig config) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IPortalApp#processAction(fr.mby.portal.api.message.IActionMessage,
	 * fr.mby.portal.api.message.IActionReply)
	 */
	@Override
	public void processAction(final IActionMessage request, final IActionReply response) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IPortalApp#render(fr.mby.portal.api.message.IRenderMessage,
	 * fr.mby.portal.api.message.IRenderReply)
	 */
	@Override
	public void render(final IRenderMessage request, final IRenderReply response) {
		// TODO Auto-generated method stub

	}

}