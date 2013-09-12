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

import org.springframework.stereotype.Repository;

import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.CasingProposal;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.opa.pics.model.Session;
import fr.mby.opa.pics.model.Shoot;
import fr.mby.opa.pics.service.IProposalDao;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Repository
public class DbProposalDao extends AbstractPicsDao implements IProposalDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.opa.pics.service.IProposalDao#createOrderingProposal(fr.mby.opa.pics.model.ProposalBag,
	 * fr.mby.opa.pics.model.Album)
	 */
	@Override
	public ProposalBag createOrderingProposal(final ProposalBag proposal, final Album album) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.opa.pics.service.IProposalDao#createUnitProposal(fr.mby.opa.pics.model.CasingProposal)
	 */
	@Override
	public CasingProposal createUnitProposal(final CasingProposal proposal) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.opa.pics.service.IProposalDao#createSession(fr.mby.opa.pics.model.Session)
	 */
	@Override
	public Session createSession(final Session session) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.opa.pics.service.IProposalDao#createShoot(fr.mby.opa.pics.model.Shoot)
	 */
	@Override
	public Shoot createShoot(final Shoot session) {
		// TODO Auto-generated method stub
		return null;
	}

}
