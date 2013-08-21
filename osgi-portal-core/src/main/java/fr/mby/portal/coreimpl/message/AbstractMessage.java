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
import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.ISession;
import fr.mby.portal.api.message.IMessage;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public abstract class AbstractMessage implements IMessage {

	private IApp targetedApp;

	private ISession appSession;

	private final IUserAction userAction;

	/** Protected constructor. Use the factory. */
	protected AbstractMessage(final IUserAction userAction) {
		super();

		Assert.notNull(userAction, "No IUserAction provided !");

		this.userAction = userAction;
	}

	@Override
	public IUserAction getUserAction() {
		return this.userAction;
	}

	/**
	 * Getter of targetedApp.
	 * 
	 * @return the targetedApp
	 */
	@Override
	public IApp getTargetedApp() {
		return this.targetedApp;
	}

	/**
	 * Setter of targetedApp.
	 * 
	 * @param targetedApp
	 *            the targetedApp to set
	 */
	public void setTargetedApp(final IApp targetedApp) {
		this.targetedApp = targetedApp;
	}

	/**
	 * Getter of appSession.
	 * 
	 * @return the appSession
	 */
	@Override
	public ISession getAppSession() {
		return this.appSession;
	}

	/**
	 * Setter of appSession.
	 * 
	 * @param appSession
	 *            the appSession to set
	 */
	public void setAppSession(final ISession appSession) {
		this.appSession = appSession;
	}

}
