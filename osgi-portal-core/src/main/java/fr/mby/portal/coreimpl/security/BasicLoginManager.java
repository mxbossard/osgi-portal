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

package fr.mby.portal.coreimpl.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.ISession;
import fr.mby.portal.core.auth.AuthenticationException;
import fr.mby.portal.core.auth.IAuthentication;
import fr.mby.portal.core.auth.IAuthenticationManager;
import fr.mby.portal.core.security.ILoginManager;
import fr.mby.portal.core.security.LoginException;
import fr.mby.portal.core.session.ISessionManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicLoginManager implements ILoginManager {

	@Autowired(required = true)
	private ISessionManager sessionManager;

	@Autowired(required = true)
	private IAuthenticationManager authenticationManager;

	@Override
	public void login(final HttpServletRequest request, final IAuthentication authentication) throws LoginException {
		Assert.notNull(request, "No Http request supplied !");
		Assert.notNull(authentication, "No IAuthentication supplied !");

		final IAuthentication resultingAuth;
		try {
			resultingAuth = this.authenticationManager.authenticate(authentication);
		} catch (final AuthenticationException e) {
			throw new LoginException(authentication, e);
		}

		if (resultingAuth != null && resultingAuth.isAuthenticated()) {
			// Invalidate current session a rebuild a new one
			request.getSession().invalidate();
			request.getSession(true);

			// Store principal in Portal Session
			final ISession portalSession = this.sessionManager.getPortalSession(request);
			portalSession.setAttribute(SessionPrincipalResolver.PRINCIPAL_SESSION_ATTR_NAME, authentication);
		} else {
			throw new LoginException(authentication, "Supplied principal was not previously authenticared !");
		}
	}

	@Override
	public void logout(final HttpServletRequest request) {
		// Destroy Portal sessions and Http session
		this.sessionManager.destroySessions(request);
		request.getSession().invalidate();
	}

}
