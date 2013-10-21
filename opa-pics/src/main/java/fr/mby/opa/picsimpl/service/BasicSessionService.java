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

import fr.mby.opa.pics.dao.IPictureDao;
import fr.mby.opa.pics.dao.IProposalDao;
import fr.mby.opa.pics.exception.ProposalBagCommitedException;
import fr.mby.opa.pics.exception.ProposalBagNotFoundException;
import fr.mby.opa.pics.model.CasingProposal;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.opa.pics.model.Session;
import fr.mby.opa.pics.service.IProposalService;
import fr.mby.opa.pics.service.ISessionService;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicSessionService implements ISessionService {

	@Autowired
	private IProposalService proposalService;

	@Autowired
	private IPictureDao pictureDao;

	@Autowired
	private IProposalDao proposalDao;

	@Override
	public Session createSession(final long pictureId, final long bagId, final String name, final String description) {
		Session session = null;

		final ProposalBag bag = this.proposalDao.findBag(bagId);

		final Picture picture = this.pictureDao.findPictureById(pictureId);

		if (bag != null && !bag.isCommited() && picture != null) {
			final Timestamp pictureCreationTime = picture.getCreationTime();

			session = new Session();
			session.setCreationTime(new Timestamp(System.currentTimeMillis()));
			session.setName(name);
			session.setDescription(description);
			session.setStartTime(pictureCreationTime);
			session.setEndTime(pictureCreationTime);
			final CasingProposal proposal = this.proposalService.createCasingProposal(pictureId, session);

			bag.getCasingProposals().add(proposal);
			try {
				this.proposalService.updateBag(bag);
			} catch (final ProposalBagNotFoundException e) {
				// Should not happened
				throw new IllegalStateException(e);
			} catch (final ProposalBagCommitedException e) {
				// Should not happened
				throw new IllegalStateException(e);
			}
		}

		return session;
	}

	@Override
	public Session updateSession(final long sessionId, final String name, final String description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session addPictureToSession(final long pictureId, final long sessionId, final long branchId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session addPicturesToSession(final long[] picturesId, final long sessionId, final long branchId) {
		// TODO Auto-generated method stub
		return null;
	}

}
