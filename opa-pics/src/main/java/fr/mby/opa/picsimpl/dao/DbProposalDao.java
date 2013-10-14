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

package fr.mby.opa.picsimpl.dao;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import fr.mby.opa.pics.dao.IProposalDao;
import fr.mby.opa.pics.model.AbstractUnitProposal;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.utils.common.jpa.TxCallback;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Repository
public class DbProposalDao extends AbstractPicsDao implements IProposalDao {

	@Override
	public ProposalBag createProposalBag(final ProposalBag proposalBag) {
		Assert.notNull(proposalBag, "No ProposalBag supplied !");
		Assert.isNull(proposalBag.getId(), "Id should not be set for creation !");

		new TxCallback(this.getEmf()) {

			@Override
			protected void executeInTransaction(final EntityManager em) {
				em.persist(proposalBag);
			}
		};

		return proposalBag;
	}

	@Override
	public AbstractUnitProposal createProposal(final AbstractUnitProposal proposal) {
		Assert.notNull(proposal, "No Proposal supplied !");
		Assert.isNull(proposal.getId(), "Id should not be set for creation !");

		new TxCallback(this.getEmf()) {

			@Override
			protected void executeInTransaction(final EntityManager em) {
				em.persist(proposal);
			}
		};

		return proposal;
	}

	@Override
	public ProposalBag findLastProposalBag(final Long albumId) {
		// TODO Auto-generated method stub
		return null;
	}

}
