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

import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IRoleFactory;
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
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicAclManagerTest {

	@Autowired(required=true)
	private BasicAclManager basicAclManager;

	@Autowired(required=true)
	private IRoleFactory roleFactory;
	
	@Test
	public void testRetrievePrincipalRoles() throws Exception {
		Set<IRole> roles = basicAclManager.retrievePrincipalRoles(BasicAclManager.ADMIN);
		
		Assert.assertNotNull("Admin roles should not be null !", roles);
		Assert.assertEquals("Admin roles set size should be 1 !", 1, roles.size());
		Assert.assertEquals("Admin unique role should be 'admin' !", "admin", roles.iterator().next().getName());
	}
	
	@Test(expected=PrincipalNotFoundException.class)
	public void testRetrieveUnknownPrincipalRoles() throws Exception {
		basicAclManager.retrievePrincipalRoles(new PortalUserPrincipal("unkown"));
	}
	
	@Test(expected=PrincipalAlreadyExistsException.class)
	public void testRegisterPrincipalWichAlreadyExists() throws Exception {
		basicAclManager.registerPrincipal(BasicAclManager.ADMIN);
	}
	
	@Test(expected=RoleNotFoundException.class)
	public void testRegisterPrincipalRoleWhichDoesntExists() throws Exception {
		Set<IRole> newRoles = new HashSet<IRole>();
		newRoles.add(roleFactory.initializeRole("notRegisteredRole", null, null));
		basicAclManager.registerPrincipalRoles(BasicAclManager.ADMIN, newRoles);
	}
	
	@Test(expected=PrincipalNotFoundException.class)
	public void testRegisterUnkownPrincipalRoles() throws Exception {
		Set<IRole> newRoles = new HashSet<IRole>();
		newRoles.add(roleFactory.initializeRole("notRegisteredRole", null, null));
		basicAclManager.registerPrincipalRoles(new PortalUserPrincipal("unkown"), newRoles);
	}
	
	
}
