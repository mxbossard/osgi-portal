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

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:basicAclTestContext.xml")
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicRoleTest {

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
	
	@Test
	public void testIsGranted() throws Exception {
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
		IRole initializedRole1 = this.basicRoleFactory.initializeRole(TEST_ROLE_NAME_1, permissionsRole1 , null);
		
		// Check Role 1 perms
		Assert.assertTrue("Permission 3 should be granted to Role 1 !", initializedRole1.isGranted(perm3));
		Assert.assertTrue("Permission 2 should be granted to Role 1 !", initializedRole1.isGranted(perm2));
		
		Assert.assertFalse("Permission 1 shouldn't be granted to Role 1 !", initializedRole1.isGranted(perm1));
		Assert.assertFalse("Permission 4 shouldn't be granted to Role 1 !", initializedRole1.isGranted(perm4));
				
		// Create Role 2 with Perms 4 and sub-role 1
		Set<IPermission> permissionsRole2 = new HashSet<IPermission>();
		permissionsRole2.add(perm4);
		Set<IRole> subRolesRole2 = new HashSet<IRole>();
		subRolesRole2.add(initializedRole1);
		IRole initializedRole2 = this.basicRoleFactory.initializeRole(TEST_ROLE_NAME_2, permissionsRole2 , subRolesRole2);
	
		// Check Role 2 perms
		Assert.assertTrue("Permission 3 should be granted to Role 1 !", initializedRole2.isGranted(perm3));
		Assert.assertTrue("Permission 2 should be granted to Role 1 !", initializedRole2.isGranted(perm2));
		Assert.assertTrue("Permission 4 should be granted to Role 1 !", initializedRole2.isGranted(perm4));
		
		Assert.assertFalse("Permission 1 shouldn't be granted to Role 1 !", initializedRole2.isGranted(perm1));
	
	}
	
}
