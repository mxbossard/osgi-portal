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

package fr.mby.portal.core.configuration;

import java.util.Collection;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Configuration manager in charge to centralize all basic configuration.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public interface IConfigurationManager {

	static final String CORE_TAG = "core";

	/**
	 * Initialize a config element.
	 * 
	 * @param key
	 *            the key of the configuration element
	 * @param valueType
	 *            the required type of configuration values
	 * @param tag
	 *            a tag for the configuration
	 * @return
	 * @throws IllegalArgumentException
	 *             if config element already initialized or one of the argument is null
	 * @throws ConfigurationException
	 *             if problem with apache configuration
	 */
	Configuration initConfiguration(String key, Class<?> valueType, String tag) throws IllegalArgumentException,
			ConfigurationException;

	/**
	 * Retrieve a configuration element.
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 *             if key does not correspond to a config element
	 */
	Configuration getConfiguration(String key) throws IllegalArgumentException;

	/**
	 * Retrieve the value type of a configuration element
	 * 
	 * @param key
	 * @return
	 * @throws IllegalArgumentException
	 *             if key does not correspond to a config element
	 */
	Class<?> getConfigurationValueType(String key) throws IllegalArgumentException;

	/**
	 * Get all configuration tags registered.
	 * 
	 * @return
	 */
	Collection<String> getConfigurationTags();

	/**
	 * Get all configuration keys registered for a tag.
	 * 
	 * @param tag
	 * @return
	 * @throws IllegalArgumentException
	 *             if tag does not correspond to a registered tag
	 */
	Collection<String> getConfigurationKeys(String tag) throws IllegalArgumentException;

	/**
	 * Flush the configuration.
	 * 
	 * @param key
	 * @throws IllegalArgumentException
	 *             if key does not correspond to a config element
	 * @throws ConfigurationException
	 *             if problem with apache configuration
	 */
	void flushConfiguration(String key) throws IllegalArgumentException, ConfigurationException;
}
