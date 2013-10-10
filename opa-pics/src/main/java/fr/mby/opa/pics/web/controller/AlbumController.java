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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IAlbumDao;
import fr.mby.opa.pics.service.IPictureDao;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping(AlbumController.ALBUM_CONTROLLER_PATH)
public class AlbumController {

	public static final String ALBUM_CONTROLLER_PATH = "album";

	@Autowired
	private IAlbumDao albumDao;

	@Autowired
	private IPictureDao pictureDao;

	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET)
	public Collection<Album> findAllAlbumsJson(final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {
		final Collection<Album> albums = this.albumDao.findAllAlbums();

		return albums;
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public Album createAlbumJson(@Valid @RequestBody final Album album) throws Exception {
		Assert.notNull(album, "No Album supplied !");

		album.setCreationTime(new Timestamp(System.currentTimeMillis()));
		album.setLocked(false);

		this.albumDao.createAlbum(album);

		return album;
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(method = RequestMethod.PUT)
	public Album updateAlbumJson(@Valid @RequestBody final Album album) throws Exception {

		final Album updatedAlbum = this.albumDao.updateAlbum(album);

		return updatedAlbum;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "{albumId}", method = RequestMethod.DELETE)
	public void removeAlbum(@PathVariable final Long albumId) throws Exception {
		Assert.notNull(albumId, "No Album Id supplied !");

		final Album album = this.albumDao.findAlbumById(albumId);
		Assert.notNull(album, "No Album found !");

		this.albumDao.deleteAlbum(album);
	}

	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "{albumId}/pictures", method = RequestMethod.GET)
	public List<Picture> findAlbumPicturesJson(@PathVariable final Long albumId,
			@RequestParam(value = "since", required = false) final Long since, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		final List<Picture> pictures = this.pictureDao.findPicturesByAlbumId(albumId, since);

		return pictures;
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleMethodArgumentNotValidException(final MethodArgumentNotValidException error) {
		return "Bad request: " + error.getMessage();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleIllegalArgumentException(final IllegalArgumentException e) {
		return "Error while validating arguments !";
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(final Exception e) {
		return "return error object instead";
	}

}