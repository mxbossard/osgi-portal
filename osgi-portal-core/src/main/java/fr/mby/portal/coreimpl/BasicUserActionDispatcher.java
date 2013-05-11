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

package fr.mby.portal.coreimpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.core.IUserActionDispatcher;
import fr.mby.portal.core.message.IMessageFactory;
import fr.mby.portal.core.message.IReplyFactory;
import fr.mby.portal.message.IMessage;
import fr.mby.portal.message.IMessage.MessageType;
import fr.mby.portal.message.IMessageDispatcher;
import fr.mby.portal.message.IReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicUserActionDispatcher implements IUserActionDispatcher, InitializingBean {

	@Autowired
	private IMessageFactory messageFactory;

	@Autowired
	private IReplyFactory replyFactory;

	@Autowired
	private IMessageDispatcher messageDispatcher;

	@Override
	public void dispatch(final HttpServletRequest request, final HttpServletResponse response) {
		this.internalDispatch(request, response, MessageType.ACTION);
		this.internalDispatch(request, response, MessageType.RENDER);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.messageFactory, "No IMessageFactory configured");
		Assert.notNull(this.replyFactory, "No IReplyFactory configured");
		Assert.notNull(this.messageDispatcher, "No IMessageDispatcher configured");
	}

	protected void internalDispatch(final HttpServletRequest request, final HttpServletResponse response,
			final MessageType messageType) {
		final IMessage message = this.messageFactory.build(request, messageType);
		final IReply reply = this.replyFactory.build(response, messageType);

		this.internalDispatch(message, reply);
	}

	protected void internalDispatch(final IMessage message, final IReply reply) {

		this.messageDispatcher.dispatch(message, reply);
	}

}
