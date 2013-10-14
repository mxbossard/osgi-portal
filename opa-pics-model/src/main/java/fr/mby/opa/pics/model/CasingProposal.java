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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class CasingProposal extends AbstractUnitProposal {

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "SESSION_ID", nullable = false)
	private Session session;

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
