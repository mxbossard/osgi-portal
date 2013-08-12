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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.app.IAppStore;
import fr.mby.portal.core.session.ISessionManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class SessionAppStore implements IAppStore, InitializingBean {

	/** TODO: replace the map by a cache. */
	private final Map<String, UserAppStore> userAppStoreBySession = new ConcurrentHashMap<String, UserAppStore>(16);

	@Autowired
	private ISessionManager sessionManager;

	@Override
	public synchronized void storeApp(final IApp app, final HttpServletRequest request) {
		final String sessionId = this.sessionManager.getPortalSessionId(request);

		UserAppStore userAppStore = this.userAppStoreBySession.get(sessionId);
		if (userAppStore == null) {
			// Init UserAppStore
			userAppStore = new UserAppStore();
			this.userAppStoreBySession.put(sessionId, userAppStore);
		}

		userAppStore.storeApp(app, request);

	}

	@Override
	public IApp retrieveApp(final HttpServletRequest request) {
		IApp retrievedApp = null;

		final String sessionId = this.sessionManager.getPortalSessionId(request);
		final UserAppStore userAppStore = this.userAppStoreBySession.get(sessionId);

		if (userAppStore != null) {
			retrievedApp = userAppStore.retrieveApp(request);
		}

		return retrievedApp;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.sessionManager, "No ISessionManager configured !");
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
