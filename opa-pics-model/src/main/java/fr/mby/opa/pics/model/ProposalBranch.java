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
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fr.mby.opa.pics.model.converter.TimestampJsonSerializer;

/**
 * Proposal to Order an Album.
 * 
 * Contains some Set of unit Proposals of different types.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@NamedQueries({
		@NamedQuery(name = ProposalBranch.FIND_BRANCH_BY_NAME, query = "SELECT br"
				+ " FROM ProposalBranch br WHERE br.album.id = :albumId AND br.name = :name"),
		@NamedQuery(name = ProposalBranch.FIND_BRANCHES_OF_ALBUM, query = "SELECT br"
				+ " FROM ProposalBranch br WHERE br.album.id = :albumId ORDER BY br.creationTime DESC"),
		@NamedQuery(name = ProposalBranch.LOAD_BRANCH_UNTIL, query = "SELECT br"
				+ " FROM ProposalBranch br JOIN FETCH br.proposalBags WHERE br.id = :branchId"
				+ " AND br.proposalBags.creationTime < :until ORDER BY br.creationTime DESC"),
		@NamedQuery(name = ProposalBranch.LOAD_FULL_BRANCH, query = "SELECT br"
				+ " FROM ProposalBranch br JOIN FETCH br.proposalBags WHERE br.id = :branchId"
				+ " ORDER BY br.creationTime DESC")})
@Entity
@Table(name = "PROPOSAL_BRANCH", uniqueConstraints = {@UniqueConstraint(columnNames = {"ALBUM_ID", "NAME"})})
@JsonInclude(Include.NON_NULL)
public class ProposalBranch {

	public static final String FIND_BRANCH_BY_NAME = "FIND_BRANCH_BY_NAME";

	public static final String FIND_BRANCHES_OF_ALBUM = "FIND_BRANCHES_OF_ALBUM";

	public static final String LOAD_BRANCH_UNTIL = "LOAD_BRANCH_UNTIL";

	public static final String LOAD_FULL_BRANCH = "LOAD_FULL_BRANCH";

	@Version
	@JsonIgnore
	private Long version;

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
	@Column(name = "CREATION_TIME", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
	@JsonSerialize(using = TimestampJsonSerializer.class)
	private Timestamp creationTime;

	@ElementCollection
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ProposalBag> proposalBags;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "HEAD_BAG_ID", updatable = true)
	private ProposalBag head;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ALBUM_ID", nullable = false, updatable = false)
	@JsonIgnore
	private Album album;

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

	/**
	 * Getter of proposalBags.
	 * 
	 * @return the proposalBags
	 */
	public List<ProposalBag> getProposalBags() {
		return this.proposalBags;
	}

	/**
	 * Setter of proposalBags.
	 * 
	 * @param proposalBags
	 *            the proposalBags to set
	 */
	public void setProposalBags(final List<ProposalBag> proposalBags) {
		this.proposalBags = proposalBags;
	}

	/**
	 * Getter of head.
	 * 
	 * @return the head
	 */
	public ProposalBag getHead() {
		return this.head;
	}

	/**
	 * Setter of head.
	 * 
	 * @param head
	 *            the head to set
	 */
	public void setHead(final ProposalBag head) {
		this.head = head;
	}

}
