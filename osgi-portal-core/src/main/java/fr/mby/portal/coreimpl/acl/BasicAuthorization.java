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

import java.util.HashSet;
import java.util.Set;

import fr.mby.portal.api.acl.IAuthorization;
import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicAuthorization implements IAuthorization {

	private final Set<IPermission> permissions = new HashSet<IPermission>(8);

	/** */
	public BasicAuthorization() {
		super();
	}

	/** */
	public BasicAuthorization(final Set<IRole> roles) {
		super();

		if (roles != null) {
			for (final IRole role : roles) {
				this.addRole(role);
			}
		}
	}

	@Override
	public boolean isGranted(final IPermission permission) {
		return this.permissions.contains(permission);
	}

	public void addPermission(final IPermission permission) {
		if (permission != null) {
			this.permissions.add(permission);
		}
	}

	public void addRole(final IRole role) {
		if (role != null) {
			this.permissions.addAll(role.getPermissions());
			for (final IRole subRole : role.getSubRoles()) {
				this.addRole(subRole);
			}
		}
	}

}
