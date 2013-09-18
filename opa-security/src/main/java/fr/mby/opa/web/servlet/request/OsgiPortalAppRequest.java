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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

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

	/**
	 * @param response
	 */
	public OsgiPortalAppRequest(final HttpServletRequest request, final String opaSignature) {
		super(request);

		Preconditions.checkArgument(!Strings.isNullOrEmpty(opaSignature), "No OPA signature supplied !");

		this.request = request;
		this.opaSignature = opaSignature;
	}

	/**
	 * Getter of opaSignature.
	 * 
	 * @return the opaSignature
	 */
	public String getOpaSignature() {
		return this.opaSignature;
	}

}
