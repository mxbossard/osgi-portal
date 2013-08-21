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

package fr.mby.portal.api.app;

import java.util.Set;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IAppConfig {

	/**
	 * Returns the IAppContext of the application.
	 * 
	 * @return the IAppContext of the application
	 */
	IAppContext getContext();

	/**
	 * Return the symbolic name of the IApp.
	 * 
	 * @return the symbolic name of the IApp.
	 */
	String getSymbolicName();

	/**
	 * Return the version of the IApp.
	 * 
	 * @return the version of the IApp.
	 */
	String getVersion();

	/**
	 * The default width of an OPA.
	 * 
	 * @return the IApp width
	 */
	String getDefaultWidth();

	/**
	 * The default height of an OPA.
	 * 
	 * @return the IApp height
	 */
	String getDefaultHeight();

	/**
	 * The default title of an OPA.
	 * 
	 * @return the IApp title
	 */
	String getDefaultTitle();

	/**
	 * The OPA rendering mode.
	 * 
	 * @return the IApp rendering mode
	 */
	RenderingMode getRenderingMode();

	/**
	 * OPA declared roles.
	 * 
	 * @return the roles declared to be used by the OPA.
	 */
	Set<IRole> getDeclaredRoles();

	/**
	 * Retireve an OPA special role.
	 * 
	 * @return the special role for this OPA.
	 */
	IRole getSpecialRole(SpecialRole specialRole);

	/**
	 * OPA declared permissions.
	 * 
	 * @return the permissions declared to be used by the OPA.
	 */
	Set<IPermission> getDeclaredPermissions();

	/**
	 * Retireve an OPA special permission.
	 * 
	 * @return the special permission for this OPA.
	 */
	IPermission getSpecialPermission(SpecialPermission specialPermission);

	/**
	 * The list of preferences key declared by this App.
	 * 
	 * @return The list of preferences key declared by this App.
	 */
	Set<String> getDeclaredPreferencesKey();

	/**
	 * The default configured preferences.
	 * 
	 * @return The default configured preferences.
	 */
	IAppPreferences getDefaultPreferences();

	/**
	 * Available OPA rendering modes.
	 * 
	 * @author Maxime Bossard - 2013
	 * 
	 */
	public enum RenderingMode {
		IFRAMED, RENDERED;
	}

	/**
	 * Special roles always available for an OPA.
	 * 
	 * @author Maxime Bossard - 2013
	 * 
	 */
	public enum SpecialRole {
		GUEST, LOGGED, ADMIN;
	}

	/**
	 * Special permissions always available for an OPA.
	 * 
	 * @author Maxime Bossard - 2013
	 * 
	 */
	public enum SpecialPermission {
		CAN_RENDER, CAN_EDIT;
	}

}
