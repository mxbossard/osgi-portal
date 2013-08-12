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

package fr.mby.portal.core.app;

import javax.servlet.http.HttpServletRequest;

import fr.mby.portal.api.app.IApp;

/**
 * Responsible for IApp signing.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public interface IAppSigner {

	/**
	 * Generate the signature of an IApp.
	 * 
	 * @param request
	 *            the HTTP request.
	 * @param app
	 *            the IApp to sign.
	 * @return the IApp signature.
	 */
	String generateSignature(HttpServletRequest request, IApp app);

	/**
	 * Retrieve the signature of an IApp relative to a HTTP request.
	 * 
	 * @param request
	 *            the HTTP request.
	 * @return the IApp signature or null if no signature can be found.
	 */
	String retrieveSignature(HttpServletRequest request);

}
