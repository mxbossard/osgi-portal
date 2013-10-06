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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.mby.opa.pics.model.converter.TimestampJsonSerializer;

/**
 * A CasingProposal case a Picture in a Session. Multiple CasingProposal are grouped in a ProposalBag.
 * 
 * Unique constraints :
 * <ul>
 * <li>(ProposalBag, Picture) : cannot case multiple time the same in Picture in a ProposalBag.</li>
 * </ul>
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Entity
@Table(name = "CASING_PROPOSAL", uniqueConstraints = {@UniqueConstraint(columnNames = "PROPOSAL_BAG_ID,PICTURE_ID")})
@JsonInclude(Include.NON_NULL)
public class CasingProposal {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic(optional = false)
	@Column(name = "CREATION_TIME", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
	@JsonSerialize(using = TimestampJsonSerializer.class)
	private Timestamp creationTime;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "PROPOSAL_BAG_ID", nullable = false, updatable = false)
	private ProposalBag proposalBag;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SESSION_ID", nullable = false)
	private Session session;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "PICTURE_ID", nullable = false)
	private Picture picture;

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
	 * Getter of proposalBag.
	 * 
	 * @return the proposalBag
	 */
	public ProposalBag getProposalBag() {
		return this.proposalBag;
	}

	/**
	 * Setter of proposalBag.
	 * 
	 * @param proposalBag
	 *            the proposalBag to set
	 */
	public void setProposalBag(final ProposalBag proposalBag) {
		this.proposalBag = proposalBag;
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
