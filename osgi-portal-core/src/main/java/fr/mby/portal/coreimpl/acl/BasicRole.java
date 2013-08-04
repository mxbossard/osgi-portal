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

package fr.mby.portal.coreimpl.acl;

import java.util.Iterator;
import java.util.Set;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicRole extends BasicPermission implements IRole {

	private Set<IPermission> permissions;

	private Set<IRole> subRoles;

	/** Protected constructor. */
	protected BasicRole(final String name) {
		super(name);
	}

	@Override
	public boolean isGranted(final IPermission permission) {
		boolean test = false;

		if (this.permissions != null) {
			final Iterator<IPermission> perIt = this.permissions.iterator();
			while (!test && perIt.hasNext()) {
				final IPermission perm = perIt.next();
				test = perm != null && perm.equals(permission);
			}
		}

		if (this.subRoles != null) {
			final Iterator<IRole> rolIt = this.subRoles.iterator();
			while (!test && rolIt.hasNext()) {
				final IRole subRole = rolIt.next();
				test = subRole != null && subRole.isGranted(permission);
			}
		}

		return test;
	}

	/**
	 * Getter of permissions.
	 * 
	 * @return the permissions
	 */
	@Override
	public Set<IPermission> getPermissions() {
		return this.permissions;
	}

	/**
	 * Setter of permissions.
	 * 
	 * @param permissions
	 *            the permissions to set
	 */
	public void setPermissions(final Set<IPermission> permissions) {
		this.permissions = permissions;
	}

	/**
	 * Getter of subRoles.
	 * 
	 * @return the subRoles
	 */
	@Override
	public Set<IRole> getSubRoles() {
		return this.subRoles;
	}

	/**
	 * Setter of subRoles.
	 * 
	 * @param subRoles
	 *            the subRoles to set
	 */
	public void setSubRoles(final Set<IRole> subRoles) {
		this.subRoles = subRoles;
	}

}
