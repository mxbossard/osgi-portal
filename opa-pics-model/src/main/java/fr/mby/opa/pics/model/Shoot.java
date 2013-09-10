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
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.ReadableDateTime;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Entity
@Converter(name = "jodaDateTime", converterClass = JodaDateTimeConverter.class)
@Table(name = "SHOOT")
public class Shoot {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Basic(optional = false)
	@Column(name = "START_TIME", columnDefinition = "TIMESTAMP")
	@Convert("jodaDateTime")
	private ReadableDateTime startTime;

	@Basic(optional = false)
	@Column(name = "END_TIME", columnDefinition = "TIMESTAMP")
	@Convert("jodaDateTime")
	private ReadableDateTime endTime;

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
	 * Getter of startTime.
	 * 
	 * @return the startTime
	 */
	public ReadableDateTime getStartTime() {
		return this.startTime;
	}

	/**
	 * Setter of startTime.
	 * 
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(final ReadableDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Getter of endTime.
	 * 
	 * @return the endTime
	 */
	public ReadableDateTime getEndTime() {
		return this.endTime;
	}

	/**
	 * Setter of endTime.
	 * 
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(final ReadableDateTime endTime) {
		this.endTime = endTime;
	}

}