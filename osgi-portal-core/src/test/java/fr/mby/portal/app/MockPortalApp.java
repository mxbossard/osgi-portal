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

package fr.mby.portal.app;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;

import fr.mby.portal.api.app.IAppConfig;
import fr.mby.portal.api.app.IEventApp;
import fr.mby.portal.api.app.IPortalApp;
import fr.mby.portal.api.message.IActionMessage;
import fr.mby.portal.api.message.IActionReply;
import fr.mby.portal.api.message.IEventMessage;
import fr.mby.portal.api.message.IEventReply;
import fr.mby.portal.api.message.IRenderMessage;
import fr.mby.portal.api.message.IRenderReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class MockPortalApp implements IPortalApp, IEventApp {

	@Override
	public void destory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(final IAppConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processAction(final IActionMessage request, final IActionReply response) {
		response.setProperty("actionProp1", "actionVal1");
		response.addProperty(new Cookie("actionCookie1", "actionCookieVal1"));
	}

	@Override
	public void render(final IRenderMessage request, final IRenderReply response) {
		response.setProperty("renderProp1", "renderVal1");
		response.addProperty(new Cookie("renderCookie1", "renderCookieVal1"));

		try {
			final PrintWriter writer = response.getWriter();

			writer.append("<html>");
			writer.append("<body>");
			writer.append("<h1>Test</h1>");
			writer.append("</body>");
			writer.append("</html>");

		} catch (final IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void processEvent(final IEventMessage request, final IEventReply response) {
		// TODO Auto-generated method stub

	}

}
