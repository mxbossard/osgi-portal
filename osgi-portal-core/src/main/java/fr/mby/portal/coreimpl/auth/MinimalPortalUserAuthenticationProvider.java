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

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.acl.IAuthorization;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.acl.IAclManager;
import fr.mby.portal.core.auth.AuthenticationException;
import fr.mby.portal.core.auth.IAuthentication;
import fr.mby.portal.core.auth.IAuthenticationProvider;
import fr.mby.portal.core.auth.PortalUserAuthentication;
import fr.mby.portal.core.security.PrincipalNotFoundException;
import fr.mby.portal.coreimpl.acl.BasicAclManager;
import fr.mby.portal.coreimpl.acl.BasicAuthorization;
import fr.mby.portal.model.user.PortalUser;

/**
 * IAuthenticationProvider able to authenticate only 2 generic accounts :
 * <ul>
 * <li>user / user123 ==> BasicAclManager.USER</li>
 * <li>admin / admin123 ==> BasicAclManager.ADMIN</li>
 * </ul>
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Service
@Order(value = 1000)
public class MinimalPortalUserAuthenticationProvider implements IAuthenticationProvider, InitializingBean {

	@Autowired(required = true)
	private IAclManager aclManager;

	/** Wired by OSGi. */
	private EntityManagerFactory portalUserEmf;

	@Override
	public boolean supports(final IAuthentication authentication) {
		return authentication != null && PortalUserAuthentication.class.isAssignableFrom(authentication.getClass());
	}

	@Override
	public IAuthentication authenticate(final IAuthentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication supplied !");
		Assert.isInstanceOf(PortalUserAuthentication.class, authentication,
				"IAuthentication shoud be a PortalUserAuthentication type !");

		if (authentication.isAuthenticated()) {
			throw new AuthenticationException(authentication, "Already authenticated IAuthentication token supplied !");
		}

		final PortalUserAuthentication auth = (PortalUserAuthentication) authentication;

		IAuthentication resultingAuth = null;

		final String login = authentication.getName();
		switch (login) {
			case "admin" :
				resultingAuth = this.performAuthentication(auth, "admin123", BasicAclManager.ADMIN);
				break;
			case "user" :
				resultingAuth = this.performAuthentication(auth, "user123", BasicAclManager.LOGGED);
				break;
			default :
				break;
		}

		if (resultingAuth == null) {
			throw new AuthenticationException(authentication, "Unable to authenticate supplied authentication !");
		}

		return resultingAuth;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final EntityManager entityManager = this.portalUserEmf.createEntityManager();

		final PortalUser user = new PortalUser();
		user.setLogin("user");
		user.setPassword("user123");

		entityManager.persist(user);

		final PortalUser admin = new PortalUser();
		admin.setLogin("admin");
		admin.setPassword("admin123");

		entityManager.persist(admin);

		entityManager.flush();

		final Query query1 = entityManager.createNamedQuery(PortalUser.FIND_PORTAL_USER);
		query1.setParameter("login", "user");
		final PortalUser found1 = (PortalUser) query1.getSingleResult();

		final Query query2 = entityManager.createNamedQuery(PortalUser.FIND_PORTAL_USER);
		query2.setParameter("login", "user");
		final PortalUser found2 = (PortalUser) query2.getSingleResult();

		final boolean test1 = user.getLogin().equals(found1.getLogin());
		final boolean test2 = admin.getLogin().equals(found2.getLogin());
	}

	/**
	 * @param auth
	 * @param user
	 */
	protected PortalUserAuthentication performAuthentication(final PortalUserAuthentication auth,
			final String password, final Principal user) {
		PortalUserAuthentication resultingAuth = null;

		final String creds = (String) auth.getCredentials();
		if (password.equals(creds)) {
			Set<IRole> roles = null;
			try {
				roles = this.aclManager.retrievePrincipalRoles(user);
			} catch (final PrincipalNotFoundException e) {
				roles = Collections.emptySet();
			}

			final IAuthorization authorizations = new BasicAuthorization(roles);
			resultingAuth = new PortalUserAuthentication(user.getName(), creds, authorizations);
		}

		return resultingAuth;
	}

	/**
	 * Getter of aclManager.
	 * 
	 * @return the aclManager
	 */
	public IAclManager getAclManager() {
		return this.aclManager;
	}

	/**
	 * Setter of aclManager.
	 * 
	 * @param aclManager
	 *            the aclManager to set
	 */
	public void setAclManager(final IAclManager aclManager) {
		this.aclManager = aclManager;
	}

	/**
	 * Getter of portalUserEmf.
	 * 
	 * @return the portalUserEmf
	 */
	public EntityManagerFactory getPortalUserEmf() {
		return this.portalUserEmf;
	}

	/**
	 * Setter of portalUserEmf.
	 * 
	 * @param portalUserEmf
	 *            the portalUserEmf to set
	 */
	public void setPortalUserEmf(final EntityManagerFactory portalUserEmf) {
		this.portalUserEmf = portalUserEmf;
	}

}
