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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.app.IAppContext;
import fr.mby.portal.api.app.ISession;
import fr.mby.portal.core.IPortalRenderer;
import fr.mby.portal.core.context.IAppContextResolver;
import fr.mby.portal.core.session.ISessionManager;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class MemorySessionManager implements ISessionManager {

	private static final Logger LOG = LogManager.getLogger(MemorySessionManager.class);

	private final Set<String> generatedSessionIds = Collections.synchronizedSet(new HashSet<String>(1024));

	private final Map<String, SessionBucket> sessionBucketCache = new HashMap<String, SessionBucket>(1024);

	@Autowired(required = true)
	private IAppContextResolver<IUserAction> appContextResolver;

	@Override
	public ISession getAppSession(final IUserAction userAction, final boolean create) {
		final IAppContext appContext = this.appContextResolver.resolve(userAction);

		final SessionBucket sessionBucket = this.loadSessionBucket(userAction.getPortalSessionId());
		final ISession appSession = sessionBucket.getAppSession(appContext, create);

		return appSession;
	}

	@Override
	public ISession getPortalSession(final HttpServletRequest request) {
		final String portalSessionId = this.getPortalSessionId(request);

		final SessionBucket sessionBucket = this.loadSessionBucket(portalSessionId);
		final ISession portalSession = sessionBucket.getPortalSession();

		return portalSession;
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
	public void destroySessions(final HttpServletRequest request) {
		final String portalSessionId = this.getPortalSessionId(request);
		final SessionBucket sessionBucket = this.sessionBucketCache.remove(portalSessionId);

		sessionBucket.destroy();
	}

	/**
	 * Load the corresponding session bucket from cache.
	 * 
	 * @param portalSessionId
	 * @return
	 */
	protected SessionBucket loadSessionBucket(final String portalSessionId) {
		SessionBucket sessionBucket = this.sessionBucketCache.get(portalSessionId);

		if (sessionBucket == null) {
			sessionBucket = new SessionBucket(portalSessionId);
			this.sessionBucketCache.put(portalSessionId, sessionBucket);
		}

		return sessionBucket;
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
			MemorySessionManager.LOG.error("Error while generating Portal Session Id !", e);
			throw new RuntimeException("Error while generating Portal Session Id !", e);
		}

		return hashedSessionId;
	}

	/** Bucket which contain all sessions matching a portal session Id. */
	private class SessionBucket {

		private final String portalSessionId;

		private ISession portalSession;

		private Map<IAppContext, ISession> appSessionCache;

		/**
		 * @param portalSessionId
		 */
		protected SessionBucket(final String portalSessionId) {
			super();
			this.portalSessionId = portalSessionId;

			this.portalSession = new BasicSession();
			this.appSessionCache = new HashMap<IAppContext, ISession>(8);
		}

		public void destroy() {
			this.appSessionCache.clear();
			this.appSessionCache = null;
			this.portalSession = null;
		}

		/**
		 * Getter of portalSession.
		 * 
		 * @return the portalSession
		 */
		public ISession getPortalSession() {
			return this.portalSession;
		}

		/**
		 * Getter of appSessionCache.
		 * 
		 * @return the appSessionCache
		 */
		public ISession getAppSession(final IAppContext appContext, final boolean create) {
			ISession appSession = this.appSessionCache.get(appContext);

			if (appSession == null && create) {
				appSession = new BasicSession();
				this.appSessionCache.put(appContext, appSession);
			}

			return appSession;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + this.getOuterType().hashCode();
			result = prime * result + ((this.portalSessionId == null) ? 0 : this.portalSessionId.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (this.getClass() != obj.getClass()) {
				return false;
			}
			final SessionBucket other = (SessionBucket) obj;
			if (!this.getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (this.portalSessionId == null) {
				if (other.portalSessionId != null) {
					return false;
				}
			} else if (!this.portalSessionId.equals(other.portalSessionId)) {
				return false;
			}
			return true;
		}

		private MemorySessionManager getOuterType() {
			return MemorySessionManager.this;
		}

	}
}
