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

import fr.mby.opa.pics.exception.ProposalBagLockedException;
import fr.mby.opa.pics.exception.ProposalBagNotFoundException;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.opa.pics.model.ProposalBranch;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IProposalDao {

	ProposalBranch createBranch(ProposalBranch branch);

	ProposalBranch updateBranch(ProposalBranch branch);

	ProposalBag createBag(ProposalBag bag);

	ProposalBag updateBag(ProposalBag bag) throws ProposalBagNotFoundException, ProposalBagLockedException;

	ProposalBag commitBag(ProposalBag bag) throws ProposalBagNotFoundException, ProposalBagLockedException;

	ProposalBag findLastBag(long albumId);

	List<ProposalBranch> findAllBranches(long albumId);

	List<ProposalBag> findBagAncestry(long branchId, long until);

}
