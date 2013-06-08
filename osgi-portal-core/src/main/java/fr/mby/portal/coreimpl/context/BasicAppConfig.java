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

import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppContext;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicAppConfig implements IAppConfig {

	private IAppContext appContext;

	private String symbolicName;

	private String version;

	/** Protected constructor. */
	protected BasicAppConfig() {
		super();
	}

	/**
	 * Getter of appContext.
	 * 
	 * @return the appContext
	 */
	@Override
	public IAppContext getAppContext() {
		return this.appContext;
	}

	/**
	 * Setter of appContext.
	 * 
	 * @param appContext
	 *            the appContext to set
	 */
	public void setAppContext(final IAppContext appContext) {
		this.appContext = appContext;
	}

	/**
	 * Getter of symbolicName.
	 * 
	 * @return the symbolicName
	 */
	@Override
	public String getSymbolicName() {
		return this.symbolicName;
	}

	/**
	 * Setter of symbolicName.
	 * 
	 * @param symbolicName
	 *            the symbolicName to set
	 */
	public void setSymbolicName(final String symbolicName) {
		this.symbolicName = symbolicName;
	}

	/**
	 * Getter of version.
	 * 
	 * @return the version
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/**
	 * Setter of version.
	 * 
	 * @param version
	 *            the version to set
	 */
	public void setVersion(final String version) {
		this.version = version;
	}

}
