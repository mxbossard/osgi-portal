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

package fr.mby.portal.coreimpl.security;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import fr.mby.portal.core.security.IPrincipalResolver;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class UniquePrincipalResolver implements IPrincipalResolver<HttpServletRequest>, InitializingBean {

	private Principal uniquePrincipal;

	@Override
	public Principal resolve(HttpServletRequest object) {
		return this.uniquePrincipal;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final String key = "UniquePrincipalResolver";
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("user"));
		authorities.add(new SimpleGrantedAuthority("admin"));
		final Object principal = new User("Anonymous", "Anonymous", authorities);

		this.uniquePrincipal = new AnonymousAuthenticationToken(key, principal, authorities);
	}

}
