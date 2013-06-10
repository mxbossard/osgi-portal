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

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.app.IAppStore;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class SessionAppStore implements IAppStore {

	/** TODO: replace the map by a cache. */
	private Map<String, AppStore> appStoreBySession;

	@Override
	public void storeApp(final IApp app, final HttpServletRequest request) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.core.app.IAppStore#retrieveApp(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public IApp retrieveApp(final HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
