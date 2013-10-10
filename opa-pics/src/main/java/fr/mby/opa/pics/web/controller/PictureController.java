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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IAlbumDao;
import fr.mby.opa.pics.service.IPictureDao;
import fr.mby.opa.pics.service.IPictureService;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping(PictureController.PICTURE_CONTROLLER_PATH)
public class PictureController {

	public static final String PICTURE_CONTROLLER_PATH = "picture";

	public static final String ROTATE_PICTURE_SUBPATH = "/rotate";

	@Autowired
	private IAlbumDao albumDao;

	@Autowired
	private IPictureDao pictureDao;

	@Autowired
	private IPictureService pictureService;

	@ResponseBody
	@RequestMapping(value = "{id}" + PictureController.ROTATE_PICTURE_SUBPATH + "/{angle}", method = RequestMethod.GET)
	public Picture rotatePictureJson(@PathVariable final Long id, @PathVariable final Integer angle,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		Assert.notNull(id, "Picture Id not supplied !");
		Assert.notNull(angle, "Rotating angle not supplied !");

		Picture rotatedPicture = null;

		final Integer normalizedAngle = ((angle / 90) % 4) * 90;
		if (normalizedAngle != 0) {
			rotatedPicture = this.pictureService.rotatePicture(id, normalizedAngle);
		}

		return rotatedPicture;
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

}