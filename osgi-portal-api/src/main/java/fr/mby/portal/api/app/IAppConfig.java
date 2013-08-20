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
	 * Test if the Role can render this OPA.
	 * 
	 * @param role
	 *            the role to test.
	 * @return true if the role is authorized to render the OPA.
	 */
	boolean canRender(IRole role);

	/**
	 * Test if the role can edit this OPA.
	 * 
	 * @param role
	 *            the role to test.
	 * @return true if the role is authorized to edit the OPA.
	 */
	boolean canEdit(IRole role);

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

	public enum RenderingMode {
		IFRAMED, RENDERED;
	}

}
