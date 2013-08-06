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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IAclDao;
import fr.mby.portal.core.acl.IAclManager;
import fr.mby.portal.core.acl.IPermissionFactory;
import fr.mby.portal.core.acl.IRoleFactory;
import fr.mby.portal.core.acl.RoleNotFoundException;
import fr.mby.portal.core.security.PrincipalAlreadyExistsException;
import fr.mby.portal.core.security.PrincipalNotFoundException;
import fr.mby.portal.coreimpl.security.PortalUserPrincipal;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAclManager implements IAclManager, InitializingBean {

	public static final Principal ADMIN = new PortalUserPrincipal("admin");
	
	public static final Principal GUEST = new PortalUserPrincipal("guest");
	
	public static final Principal USER = new PortalUserPrincipal("user");
	
	public static final String ALL_PERMISSIONS = "allPermissions";
	
	public static final String USER_PERMISSION = "userPermission";
	
	@Autowired(required = true)
	private IRoleFactory roleFactory;

	@Autowired(required = true)
	private IPermissionFactory permissionFactory;

	@Autowired(required = true)
	private IAclDao aclDao;

	@Override
	public void registerPrincipal(final Principal principal) throws PrincipalAlreadyExistsException {
		Assert.notNull(principal, "No Principal supplied !");

		this.aclDao.registerPrincipal(principal);
	}

	@Override
	public void registerPrincipalRoles(final Principal principal, final Set<IRole> roles)
			throws PrincipalNotFoundException, RoleNotFoundException {
		Assert.notNull(principal, "No Principal supplied !");
		Assert.notNull(roles, "No Roles provided !");

		this.aclDao.grantRoles(principal, roles);
	}

	@Override
	public Set<IRole> retrievePrincipalRoles(final Principal principal) throws PrincipalNotFoundException {
		Assert.notNull(principal, "No Principal supplied !");

		final Set<IRole> roles = this.aclDao.findPrincipalRoles(principal);

		return Collections.unmodifiableSet(roles);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// Initialize ACL
		initializeAcl();
		
	}

	protected void initializeAcl() throws PrincipalAlreadyExistsException,
			PrincipalNotFoundException, RoleNotFoundException {
		// Guest with no permission
		final IRole guestRole = this.roleFactory.initializeRole(GUEST.getName(), null, null);
		final Set<IRole> guestRolesSet = new HashSet<IRole>(1);
		guestRolesSet.add(guestRole);

		this.registerPrincipal(GUEST);
		this.registerPrincipalRoles(GUEST, guestRolesSet);
		
		// User with "user permission"
		final IPermission userPermission = this.permissionFactory.build(USER_PERMISSION);
		final Set<IPermission> userPermissionSet = new HashSet<IPermission>(1);
		userPermissionSet.add(userPermission);

		final IRole userRole = this.roleFactory.initializeRole(USER.getName(), userPermissionSet, null);
		final Set<IRole> userRoleSet = new HashSet<IRole>(1);
		userRoleSet.add(userRole);

		this.registerPrincipal(USER);
		this.registerPrincipalRoles(USER, userRoleSet);
		
		// Admin with "all permissions"
		final IPermission allPermissions = this.permissionFactory.build(ALL_PERMISSIONS);
		final Set<IPermission> allPermissionsSet = new HashSet<IPermission>(1);
		allPermissionsSet.add(allPermissions);

		final IRole adminRole = this.roleFactory.initializeRole(ADMIN.getName(), allPermissionsSet, userRoleSet);
		final Set<IRole> adminRolesSet = new HashSet<IRole>(1);
		adminRolesSet.add(adminRole);

		this.registerPrincipal(ADMIN);
		this.registerPrincipalRoles(ADMIN, adminRolesSet);
	}

}
