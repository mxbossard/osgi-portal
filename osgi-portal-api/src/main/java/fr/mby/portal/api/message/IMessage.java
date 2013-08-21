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

package fr.mby.portal.api.message;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.ISession;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IMessage {

	public enum MessageType {
		ACTION, RENDER, EVENT;
	}

	/**
	 * Returns the user action origin of this application action.
	 * 
	 * @return the user action
	 */
	IUserAction getUserAction();

	/**
	 * Returns the instance of IApp targeted by this message.
	 * 
	 * @return the current IApp instance.
	 */
	IApp getTargetedApp();

	/**
	 * Returns the current app session or, if there is no current session and the given flag is true, creates one and
	 * returns the new session. If the given flag is false and there is no current app session, this method returns
	 * null. Creating a new portlet session will result in creating a new HttpSession on which the app session is based
	 * on.
	 * 
	 * @return the app session
	 */
	ISession getAppSession();

}
