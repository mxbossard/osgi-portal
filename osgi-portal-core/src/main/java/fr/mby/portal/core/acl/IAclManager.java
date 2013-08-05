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

import java.security.Principal;
import java.util.Set;

import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.core.security.PrincipalAlreadyExistsException;
import fr.mby.portal.core.security.PrincipalNotFoundException;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IAclManager {

	/**
	 * Register a Principal.
	 * 
	 * @param principal
	 * @throws PrincipalAlreadyExistsException
	 *             if the specified Principal was already registered previously
	 */
	void registerPrincipal(Principal principal) throws PrincipalAlreadyExistsException;

	/**
	 * Register the Set of IRole of a Principal.
	 * 
	 * @param principal
	 * @param roles
	 * @throws PrincipalNotFoundException
	 *             if the specified principal is not already registered
	 * @throws RoleNotFoundException
	 *             if specified roles are not registered
	 */
	void registerPrincipalRoles(Principal principal, Set<IRole> roles) throws PrincipalNotFoundException,
			RoleNotFoundException;

	/**
	 * Search for the Roles available to a Principal.
	 * 
	 * @param principal
	 * @return the Set of IRole available
	 * @throws PrincipalNotFoundException
	 *             if the specified principal is not already registered
	 */
	Set<IRole> retrievePrincipalRoles(Principal principal) throws PrincipalNotFoundException;

}
