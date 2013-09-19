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

package fr.mby.opa.web.servlet.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.Assert;

import fr.mby.opa.osgi.service.OsgiPortalServiceLocator;
import fr.mby.portal.api.IPortalService;
import fr.mby.portal.api.app.IApp;

/**
 * Override encodeURL methods to add Opa signature parameter.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public class OsgiPortalAppRequest extends HttpServletRequestWrapper {

	/** Backing request. */
	@SuppressWarnings("unused")
	private final HttpServletRequest request;

	private final String opaSignature;

	private final IApp opaApp;

	/**
	 * @param response
	 */
	public OsgiPortalAppRequest(final HttpServletRequest request, final String opaSignature) {
		super(request);

		Assert.hasText(opaSignature, "No OPA signature supplied !");

		this.request = request;
		this.opaSignature = opaSignature;

		final IPortalService portalService = OsgiPortalServiceLocator.retrievePortalService();
		this.opaApp = portalService.getTargetedApp(request);
	}

	/**
	 * Getter of opaSignature.
	 * 
	 * @return the opaSignature
	 */
	public String getOpaSignature() {
		return this.opaSignature;
	}

	/**
	 * Getter of opaApp.
	 * 
	 * @return the opaApp
	 */
	public IApp getOpaApp() {
		return this.opaApp;
	}

}
