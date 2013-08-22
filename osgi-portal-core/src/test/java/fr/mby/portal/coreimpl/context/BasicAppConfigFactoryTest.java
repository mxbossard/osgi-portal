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
import java.util.Properties;
import java.util.Set;

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
import fr.mby.portal.api.app.IAppConfig.SpecialPermission;
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

	private Properties emptyConfig;

	private Properties missingConfig;

	private Properties koConfig;

	private Properties okConfig;

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
