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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.app.IAppSigner;
import fr.mby.portal.core.app.IAppStore;
import fr.mby.portal.core.cache.ICachingService;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class UserAppStore implements IAppStore, ICachingService {

	/** TODO: need to clean the map : a cache ? */
	/** The user IApp are stored by signature. */
	private final Map<String, IApp> userAppStore = new ConcurrentHashMap<String, IApp>(16);

	@Autowired
	private IAppSigner appSigner;

	@Override
	public synchronized void storeApp(final IApp app, final HttpServletRequest request) {
		final String appSignature = app.getSignature();

		Assert.isTrue(!this.userAppStore.containsKey(appSignature),
				"UserAppStore cannot store an already stored IApp !");

		this.userAppStore.put(appSignature, app);

	}

	@Override
	public IApp retrieveApp(final HttpServletRequest request) {
		final String appSignature = this.appSigner.retrieveSignature(request);

		Assert.hasText(appSignature, "No App signature found in Http request !");

		return this.userAppStore.get(appSignature);
	}

	@Override
	public void clearCache() {
		this.userAppStore.clear();
	}

}
