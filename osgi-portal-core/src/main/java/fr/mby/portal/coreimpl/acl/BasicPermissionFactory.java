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

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.core.acl.IPermissionFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicPermissionFactory implements IPermissionFactory {

	private final Map<String, IPermission> permissionsCache = new HashMap<String, IPermission>(128);

	@Override
	public IPermission build(final String name) {
		Assert.hasText(name, "No name provided !");

		IPermission newPermission = this.permissionsCache.get(name);
		
		if (newPermission == null) {
			newPermission = new BasicPermission(name);
			this.permissionsCache.put(name, newPermission);
		}

		return newPermission;
	}

}
