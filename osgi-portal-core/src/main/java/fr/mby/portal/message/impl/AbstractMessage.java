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

import org.springframework.util.Assert;

import fr.mby.portal.action.IUserAction;
import fr.mby.portal.app.IAppContext;
import fr.mby.portal.app.IAppPreferences;
import fr.mby.portal.app.ISession;
import fr.mby.portal.message.IMessage;
import fr.mby.portal.session.ISessionManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public abstract class AbstractMessage implements IMessage {

	private final IAppContext appContext;
	private final ISessionManager sessionManager;
	private final IAppPreferences appPreferences;
	private final IUserAction userAction;

	/** Protected constructor. Use the factory. */
	protected AbstractMessage(final IAppContext appContext, final ISessionManager sessionManager,
			final IAppPreferences appPreferences, final IUserAction userAction) {
		super();

		Assert.notNull(appContext, "No IAppContext provided !");
		Assert.notNull(sessionManager, "No ISessionManager provided !");
		Assert.notNull(appPreferences, "No IAppPreferences provided !");
		Assert.notNull(userAction, "No IUserAction provided !");

		this.appContext = appContext;
		this.sessionManager = sessionManager;
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
		final ISession session = this.sessionManager.getAppSession(this.userAction, create);

		return session;
	}

	@Override
	public IAppPreferences getPreferences() {
		return this.appPreferences;
	}

}
