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

package fr.mby.opa.picsimpl.model;

import java.util.Collection;

import org.joda.time.ReadableDateTime;

import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.model.Tag;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class ImmutablePicture extends Picture {

	private final Picture picture;

	public ImmutablePicture(final Picture pic) {
		super();

		this.picture = pic;
	}

	@Override
	public long getId() {
		return this.picture.getId();
	}

	@Override
	public String getUniqueHash() {
		return this.picture.getUniqueHash();
	}

	@Override
	public String getFilename() {
		return this.picture.getFilename();
	}

	@Override
	public String getName() {
		return this.picture.getName();
	}

	@Override
	public ReadableDateTime getCreationTime() {
		return this.picture.getCreationTime();
	}

	@Override
	public byte[] getPicture() {
		return this.picture.getPicture();
	}

	@Override
	public byte[] getThumbnail() {
		return this.picture.getThumbnail();
	}

	@Override
	public Collection<Tag> getTags() {
		return this.picture.getTags();
	}

	@Override
	public void setId(final long id) {
		// Do nothing
		throw new IllegalAccessError("This picture is immutable !");
	}

	@Override
	public void setUniqueHash(final String uniqueHash) {
		// Do nothing
		throw new IllegalAccessError("This picture is immutable !");
	}

	@Override
	public void setFilename(final String filename) {
		// Do nothing
		throw new IllegalAccessError("This picture is immutable !");
	}

	@Override
	public void setName(final String name) {
		// Do nothing
		throw new IllegalAccessError("This picture is immutable !");
	}

	@Override
	public void setCreationTime(final ReadableDateTime creationTime) {
		// Do nothing
		throw new IllegalAccessError("This picture is immutable !");
	}

	@Override
	public void setPicture(final byte[] picture) {
		// Do nothing
		throw new IllegalAccessError("This picture is immutable !");
	}

	@Override
	public void setThumbnail(final byte[] thumbnail) {
		// Do nothing
		throw new IllegalAccessError("This picture is immutable !");
	}

	@Override
	public void setTags(final Collection<Tag> tags) {
		// Do nothing
		throw new IllegalAccessError("This picture is immutable !");
	}

}
