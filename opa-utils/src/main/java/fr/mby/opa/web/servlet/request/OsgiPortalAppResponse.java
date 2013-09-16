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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.mby.portal.api.IPortal;

/**
 * Override encodeURL methods to add Opa signature parameter.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public class OsgiPortalAppResponse extends HttpServletResponseWrapper {

	/** Backing response. */
	private final HttpServletResponse response;

	private final String opaSignature;

	/**
	 * @param response
	 */
	public OsgiPortalAppResponse(final HttpServletResponse response, final String opaSignature) {
		super(response);

		Preconditions.checkArgument(!Strings.isNullOrEmpty(opaSignature), "");

		this.response = response;
		this.opaSignature = opaSignature;
	}

	@Override
	public String encodeURL(final String url) {
		final String encodedUrl = this.response.encodeURL(url);
		final String urlWithSignature = this.appendOpaSignature(encodedUrl);

		return urlWithSignature;
	}

	@Override
	@SuppressWarnings("deprecation")
	public String encodeUrl(final String url) {
		final String encodedUrl = this.response.encodeUrl(url);
		final String urlWithSignature = this.appendOpaSignature(encodedUrl);

		return urlWithSignature;
	}

	/**
	 * @param url
	 * @param encodedUrl
	 * @return
	 * @throws ServletException
	 */
	protected String appendOpaSignature(final String encodedUrl) {
		final StringBuilder sb = new StringBuilder(encodedUrl.length() + 128);
		sb.append(encodedUrl);

		if (encodedUrl.contains("?")) {
			sb.append("&");
		} else {
			sb.append("?");
		}

		sb.append(IPortal.SIGNATURE_PARAM_NAME + "=");
		sb.append(this.opaSignature);

		return sb.toString();
	}

}
