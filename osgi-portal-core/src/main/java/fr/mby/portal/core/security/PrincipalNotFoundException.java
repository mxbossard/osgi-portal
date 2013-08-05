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

package fr.mby.portal.core.security;

import java.security.Principal;

import fr.mby.portal.core.acl.AbstractPortalException;

/**
 * Thrown in case of search against a non existing Principal.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public class PrincipalNotFoundException extends AbstractPortalException {

	/** Svuid. */
	private static final long serialVersionUID = 5290104026847510729L;

	private final Principal principal;

	/**
	 * @param role
	 */
	public PrincipalNotFoundException(final Principal principal) {
		super(String.format("Principal: [%1$s] does not exists !", principal.toString()));
		this.principal = principal;
	}

	/**
	 * Getter of principal.
	 * 
	 * @return the principal
	 */
	public Principal getPrincipal() {
		return this.principal;
	}

}
