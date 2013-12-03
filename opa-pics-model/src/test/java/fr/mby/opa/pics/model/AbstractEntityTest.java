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

package fr.mby.opa.pics.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import fr.mby.utils.common.jpa.TxCallback;

/**
 * Abstract Unit Test to perform basic EM operations on entity.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(BlockJUnit4ClassRunner.class)
public abstract class AbstractEntityTest<T> {

	protected static final String PU_NAME = "test-opaPicsPu";

	protected static EntityManagerFactory emf;

	@BeforeClass
	public static void initTestFixture() throws Exception {
		// Get the entity manager for the tests.
		AbstractEntityTest.emf = Persistence.createEntityManagerFactory(AbstractEntityTest.PU_NAME);
	}

	abstract T buildEntity();

	abstract void updateEntity(T entity);

	abstract Object getEntityId(T entity);

	abstract boolean areEquals(T entity1, T entity2);

	protected boolean _areEquals(final T entity1, final T entity2) {
		return entity1 == null && entity2 == null || entity1 != null && entity2 != null
				&& this.areEquals(entity1, entity2);
	}

	@Test
	public void testPersist() throws Exception {

		final T newEntity = this.buildEntity();

		new TxCallback(AbstractEntityTest.emf) {

			@Override
			protected void executeInTransaction(final EntityManager em) throws PersistenceException {
				em.persist(newEntity);
				em.flush();

				Assert.assertNotNull("Created entity should have an Id !",
						AbstractEntityTest.this.getEntityId(newEntity));

				final Object id = AbstractEntityTest.this.getEntityId(newEntity);
				@SuppressWarnings("unchecked")
				final T foundEntity = (T) em.find(newEntity.getClass(), id);
				Assert.assertNotNull("Created entity should be found !", foundEntity);

				Assert.assertTrue("Found entity should be equals to created entity !",
						AbstractEntityTest.this.areEquals(foundEntity, newEntity));
			}
		};

	}

	@Test
	public void testUpdate() throws Exception {

		final T newEntity = this.buildEntity();

		new TxCallback(AbstractEntityTest.emf) {

			@Override
			protected void executeInTransaction(final EntityManager em) throws PersistenceException {
				em.persist(newEntity);
				em.flush();

				AbstractEntityTest.this.updateEntity(newEntity);

				final T mergedEntity = em.merge(newEntity);
				Assert.assertNotNull("Merged entity should not be null !", mergedEntity);

				Assert.assertTrue("Merged entity shoud be equal to updated entity !",
						AbstractEntityTest.this.areEquals(newEntity, mergedEntity));

				final Object id = AbstractEntityTest.this.getEntityId(newEntity);
				@SuppressWarnings("unchecked")
				final T foundEntity = (T) em.find(newEntity.getClass(), id);
				Assert.assertNotNull("Created entity should be found !", foundEntity);

				Assert.assertTrue("Found entity should be equals to merged entity !",
						AbstractEntityTest.this.areEquals(foundEntity, mergedEntity));
			}
		};

	}

	@Test
	public void testDelete() throws Exception {

		final T newEntity = this.buildEntity();

		new TxCallback(AbstractEntityTest.emf) {

			@Override
			protected void executeInTransaction(final EntityManager em) throws PersistenceException {
				em.persist(newEntity);
				em.flush();

				final Object id = AbstractEntityTest.this.getEntityId(newEntity);
				final Object foundEntity = em.find(newEntity.getClass(), id);
				Assert.assertNotNull("Created entity should be found !", foundEntity);

				em.remove(newEntity);
				em.flush();

				final Object foundEntity2 = em.find(newEntity.getClass(), id);
				Assert.assertNull("Removed entity should not be found !", foundEntity2);
			}
		};

	}

}
