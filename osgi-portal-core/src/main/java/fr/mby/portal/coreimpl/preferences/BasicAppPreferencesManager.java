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

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppPreferences;
import fr.mby.portal.core.preferences.IAppPreferencesManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAppPreferencesManager implements IAppPreferencesManager {

	@Override
	public IAppPreferences getAppPreferences(final IAppConfig appConfig) {
		Assert.notNull(appConfig, "No IAppConfig supplied !");
		return new BasicAppPreferences(appConfig.getDefaultPreferences().getMap());
	}

	@Override
	public void storeAppPreferences(final IApp app, final IAppPreferences appPreferences) {
		// TODO Auto-generated method stub

	}

}
