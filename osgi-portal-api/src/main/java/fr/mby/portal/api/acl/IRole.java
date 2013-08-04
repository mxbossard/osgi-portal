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

package fr.mby.portal.api.acl;

import java.util.Set;

/**
 * Represents a Role which aggregate multiple permissions. A Role may aggregate multiple sub-roles and be granted their
 * permissions.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public interface IRole {

	/**
	 * Return the role name.
	 * 
	 * @return the role name
	 */
	String getName();

	/**
	 * Test if the specified permission is granted by this Role.
	 * 
	 * @param permission
	 * @return true if the specified permission is granted by this Role
	 */
	boolean isGranted(IPermission permission);

	/**
	 * Retrieve all permissions granted to this role.
	 * 
	 * @return a Set of all permissions granted to this role
	 */
	Set<IPermission> getPermissions();

	/**
	 * Retrieve all sub-roles which are included in this role.
	 * 
	 * @return a set of all sub-roles included in this role
	 */
	Set<IRole> getSubRoles();

}
