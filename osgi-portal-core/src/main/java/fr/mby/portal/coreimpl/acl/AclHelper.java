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

import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public abstract class AclHelper {

	public static Set<IRole> buileRoleSet(final IRole... roles) {
		Assert.notNull(roles, "No roles supplied !");
		final Set<IRole> set = new HashSet<IRole>(roles.length);
		for (final IRole role : roles) {
			if (role != null) {
				set.add(role);
			}
		}
		return set;
	}

	public static Set<IPermission> builePermissionSet(final IPermission... permissions) {
		Assert.notNull(permissions, "No permissions supplied !");
		final Set<IPermission> set = new HashSet<IPermission>(permissions.length);
		for (final IPermission perm : permissions) {
			if (perm != null) {
				set.add(perm);
			}
		}
		return set;
	}

}
