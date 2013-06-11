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

import fr.mby.portal.api.IPortalService;
import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IPortalApp;
import fr.mby.portal.api.message.IActionMessage;
import fr.mby.portal.api.message.IActionReply;
import fr.mby.portal.api.message.IRenderMessage;
import fr.mby.portal.api.message.IRenderReply;
import fr.mby.portal.core.IUserActionDispatcher;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping("/")
public class HelloController implements IPortalApp {

	@Autowired
	private Collection<IUserActionDispatcher> userActionDispatchers;

	@Autowired
	private IPortalService portalService;

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final ModelAndView mv = new ModelAndView("hello");

		mv.addObject("message", "Spring 3 MVC Hello World !");

		if (this.userActionDispatchers == null) {
			mv.addObject("userActionDispatcherCount", "null");
		} else {
			mv.addObject("userActionDispatcherCount", this.userActionDispatchers.size());
		}

		final IApp helloApp = this.portalService.getTargetedApp(request);
		mv.addObject("app", helloApp);

		return mv;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IPortalApp#destory()
	 */
	@Override
	public void destory() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IPortalApp#init(fr.mby.portal.api.app.IAppConfig)
	 */
	@Override
	public void init(final IAppConfig config) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IPortalApp#processAction(fr.mby.portal.api.message.IActionMessage,
	 * fr.mby.portal.api.message.IActionReply)
	 */
	@Override
	public void processAction(final IActionMessage request, final IActionReply response) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IPortalApp#render(fr.mby.portal.api.message.IRenderMessage,
	 * fr.mby.portal.api.message.IRenderReply)
	 */
	@Override
	public void render(final IRenderMessage request, final IRenderReply response) {
		// TODO Auto-generated method stub

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

	/**
	 * Getter of portalService.
	 * 
	 * @return the portalService
	 */
	public IPortalService getPortalService() {
		return this.portalService;
	}

	/**
	 * Setter of portalService.
	 * 
	 * @param portalService
	 *            the portalService to set
	 */
	public void setPortalService(final IPortalService portalService) {
		this.portalService = portalService;
	}

}