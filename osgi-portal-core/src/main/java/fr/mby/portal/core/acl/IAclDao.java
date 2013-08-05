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

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IAclDao {

	/**
	 * Create an IRole with some permissions.
	 * 
	 * @param role
	 */
	void createRole(IRole role);
	
	/**
	 * Find an IRole by its name.
	 * 
	 * @param role
	 * @return the found IRole
	 */
	IRole findRole(String name);

	/**
	 * Update an IRole by adding it permissions.
	 * 
	 * @param role
	 *            the IRole to update
	 * @param permissions
	 *            the IPermissions to add to the Irole
	 * @return the updated IRole
	 */
	IRole addRolePermissions(IRole role, Set<IPermission> permissions);

	/**
	 * Reset an IRole.
	 * 
	 * @param role
	 *            the IRole to reset
	 * @return the reseted IRole
	 */
	void resetRole(IRole role);

	/**
	 * Grant roles to a Principal.
	 * 
	 * @param principal
	 * @param roles
	 * @return the Set of every IRoles currently available for the Principal
	 */
	Set<IRole> grantRoles(Principal principal, Set<IRole> roles);

	/**
	 * Revoke roles from a Principal.
	 * 
	 * @param principal
	 * @param roles
	 * @return the Set of every IRoles currently available for the Principal
	 */
	Set<IRole> revokeRoles(Principal principal, Set<IRole> roles);

	/**
	 * Find the Set of every IRoles currently available for a principal.
	 * 
	 * @param principal
	 * @return the Set of every IRoles currently available for the Principal
	 */
	Set<IRole> findPrincipalRoles(Principal principal);

}
