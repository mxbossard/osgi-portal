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
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.ISession;
import fr.mby.portal.core.auth.AuthenticationException;
import fr.mby.portal.core.auth.IAuthentication;
import fr.mby.portal.core.auth.IAuthenticationManager;
import fr.mby.portal.core.security.ILoginManager;
import fr.mby.portal.core.security.LoginException;
import fr.mby.portal.core.session.ISessionManager;
import fr.mby.portal.core.session.SessionNotInitializedException;

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
	public IAuthentication login(final HttpServletRequest request, final HttpServletResponse response,
			final IAuthentication authentication) throws LoginException {
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
			// request.getSession().invalidate();
			// request.getSession(true);

			final ISession portalSession;
			try {
				this.sessionManager.destroySessions(request, response);
				this.sessionManager.initPortalSession(request, response);
				portalSession = this.sessionManager.getPortalSession(request);
			} catch (final SessionNotInitializedException e) {
				throw new LoginException(authentication, e);
			}

			// Store principal in Portal Session
			portalSession.setAttribute(SessionPrincipalResolver.PRINCIPAL_SESSION_ATTR_NAME, resultingAuth);
		} else {
			throw new LoginException(authentication, "Supplied principal was not previously authenticared !");
		}

		return resultingAuth;
	}

	@Override
	public boolean isLogged(final HttpServletRequest request) {
		boolean authenticated = false;

		final ISession portalSession;
		try {
			portalSession = this.sessionManager.getPortalSession(request);
			final Object auth = portalSession.getAttribute(SessionPrincipalResolver.PRINCIPAL_SESSION_ATTR_NAME);
			authenticated = auth != null && IAuthentication.class.isAssignableFrom(auth.getClass())
					&& ((IAuthentication) auth).isAuthenticated();
		} catch (final SessionNotInitializedException e) {
			// Error while retrieving Session
			authenticated = false;
		}

		return authenticated;
	}

	@Override
	public IAuthentication getLoggedAuthentication(final HttpServletRequest request) {
		IAuthentication auth = null;

		if (this.isLogged(request)) {
			try {

				final ISession portalSession = this.sessionManager.getPortalSession(request);
				final Object authInSession = portalSession
						.getAttribute(SessionPrincipalResolver.PRINCIPAL_SESSION_ATTR_NAME);
				if (authInSession != null && IAuthentication.class.isAssignableFrom(authInSession.getClass())) {
					auth = (IAuthentication) portalSession
							.getAttribute(SessionPrincipalResolver.PRINCIPAL_SESSION_ATTR_NAME);
				}

			} catch (final SessionNotInitializedException e) {
				// Error while retrieving Session
			}
		}

		return auth;
	}

	@Override
	public void logout(final HttpServletRequest request, final HttpServletResponse response) {
		// Destroy Portal sessions and Http session
		this.sessionManager.destroySessions(request, response);
	}

}
