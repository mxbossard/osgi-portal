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

package fr.mby.opa.pics.web.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class PicturesUploadForm {

	private List<MultipartFile> pictures;

	/**
	 * Getter of pictures.
	 * 
	 * @return the pictures
	 */
	public List<MultipartFile> getPictures() {
		return this.pictures;
	}

	/**
	 * Setter of pictures.
	 * 
	 * @param pictures
	 *            the pictures to set
	 */
	public void setPictures(final List<MultipartFile> pictures) {
		this.pictures = pictures;
	}

}
