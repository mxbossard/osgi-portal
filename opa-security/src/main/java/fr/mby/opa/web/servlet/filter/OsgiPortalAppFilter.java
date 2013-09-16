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

package fr.mby.opa.web.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

import fr.mby.opa.web.servlet.request.OsgiPortalAppResponse;
import fr.mby.portal.api.IPortal;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class OsgiPortalAppFilter implements Filter {

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		final String opaSignature = request.getParameter(IPortal.SIGNATURE_PARAM_NAME);

		OsgiPortalAppFilter.checkOpaSignature(opaSignature);

		request.setAttribute(IPortal.SIGNATURE_PARAM_NAME, opaSignature);

		if (HttpServletResponse.class.isAssignableFrom(response.getClass())) {
			final HttpServletResponse httpResponse = (HttpServletResponse) response;
			final OsgiPortalAppResponse opaResponse = new OsgiPortalAppResponse(httpResponse, opaSignature);

			chain.doFilter(request, opaResponse);
		} else {
			chain.doFilter(request, response);
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public static String getOpaSignature(final ServletRequest request) throws ServletException {
		final String opaSignature = (String) request.getAttribute(IPortal.SIGNATURE_PARAM_NAME);

		return opaSignature;
	}

	protected static void checkOpaSignature(final String opaSignature) throws ServletException {
		if (Strings.isNullOrEmpty(opaSignature)) {
			throw new ServletException("OSGi Portal Application signature cannot be found in request !");
		}
	}

}
