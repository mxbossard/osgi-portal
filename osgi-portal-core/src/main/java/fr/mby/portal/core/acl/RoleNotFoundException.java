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

/**
 * Thrown if no Role was found after a search.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public class RoleNotFoundException extends AbstractPortalException {

	/** Svuid. */
	private static final long serialVersionUID = 2128397941356415777L;

	private final String roleName;

	/**
	 * @param role
	 */
	public RoleNotFoundException(final String roleName) {
		super(String.format("IRole with name: [%1$s] was not found !", roleName));
		this.roleName = roleName;
	}

	/**
	 * Getter of roleName.
	 * 
	 * @return the roleName
	 */
	public String getRoleName() {
		return this.roleName;
	}

}
