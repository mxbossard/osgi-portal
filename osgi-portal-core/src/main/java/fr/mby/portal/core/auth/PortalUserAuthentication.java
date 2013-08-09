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

package fr.mby.portal.core.auth;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.coreimpl.security.PortalUserPrincipal;

/**
 * Basic IAuthentication with username and password for Portal User.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public class PortalUserAuthentication implements IAuthentication {

	/** Svuid. */
	private static final long serialVersionUID = 3494190370067218476L;

	private final Principal principal;

	private final String credentials;

	private final boolean authenticated;

	private Set<IRole> roles;

	/**
	 * Normal constructor of PortalUserAuthentication.
	 * 
	 * @param principal
	 * @param credentials
	 */
	public PortalUserAuthentication(final String username, final String password) {
		super();

		Assert.hasText(username, "No username supplied !");
		Assert.hasText(password, "No password supplied !");

		this.principal = new PortalUserPrincipal(username);
		this.credentials = password;
		this.authenticated = false;
	}

	/**
	 * Constructor which should only be used internally for authenticated Principal.
	 * 
	 * @param principal
	 * @param credentials
	 */
	public PortalUserAuthentication(final String username, final String password, final Set<IRole> roles) {
		super();

		Assert.hasText(username, "No username supplied !");
		Assert.hasText(password, "No password supplied !");

		this.principal = new PortalUserPrincipal(username);
		this.credentials = password;

		if (roles != null) {
			this.roles = roles;
		} else {
			this.roles = Collections.emptySet();
		}

		this.authenticated = true;
	}

	@Override
	public String getName() {
		return this.getPrincipal().getName();
	}

	/**
	 * Getter of principal.
	 * 
	 * @return the principal
	 */
	@Override
	public Principal getPrincipal() {
		return this.principal;
	}

	/**
	 * Getter of credentials.
	 * 
	 * @return the credentials
	 */
	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	/**
	 * Getter of authenticated.
	 * 
	 * @return the authenticated
	 */
	@Override
	public boolean isAuthenticated() {
		return this.authenticated;
	}

	/**
	 * Getter of roles.
	 * 
	 * @return the roles
	 */
	@Override
	public Set<IRole> getRoles() {
		return this.roles;
	}

	@Override
	public String toString() {
		return "PortalUserAuthentication [principal=" + this.principal + ", authenticated=" + this.authenticated
				+ ", roles=" + this.roles + "]";
	}

}
