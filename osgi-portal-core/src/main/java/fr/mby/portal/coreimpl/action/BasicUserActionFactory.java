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

package fr.mby.portal.coreimpl.action;

import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.action.IUserAction;
import fr.mby.portal.context.IPortalContext;
import fr.mby.portal.core.action.IUserActionFactory;
import fr.mby.portal.core.context.IPortalContextResolver;
import fr.mby.portal.core.properties.IRequestPropertiesResolver;
import fr.mby.portal.core.security.IPrincipalResolver;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicUserActionFactory implements IUserActionFactory, InitializingBean {

	@Autowired
	/** Resolver for portal context. */
	private IPortalContextResolver<HttpServletRequest> portalContextResolver;

	@Autowired
	/** Resolver for principal. */
	private IPrincipalResolver<HttpServletRequest> principalResolver;

	@Autowired
	/** Resolver for properties. */
	private IRequestPropertiesResolver requestPropertiesResolver;

	@Override
	@SuppressWarnings("unchecked")
	public IUserAction build(final HttpServletRequest request) {
		Assert.notNull(request, "No HTTP request provided !");

		final IPortalContext portalContext = this.portalContextResolver.resolve(request);
		final Principal userPrincipal = this.principalResolver.resolve(request);
		final Map<String, Iterable<String>> properties = this.requestPropertiesResolver.resolve(request);
		final Map<String, String[]> parameters = request.getParameterMap();
		final Map<String, Object> attributes = this.extractAttributes(request);

		final BasicUserAction userAction = new BasicUserAction(portalContext, userPrincipal, properties, parameters,
				attributes);

		return userAction;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> extractAttributes(final HttpServletRequest request) {
		final Map<String, Object> attributes = new HashMap<String, Object>();

		final Enumeration<String> names = request.getAttributeNames();
		if (names != null) {
			while (names.hasMoreElements()) {
				final String name = names.nextElement();
				attributes.put(name, request.getAttribute(name));
			}
		}

		return attributes;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.portalContextResolver, "No IPortalContextResolver configured !");
		Assert.notNull(this.principalResolver, "No IPrincipalResolver configured !");
		Assert.notNull(this.requestPropertiesResolver, "No IRequestPropertiesResolver configured !");
	}

}