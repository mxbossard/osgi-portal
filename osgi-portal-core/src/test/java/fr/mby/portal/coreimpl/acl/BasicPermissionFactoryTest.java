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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.mby.portal.api.acl.IPermission;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:basicAclTestContext.xml")
public class BasicPermissionFactoryTest {

	private static final String TEST_PERM_NAME_1 = "TEST_PERM_NAME_1";
	
	private static final String TEST_PERM_NAME_2 = "TEST_PERM_NAME_2";
	
	@Autowired(required=true)
	private BasicPermissionFactory basicPermissionFactory;
	
	@Test
	public void testBuild() throws Exception {
		IPermission perm1 = basicPermissionFactory.build(TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 1 should not be null !", perm1);
		Assert.assertEquals("Bad permission 1 name !", TEST_PERM_NAME_1, perm1.getName());
		
		IPermission perm2 = basicPermissionFactory.build(TEST_PERM_NAME_2);
		Assert.assertNotNull("Permission 2 should not be null !", perm2);
		Assert.assertFalse("Permission 1 & 2 should be different !", perm1 == perm2);
		Assert.assertFalse("Permission 1 & 2 should be different !", perm1.equals(perm2));
		Assert.assertEquals("Bad permission 2 name !", TEST_PERM_NAME_2, perm2.getName());
		
		IPermission perm3 = basicPermissionFactory.build(TEST_PERM_NAME_1);
		Assert.assertNotNull("Permission 3 should not be null !", perm3);
		Assert.assertTrue("Permission 1 & 3 should be identical !", perm1 == perm3);
		Assert.assertTrue("Permission 1 & 3 should be identical !", perm1.equals(perm3));
		Assert.assertEquals("Bad permission 3 name !", TEST_PERM_NAME_1, perm3.getName());
		
	}
	
}
