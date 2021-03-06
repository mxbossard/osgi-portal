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

package fr.mby.portal.coreimpl.action;

import java.util.Collections;
import java.util.Map;

import org.springframework.util.Assert;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.context.IPortalContext;
import fr.mby.portal.api.user.IUserDetails;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicUserAction implements IUserAction {

	private final IPortalContext portalContext;

	private final IUserDetails userDetails;

	private final String portalSessionId;

	private final Map<String, Iterable<String>> properties;

	private final Map<String, String[]> parameters;

	private final Map<String, Object> attributes;

	/** Protected constructor. Use the factory. */
	protected BasicUserAction(final IPortalContext portalContext, final IUserDetails userDetails,
			final String portalSessionId, final Map<String, Iterable<String>> properties,
			final Map<String, String[]> parameters, final Map<String, Object> attributes) {
		Assert.notNull(portalContext, "No PortalContext provided !");
		Assert.notNull(userDetails, "No User Principal provided !");
		Assert.hasText(portalSessionId, "No Portal Session Id provided !");
		Assert.notNull(properties, "No properties provided !");
		Assert.notNull(parameters, "No parameters provided !");
		Assert.notNull(attributes, "No attributes provided !");

		this.portalContext = portalContext;
		this.userDetails = userDetails;
		this.portalSessionId = portalSessionId;
		this.properties = properties;
		this.parameters = parameters;
		this.attributes = attributes;
	}

	@Override
	public IPortalContext getPortalContext() {
		return this.portalContext;
	}

	@Override
	public IUserDetails getUserDetails() {
		return this.userDetails;
	}

	@Override
	public String getPortalSessionId() {
		return this.portalSessionId;
	}

	@Override
	public String getProperty(final String name) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		return this.getProperties(name).iterator().next();
	}

	@Override
	public Iterable<String> getProperties(final String name) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		return this.properties.get(name);
	}

	@Override
	public Iterable<String> getPropertyNames() {
		return this.properties.keySet();
	}

	@Override
	public String getParameter(final String name) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		String value = null;
		final String[] values = this.getParameterValues(name);
		if (values != null && values.length > 0) {
			value = values[0];
		}

		return value;
	}

	@Override
	public String[] getParameterValues(final String name) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		final String[] values = this.parameters.get(name);
		return values;
	}

	@Override
	public Iterable<String> getParameterNames() {
		return this.parameters.keySet();
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return Collections.unmodifiableMap(this.parameters);
	}

	@Override
	public Object getAttribute(final String name) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		return this.attributes.get(name);
	}

	@Override
	public Iterable<String> getAttributeNames() {
		return this.attributes.keySet();
	}

	@Override
	public void setAttribute(final String name, final Object value) throws IllegalArgumentException {
		this.attributes.put(name, value);
	}

	@Override
	public Object removeAttribute(final String name) throws IllegalArgumentException {
		Assert.hasText(name, "No property name provided !");

		return this.removeAttribute(name);
	}

}
