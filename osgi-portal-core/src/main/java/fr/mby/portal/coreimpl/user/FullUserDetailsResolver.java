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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Service;

import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.api.user.IUserDetails;
import fr.mby.portal.core.acl.IAclManager;
import fr.mby.portal.core.security.IPrincipalResolver;
import fr.mby.portal.core.security.PrincipalNotFoundException;
import fr.mby.portal.core.user.IUserDetailsFactory;
import fr.mby.portal.core.user.IUserDetailsResolver;
import fr.mby.portal.coreimpl.acl.BasicAclManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class FullUserDetailsResolver implements IUserDetailsResolver {

	@Autowired(required = true)
	private IUserDetailsFactory userDetailsFactory;

	@Autowired(required = true)
	private IAclManager aclManager;

	@Autowired(required = true)
	private List<IPrincipalResolver> principalResolvers;

	@PostConstruct
	public void init() {
		// Sort the IPrincipalResolver list by Annotation Order
		Collections.sort(this.principalResolvers, AnnotationAwareOrderComparator.INSTANCE);
	}

	@Override
	public IUserDetails resolve(final HttpServletRequest request) {
		Principal userPrincipal = null;

		final Iterator<IPrincipalResolver> resolverIt = this.principalResolvers.iterator();
		while (userPrincipal == null && resolverIt.hasNext()) {
			final IPrincipalResolver principalResolver = resolverIt.next();
			userPrincipal = principalResolver.resolve(request);
		}

		if (userPrincipal == null) {
			userPrincipal = BasicAclManager.GUEST;
		}

		final boolean isAuthenticated = userPrincipal != BasicAclManager.GUEST;
		Set<IRole> roles;
		try {
			roles = this.aclManager.retrievePrincipalRoles(userPrincipal);
		} catch (final PrincipalNotFoundException e) {
			roles = null;
		}
		final Map<String, String> details = null;
		final IUserDetails userDetails = this.userDetailsFactory.build(userPrincipal, isAuthenticated, roles, details);

		return userDetails;
	}

}
