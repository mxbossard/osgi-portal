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
package fr.mby.portal.impl;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

import fr.mby.portal.IPortalContext;
import fr.mby.portal.IUserAction;
import fr.mby.portal.IUserActionFactory;

/**
 * @author Maxime Bossard - 2013
 *
 */
public class BasicUserActionFactory implements IUserActionFactory {

	@Override
	public IUserAction build(final HttpServletRequest request) {
		Assert.notNull(request, "No HTTP request provided !");
		
		IPortalContext portalContext = null;
		Principal userPrincipal = null;
		Map<String, Iterable<String>> properties = null;
		@SuppressWarnings("unchecked")
		Map<String, String[]> parameters = request.getParameterMap();
		Map<String, Object> attributes = null;
		
		final BasicUserAction userAction = new BasicUserAction(
				portalContext, userPrincipal, properties, parameters, attributes);
		
		return userAction;
	}

}
