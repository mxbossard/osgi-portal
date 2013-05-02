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
package fr.mby.portal.message.impl;

import fr.mby.portal.message.IMessage.MessageType;
import fr.mby.portal.message.IMessageFactory;
import fr.mby.portal.message.IMessage;

/**
 * @author Maxime Bossard - 2013
 *
 */
public class BasicMessageFactory implements IMessageFactory {

	@Override
	public IMessage build(final MessageType messageType) {
		final IMessage message;
		
		switch (messageType) {
		case ACTION:
			message = new BasicActionMessage();
			break;
		case EVENT:
			message = new BasicEventMessage();
			break;
		case RENDER:
			message = new BasicRenderMessage();
			break;
		default:
			throw new IllegalArgumentException("Unknown message type !");
		}
		
		return message;
	}

}
