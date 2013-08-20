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

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.core.app.IAppConfigFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicAppConfigFactory implements IAppConfigFactory {

	@Override
	public IAppConfig build(final Bundle appBundle) throws AppConfigNotFoundException {
		Assert.notNull(appBundle, "No bunlde supplied !");

		final BasicAppConfig appConfig = new BasicAppConfig();

		appConfig.setSymbolicName(appBundle.getSymbolicName());
		appConfig.setVersion(appBundle.getVersion().toString());

		final String opaSn = appBundle.getSymbolicName();
		final Properties opaConfig = this.loadOpaConfig(appBundle);
		appConfig.setDefaultTitle(this.getPropertyValue(opaSn, opaConfig, OpaConfigKeys.DEFAULT_TITLE));
		appConfig.setDefaultWidth(this.getPropertyValue(opaSn, opaConfig, OpaConfigKeys.DEFAULT_WIDTH));
		appConfig.setDefaultHeight(this.getPropertyValue(opaSn, opaConfig, OpaConfigKeys.DEFAULT_HEIGHT));

		final BasicAppContext appContext = new BasicAppContext();
		appContext.setBundleId(appBundle.getBundleId());
		appContext.setWebContextPath(this.buildWebAppBundlePath(appBundle));

		appConfig.setContext(appContext);
		return appConfig;
	}

	protected Properties loadOpaConfig(final Bundle opaBundle) throws AppConfigNotFoundException {
		final URL opaPropertiesUrl = opaBundle.getResource(IAppConfigFactory.OPA_CONFIG_FILE_PATH);
		if (opaPropertiesUrl == null) {
			final String message = String.format("No OPA configuration file (%1$s) found in OPA [%2$s] !",
					IAppConfigFactory.OPA_CONFIG_FILE_PATH, opaBundle.getSymbolicName());
			throw new AppConfigNotFoundException(message);
		}

		final Properties props = new Properties();
		try {
			props.load(opaPropertiesUrl.openStream());
		} catch (final IOException e) {
			final String message = String.format("Unable to open OPA configuration file (%1$s) found in OPA [%2$s] !",
					IAppConfigFactory.OPA_CONFIG_FILE_PATH, opaBundle.getSymbolicName());
			throw new AppConfigNotFoundException(message, e);
		}

		return props;
	}

	protected String getPropertyValue(final String opaSymbolicName, final Properties opaProps,
			final OpaConfigKeys opaConfigKey) throws AppConfigNotFoundException {
		final String propertyKey = opaConfigKey.getKey();
		final String propertyValue = opaProps.getProperty(propertyKey);

		if (!StringUtils.hasText(propertyValue)) {
			final String message = String.format("Missing property '%1$s' detected in OPA [%2$s] configuration file !",
					propertyKey, opaSymbolicName);
			throw new AppConfigNotFoundException(message);
		}

		return propertyValue;

	}

	/**
	 * @param webContextPathHeader
	 * @return
	 */
	protected String buildWebAppBundlePath(final Bundle bundle) {
		final String webContextPathHeader = (String) bundle.getHeaders().get(
				IAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER);
		final String webBundlePath = "/".concat(webContextPathHeader.replaceAll("[\\/\\\\]+", ""));

		return webBundlePath;
	}

}
