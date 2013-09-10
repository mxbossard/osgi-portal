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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * An Image represent the Picture binary contents.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Entity
@Table(name = "BINARY_IMAGE")
public class BinaryImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Basic(optional = false)
	@Column(name = "FILENAME", nullable = false, updatable = false)
	private String filename;

	@Basic(optional = false)
	@Column(name = "WIDTH", nullable = false, updatable = false)
	private Integer width;

	@Basic(optional = false)
	@Column(name = "HEIGHT", nullable = false, updatable = false)
	private Integer height;

	@Basic(optional = false)
	@Column(name = "FORMAT", nullable = false, updatable = false)
	private String format;

	@Lob
	@Basic(optional = false)
	@Column(name = "DATA")
	private byte[] data;

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
	 * Getter of data.
	 * 
	 * @return the data
	 */
	public byte[] getData() {
		return this.data;
	}

	/**
	 * Setter of data.
	 * 
	 * @param data
	 *            the data to set
	 */
	public void setData(final byte[] data) {
		this.data = data;
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

}
