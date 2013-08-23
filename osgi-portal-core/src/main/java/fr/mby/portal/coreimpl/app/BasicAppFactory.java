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

package fr.mby.portal.coreimpl.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppPreferences;
import fr.mby.portal.core.IPortalRenderer;
import fr.mby.portal.core.app.IAppFactory;
import fr.mby.portal.core.app.IAppSigner;
import fr.mby.portal.core.preferences.IAppPreferencesManager;
import fr.mby.portal.core.session.ISessionManager;
import fr.mby.portal.coreimpl.context.AppConfigNotFoundException;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAppFactory implements IAppFactory {

	/** Logger. */
	private static final Logger LOG = LogManager.getLogger(BasicAppFactory.class);

	/** Request build informations param name. */
	private static final String REQUEST_BUILD_INFO = "REQUEST_BUILD_INFO";

	@Autowired
	private ISessionManager sessionManager;

	@Autowired
	private IAppSigner appSigner;

	@Autowired
	private IAppPreferencesManager appPreferencesManager;

	@Override
	public IApp build(final HttpServletRequest request, final IAppConfig appConfig) {
		Assert.notNull(request, "No HttpServletRequest supplied !");
		Assert.notNull(appConfig, "No IAppConfig supplied !");

		final BasicApp app = new BasicApp(appConfig);

		try {
			final IAppPreferences appPrefs = this.appPreferencesManager.load(app);
			app.setPreferences(appPrefs);
		} catch (final AppConfigNotFoundException e) {
			throw new IllegalStateException(e);
		}

		final String namespace = this.generateNamespace(request, appConfig);
		app.setNamespace(namespace);

		final String signatrue = this.appSigner.generateSignature(request, app);
		app.setSignature(signatrue);

		final String portalSessionId = this.sessionManager.getPortalSessionId(request);

		try {
			final String encodedSignature = URLEncoder.encode(signatrue, "UTF-8");
			final String encodedPortalSessionId = URLEncoder.encode(portalSessionId, "UTF-8");

			final StringBuilder webPathBuilder = new StringBuilder(64);
			webPathBuilder.append(appConfig.getContext().getWebContextPath());

			// Add signature to IApp webPath
			webPathBuilder.append("/?");
			webPathBuilder.append(IPortalRenderer.SIGNATURE_PARAM_NAME);
			webPathBuilder.append("=");
			webPathBuilder.append(encodedSignature);

			// Add portal session to IApp webPath
			webPathBuilder.append("&");
			webPathBuilder.append(IPortalRenderer.PORTAL_SESSION_ID_PARAM_NAME);
			webPathBuilder.append("=");
			webPathBuilder.append(encodedPortalSessionId);

			app.setWebPath(webPathBuilder.toString());
		} catch (final UnsupportedEncodingException e) {
			BasicAppFactory.LOG.error("Error while URL encoding IApp signature !", e);
		}

		// Display
		app.setTitle(appConfig.getDefaultTitle());
		app.setWidth(appConfig.getDefaultWidth());
		app.setHeight(appConfig.getDefaultHeight());

		return app;
	}

	/**
	 * @param request
	 * @param appConfig
	 * @return
	 */
	protected String generateNamespace(final HttpServletRequest request, final IAppConfig appConfig) {
		BuildInfo buildInfo = (BuildInfo) request.getAttribute(BasicAppFactory.REQUEST_BUILD_INFO);

		if (buildInfo == null) {
			buildInfo = new BuildInfo();
			request.setAttribute(BasicAppFactory.REQUEST_BUILD_INFO, buildInfo);
		}

		final String sn = appConfig.getSymbolicName();

		final String namespace = sn + "_" + buildInfo.nextNamespaceCount(sn);

		return namespace;
	}

	private class BuildInfo {

		private final Map<String, Integer> namespaceCount = new HashMap<String, Integer>(8);

		public int nextNamespaceCount(final String namespace) {
			Integer count = this.namespaceCount.get(namespace);

			if (count == null) {
				count = 0;
				this.namespaceCount.put(namespace, 1);
			} else {
				this.namespaceCount.put(namespace, count + 1);
			}

			return count;
		}
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
