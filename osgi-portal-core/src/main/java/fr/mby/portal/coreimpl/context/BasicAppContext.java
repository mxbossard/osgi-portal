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

import fr.mby.portal.api.app.IAppContext;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicAppContext implements IAppContext {

	private long bundleId;

	private String webContextPath;

	/** Protected builder. */
	protected BasicAppContext() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void log(final String message, final Object[]... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public void log(final String message, final Throwable throwable, final Object[]... objects) {
		// TODO Auto-generated method stub

	}

	/**
	 * Getter of bundleId.
	 * 
	 * @return the bundleId
	 */
	@Override
	public long getBundleId() {
		return this.bundleId;
	}

	/**
	 * Setter of bundleId.
	 * 
	 * @param bundleId
	 *            the bundleId to set
	 */
	public void setBundleId(final long bundleId) {
		this.bundleId = bundleId;
	}

	/**
	 * Getter of webContextPath.
	 * 
	 * @return the webContextPath
	 */
	@Override
	public String getWebContextPath() {
		return this.webContextPath;
	}

	/**
	 * Setter of webContextPath.
	 * 
	 * @param webContextPath
	 *            the webContextPath to set
	 */
	public void setWebContextPath(final String webContextPath) {
		this.webContextPath = webContextPath;
	}

}
