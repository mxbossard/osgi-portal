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

	@Override
	public void log(final String message, final Object[]... objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public void log(final String message, final Throwable throwable, final Object[]... objects) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IAppContext#getBundleId()
	 */
	@Override
	public int getBundleId() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.mby.portal.api.app.IAppContext#getWebContextPath()
	 */
	@Override
	public String getWebContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

}
