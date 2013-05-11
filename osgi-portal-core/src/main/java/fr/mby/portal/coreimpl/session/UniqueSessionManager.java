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

package fr.mby.portal.coreimpl.session;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import fr.mby.portal.action.IUserAction;
import fr.mby.portal.app.ISession;
import fr.mby.portal.core.session.ISessionManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class UniqueSessionManager implements ISessionManager, InitializingBean {

	private ISession uniqueSession;

	@Override
	public ISession getAppSession(IUserAction userAction, boolean create) {
		return this.uniqueSession;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.uniqueSession = new BasicSession();
	}

}
