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

import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppPreferences;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicApp implements IApp {

	private final IAppConfig config;

	private final IAppPreferences preferences;

	private String namespace;

	private String signature;

	private String width;

	private String height;

	private String title;

	private String webPath;

	@Override
	public String toString() {
		return "BasicApp [namespace= " + this.namespace + " , signature= " + this.signature + "]";
	}

	/**
	 * @param appConfig
	 */
	protected BasicApp(final IAppConfig appConfig, final IAppPreferences appPreferences) {
		super();
		this.config = appConfig;
		this.preferences = appPreferences;
	}

	/**
	 * Getter of appConfig.
	 * 
	 * @return the appConfig
	 */
	@Override
	public IAppConfig getConfig() {
		return this.config;
	}

	@Override
	public IAppPreferences getPreferences() {
		return this.preferences;
	}

	/**
	 * Getter of namespace.
	 * 
	 * @return the namespace
	 */
	@Override
	public String getNamespace() {
		return this.namespace;
	}

	/**
	 * Setter of namespace.
	 * 
	 * @param namespace
	 *            the namespace to set
	 */
	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Getter of signature.
	 * 
	 * @return the signature
	 */
	@Override
	public String getSignature() {
		return this.signature;
	}

	/**
	 * Setter of signature.
	 * 
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(final String signature) {
		this.signature = signature;
	}

	/**
	 * Getter of width.
	 * 
	 * @return the width
	 */
	@Override
	public String getWidth() {
		return this.width;
	}

	/**
	 * Setter of width.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(final String width) {
		this.width = width;
	}

	/**
	 * Getter of height.
	 * 
	 * @return the height
	 */
	@Override
	public String getHeight() {
		return this.height;
	}

	/**
	 * Setter of height.
	 * 
	 * @param height
	 *            the height to set
	 */
	public void setHeight(final String height) {
		this.height = height;
	}

	/**
	 * Getter of title.
	 * 
	 * @return the title
	 */
	@Override
	public String getTitle() {
		return this.title;
	}

	/**
	 * Setter of title.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Getter of webPath.
	 * 
	 * @return the webPath
	 */
	@Override
	public String getWebPath() {
		return this.webPath;
	}

	/**
	 * Setter of webPath.
	 * 
	 * @param webPath
	 *            the webPath to set
	 */
	public void setWebPath(final String webPath) {
		this.webPath = webPath;
	}

}
