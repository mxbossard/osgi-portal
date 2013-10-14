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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import fr.mby.opa.pics.dao.IAlbumDao;
import fr.mby.opa.pics.dao.IPictureDao;
import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IPictureService;
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
@RequestMapping(PicsController.PICS_CONTROLLER_PATH)
public class PicsController implements IPortalApp {

	public static final String PICS_CONTROLLER_PATH = "";

	@Autowired
	private IAlbumDao albumDao;

	@Autowired
	private IPictureDao pictureDao;

	@Autowired
	private IPictureService pictureService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final ModelAndView mv = new ModelAndView("index");

		return mv;
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

		Long since = 0L;
		List<Picture> pictures = null;
		do {
			pictures = this.pictureDao.findAllPictures(since);
			final Collection<Future<Void>> futures = new ArrayList<>(pictures.size());

			for (final Picture picture : pictures) {
				final Future<Void> future = rebuildThumbnailsExecutor.submit(new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						final BinaryImage generated = PicsController.this.pictureService.generateThumbnail(picture,
								selectedWidth, selectedHeight, true, selectedFormat);
						// Set the old Id to update
						final BinaryImage thumbnailToUpdate = picture.getThumbnail();
						thumbnailToUpdate.setData(generated.getData());
						thumbnailToUpdate.setFormat(generated.getFormat());
						thumbnailToUpdate.setWidth(generated.getWidth());
						thumbnailToUpdate.setHeight(generated.getHeight());

						picture.setThumbnail(generated);
						picture.setThumbnailWidth(generated.getWidth());
						picture.setThumbnailHeight(generated.getHeight());
						picture.setThumbnailFormat(generated.getFormat());
						picture.setThumbnailSize(generated.getData().length);

						PicsController.this.pictureDao.updatePicture(picture);

						// Free memory
						picture.setThumbnail(null);
						picture.setImage(null);

						return null;
					}
				});

				since = picture.getOriginalTime().getTime();
				futures.add(future);
			}

			for (final Future<Void> future : futures) {
				future.get();
			}

		} while (pictures != null && pictures.size() > 0);

		return "index";
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleMethodArgumentNotValidException(final MethodArgumentNotValidException error) {
		return "Bad request: " + error.getMessage();
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(final Exception e) {
		return "return error object instead";
	}

	protected void buildPicturesJson(final List<Picture> pictures) throws JSONException {

		final JSONStringer jsonStringer = new JSONStringer();
		// Start Pic Array
		jsonStringer.array();

		final Iterator<Picture> dirPic = pictures.iterator();
		while (dirPic.hasNext()) {
			final Picture pic = dirPic.next();
			// Start Picture
			jsonStringer.object();

			jsonStringer.key("id").value(pic.getId());
			jsonStringer.key("name").value(pic.getName());
			jsonStringer.key("thumbnailId").value(pic.getThumbnail().getId());
			jsonStringer.key("imageId").value(pic.getImage().getId());

			// End Picture
			jsonStringer.endObject();
		}

		// End Pic Array
		jsonStringer.endArray();

		String jsonMetadata = jsonStringer.toString();
		jsonMetadata = jsonMetadata.replaceAll("\"", "'");

		// return new ResponseEntity<String>(jsonMetadata, null, HttpStatus.CREATED);
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