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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.IPortalRenderer;
import fr.mby.portal.core.session.ISessionManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping("/")
public class PortalController {

	/** Name of view attribute for Apps to render. */
	private static final String APPS_TO_RENDER = "appsToRender";

	private Collection<IPortalRenderer> portalRenderers;

	private ISessionManager sessionManager;

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final ModelAndView view = new ModelAndView("portal");

		// Init portal session
		this.sessionManager.initPortalSession(request, response);

		final IPortalRenderer firstPortalRenderer = this.chooseOnePortalRenderer();

		final List<IApp> appsToRender = firstPortalRenderer.getAppsToRender(request);
		view.addObject(PortalController.APPS_TO_RENDER, appsToRender);

		return view;
	}

	protected IPortalRenderer chooseOnePortalRenderer() throws Exception {
		final IPortalRenderer firstPortalRenderer;

		if (this.portalRenderers != null && this.portalRenderers.size() > 0) {
			firstPortalRenderer = this.portalRenderers.iterator().next();
		} else {
			throw new Exception("No IPortalRenderer available !");
		}

		return firstPortalRenderer;
	}

	/**
	 * Getter of portalRenderers.
	 * 
	 * @return the portalRenderers
	 */
	public Collection<IPortalRenderer> getPortalRenderers() {
		return this.portalRenderers;
	}

	/**
	 * Setter of portalRenderers.
	 * 
	 * @param portalRenderers
	 *            the portalRenderers to set
	 */
	public void setPortalRenderers(final Collection<IPortalRenderer> portalRenderers) {
		this.portalRenderers = portalRenderers;
	}

	/**
	 * Getter of sessionManager.
	 * 
	 * @return the sessionManager
	 */
	public ISessionManager getSessionManager() {
		return this.sessionManager;
	}

	/**
	 * Setter of sessionManager.
	 * 
	 * @param sessionManager
	 *            the sessionManager to set
	 */
	public void setSessionManager(final ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}