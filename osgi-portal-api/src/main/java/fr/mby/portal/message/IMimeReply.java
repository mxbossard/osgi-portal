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

package fr.mby.portal.message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IMimeReply extends IReply {

	/**
	 * Returns the MIME type that can be used to contribute markup to the render response. If no content type was set
	 * previously using the setContentType(java.lang.String) method this method returns null.
	 * 
	 * @return the MIME type of the response, or null if no content type is set
	 */
	String getContentType();

	/**
	 * Sets the MIME type for the render response. The portlet should set the content type before calling
	 * MimeResponse.getWriter() or MimeResponse.getPortletOutputStream(). Calling setContentType after getWriter or
	 * getOutputStream does not change the content type. The portlet container will ignore any character encoding
	 * specified as part of the content type for render calls.
	 * 
	 * @param type
	 *            the content MIME type
	 * @throws IllegalArgumentException
	 *             if the given type is not in the list returned by PortletRequest.getResponseContentTypes
	 */
	void setContentType(String type) throws IllegalArgumentException;

	/**
	 * Returns the name of the charset used for the MIME body sent in this response. See RFC 2047 for more information
	 * about character encoding and MIME.
	 * 
	 * @return a String specifying the name of the charset, for example, ISO-8859-1
	 */
	String getCharacterEncoding();

	/**
	 * Returns a PrintWriter object that can send character text to the portal. Before calling this method the content
	 * type of the render response should be set using the setContentType(java.lang.String) method. Either this method
	 * or getOutputStream() may be called to write the body, not both.
	 * 
	 * @return a PrintWriter object that can return character data to the portal
	 * @throws IOException
	 *             if an input or output exception occurred
	 * @throws IllegalStateException
	 *             if the getPortletOutputStream method has been called on this response
	 */
	PrintWriter getWriter() throws IOException, IllegalStateException;

	/**
	 * Returns a OutputStream suitable for writing binary data in the response. The portlet container does not encode
	 * the binary data. Before calling this method the content type of the render response must be set using the
	 * setContentType(java.lang.String) method. Calling flush() on the OutputStream commits the response. Either this
	 * method or getWriter() may be called to write the body, not both.
	 * 
	 * @return a OutputStream for writing binary data
	 * @throws IOException
	 *             if an input or output exception occurred
	 * @throws IllegalStateException
	 *             if the getWriter method has been called on this response.
	 */
	OutputStream getOutputStream() throws IOException, IllegalStateException;

	/**
	 * Forces any content in the buffer to be written to the underlying output stream. A call to this method
	 * automatically commits the response.
	 * 
	 * @throws IOException
	 *             if an error occurred when writing the output
	 */
	void flushBuffer() throws IOException;

	/**
	 * Returns a boolean indicating if the response has been committed.
	 * 
	 * @return a boolean indicating if the response has been committed
	 */
	boolean isCommitted();

	/**
	 * Returns the locale assigned to the response.
	 * 
	 * @return Locale of this response
	 */
	Locale getLocale();

}
