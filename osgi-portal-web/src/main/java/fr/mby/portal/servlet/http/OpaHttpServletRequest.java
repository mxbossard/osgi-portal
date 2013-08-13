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

package fr.mby.portal.servlet.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.IPortalRenderer;

/**
 * HttpServletRequestWrapper which allow to dispatch a request to an OPA.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public class OpaHttpServletRequest extends HttpServletRequestWrapper {

	private final IApp targetedApp;

	/**
	 * @param request
	 */
	public OpaHttpServletRequest(final HttpServletRequest request, final IApp app) {
		super(request);
		this.targetedApp = app;

		this.setAttribute(IPortalRenderer.SIGNATURE_PARAM_NAME, app.getSignature());
	}

	/**
	 * Getter of targetedApp.
	 * 
	 * @return the targetedApp
	 */
	public IApp getTargetedApp() {
		return this.targetedApp;
	}

}
