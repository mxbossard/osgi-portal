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

package fr.mby.portal.coreimpl.auth;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.mby.portal.core.auth.AuthenticationException;
import fr.mby.portal.core.auth.IAuthentication;
import fr.mby.portal.core.auth.PortalUserAuthentication;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:minimalAuthTestContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MinimalPortalUserAuthenticationProviderTest {

	@Autowired(required = true)
	private MinimalPortalUserAuthenticationProvider minimalAuthProvider;

	@Test
	public void testAuthenticateAdmin() throws Exception {
		final IAuthentication authentication = new PortalUserAuthentication("admin", "admin123");
		final IAuthentication providedAuth = this.minimalAuthProvider.authenticate(authentication);

		Assert.assertNotNull("Provided Authentication should not be null !", providedAuth);
		Assert.assertTrue("Provided Authentication should be authenticated !", providedAuth.isAuthenticated());
	}

	@Test
	public void testAuthenticateUser() throws Exception {
		final IAuthentication authentication = new PortalUserAuthentication("user", "user123");
		final IAuthentication providedAuth = this.minimalAuthProvider.authenticate(authentication);

		Assert.assertNotNull("Provided Authentication should not be null !", providedAuth);
		Assert.assertTrue("Provided Authentication should be authenticated !", providedAuth.isAuthenticated());
	}

	@Test(expected = AuthenticationException.class)
	public void testAuthenticateNoOne() throws Exception {
		final IAuthentication authentication = new PortalUserAuthentication("noone", "noone");
		this.minimalAuthProvider.authenticate(authentication);
	}

}
