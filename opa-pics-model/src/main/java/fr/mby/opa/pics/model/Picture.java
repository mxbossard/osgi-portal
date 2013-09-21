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

import java.sql.Timestamp;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.Converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.mby.opa.pics.model.converter.JodaDateTimeConverter;
import fr.mby.opa.pics.model.converter.TimestampJsonSerializer;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@NamedQueries({
		@NamedQuery(name = Picture.LOAD_FULL_PICTURE_BY_ID, query = "SELECT p"
				+ " FROM Picture p JOIN FETCH p.image JOIN FETCH p.thumbnail WHERE p.id = :id"),
		@NamedQuery(name = Picture.FIND_PICTURE_ID_BY_HASH, query = "SELECT p.id"
				+ " FROM Picture p WHERE p.hash = :hash"),
		@NamedQuery(name = Picture.FIND_PICTURE_BY_ALBUM_ORDER_BY_DATE, query = "SELECT new fr.mby.opa.pics.model.Picture"
				+ "(p.id, p.filename, p.name, p.originalTime, p.creationTime, p.width, p.height, p.format, p.thumbnailWidth,"
				+ " p.thumbnailHeight, p.jsonMetadata, p.imageId, p.thumbnailId)"
				+ " FROM Picture p JOIN FETCH p.tags WHERE p.album.id = :albumId AND p.originalTime > :since"
				+ " ORDER BY p.originalTime"),
		@NamedQuery(name = Picture.FIND_ALL_PICTURES_ORDER_BY_DATE, query = "SELECT p"
				+ " FROM Picture p WHERE p.originalTime > :since ORDER BY p.originalTime ASC")})
@Entity
@Converter(name = "jodaDateTime", converterClass = JodaDateTimeConverter.class)
@Table(name = "PICTURE", uniqueConstraints = @UniqueConstraint(columnNames = {"hash"}))
// indexes = {@Index(columnList = "id"), @Index(columnList = "uniqueHash"), @Index(columnList = "creationTime")}
@JsonInclude(Include.NON_NULL)
public class Picture {

	/** Load a Picture withs its contents by Id. Params: id */
	public static final String LOAD_FULL_PICTURE_BY_ID = "LOAD_FULL_PICTURE_BY_ID";

	/** Find a Picture by Hash. Params: hash */
	public static final String FIND_PICTURE_ID_BY_HASH = "FIND_PICTURE_ID_BY_HASH";

	/** Find all Pictures of an Album. Params: albumId, since */
	public static final String FIND_PICTURE_BY_ALBUM_ORDER_BY_DATE = "FIND_PICTURE_BY_ALBUM_ORDER_BY_DATE";

	/** Find all Pictures order by original time. Params: since */
	public static final String FIND_ALL_PICTURES_ORDER_BY_DATE = "FIND_ALL_PICTURES_ORDER_BY_DATE";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Basic(optional = false)
	@Column(name = "HASH", nullable = false, updatable = false, unique = true)
	private String hash;

	@Basic(optional = false)
	@Column(name = "FILENAME", nullable = false, updatable = false)
	private String filename;

	@Basic(optional = false)
	@Column(name = "NAME")
	private String name;

	@Basic(optional = false)
	@Column(name = "ORIGINAL_TIME", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
	// @Convert("jodaDateTime")
	// @JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	@JsonSerialize(using = TimestampJsonSerializer.class)
	private Timestamp originalTime;

	@Basic(optional = false)
	@Column(name = "CREATION_TIME", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
	// @Convert("jodaDateTime")
	// @JsonSerialize(using = JodaDateTimeJsonSerializer.class)
	@JsonSerialize(using = TimestampJsonSerializer.class)
	private Timestamp creationTime;

	@Basic(optional = false)
	@Column(name = "WIDTH", nullable = false, updatable = false)
	private Integer width;

	@Basic(optional = false)
	@Column(name = "HEIGHT", nullable = false, updatable = false)
	private Integer height;

	@Basic(optional = false)
	@Column(name = "SIZE", nullable = false, updatable = false)
	private Integer size;

	@Basic(optional = false)
	@Column(name = "FORMAT", nullable = false, updatable = false)
	private String format;

	@Basic(optional = false)
	@Column(name = "THUMBNAIL_WIDTH", nullable = false, updatable = true)
	private Integer thumbnailWidth;

	@Basic(optional = false)
	@Column(name = "THUMBNAIL_HEIGHT", nullable = false, updatable = true)
	private Integer thumbnailHeight;

	@Basic(optional = false)
	@Column(name = "THUMBNAIL_SIZE", nullable = false, updatable = true)
	private Integer thumbnailSize;

	@Basic(optional = false)
	@Column(name = "THUMBNAIL_FORMAT", nullable = false, updatable = true)
	private String thumbnailFormat;

	@Basic(optional = true)
	@Column(name = "JSON_METADATA", length = 8192)
	@JsonRawValue
	private String jsonMetadata;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tagsOnPicture")
	@MapKey(name = "id")
	@JoinColumn(name = "TAGS_ID")
	private Map<Long, Tag> tags;

	@Basic(optional = false)
	@Column(name = "IMAGE_ID", nullable = false, insertable = false, updatable = false)
	private Long imageId;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "IMAGE_ID", nullable = false, updatable = false)
	private BinaryImage image;

	@Basic(optional = false)
	@Column(name = "THUMBNAIL_ID", nullable = false, insertable = false, updatable = false)
	private Long thumbnailId;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "THUMBNAIL_ID", nullable = false, updatable = false)
	private BinaryImage thumbnail;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ALBUM_ID", nullable = false, updatable = false)
	private Album album;

