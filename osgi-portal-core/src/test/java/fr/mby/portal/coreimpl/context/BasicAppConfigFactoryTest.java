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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.osgi.mock.MockBundle;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:basicAppConfigFactoryTestContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BasicAppConfigFactoryTest {

	@Autowired
	private BasicAppConfigFactory basicAppConfigFactory;

	@Test
	@SuppressWarnings("unchecked")
	public void testBuildWebAppBundlePath1() throws Exception {
		final MockBundle bundle = new MockBundle();
		bundle.getHeaders().put(BasicAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER, "test");

		final String path = this.basicAppConfigFactory.buildWebAppBundlePath(bundle);
		Assert.assertEquals("Bad url path !", "/test", path);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBuildWebAppBundlePath2() throws Exception {
		final MockBundle bundle = new MockBundle();
		bundle.getHeaders().put(BasicAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER, "\\test");

		final String path = this.basicAppConfigFactory.buildWebAppBundlePath(bundle);
		Assert.assertEquals("Bad url path !", "/test", path);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBuildWebAppBundlePath3() throws Exception {
		final MockBundle bundle = new MockBundle();
		bundle.getHeaders().put(BasicAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER, "/test");

		final String path = this.basicAppConfigFactory.buildWebAppBundlePath(bundle);
		Assert.assertEquals("Bad url path !", "/test", path);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testBuildWebAppBundlePath4() throws Exception {
		final MockBundle bundle = new MockBundle();
		bundle.getHeaders().put(BasicAppConfigFactory.WEB_CONTEXT_PATH_BUNDLE_HEADER, "/test/");

		final String path = this.basicAppConfigFactory.buildWebAppBundlePath(bundle);
		Assert.assertEquals("Bad url path !", "/test", path);
	}

}
