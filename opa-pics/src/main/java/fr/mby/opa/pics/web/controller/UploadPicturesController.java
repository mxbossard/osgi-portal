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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import fr.mby.opa.pics.model.Picture;
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

	@Autowired
	private IPictureFactory pictureFactory;

	@Autowired
	private IPictureDao picsDao;

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
			for (final MultipartFile multipartFile : pictures) {
				final Picture picture = this.pictureFactory.build(multipartFile);
				if (picture != null) {
					try {
						this.picsDao.createPicture(picture);
						picturesNames.add(picture.getFilename());
					} catch (final PictureAlreadyExistsException e) {
						// Ignore picture
					}
				}
			}
		}

		map.addAttribute("picturesNames", picturesNames);
		return "file_upload_success";
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
		return this.picsDao;
	}

	/**
	 * Setter of picsDao.
	 * 
	 * @param picsDao
	 *            the picsDao to set
	 */
	public void setPicsDao(final IPictureDao picsDao) {
		this.picsDao = picsDao;
	}

}
