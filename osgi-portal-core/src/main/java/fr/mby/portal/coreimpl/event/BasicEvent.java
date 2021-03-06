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

package fr.mby.portal.coreimpl.event;

import java.io.Serializable;

import org.springframework.util.Assert;

import fr.mby.portal.api.event.IEvent;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicEvent implements IEvent {

	private final String name;

	private final Serializable value;

	/** Protected constructor. Use the factory. */
	protected BasicEvent(final String name, final Serializable value) {
		super();

		Assert.notNull(name, "No name provided !");
		Assert.notNull(value, "No value provided !");

		this.name = name;
		this.value = value;
	}

	/**
	 * Getter of name.
	 * 
	 * @return the name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Serializable getValue() {
		return this.value;
	}

}
