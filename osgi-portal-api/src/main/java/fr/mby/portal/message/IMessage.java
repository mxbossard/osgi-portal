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
package fr.mby.portal.message;

import fr.mby.portal.action.IUserAction;
import fr.mby.portal.app.IAppContext;
import fr.mby.portal.app.IAppPreferences;
import fr.mby.portal.app.ISession;

/**
 * @author Maxime Bossard - 2013
 *
 */
public interface IMessage {

	public enum MessageType {
		ACTION,
		RENDER,
		EVENT;
	}
	
	/**
	 * Returns the user action origin of this application action.
	 * 
	 * @return the user action
	 */
	IUserAction getUserAction();
	
	
	IAppContext getAppContext();
	
	/**
	 * Returns the current app session or, if there is no current session and the given flag is true, creates one and returns the new session.
	 * If the given flag is false and there is no current app session, this method returns null.
	 * Creating a new portlet session will result in creating a new HttpSession on which the app session is based on.
	 * 
	 * @param create - true to create a new session, false to return null if there is no current session
	 * @return the app session
	 */
	ISession getAppSession(boolean create);
	
	/**
	 * Returns the preferences object associated with the app.
	 * 
	 * @return the app preferences
	 */
	IAppPreferences getPreferences();
	
}
