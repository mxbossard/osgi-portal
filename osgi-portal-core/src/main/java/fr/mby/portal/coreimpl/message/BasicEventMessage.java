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

import org.springframework.util.Assert;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.event.IEvent;
import fr.mby.portal.api.message.IEventMessage;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicEventMessage extends AbstractMessage implements IEventMessage {

	private final IEvent event;

	/**
	 * @param userAction
	 * @param event
	 */
	protected BasicEventMessage(final IUserAction userAction, final IEvent event) {
		super(userAction);

		Assert.notNull(event, "No IEvent provided !");

		this.event = event;
	}

	@Override
	public IEvent getEvent() {
		return this.event;
	}

}
