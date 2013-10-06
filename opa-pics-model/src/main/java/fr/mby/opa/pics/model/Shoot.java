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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.mby.opa.pics.model.converter.TimestampJsonSerializer;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Entity
@Table(name = "SHOOT")
@JsonInclude(Include.NON_NULL)
public class Shoot {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic(optional = false)
	@Column(name = "CREATION_TIME", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
	@JsonSerialize(using = TimestampJsonSerializer.class)
	private Timestamp creationTime;

	@Basic(optional = false)
	@Column(name = "START_TIME", columnDefinition = "TIMESTAMP", nullable = false)
	@JsonSerialize(using = TimestampJsonSerializer.class)
	private Timestamp startTime;

	@Basic(optional = false)
	@Column(name = "END_TIME", columnDefinition = "TIMESTAMP", nullable = false)
	@JsonSerialize(using = TimestampJsonSerializer.class)
	private Timestamp endTime;

	@Version
	@JsonIgnore
	private Long version;

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
	 * Getter of startTime.
	 * 
	 * @return the startTime
	 */
	public Timestamp getStartTime() {
		return this.startTime;
	}

	/**
	 * Setter of startTime.
	 * 
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(final Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * Getter of endTime.
	 * 
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return this.endTime;
	}

	/**
	 * Setter of endTime.
	 * 
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(final Timestamp endTime) {
		this.endTime = endTime;
	}

}
