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

package fr.mby.portal.core.session;

import javax.servlet.http.HttpServletRequest;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.app.ISession;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface ISessionManager {

	/**
	 * Retrieve the App Session corresponding to the user action. An App Session must be App private : visible only to
	 * the App.
	 * 
	 * @param userAction
	 * @param create
	 * @return
	 */
	ISession getAppSession(IUserAction userAction, boolean create);

	/**
	 * Retrieve the Portal Session corresponding to the request. A Portal Session must be Portal private : no external
	 * App shoud access it.
	 * 
	 * @param request
	 * @return
	 */
	ISession getPortalSession(HttpServletRequest request);

	/**
	 * Retrieve the unique portal session Id corresponding to the request.
	 * 
	 * @param request
	 * @return
	 */
	String getPortalSessionId(HttpServletRequest request);

	/**
	 * Destroy all sessions attached to the request.
	 * 
	 * @param request
	 */
	void destroySessions(HttpServletRequest request);

}
