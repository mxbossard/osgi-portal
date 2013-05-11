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

package fr.mby.portal.coreimpl.message;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.Cookie;

import fr.mby.portal.message.IReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public abstract class AbstractReply implements IReply {

	private final Map<String, String> properties;

	private final Collection<Cookie> cookies;

	/** Protected constructor. Use the factory. */
	protected AbstractReply() {
		super();

		this.properties = new HashMap<String, String>();
		this.cookies = new HashSet<Cookie>();
	}

	@Override
	public void setProperty(final String key, final String value) throws IllegalArgumentException {
		this.properties.put(key, value);
	}

	@Override
	public String encodeURL(final String path) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addProperty(final Cookie cookie) throws IllegalArgumentException {
		this.cookies.add(cookie);
	}

}