	@Version
	private Long version;

	/** */
	public Picture() {
		super();
	}

	/**
	 * @param id
	 * @param filename
	 * @param name
	 * @param originalTime
	 * @param creationTime
	 * @param width
	 * @param height
	 * @param format
	 * @param thumbnailWidth
	 * @param thumbnailHeigth
	 * @param jsonMetadata
	 * @param thumbnailId
	 */
	public Picture(final Long id, final String filename, final String name, final Timestamp originalTime,
			final Timestamp creationTime, final Integer width, final Integer height, final String format,
			final Integer thumbnailWidth, final Integer thumbnailHeight, final String jsonMetadata, final Long imageId,
			final Long thumbnailId) {
		super();
		this.id = id;
		this.filename = filename;
		this.name = name;
		this.originalTime = originalTime;
		this.creationTime = creationTime;
		this.width = width;
		this.height = height;
		this.format = format;
		this.thumbnailWidth = thumbnailWidth;
		this.thumbnailHeight = thumbnailHeight;
		this.jsonMetadata = jsonMetadata;
		this.imageId = imageId;
		this.thumbnailId = thumbnailId;
	}

	/**
	 * Getter of id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Setter of id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Getter of uniqueHash.
	 * 
	 * @return the uniqueHash
	 */
	public String getHash() {
		return this.hash;
	}

	/**
	 * Setter of uniqueHash.
	 * 
	 * @param hash
	 *            the uniqueHash to set
	 */
	public void setHash(final String hash) {
		this.hash = hash;
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
	 * Getter of originalTime.
	 * 
	 * @return the originalTime
	 */
	public Timestamp getOriginalTime() {
		return this.originalTime;
	}

	/**
	 * Setter of originalTime.
	 * 
	 * @param originalTime
	 *            the originalTime to set
	 */
	public void setOriginalTime(final Timestamp originalTime) {
		this.originalTime = originalTime;
	}

	/**
	 * Getter of creationTime.
	 * 
	 * @return the creationTime
	 */
	public Timestamp getCreationTime() {
		return this.creationTime;
	}

	/**
	 * Setter of creationTime.
	 * 
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(final Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Getter of imageId.
	 * 
	 * @return the imageId
	 */
	public Long getImageId() {
		return this.imageId;
	}

	/**
	 * Getter of image.
	 * 
	 * @return the image
	 */
	public BinaryImage getImage() {
		return this.image;
	}

	/**
	 * Setter of image.
	 * 
	 * @param image
	 *            the image to set
	 */
	public void setImage(final BinaryImage image) {
		this.image = image;
	}

	/**
	 * Getter of thumbnailId.
	 * 
	 * @return the thumbnailId
	 */
	public Long getThumbnailId() {
		return this.thumbnailId;
	}

	/**
	 * Getter of thumbnail.
	 * 
	 * @return the thumbnail
	 */
	public BinaryImage getThumbnail() {
		return this.thumbnail;
	}

	/**
	 * Setter of thumbnail.
	 * 
	 * @param thumbnail
	 *            the thumbnail to set
	 */
	public void setThumbnail(final BinaryImage thumbnail) {
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
	public Integer getWidth() {
		return this.width;
	}

	/**
	 * Setter of width.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(final Integer width) {
		this.width = width;
	}

	/**
	 * Getter of height.
	 * 
	 * @return the height
	 */
	public Integer getHeight() {
		return this.height;
	}

	/**
	 * Setter of height.
	 * 
	 * @param height
	 *            the height to set
	 */
	public void setHeight(final Integer height) {
		this.height = height;
	}

	/**
	 * Getter of size.
	 * 
	 * @return the size
	 */
	public Integer getSize() {
		return this.size;
	}

	/**
	 * Setter of size.
	 * 
	 * @param size
	 *            the size to set
	 */
	public void setSize(final Integer size) {
		this.size = size;
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
	 * Getter of thumbnailWidth.
	 * 
	 * @return the thumbnailWidth
	 */
	public Integer getThumbnailWidth() {
		return this.thumbnailWidth;
	}

	/**
	 * Setter of thumbnailWidth.
	 * 
	 * @param thumbnailWidth
	 *            the thumbnailWidth to set
	 */
	public void setThumbnailWidth(final Integer thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	/**
	 * Getter of thumbnailHeigth.
	 * 
	 * @return the thumbnailHeigth
	 */
	public Integer getThumbnailHeight() {
		return this.thumbnailHeight;
	}

	/**
	 * Setter of thumbnailHeight.
	 * 
	 * @param thumbnailHeight
	 *            the thumbnailHeight to set
	 */
	public void setThumbnailHeight(final Integer thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

	/**
	 * Getter of thumbnailSize.
	 * 
	 * @return the thumbnailSize
	 */
	public Integer getThumbnailSize() {
		return this.thumbnailSize;
	}

	/**
	 * Setter of thumbnailSize.
	 * 
	 * @param thumbnailSize
	 *            the thumbnailSize to set
	 */
	public void setThumbnailSize(final Integer thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
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

	/**
	 * Getter of album.
	 * 
	 * @return the album
	 */
	public Album getAlbum() {
		return this.album;
	}

	/**
	 * Setter of album.
	 * 
	 * @param album
	 *            the album to set
	 */
	public void setAlbum(final Album album) {
		this.album = album;
	}

}
