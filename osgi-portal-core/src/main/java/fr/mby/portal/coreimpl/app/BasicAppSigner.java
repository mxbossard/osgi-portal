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

package fr.mby.portal.coreimpl.app;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.IPortal;
import fr.mby.portal.api.app.IApp;
import fr.mby.portal.core.app.IAppSigner;
import fr.mby.portal.core.session.ISessionManager;

/**
 * Generate the App signature and is able to retrieve it from HTTP request within param or cookie.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAppSigner implements IAppSigner {

	@Autowired
	private ISessionManager sessionManager;

	@Override
	public String generateSignature(final HttpServletRequest request, final IApp app) {
		String signature = app.getSignature();

		if (!StringUtils.hasText(signature)) {
			signature = this.generate(request, app);
		}

		return signature;
	}

	@Override
	public String retrieveSignature(final HttpServletRequest request) {
		String signature = null;

		final Object attrObject = request.getAttribute(IPortal.SIGNATURE_PARAM_NAME);
		if (attrObject != null && attrObject instanceof String) {
			signature = (String) attrObject;
		}

		if (!StringUtils.hasText(signature)) {
			signature = request.getParameter(IPortal.SIGNATURE_PARAM_NAME);
		}

		if (!StringUtils.hasText(signature)) {
			final Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (final Cookie cookie : cookies) {
					if (cookie != null && IPortal.SIGNATURE_PARAM_NAME.equals(cookie.getName())) {
						signature = cookie.getValue();
					}
				}
			}
		}

		if (!StringUtils.hasText(signature)) {
			request.setAttribute(IPortal.SIGNATURE_PARAM_NAME, signature);
		}

		return signature;
	}

	/**
	 * @param request
	 * @param appConfig
	 * @return
	 */
	protected String generate(final HttpServletRequest request, final IApp app) {

		return "signature_" + app.hashCode() + "_" + System.currentTimeMillis() + "_"
				+ this.sessionManager.getPortalSessionId(request);
	}

}
