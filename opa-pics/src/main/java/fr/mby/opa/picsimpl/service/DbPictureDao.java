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

package fr.mby.opa.picsimpl.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.eclipse.gemini.blueprint.context.BundleContextAware;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IPictureDao;
import fr.mby.opa.pics.service.PictureAlreadyExistsException;
import fr.mby.opa.pics.service.PictureNotFoundException;
import fr.mby.utils.common.jpa.OsgiJpaHelper;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class DbPictureDao implements IPictureDao, BundleContextAware, InitializingBean, DisposableBean {

	private static final String PICTURE_PU_NAME = "opaPicsPu";

	private BundleContext bundleContext;

	private EntityManagerFactory emf;

	@Override
	public Picture createPicture(final Picture picture) throws PictureAlreadyExistsException {
		Assert.notNull(picture, "No Picture supplied !");
		Assert.isNull(picture.getId(), "Id should not be set for creation !");

		EntityManager em = null;
		try {
			em = this.getNewEntityManager();

			this.testHashUniqueness(picture, em);

			em.getTransaction().begin();

			em.persist(picture);

			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen() && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			if (em != null) {
				em.close();
			}
		}

		return picture;
	}

	@Override
	public Picture updatePicture(final Picture picture) throws PictureNotFoundException {
		Assert.notNull(picture, "No Picture supplied !");
		Assert.notNull(picture.getId(), "Id should be set for update !");

		Picture updatedPicture = null;

		EntityManager em = null;
		try {
			em = this.getNewEntityManager();

			em.getTransaction().begin();

			final Picture managedPicture = em.find(Picture.class, picture.getId(), LockModeType.WRITE);
			if (managedPicture == null) {
				throw new PictureNotFoundException();
			}

			updatedPicture = em.merge(picture);

			em.lock(picture, LockModeType.NONE);

			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen() && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			if (em != null) {
				em.close();
			}
		}

		return updatedPicture;
	}

	@Override
	public void deletePicture(final Picture picture) throws PictureNotFoundException {
		Assert.notNull(picture, "No Picture supplied !");
		Assert.notNull(picture.getId(), "Id should be set for delete !");

		EntityManager em = null;
		try {
			em = this.getNewEntityManager();

			em.getTransaction().begin();

			final Picture managedPicture = em.find(Picture.class, picture.getId(), LockModeType.WRITE);
			if (managedPicture == null) {
				throw new PictureNotFoundException();
			}

			em.remove(picture);

			em.lock(picture, LockModeType.NONE);

			em.getTransaction().commit();
		} finally {
			if (em != null && em.isOpen() && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public Picture findPictureById(final Long id) {
		Assert.notNull(id, "Picture Id should be supplied !");

		Picture picture = null;

		EntityManager em = null;
		try {
			em = this.getNewEntityManager();

			final Query findByIdQuery = em.createNamedQuery(Picture.FIND_PIC_BY_ID);
			findByIdQuery.setParameter("id", id);
			final List<?> pictures = findByIdQuery.getResultList();
			if (!pictures.isEmpty()) {
				picture = (Picture) pictures.iterator().next();
			}
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return picture;
	}

	@Override
	public BinaryImage findImageById(final Long id) {
		Assert.notNull(id, "Image Id should be supplied !");

		BinaryImage image = null;

		EntityManager em = null;
		try {
			em = this.getNewEntityManager();

			image = em.find(BinaryImage.class, id);
		} finally {
			if (em != null) {
				em.close();
			}
		}

		return image;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Picture> findAllPictures() {
		Collection<Picture> pictures = null;

		EntityManager em = null;
		try {
			em = this.getNewEntityManager();

			final Query findAllQuery = em.createNamedQuery(Picture.FIND_ALL_ORDER_BY_DATE);
			pictures = findAllQuery.getResultList();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		if (pictures == null) {
			pictures = Collections.emptyList();
		}

		return pictures;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.emf = OsgiJpaHelper.retrieveEmfByName(this.bundleContext, DbPictureDao.PICTURE_PU_NAME);
	}

	@Override
	public void destroy() throws Exception {
		this.emf.close();
		this.emf = null;
	}

	@Override
	public void setBundleContext(final BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * Get a new EntityManager.
	 * 
	 * @return
	 */
	protected EntityManager getNewEntityManager() {
		this.emf.getCache().evictAll();
		return this.emf.createEntityManager();
	}

	/**
	 * Test if a picture is already presents in DB.
	 * 
	 * @param picture
	 * @param em
	 * @throws PictureAlreadyExistsException
	 */
	protected void testHashUniqueness(final Picture picture, final EntityManager em)
			throws PictureAlreadyExistsException {
		final Query findPicByHashQuery = em.createNamedQuery(Picture.FIND_PIC_ID_BY_HASH);
		findPicByHashQuery.setParameter("uniqueHash", picture.getUniqueHash());
		final List<?> results = findPicByHashQuery.getResultList();
		if (!results.isEmpty()) {
			// A picture with same hash was found
			throw new PictureAlreadyExistsException();
		}
	}

}
