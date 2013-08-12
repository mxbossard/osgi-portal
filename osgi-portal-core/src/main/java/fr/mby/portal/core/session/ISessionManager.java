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
import javax.servlet.http.HttpServletResponse;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.ISession;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface ISessionManager {

	/**
	 * Retrieve the App Session corresponding to the HTTP request. An App Session must be App private : visible only to
	 * the App.
	 * 
	 * @param request
	 * @param create
	 * @return
	 */
	ISession getAppSession(HttpServletRequest request, boolean create);

	/**
	 * Retrieve the App Session corresponding to an App. An App Session must be App private : visible only to the App.
	 * 
	 * @param app
	 * @param create
	 * @return
	 */
	ISession getAppSession(IApp app, boolean create);

	/**
	 * Retrieve the Shared Session corresponding to the HTTP request.
	 * 
	 * @param request
	 * @param create
	 * @return
	 * @throws SessionNotInitializedException
	 *             if trying to get a not Initialized session.
	 */
	ISession getSharedSession(HttpServletRequest request) throws SessionNotInitializedException;

	/**
	 * Retrieve the Portal Session corresponding to the request. A Portal Session must be Portal private : no external
	 * App shoud access it.
	 * 
	 * @param request
	 * @return
	 * @throws SessionNotInitializedException
	 *             if trying to get a not Initialized session.
	 */
	ISession getPortalSession(HttpServletRequest request) throws SessionNotInitializedException;

	/**
	 * Retrieve the unique portal session Id corresponding to the request.
	 * 
	 * @param request
	 * @return the sessionId or null if session not initialized.
	 */
	String getPortalSessionId(HttpServletRequest request);

	/**
	 * Initialize the portal Session.
	 * 
	 * @param request
	 * @param response
	 */
	void initPortalSession(HttpServletRequest request, HttpServletResponse response);

	/**
	 * Destroy all sessions attached to the request.
	 * 
	 * @param request
	 */
	void destroySessions(HttpServletRequest request, HttpServletResponse response);

}
