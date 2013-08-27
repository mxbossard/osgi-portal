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
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.app.IAppSigner;
import fr.mby.portal.core.app.IAppStore;
import fr.mby.portal.core.cache.ICachingService;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class MemoryAppStore implements IAppStore, ICachingService {

	/** TODO: replace the map by a cache. */
	private final Map<String, IApp> appStore = new ConcurrentHashMap<String, IApp>(16);

	@Autowired
	private IAppSigner appSigner;

	@Override
	public synchronized void storeApp(final IApp app, final HttpServletRequest request) {
		final String signature = app.getSignature();

		if (!this.appStore.containsKey(signature)) {
			// Init UserAppStore
			this.appStore.put(signature, app);
		} else {
			throw new IllegalStateException("An IApp is already stored with this signature !");
		}

	}

	@Override
	public IApp retrieveApp(final HttpServletRequest request) {
		final String signature = this.appSigner.retrieveSignature(request);

		Assert.hasText(signature, "No App signature found in Http request !");

		final IApp retrievedApp = this.appStore.get(signature);

		return retrievedApp;
	}

	@Override
	public void clearCache() {
		this.appStore.clear();
	}
}
