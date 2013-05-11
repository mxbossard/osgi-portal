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

import javax.servlet.http.Cookie;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public interface IReply {

	/**
	 * Sets a String property to be returned to the portal. Response properties can be viewed as header values set for
	 * the portal application. If these header values are intended to be transmitted to the client they should be set
	 * before the response is committed. This method resets all properties previously added with the same key.
	 * 
	 * @param key
	 *            the key of the property to be returned to the portal
	 * @param value
	 *            the value of the property to be returned to the portal
	 * @throws IllegalArgumentException
	 *             if key is null.
	 */
	void setProperty(String key, String value) throws IllegalArgumentException;

	/**
	 * 
	 * Returns the encoded URL of the resource, like servlets, JSPs, images and other static files, at the given path.
	 * Portlets should encode all resource URLs pointing to resources in the portlet application via this method in
	 * order to ensure that they get served via the portal application. Some portal/portlet-container implementation may
	 * require those URLs to contain implementation specific data encoded in it. Because of that, portlets should use
	 * this method to create such URLs. The encodeURL method may include the session ID and other
	 * portal/portlet-container specific information into the URL. If encoding is not needed, it returns the URL
	 * unchanged. Portlet developer should be aware that the returned URL might not be a well formed URL but a special
	 * token at the time the portlet is generating its content. Thus portlets should not add additional parameters on
	 * the resulting URL or expect to be able to parse the URL. As a result, the outcome of the encodeURL call may be
	 * different than calling encodeURL in the servlet world.
	 * 
	 * @param path
	 *            the URI path to the resource. This must be either an absolute URL (e.g.
	 *            http://my.co/myportal/mywebap/myfolder/myresource.gif) or a full path URI (e.g.
	 *            /myportal/mywebap/myfolder/myresource.gif).
	 * @return the URI path to the resource. This must be either an absolute URL (e.g.
	 *         http://my.co/myportal/mywebap/myfolder/myresource.gif) or a full path URI (e.g.
	 *         /myportal/mywebap/myfolder/myresource.gif).
	 * @throws IllegalArgumentException
	 *             if path doesn't have a leading slash or is not an absolute URL
	 */
	String encodeURL(String path) throws IllegalArgumentException;

	/**
	 * Adds a HTTP Cookie property to the response. The portlet should note that the cookie may not make it to the
	 * client, but may be stored at the portal. This method allows response properties to have multiple cookies.
	 * 
	 * @param cookie
	 *            the cookie to be added to the response
	 * @throws IllegalArgumentException
	 *             if cookie is null
	 */
	void addProperty(Cookie cookie) throws IllegalArgumentException;

}
