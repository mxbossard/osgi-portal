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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.IPortalService;
import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.app.IAppStore;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicPortalService implements IPortalService {

	@Autowired
	private IAppStore appStore;

	@Override
	public IApp getTargetedApp(final HttpServletRequest request) {
		final IApp app = this.appStore.retrieveApp(request);

		Assert.state(app != null, "IApp should exists in the store !");

		return app;
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
