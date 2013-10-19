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

package fr.mby.opa.pics.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.mby.opa.pics.model.CasingProposal;
import fr.mby.opa.pics.model.ProposalBag;
import fr.mby.opa.pics.model.ProposalBranch;
import fr.mby.opa.pics.model.Session;
import fr.mby.opa.pics.service.IProposalService;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping(SessionController.SESSION_CONTROLLER_PATH)
public class SessionController {

	public static final String SESSION_CONTROLLER_PATH = "session";

	@Autowired
	private IProposalService proposalService;

	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public Session createSessionJson(@Valid @RequestBody final Session session, @RequestParam final Long pictureId,
			@RequestParam final Long branchId) throws Exception {
		Assert.notNull(session, "No Session supplied !");
		Assert.notNull(pictureId, "No Picture Id supplied !");
		Assert.notNull(branchId, "No ProposalBranch Id supplied !");

		final ProposalBranch branch = this.proposalService.findBranch(branchId);

		final ProposalBag currentBag = branch.getHead();

		final CasingProposal proposal = this.proposalService.createCasingProposal(pictureId, session);

		return proposal.getSession();
	}

	@ExceptionHandler
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleMethodArgumentNotValidException(final MethodArgumentNotValidException error) {
		return "Bad request: " + error.getMessage();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleIllegalArgumentException(final IllegalArgumentException e) {
		return "Error while validating arguments !";
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(final Exception e) {
		return "return error object instead";
	}

}