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

	private IAppContext context;

	private String symbolicName;

	private String version;

	private String defaultTitle;

	private String defaultWidth;

	private String defaultHeight;

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
	public IAppContext getContext() {
		return this.context;
	}

	/**
	 * Setter of appContext.
	 * 
	 * @param context
	 *            the appContext to set
	 */
	public void setContext(final IAppContext context) {
		this.context = context;
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

	/**
	 * Getter of defaultTitle.
	 * 
	 * @return the defaultTitle
	 */
	@Override
	public String getDefaultTitle() {
		return this.defaultTitle;
	}

	/**
	 * Setter of defaultTitle.
	 * 
	 * @param defaultTitle
	 *            the defaultTitle to set
	 */
	public void setDefaultTitle(final String defaultTitle) {
		this.defaultTitle = defaultTitle;
	}

	/**
	 * Getter of defaultWidth.
	 * 
	 * @return the defaultWidth
	 */
	@Override
	public String getDefaultWidth() {
		return this.defaultWidth;
	}

	/**
	 * Setter of defaultWidth.
	 * 
	 * @param defaultWidth
	 *            the defaultWidth to set
	 */
	public void setDefaultWidth(final String defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	/**
	 * Getter of defaultHeight.
	 * 
	 * @return the defaultHeight
	 */
	@Override
	public String getDefaultHeight() {
		return this.defaultHeight;
	}

	/**
	 * Setter of defaultHeight.
	 * 
	 * @param defaultHeight
	 *            the defaultHeight to set
	 */
	public void setDefaultHeight(final String defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

}
