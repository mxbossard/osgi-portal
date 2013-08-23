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

package fr.mby.portal.core.preferences;

import java.util.Map;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppPreferences;
import fr.mby.portal.coreimpl.context.AppConfigNotFoundException;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IAppPreferencesManager {

	/**
	 * Initialize the IAppPreferences of an IAppConfig instance.
	 * 
	 * @param appConfig
	 * @param preferencesMap
	 * @return the convenient IAppPreferences.
	 */
	void init(IAppConfig appConfig, Map<String, String[]> preferencesMap);

	/**
	 * Load an IAppPreferences.
	 * 
	 * @param app
	 * @return the correspondant IAppPreferences, cannot be null.
	 * @throws AppConfigNotFoundException
	 */
	IAppPreferences load(IApp app) throws AppConfigNotFoundException;

	/**
	 * Save IAppPreferences modifications.
	 * 
	 * @param appPreferences
	 * @param preferencesMap
	 * @return the correspondant IAppPreferences, cannot be null.
	 * @throws AppConfigNotFoundException
	 */
	IAppPreferences update(IApp app, Map<String, String[]> preferencesMap) throws AppConfigNotFoundException;

}
