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

import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.DateTime;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@NamedQueries({
		@NamedQuery(name = Picture.FIND_PIC_BY_ID, query = "SELECT record"
				+ " FROM Picture record WHERE record.id = :id"),
		@NamedQuery(name = Picture.FIND_PIC_ID_BY_HASH, query = "SELECT record.id"
				+ " FROM Picture record WHERE record.uniqueHash = :uniqueHash"),
		@NamedQuery(name = Picture.FIND_ALL_ORDER_BY_DATE, query = "SELECT record"
				+ " FROM Picture record ORDER BY record.creationTime ASC")})
@Entity
@Converter(name = "jodaDateTime", converterClass = JodaDateTimeConverter.class)
@Table(name = "picture", uniqueConstraints = @UniqueConstraint(columnNames = {"uniqueHash"}))
// indexes = {@Index(columnList = "id"), @Index(columnList = "uniqueHash"), @Index(columnList = "creationTime")}
public class Picture {

	/** Find a Picture by Id. Params: id */
	public static final String FIND_PIC_BY_ID = "FIND_PIC_BY_ID";

	/** Find a Picture by Hash. Params: uniqueHash */
	public static final String FIND_PIC_ID_BY_HASH = "FIND_PIC_ID_BY_HASH";

	/** Find a Picture by Hash. Params: uniqueHash */
	public static final String FIND_ALL_ORDER_BY_DATE = "FIND_ALL_ORDER_BY_DATE";

	@Id
	@Column(name = "id")
	private long id;

	@Basic(optional = false)
	@Column(name = "uniqueHash", nullable = false, updatable = false, unique = true)
	private String uniqueHash;

	@Basic(optional = false)
	@Column(name = "filename", nullable = false, updatable = false)
	private String filename;

	@Basic(optional = false)
	@Column(name = "name")
	private String name;

	@Basic(optional = false)
	@Column(name = "creationTime", nullable = false, updatable = false)
	@Convert("jodaDateTime")
	private DateTime creationTime;

	@Basic(optional = false)
	@Column(name = "width", nullable = false, updatable = false)
	private int width;

	@Basic(optional = false)
	@Column(name = "heigth", nullable = false, updatable = false)
	private int heigth;

	@Basic(optional = false)
	@Column(name = "format", nullable = false, updatable = false)
	private String format;

	@Lob
	@Basic(optional = false)
	@Column(name = "thumbnail", nullable = false, updatable = false)
	private byte[] thumbnail;

	@Basic(optional = false)
	@Column(name = "thumbnailWidth", nullable = false, updatable = false)
	private int thumbnailWidth;

	@Basic(optional = false)
	@Column(name = "thumbnailHeigth", nullable = false, updatable = false)
	private int thumbnailHeigth;

	@Basic(optional = false)
	@Column(name = "thumbnailFormat", nullable = false, updatable = false)
	private String thumbnailFormat;

	@Basic(optional = true)
	@Column(name = "jsonMetadata", length = 8192)
	private String jsonMetadata;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tagsOnPicture")
	@MapKey(name = "id")
	@JoinColumn(name = "tags")
	private Map<Long, Tag> tags;

	@MapsId
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false, mappedBy = "picture")
	@JoinColumn(name = "id", nullable = false, updatable = false)
	private PictureContents contents;

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
	public DateTime getCreationTime() {
		return this.creationTime;
	}

	/**
	 * Setter of creationTime.
	 * 
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(final DateTime creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Getter of contents.
	 * 
	 * @return the contents
	 */
	public PictureContents getContents() {
		return this.contents;
	}

	/**
	 * Setter of contents.
	 * 
	 * @param contents
	 *            the contents to set
	 */
	public void setContents(final PictureContents contents) {
		this.contents = contents;
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
	public Map<Long, Tag> getTags() {
		return this.tags;
	}

	/**
	 * Setter of tags.
	 * 
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(final Map<Long, Tag> tags) {
		this.tags = tags;
	}

	/**
	 * Getter of jsonMetadata.
	 * 
	 * @return the jsonMetadata
	 */
	public String getJsonMetadata() {
		return this.jsonMetadata;
	}

	/**
	 * Setter of jsonMetadata.
	 * 
	 * @param jsonMetadata
	 *            the jsonMetadata to set
	 */
	public void setJsonMetadata(final String jsonMetadata) {
		this.jsonMetadata = jsonMetadata;
	}

	/**
	 * Getter of width.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Setter of width.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * Getter of heigth.
	 * 
	 * @return the heigth
	 */
	public int getHeigth() {
		return this.heigth;
	}

	/**
	 * Setter of heigth.
	 * 
	 * @param heigth
	 *            the heigth to set
	 */
	public void setHeigth(final int heigth) {
		this.heigth = heigth;
	}

	/**
	 * Getter of thumbnailWidth.
	 * 
	 * @return the thumbnailWidth
	 */
	public int getThumbnailWidth() {
		return this.thumbnailWidth;
	}

	/**
	 * Setter of thumbnailWidth.
	 * 
	 * @param thumbnailWidth
	 *            the thumbnailWidth to set
	 */
	public void setThumbnailWidth(final int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	/**
	 * Getter of thumbnailHeigth.
	 * 
	 * @return the thumbnailHeigth
	 */
	public int getThumbnailHeigth() {
		return this.thumbnailHeigth;
	}

	/**
	 * Setter of thumbnailHeigth.
	 * 
	 * @param thumbnailHeigth
	 *            the thumbnailHeigth to set
	 */
	public void setThumbnailHeigth(final int thumbnailHeigth) {
		this.thumbnailHeigth = thumbnailHeigth;
	}

	/**
	 * Getter of format.
	 * 
	 * @return the format
	 */
	public String getFormat() {
		return this.format;
	}

	/**
	 * Setter of format.
	 * 
	 * @param format
	 *            the format to set
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

	/**
	 * Getter of thumbnailFormat.
	 * 
	 * @return the thumbnailFormat
	 */
	public String getThumbnailFormat() {
		return this.thumbnailFormat;
	}

	/**
	 * Setter of thumbnailFormat.
	 * 
	 * @param thumbnailFormat
	 *            the thumbnailFormat to set
	 */
	public void setThumbnailFormat(final String thumbnailFormat) {
		this.thumbnailFormat = thumbnailFormat;
	}

}
