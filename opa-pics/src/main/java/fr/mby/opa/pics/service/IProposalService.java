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

import fr.mby.opa.pics.exception.PictureNotFoundException;
import fr.mby.opa.pics.exception.ProposalBagCommitedException;
import fr.mby.opa.pics.exception.ProposalBagNotFoundException;
import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.CasingProposal;
import fr.mby.opa.pics.model.EraseProposal;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.opa.pics.model.ProposalBranch;
import fr.mby.opa.pics.model.RankingProposal;
import fr.mby.opa.pics.model.Session;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IProposalService {

	ProposalBranch createBranch(String name, String description, Album album, Long branchToForkId);

	ProposalBranch findBranch(long branchId);

	ProposalBag createBag(String name, String description, long branchId);

	ProposalBag updateBag(ProposalBag proposalBag) throws ProposalBagNotFoundException, ProposalBagCommitedException;

	ProposalBag commitBag(ProposalBag proposalBag) throws ProposalBagNotFoundException, ProposalBagCommitedException;

	ProposalBag findLastBag(long albumId);

	List<ProposalBranch> findAllBranches(long albumId);

	List<ProposalBag> findBagAncestry(long branchId, long until);

	CasingProposal createCasingProposal(long pictureId, Session session) throws PictureNotFoundException;

	RankingProposal createRankingProposal(long pictureId, int rank) throws PictureNotFoundException;

	EraseProposal createEraseProposal(long pictureId, boolean erase) throws PictureNotFoundException;

}
