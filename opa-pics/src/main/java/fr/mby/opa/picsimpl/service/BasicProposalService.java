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

package fr.mby.opa.picsimpl.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.opa.pics.dao.IProposalDao;
import fr.mby.opa.pics.exception.ProposalBagLockedException;
import fr.mby.opa.pics.exception.ProposalBagNotFoundException;
import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.CasingProposal;
import fr.mby.opa.pics.model.EraseProposal;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.opa.pics.model.ProposalBranch;
import fr.mby.opa.pics.model.RankingProposal;
import fr.mby.opa.pics.model.Session;
import fr.mby.opa.pics.service.IProposalService;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicProposalService implements IProposalService {

	@Autowired
	private IProposalDao proposalDao;

	@Override
	public ProposalBranch createBranch(final String name, final String description, final Album album,
			final ProposalBranch fork) {
		Assert.hasText(name, "No name supplied !");
		Assert.notNull(album, "No Album supplied !");

		final ProposalBranch branch = new ProposalBranch();
		branch.setCreationTime(new Timestamp(System.currentTimeMillis()));
		branch.setName(name);
		branch.setDescription(description);

		if (fork != null) {
			// We fork the branch => we duplicate it
			// TODO
		}

		final ProposalBranch createdBranch = this.proposalDao.createBranch(branch);

		return createdBranch;
	}

	@Override
	public ProposalBag createBag(final String name, final String description, final ProposalBranch branch) {
		Assert.hasText(name, "No name supplied !");
		Assert.notNull(branch, "No ProposalBranch supplied !");

		final ProposalBag bag = new ProposalBag();
		bag.setBranch(branch);
		bag.setCreationTime(new Timestamp(System.currentTimeMillis()));
		bag.setName(name);
		bag.setDescription(description);
		bag.setCommited(false);

		final ProposalBag createdBag = this.proposalDao.createBag(bag);

		return createdBag;
	}

	@Override
	public ProposalBag updateBag(final ProposalBag proposalBag) throws ProposalBagNotFoundException,
			ProposalBagLockedException {
		Assert.notNull(proposalBag, "No ProposalBag supplied !");

		final ProposalBag updatedBag = this.proposalDao.updateBag(proposalBag);
		return updatedBag;
	}

	@Override
	public ProposalBag commitBag(final ProposalBag proposalBag) throws ProposalBagNotFoundException,
			ProposalBagLockedException {
		Assert.notNull(proposalBag, "No ProposalBag supplied !");

		// TODO pull full revision to this bag and descend diff

		final ProposalBag commitedBag = this.proposalDao.commitBag(proposalBag);
		return commitedBag;
	}

	@Override
	public ProposalBag findLastBag(final long albumId) {
		return this.proposalDao.findLastBag(albumId);
	}

	@Override
	public List<ProposalBranch> findAllBranches(final long albumId) {
		return this.proposalDao.findAllBranches(albumId);
	}

	@Override
	public List<ProposalBag> findBagAncestry(final long branchId, final long until) {
		return this.proposalDao.findBagAncestry(branchId, until);
	}

	@Override
	public CasingProposal createCasingProposal(final Picture picture, final Session session) {
		final CasingProposal proposal = new CasingProposal();
		proposal.setCreationTime(this._getCurrentTimestamp());
		proposal.setPicture(picture);
		proposal.setSession(session);

		return proposal;
	}

	@Override
	public RankingProposal createRankingProposal(final Picture picture, final int rank) {
		final RankingProposal proposal = new RankingProposal();
		proposal.setCreationTime(this._getCurrentTimestamp());
		proposal.setPicture(picture);
		proposal.setRank(rank);

		return proposal;
	}

	@Override
	public EraseProposal createEraseProposal(final Picture picture, final boolean erase) {
		final EraseProposal proposal = new EraseProposal();
		proposal.setCreationTime(this._getCurrentTimestamp());
		proposal.setPicture(picture);
		proposal.setErasable(erase);

		return proposal;
	}

	protected Timestamp _getCurrentTimestamp() {
		final Timestamp now = new Timestamp(System.currentTimeMillis());
		return now;
	}

}
