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

package fr.mby.opa.pics.service;

import java.util.List;

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

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IProposalService {

	ProposalBranch createBranch(String name, String description, Album album, ProposalBranch fork);

	ProposalBag createBag(String name, String description, ProposalBranch branch);

	ProposalBag updateBag(ProposalBag proposalBag) throws ProposalBagNotFoundException, ProposalBagLockedException;

	ProposalBag commitBag(ProposalBag proposalBag) throws ProposalBagNotFoundException, ProposalBagLockedException;

	ProposalBag findLastBag(long albumId);

	List<ProposalBranch> findAllBranches(long albumId);

	List<ProposalBag> findBagAncestry(long branchId, long until);

	CasingProposal createCasingProposal(Picture picture, Session session);

	RankingProposal createRankingProposal(Picture picture, int rank);

	EraseProposal createEraseProposal(Picture picture, boolean erase);

}
