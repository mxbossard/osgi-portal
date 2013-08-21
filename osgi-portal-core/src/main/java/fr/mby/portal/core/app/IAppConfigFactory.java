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

import org.osgi.framework.Bundle;

import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.coreimpl.context.AppConfigNotFoundException;
import fr.mby.portal.coreimpl.context.BadAppConfigException;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IAppConfigFactory {

	public static final Object WEB_CONTEXT_PATH_BUNDLE_HEADER = "Web-ContextPath";

	public static final String OPA_CONFIG_FILE_PATH = "/WEB-INF/opa.properties";

	public static final String OPA_CONFIG_LIST_SPLITER = ",";

	IAppConfig build(Bundle appBundle) throws AppConfigNotFoundException, BadAppConfigException;

	public enum OpaConfigKeys {

		/** OPA display Title. */
		DEFAULT_TITLE("opa.title.default"),

		/** OPA default display width. */
		DEFAULT_WIDTH("opa.width.default"),

		/** OPA default display height. */
		DEFAULT_HEIGHT("opa.height.default"),

		/** OPA rendering mode : iframed / rendered */
		RENDERING_MODE("opa.rendering.mode"),

		/** List roles declared by the OPA. */
		ACL_ROLES("opa.acl.roles"),

		/** List permissions declared by the OPA. */
		ACL_PERMISSIONS("opa.acl.permissions"),

		/** Roles able to render the OPA. */
		ACL_SUBROLES_ASSIGNMENT_SUFFIX(".subRoles"),

		/** Roles able to render the OPA. */
		ACL_PERMISSIONS_ASSIGNMENT_SUFFIX(".perms"),

		/** List of preferences key declared by the OPA. */
		PREFERENCES("opa.preferences");

		private final String key;

		OpaConfigKeys(final String key) {
			this.key = key;
		}

		/**
		 * Getter of key.
		 * 
		 * @return the key
		 */
		public String getKey() {
			return this.key;
		}

	}
}