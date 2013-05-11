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

import org.springframework.util.Assert;

import fr.mby.portal.event.IEvent;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicEvent implements IEvent {

	private final String name;

	/** Protected constructor. Use the factory. */
	protected BasicEvent(final String name) {
		super();

		Assert.notNull(name, "No name provided !");

		this.name = name;
	}

	/**
	 * Getter of name.
	 * 
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

}
