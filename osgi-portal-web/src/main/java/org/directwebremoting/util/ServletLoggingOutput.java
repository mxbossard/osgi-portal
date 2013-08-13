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

import javax.servlet.http.HttpServlet;

/**
 * An implementation of LoggingOutput that sends stuff to the Servlet.log stream.
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ServletLoggingOutput implements LoggingOutput {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#debug(java.lang.String)
	 */
	@Override
	public void debug(final String message) {
		ServletLoggingOutput.log(LoggingOutput.LEVEL_DEBUG, message, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#info(java.lang.String)
	 */
	@Override
	public void info(final String message) {
		ServletLoggingOutput.log(LoggingOutput.LEVEL_INFO, message, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#warn(java.lang.String)
	 */
	@Override
	public void warn(final String message) {
		ServletLoggingOutput.log(LoggingOutput.LEVEL_WARN, message, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#warn(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void warn(final String message, final Throwable th) {
		ServletLoggingOutput.log(LoggingOutput.LEVEL_WARN, message, th);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#error(java.lang.String)
	 */
	@Override
	public void error(final String message) {
		ServletLoggingOutput.log(LoggingOutput.LEVEL_ERROR, message, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#error(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void error(final String message, final Throwable th) {
		ServletLoggingOutput.log(LoggingOutput.LEVEL_ERROR, message, th);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#fatal(java.lang.String)
	 */
	@Override
	public void fatal(final String message) {
		ServletLoggingOutput.log(LoggingOutput.LEVEL_FATAL, message, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#fatal(java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void fatal(final String message, final Throwable th) {
		ServletLoggingOutput.log(LoggingOutput.LEVEL_FATAL, message, th);
	}

	/**
	 * Internal log implementation.
	 * 
	 * @param loglevel
	 *            The level to log at
	 * @param message
	 *            The (optional) message to log
	 * @param th
	 *            The (optional) exception
	 */
	private static void log(final int loglevel, final String message, final Throwable th) {
		if (loglevel >= ServletLoggingOutput.level) {
			final HttpServlet servlet = (HttpServlet) ServletLoggingOutput.servlets.get();
			if (servlet != null) {
				// Tomcat 4 NPEs is th is null
				if (th == null) {
					servlet.log(message);
				} else {
					servlet.log(message, th);
				}
			} else {
				if (message != null) {
					System.out.println(message);
				}

				if (th != null) {
					th.printStackTrace();
				}
			}
		}
	}

	/**
	 * Associate a servlet with this thread for logging purposes.
	 * 
	 * @param servlet
	 *            The servlet to use for logging in this thread
	 */
	public static void setExecutionContext(final HttpServlet servlet) {
		ServletLoggingOutput.servlets.set(servlet);
	}

	/**
	 * Remove the servlet from this thread for logging purposes
	 */
	public static void unsetExecutionContext() {
		ServletLoggingOutput.servlets.set(null);
	}

	/**
	 * String version of setLevel.
	 * 
	 * @param logLevel
	 *            One of FATAL, ERROR, WARN, INFO, DEBUG
	 */
	public static void setLevel(final String logLevel) {
		if (logLevel.equalsIgnoreCase("FATAL")) {
			ServletLoggingOutput.setLevel(LoggingOutput.LEVEL_FATAL);
		} else if (logLevel.equalsIgnoreCase("ERROR")) {
			ServletLoggingOutput.setLevel(LoggingOutput.LEVEL_ERROR);
		} else if (logLevel.equalsIgnoreCase("WARN")) {
			ServletLoggingOutput.setLevel(LoggingOutput.LEVEL_WARN);
		} else if (logLevel.equalsIgnoreCase("INFO")) {
			ServletLoggingOutput.setLevel(LoggingOutput.LEVEL_INFO);
		} else if (logLevel.equalsIgnoreCase("DEBUG")) {
			ServletLoggingOutput.setLevel(LoggingOutput.LEVEL_DEBUG);
		} else {
			throw new IllegalArgumentException("Unknown log level: " + logLevel);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.directwebremoting.util.LoggingOutput#isDebugEnabled()
	 */
	@Override
	public boolean isDebugEnabled() {
		return ServletLoggingOutput.level == LoggingOutput.LEVEL_DEBUG;
	}

	/**
	 * @param level
	 *            The logging level to set.
	 */
	public static void setLevel(final int level) {
		ServletLoggingOutput.level = level;
	}

	/**
	 * @return Returns the logging level.
	 */
	public static int getLevel() {
		return ServletLoggingOutput.level;
	}

	/**
	 * The container for all known threads
	 */
	private static final ThreadLocal servlets = new ThreadLocal();

	/**
	 * What is the current debug level?
	 */
	private static int level = LoggingOutput.LEVEL_WARN;
}
