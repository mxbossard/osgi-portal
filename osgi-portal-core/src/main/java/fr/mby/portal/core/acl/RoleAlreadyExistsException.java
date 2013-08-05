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

package fr.mby.portal.core.acl;

import fr.mby.portal.api.acl.IRole;

/**
 * Exception thrown if someone attempt to create an IRole which already exists.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public class RoleAlreadyExistsException extends AbstractPortalException {

	/** Svuid. */
	private static final long serialVersionUID = 7768783350279360719L;

	private final IRole role;

	/**
	 * @param role
	 */
	public RoleAlreadyExistsException(final IRole role) {
		super(String.format("IRole with name: [%1$s] already exists !", role.getName()));
		this.role = role;
	}

	/**
	 * Getter of role.
	 * 
	 * @return the role
	 */
	public IRole getRole() {
		return this.role;
	}

}
