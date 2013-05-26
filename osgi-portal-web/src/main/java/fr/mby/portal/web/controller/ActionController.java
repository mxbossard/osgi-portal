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

package fr.mby.portal.web.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.mby.portal.core.IUserActionDispatcher;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping("/action")
public class ActionController {

	@Autowired
	private Collection<IUserActionDispatcher> userActionDispatchers;

	/**
	 * Process the request and return a ModelAndView object which the DispatcherServlet will render.
	 */
	@RequestMapping(method = RequestMethod.GET)
	ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		String viewName = "portal";

		if (this.userActionDispatchers == null) {
			viewName = "noUserActionDispatcher";
		} else {
			final IUserActionDispatcher userActionDispatcher = this.userActionDispatchers.iterator().next();
			userActionDispatcher.dispatch(request, response);
		}

		return new ModelAndView(viewName);

	}

	/**
	 * Getter of userActionDispatchers.
	 * 
	 * @return the userActionDispatchers
	 */
	public Collection<IUserActionDispatcher> getUserActionDispatchers() {
		return this.userActionDispatchers;
	}

	/**
	 * Setter of userActionDispatchers.
	 * 
	 * @param userActionDispatchers
	 *            the userActionDispatchers to set
	 */
	public void setUserActionDispatchers(final Collection<IUserActionDispatcher> userActionDispatchers) {
		this.userActionDispatchers = userActionDispatchers;
	}

}