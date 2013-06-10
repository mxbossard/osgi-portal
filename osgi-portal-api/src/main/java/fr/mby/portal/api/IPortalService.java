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

package fr.mby.portal.api;

import javax.servlet.http.HttpServletRequest;

import fr.mby.portal.api.app.IApp;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IPortalService {

	/**
	 * Retrieve the IApp targeted by this HTTP request.
	 * 
	 * @param request
	 * @return
	 */
	IApp getTargetedApp(HttpServletRequest request);

}
