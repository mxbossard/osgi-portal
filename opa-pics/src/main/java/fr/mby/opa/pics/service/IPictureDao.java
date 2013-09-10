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

import java.util.Collection;

import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IPictureDao {

	Picture createPicture(Picture picture) throws PictureAlreadyExistsException;

	Picture updatePicture(Picture picture) throws PictureNotFoundException;

	void deletePicture(Picture picture) throws PictureNotFoundException;

	Picture findPictureById(Long id);

	Picture loadFullPictureById(Long id);

	BinaryImage findImageById(Long id);

	Collection<Picture> findAllPictures();

}