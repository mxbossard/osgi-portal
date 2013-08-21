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

import java.util.Map;
import java.util.Set;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppContext;
import fr.mby.portal.api.app.IAppPreferences;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicAppConfig implements IAppConfig {

	private IAppContext context;

	private String symbolicName;

	private String version;

	private String defaultTitle;

	private String defaultWidth;

	private String defaultHeight;

	private RenderingMode renderingMode;

	private Set<IRole> declaredRoles;

	private Map<SpecialRole, IRole> specialRoles;

	private Set<IPermission> declaredPermissions;

	private Map<SpecialPermission, IPermission> specialPermissions;

	private Set<String> declaredPreferencesKey;

	private IAppPreferences defaultPreferences;

	/** Protected constructor. */
	protected BasicAppConfig() {
		super();
	}

	@Override
	public IRole getSpecialRole(final SpecialRole specialRole) {
		return this.specialRoles.get(specialRole);
	}

	@Override
	public IPermission getSpecialPermission(final SpecialPermission specialPermission) {
		return this.specialPermissions.get(specialPermission);
	}

	/**
	 * Getter of appContext.
	 * 
	 * @return the appContext
	 */
	@Override
	public IAppContext getContext() {
		return this.context;
	}

	/**
	 * Setter of appContext.
	 * 
	 * @param context
	 *            the appContext to set
	 */
	protected void setContext(final IAppContext context) {
		this.context = context;
	}

	/**
	 * Getter of symbolicName.
	 * 
	 * @return the symbolicName
	 */
	@Override
	public String getSymbolicName() {
		return this.symbolicName;
	}

	/**
	 * Setter of symbolicName.
	 * 
	 * @param symbolicName
	 *            the symbolicName to set
	 */
	protected void setSymbolicName(final String symbolicName) {
		this.symbolicName = symbolicName;
	}

	/**
	 * Getter of version.
	 * 
	 * @return the version
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/**
	 * Setter of version.
	 * 
	 * @param version
	 *            the version to set
	 */
	protected void setVersion(final String version) {
		this.version = version;
	}

	/**
	 * Getter of defaultTitle.
	 * 
	 * @return the defaultTitle
	 */
	@Override
	public String getDefaultTitle() {
		return this.defaultTitle;
	}

	/**
	 * Setter of defaultTitle.
	 * 
	 * @param defaultTitle
	 *            the defaultTitle to set
	 */
	protected void setDefaultTitle(final String defaultTitle) {
		this.defaultTitle = defaultTitle;
	}

	/**
	 * Getter of defaultWidth.
	 * 
	 * @return the defaultWidth
	 */
	@Override
	public String getDefaultWidth() {
		return this.defaultWidth;
	}

	/**
	 * Setter of defaultWidth.
	 * 
	 * @param defaultWidth
	 *            the defaultWidth to set
	 */
	protected void setDefaultWidth(final String defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	/**
	 * Getter of defaultHeight.
	 * 
	 * @return the defaultHeight
	 */
	@Override
	public String getDefaultHeight() {
		return this.defaultHeight;
	}

	/**
	 * Setter of defaultHeight.
	 * 
	 * @param defaultHeight
	 *            the defaultHeight to set
	 */
	protected void setDefaultHeight(final String defaultHeight) {
		this.defaultHeight = defaultHeight;
	}

	/**
	 * Getter of renderingMode.
	 * 
	 * @return the renderingMode
	 */
	@Override
	public RenderingMode getRenderingMode() {
		return this.renderingMode;
	}

	/**
	 * Setter of renderingMode.
	 * 
	 * @param renderingMode
	 *            the renderingMode to set
	 */
	protected void setRenderingMode(final RenderingMode renderingMode) {
		this.renderingMode = renderingMode;
	}

	/**
	 * Getter of declaredRoles.
	 * 
	 * @return the declaredRoles
	 */
	@Override
	public Set<IRole> getDeclaredRoles() {
		return this.declaredRoles;
	}

	/**
	 * Setter of declaredRoles.
	 * 
	 * @param declaredRoles
	 *            the declaredRoles to set
	 */
	protected void setDeclaredRoles(final Set<IRole> declaredRoles) {
		this.declaredRoles = declaredRoles;
	}

	/**
	 * Getter of declaredPermissions.
	 * 
	 * @return the declaredPermissions
	 */
	@Override
	public Set<IPermission> getDeclaredPermissions() {
		return this.declaredPermissions;
	}

	/**
	 * Setter of declaredPermissions.
	 * 
	 * @param declaredPermissions
	 *            the declaredPermissions to set
	 */
	protected void setDeclaredPermissions(final Set<IPermission> declaredPermissions) {
		this.declaredPermissions = declaredPermissions;
	}

	/**
	 * Getter of declaredPreferencesKey.
	 * 
	 * @return the declaredPreferencesKey
	 */
	@Override
	public Set<String> getDeclaredPreferencesKey() {
		return this.declaredPreferencesKey;
	}

	/**
	 * Setter of declaredPreferencesKey.
	 * 
	 * @param declaredPreferencesKey
	 *            the declaredPreferencesKey to set
	 */
	protected void setDeclaredPreferencesKey(final Set<String> declaredPreferencesKey) {
		this.declaredPreferencesKey = declaredPreferencesKey;
	}

	/**
	 * Getter of defaultPreferences.
	 * 
	 * @return the defaultPreferences
	 */
	@Override
	public IAppPreferences getDefaultPreferences() {
		return this.defaultPreferences;
	}

	/**
	 * Setter of defaultPreferences.
	 * 
	 * @param defaultPreferences
	 *            the defaultPreferences to set
	 */
	protected void setDefaultPreferences(final IAppPreferences defaultPreferences) {
		this.defaultPreferences = defaultPreferences;
	}

	/**
	 * Setter of specialRoles.
	 * 
	 * @param specialRoles
	 *            the specialRoles to set
	 */
	protected void setSpecialRoles(final Map<SpecialRole, IRole> specialRoles) {
		this.specialRoles = specialRoles;
	}

	/**
	 * Setter of specialPermissions.
	 * 
	 * @param specialPermissions
	 *            the specialPermissions to set
	 */
	protected void setSpecialPermissions(final Map<SpecialPermission, IPermission> specialPermissions) {
		this.specialPermissions = specialPermissions;
	}

}
