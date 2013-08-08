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

import java.security.Principal;

import fr.mby.portal.core.acl.AbstractPortalException;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class LoginException extends AbstractPortalException {

	/** Svuid. */
	private static final long serialVersionUID = 3142935384489443263L;

	private final Principal principal;

	/**
	 * @param message
	 * @param cause
	 */
	public LoginException(final Principal principal, final Throwable cause) {
		super(cause);

		this.principal = principal;
	}

	/**
	 * @param role
	 */
	public LoginException(final Principal principal, final String message) {
		super(String.format("Unable to perform login for Principal: [%1$s] ! Error messages is: [%2$s]",
				principal.getName(), message));
		this.principal = principal;
	}

	/**
	 * Getter of principal.
	 * 
	 * @return the principal
	 */
	public Principal getPrincipal() {
		return this.principal;
	}

}
