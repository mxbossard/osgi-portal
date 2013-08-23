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

package fr.mby.portal.coreimpl.preferences;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppPreferences;
import fr.mby.portal.core.cache.ICachingService;
import fr.mby.portal.core.preferences.IAppPreferencesManager;
import fr.mby.portal.coreimpl.context.AppConfigNotFoundException;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class CachingAppPreferencesManager implements IAppPreferencesManager, ICachingService {

	private final Map<IAppConfig, IAppPreferences> defaultCache = new WeakHashMap<IAppConfig, IAppPreferences>(8);

	private final Map<IApp, IAppPreferences> appCache = new WeakHashMap<IApp, IAppPreferences>(8);

	@Override
	public void init(final IAppConfig appConfig, final Map<String, String[]> preferencesMap) {
		Assert.notNull(appConfig, "No IAppConfig supplied !");
		Assert.notNull(preferencesMap, "No IAppConfig supplied !");

		IAppPreferences prefs = this.defaultCache.get(appConfig);
		if (prefs == null) {
			prefs = new BasicAppPreferences(preferencesMap);
			this.defaultCache.put(appConfig, prefs);
		}

	}

	@Override
	public IAppPreferences load(final IApp app) throws AppConfigNotFoundException {
		Assert.notNull(app, "No IApp supplied !");

		IAppPreferences appPrefs = this.appCache.get(app);
		if (appPrefs == null) {
			final IAppConfig appConfig = app.getConfig();
			final IAppPreferences defaultPrefs = this.defaultCache.get(appConfig);
			if (defaultPrefs == null) {
				final String message = String.format("No default preferences found attached to the IAppConfig [%1$s] !"
						+ "Preferences should be initialized whith IAppConfig !", appConfig.getSymbolicName());
				throw new AppConfigNotFoundException(message);
			}
			appPrefs = new BasicAppPreferences(defaultPrefs.getMap());
			this.appCache.put(app, appPrefs);
		}

		return appPrefs;
	}

	@Override
	public IAppPreferences update(final IApp app, final Map<String, String[]> preferencesMap)
			throws AppConfigNotFoundException {
		Assert.notNull(app, "No IApp supplied !");
		Assert.notNull(preferencesMap, "No preferences map supplied !");

		final IAppPreferences appPrefs = this.load(app);
		if (appPrefs == null) {
			final IAppConfig appConfig = app.getConfig();
			final String message = String.format("No default preferences found attached to the IAppConfig [%1$s] !"
					+ "Preferences should be initialized whith IAppConfig !", appConfig.getSymbolicName());
			throw new AppConfigNotFoundException(message);
		}

		// Update the map
		final Map<String, String[]> mergeMap = new HashMap<String, String[]>(appPrefs.getMap());
		mergeMap.putAll(preferencesMap);

		final BasicAppPreferences updatedPrefs = new BasicAppPreferences(mergeMap);
		this.appCache.put(app, updatedPrefs);

		return updatedPrefs;
	}

	@Override
	public void clearCache() {
		this.defaultCache.clear();
		this.appCache.clear();
	}

}
