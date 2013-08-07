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

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import fr.mby.portal.core.security.IPrincipalResolver;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
@Order(value = 100)
public class SessionPrincipalResolver implements IPrincipalResolver {

	public static final String PRINCIPAL_SESSION_ATTR_NAME = "Principal";

	@Override
	public Principal resolve(final HttpServletRequest request) {
		final Principal resolvedPrincipal = null;

		final HttpSession session = request.getSession(true);

		final Object principalInSession = session.getAttribute(SessionPrincipalResolver.PRINCIPAL_SESSION_ATTR_NAME);

		if (principalInSession != null && !Principal.class.isAssignableFrom(principalInSession.getClass())) {
			// If Principal in session got a bad type we remove it
			session.removeAttribute(SessionPrincipalResolver.PRINCIPAL_SESSION_ATTR_NAME);
		}

		return resolvedPrincipal;
	}

}
