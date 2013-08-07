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

package fr.mby.portal.coreimpl.message;

import org.springframework.util.Assert;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.app.IAppContext;
import fr.mby.portal.api.app.IAppPreferences;
import fr.mby.portal.api.app.ISession;
import fr.mby.portal.api.message.IMessage;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public abstract class AbstractMessage implements IMessage {

	private final IAppContext appContext;
	private final ISession appSession;
	private final IAppPreferences appPreferences;
	private final IUserAction userAction;

	/** Protected constructor. Use the factory. */
	protected AbstractMessage(final IAppContext appContext, final ISession appSession,
			final IAppPreferences appPreferences, final IUserAction userAction) {
		super();

		Assert.notNull(appContext, "No IAppContext provided !");
		Assert.notNull(appSession, "No AppSession provided !");
		Assert.notNull(appPreferences, "No IAppPreferences provided !");
		Assert.notNull(userAction, "No IUserAction provided !");

		this.appContext = appContext;
		this.appSession = appSession;
		this.appPreferences = appPreferences;
		this.userAction = userAction;
	}

	@Override
	public IUserAction getUserAction() {
		return this.userAction;
	}

	@Override
	public IAppContext getAppContext() {
		return this.appContext;
	}

	@Override
	public ISession getAppSession(final boolean create) {
		return this.appSession;
	}

	@Override
	public IAppPreferences getPreferences() {
		return this.appPreferences;
	}

}
