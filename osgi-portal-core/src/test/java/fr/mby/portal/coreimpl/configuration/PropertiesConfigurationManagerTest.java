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

package fr.mby.portal.coreimpl.configuration;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:fileConfigurationManagerTestContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PropertiesConfigurationManagerTest {

	private static final String KEY1 = "KEY1";
	private static final Class<?> TYPE1 = Long.class;
	private static final String TAG1 = "TAG1";

	private static final String KEY2 = PropertiesConfigurationManagerTest.class.getName();
	private static final Class<?> TYPE2 = String.class;
	private static final String TAG2 = "TAG2";

	@Autowired
	private PropertiesConfigurationManager configManager;

	/**
	 * test 1.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception {
		final Configuration config1 = this.configManager.initConfiguration(PropertiesConfigurationManagerTest.KEY1,
				PropertiesConfigurationManagerTest.TYPE1, PropertiesConfigurationManagerTest.TAG1);
		Assert.assertNotNull("Config not initialized !", config1);
		config1.addProperty("key1", 42L);

		final Configuration config2 = this.configManager.getConfiguration(PropertiesConfigurationManagerTest.KEY1);
		Assert.assertNotNull("Config cannot be retrieved !", config2);
		Assert.assertEquals("Bad property value in config !", 42L, config2.getLong("key1"));

		Assert.assertNotNull("Tags cannot be retrieved !", this.configManager.getConfigurationTags());
		Assert.assertEquals("Tags count bad !", 1, this.configManager.getConfigurationTags().size());
		final String configKey = this.configManager.getConfigurationTags().iterator().next();
		Assert.assertEquals("Tag name bad !", PropertiesConfigurationManagerTest.KEY1, configKey);

		Assert.assertNotNull("Empty collection not returned !", this.configManager.getConfigurationKeys("unknownTag"));
	}

	/**
	 * test 2.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		final Configuration config1 = this.configManager.initConfiguration(PropertiesConfigurationManagerTest.KEY2,
				PropertiesConfigurationManagerTest.TYPE2, PropertiesConfigurationManagerTest.TAG2);
		Assert.assertNotNull("Config not initialized !", config1);
		config1.addProperty("key2", "Value2");

		final Configuration config2 = this.configManager.getConfiguration(PropertiesConfigurationManagerTest.KEY2);
		Assert.assertNotNull("Config cannot be retrieved !", config2);
		Assert.assertEquals("Bad property value in config !", "Value2", config2.getString("key2"));

		Assert.assertNotNull("Tags cannot be retrieved !", this.configManager.getConfigurationTags());
		Assert.assertEquals("Tags count bad !", 1, this.configManager.getConfigurationTags().size());
		final String configKey = this.configManager.getConfigurationTags().iterator().next();
		Assert.assertEquals("Tag name bad !", PropertiesConfigurationManagerTest.KEY2, configKey);

		Assert.assertNotNull("Empty collection not returned !", this.configManager.getConfigurationKeys("unknownTag"));
	}

	/**
	 * test 3.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFlush() throws Exception {
		final Configuration config1 = this.configManager.initConfiguration(PropertiesConfigurationManagerTest.KEY2,
				PropertiesConfigurationManagerTest.TYPE2, PropertiesConfigurationManagerTest.TAG2);
		Assert.assertNotNull("Config not initialized !", config1);
		config1.addProperty("key2", "Value2");

		// Flush
		this.configManager.flushConfiguration(PropertiesConfigurationManagerTest.KEY2);

		final Configuration config2 = this.configManager.getConfiguration(PropertiesConfigurationManagerTest.KEY2);
		Assert.assertNotNull("Config cannot be retrieved !", config2);
		Assert.assertEquals("Bad property value in config !", "Value2", config2.getString("key2"));

		Assert.assertNotNull("Tags cannot be retrieved !", this.configManager.getConfigurationTags());
		Assert.assertEquals("Tags count bad !", 1, this.configManager.getConfigurationTags().size());
		final String configKey = this.configManager.getConfigurationTags().iterator().next();
		Assert.assertEquals("Tag name bad !", PropertiesConfigurationManagerTest.KEY2, configKey);

		Assert.assertNotNull("Empty collection not returned !", this.configManager.getConfigurationKeys("unknownTag"));
	}
}
