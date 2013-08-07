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

package fr.mby.portal.coreimpl.user;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.api.user.IUserDetails;
import fr.mby.portal.core.user.IUserDetailsFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicUserDetailsFactory implements IUserDetailsFactory {

	@Override
	public IUserDetails build(final Principal principal, final boolean isAuthenticated, final Set<IRole> roles,
			final Map<String, String> details) {
		Assert.notNull(principal, "No principal supplied !");

		final BasicUserDetails userDetails = new BasicUserDetails();
		userDetails.setPrincipal(principal);
		userDetails.setAuthenticated(isAuthenticated);

		if (roles != null) {
			userDetails.setRoles(roles);
		} else {
			userDetails.setRoles(Collections.<IRole> emptySet());
		}

		if (details != null) {
			userDetails.setDetails(details);
		} else {
			userDetails.setDetails(Collections.<String, String> emptyMap());
		}

		return userDetails;
	}

}
