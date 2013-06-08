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

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicApp implements IApp {

	private final IAppConfig appConfig;

	private String namespace;

	private String signature;

	private int width;

	private int height;

	/**
	 * @param appConfig
	 */
	protected BasicApp(final IAppConfig appConfig) {
		super();
		this.appConfig = appConfig;
	}

	/**
	 * Getter of appConfig.
	 * 
	 * @return the appConfig
	 */
	@Override
	public IAppConfig getAppConfig() {
		return this.appConfig;
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
	public int getWidth() {
		return this.width;
	}

	/**
	 * Setter of width.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * Getter of height.
	 * 
	 * @return the height
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

	/**
	 * Setter of height.
	 * 
	 * @param height
	 *            the height to set
	 */
	public void setHeight(final int height) {
		this.height = height;
	}

}
