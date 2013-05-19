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

package fr.mby.portal.coreimpl.session;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import fr.mby.portal.api.app.ISession;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicSession implements ISession {

	private final Map<String, Object> session;

	/**
	 * 
	 */
	protected BasicSession() {
		super();

		this.session = new HashMap<String, Object>();
	}

	@Override
	public Object getAttribute(String name) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		return this.session.get(name);
	}

	@Override
	public void setAttribute(String name, Object value) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		this.session.put(name, value);
	}

	@Override
	public Object removeAttribute(String name) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		return this.session.remove(name);
	}

}
