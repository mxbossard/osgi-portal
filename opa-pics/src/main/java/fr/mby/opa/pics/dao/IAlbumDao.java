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

package fr.mby.opa.pics.dao;

import java.util.Collection;

import fr.mby.opa.pics.exception.AlbumNotFoundException;
import fr.mby.opa.pics.model.Album;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IAlbumDao {

	Album createAlbum(Album album);

	Album updateAlbum(Album album) throws AlbumNotFoundException;

	void deleteAlbum(Album album) throws AlbumNotFoundException;

	Album findAlbumById(Long id);

	Album loadAlbumById(Long id);

	Collection<Album> findAllAlbums();

}
