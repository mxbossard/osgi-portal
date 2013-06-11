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

package fr.mby.portal.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.mby.portal.api.app.IApp;

/**
 * IPortalRenderer implementation is in charge of rendering the portal.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public interface IPortalRenderer {

	static final String SIGNATURE_REQUEST_PARAM = "signature";

	void render(HttpServletRequest request, HttpServletResponse response) throws Exception;

	List<IApp> getAppsToRender(HttpServletRequest request) throws Exception;

}
