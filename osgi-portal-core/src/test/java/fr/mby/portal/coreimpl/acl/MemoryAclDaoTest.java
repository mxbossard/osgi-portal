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
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
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
public class MemoryAclDaoTest {

	private static final String TEST_PERM_NAME_1 = "TEST_PERM_NAME_1";

	private static final String TEST_PERM_NAME_2 = "TEST_PERM_NAME_2";

	private static final String TEST_PERM_NAME_3 = "TEST_PERM_NAME_3";

	private static final String TEST_PERM_NAME_4 = "TEST_PERM_NAME_4";

	private static final String TEST_ROLE_NAME_1 = "TEST_ROLE_NAME_1";

	private static final String TEST_ROLE_NAME_2 = "TEST_ROLE_NAME_2";

	@Autowired(required = true)
	private BasicPermissionFactory basicPermissionFactory;

	@Autowired(required = true)
	private BasicRoleFactory basicRoleFactory;

	@Autowired(required = true)
	private MemoryAclDao memoryAclDao;

	@Test
	public void testCreateAndFindRole() throws Exception {
		final IPermission perm1 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 1 should not be null !", perm1);

		final IPermission perm2 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_2);
		Assert.assertNotNull("Permission 2 should not be null !", perm2);

		final IPermission perm3 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_3);
		Assert.assertNotNull("Permission 3 should not be null !", perm3);

		final IPermission perm4 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_4);
		Assert.assertNotNull("Permission 4 should not be null !", perm4);

		// Create Role 1 whith Perms 2 & 3
		final Set<IPermission> permissionsRole1 = new HashSet<IPermission>();
		permissionsRole1.add(perm2);
		permissionsRole1.add(perm3);
		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, permissionsRole1,
				null);

		this.memoryAclDao.createRole(role1);
		final IRole role1found = this.memoryAclDao.findRole(MemoryAclDaoTest.TEST_ROLE_NAME_1);
		Assert.assertTrue("Role 1 created and role 1 found shoud be the same !", role1 == role1found);

		// Create Role 2 with Perms 4 and sub-role 1
		final Set<IPermission> permissionsRole2 = new HashSet<IPermission>();
		permissionsRole2.add(perm4);
		final Set<IRole> subRolesRole2 = new HashSet<IRole>();
		subRolesRole2.add(role1);
		final IRole role2 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_2, permissionsRole2,
				subRolesRole2);

		this.memoryAclDao.createRole(role2);
		final IRole role2found = this.memoryAclDao.findRole(MemoryAclDaoTest.TEST_ROLE_NAME_2);
		Assert.assertTrue("Role 2 created and role 2 found shoud be the same !", role2 == role2found);
	}

	@Test(expected = RoleAlreadyExistsException.class)
	public void testCreateRoleWhichAlreadyExists() throws Exception {
		final IPermission perm1 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 1 should not be null !", perm1);

		final IPermission perm2 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_2);
		Assert.assertNotNull("Permission 2 should not be null !", perm2);

		final IPermission perm3 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_3);
		Assert.assertNotNull("Permission 3 should not be null !", perm3);

		final IPermission perm4 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_4);
		Assert.assertNotNull("Permission 4 should not be null !", perm4);

		// Create Role 1 whith Perms 2 & 3
		final Set<IPermission> permissionsRole1 = new HashSet<IPermission>();
		permissionsRole1.add(perm2);
		permissionsRole1.add(perm3);
		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, permissionsRole1,
				null);

		this.memoryAclDao.createRole(role1);
		final IRole role1found = this.memoryAclDao.findRole(MemoryAclDaoTest.TEST_ROLE_NAME_1);
		Assert.assertTrue("Role 1 created and role 1 found shoud be the same !", role1 == role1found);

		// ReCreate Role 1
		this.memoryAclDao.createRole(role1);
	}

	@Test(expected = RoleNotFoundException.class)
	public void testFindRoleWhichDoesntExists() throws Exception {
		this.memoryAclDao.findRole(MemoryAclDaoTest.TEST_ROLE_NAME_2);
	}

	@Test
	public void testUpdateRole() throws Exception {
		final IPermission perm1 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 1 should not be null !", perm1);

		final IPermission perm2 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_2);
		Assert.assertNotNull("Permission 2 should not be null !", perm2);

		final IPermission perm3 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_3);
		Assert.assertNotNull("Permission 3 should not be null !", perm3);

		final IPermission perm4 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_4);
		Assert.assertNotNull("Permission 4 should not be null !", perm4);

		// Create Role 1 whith Perms 2 & 3
		final Set<IPermission> permissionsRole1 = new HashSet<IPermission>();
		permissionsRole1.add(perm2);
		permissionsRole1.add(perm3);
		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, permissionsRole1,
				null);

		this.memoryAclDao.createRole(role1);
		final IRole role1found = this.memoryAclDao.findRole(MemoryAclDaoTest.TEST_ROLE_NAME_1);
		Assert.assertTrue("Role 1 created and role 1 found shoud be the same !", role1 == role1found);

		// Reinitialize Role 1 in factory with perm 1 only
		final Set<IPermission> permissionsRole1Reseted = new HashSet<IPermission>();
		permissionsRole1Reseted.add(perm1);
		final IRole role1Reseted = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1,
				permissionsRole1Reseted, null);

		Assert.assertTrue("Role 1 found then reseted must be different !", role1found != role1Reseted);
		Assert.assertTrue("Role 1 reseted perm set size should be 1 !", role1Reseted.getPermissions().size() == 1);
		Assert.assertTrue("Role 1 found perm should be Perm 1 !",
				role1Reseted.getPermissions().iterator().next() == perm1);

		final IRole role1foundBeforeReset = this.memoryAclDao.findRole(MemoryAclDaoTest.TEST_ROLE_NAME_1);
		Assert.assertTrue("Role 1 created and role 1 found before reset shoud be the same !",
				role1 == role1foundBeforeReset);

		this.memoryAclDao.updateRole(role1Reseted);
		final IRole role1foundAfterReset = this.memoryAclDao.findRole(MemoryAclDaoTest.TEST_ROLE_NAME_1);
		Assert.assertTrue("Role 1 reseted and role 1 found after reset shoud be the same !",
				role1Reseted == role1foundAfterReset);
	}

	@Test(expected = RoleNotFoundException.class)
	public void testUpdateRoleNotFound() throws Exception {
		final IPermission perm1 = this.basicPermissionFactory.build(MemoryAclDaoTest.TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 1 should not be null !", perm1);

		// Create Role 1 whith Perms 1
		final Set<IPermission> permissionsRole1 = new HashSet<IPermission>();
		permissionsRole1.add(perm1);
		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, permissionsRole1,
				null);

		this.memoryAclDao.updateRole(role1);
	}

	@Test
	public void testRegisterAndFindPrincipal() throws Exception {
		final Principal testPrincipal = new PortalUserPrincipal("test");
		this.memoryAclDao.registerPrincipal(testPrincipal);

		final Set<IRole> roles = this.memoryAclDao.findPrincipalRoles(testPrincipal);
		Assert.assertNotNull("Roles set should not be null after registration !", roles);
		Assert.assertTrue("Roles set should be empty after registration !", roles.isEmpty());
	}

	@Test(expected = PrincipalAlreadyExistsException.class)
	public void testRegisterAlreadyRegisteredPrincipal() throws Exception {
		final Principal testPrincipal = new PortalUserPrincipal("test");
		this.memoryAclDao.registerPrincipal(testPrincipal);
		this.memoryAclDao.registerPrincipal(testPrincipal);
	}

	@Test
	public void testGrantRoles() throws Exception {
		final Principal testPrincipal = new PortalUserPrincipal("test");
		this.memoryAclDao.registerPrincipal(testPrincipal);

		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, null, null);
		this.memoryAclDao.createRole(role1);

		final Set<IRole> refreshedRoles = this.memoryAclDao.grantRoles(testPrincipal, AclHelper.buileRoleSet(role1));

		final Set<IRole> roles = this.memoryAclDao.findPrincipalRoles(testPrincipal);
		Assert.assertNotNull("Roles set should not be null after registration !", roles);
		Assert.assertTrue("Roles set size should be 1 after granting the role !", roles.size() == 1);
		Assert.assertEquals("Role found should be the one we granted !", role1, roles.iterator().next());
		Assert.assertTrue("Roles sets found and return by grant method shoud be equals !", roles.equals(refreshedRoles));
	}

	@Test(expected = RoleNotFoundException.class)
	public void testGrantNotFoundRoles() throws Exception {
		final Principal testPrincipal = new PortalUserPrincipal("test");
		this.memoryAclDao.registerPrincipal(testPrincipal);

		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, null, null);

		this.memoryAclDao.grantRoles(testPrincipal, AclHelper.buileRoleSet(role1));
	}

	@Test(expected = PrincipalNotFoundException.class)
	public void testGrantRolesToNotFoundPrincipal() throws Exception {
		final Principal testPrincipal = new PortalUserPrincipal("test");

		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, null, null);
		this.memoryAclDao.createRole(role1);

		this.memoryAclDao.grantRoles(testPrincipal, AclHelper.buileRoleSet(role1));
	}

	@Test
	public void testRevokeRoles() throws Exception {
		final Principal testPrincipal = new PortalUserPrincipal("test");
		this.memoryAclDao.registerPrincipal(testPrincipal);

		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, null, null);
		this.memoryAclDao.createRole(role1);
		final IRole role2 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_2, null, null);
		this.memoryAclDao.createRole(role2);

		// Grant role 1 & 2
		this.memoryAclDao.grantRoles(testPrincipal, AclHelper.buileRoleSet(role1, role2));

		final Set<IRole> roles = this.memoryAclDao.findPrincipalRoles(testPrincipal);
		Assert.assertNotNull("Roles set should not be null after registration !", roles);
		Assert.assertTrue("Roles set size should be 1 after granting the role !", roles.size() == 2);

		// Revoke role 1
		final Set<IRole> refreshedRoles = this.memoryAclDao.revokeRoles(testPrincipal, AclHelper.buileRoleSet(role1));

		final Set<IRole> rolesAfterRevoke = this.memoryAclDao.findPrincipalRoles(testPrincipal);
		Assert.assertNotNull("Roles set should not be null after registration !", rolesAfterRevoke);
		Assert.assertTrue("Roles set size should be 1 after granting the role !", rolesAfterRevoke.size() == 1);
		Assert.assertEquals("Roles should be role 2 after revoking role 1 !", role2, rolesAfterRevoke.iterator().next());
		Assert.assertTrue("Roles sets found and return by grant method shoud be equals !",
				rolesAfterRevoke.equals(refreshedRoles));
	}

	@Test(expected = RoleNotFoundException.class)
	public void testRevokeNotFoundRoles() throws Exception {
		final Principal testPrincipal = new PortalUserPrincipal("test");
		this.memoryAclDao.registerPrincipal(testPrincipal);

		final IRole role3 = this.basicRoleFactory.initializeRole("role 3", null, null);

		// Revoke role 3
		this.memoryAclDao.revokeRoles(testPrincipal, AclHelper.buileRoleSet(role3));
	}

	@Test(expected = PrincipalNotFoundException.class)
	public void testRevokeRolesToNotFoundPrincipal() throws Exception {
		final Principal testPrincipal = new PortalUserPrincipal("test");

		final IRole role1 = this.basicRoleFactory.initializeRole(MemoryAclDaoTest.TEST_ROLE_NAME_1, null, null);
		this.memoryAclDao.createRole(role1);

		// Revoke role 1
		this.memoryAclDao.revokeRoles(testPrincipal, AclHelper.buileRoleSet(role1));
	}

}
