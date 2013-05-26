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

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import fr.mby.portal.core.configuration.IConfigurationManager;

/**
 * Implementation based on Files to store the configuration elements.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class PropertiesConfigurationManager implements IConfigurationManager, InitializingBean {

	private final Map<String, Set<String>> keysByTag = Collections.synchronizedMap(new TreeMap<String, Set<String>>());

	private final Map<String, Class<?>> valueTypeByKey = Collections.synchronizedMap(new TreeMap<String, Class<?>>());

	private final Map<String, PropertiesConfiguration> configurationByKey = Collections
			.synchronizedMap(new TreeMap<String, PropertiesConfiguration>());

	private String configFileDirectory;

	@Override
	public PropertiesConfiguration initConfiguration(final String key, final Class<?> valueType, final String tag)
			throws IllegalArgumentException, ConfigurationException {
		Assert.hasText(key, "No key provided !");
		if (this.configurationByKey.containsKey(key)) {
			throw new IllegalArgumentException("Configuration element already initialized for key: " + key + " !");
		}
		Assert.notNull(valueType, "No value type provided !");
		Assert.hasText(tag, "No tag provided !");

		final String propertiesFileName = FilenameUtils.concat(this.configFileDirectory, key + ".properties");
		final File propertiesFile = new File(propertiesFileName);
		final PropertiesConfiguration config = new PropertiesConfiguration(propertiesFile);
		this.configurationByKey.put(key, config);
		this.valueTypeByKey.put(key, valueType);

		this.tagConfigurationKey(key, tag);

		return config;
	}

	@Override
	public PropertiesConfiguration getConfiguration(final String key) throws IllegalArgumentException {
		Assert.hasText(key, "No key provided !");

		final PropertiesConfiguration config = this.configurationByKey.get(key);
		if (config == null) {
			throw new IllegalArgumentException("No configuration element found for key: " + key + " !");
		}

		return config;
	}

	@Override
	public Class<?> getConfigurationValueType(final String key) throws IllegalArgumentException {
		Assert.hasText(key, "No key provided !");

		final Class<?> valueType = this.valueTypeByKey.get(key);
		if (valueType == null) {
			throw new IllegalArgumentException("No configuration element found for key: " + key + " !");
		}

		return valueType;
	}

	@Override
	public Collection<String> getConfigurationTags() {
		return this.keysByTag.keySet();
	}

	@Override
	public Collection<String> getConfigurationKeys(final String tag) throws IllegalArgumentException {
		Set<String> keys = this.keysByTag.get(tag);

		if (keys == null) {
			keys = Collections.emptySet();
		}

		return keys;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasText(this.configFileDirectory)) {
			// Init directory
			this.configFileDirectory = SystemUtils.getJavaIoTmpDir().getPath();
		}

		// Test if the direcotry exists
		final File dir = new File(this.configFileDirectory);
		if (!dir.isDirectory()) {
			this.configFileDirectory = SystemUtils.getJavaIoTmpDir().getPath();
		}
	}

	@Override
	public void flushConfiguration(final String key) throws IllegalArgumentException, ConfigurationException {
		Assert.hasText(key, "No key provided !");

		final PropertiesConfiguration config = this.configurationByKey.get(key);
		if (config == null) {
			throw new IllegalArgumentException("No configuration element found for key: " + key + " !");
		}

		config.save();
	}

	/**
	 * Tag the configuration key.
	 * 
	 * @param key
	 * @param tag
	 */
	protected void tagConfigurationKey(final String key, final String tag) {
		Set<String> keys = this.keysByTag.get(tag);
		if (keys == null) {
			keys = new TreeSet<String>();
			keys = Collections.synchronizedSet(keys);
			this.keysByTag.put(key, keys);
		}
		keys.add(key);
	}

}
