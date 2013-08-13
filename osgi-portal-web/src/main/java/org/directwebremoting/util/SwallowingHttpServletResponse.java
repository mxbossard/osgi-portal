/*
 * Copyright 2005 Joe Walker
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

package org.directwebremoting.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Used by ExecutionContext to forward results back via javascript.
 * <p>
 * We could like to implement {@link HttpServletResponse}, but there is a bug in WebLogic where it casts to a
 * {@link HttpServletResponseWrapper} so we need to extend that.
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class SwallowingHttpServletResponse extends HttpServletResponseWrapper implements HttpServletResponse {

	/**
	 * Create a new HttpServletResponse that allows you to catch the body
	 * 
	 * @param response
	 *            The original HttpServletResponse
	 * @param sout
	 *            The place we copy responses to
	 * @param characterEncoding
	 *            The output encoding
	 */
	public SwallowingHttpServletResponse(final HttpServletResponse response, final Writer sout,
			final String characterEncoding) {
		super(response);

		this.pout = new PrintWriter(sout);
		this.outputStream = new WriterOutputStream(sout, characterEncoding);

		this.characterEncoding = characterEncoding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#addCookie(javax.servlet.http.Cookie)
	 */
	@Override
	public void addCookie(final Cookie cookie) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#addDateHeader(java.lang.String, long)
	 */
	@Override
	public void addDateHeader(final String name, final long value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(final String name, final String value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#addIntHeader(java.lang.String, int)
	 */
	@Override
	public void addIntHeader(final String name, final int value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#containsHeader(java.lang.String)
	 */
	@Override
	public boolean containsHeader(final String name) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#encodeRedirectUrl(java.lang.String)
	 */
	@Override
	public String encodeRedirectUrl(final String url) {
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#encodeRedirectURL(java.lang.String)
	 */
	@Override
	public String encodeRedirectURL(final String url) {
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#encodeUrl(java.lang.String)
	 */
	@Override
	public String encodeUrl(final String url) {
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#encodeURL(java.lang.String)
	 */
	@Override
	public String encodeURL(final String url) {
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#flushBuffer()
	 */
	@Override
	public void flushBuffer() throws IOException {
		this.pout.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#getBufferSize()
	 */
	@Override
	public int getBufferSize() {
		return this.bufferSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#getCharacterEncoding()
	 */
	@Override
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	/**
	 * @return The MIME type of the content
	 * @see javax.servlet.ServletResponse#setContentType(String)
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}

	/**
	 * Accessor for any error messages set using {@link #sendError(int)} or {@link #sendError(int, String)}
	 * 
	 * @return The current error message
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#getLocale()
	 */
	@Override
	public Locale getLocale() {
		return this.locale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
	@Override
	public ServletOutputStream getOutputStream() {
		return this.outputStream;
	}

	/**
	 * Accessor for the redirect URL set using {@link #sendRedirect(String)}
	 * 
	 * @return The redirect URL
	 */
	public String getRedirectedUrl() {
		return this.redirectedUrl;
	}

	/**
	 * What HTTP status code should be returned?
	 * 
	 * @return The current http status code
	 */
	public int getStatus() {
		return this.status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
	@Override
	public PrintWriter getWriter() {
		return this.pout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#isCommitted()
	 */
	@Override
	public boolean isCommitted() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#reset()
	 */
	@Override
	public void reset() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#resetBuffer()
	 */
	@Override
	public void resetBuffer() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#sendError(int)
	 */
	@Override
	public void sendError(final int newStatus) {
		if (this.committed) {
			throw new IllegalStateException("Cannot set error status - response is already committed");
		}

		SwallowingHttpServletResponse.log.warn("Ignoring call to sendError(" + newStatus + ')');

		this.status = newStatus;
		this.committed = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#sendError(int, java.lang.String)
	 */
	@Override
	public void sendError(final int newStatus, final String newErrorMessage) {
		if (this.committed) {
			throw new IllegalStateException("Cannot set error status - response is already committed");
		}

		SwallowingHttpServletResponse.log
				.warn("Ignoring call to sendError(" + newStatus + ", " + newErrorMessage + ')');

		this.status = newStatus;
		this.errorMessage = newErrorMessage;
		this.committed = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#sendRedirect(java.lang.String)
	 */
	@Override
	public void sendRedirect(final String location) {
		if (this.committed) {
			throw new IllegalStateException("Cannot send redirect - response is already committed");
		}

		SwallowingHttpServletResponse.log.warn("Ignoring call to sendRedirect(" + location + ')');

		this.redirectedUrl = location;
		this.committed = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#setBufferSize(int)
	 */
	@Override
	public void setBufferSize(final int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @param characterEncoding
	 *            The new encoding to use for response strings
	 * @see javax.servlet.ServletResponseWrapper#getCharacterEncoding()
	 */
	@Override
	public void setCharacterEncoding(final String characterEncoding) {
		this.characterEncoding = characterEncoding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#setContentLength(int)
	 */
	@Override
	public void setContentLength(final int i) {
		// The content length of the original document is not likely to be the
		// same as the content length of the new document.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#setContentType(java.lang.String)
	 */
	@Override
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#setDateHeader(java.lang.String, long)
	 */
	@Override
	public void setDateHeader(final String name, final long value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void setHeader(final String name, final String value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String, int)
	 */
	@Override
	public void setIntHeader(final String name, final int value) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletResponseWrapper#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(final Locale locale) {
		this.locale = locale;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletResponseWrapper#setStatus(int)
	 */
	@Override
	public void setStatus(final int status) {
		this.status = status;
		SwallowingHttpServletResponse.log.warn("Ignoring call to setStatus(" + status + ')');
	}

	/**
	 * @see javax.servlet.http.HttpServletResponse#setStatus(int, java.lang.String)
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void setStatus(final int newStatus, final String newErrorMessage) {
		this.status = newStatus;
		this.errorMessage = newErrorMessage;
		SwallowingHttpServletResponse.log
				.warn("Ignoring call to setStatus(" + newStatus + ", " + newErrorMessage + ')');
	}

	/**
	 * The ignored buffer size
	 */
	private int bufferSize = 0;

	/**
	 * The character encoding used
	 */
	private String characterEncoding;

	/**
	 * Has the response been comitted
	 */
	private boolean committed = false;

	/**
	 * The MIME type of the output body
	 */
	private String contentType;

	/**
	 * The error message sent with a status != HttpServletResponse.SC_OK
	 */
	private String errorMessage;

	/**
	 * Locale setting: defaults to platform default
	 */
	private Locale locale = Locale.getDefault();

	/**
	 * The forwarding output stream
	 */
	private final ServletOutputStream outputStream;

	/**
	 * The forwarding output stream
	 */
	private final PrintWriter pout;

	/**
	 * Where are we to redirect the user to?
	 */
	private String redirectedUrl;

	/**
	 * The HTTP status
	 */
	private int status = HttpServletResponse.SC_OK;

	/**
	 * The log stream
	 */
	private static final Logger log = Logger.getLogger(SwallowingHttpServletResponse.class);
}
