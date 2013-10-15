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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.opa.pics.dao.IProposalDao;
import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.CasingProposal;
import fr.mby.opa.pics.model.EraseProposal;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.opa.pics.model.RankingProposal;
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
	public ProposalBag createProposalBag(final String name, final String description, final Album album,
			final ProposalBag parent) {
		Assert.hasText(name, "No name supplied !");
		Assert.notNull(album, "No Album supplied !");

		final ProposalBag bag = new ProposalBag();
		bag.setAlbum(album);
		bag.setBaseProposal(parent);
		bag.setCreationTime(new Timestamp(System.currentTimeMillis()));
		bag.setName(name);
		bag.setDescription(description);
		bag.setCommited(false);

		final ProposalBag createdBag = this.proposalDao.createProposalBag(bag);

		return createdBag;
	}

	@Override
	public ProposalBag updateProposalBag(final ProposalBag proposalBag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProposalBag commitProposalBag(final ProposalBag proposalBag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CasingProposal createCasingProposal(final Picture picture) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RankingProposal createRankingProposal(final Picture picture) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EraseProposal createEraseProposal(final Picture picture) {
		// TODO Auto-generated method stub
		return null;
	}

}
