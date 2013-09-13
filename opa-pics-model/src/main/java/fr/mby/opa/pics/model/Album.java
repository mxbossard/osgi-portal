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

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.ReadableDateTime;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@NamedQueries({
		@NamedQuery(name = Album.LOAD_FULL_ALBUM_BY_ID, query = "SELECT a FROM Album a "
				+ "JOIN FETCH a.selectedOrderingProposal JOIN FETCH a.pictures WHERE a.id = :id"),
		@NamedQuery(name = Album.FIND_ALL_ALBUMS_ORDER_BY_DATE, query = "SELECT a"
				+ " FROM Album a ORDER BY a.creationTime ASC")})
@Entity
@Converter(name = "jodaDateTime", converterClass = JodaDateTimeConverter.class)
@Table(name = "ALBUM")
public class Album {

	/** Load an Album and its surronding objects by Id. Params: id */
	public static final String LOAD_FULL_ALBUM_BY_ID = "LOAD_FULL_ALBUM_BY_ID";

	/** Find an Album by Id ordered by Date. Params: id */
	public static final String FIND_ALL_ALBUMS_ORDER_BY_DATE = "FIND_ALL_ALBUMS_ORDER_BY_DATE";

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic(optional = false)
	@Column(name = "NAME")
	private String name;

	@Basic
	@Column(name = "DESCRIPTION")
	private String description;

	@Basic(optional = false)
	@Column(name = "CREATION_TIME", columnDefinition = "TIMESTAMP")
	@Convert("jodaDateTime")
	private ReadableDateTime creationTime;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SELECTED_ORDERING_PROPOSAL_ID")
	private ProposalBag selectedOrderingProposal;

	@OneToMany(fetch = FetchType.LAZY)
	private List<ProposalBag> submitedOrderingProposals;

	@OneToMany(fetch = FetchType.LAZY)
	private List<ProposalBag> validatedOrderingProposals;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "album")
	private List<Picture> pictures;

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
	 * Getter of description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Setter of description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
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
	 * Getter of selectedOrderingProposal.
	 * 
	 * @return the selectedOrderingProposal
	 */
	public ProposalBag getSelectedOrderingProposal() {
		return this.selectedOrderingProposal;
	}

	/**
	 * Setter of selectedOrderingProposal.
	 * 
	 * @param selectedOrderingProposal
	 *            the selectedOrderingProposal to set
	 */
	public void setSelectedOrderingProposal(final ProposalBag selectedOrderingProposal) {
		this.selectedOrderingProposal = selectedOrderingProposal;
	}

	/**
	 * Getter of submitedOrderingProposals.
	 * 
	 * @return the submitedOrderingProposals
	 */
	public List<ProposalBag> getSubmitedOrderingProposals() {
		return this.submitedOrderingProposals;
	}

	/**
	 * Setter of submitedOrderingProposals.
	 * 
	 * @param submitedOrderingProposals
	 *            the submitedOrderingProposals to set
	 */
	public void setSubmitedOrderingProposals(final List<ProposalBag> submitedOrderingProposals) {
		this.submitedOrderingProposals = submitedOrderingProposals;
	}

	/**
	 * Getter of validatedOrderingProposals.
	 * 
	 * @return the validatedOrderingProposals
	 */
	public List<ProposalBag> getValidatedOrderingProposals() {
		return this.validatedOrderingProposals;
	}

	/**
	 * Setter of validatedOrderingProposals.
	 * 
	 * @param validatedOrderingProposals
	 *            the validatedOrderingProposals to set
	 */
	public void setValidatedOrderingProposals(final List<ProposalBag> validatedOrderingProposals) {
		this.validatedOrderingProposals = validatedOrderingProposals;
	}

	/**
	 * Getter of pictures.
	 * 
	 * @return the pictures
	 */
	public List<Picture> getPictures() {
		return this.pictures;
	}

	/**
	 * Setter of pictures.
	 * 
	 * @param pictures
	 *            the pictures to set
	 */
	public void setPictures(final List<Picture> pictures) {
		this.pictures = pictures;
	}

}
