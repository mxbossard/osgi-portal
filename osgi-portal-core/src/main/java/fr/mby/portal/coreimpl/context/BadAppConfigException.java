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

import fr.mby.portal.core.acl.AbstractPortalException;

/**
 * Thrown if bad configuration in OPA.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public class BadAppConfigException extends AbstractPortalException {

	/** Svuid. */
	private static final long serialVersionUID = 1268532688814721790L;

	/**
	 * @param message
	 */
	public BadAppConfigException(final String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	protected BadAppConfigException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
