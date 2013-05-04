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
package fr.mby.portal.message.impl;

import fr.mby.portal.action.IUserAction;
import fr.mby.portal.app.IAppContext;
import fr.mby.portal.app.IAppPreferences;
import fr.mby.portal.app.IAppSession;
import fr.mby.portal.message.IMessage;

/**
 * @author Maxime Bossard - 2013
 *
 */
public abstract class AbstractMessage implements IMessage {

	/* (non-Javadoc)
	 * @see fr.mby.portal.message.IMessage#getUserAction()
	 */
	@Override
	public IUserAction getUserAction() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.message.IMessage#getAppContext()
	 */
	@Override
	public IAppContext getAppContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.message.IMessage#getAppSession(boolean)
	 */
	@Override
	public IAppSession getAppSession(boolean create) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.message.IMessage#getPreferences()
	 */
	@Override
	public IAppPreferences getPreferences() {
		// TODO Auto-generated method stub
		return null;
	}

}
