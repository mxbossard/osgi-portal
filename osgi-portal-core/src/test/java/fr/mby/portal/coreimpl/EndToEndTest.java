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

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.mby.portal.core.IUserActionDispatcher;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:endToEndTestContext.xml")
public class EndToEndTest {

	@Autowired
	private IUserActionDispatcher userActionDispatcher;

	/**
	 * End to end test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDispatch() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();

		this.userActionDispatcher.dispatch(request, response);

		// Test headers
		final String actionHeader1 = response.getHeader("actionProp1");
		final String renderHeader1 = response.getHeader("renderProp1");
		Assert.assertEquals("Bad action header value !", "actionVal1", actionHeader1);
		Assert.assertEquals("Bad render header value !", "renderVal1", renderHeader1);

		final Cookie actionCookie1 = response.getCookie("actionCookie1");
		final Cookie renderCookie1 = response.getCookie("renderCookie1");
		Assert.assertNotNull("Action cookie is null !", actionCookie1);
		Assert.assertNotNull("Render cookie is null !", renderCookie1);
		Assert.assertEquals("Bad action cookie value !", "actionCookieVal1", actionCookie1.getValue());
		Assert.assertEquals("Bad render cookie value !", "renderCookieVal1", renderCookie1.getValue());

		// Test response
		response.flushBuffer();
		final String reponseOutputStream = response.getContentAsString();
		Assert.assertEquals("Bad response output stream !", "<html><body><h1>Test</h1></body></html>",
				reponseOutputStream);
	}
}
