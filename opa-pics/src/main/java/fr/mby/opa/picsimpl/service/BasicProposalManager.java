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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.opa.pics.dao.IProposalDao;
import fr.mby.opa.pics.model.ProposalBranch;
import fr.mby.opa.pics.service.IProposalManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicProposalManager implements IProposalManager {

	private static final String SELECTED_BRANCH_ID = "SELECTED_BRANCH_ID_";

	@Autowired
	private IProposalDao proposalDao;

	@Override
	public void selectBranch(final long albumId, final ProposalBranch branch, final HttpServletRequest request) {
		Assert.notNull(branch, "No ProposalBranch supplied !");
		Assert.notNull(request, "No HttpServletRequest supplied !");

		request.getSession().setAttribute(BasicProposalManager.SELECTED_BRANCH_ID + albumId, branch);
	}

	@Override
	public ProposalBranch getSelectedBranch(final long albumId, final HttpServletRequest request) {
		Assert.notNull(request, "No HttpServletRequest supplied !");

		ProposalBranch branch = this._getSelectedBranch(albumId, request);
		if (branch == null) {
			// get master branch
			final ProposalBranch masterBranch = this.proposalDao.findBranchByName(albumId,
					IProposalDao.MASTER_BRANCH_NAME);
			if (masterBranch != null) {
				this.selectBranch(albumId, masterBranch, request);
				branch = masterBranch;
			} else {
				throw new IllegalStateException("Master branch should exists !");
			}
		}

		return branch;
	}

	protected ProposalBranch _getSelectedBranch(final long albumId, final HttpServletRequest request) {
		final ProposalBranch branch = (ProposalBranch) request.getSession().getAttribute(
				BasicProposalManager.SELECTED_BRANCH_ID + albumId);
		return branch;
	}

}
