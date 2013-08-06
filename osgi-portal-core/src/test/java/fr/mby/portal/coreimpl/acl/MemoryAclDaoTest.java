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

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:basicAclTestContext.xml")
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class MemoryAclDaoTest {

	private static final String TEST_PERM_NAME_1 = "TEST_PERM_NAME_1";
	
	private static final String TEST_PERM_NAME_2 = "TEST_PERM_NAME_2";
	
	private static final String TEST_PERM_NAME_3 = "TEST_PERM_NAME_3";
	
	private static final String TEST_PERM_NAME_4 = "TEST_PERM_NAME_4";
	
	private static final String TEST_ROLE_NAME_1 = "TEST_ROLE_NAME_1";
	
	private static final String TEST_ROLE_NAME_2 = "TEST_ROLE_NAME_2";
	
	@Autowired(required=true)
	private BasicPermissionFactory basicPermissionFactory;
	
	@Autowired(required=true)
	private BasicRoleFactory basicRoleFactory;
	
	@Autowired(required=true)
	private MemoryAclDao memoryAclDao;
	
	@Test
	public void testCreateAndFindRole() throws Exception {
		IPermission perm1 = basicPermissionFactory.build(TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 1 should not be null !", perm1);
		
		IPermission perm2 = basicPermissionFactory.build(TEST_PERM_NAME_2);
		Assert.assertNotNull("Permission 2 should not be null !", perm2);
		
		IPermission perm3 = basicPermissionFactory.build(TEST_PERM_NAME_3);
		Assert.assertNotNull("Permission 3 should not be null !", perm3);
		
		IPermission perm4 = basicPermissionFactory.build(TEST_PERM_NAME_4);
		Assert.assertNotNull("Permission 4 should not be null !", perm4);
		
		// Create Role 1 whith Perms 2 & 3
		Set<IPermission> permissionsRole1 = new HashSet<IPermission>();
		permissionsRole1.add(perm2);
		permissionsRole1.add(perm3);
		IRole role1 = this.basicRoleFactory.initializeRole(TEST_ROLE_NAME_1, permissionsRole1 , null);
		
		memoryAclDao.createRole(role1);
		IRole role1found = memoryAclDao.findRole(TEST_ROLE_NAME_1);
		Assert.assertTrue("Role 1 created and role 1 found shoud be the same !", role1 == role1found);

		// Create Role 2 with Perms 4 and sub-role 1
		Set<IPermission> permissionsRole2 = new HashSet<IPermission>();
		permissionsRole2.add(perm4);
		Set<IRole> subRolesRole2 = new HashSet<IRole>();
		subRolesRole2.add(role1);
		IRole role2 = this.basicRoleFactory.initializeRole(TEST_ROLE_NAME_2, permissionsRole2 , subRolesRole2);

		memoryAclDao.createRole(role2);
		IRole role2found = memoryAclDao.findRole(TEST_ROLE_NAME_2);
		Assert.assertTrue("Role 2 created and role 2 found shoud be the same !", role2 == role2found);
	}

	@Test(expected=RoleAlreadyExistsException.class)
	public void testCreateRoleWhichAlreadyExists() throws Exception {
		IPermission perm1 = basicPermissionFactory.build(TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 1 should not be null !", perm1);
		
		IPermission perm2 = basicPermissionFactory.build(TEST_PERM_NAME_2);
		Assert.assertNotNull("Permission 2 should not be null !", perm2);
		
		IPermission perm3 = basicPermissionFactory.build(TEST_PERM_NAME_3);
		Assert.assertNotNull("Permission 3 should not be null !", perm3);
		
		IPermission perm4 = basicPermissionFactory.build(TEST_PERM_NAME_4);
		Assert.assertNotNull("Permission 4 should not be null !", perm4);
		
		// Create Role 1 whith Perms 2 & 3
		Set<IPermission> permissionsRole1 = new HashSet<IPermission>();
		permissionsRole1.add(perm2);
		permissionsRole1.add(perm3);
		IRole role1 = this.basicRoleFactory.initializeRole(TEST_ROLE_NAME_1, permissionsRole1 , null);
		
		memoryAclDao.createRole(role1);
		IRole role1found = memoryAclDao.findRole(TEST_ROLE_NAME_1);
		Assert.assertTrue("Role 1 created and role 1 found shoud be the same !", role1 == role1found);

		// Create Role 2 with Perms 4 but with same name as Role 1
		Set<IPermission> permissionsRole2 = new HashSet<IPermission>();
		permissionsRole2.add(perm4);
		this.basicRoleFactory.initializeRole(TEST_ROLE_NAME_1, permissionsRole2 , null);
	}
	
	@Test(expected=RoleNotFoundException.class)
	public void testFindRoleWhichDoesntExists() throws Exception {
		memoryAclDao.findRole(TEST_ROLE_NAME_2);
	}
	
	@Test
	public void testAddRolePermission() throws Exception {
		IPermission perm1 = basicPermissionFactory.build(TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 1 should not be null !", perm1);
		
		IPermission perm2 = basicPermissionFactory.build(TEST_PERM_NAME_2);
		Assert.assertNotNull("Permission 2 should not be null !", perm2);
		
		IPermission perm3 = basicPermissionFactory.build(TEST_PERM_NAME_3);
		Assert.assertNotNull("Permission 3 should not be null !", perm3);
		
		IPermission perm4 = basicPermissionFactory.build(TEST_PERM_NAME_4);
		Assert.assertNotNull("Permission 4 should not be null !", perm4);
		
		// Create Role 1 whith Perms 2 & 3
		Set<IPermission> permissionsRole1 = new HashSet<IPermission>();
		permissionsRole1.add(perm2);
		permissionsRole1.add(perm3);
		IRole role1 = this.basicRoleFactory.initializeRole(TEST_ROLE_NAME_1, permissionsRole1 , null);
		
		memoryAclDao.createRole(role1);
		IRole role1found = memoryAclDao.findRole(TEST_ROLE_NAME_1);
		
		Set<IPermission> extraPermissions = new HashSet<IPermission>();
		extraPermissions.add(perm1);
		IRole modifiedRole = memoryAclDao.addRolePermissions(role1found, extraPermissions);
		Assert.assertEquals("Modified Role perms size should be 3 !", 3, modifiedRole.getPermissions().size());
	}
	
	@Test
	public void testResetRole() throws Exception {
		
	}
	
	@Test
	public void testGrantRoles() throws Exception {
		
	}
	
	@Test
	public void testRevokeRoles() throws Exception {
		
	}
	
	@Test
	public void testFindPrincipalRoles() throws Exception {
		
	}
	
}
