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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.eclipse.gemini.blueprint.context.BundleContextAware;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
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
import fr.mby.portal.coreimpl.acl.BasicAuthorization;
import fr.mby.portal.model.user.PortalUser;
import fr.mby.utils.common.jpa.OsgiJpaHelper;

/**
 * IAuthenticationProvider able to authenticate accounts in DB with JPA (see PortalUser entity).
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Service
@Order(value = 900)
public class DbPortalUserAuthenticationProvider
		implements
			IAuthenticationProvider,
			BundleContextAware,
			InitializingBean {

	@Autowired(required = true)
	private IAclManager aclManager;

	/** Wired by Spring. */
	private EntityManagerFactory portalUserEmf;

	private BundleContext bundleContext;

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
		final EntityManager portalUserEm = this.portalUserEmf.createEntityManager();
		final PortalUser portalUser = this.findPortalUserByLogin(portalUserEm, login);
		portalUserEm.close();

		if (portalUser != null) {
			// We found the user in the DB => we can authenticate him
			resultingAuth = this.performAuthentication(auth, portalUser);
		}

		if (resultingAuth == null) {
			// throw new AuthenticationException(authentication, "Unable to authenticate supplied authentication !");
		}

		return resultingAuth;
	}

	@Override
	public void setBundleContext(final BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		// Load EntityManager
		this.portalUserEmf = OsgiJpaHelper.retrieveEmfByName(this.bundleContext, "portalUserPu");

		this.initDefaultUsers();
	}

	/** Init the DB with default users : user:user456 & admin:admin456 */
	protected void initDefaultUsers() {
		final PortalUser user = this.buildPortalUser("user", "user456");
		final PortalUser admin = this.buildPortalUser("admin", "admin456");

		final EntityManager portalUserEm = this.portalUserEmf.createEntityManager();

		// Remove transaction
		portalUserEm.getTransaction().begin();

		final PortalUser foundAdmin = this.findPortalUserByLogin(portalUserEm, "admin");
		if (foundAdmin != null) {
			portalUserEm.remove(foundAdmin);
		}

		final PortalUser foundUser = this.findPortalUserByLogin(portalUserEm, "user");
		if (foundUser != null) {
			portalUserEm.remove(foundUser);
		}

		portalUserEm.getTransaction().commit();

		// Insert transaction
		portalUserEm.getTransaction().begin();

		portalUserEm.persist(user);
		portalUserEm.persist(admin);

		portalUserEm.getTransaction().commit();

		portalUserEm.close();
	}

	protected PortalUser findPortalUserByLogin(final EntityManager portalUserEm, final String login) {
		final Query query = portalUserEm.createNamedQuery(PortalUser.FIND_PORTAL_USER);
		query.setParameter("login", login);

		PortalUser found = null;
		final List<?> founds = query.getResultList();
		if (founds != null && founds.size() == 1) {
			found = (PortalUser) founds.iterator().next();
		}

		return found;
	}

	protected PortalUser buildPortalUser(final String login, final String password) {
		final PortalUser user = new PortalUser();
		user.setLogin(login);
		user.setPassword(password);
		return user;
	}

	protected EntityManagerFactory retrieveEntityManagerFactory(final String unitName) {
		ServiceReference[] refs = null;
		try {
			refs = this.bundleContext.getServiceReferences(EntityManagerFactory.class.getName(), "(osgi.unit.name="
					+ unitName + ")");
		} catch (final InvalidSyntaxException isEx) {
			throw new RuntimeException("Filter error", isEx);
		}
		return (refs == null) ? null : (EntityManagerFactory) this.bundleContext.getService(refs[0]);
	}

	/**
	 * @param auth
	 * @param user
	 */
	protected PortalUserAuthentication performAuthentication(final PortalUserAuthentication auth,
			final PortalUser portalUser) {
		PortalUserAuthentication resultingAuth = null;

		final String creds = (String) auth.getCredentials();
		if (portalUser.getPassword().equals(creds)) {
			Set<IRole> roles = null;
			try {
				roles = this.aclManager.retrievePrincipalRoles(auth);
			} catch (final PrincipalNotFoundException e) {
				roles = Collections.emptySet();
			}

			final IAuthorization authorizations = new BasicAuthorization(roles);
			resultingAuth = new PortalUserAuthentication(auth, authorizations);
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
