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

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IAclDao;
import fr.mby.portal.core.acl.IAclManager;
import fr.mby.portal.core.acl.IPermissionFactory;
import fr.mby.portal.core.acl.IRoleFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAclManager implements IAclManager {

	@Autowired(required = true)
	private IRoleFactory roleFactory;

	@Autowired(required = true)
	private IPermissionFactory permissionFactory;

	@Autowired(required = true)
	private IAclDao aclDao;

	private final Map<String, IRole> rolesCache = new HashMap<String, IRole>(128);

	private final Map<String, IPermission> permissionsCache = new HashMap<String, IPermission>(128);

	@Override
	public Set<IRole> retrievePrincipalRoles(final Principal principal) {
		Assert.notNull(principal, "No Principal provided !");

		final Set<IRole> roles = this.aclDao.findPrincipalRoles(principal);

		return roles;
	}

}
