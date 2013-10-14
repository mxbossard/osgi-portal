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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * An EraseProposal ask for erasing. Multiple EraseProposal are grouped in a ProposalBag.
 * 
 * Unique constraints :
 * <ul>
 * <li>(ProposalBag, Picture) : cannot erase multiple time the same in Picture in a ProposalBag.</li>
 * </ul>
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Entity
@Table(name = "ERASE_PROPOSAL", uniqueConstraints = {@UniqueConstraint(columnNames = "PROPOSAL_BAG_ID,PICTURE_ID")})
@JsonInclude(Include.NON_NULL)
public class EraseProposal extends AbstractUnitProposal {

	@Basic(optional = false)
	@Column(name = "ERASABLE")
	private Boolean erasable;

	/**
	 * Getter of erasable.
	 * 
	 * @return the erasable
	 */
	public Boolean isErasable() {
		return this.erasable;
	}

	/**
	 * Setter of erasable.
	 * 
	 * @param erasable
	 *            the erasable to set
	 */
	public void setErasable(final Boolean erasable) {
		this.erasable = erasable;
	}

}
