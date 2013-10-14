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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A RankingProposal rank a Picture in a Shoot. Multiple RankingProposal are grouped in a ProposalBag.
 * 
 * Unique constraints :
 * <ul>
 * <li>(ProposalBag, Picture) : cannot rank multiple time the same in Picture in a ProposalBag.</li>
 * <li>(ProposalBag, Shoot, Rank) : cannot attribute 2 identical Rank in same Shoot in same ProposalBag.</li>
 * </ul>
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Entity
@Table(name = "RANKING_PROPOSAL", uniqueConstraints = {@UniqueConstraint(columnNames = "PROPOSAL_BAG_ID,PICTURE_ID"),
		@UniqueConstraint(columnNames = "PROPOSAL_BAG_ID,SHOOT_ID,RANK")})
@JsonInclude(Include.NON_NULL)
public class RankingProposal extends AbstractUnitProposal {

	@Basic(optional = false)
	@Column(name = "RANK")
	private Integer rank;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SHOOT_ID", nullable = false, updatable = false)
	private Shoot shoot;

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

}
