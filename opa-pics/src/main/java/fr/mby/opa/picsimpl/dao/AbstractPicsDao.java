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

package fr.mby.opa.picsimpl.dao;

import javax.persistence.EntityManagerFactory;

import fr.mby.opa.pics.model.IPicsPersistenceUnit;
import fr.mby.utils.common.jpa.AbstractOsgiJpaDao;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public abstract class AbstractPicsDao extends AbstractOsgiJpaDao {

	@Override
	protected String getPersistenceUnitName() {
		return IPicsPersistenceUnit.PU_NAME;
	}

	@Override
	protected EntityManagerFactory getEmf() {
		final EntityManagerFactory emf = super.getEmf();

		// emf.getCache().evictAll();

		return emf;
	}

}
