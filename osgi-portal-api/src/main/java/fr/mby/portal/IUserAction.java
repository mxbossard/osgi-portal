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
package fr.mby.portal;

import java.security.Principal;
import java.util.Enumeration;
import java.util.Map;

/**
 * Represents an action sent by the client to the portal.
 * 
 * @author Maxime Bossard - 2013
 *
 */
public interface IUserAction {

	/**
	 * Returns the context of the calling portal.
	 * 
	 * @return the context of the calling portal
	 */
	IPortalContext getPortalContext();
	
	/**
	 * Returns a java.security.Principal object containing the name of the current authenticated user.
	 * 
	 * @return a java.security.Principal containing the name of the user making this request, or null if the user has not been authenticated.
	 */
	Principal getUserPrincipal();
	
	/**
	 * Returns the value of the specified request property as a String. If the request did not include a property of the specified name, this method returns null.
	 * A portlet can access portal/portlet-container specific properties through this method and, if available, the headers of the HTTP client request.
	 * This method should only be used if the property has only one value. If the property might have more than one value, use getProperties(java.lang.String).
	 * If this method is used with a multivalued parameter, the value returned is equal to the first value in the Enumeration returned by getProperties.
	 * 
	 * @param name a String specifying the property name
	 * @return a String containing the value of the requested property, or null if the request does not have a property of that name.
	 * @throws IllegalArgumentException - if name is null.
	 */
	String getProperty(String name) throws IllegalArgumentException;
	
	/**
	 * Returns all the values of the specified request property as a Enumeration of String objects.
	 * If the request did not include any properties of the specified name, this method returns an empty Enumeration. The property name is case insensitive. You can use this method with any request property.
	 * 
	 * @param name a String specifying the property name
	 * @return an Enumeration containing the values of the requested property. If the request does not have any properties of that name return an empty Enumeration.
	 * @throws IllegalArgumentException - if name is null.
	 */
	Enumeration<java.lang.String> getProperties(String name) throws IllegalArgumentException;
	
	/**
	 * Returns a Enumeration of all the property names this request contains. If the request has no properties, this method returns an empty Enumeration.
	 * 
	 * @return an Enumeration of all the property names sent with this request; if the request has no properties, an empty Enumeration.
	 */
	Enumeration<java.lang.String> getPropertyNames();

	/**
	 * Returns the value of a request parameter as a String, or null if the parameter does not exist. Request parameters are extra information sent with the request. The returned parameter are "x-www-form-urlencoded" decoded.
	 * Only parameters targeted to the current portlet are accessible.
	 * This method should only be used if the parameter has only one value. If the parameter might have more than one value, use getParameterValues(java.lang.String).
	 * If this method is used with a multivalued parameter, the value returned is equal to the first value in the array returned by getParameterValues.
	 * 
	 * @param name a String specifying the name of the parameter
	 * @return a String representing the single value of the parameter
	 * @throws IllegalArgumentException - if name is null.
	 */
	String getParameter(String name) throws IllegalArgumentException;
	
	/**
	 * Returns an array of String objects containing all of the values the given request parameter has, or null if the parameter does not exist. The returned parameters are "x-www-form-urlencoded" decoded.
	 * If the parameter has a single value, the array has a length of 1.
	 * 
	 * @param name a String specifying the name of the parameter
	 * @return an array of String objects containing the parameter values.
	 * @throws IllegalArgumentException - if name is null.
	 */
	String[] getParameterValues(String name) throws IllegalArgumentException;
	
	/**
	 * Returns an Enumeration of String objects containing the names of the parameters contained in this request. If the request has no parameters, the method returns an empty Enumeration.
	 * Only parameters targeted to the current portlet are returned.
	 * 
	 * @return an Enumeration of String objects, each String containing the name of a request parameter; or an empty Enumeration if the request has no parameters.
	 */
	Enumeration<java.lang.String> getParameterNames();
	
	/**
	 * Returns a Map of the parameters of this request. Request parameters are extra information sent with the request. The returned parameters are "x-www-form-urlencoded" decoded.
	 * The values in the returned Map are from type String array (String[]).
	 * If no parameters exist this method returns an empty Map.
	 * 
	 * @return an immutable Map containing parameter names as keys and parameter values as map values, or an empty Map if no parameters exist. The keys in the parameter map are of type String. The values in the parameter map are of type String array (String[]).
	 */
	Map<String, String[]> getParameterMap();
	
	/**
	 * Returns the value of the named attribute as an Object, or null if no attribute of the given name exists.
	 * Attribute names should follow the same conventions as package names. This specification reserves names matching java.*, and javax.*.
	 * In a distributed portlet web application the Object needs to be serializable.
	 * 
	 * @param name a String specifying the name of the attribute
	 * @returnan Object containing the value of the attribute, or null if the attribute does not exist.
	 * @throws IllegalArgumentException - if name is null.
	 */
	Object getAttribute(String name) throws IllegalArgumentException;

	/**
	 * Returns an Enumeration containing the names of the attributes available to this request. This method returns an empty Enumeration if the request has no attributes available to it.
	 * 
	 * @return an Enumeration of strings containing the names of the request attributes, or an empty Enumeration if the request has no attributes available to it.
	 */
	Enumeration<java.lang.String> getAttributeNames();

	/**
	 * Stores an attribute in this request.
	 * Attribute names should follow the same conventions as package names. Names beginning with java.*, javax.*, and com.sun.* are reserved. 
	 * 
	 * @param name a String specifying the name of the attribute
	 * @param value the Object to be stored
	 * @throws IllegalArgumentException - if name or value is null.
	 */
	void setAttribute(String name, Object value) throws IllegalArgumentException;
	
	/**
	 * Removes an attribute from this request. This method is not generally needed, as attributes only persist as long as the request is being handled.
	 * Attribute names should follow the same conventions as package names. Names beginning with java.*, javax.*, and com.sun.* are reserved.
	 * 
	 * @param name a String specifying the name of the attribute
	 * @return the value of the removed attribute
	 * @throws IllegalArgumentException - if name is null.
	 */
	Object removeAttribute(String name) throws IllegalArgumentException;
	
}
