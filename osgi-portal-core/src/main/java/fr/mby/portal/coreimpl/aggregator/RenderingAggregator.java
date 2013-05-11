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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import fr.mby.portal.core.message.IInternalRenderReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class RenderingAggregator extends AbstractReplyAggregator<IInternalRenderReply> {

	/** Logger. */
	private static final Logger log = LogManager.getLogger(RenderingAggregator.class);

	@Override
	public void aggregate(final HttpServletResponse response, final IInternalRenderReply reply) {
		try {
			reply.flushBuffer();

			final PrintWriter writer = response.getWriter();
			final OutputStream replyStream = reply.getBackingOutputStream();

			writer.write(replyStream.toString());
			replyStream.close();
		} catch (final IOException e) {
			RenderingAggregator.log.error("Error while rendering IReply !", e);
		}

	}

}
