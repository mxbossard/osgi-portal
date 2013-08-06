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
public interface IAclDao {

	/**
	 * Create an IRole with some permissions.
	 * 
	 * @param role
	 * @throws RoleAlreadyExistsException
	 *             if specified role was already created previously
	 */
	void createRole(IRole role) throws RoleAlreadyExistsException;

	/**
	 * Find an IRole by its name.
	 * 
	 * @param role
	 * @return the found IRole
	 * @throws RoleNotFoundException
	 *             if no IRole match the specified name
	 */
	IRole findRole(String name) throws RoleNotFoundException;

	/**
	 * Update an IRole.
	 * 
	 * @param role
	 *            the IRole to reset
	 * @return the reseted IRole
	 * @throws RoleNotFoundException
	 *             if the specified IRole wasn't previously created
	 */
	void updateRole(IRole role) throws RoleNotFoundException;

	/**
	 * Register a new Principal.
	 * 
	 * @param principal
	 * @throws PrincipalAlreadyExistsException
	 *             if the Principal already exists
	 */
	void registerPrincipal(Principal principal) throws PrincipalAlreadyExistsException;

	/**
	 * Grant roles to a Principal.
	 * 
	 * @param principal
	 * @param roles
	 * @return the Set of every IRoles currently available for the Principal
	 * @throws RoleNotFoundException
	 *             if at least one of the specified IRole wasn't previously created
	 * @throws PrincipalNotFoundException
	 *             if the specified principal is not already known
	 */
	Set<IRole> grantRoles(Principal principal, Set<IRole> roles) throws RoleNotFoundException,
			PrincipalNotFoundException;

	/**
	 * Revoke roles from a Principal.
	 * 
	 * @param principal
	 * @param roles
	 * @return the Set of every IRoles currently available for the Principal
	 * @throws RoleNotFoundException
	 *             if at least one of the specified IRole wasn't previously created
	 * @throws PrincipalNotFoundException
	 *             if the specified principal is not already known
	 */
	Set<IRole> revokeRoles(Principal principal, Set<IRole> roles) throws RoleNotFoundException,
			PrincipalNotFoundException;

	/**
	 * Find the Set of every IRoles currently available for a principal.
	 * 
	 * @param principal
	 * @return the Set of every IRoles currently available for the Principal
	 * @throws PrincipalNotFoundException
	 *             if the specified principal is not already known
	 */
	Set<IRole> findPrincipalRoles(Principal principal) throws PrincipalNotFoundException;

}
