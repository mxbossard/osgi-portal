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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.service.IPictureDao;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping(ImageController.IMAGE_CONTROLLER_PATH)
public class ImageController {

	public static final String IMAGE_CONTROLLER_PATH = "/image";

	@Autowired
	private IPictureDao pictureDao;

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
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