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

package fr.mby.portal.coreimpl.session;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.app.ISession;
import fr.mby.portal.core.IPortalRenderer;
import fr.mby.portal.core.session.ISessionManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class UniqueSessionManager implements ISessionManager, InitializingBean {

	private static final Logger LOG = LogManager.getLogger(UniqueSessionManager.class);

	private ISession uniqueSession = new BasicSession();

	private final Set<String> generatedSessionIds = Collections.synchronizedSet(new HashSet<String>(1024));

	@Override
	public ISession getAppSession(final IUserAction userAction, final boolean create) {
		return this.uniqueSession;
	}

	@Override
	public String getPortalSessionId(final HttpServletRequest request) {
		// Search Portal Session Id in Http Session
		String portalSessionId = (String) request.getSession(true).getAttribute(
				IPortalRenderer.PORTAL_SESSION_ID_REQUEST_PARAM);

		// Search Portal Session Id in Http Request params
		if (!StringUtils.hasText(portalSessionId)) {
			portalSessionId = request.getParameter(IPortalRenderer.PORTAL_SESSION_ID_REQUEST_PARAM);
		}

		// Generate Portal Session Id
		if (!StringUtils.hasText(portalSessionId)) {
			portalSessionId = this.genSessionId(request);
			request.getSession(true).setAttribute(IPortalRenderer.PORTAL_SESSION_ID_REQUEST_PARAM, portalSessionId);
		}

		return portalSessionId;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.uniqueSession = new BasicSession();
	}

	/**
	 * Generate a Portal session Id.
	 * 
	 * @param request
	 * @return the generated portal session Id
	 */
	protected String genSessionId(final HttpServletRequest request) {
		String portalSessionId = null;

		do {
			// Generate Id while we don't generate a new one
			portalSessionId = RandomStringUtils.randomAlphanumeric(16);
		} while (this.generatedSessionIds.contains(portalSessionId));

		this.generatedSessionIds.add(portalSessionId);

		return portalSessionId;
	}

	/**
	 * @param request
	 */
	protected String hashHttpSessionId(final HttpServletRequest request) {
		String hashedSessionId = null;

		final String sessionId = request.getSession(true).getId();
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			digest.update(sessionId.getBytes("UTF-8"));
			final byte[] hash = digest.digest();
			hashedSessionId = new String(hash);
		} catch (final Exception e) {
			UniqueSessionManager.LOG.error("Error while generating Portal Session Id !", e);
			throw new RuntimeException("Error while generating Portal Session Id !", e);
		}

		return hashedSessionId;
	}

}
