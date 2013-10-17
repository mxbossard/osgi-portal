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

import java.util.List;

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
import fr.mby.opa.pics.model.ProposalBranch;
import fr.mby.utils.common.jpa.EmCallback;
import fr.mby.utils.common.jpa.TxCallback;
import fr.mby.utils.common.jpa.TxCallbackReturn;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Repository
public class DbProposalDao extends AbstractPicsDao implements IProposalDao {

	/** Page size used for pagination. */
	protected static final int PAGINATION_SIZE = 50;

	@Override
	public ProposalBranch createBranch(final ProposalBranch branch) {
		Assert.notNull(branch, "No ProposalBag supplied !");
		Assert.isNull(branch.getId(), "Id should not be set for creation !");

		new TxCallback(this.getEmf()) {

			@Override
			protected void executeInTransaction(final EntityManager em) {
				em.persist(branch);
			}
		};

		return branch;
	}

	@Override
	public ProposalBag createBag(final ProposalBag bag) {
		Assert.notNull(bag, "No ProposalBag supplied !");
		Assert.isNull(bag.getId(), "Id should not be set for creation !");

		new TxCallback(this.getEmf()) {

			@Override
			protected void executeInTransaction(final EntityManager em) {
				em.persist(bag);
			}
		};

		return bag;
	}

	@Override
	public ProposalBag updateBag(final ProposalBag bag) throws ProposalBagNotFoundException,
			ProposalBagLockedException {
		Assert.notNull(bag, "No ProposalBag supplied !");
		Assert.notNull(bag.getId(), "Id should be set for update !");

		if (bag.isCommited()) {
			throw new ProposalBagLockedException();
		}

		return this._updateProposalBagInternal(bag);
	}

	@Override
	public ProposalBag commitBag(final ProposalBag bag) throws ProposalBagNotFoundException,
			ProposalBagLockedException {
		Assert.notNull(bag, "No ProposalBag supplied !");
		if (bag.isCommited()) {
			throw new ProposalBagLockedException();
		}

		this._checkProposalBagIntegrity(bag);

		bag.setCommited(true);

		return this._updateProposalBagInternal(bag);
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
	public ProposalBag findLastBag(final long albumId) {
		final EmCallback<ProposalBag> emCallback = new EmCallback<ProposalBag>(this.getEmf()) {

			@Override
			@SuppressWarnings("unchecked")
			protected ProposalBag executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findLastBagQuery = em.createNamedQuery(ProposalBag.FIND_LAST_ALBUM_BAG);
				findLastBagQuery.setParameter("albumId", albumId);
				findLastBagQuery.setMaxResults(1);

				final ProposalBag bag = Iterables.getFirst(findLastBagQuery.getResultList(), null);
				return bag;
			}
		};

		return emCallback.getReturnedValue();
	}

	@Override
	public List<ProposalBranch> findAllBranches(final long albumId) {
		final EmCallback<List<ProposalBranch>> emCallback = new EmCallback<List<ProposalBranch>>(this.getEmf()) {

			@Override
			@SuppressWarnings("unchecked")
			protected List<ProposalBranch> executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findLastBagQuery = em.createNamedQuery(ProposalBranch.FIND_BRANCHES_OF_ALBUM);
				findLastBagQuery.setParameter("albumId", albumId);

				final List<ProposalBranch> branches = findLastBagQuery.getResultList();
				return branches;
			}
		};

		return emCallback.getReturnedValue();
	}

	@Override
	public List<ProposalBag> findBagAncestry(final long branchId, final long until) {
		final EmCallback<List<ProposalBag>> emCallback = new EmCallback<List<ProposalBag>>(this.getEmf()) {

			@Override
			@SuppressWarnings("unchecked")
			protected List<ProposalBag> executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findLastBagQuery = em.createNamedQuery(ProposalBag.FIND_BRANCH_BAGS_UNTIL);
				findLastBagQuery.setParameter("branchId", branchId);
				findLastBagQuery.setParameter("until", until);
				findLastBagQuery.setMaxResults(DbProposalDao.PAGINATION_SIZE);

				final List<ProposalBag> branches = findLastBagQuery.getResultList();
				return branches;
			}
		};

		return emCallback.getReturnedValue();
	}

	/**
	 * @param proposalBag
	 */
	protected void _checkProposalBagIntegrity(final ProposalBag proposalBag) {
		// TODO Auto-generated method stub

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
