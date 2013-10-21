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

package fr.mby.opa.pics.dao;

import java.util.List;

import fr.mby.opa.pics.exception.ProposalBagCommitedException;
import fr.mby.opa.pics.exception.ProposalBagNotFoundException;
import fr.mby.opa.pics.exception.ProposalBranchNotFoundException;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.opa.pics.model.ProposalBranch;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IProposalDao {

	public static final String MASTER_BRANCH_NAME = "master";

	public static final String INITIAL_PROPOSAL_NAME = "initial proposal";

	ProposalBranch createBranch(ProposalBranch branch);

	ProposalBranch forkBranch(ProposalBranch fork, long branchToForkId) throws ProposalBranchNotFoundException;

	ProposalBranch updateBranch(ProposalBranch branch) throws ProposalBranchNotFoundException;

	ProposalBranch findBranch(long branchId);

	ProposalBranch findBranchByName(long albumId, String name);

	ProposalBag createBag(ProposalBag bag, long branchId) throws ProposalBranchNotFoundException;

	ProposalBag updateBag(ProposalBag bag) throws ProposalBagNotFoundException, ProposalBagCommitedException;

	ProposalBag commitBag(ProposalBag bag) throws ProposalBagNotFoundException, ProposalBagCommitedException;

	ProposalBag findBag(long bagId);

	ProposalBag findLastBag(long albumId);

	List<ProposalBranch> findAllBranches(long albumId);

	List<ProposalBag> findBagAncestry(long branchId, long until);

}
