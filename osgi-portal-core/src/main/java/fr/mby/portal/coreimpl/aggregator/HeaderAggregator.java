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

package fr.mby.portal.coreimpl.aggregator;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import fr.mby.portal.core.message.IInternalReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class HeaderAggregator extends AbstractReplyAggregator<IInternalReply> {

	/** Logger. */
	@SuppressWarnings("unused")
	private static final Logger log = LogManager.getLogger(HeaderAggregator.class);

	@Override
	public void aggregate(final HttpServletResponse response, final IInternalReply reply) {
		// Add cookies
		final Collection<Cookie> cookies = reply.getCookies();
		if (!CollectionUtils.isEmpty(cookies)) {
			for (final Cookie cookie : cookies) {
				response.addCookie(cookie);
			}
		}

		// Add headers
		final Map<String, String> headers = reply.getHeaders();
		if (!CollectionUtils.isEmpty(headers)) {
			for (final Entry<String, String> header : headers.entrySet()) {
				response.addHeader(header.getKey(), header.getValue());
			}
		}
	}

}
