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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "UNIT_PROPOSAL")
public class UnitProposal {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic(optional = false)
	@Column(name = "RANK")
	private Integer rank;

	@Basic(optional = false)
	@Column(name = "VALID")
	private Boolean valid;

	@Basic(optional = false)
	@Column(name = "LOCKED")
	private Boolean locked;

	@Basic(optional = false)
	@Column(name = "CREATION_TIME")
	@Convert("jodaDateTime")
	private ReadableDateTime creationTime;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ORDERING_PROPOSAL_ID", nullable = false, updatable = false)
	private OrderingProposal orderingProposal;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SESSION_ID", nullable = false)
	private Session session;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "SHOOT_ID", nullable = true)
	private Shoot shoot;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "PICTURE_ID", nullable = false)
	private Picture picture;

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
	 * Getter of rank.
	 * 
	 * @return the rank
	 */
	public Integer getRank() {
		return this.rank;
	}

	/**
	 * Setter of rank.
	 * 
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(final Integer rank) {
		this.rank = rank;
	}

	/**
	 * Getter of valid.
	 * 
	 * @return the valid
	 */
	public Boolean getValid() {
		return this.valid;
	}

	/**
	 * Setter of valid.
	 * 
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(final Boolean valid) {
		this.valid = valid;
	}

	/**
	 * Getter of locked.
	 * 
	 * @return the locked
	 */
	public Boolean getLocked() {
		return this.locked;
	}

	/**
	 * Setter of locked.
	 * 
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(final Boolean locked) {
		this.locked = locked;
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
	 * Getter of orderingProposal.
	 * 
	 * @return the orderingProposal
	 */
	public OrderingProposal getOrderingProposal() {
		return this.orderingProposal;
	}

	/**
	 * Setter of orderingProposal.
	 * 
	 * @param orderingProposal
	 *            the orderingProposal to set
	 */
	public void setOrderingProposal(final OrderingProposal orderingProposal) {
		this.orderingProposal = orderingProposal;
	}

	/**
	 * Getter of picture.
	 * 
	 * @return the picture
	 */
	public Picture getPicture() {
		return this.picture;
	}

	/**
	 * Setter of picture.
	 * 
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(final Picture picture) {
		this.picture = picture;
	}

	/**
	 * Getter of shoot.
	 * 
	 * @return the shoot
	 */
	public Shoot getShoot() {
		return this.shoot;
	}

	/**
	 * Setter of shoot.
	 * 
	 * @param shoot
	 *            the shoot to set
	 */
	public void setShoot(final Shoot shoot) {
		this.shoot = shoot;
	}

	/**
	 * Getter of session.
	 * 
	 * @return the session
	 */
	public Session getSession() {
		return this.session;
	}

	/**
	 * Setter of session.
	 * 
	 * @param session
	 *            the session to set
	 */
	public void setSession(final Session session) {
		this.session = session;
	}

}
