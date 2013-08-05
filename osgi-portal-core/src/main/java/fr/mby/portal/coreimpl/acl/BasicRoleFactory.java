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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IRoleFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicRoleFactory implements IRoleFactory {

	private final Map<String, IRole> rolesCache = new HashMap<String, IRole>(128);

	@Override
	public IRole build(final String name) {
		Assert.hasText(name, "No name provided !");

		final IRole newRole = this.rolesCache.get(name);

		Assert.notNull(newRole, "This role wasn't initialized !");
		
		return newRole;
	}
	
	@Override
	public IRole initializeRole(final String name, final Set<IPermission> permissions, final Set<IRole> subRoles) {
		Assert.hasText(name, "No name provided !");
		Assert.notEmpty(permissions, "No permission provided !");

		final BasicRole newRole = new BasicRole(name);
		newRole.setPermissions(Collections.unmodifiableSet(permissions));
		
		if(subRoles != null) {
			newRole.setSubRoles(Collections.unmodifiableSet(subRoles));
		} else {
			newRole.setSubRoles(Collections.<IRole> emptySet());
		}
		
		this.rolesCache.put(name, newRole);

		return newRole;
	}

}
