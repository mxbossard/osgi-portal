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

package fr.mby.opa.osgi.service;

import org.springframework.util.Assert;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import fr.mby.portal.api.IPortalService;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public abstract class OsgiPortalServiceLocator {

	public static WebApplicationContext retrieveWebApplicationContext() {
		final WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

		Assert.notNull(wac, "WebApplicationContext not initialized yet !");

		return wac;
	}

	public static IPortalService retrievePortalService() {
		final IPortalService portalService = OsgiPortalServiceLocator.retrieveWebApplicationContext().getBean(
				IPortalService.class);

		Assert.notNull(portalService, "No OSGi IPortalService service referenced in context !");

		return portalService;
	}

}
