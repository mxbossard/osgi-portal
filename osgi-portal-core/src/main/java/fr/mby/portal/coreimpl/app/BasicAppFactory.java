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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.core.app.IAppFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAppFactory implements IAppFactory {

	private static final String REQUEST_BUILD_INFO = "REQUEST_BUILD_INFO";

	@Override
	public IApp build(final HttpServletRequest request, final IAppConfig appConfig) {
		Assert.notNull(request, "No HttpServletRequest supplied !");
		Assert.notNull(appConfig, "No IAppConfig supplied !");

		final BasicApp app = new BasicApp(appConfig);

		app.setNamespace(this.generateNamespace(request, appConfig));
		app.setSignature(this.generateSignature(request, appConfig));

		return app;
	}

	/**
	 * @param request
	 * @param appConfig
	 * @return
	 */
	protected String generateSignature(final HttpServletRequest request, final IAppConfig appConfig) {

		return "signature_" + System.currentTimeMillis();
	}

	/**
	 * @param request
	 * @param appConfig
	 * @return
	 */
	protected String generateNamespace(final HttpServletRequest request, final IAppConfig appConfig) {
		BuildInfo buildInfo = (BuildInfo) request.getAttribute(BasicAppFactory.REQUEST_BUILD_INFO);

		if (buildInfo == null) {
			buildInfo = new BuildInfo();
			request.setAttribute(BasicAppFactory.REQUEST_BUILD_INFO, buildInfo);
		}

		final String sn = appConfig.getSymbolicName();

		final String namespace = sn + "_" + buildInfo.nextNamespaceCount(sn);

		return namespace;
	}

	private class BuildInfo {

		private final Map<String, Integer> namespaceCount = new HashMap<String, Integer>(8);

		public int nextNamespaceCount(final String namespace) {
			Integer count = this.namespaceCount.get(namespace);

			if (count == null) {
				count = 0;
				this.namespaceCount.put(namespace, 1);
			} else {
				this.namespaceCount.put(namespace, count + 1);
			}

			return count;
		}
	}
}
