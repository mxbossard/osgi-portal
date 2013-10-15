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

import java.util.Collection;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.google.common.collect.Iterables;

import fr.mby.opa.pics.dao.IAlbumDao;
import fr.mby.opa.pics.exception.AlbumNotFoundException;
import fr.mby.opa.pics.exception.PictureNotFoundException;
import fr.mby.opa.pics.model.Album;
import fr.mby.utils.common.jpa.EmCallback;
import fr.mby.utils.common.jpa.TxCallback;
import fr.mby.utils.common.jpa.TxCallbackReturn;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Repository
public class DbAlbumDao extends AbstractPicsDao implements IAlbumDao {

	@Override
	public Album createAlbum(final Album album) {
		Assert.notNull(album, "No Album supplied !");
		Assert.isNull(album.getId(), "Id should not be set for creation !");

		new TxCallback(this.getEmf()) {

			@Override
			protected void executeInTransaction(final EntityManager em) {
				em.persist(album);
			}
		};

		return album;
	}

	@Override
	public Album updateAlbum(final Album album) throws AlbumNotFoundException {
		Assert.notNull(album, "No Album supplied !");
		Assert.notNull(album.getId(), "Id should be set for update !");

		final TxCallbackReturn<Album> txCallback = new TxCallbackReturn<Album>(this.getEmf()) {

			@Override
			protected Album executeInTransaction(final EntityManager em) {
				final Album managedAlbum = em.find(Album.class, album.getId(), LockModeType.WRITE);
				if (managedAlbum == null) {
					throw new AlbumNotFoundException();
				}

				Album updatedAlbum = null;

				if (!managedAlbum.getLocked()) {
					// Update album if not locked !
					managedAlbum.setName(album.getName());
					managedAlbum.setDescription(album.getDescription());

					updatedAlbum = em.merge(managedAlbum);
				}

				return updatedAlbum;
			}
		};

		return txCallback.getReturnedValue();
	}

	@Override
	public void deleteAlbum(final Album album) throws AlbumNotFoundException {
		Assert.notNull(album, "No Album supplied !");
		Assert.notNull(album.getId(), "Id should be set for delete !");

		new TxCallback(this.getEmf()) {

			@Override
			protected void executeInTransaction(final EntityManager em) {
				final Album managedAlbum = em.find(Album.class, album.getId(), LockModeType.WRITE);
				if (managedAlbum == null) {
					throw new PictureNotFoundException();
				}

				em.remove(managedAlbum);
			}
		};
	}

	@Override
	public Album findAlbumById(final Long id) {
		Assert.notNull(id, "Album Id should be supplied !");

		final EmCallback<Album> emCallback = new EmCallback<Album>(this.getEmf()) {

			@Override
			protected Album executeWithEntityManager(final EntityManager em) throws PersistenceException {
				return em.find(Album.class, id);
			}
		};

		return emCallback.getReturnedValue();
	}

	@Override
	public Album loadAlbumById(final Long id) {
		Assert.notNull(id, "Album Id should be supplied !");

		final EmCallback<Album> emCallback = new EmCallback<Album>(this.getEmf()) {

			@Override
			@SuppressWarnings("unchecked")
			protected Album executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findByIdQuery = em.createNamedQuery(Album.LOAD_FULL_ALBUM_BY_ID);
				findByIdQuery.setParameter("id", id);

				final Album album = Iterables.getFirst(findByIdQuery.getResultList(), null);
				return album;
			}
		};

		return emCallback.getReturnedValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Album> findAllAlbums() {
		final EmCallback<Collection<Album>> emCallback = new EmCallback<Collection<Album>>(this.getEmf()) {

			@Override
			protected Collection<Album> executeWithEntityManager(final EntityManager em) throws PersistenceException {
				final Query findAllQuery = em.createNamedQuery(Album.FIND_ALL_ALBUMS_ORDER_BY_DATE);
				return findAllQuery.getResultList();
			}
		};

		Collection<Album> albums = emCallback.getReturnedValue();

		if (albums == null) {
			albums = Collections.emptyList();
		}

		return albums;
	}

}
