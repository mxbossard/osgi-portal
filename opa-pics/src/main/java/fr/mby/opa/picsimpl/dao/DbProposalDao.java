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
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.google.common.collect.Iterables;

import fr.mby.opa.pics.dao.IProposalDao;
import fr.mby.opa.pics.exception.ProposalBagLockedException;
import fr.mby.opa.pics.exception.ProposalBagNotFoundException;
import fr.mby.opa.pics.model.AbstractUnitProposal;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.utils.common.jpa.EmCallback;
import fr.mby.utils.common.jpa.TxCallback;
import fr.mby.utils.common.jpa.TxCallbackReturn;

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
	public ProposalBag updateProposalBag(final ProposalBag proposalBag) throws ProposalBagNotFoundException,
			ProposalBagLockedException {
		Assert.notNull(proposalBag, "No ProposalBag supplied !");
		Assert.notNull(proposalBag.getId(), "Id should be set for update !");

		if (proposalBag.isCommited()) {
			throw new ProposalBagLockedException();
		}

		return this._updateProposalBagInternal(proposalBag);
	}

	@Override
	public ProposalBag commitProposalBag(final ProposalBag proposalBag) throws ProposalBagNotFoundException,
			ProposalBagLockedException {
		Assert.notNull(proposalBag, "No ProposalBag supplied !");
		if (proposalBag.isCommited()) {
			throw new ProposalBagLockedException();
		}

		proposalBag.setCommited(true);

		return this._updateProposalBagInternal(proposalBag);
	}

	// @Override
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
		Assert.notNull(albumId, "Album Id should be supplied !");

		final EmCallback<ProposalBag> emCallback = new EmCallback<ProposalBag>(this.getEmf()) {

			@Override
			@SuppressWarnings("unchecked")
			protected ProposalBag executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findLastBagQuery = em.createNamedQuery(ProposalBag.FIND_LAST_PROPOSAL_BAG);
				findLastBagQuery.setParameter("albumId", albumId);
				findLastBagQuery.setMaxResults(1);

				final ProposalBag bag = Iterables.getFirst(findLastBagQuery.getResultList(), null);
				return bag;
			}
		};

		return emCallback.getReturnedValue();
	}

	/**
	 * @param proposalBag
	 * @return
	 */
	protected ProposalBag _updateProposalBagInternal(final ProposalBag proposalBag) {
		final TxCallbackReturn<ProposalBag> txCallback = new TxCallbackReturn<ProposalBag>(this.getEmf()) {

			@Override
			protected ProposalBag executeInTransaction(final EntityManager em) {
				final ProposalBag updatedBag = em.merge(proposalBag);
				em.flush();
				em.refresh(updatedBag);
				return updatedBag;
			}
		};

		final ProposalBag updatedBag = txCallback.getReturnedValue();
		return updatedBag;
	}

}
