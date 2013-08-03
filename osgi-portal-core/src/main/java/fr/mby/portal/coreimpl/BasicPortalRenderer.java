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

package fr.mby.portal.coreimpl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppContext;
import fr.mby.portal.api.app.IPortalApp;
import fr.mby.portal.core.IPortalRenderer;
import fr.mby.portal.core.app.IAppConfigFactory;
import fr.mby.portal.core.app.IAppFactory;
import fr.mby.portal.core.app.IAppStore;
import fr.mby.portal.coreimpl.app.PortalAppReferenceListener;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicPortalRenderer implements IPortalRenderer, InitializingBean {

	/** All IAppReferences. */
	private Collection<IPortalApp> portalApps;

	private PortalAppReferenceListener portalAppReferenceListener;

	@Autowired
	private IAppConfigFactory appConfigFactory;

	@Autowired
	private IAppFactory appFactory;

	@Autowired
	private IAppStore appStore;

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		final PrintWriter writer = response.getWriter();

		if (this.portalApps != null) {
			for (final IPortalApp portalApp : this.portalApps) {
				// Nothing
				portalApp.processAction(null, null);
			}
		}

		writer.append("<!doctype html>\n<html>\n<head>\n</head>\n<body>\n");
		writer.append("<h1>OSGi Portal !</h1>\n");

		if (this.portalAppReferenceListener != null) {
			for (final ServiceReference<?> appRef : this.portalAppReferenceListener.getPortalAppReferences()) {
				final Bundle bundle = appRef.getBundle();
				final IAppConfig appConfig = this.appConfigFactory.build(bundle);

				// Each App is displayed 3 times
				for (int k = 0; k < 3; k++) {
					final IApp app = this.appFactory.build(request, appConfig);

					this.renderApp(writer, app);
				}
			}
		}

		writer.append("</body>\n</html>\n");
		writer.flush();
	}

	@Override
	public List<IApp> getAppsToRender(final HttpServletRequest request) throws Exception {
		final ArrayList<IApp> appsToRender = new ArrayList<IApp>(16);

		if (this.portalAppReferenceListener != null) {
			for (final ServiceReference<?> appRef : this.portalAppReferenceListener.getPortalAppReferences()) {
				final Bundle bundle = appRef.getBundle();
				final IAppConfig appConfig = this.appConfigFactory.build(bundle);

				// Each App is displayed 3 times
				for (int k = 0; k < 3; k++) {
					final IApp app = this.appFactory.build(request, appConfig);

					this.appStore.storeApp(app, request);

					appsToRender.add(app);
				}
			}
		}

		return appsToRender;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.appConfigFactory, "No IAppConfigFactory configured !");
		Assert.notNull(this.appFactory, "No IAppFactory configured !");
	}

	/**
	 * @param writer
	 * @param bundle
	 */
	protected void renderApp(final PrintWriter writer, final IApp app) {
		final IAppConfig appConfig = app.getConfig();
		final IAppContext appContext = appConfig.getContext();

		final String webBundlePath = appContext.getWebContextPath();

		if (StringUtils.hasText(webBundlePath)) {
			writer.append("<div id=\"");
			writer.append(app.getNamespace());
			writer.append("\">\n");

			final String symbolicName = appConfig.getSymbolicName();
			writer.append("<h2>");
			writer.append(symbolicName);
			writer.append("</h2>\n");

			writer.append("<iframe src=\"");
			writer.append(webBundlePath);
			writer.append("\" style=\"width: ");
			writer.append(app.getWidth());
			writer.append("; height: ");
			writer.append(app.getHeight());
			writer.append("\"></iframe>\n");

			writer.append("</div>\n");
		}
	}

	/**
	 * Getter of portalApps.
	 * 
	 * @return the portalApps
	 */
	public Collection<IPortalApp> getPortalApps() {
		return this.portalApps;
	}

	/**
	 * Setter of portalApps.
	 * 
	 * @param portalApps
	 *            the portalApps to set
	 */
	public void setPortalApps(final Collection<IPortalApp> portalApps) {
		this.portalApps = portalApps;
	}

	/**
	 * Getter of portalAppReferenceListener.
	 * 
	 * @return the portalAppReferenceListener
	 */
	public PortalAppReferenceListener getPortalAppReferenceListener() {
		return this.portalAppReferenceListener;
	}

	/**
	 * Setter of portalAppReferenceListener.
	 * 
	 * @param portalAppReferenceListener
	 *            the portalAppReferenceListener to set
	 */
	public void setPortalAppReferenceListener(final PortalAppReferenceListener portalAppReferenceListener) {
		this.portalAppReferenceListener = portalAppReferenceListener;
	}

	/**
	 * Getter of appConfigFactory.
	 * 
	 * @return the appConfigFactory
	 */
	public IAppConfigFactory getAppConfigFactory() {
		return this.appConfigFactory;
	}

	/**
	 * Setter of appConfigFactory.
	 * 
	 * @param appConfigFactory
	 *            the appConfigFactory to set
	 */
	public void setAppConfigFactory(final IAppConfigFactory appConfigFactory) {
		this.appConfigFactory = appConfigFactory;
	}

	/**
	 * Getter of appFactory.
	 * 
	 * @return the appFactory
	 */
	public IAppFactory getAppFactory() {
		return this.appFactory;
	}

	/**
	 * Setter of appFactory.
	 * 
	 * @param appFactory
	 *            the appFactory to set
	 */
	public void setAppFactory(final IAppFactory appFactory) {
		this.appFactory = appFactory;
	}

	/**
	 * Getter of appStore.
	 * 
	 * @return the appStore
	 */
	public IAppStore getAppStore() {
		return this.appStore;
	}

	/**
	 * Setter of appStore.
	 * 
	 * @param appStore
	 *            the appStore to set
	 */
	public void setAppStore(final IAppStore appStore) {
		this.appStore = appStore;
	}

}
