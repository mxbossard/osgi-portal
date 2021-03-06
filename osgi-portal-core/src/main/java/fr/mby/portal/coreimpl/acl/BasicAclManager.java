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
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IAuthorization;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IAclDao;
import fr.mby.portal.core.acl.IAclManager;
import fr.mby.portal.core.acl.IPermissionFactory;
import fr.mby.portal.core.acl.IRoleFactory;
import fr.mby.portal.core.acl.RoleAlreadyExistsException;
import fr.mby.portal.core.acl.RoleNotFoundException;
import fr.mby.portal.core.security.PrincipalAlreadyExistsException;
import fr.mby.portal.core.security.PrincipalNotFoundException;
import fr.mby.portal.coreimpl.security.PortalUserPrincipal;

/**
 * Basic ACL manager wich self register 3 Principal with some roles.
 * 
 * <ul>
 * <li>
 * GUEST : role guest with no permission</li>
 * <li>
 * LOGGED : role user with "logged permission"</li>
 * <li>
 * ADMIN : role admin with "all permissions" and sub-role USER</li>
 * </ul>
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAclManager implements IAclManager, InitializingBean {

	public static final Principal ADMIN = new PortalUserPrincipal("special_admin");

	public static final Principal GUEST = new PortalUserPrincipal("special_guest");

	public static final Principal LOGGED = new PortalUserPrincipal("special_logged");

	// public static final String ALL_PERMISSIONS = "allPermissions";

	// public static final String USER_PERMISSION = "userPermission";

	// public static final String CAN_RENDER_PERMISSION = "special_canRender";

	// public static final String CAN_EDIT_PERMISSION = "special_canEdit";

	public static final IAuthorization ALL_PERMISSION_WILDCARD = new AllPermissionsWildcard();

	@Autowired
	private IRoleFactory roleFactory;

	@Autowired
	private IPermissionFactory permissionFactory;

	@Autowired
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
		this.initializeAcl();

	}

	protected void initializeAcl() throws PrincipalAlreadyExistsException, PrincipalNotFoundException,
			RoleNotFoundException, RoleAlreadyExistsException {

		// Nothing
	}

	/**
	 * Getter of roleFactory.
	 * 
	 * @return the roleFactory
	 */
	public IRoleFactory getRoleFactory() {
		return this.roleFactory;
	}

	/**
	 * Setter of roleFactory.
	 * 
	 * @param roleFactory
	 *            the roleFactory to set
	 */
	public void setRoleFactory(final IRoleFactory roleFactory) {
		this.roleFactory = roleFactory;
	}

	/**
	 * Getter of permissionFactory.
	 * 
	 * @return the permissionFactory
	 */
	public IPermissionFactory getPermissionFactory() {
		return this.permissionFactory;
	}

	/**
	 * Setter of permissionFactory.
	 * 
	 * @param permissionFactory
	 *            the permissionFactory to set
	 */
	public void setPermissionFactory(final IPermissionFactory permissionFactory) {
		this.permissionFactory = permissionFactory;
	}

	/**
	 * Getter of aclDao.
	 * 
	 * @return the aclDao
	 */
	public IAclDao getAclDao() {
		return this.aclDao;
	}

	/**
	 * Setter of aclDao.
	 * 
	 * @param aclDao
	 *            the aclDao to set
	 */
	public void setAclDao(final IAclDao aclDao) {
		this.aclDao = aclDao;
	}

}
