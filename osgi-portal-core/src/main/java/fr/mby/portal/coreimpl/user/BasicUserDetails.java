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

package fr.mby.portal.coreimpl.user;

import java.security.Principal;
import java.util.Map;
import java.util.Set;

import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.api.user.IUserDetails;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicUserDetails implements IUserDetails {

	private Principal principal;

	private boolean authenticated;

	private Set<IRole> roles;

	private Map<String, String> details;

	/** Protected constructor. */
	protected BasicUserDetails() {
		super();
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
	 * Setter of principal.
	 * 
	 * @param principal
	 *            the principal to set
	 */
	public void setPrincipal(final Principal principal) {
		this.principal = principal;
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
	 * Setter of authenticated.
	 * 
	 * @param authenticated
	 *            the authenticated to set
	 */
	public void setAuthenticated(final boolean authenticated) {
		this.authenticated = authenticated;
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

	/**
	 * Setter of roles.
	 * 
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(final Set<IRole> roles) {
		this.roles = roles;
	}

	/**
	 * Getter of details.
	 * 
	 * @return the details
	 */
	@Override
	public Map<String, String> getDetails() {
		return this.details;
	}

	/**
	 * Setter of details.
	 * 
	 * @param details
	 *            the details to set
	 */
	public void setDetails(final Map<String, String> details) {
		this.details = details;
	}

}
