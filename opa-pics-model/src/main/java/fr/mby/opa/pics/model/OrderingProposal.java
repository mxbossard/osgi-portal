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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Entity
@Converter(name = "jodaDateTime", converterClass = JodaDateTimeConverter.class)
@Table(name = "ORDERING_PROPOSAL")
public class OrderingProposal {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic(optional = true)
	@Column(name = "NAME")
	private String name;

	@Basic(optional = true)
	@Column(name = "DESCRIPTION")
	private String description;

	@Basic(optional = false)
	@Column(name = "LOCKED")
	private Boolean locked;

	@Basic(optional = false)
	@Column(name = "CREATION_TIME")
	@Convert("jodaDateTime")
	private ReadableDateTime creationTime;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BASE_PROPOSAL_ID", updatable = false)
	private OrderingProposal baseProposal;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderingProposal")
	private Collection<UnitProposal> unitProposals;

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
	 * Getter of baseProposal.
	 * 
	 * @return the baseProposal
	 */
	public OrderingProposal getBaseProposal() {
		return this.baseProposal;
	}

	/**
	 * Setter of baseProposal.
	 * 
	 * @param baseProposal
	 *            the baseProposal to set
	 */
	public void setBaseProposal(final OrderingProposal baseProposal) {
		this.baseProposal = baseProposal;
	}

	/**
	 * Getter of unitProposals.
	 * 
	 * @return the unitProposals
	 */
	public Collection<UnitProposal> getUnitProposals() {
		return this.unitProposals;
	}

	/**
	 * Setter of unitProposals.
	 * 
	 * @param unitProposals
	 *            the unitProposals to set
	 */
	public void setUnitProposals(final Collection<UnitProposal> unitProposals) {
		this.unitProposals = unitProposals;
	}

}
