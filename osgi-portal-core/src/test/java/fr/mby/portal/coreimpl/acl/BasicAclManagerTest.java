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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IAclDao;
import fr.mby.portal.core.acl.IPermissionFactory;
import fr.mby.portal.core.acl.IRoleFactory;
import fr.mby.portal.core.acl.RoleAlreadyExistsException;
import fr.mby.portal.core.acl.RoleNotFoundException;
import fr.mby.portal.core.security.PrincipalAlreadyExistsException;
import fr.mby.portal.core.security.PrincipalNotFoundException;
import fr.mby.portal.coreimpl.security.PortalUserPrincipal;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:basicAclTestContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicAclManagerTest {

	public static final String ALL_PERMISSIONS = "allPermissions";

	public static final String USER_PERMISSION = "userPermission";

	public static final String CAN_RENDER_PERMISSION = "special_canRender";

	public static final String CAN_EDIT_PERMISSION = "special_canEdit";

	private IAclDao aclDao;
	private IRoleFactory roleFactory;
	private IPermissionFactory permissionFactory;

	@Autowired()
	private BasicAclManager basicAclManager;

	@Before
	public void initializeAcl() throws PrincipalAlreadyExistsException, PrincipalNotFoundException,
			RoleNotFoundException, RoleAlreadyExistsException {

		this.aclDao = this.basicAclManager.getAclDao();
		this.roleFactory = this.basicAclManager.getRoleFactory();
		this.permissionFactory = this.basicAclManager.getPermissionFactory();

		// Guest with no permission
		final IRole guestRole = this.roleFactory.initializeRole(BasicAclManager.GUEST.getName(), null, null);
		final Set<IRole> guestRolesSet = new HashSet<IRole>(1);
		guestRolesSet.add(guestRole);

		this.basicAclManager.registerPrincipal(BasicAclManager.GUEST);
		this.aclDao.createRole(guestRole);
		this.basicAclManager.registerPrincipalRoles(BasicAclManager.GUEST, guestRolesSet);

		// User with "user permission"
		final IPermission userPermission = this.permissionFactory.build(BasicAclManagerTest.USER_PERMISSION);
		final Set<IPermission> userPermissionSet = new HashSet<IPermission>(1);
		userPermissionSet.add(userPermission);

		final IRole userRole = this.roleFactory.initializeRole(BasicAclManager.LOGGED.getName(), userPermissionSet,
				null);
		final Set<IRole> userRoleSet = new HashSet<IRole>(1);
		userRoleSet.add(userRole);

		this.basicAclManager.registerPrincipal(BasicAclManager.LOGGED);
		this.aclDao.createRole(userRole);
		this.basicAclManager.registerPrincipalRoles(BasicAclManager.LOGGED, userRoleSet);

		// Admin with "all permissions"
		final IPermission allPermissions = this.permissionFactory.build(BasicAclManagerTest.ALL_PERMISSIONS);
		final Set<IPermission> allPermissionsSet = new HashSet<IPermission>(1);
		allPermissionsSet.add(allPermissions);

		final IRole adminRole = this.roleFactory.initializeRole(BasicAclManager.ADMIN.getName(), allPermissionsSet,
				userRoleSet);
		final Set<IRole> adminRolesSet = new HashSet<IRole>(1);
		adminRolesSet.add(adminRole);

		this.basicAclManager.registerPrincipal(BasicAclManager.ADMIN);
		this.aclDao.createRole(adminRole);
		this.basicAclManager.registerPrincipalRoles(BasicAclManager.ADMIN, adminRolesSet);
	}

	@Test
	public void testRetrievePrincipalRoles() throws Exception {
		final Set<IRole> roles = this.basicAclManager.retrievePrincipalRoles(BasicAclManager.ADMIN);

		Assert.assertNotNull("Admin roles should not be null !", roles);
		Assert.assertEquals("Admin roles set size should be 1 !", 1, roles.size());
		Assert.assertEquals("Admin unique role should be 'special_admin' !", "special_admin", roles.iterator().next()
				.getName());
	}

	@Test(expected = PrincipalNotFoundException.class)
	public void testRetrieveUnknownPrincipalRoles() throws Exception {
		this.basicAclManager.retrievePrincipalRoles(new PortalUserPrincipal("unkown"));
	}

	@Test(expected = PrincipalAlreadyExistsException.class)
	public void testRegisterPrincipalWichAlreadyExists() throws Exception {
		this.basicAclManager.registerPrincipal(BasicAclManager.ADMIN);
	}

	@Test(expected = RoleNotFoundException.class)
	public void testRegisterPrincipalRoleWhichDoesntExists() throws Exception {
		final Set<IRole> newRoles = new HashSet<IRole>();
		newRoles.add(this.roleFactory.initializeRole("notRegisteredRole", null, null));
		this.basicAclManager.registerPrincipalRoles(BasicAclManager.ADMIN, newRoles);
	}

	@Test(expected = PrincipalNotFoundException.class)
	public void testRegisterUnkownPrincipalRoles() throws Exception {
		final Set<IRole> newRoles = new HashSet<IRole>();
		newRoles.add(this.roleFactory.initializeRole("notRegisteredRole", null, null));
		this.basicAclManager.registerPrincipalRoles(new PortalUserPrincipal("unkown"), newRoles);
	}

}
