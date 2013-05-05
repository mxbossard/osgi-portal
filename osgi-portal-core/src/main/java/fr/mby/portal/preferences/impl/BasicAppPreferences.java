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

package fr.mby.portal.preferences.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.mby.portal.app.IAppPreferences;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicAppPreferences implements IAppPreferences {

	private final Map<String, String[]> preferences;

	/**
	 * 
	 */
	protected BasicAppPreferences() {
		super();
		this.preferences = new HashMap<String, String[]>();
	}

	@Override
	public Map<String, String[]> getMap() {
		return Collections.unmodifiableMap(this.preferences);
	}

	@Override
	public Iterable<String> getNames() {
		return this.preferences.keySet();
	}

	@Override
	public String getValue(String name, String def) {
		String[] values = this.preferences.get(name);
		final String value;
		if (values == null) {
			value = def;
		} else {
			value = values[0];
		}
		return value;
	}

	@Override
	public String[] getValues(String name, String[] def) {
		String[] values = this.preferences.get(name);
		if (values == null) {
			values = def;
		}
		return values;
	}

}
