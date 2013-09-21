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

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.google.common.collect.Iterables;

import fr.mby.opa.pics.model.Album;
import fr.mby.opa.pics.model.BinaryImage;
import fr.mby.opa.pics.model.Picture;
import fr.mby.opa.pics.service.IPictureDao;
import fr.mby.opa.pics.service.PictureAlreadyExistsException;
import fr.mby.opa.pics.service.PictureNotFoundException;
import fr.mby.utils.common.jpa.EmCallback;
import fr.mby.utils.common.jpa.TxCallback;
import fr.mby.utils.common.jpa.TxCallbackReturn;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Repository
public class DbPictureDao extends AbstractPicsDao implements IPictureDao {

	/** Page size used for pagination. */
	protected static final int PAGINATION_SIZE = 50;

	@Override
	public Picture createPicture(final Picture picture, final Album album) throws PictureAlreadyExistsException {
		Assert.notNull(picture, "No Picture supplied !");
		Assert.notNull(album, "No Album supplied !");
		Assert.isNull(picture.getId(), "Id should not be set for creation !");

		picture.setAlbum(album);

		new TxCallback(this.getEmf()) {

			@Override
			protected void executeInTransaction(final EntityManager em) {
				DbPictureDao.this.testHashUniqueness(picture, em);
				em.persist(picture);
			}
		};

		return picture;
	}

	@Override
	public Picture updatePicture(final Picture picture) throws PictureNotFoundException {
		Assert.notNull(picture, "No Picture supplied !");
		Assert.notNull(picture.getId(), "Id should be set for update !");

		final TxCallbackReturn<Picture> txCallback = new TxCallbackReturn<Picture>(this.getEmf()) {

			@Override
			protected Picture executeInTransaction(final EntityManager em) {
				final Picture updatedPicture = em.merge(picture);
				return updatedPicture;
			}
		};

		return txCallback.getReturnedValue();
	}

	@Override
	public void deletePicture(final Picture picture) throws PictureNotFoundException {
		Assert.notNull(picture, "No Picture supplied !");
		Assert.notNull(picture.getId(), "Id should be set for delete !");

		new TxCallback(this.getEmf()) {

			@Override
			protected void executeInTransaction(final EntityManager em) {
				final Picture managedPicture = em.find(Picture.class, picture.getId(), LockModeType.WRITE);
				if (managedPicture == null) {
					throw new PictureNotFoundException();
				}

				em.remove(picture);
				em.lock(picture, LockModeType.NONE);
			}
		};
	}

	@Override
	public List<Picture> findPicturesByAlbumId(final Long albumId, final Long since) {
		Assert.notNull(albumId, "Album Id should be supplied !");
		final Timestamp sinceTimstamp = new Timestamp((since != null) ? since : 0L);

		final EmCallback<List<Picture>> emCallback = new EmCallback<List<Picture>>(this.getEmf()) {

			@Override
			@SuppressWarnings("unchecked")
			protected List<Picture> executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findByAlbumQuery = em.createNamedQuery(Picture.FIND_PICTURE_BY_ALBUM_ORDER_BY_DATE);
				findByAlbumQuery.setParameter("albumId", albumId);
				findByAlbumQuery.setParameter("since", sinceTimstamp);
				findByAlbumQuery.setMaxResults(DbPictureDao.PAGINATION_SIZE);

				final List<Picture> pictureId = findByAlbumQuery.getResultList();
				return pictureId;
			}
		};

		List<Picture> pictures = emCallback.getReturnedValue();
		if (pictures == null) {
			pictures = Collections.emptyList();
		}

		return pictures;
	}

	@Override
	public Picture findPictureById(final Long id) {
		Assert.notNull(id, "Picture Id should be supplied !");

		final EmCallback<Picture> emCallback = new EmCallback<Picture>(this.getEmf()) {

			@Override
			protected Picture executeWithEntityManager(final EntityManager em) throws PersistenceException {
				return em.find(Picture.class, id);
			}
		};

		return emCallback.getReturnedValue();
	}

	@Override
	public Long findPictureIdByHash(final String hash) {
		Assert.hasText(hash, "Picture hash should be supplied !");

		final EmCallback<Long> emCallback = new EmCallback<Long>(this.getEmf()) {

			@Override
			@SuppressWarnings("unchecked")
			protected Long executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findByHashQuery = em.createNamedQuery(Picture.FIND_PICTURE_ID_BY_HASH);
				findByHashQuery.setParameter("hash", hash);

				final Long pictureId = Iterables.getFirst(findByHashQuery.getResultList(), null);
				return pictureId;
			}
		};

		return emCallback.getReturnedValue();
	}

	@Override
	public Picture loadFullPictureById(final Long id) {
		Assert.notNull(id, "Picture Id should be supplied !");

		final EmCallback<Picture> emCallback = new EmCallback<Picture>(this.getEmf()) {

			@Override
			@SuppressWarnings("unchecked")
			protected Picture executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findByIdQuery = em.createNamedQuery(Picture.LOAD_FULL_PICTURE_BY_ID);
				findByIdQuery.setParameter("id", id);

				final Picture picture = Iterables.getFirst(findByIdQuery.getResultList(), null);
				return picture;
			}
		};

		return emCallback.getReturnedValue();
	}

	@Override
	public BinaryImage findImageById(final Long id) {
		Assert.notNull(id, "Image Id should be supplied !");

		final EmCallback<BinaryImage> emCallback = new EmCallback<BinaryImage>(this.getEmf()) {

			@Override
			protected BinaryImage executeWithEntityManager(final EntityManager em) throws PersistenceException {
				return em.find(BinaryImage.class, id);
			}
		};

		return emCallback.getReturnedValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Picture> findAllPictures(final Long since) {
		final Timestamp sinceTimstamp = new Timestamp((since != null) ? since : 0L);

		final EmCallback<List<Picture>> emCallback = new EmCallback<List<Picture>>(this.getEmf()) {

			@Override
			protected List<Picture> executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findAllQuery = em.createNamedQuery(Picture.FIND_ALL_PICTURES_ORDER_BY_DATE);
				findAllQuery.setParameter("since", sinceTimstamp);
				findAllQuery.setMaxResults(DbPictureDao.PAGINATION_SIZE);

				return findAllQuery.getResultList();
			}
		};

		List<Picture> pictures = emCallback.getReturnedValue();

		if (pictures == null) {
			pictures = Collections.emptyList();
		}

		return pictures;
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
		final Query findPicByHashQuery = em.createNamedQuery(Picture.FIND_PICTURE_ID_BY_HASH);
		findPicByHashQuery.setParameter("hash", picture.getHash());
		final List<?> results = findPicByHashQuery.getResultList();
		if (!results.isEmpty()) {
			// A picture with same hash was found
			throw new PictureAlreadyExistsException(picture.getFilename());
		}
	}

}
