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

package fr.mby.opa.login.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.mby.opa.login.web.model.LoginForm;
import fr.mby.portal.api.IPortalService;
import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IPortalApp;
import fr.mby.portal.api.message.IActionMessage;
import fr.mby.portal.api.message.IActionReply;
import fr.mby.portal.api.message.IRenderMessage;
import fr.mby.portal.api.message.IRenderReply;
import fr.mby.portal.core.auth.IAuthentication;
import fr.mby.portal.core.auth.PortalUserAuthentication;
import fr.mby.portal.core.security.ILoginManager;
import fr.mby.portal.core.security.LoginException;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping("")
public class LoginController implements IPortalApp {

	private static final String LOGIN_PAGE = "login";

	private static final String LOGOUT_PAGE = "logout";

	@Autowired(required = true)
	private IPortalService portalService;

	@Autowired(required = true)
	private ILoginManager loginManager;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String display(final ModelMap model, final HttpServletRequest request) {
		String view = LoginController.LOGIN_PAGE;

		// On basic GET which page do we display ?
		final boolean logged = this.loginManager.isLogged(request);
		if (logged) {
			view = LoginController.LOGOUT_PAGE;
		}

		this.initModelMap(model, request);

		return view;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(final ModelMap model, @Valid final LoginForm loginForm, final BindingResult result,
			final HttpServletRequest request) {
		String view = LoginController.LOGIN_PAGE;

		if (!result.hasErrors()) {
			// If no errors
			final IAuthentication authentication = new PortalUserAuthentication(loginForm.getUsername(),
					loginForm.getPassword());
			try {
				// Try to login user
				this.loginManager.login(request, authentication);
				// If no error user is logged => display logout page
				view = LoginController.LOGOUT_PAGE;
			} catch (final LoginException e) {
				// If login exception return to login page
			}
		}

		this.initModelMap(model, request);

		return view;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(final ModelMap model, final HttpServletRequest request) {
		this.loginManager.logout(request);

		this.initModelMap(model, request);

		return LoginController.LOGIN_PAGE;
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
	 * @param model
	 * @param request
	 */
	protected void initModelMap(final ModelMap model, final HttpServletRequest request) {
		final IApp thisApp = this.portalService.getTargetedApp(request);
		model.addAttribute("app", thisApp);
		model.addAttribute("loginForm", new LoginForm());
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

	/**
	 * Getter of loginManager.
	 * 
	 * @return the loginManager
	 */
	public ILoginManager getLoginManager() {
		return this.loginManager;
	}

	/**
	 * Setter of loginManager.
	 * 
	 * @param loginManager
	 *            the loginManager to set
	 */
	public void setLoginManager(final ILoginManager loginManager) {
		this.loginManager = loginManager;
	}

}