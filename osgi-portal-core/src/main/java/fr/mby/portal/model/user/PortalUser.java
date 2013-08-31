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

package fr.mby.portal.model.user;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@NamedQueries({@NamedQuery(name = PortalUser.FIND_PORTAL_USER, query = "SELECT record"
		+ " FROM PortalUser record WHERE record.login = :login")})
@Entity
@Table(name = "portal_user", uniqueConstraints = @UniqueConstraint(columnNames = {"login"}), indexes = {
		@Index(columnList = "id"), @Index(columnList = "login")})
public class PortalUser {

	public static final String FIND_PORTAL_USER = "FIND_PORTAL_USER";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;

	@Column(name = "login")
	@Basic(fetch = FetchType.EAGER, optional = false)
	private String login;

	@Column(name = "password")
	@Basic(fetch = FetchType.EAGER, optional = false)
	private String password;

	/**
	 * Getter of id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * Getter of login.
	 * 
	 * @return the login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Setter of login.
	 * 
	 * @param login
	 *            the login to set
	 */
	public void setLogin(final String login) {
		this.login = login;
	}

	/**
	 * Getter of password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Setter of password.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

}
