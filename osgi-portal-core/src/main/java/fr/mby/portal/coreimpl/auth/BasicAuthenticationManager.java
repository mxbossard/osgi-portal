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
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import fr.mby.portal.core.auth.AuthenticationException;
import fr.mby.portal.core.auth.IAuthentication;
import fr.mby.portal.core.auth.IAuthenticationManager;
import fr.mby.portal.core.auth.IAuthenticationProvider;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAuthenticationManager implements IAuthenticationManager {

	@Autowired(required = true)
	private List<IAuthenticationProvider> internalAuthenticationProviders;

	private List<IAuthenticationProvider> externalAuthenticationProviders;

	@PostConstruct
	public void init() {
		// Sort the IAuthenticationProvider list by Annotation Order
		Collections.sort(this.internalAuthenticationProviders, AnnotationAwareOrderComparator.INSTANCE);
	}

	@Override
	public IAuthentication authenticate(final IAuthentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No Authentication supplied !");
		Assert.notNull(authentication.getPrincipal(), "No Principal found in Authentication !");
		Assert.notNull(authentication.getCredentials(), "No Credentials found in Authentication !");

		IAuthentication resultingAuth = null;

		// Try to authenticate with internal providers
		final Iterator<IAuthenticationProvider> internalProviderIt = this.internalAuthenticationProviders.iterator();
		while ((resultingAuth == null || !resultingAuth.isAuthenticated()) && internalProviderIt.hasNext()) {
			final IAuthenticationProvider provider = internalProviderIt.next();
			if (provider != null && provider.supports(authentication)) {
				try {
					resultingAuth = provider.authenticate(authentication);
				} catch (final AuthenticationException e) {
					// Nothing to do : try the next provider
				}
			}
		}

		// Try to authenticate with external providers
		if (!CollectionUtils.isEmpty(this.externalAuthenticationProviders)) {
			final Iterator<IAuthenticationProvider> externalProviderIt = this.externalAuthenticationProviders
					.iterator();
			while ((resultingAuth == null || !resultingAuth.isAuthenticated()) && externalProviderIt.hasNext()) {
				final IAuthenticationProvider provider = externalProviderIt.next();
				if (provider != null && provider.supports(authentication)) {
					try {
						resultingAuth = provider.authenticate(authentication);
					} catch (final AuthenticationException e) {
						// Nothing to do : try the next provider
					}
				}
			}
		}

		if (resultingAuth == null) {
			throw new AuthenticationException(authentication,
					"No Provider was able to authenticate the supplied IAuthentication object !");
		}

		return resultingAuth;
	}

	/**
	 * Getter of externalAuthenticationProviders.
	 * 
	 * @return the externalAuthenticationProviders
	 */
	public List<IAuthenticationProvider> getExternalAuthenticationProviders() {
		return this.externalAuthenticationProviders;
	}

	/**
	 * Setter of externalAuthenticationProviders.
	 * 
	 * @param externalAuthenticationProviders
	 *            the externalAuthenticationProviders to set
	 */
	public void setExternalAuthenticationProviders(final List<IAuthenticationProvider> externalAuthenticationProviders) {
		this.externalAuthenticationProviders = externalAuthenticationProviders;
	}

}
