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

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IAppConfig.RenderingMode;
import fr.mby.portal.api.app.IAppConfig.SpecialPermission;
import fr.mby.portal.api.app.IAppConfig.SpecialRole;
import fr.mby.portal.core.acl.IAclManager;
import fr.mby.portal.core.acl.IPermissionFactory;
import fr.mby.portal.core.acl.IRoleFactory;
import fr.mby.portal.core.app.IAppConfigFactory;
import fr.mby.portal.core.cache.ICachingService;
import fr.mby.portal.core.preferences.IAppPreferencesManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class PropertiesAppConfigFactory implements IAppConfigFactory, ICachingService {

	private final Map<BundleContext, IAppConfig> configCache = new WeakHashMap<BundleContext, IAppConfig>(8);

	@Autowired
	private IRoleFactory roleFactory;

	@Autowired
	private IPermissionFactory permissionFactory;

	@Autowired
	private IAclManager aclManager;

	@Autowired
	private IAppPreferencesManager appPreferencesManager;

	@Override
	public IAppConfig build(final Bundle bundleApp) throws AppConfigNotFoundException, BadAppConfigException {
		Assert.notNull(bundleApp, "No bunlde supplied !");

		final BundleContext bundleContext = bundleApp.getBundleContext();
		if (bundleContext == null) {
			throw new IllegalStateException("No BundleCotext attache to this OPA !");
		}

		IAppConfig appConfig = this.configCache.get(bundleApp.getBundleContext());
		if (appConfig == null) {
			appConfig = this.buildConfig(bundleApp);
			this.configCache.put(bundleContext, appConfig);
		}

		return appConfig;
	}

	/**
	 * @param bundleApp
	 * @return
	 * @throws AppConfigNotFoundException
	 * @throws BadAppConfigException
	 */
	protected BasicAppConfig buildConfig(final Bundle bundleApp) throws AppConfigNotFoundException,
			BadAppConfigException {
		final BasicAppConfig appConfig = new BasicAppConfig();

		appConfig.setSymbolicName(bundleApp.getSymbolicName());
		appConfig.setVersion(bundleApp.getVersion().toString());

		// ---------- IApp Context ----------
		final BasicAppContext appContext = new BasicAppContext();
		appContext.setBundleId(bundleApp.getBundleId());
		appContext.setWebContextPath(this.buildWebAppBundlePath(bundleApp));
		appConfig.setContext(appContext);

		final String opaSn = bundleApp.getSymbolicName();
		final Properties opaConfig = this.loadOpaConfig(bundleApp);

		// ---------- Diplay configuration ----------
		appConfig.setDefaultTitle(this.getMandatoryValue(opaSn, opaConfig, OpaConfigKeys.DEFAULT_TITLE));
		appConfig.setDefaultWidth(this.getMandatoryValue(opaSn, opaConfig, OpaConfigKeys.DEFAULT_WIDTH));
		appConfig.setDefaultHeight(this.getMandatoryValue(opaSn, opaConfig, OpaConfigKeys.DEFAULT_HEIGHT));

		// ---------- Rendering configuration ----------
		try {
			final String renderingModeVal = this.getMandatoryValue(opaSn, opaConfig, OpaConfigKeys.RENDERING_MODE);
			appConfig.setRenderingMode(Enum.valueOf(RenderingMode.class, renderingModeVal));
		} catch (final IllegalArgumentException e) {
			throw new AppConfigNotFoundException("Bad [" + OpaConfigKeys.RENDERING_MODE.getKey() + "] property value !");
		}

		// ---------- ACL configuration ----------

		// ----- Permissions -----
		final Map<String, IPermission> permissionsMap = this.processAclPermissions(bundleApp, appConfig, opaConfig);

		// ----- Roles -----
		this.processAclRoles(bundleApp, appConfig, opaConfig, permissionsMap);

		// ---------- Preferences configuration ----------
		this.processPreferences(appConfig, opaConfig);

		return appConfig;
	}

	@Override
	public void clearCache() {
		this.configCache.clear();
	}

	/**
	 * Build unique final permission name.
	 * 
	 * @param bundle
	 * @param permissionName
	 * @return
	 */
	protected String buildFinalPermissionName(final Bundle bundleApp, final String permissionName) {
		return bundleApp.getSymbolicName().concat("_").concat(permissionName);
	}

	/**
	 * Build unique final role name.
	 * 
	 * @param bundle
	 * @param roleName
	 * @return
	 */
	protected String buildFinalRoleName(final Bundle bundleApp, final String roleName) {
		return bundleApp.getSymbolicName().concat("_").concat(roleName);
	}

	/**
	 * Process ACL permissions map from OPA config.
	 * 
	 * @param bundleApp
	 * @param appConfig
	 * @param opaConfig
	 * 
	 * @return the map of all configured permissions.
	 * @throws AppConfigNotFoundException
	 */
	protected Map<String, IPermission> processAclPermissions(final Bundle bundleApp, final BasicAppConfig appConfig,
			final Properties opaConfig) {
		final Map<String, IPermission> permissionsByAclName = new HashMap<String, IPermission>(4);

		// Special Permissions
		final Map<SpecialPermission, IPermission> specialPermissions = new HashMap<SpecialPermission, IPermission>(
				SpecialPermission.values().length);
		for (final SpecialPermission spEnum : SpecialPermission.values()) {
			final String finalPermissionName = this.buildFinalPermissionName(bundleApp, spEnum.name());
			final IPermission specialPerm = this.permissionFactory.build(finalPermissionName);
			specialPermissions.put(spEnum, specialPerm);
			permissionsByAclName.put(spEnum.name(), specialPerm);
		}
		appConfig.setSpecialPermissions(Collections.unmodifiableMap(specialPermissions));

		// Declared Permissions
		final String aclPermissionsVal = this.getOptionalValue(opaConfig, OpaConfigKeys.ACL_PERMISSIONS.getKey());
		final String[] aclPermissionsArray = StringUtils.delimitedListToStringArray(aclPermissionsVal,
				IAppConfigFactory.OPA_CONFIG_LIST_SPLITER);
		if (aclPermissionsArray != null) {
			for (final String aclPermissionName : aclPermissionsArray) {
				if (StringUtils.hasText(aclPermissionName)) {
					final String finalPermissionName = this.buildFinalPermissionName(bundleApp, aclPermissionName);
					final IPermission permission = this.permissionFactory.build(finalPermissionName);
					permissionsByAclName.put(aclPermissionName, permission);
				}
			}
		}
		final Set<IPermission> permissionsSet = new HashSet<IPermission>(permissionsByAclName.values());
		appConfig.setDeclaredPermissions(Collections.unmodifiableSet(permissionsSet));

		return permissionsByAclName;
	}

	/**
	 * Process ACL roles map from OPA config.
	 * 
	 * @param bundleApp
	 * @param appConfig
	 * @param opaConfig
	 * @param permissionsMap
	 * 
	 * @return the map of all configured roles.
	 * @throws AppConfigNotFoundException
	 * @throws BadAppConfigException
	 */
	protected Map<String, IRole> processAclRoles(final Bundle bundleApp, final BasicAppConfig appConfig,
			final Properties opaConfig, final Map<String, IPermission> permissionsMap) throws BadAppConfigException {
		final Map<String, IRole> rolesByAclName = new HashMap<String, IRole>(4);

		// Special Roles
		final Set<String> specialRolesNames = new HashSet<String>(SpecialRole.values().length);
		final Map<SpecialRole, IRole> specialRoles = new HashMap<SpecialRole, IRole>(SpecialRole.values().length);
		for (final SpecialRole srEnum : SpecialRole.values()) {
			specialRolesNames.add(srEnum.name());
		}

		// Declared Roles
		final Set<String> aclRolesNames = new HashSet<String>();
		aclRolesNames.addAll(specialRolesNames);
		final String aclRolesVal = this.getOptionalValue(opaConfig, OpaConfigKeys.ACL_ROLES.getKey());
		final String[] aclRolesArray = StringUtils.delimitedListToStringArray(aclRolesVal,
				IAppConfigFactory.OPA_CONFIG_LIST_SPLITER);
		if (aclRolesArray != null) {
			for (final String aclRoleName : aclRolesArray) {
				if (StringUtils.hasText(aclRoleName)) {
					aclRolesNames.add(aclRoleName);
				}
			}
		}

		// Search permissions assignment
		final Map<String, Set<IPermission>> permissionsAssignment = new HashMap<String, Set<IPermission>>(4);
		for (final String aclRoleName : aclRolesNames) {
			// For each role search perm assignment
			final Set<IPermission> rolePermissionSet;
			final String permKey = OpaConfigKeys.ACL_ROLES.getKey().concat(".").concat(aclRoleName)
					.concat(OpaConfigKeys.ACL_PERMISSIONS_ASSIGNMENT_SUFFIX.getKey());
			final String rolePermissionsVal = this.getOptionalValue(opaConfig, permKey);
			final String[] rolePermissionsArray = StringUtils.delimitedListToStringArray(rolePermissionsVal,
					IAppConfigFactory.OPA_CONFIG_LIST_SPLITER);
			if (rolePermissionsArray != null) {
				// We found a perm assignment
				rolePermissionSet = new HashSet<IPermission>();
				for (final String rolePermissionName : rolePermissionsArray) {
					if (StringUtils.hasText(rolePermissionName)) {
						final IPermission perm = permissionsMap.get(rolePermissionName);
						if (perm != null) {
							rolePermissionSet.add(perm);
						} else {
							final String message = String.format(
									"Try to assign undeclared permission: [%1$s] to role: [%2$s] in OPA: [%3$s] !",
									rolePermissionName, aclRoleName, bundleApp.getSymbolicName());
							throw new BadAppConfigException(message);
						}
					}
				}
			} else {
				rolePermissionSet = Collections.emptySet();
			}

			permissionsAssignment.put(aclRoleName, rolePermissionSet);
		}

		// Search sub-roles assignment
		final Map<String, Set<String>> subRolesAssignment = new HashMap<String, Set<String>>(4);
		for (final String aclRoleName : aclRolesNames) {
			final Set<String> subRolesSet;
			final String key = OpaConfigKeys.ACL_ROLES.getKey().concat(".").concat(aclRoleName)
					.concat(OpaConfigKeys.ACL_SUBROLES_ASSIGNMENT_SUFFIX.getKey());
			final String subRolesVal = this.getOptionalValue(opaConfig, key);
			final String[] subRolesArray = StringUtils.delimitedListToStringArray(subRolesVal,
					IAppConfigFactory.OPA_CONFIG_LIST_SPLITER);
			if (subRolesArray != null) {
				subRolesSet = new HashSet<String>();
				for (final String subRoleName : subRolesArray) {
					if (StringUtils.hasText(subRoleName) && aclRolesNames.contains(subRoleName)) {
						subRolesSet.add(subRoleName);
					} else {
						final String message = String.format(
								"Try to assign undeclared sub-role: [%1$s] to role: [%2$s] in OPA: [%3$s] !",
								subRoleName, aclRoleName, bundleApp.getSymbolicName());
						throw new BadAppConfigException(message);
					}
				}
			} else {
				subRolesSet = Collections.emptySet();
			}

			subRolesAssignment.put(aclRoleName, subRolesSet);
		}

		// Build roles : begin with the sub-roles
		final Set<String> rolesToBuild = new HashSet<String>(aclRolesNames);
		final Set<String> builtRoles = new HashSet<String>(rolesToBuild.size());
		while (!rolesToBuild.isEmpty()) {
			// Loop while some roles are not built yet
			final Iterator<String> rolesToBuildIt = rolesToBuild.iterator();
			while (rolesToBuildIt.hasNext()) {
				// Loop on all roles not built yet to find one candidate to build
				final String aclRoleName = rolesToBuildIt.next();
				final Set<String> subRolesNames = subRolesAssignment.get(aclRoleName);
				if (builtRoles.containsAll(subRolesNames)) {
					// If all sub-roles are already built we can build the role
					final String finalRoleName = this.buildFinalRoleName(bundleApp, aclRoleName);
					final Set<IPermission> permissions = permissionsAssignment.get(aclRoleName);
					final Set<IRole> subRoles = new HashSet<IRole>(subRolesNames.size());
					for (final String subRoleName : subRolesNames) {
						subRoles.add(rolesByAclName.get(subRoleName));
					}

					final IRole newRole = this.roleFactory.initializeRole(finalRoleName, permissions, subRoles);
					rolesByAclName.put(aclRoleName, newRole);
					builtRoles.add(aclRoleName);
					rolesToBuildIt.remove();

					if (specialRolesNames.contains(aclRoleName)) {
						// Current role is special
						specialRoles.put(Enum.valueOf(SpecialRole.class, aclRoleName), newRole);
					}
				}
			}
		}

		appConfig.setSpecialRoles(Collections.unmodifiableMap(specialRoles));
		final Set<IRole> rolesSet = new HashSet<IRole>(rolesByAclName.values());
		appConfig.setDeclaredRoles(Collections.unmodifiableSet(rolesSet));

		return rolesByAclName;
	}

	/**
	 * Process default OPA permissions from config.
	 * 
	 * @param appConfig
	 * @param opaConfig
	 * @throws AppConfigNotFoundException
	 */
	protected void processPreferences(final BasicAppConfig appConfig, final Properties opaConfig) {
		final String preferencesKeysVal = this.getOptionalValue(opaConfig, OpaConfigKeys.PREFERENCES.getKey());
		final String[] preferencesKeysArray = StringUtils.delimitedListToStringArray(preferencesKeysVal,
				IAppConfigFactory.OPA_CONFIG_LIST_SPLITER);
		if (preferencesKeysArray != null) {
			final Map<String, String[]> preferencesMap = new HashMap<String, String[]>(preferencesKeysArray.length);
			for (final String prefKey : preferencesKeysArray) {
				// Search prefValue
				final String preferenceVal = this.getOptionalValue(opaConfig, prefKey);
				final String[] preferenceValArray = StringUtils.delimitedListToStringArray(preferenceVal,
						IAppConfigFactory.OPA_CONFIG_LIST_SPLITER);
				preferencesMap.put(prefKey, preferenceValArray);
			}

			this.appPreferencesManager.init(appConfig, preferencesMap);
		}
	}

	protected Properties loadOpaConfig(final Bundle opaBundle) throws AppConfigNotFoundException {
		final URL opaPropertiesUrl = opaBundle.getResource(IAppConfigFactory.OPA_CONFIG_PROPERTIES_FILE_PATH);
		if (opaPropertiesUrl == null) {
			final String message = String.format("No OPA configuration file (%1$s) found in OPA [%2$s] !",
					IAppConfigFactory.OPA_CONFIG_PROPERTIES_FILE_PATH, opaBundle.getSymbolicName());
			throw new AppConfigNotFoundException(message);
		}

		final Properties props = new Properties();
		try {
			props.load(opaPropertiesUrl.openStream());
		} catch (final IOException e) {
			final String message = String.format("Unable to open OPA configuration file (%1$s) found in OPA [%2$s] !",
					IAppConfigFactory.OPA_CONFIG_PROPERTIES_FILE_PATH, opaBundle.getSymbolicName());
			throw new AppConfigNotFoundException(message, e);
		}

		return props;
	}

	protected String getMandatoryValue(final String opaSymbolicName, final Properties opaProps,
			final OpaConfigKeys opaConfigKey) throws AppConfigNotFoundException {
		final String propertyKey = opaConfigKey.getKey();
		final String propertyValue = opaProps.getProperty(propertyKey);

		if (!StringUtils.hasText(propertyValue)) {
			final String message = String.format("Missing property '%1$s' detected in OPA [%2$s] configuration file !",
					propertyKey, opaSymbolicName);
			throw new AppConfigNotFoundException(message);
		}

		return propertyValue;

	}

	protected String getOptionalValue(final Properties opaProps, final String propertyKey) {
		final String propertyValue = opaProps.getProperty(propertyKey);

		return propertyValue;

	}

	/**
	 * @param webContextPathHeader
	 * @return
	 */
	protected String buildWebAppBundlePath(final Bundle bundle) {
		final String webContextPathHeader = (String) bundle.getHeaders().get(
				IAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER);
		final String webBundlePath = "/".concat(webContextPathHeader.replaceAll("[\\/\\\\]+", ""));

		return webBundlePath;
	}

}
