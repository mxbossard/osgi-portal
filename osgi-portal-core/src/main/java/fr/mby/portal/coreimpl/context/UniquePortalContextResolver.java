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

package fr.mby.portal.coreimpl.context;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import fr.mby.portal.api.context.IPortalContext;
import fr.mby.portal.core.context.IPortalContextResolver;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class UniquePortalContextResolver implements IPortalContextResolver<HttpServletRequest>, InitializingBean {

	private IPortalContext uniquePortalContext;

	@Override
	public IPortalContext resolve(final HttpServletRequest object) {
		return this.uniquePortalContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.uniquePortalContext = new BasicPortalContext();
	}

}
