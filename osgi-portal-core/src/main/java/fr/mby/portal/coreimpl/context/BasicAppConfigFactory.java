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

import org.osgi.framework.Bundle;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.core.app.IAppConfigFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAppConfigFactory implements IAppConfigFactory {

	public static final Object WEB_CONTEXT_PATH_BUNDLE_HEADER = "Web-ContextPath";

	@Override
	public IAppConfig build(final Bundle appBundle) {
		Assert.notNull(appBundle, "No bunlde supplied !");

		final BasicAppConfig appConfig = new BasicAppConfig();

		appConfig.setSymbolicName(appBundle.getSymbolicName());
		appConfig.setVersion(appBundle.getVersion().toString());

		final BasicAppContext appContext = new BasicAppContext();
		appContext.setBundleId(appBundle.getBundleId());
		appContext.setWebContextPath(this.buildWebAppBundlePath(appBundle));

		appConfig.setAppContext(appContext);
		return appConfig;
	}

	/**
	 * @param webContextPathHeader
	 * @return
	 */
	protected String buildWebAppBundlePath(final Bundle bundle) {
		final String webContextPathHeader = (String) bundle.getHeaders().get(
				BasicAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER);
		final String webBundlePath = "/".concat(webContextPathHeader.replaceAll("[\\/\\\\]+", ""));

		return webBundlePath;
	}

}
