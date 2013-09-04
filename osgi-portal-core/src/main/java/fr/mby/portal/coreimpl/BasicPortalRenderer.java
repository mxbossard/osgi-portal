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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IAuthorization;
import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppConfig.SpecialPermission;
import fr.mby.portal.api.app.IAppConfig.SpecialRole;
import fr.mby.portal.api.app.IPortalApp;
import fr.mby.portal.core.IPortalRenderer;
import fr.mby.portal.core.acl.IPermissionFactory;
import fr.mby.portal.core.app.IAppConfigFactory;
import fr.mby.portal.core.app.IAppFactory;
import fr.mby.portal.core.app.IAppStore;
import fr.mby.portal.core.auth.IAuthentication;
import fr.mby.portal.core.security.ILoginManager;
import fr.mby.portal.coreimpl.app.PortalAppReferenceListener;
import fr.mby.portal.coreimpl.context.AppConfigNotFoundException;
import fr.mby.portal.coreimpl.context.BadAppConfigException;

/**
 * Basic rendering : render all app the user is authorized to render.
 * 
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

	@Autowired
	private ILoginManager loginManager;

	@Autowired
	private IPermissionFactory permissionFactory;

	@Override
	public List<IApp> getAppsToRender(final HttpServletRequest request) throws Exception {
		final ArrayList<IApp> appsToRender = new ArrayList<IApp>(16);

		final IAuthentication userAuth = this.loginManager.getLoggedAuthentication(request);

		final Set<IAppConfig> startedAppConfigs = this.getStartedAppConfigs();
		for (final IAppConfig appConfig : startedAppConfigs) {
			final boolean canRenderApp = this.canRenderApp(userAuth, appConfig);

			if (canRenderApp) {
				final IApp app = this.appFactory.build(request, appConfig);
				this.appStore.storeApp(app, request);
				appsToRender.add(app);
			}
		}

		return appsToRender;
	}

	/**
	 * Retrieve the unordered set of all IAppConfig of started OPA.
	 * 
	 * @return
	 * @throws AppConfigNotFoundException
	 * @throws BadAppConfigException
	 */
	protected Set<IAppConfig> getStartedAppConfigs() throws AppConfigNotFoundException, BadAppConfigException {
		final Set<IAppConfig> appConfigs = new HashSet<IAppConfig>(16);

		if (this.portalAppReferenceListener != null) {

			for (final ServiceReference appRef : this.portalAppReferenceListener.getPortalAppReferences()) {
				final Bundle bundle = appRef.getBundle();
				final IAppConfig appConfig = this.appConfigFactory.build(bundle);

				appConfigs.add(appConfig);
			}
		}

		return appConfigs;
	}

	/**
	 * Test if user authentication is allowed to render the AppConfig. Check App special roles GUEST and LOGGED. Check
	 * User roles.
	 * 
	 * @param userAuth
	 * @param appConfig
	 * @return true if allowed.
	 */
	protected boolean canRenderApp(final IAuthentication userAuth, final IAppConfig appConfig) {
		// User roles
		IAuthorization userAuthorizations = null;
		if (userAuth != null) {
			userAuthorizations = userAuth.getAuthorization();
		}

		// Special roles
		final IAuthorization specialAuthorizations;
		if (userAuth != null && userAuth.isAuthenticated()) {
			// User is logged
			specialAuthorizations = appConfig.getSpecialRole(SpecialRole.LOGGED);
		} else {
			// User is a guest
			specialAuthorizations = appConfig.getSpecialRole(SpecialRole.GUEST);
		}

		// Retrieve the App render permission
		final IPermission canRenderPerm = appConfig.getSpecialPermission(SpecialPermission.CAN_RENDER);

		final boolean canRenderApp = specialAuthorizations.isGranted(canRenderPerm) || userAuthorizations != null
				&& userAuthorizations.isGranted(canRenderPerm);
		return canRenderApp;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.appConfigFactory, "No IAppConfigFactory configured !");
		Assert.notNull(this.appFactory, "No IAppFactory configured !");
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
