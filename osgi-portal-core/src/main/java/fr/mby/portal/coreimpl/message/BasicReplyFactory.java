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

import org.springframework.stereotype.Service;

import fr.mby.portal.action.IUserAction;
import fr.mby.portal.core.message.IReplyFactory;
import fr.mby.portal.message.IMessage.MessageType;
import fr.mby.portal.message.IReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicReplyFactory implements IReplyFactory {

	@Override
	public IReply build(final IUserAction userAction, final MessageType messageType) {
		final IReply reply;

		switch (messageType) {
			case ACTION :
				reply = new BasicActionReply();
				break;
			case RENDER :
				reply = new BasicRenderReply();
				break;
			case EVENT :
				reply = new BasicEventReply();
				break;
			default :
				throw new IllegalArgumentException("Unknown message type !");
		}

		return reply;
	}

}