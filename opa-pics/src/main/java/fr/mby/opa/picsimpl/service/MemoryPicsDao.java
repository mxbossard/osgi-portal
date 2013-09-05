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

package fr.mby.opa.picsimpl.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IPicsDao;
import fr.mby.opa.picsimpl.model.ImmutablePicture;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class MemoryPicsDao implements IPicsDao {

	private final Map<Long, Picture> store = new HashMap<Long, Picture>(8);

	@Override
	public void savePicture(final Picture picture) {
		picture.setId(this.store.size());
		this.store.put(picture.getId(), picture);
	}

	@Override
	public Picture findPictureById(final long id) {
		return new ImmutablePicture(this.store.get(id));
	}

	@Override
	public Collection<Picture> findAllPictures() {
		return Collections.unmodifiableCollection(this.store.values());
	}

}
