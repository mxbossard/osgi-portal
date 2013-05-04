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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.action.IUserAction;
import fr.mby.portal.app.IPortalApp;
import fr.mby.portal.app.IPortalAppResolver;
import fr.mby.portal.message.IActionMessage;
import fr.mby.portal.message.IActionReply;
import fr.mby.portal.message.IEventMessage;
import fr.mby.portal.message.IEventReply;
import fr.mby.portal.message.IMessage;
import fr.mby.portal.message.IMessageDispatcher;
import fr.mby.portal.message.IRenderMessage;
import fr.mby.portal.message.IRenderReply;
import fr.mby.portal.message.IReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicMessageDispatcher implements IMessageDispatcher, InitializingBean {

	@Autowired
	private IPortalAppResolver<IUserAction> portalAppResolver;

	@Override
	public void dispatch(final IMessage message, final IReply reply) {
		final Iterable<IPortalApp> portalApps = this.portalAppResolver.resolve(message.getUserAction());

		if (portalApps != null) {
			for (IPortalApp portalApp : portalApps) {
				this.dispatch(portalApp, message, reply);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.portalAppResolver, "No IPortalAppResolver configured !");
	}

	/**
	 * @param portalApp
	 * @param message
	 * @param reply
	 */
	protected void dispatch(final IPortalApp portalApp, final IMessage message, final IReply reply) {
		if (portalApp != null) {
			if (this.isActionMessage(message, reply)) {
				portalApp.processAction((IActionMessage) message, (IActionReply) reply);
			} else if (this.isRenderMessage(message, reply)) {
				portalApp.render((IRenderMessage) message, (IRenderReply) reply);
			} else if (this.isEventMessage(message, reply)) {
				// TODO implement event listener
			}
		}
	}

	protected boolean isActionMessage(final IMessage message, final IReply reply) {
		return message != null && reply != null && IActionMessage.class.isAssignableFrom(message.getClass())
				&& IActionReply.class.isAssignableFrom(reply.getClass());
	}

	protected boolean isRenderMessage(final IMessage message, final IReply reply) {
		return message != null && reply != null && IRenderMessage.class.isAssignableFrom(message.getClass())
				&& IRenderReply.class.isAssignableFrom(reply.getClass());
	}

	protected boolean isEventMessage(final IMessage message, final IReply reply) {
		return message != null && reply != null && IEventMessage.class.isAssignableFrom(message.getClass())
				&& IEventReply.class.isAssignableFrom(reply.getClass());
	}

}
