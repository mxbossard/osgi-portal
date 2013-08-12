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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.IPortalService;
import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.app.IApp;
import fr.mby.portal.api.app.ISession;
import fr.mby.portal.core.IPortalRenderer;
import fr.mby.portal.core.context.IAppContextResolver;
import fr.mby.portal.core.session.ISessionManager;
import fr.mby.portal.core.session.SessionNotInitializedException;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class MemorySessionManager implements ISessionManager {

	private static final Logger LOG = LogManager.getLogger(MemorySessionManager.class);

	private final Set<String> generatedSessionIds = Collections.synchronizedSet(new HashSet<String>(1024));

	private final Map<String, SessionBucket> sessionBucketCache = new HashMap<String, SessionBucket>(1024);

	private final Map<IApp, ISession> appSessionCache = new HashMap<IApp, ISession>(1024);

	@Autowired
	private IPortalService portalService;

	@Autowired
	private IAppContextResolver<IUserAction> appContextResolver;

	@Override
	public ISession getAppSession(final HttpServletRequest request, final boolean create) {
		Assert.notNull(request, "No HttpServletRequest supplied !");

		final IApp targetedApp = this.portalService.getTargetedApp(request);

		return this.getAppSession(targetedApp, create);
	}

	@Override
	public ISession getAppSession(final IApp app, final boolean create) {
		Assert.notNull(app, "No IApp supplied !");

		ISession appSession = this.appSessionCache.get(app);
		if (appSession == null) {
			appSession = new BasicSession(app.getSignature());
			this.appSessionCache.put(app, appSession);
		}

		return appSession;
	}

	@Override
	public void initPortalSession(final HttpServletRequest request, final HttpServletResponse response) {
		String portalSessionId = this.getPortalSessionId(request);

		if (portalSessionId == null) {
			// Can't find session Id => session wasn't initialized
			portalSessionId = this.genSessionId(request);

			this.initSessionBucket(portalSessionId);

			// Put sessionId in Cookie
			final Cookie portalSessionCookie = new Cookie(IPortalRenderer.PORTAL_SESSION_ID_COOKIE_NAME,
					portalSessionId);
			portalSessionCookie.setPath("/");
			response.addCookie(portalSessionCookie);

			// Put sessionId in current Http request
			request.setAttribute(IPortalRenderer.PORTAL_SESSION_ID_PARAM_NAME, portalSessionId);
		}

	}

	@Override
	public ISession getSharedSession(final HttpServletRequest request) throws SessionNotInitializedException {
		final String portalSessionId = this.getPortalSessionId(request);

		if (portalSessionId == null) {
			throw new SessionNotInitializedException("Trying to retrieve a not initialized portal session !");
		}

		final SessionBucket sessionBucket = this.loadSessionBucket(portalSessionId);
		final ISession sharedSession = sessionBucket.getSharedSession();

		return sharedSession;
	}

	@Override
	public ISession getPortalSession(final HttpServletRequest request) throws SessionNotInitializedException {
		final String portalSessionId = this.getPortalSessionId(request);

		if (portalSessionId == null) {
			throw new SessionNotInitializedException("Trying to retrieve a not initialized portal session !");
		}

		final SessionBucket sessionBucket = this.loadSessionBucket(portalSessionId);
		final ISession portalSession = sessionBucket.getPortalSession();

		return portalSession;
	}

	@Override
	public String getPortalSessionId(final HttpServletRequest request) {
		String portalSessionId = null;

		// Put sessionId in current Http request
		final Object attrValue = request.getAttribute(IPortalRenderer.PORTAL_SESSION_ID_PARAM_NAME);
		if (attrValue != null && attrValue instanceof String) {
			portalSessionId = (String) attrValue;
		}

		if (!StringUtils.hasText(portalSessionId)) {
			final Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (final Cookie cookie : cookies) {
					if (cookie != null && IPortalRenderer.PORTAL_SESSION_ID_COOKIE_NAME.equals(cookie.getName())) {
						portalSessionId = cookie.getValue();
					}
				}
			}
		}

		if (!StringUtils.hasText(portalSessionId)) {
			// Search Portal Session Id in Http Session
			portalSessionId = (String) request.getSession(true).getAttribute(
					IPortalRenderer.PORTAL_SESSION_ID_PARAM_NAME);
		}

		if (!StringUtils.hasText(portalSessionId)) {
			// Search Portal Session Id in Http Request params
			portalSessionId = request.getParameter(IPortalRenderer.PORTAL_SESSION_ID_PARAM_NAME);
		}

		// Null is the default value
		if (!StringUtils.hasText(portalSessionId) || !this.sessionBucketCache.containsKey(portalSessionId)) {
			// If the session Id cannot be found in the cache we cannot trust the session Id found.
			portalSessionId = null;
		}

		return portalSessionId;
	}

	@Override
	public void destroySessions(final HttpServletRequest request, final HttpServletResponse response) {
		final String portalSessionId = this.getPortalSessionId(request);
		if (portalSessionId != null) {
			final SessionBucket sessionBucket = this.sessionBucketCache.remove(portalSessionId);
			sessionBucket.destroy();
			final Cookie portalSessionCookie = new Cookie(IPortalRenderer.PORTAL_SESSION_ID_COOKIE_NAME,
					"SESSION_DESTROYED");
			portalSessionCookie.setPath("/");
			response.addCookie(portalSessionCookie);

			this.generatedSessionIds.remove(portalSessionId);
		}
	}

	/**
	 * Init a session bucket with new SessionId.
	 * 
	 * @param portalSessionId
	 * @return
	 */
	protected SessionBucket initSessionBucket(final String portalSessionId) {
		SessionBucket sessionBucket = this.sessionBucketCache.get(portalSessionId);

		if (sessionBucket == null) {
			sessionBucket = new SessionBucket(portalSessionId);
			this.sessionBucketCache.put(portalSessionId, sessionBucket);
		}

		return sessionBucket;
	}

	/**
	 * Load the corresponding session bucket from cache.
	 * 
	 * @param portalSessionId
	 * @return
	 */
	protected SessionBucket loadSessionBucket(final String portalSessionId) {
		final SessionBucket sessionBucket = this.sessionBucketCache.get(portalSessionId);

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

		private ISession sharedSession;

		/**
		 * @param portalSessionId
		 */
		protected SessionBucket(final String portalSessionId) {
			super();
			this.portalSessionId = portalSessionId;

			this.portalSession = new BasicSession(portalSessionId);
			this.sharedSession = new BasicSession(portalSessionId);
		}

		public void destroy() {
			this.portalSession = null;
			this.sharedSession = null;
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
		 * Getter of sharedSession.
		 * 
		 * @return the sharedSession
		 */
		public ISession getSharedSession() {
			return this.sharedSession;
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
