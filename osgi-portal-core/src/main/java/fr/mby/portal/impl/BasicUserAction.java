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
package fr.mby.portal.impl;

import java.security.Principal;
import java.util.Enumeration;
import java.util.Map;

import fr.mby.portal.IPortalContext;
import fr.mby.portal.IUserAction;

/**
 * @author Maxime Bossard - 2013
 *
 */
public class BasicUserAction implements IUserAction {

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getPortalContext()
	 */
	@Override
	public IPortalContext getPortalContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getProperties(java.lang.String)
	 */
	@Override
	public Enumeration<String> getProperties(String name)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getPropertyNames()
	 */
	@Override
	public Enumeration<String> getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getParameterValues(java.lang.String)
	 */
	@Override
	public String[] getParameterValues(String name)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getParameterNames()
	 */
	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getParameterMap()
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#getAttributeNames()
	 */
	@Override
	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#setAttribute(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setAttribute(String name, Object value)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see fr.mby.portal.IUserAction#removeAttribute(java.lang.String)
	 */
	@Override
	public Object removeAttribute(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

}
