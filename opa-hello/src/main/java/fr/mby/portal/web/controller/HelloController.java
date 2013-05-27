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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.mby.portal.core.IUserActionDispatcher;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping("/")
public class HelloController {

	@Autowired
	private Collection<IUserActionDispatcher> userActionDispatchers;

	@RequestMapping(method = RequestMethod.GET)
	public String hello(final Model model) {

		model.addAttribute("message", "Spring 3 MVC Hello World !");

		if (this.userActionDispatchers == null) {
			model.addAttribute("userActionDispatcherCount", "null");
		} else {
			model.addAttribute("userActionDispatcherCount", this.userActionDispatchers.size());
		}

		return "hello";

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