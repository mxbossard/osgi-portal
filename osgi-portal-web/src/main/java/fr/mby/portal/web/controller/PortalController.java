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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.util.SwallowingHttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.IPortalRenderer;
import fr.mby.portal.core.session.ISessionManager;
import fr.mby.portal.servlet.http.OpaHttpServletRequest;

/**
 * @author Maxime Bossard - 2013
 * 
 */

@Controller
@RequestMapping("/")
public class PortalController implements ServletContextAware {

	/** Name of view attribute for Apps to render. */
	private static final String APPS_TO_RENDER = "appsToRender";

	private static final String APPS_RENDERED = "renderedApps";

	private static final String APP_CONTENT_REGEXP = "\\A.*<body>(.*)<\\/body>.*\\z";

	private static final Pattern APP_CONTENT_PATTERN = Pattern.compile(PortalController.APP_CONTENT_REGEXP,
			Pattern.DOTALL);

	private Collection<IPortalRenderer> portalRenderers;

	private ISessionManager sessionManager;

	private ServletContext servletContext;

	@RequestMapping(method = RequestMethod.GET)
	ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final ModelAndView view = new ModelAndView("portal");

		// Init portal session
		this.sessionManager.initPortalSession(request, response);

		final IPortalRenderer firstPortalRenderer = this.chooseOnePortalRenderer();

		final List<IApp> appsToRender = firstPortalRenderer.getAppsToRender(request);
		view.addObject(PortalController.APPS_TO_RENDER, appsToRender);

		this.renderApp(request, response, view, appsToRender);

		return view;
	}

	/**
	 * Perform internal rendering.
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param appsToRender
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void renderApp(final HttpServletRequest request, final HttpServletResponse response,
			final ModelAndView view, final List<IApp> appsToRender) throws ServletException, IOException {
		final Map<IApp, String> appsRendered = new HashMap<IApp, String>(8);

		if (appsToRender != null) {
			for (final IApp app : appsToRender) {
				final ServletContext loginContext = this.servletContext.getContext(app.getWebPath());
				if (loginContext != null) {
					final Writer sout = new StringWriter(1024);

					final OpaHttpServletRequest opaRequest = new OpaHttpServletRequest(request, app);
					final SwallowingHttpServletResponse swallowingResponse = new SwallowingHttpServletResponse(
							response, sout, "UTF-8");
					loginContext.getRequestDispatcher("/").forward(opaRequest, swallowingResponse);
					final String appRendered = sout.toString();

					final String appContent = this.stripHeaders(appRendered);

					appsRendered.put(app, appContent);
				}
			}
		}

		view.addObject(PortalController.APPS_RENDERED, appsRendered);
	}

	/**
	 * @param appRendered
	 * @return
	 */
	protected String stripHeaders(final String appRendered) {
		String appContent = null;

		final Matcher appContentMatcher = PortalController.APP_CONTENT_PATTERN.matcher(appRendered);
		if (appContentMatcher.find()) {
			appContent = appContentMatcher.group(1);
		}

		return appContent;
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

	@Override
	public void setServletContext(final ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}