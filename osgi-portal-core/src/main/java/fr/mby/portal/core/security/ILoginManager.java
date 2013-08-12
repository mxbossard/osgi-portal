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

package fr.mby.portal.core.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.mby.portal.core.auth.IAuthentication;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface ILoginManager {

	/**
	 * Perform a login to the Portal.
	 * 
	 * @param request
	 *            the request which is the origin of the login.
	 * @param response
	 *            the response returned on login action.
	 * @param authentication
	 *            the IAuthentication object wich must already be authenticated.
	 * @return the IAuthentication wich allowed the login to be performed.
	 * @throws LoginException
	 *             when somethig goes wrong.
	 */
	IAuthentication login(HttpServletRequest request, HttpServletResponse response, IAuthentication authentication)
			throws LoginException;

	/**
	 * Test if the Http request was sent by a logged user.
	 * 
	 * @param request
	 *            Http request
	 * @return true if the user is logged.
	 */
	boolean isLogged(HttpServletRequest request);

	/**
	 * Perform a logout from the Portal.
	 * 
	 * @param request
	 *            the Http request origin of the logout.
	 * @param response
	 *            the response returned on logout action.
	 */
	void logout(HttpServletRequest request, HttpServletResponse response);

}
