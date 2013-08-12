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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.app.IAppContext;
import fr.mby.portal.api.app.IAppPreferences;
import fr.mby.portal.api.app.ISession;
import fr.mby.portal.api.event.IEvent;
import fr.mby.portal.api.message.IMessage;
import fr.mby.portal.api.message.IMessage.MessageType;
import fr.mby.portal.core.action.IUserActionFactory;
import fr.mby.portal.core.context.IAppContextResolver;
import fr.mby.portal.core.event.IEventFactory;
import fr.mby.portal.core.message.IMessageFactory;
import fr.mby.portal.core.preferences.IAppPreferencesResolver;
import fr.mby.portal.core.session.ISessionManager;
import fr.mby.portal.core.session.SessionNotInitializedException;

/**
 * FIXME: We build a message from a request. They may be multiple different message to build from a single request !
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicMessageFactory implements IMessageFactory, InitializingBean {

	@Autowired
	private IAppContextResolver<IUserAction> appContextResolver;

	@Autowired
	private IAppPreferencesResolver<IUserAction> appPreferencesResolver;

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
		final IAppContext appContext = this.appContextResolver.resolve(userAction);
		final IAppPreferences appPrefs = this.appPreferencesResolver.resolve(userAction);
		ISession appSession;
		try {
			appSession = this.sessionManager.getSharedSession(request);
		} catch (final SessionNotInitializedException e) {
			// Problem !
			throw new IllegalStateException(e);
		}

		final IMessage message;

		switch (messageType) {
			case ACTION :
				message = new BasicActionMessage(appContext, appSession, appPrefs, userAction);
				break;
			case RENDER :
				message = new BasicRenderMessage(appContext, appSession, appPrefs, userAction);
				break;
			case EVENT :
				final IEvent event = this.eventFactory.build("testEvent", "eventValue");
				message = new BasicEventMessage(appContext, appSession, appPrefs, userAction, event);
				break;
			default :
				throw new IllegalArgumentException("Unknown message type !");
		}

		return message;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.appContextResolver, "No IAppContextResolver provided !");
		Assert.notNull(this.appPreferencesResolver, "No IAppPreferencesResolver provided !");
		Assert.notNull(this.sessionManager, "No ISessionManager provided !");
		Assert.notNull(this.eventFactory, "No IEventFactory provided !");
		Assert.notNull(this.userActionFactory, "No IUserActionFactory configured");
	}

}
