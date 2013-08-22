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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.osgi.mock.MockBundle;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.mby.portal.api.acl.IPermission;
import fr.mby.portal.api.acl.IRole;
import fr.mby.portal.api.app.IAppConfig.SpecialPermission;
import fr.mby.portal.api.app.IAppConfig.SpecialRole;
import fr.mby.portal.core.acl.IPermissionFactory;
import fr.mby.portal.core.app.IAppConfigFactory;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:basicAppConfigFactoryTestContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicAppConfigFactoryTest {

	private static final String BUNDLE_SN = "BUNDLE-TEST-1.2.3";

	@Autowired
	private BasicAppConfigFactory basicAppConfigFactory;

	@Autowired
	private IPermissionFactory permissionFactory;

	private Properties emptyConfig;

	private Properties missingConfig;

	private Properties koConfig;

	private Properties okConfig;

	private final Map<String, IPermission> permissionsMap = new HashMap<String, IPermission>();

	public BasicAppConfigFactoryTest() {
		super();

		try {
			this.emptyConfig = PropertiesLoaderUtils.loadAllProperties("appConfig/opa-empty.properties");
			this.missingConfig = PropertiesLoaderUtils.loadAllProperties("appConfig/opa-missing.properties");
			this.koConfig = PropertiesLoaderUtils.loadAllProperties("appConfig/opa-ko.properties");
			this.okConfig = PropertiesLoaderUtils.loadAllProperties("appConfig/opa-ok.properties");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void init() {
		this.permissionsMap.put("CAN_EDIT", this.permissionFactory.build("CAN_EDIT"));
		this.permissionsMap.put("CAN_RENDER", this.permissionFactory.build("CAN_RENDER"));
		this.permissionsMap.put("lambdaPerm", this.permissionFactory.build("lambdaPerm"));
	}

	@Test
	public void testProcessAclPermissionsOnEmptyConf() throws Exception {
		final BasicAppConfig appConfig = this.processAclPermissions(this.emptyConfig);

		this.assertSpecialPermissionsExistence(appConfig);

		final Set<IPermission> declaredPerms = appConfig.getDeclaredPermissions();

		Assert.assertNotNull("Declared permissions should not be null !", declaredPerms);
		Assert.assertEquals("Declared permissions should be 2 (specials) !", 2, declaredPerms.size());
	}

	@Test
	public void testProcessAclPermissionsOnKoConf() throws Exception {
		final BasicAppConfig appConfig = this.processAclPermissions(this.koConfig);

		this.assertSpecialPermissionsExistence(appConfig);

		final Set<IPermission> declaredPerms = appConfig.getDeclaredPermissions();

		Assert.assertNotNull("Declared permissions should not be null !", declaredPerms);
		Assert.assertEquals("Declared permissions should be 2 (specials) !", 2, declaredPerms.size());
	}

	@Test
	public void testProcessAclPermissionsOnMissingConf() throws Exception {
		final BasicAppConfig appConfig = this.processAclPermissions(this.missingConfig);

		this.assertSpecialPermissionsExistence(appConfig);

		final Set<IPermission> declaredPerms = appConfig.getDeclaredPermissions();

		Assert.assertNotNull("Declared permissions should not be null !", declaredPerms);
		Assert.assertEquals("Declared permissions should be 2 (specials) !", 2, declaredPerms.size());
	}

	@Test
	public void testProcessAclPermissionsOnOkConf() throws Exception {
		final BasicAppConfig appConfig = this.processAclPermissions(this.okConfig);

		this.assertSpecialPermissionsExistence(appConfig);

		final Set<IPermission> declaredPerms = appConfig.getDeclaredPermissions();

		Assert.assertNotNull("Declared permissions should not be null !", declaredPerms);
		Assert.assertEquals("Declared permissions should be 3 !", 3, declaredPerms.size());
	}

	@Test
	public void testProcessAclRolesOnEmptyConf() throws Exception {
		final BasicAppConfig appConfig = this.processAclRoles(this.emptyConfig);

		this.assertSpecialRolesExistence(appConfig);

		final Set<IRole> declaredRoles = appConfig.getDeclaredRoles();

		Assert.assertNotNull("Declared permissions should not be null !", declaredRoles);
		Assert.assertEquals("Declared permissions should be 3 (specials) !", 3, declaredRoles.size());

		// Empty conf => no sub-role nor permission assignment
		final IRole logged = appConfig.getSpecialRole(SpecialRole.LOGGED);
		final IRole admin = appConfig.getSpecialRole(SpecialRole.ADMIN);

		final Set<IPermission> loggedPerms = logged.getPermissions();
		final Set<IPermission> adminPerms = admin.getPermissions();

		Assert.assertTrue("Special logged role should be empty !", loggedPerms.isEmpty());
		Assert.assertTrue("Special admin role should be empty !", adminPerms.isEmpty());
	}

	@Test(expected = BadAppConfigException.class)
	public void testProcessAclRolesOnKoConf() throws Exception {
		this.processAclRoles(this.koConfig);
	}

	@Test
	public void testProcessAclRolesOnMissingConf() throws Exception {
		final BasicAppConfig appConfig = this.processAclRoles(this.missingConfig);

		this.assertSpecialRolesExistence(appConfig);

		final Set<IRole> declaredRoles = appConfig.getDeclaredRoles();

		Assert.assertNotNull("Declared permissions should not be null !", declaredRoles);
		Assert.assertEquals("Declared permissions should be 3 (specials) !", 3, declaredRoles.size());

		// Missing conf => No admin sub-role assignment nor lambda role declaration
		final IRole logged = appConfig.getSpecialRole(SpecialRole.LOGGED);
		final IRole admin = appConfig.getSpecialRole(SpecialRole.ADMIN);

		final Set<IPermission> loggedPerms = logged.getPermissions();
		final Set<IPermission> adminPerms = admin.getPermissions();

		Assert.assertEquals("Special logged role should have 1 perm : CAN_RENDER !", 1, loggedPerms.size());
		Assert.assertEquals("Special logged role should have 1 perm : CAN_RENDER !",
				this.permissionsMap.get("CAN_RENDER"), loggedPerms.iterator().next());
		Assert.assertTrue("Special admin role should be empty !", adminPerms.isEmpty());
	}

	@Test
	public void testProcessAclRolesOnOkConf() throws Exception {
		final BasicAppConfig appConfig = this.processAclRoles(this.okConfig);

		this.assertSpecialRolesExistence(appConfig);

		final Set<IRole> declaredRoles = appConfig.getDeclaredRoles();

		Assert.assertNotNull("Declared permissions should not be null !", declaredRoles);
		Assert.assertEquals("Declared permissions should be 4 !", 4, declaredRoles.size());

		// Conf ok
		final IRole guest = appConfig.getSpecialRole(SpecialRole.GUEST);
		final IRole logged = appConfig.getSpecialRole(SpecialRole.LOGGED);
		final IRole admin = appConfig.getSpecialRole(SpecialRole.ADMIN);

		final Set<IPermission> loggedPerms = logged.getPermissions();
		final Set<IPermission> adminPerms = admin.getPermissions();

		// logged sub-role of admin
		Assert.assertEquals("Special logged role should be sub-role of admin !", logged, admin.getSubRoles().iterator()
				.next());

		// logged has can_render
		Assert.assertEquals("Special logged role should have 1 perm : CAN_RENDER !", 1, loggedPerms.size());
		Assert.assertEquals("Special logged role should have 1 perm : CAN_RENDER !",
				this.permissionsMap.get("CAN_RENDER"), loggedPerms.iterator().next());
		Assert.assertTrue("Special logged role should have 1 perm : CAN_RENDER !",
				logged.isGranted(this.permissionsMap.get("CAN_RENDER")));

		// admin has can_edit and is granted of can_render by sub-roling
		Assert.assertEquals("Special admin role should have 1 perm : CAN_EDIT !", 1, adminPerms.size());
		Assert.assertEquals("Special admin role should have 1 perm : CAN_EDIT !", this.permissionsMap.get("CAN_EDIT"),
				adminPerms.iterator().next());
		Assert.assertTrue("Special admin role should be granted 2 perms : CAN_RENDER & CAN_EDIT !",
				admin.isGranted(this.permissionsMap.get("CAN_RENDER")));
		Assert.assertTrue("Special admin role should be granted 2 perms : CAN_RENDER & CAN_EDIT !",
				admin.isGranted(this.permissionsMap.get("CAN_EDIT")));

		// lambdaRole have lambaPerm and logged has sub-role
		final Set<IRole> roles = new HashSet<IRole>(declaredRoles);
		roles.remove(guest);
		roles.remove(logged);
		roles.remove(admin);

		final IRole lambdaRole = roles.iterator().next();
		Assert.assertEquals("Lanmda role should have 1 perm : lambdaPerm !", 1, lambdaRole.getPermissions().size());
		Assert.assertEquals("Lanmda role should have 1 perm : lambdaPerm !", this.permissionsMap.get("lambdaPerm"),
				lambdaRole.getPermissions().iterator().next());
		Assert.assertTrue("Lambda role should be granted 2 perms : CAN_RENDER & lambdaPerm !",
				lambdaRole.isGranted(this.permissionsMap.get("CAN_RENDER")));
		Assert.assertTrue("Lambda role should be granted 2 perms : CAN_RENDER & lambdaPerm !",
				lambdaRole.isGranted(this.permissionsMap.get("lambdaPerm")));
	}

	protected BasicAppConfig processAclPermissions(final Properties opaConfig) throws Exception {
		final MockBundle bundleApp = new MockBundle(BasicAppConfigFactoryTest.BUNDLE_SN);
		final BasicAppConfig appConfig = new BasicAppConfig();

		this.basicAppConfigFactory.processAclPermissions(bundleApp, appConfig, opaConfig);

		return appConfig;
	}

	protected void assertSpecialPermissionsExistence(final BasicAppConfig appConfig) {
		final IPermission canEditPerm = appConfig.getSpecialPermission(SpecialPermission.CAN_EDIT);
		final IPermission canRenderPerm = appConfig.getSpecialPermission(SpecialPermission.CAN_RENDER);

		Assert.assertNotNull("Special can-edit permission should not be null !", canEditPerm);
		Assert.assertNotNull("Declared can-render permission should not be null !", canRenderPerm);
	}

	protected BasicAppConfig processAclRoles(final Properties opaConfig) throws Exception {
		final MockBundle bundleApp = new MockBundle(BasicAppConfigFactoryTest.BUNDLE_SN);
		final BasicAppConfig appConfig = new BasicAppConfig();

		this.basicAppConfigFactory.processAclRoles(bundleApp, appConfig, opaConfig, this.permissionsMap);

		return appConfig;
	}

	protected void assertSpecialRolesExistence(final BasicAppConfig appConfig) {
		final IRole guest = appConfig.getSpecialRole(SpecialRole.GUEST);
		final IRole logged = appConfig.getSpecialRole(SpecialRole.LOGGED);
		final IRole admin = appConfig.getSpecialRole(SpecialRole.ADMIN);

		Assert.assertNotNull("Special guest role should not be null !", guest);
		Assert.assertNotNull("Special logged role should not be null !", logged);
		Assert.assertNotNull("Special admin role should not be null !", admin);

		final Set<IPermission> guestPerms = guest.getPermissions();
		Assert.assertTrue("Special guest role should be empty !", guestPerms.isEmpty());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBuildWebAppBundlePath1() throws Exception {
		final MockBundle bundle = new MockBundle();
		bundle.getHeaders().put(IAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER, "test");

		final String path = this.basicAppConfigFactory.buildWebAppBundlePath(bundle);
		Assert.assertEquals("Bad url path !", "/test", path);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBuildWebAppBundlePath2() throws Exception {
		final MockBundle bundle = new MockBundle();
		bundle.getHeaders().put(IAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER, "\\test");

		final String path = this.basicAppConfigFactory.buildWebAppBundlePath(bundle);
		Assert.assertEquals("Bad url path !", "/test", path);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBuildWebAppBundlePath3() throws Exception {
		final MockBundle bundle = new MockBundle();
		bundle.getHeaders().put(IAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER, "/test");

		final String path = this.basicAppConfigFactory.buildWebAppBundlePath(bundle);
		Assert.assertEquals("Bad url path !", "/test", path);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBuildWebAppBundlePath4() throws Exception {
		final MockBundle bundle = new MockBundle();
		bundle.getHeaders().put(IAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER, "/test/");

		final String path = this.basicAppConfigFactory.buildWebAppBundlePath(bundle);
		Assert.assertEquals("Bad url path !", "/test", path);
	}

}
