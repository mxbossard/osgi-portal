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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.event.IEvent;
import fr.mby.portal.api.message.IMessage;
import fr.mby.portal.api.message.IMessage.MessageType;
import fr.mby.portal.core.action.IUserActionFactory;
import fr.mby.portal.core.event.IEventFactory;
import fr.mby.portal.core.message.IMessageFactory;
import fr.mby.portal.core.session.ISessionManager;

/**
 * FIXME: We build a message from a request. They may be multiple different message to build from a single request !
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicMessageFactory implements IMessageFactory {

	@Autowired
	private ISessionManager sessionManager;

	@Autowired
	private IEventFactory eventFactory;

	@Autowired
	private IUserActionFactory userActionFactory;

	@Override
	public IMessage build(final HttpServletRequest request, final MessageType messageType) {
		Assert.notNull(request, "No HttpServletRequest provided !");
		Assert.notNull(messageType, "No MessageType provided !");

		final IUserAction userAction = this.userActionFactory.build(request);

		final IMessage message;

		switch (messageType) {
			case ACTION :
				message = new BasicActionMessage(userAction);
				break;
			case RENDER :
				message = new BasicRenderMessage(userAction);
				break;
			case EVENT :
				final IEvent event = this.eventFactory.build("testEvent", "eventValue");
				message = new BasicEventMessage(userAction, event);
				break;
			default :
				throw new IllegalArgumentException("Unknown message type !");
		}

		return message;
	}

}
