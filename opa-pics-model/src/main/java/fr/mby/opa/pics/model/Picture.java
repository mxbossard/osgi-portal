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

package fr.mby.opa.pics.model;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.joda.time.ReadableDateTime;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Entity
@Table(name = "picture")
public class Picture {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Basic(optional = false)
	@Column(name = "uniqueHash")
	private String uniqueHash;

	@Basic(optional = false)
	@Column(name = "filename")
	private String filename;

	@Basic(optional = false)
	@Column(name = "name")
	private String name;

	@Basic(optional = false)
	@Column(name = "creationTime", columnDefinition = "TIMESTAMP")
	@Convert(converter = JodaDateTimeConverter.class)
	private ReadableDateTime creationTime;

	@Lob
	@Basic(optional = false, fetch = FetchType.LAZY)
	@Column(name = "picture")
	private byte[] picture;

	@Lob
	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "thumbnail")
	private byte[] thumbnail;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tagsOnPicture")
	private Collection<Tag> tags;

	/**
	 * Getter of id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Setter of id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Getter of uniqueHash.
	 * 
	 * @return the uniqueHash
	 */
	public String getUniqueHash() {
		return this.uniqueHash;
	}

	/**
	 * Setter of uniqueHash.
	 * 
	 * @param uniqueHash
	 *            the uniqueHash to set
	 */
	public void setUniqueHash(final String uniqueHash) {
		this.uniqueHash = uniqueHash;
	}

	/**
	 * Getter of filename.
	 * 
	 * @return the filename
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Setter of filename.
	 * 
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(final String filename) {
		this.filename = filename;
	}

	/**
	 * Getter of name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter of name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter of creationTime.
	 * 
	 * @return the creationTime
	 */
	public ReadableDateTime getCreationTime() {
		return this.creationTime;
	}

	/**
	 * Setter of creationTime.
	 * 
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(final ReadableDateTime creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Getter of picture.
	 * 
	 * @return the picture
	 */
	public byte[] getPicture() {
		return this.picture;
	}

	/**
	 * Setter of picture.
	 * 
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(final byte[] picture) {
		this.picture = picture;
	}

	/**
	 * Getter of thumbnail.
	 * 
	 * @return the thumbnail
	 */
	public byte[] getThumbnail() {
		return this.thumbnail;
	}

	/**
	 * Setter of thumbnail.
	 * 
	 * @param thumbnail
	 *            the thumbnail to set
	 */
	public void setThumbnail(final byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * Getter of tags.
	 * 
	 * @return the tags
	 */
	public Collection<Tag> getTags() {
		return this.tags;
	}

	/**
	 * Setter of tags.
	 * 
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(final Collection<Tag> tags) {
		this.tags = tags;
	}

}
