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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is intended to be used by Logger when commons-logging is available, but to not force Logger itself to
 * depend on commons-logging so Logger can catch the ClassDefNotFoundError and use other methods.
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class CommonsLoggingOutput implements LoggingOutput {

	/**
	 * Create a logger specific to commons-logging
	 * 
	 * @param base
	 *            The class to log against.
	 */
	public CommonsLoggingOutput(final Class base) {
		this.log = LogFactory.getLog(base);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#debug(java.lang.String)
	 */
	@Override
	public void debug(final String message) {
		this.log.debug(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#info(java.lang.String)
	 */
	@Override
	public void info(final String message) {
		this.log.info(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#warn(java.lang.String)
	 */
	@Override
	public void warn(final String message) {
		this.log.warn(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#warn(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void warn(final String message, final Throwable th) {
		this.log.warn(message, th);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#error(java.lang.String)
	 */
	@Override
	public void error(final String message) {
		this.log.error(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#error(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(final String message, final Throwable th) {
		this.log.error(message, th);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#fatal(java.lang.String)
	 */
	@Override
	public void fatal(final String message) {
		this.log.fatal(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#fatal(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void fatal(final String message, final Throwable th) {
		this.log.fatal(message, th);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#isDebugEnabled()
	 */
	@Override
	public boolean isDebugEnabled() {
		return this.log.isDebugEnabled();
	}

	private final Log log;
}