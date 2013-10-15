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

package fr.mby.opa.pics.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import fr.mby.opa.pics.exception.PictureAlreadyExistsException;
import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IPictureService {

	Picture createPicture(String filename, byte[] contents) throws PictureAlreadyExistsException, IOException,
			UnsupportedPictureTypeException;

	BinaryImage generateThumbnail(Picture picture, int width, int height, boolean keepScale, String format)
			throws IOException;

	Picture rotatePicture(Long pictureId, Integer angle) throws IOException;

	/**
	 * Generate Hash String repesentation of a file contents.
	 * 
	 * @param contents
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	String generateHash(byte[] contents);
}
