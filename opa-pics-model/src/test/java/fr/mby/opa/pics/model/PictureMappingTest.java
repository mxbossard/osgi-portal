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

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class PictureMappingTest {

	private static final String PU_NAME = "test-opaPicsPu";

	@PersistenceUnit(unitName = PictureMappingTest.PU_NAME)
	private static EntityManagerFactory emf;

	@BeforeClass
	public static void initTestFixture() throws Exception {
		// Get the entity manager for the tests.
		PictureMappingTest.emf = Persistence.createEntityManagerFactory(PictureMappingTest.PU_NAME);
	}

	@Test
	public void testPersistPicture() throws Exception {

		final Album album = new Album();
		album.setName("Album");
		album.setCreationTime(new DateTime());

		final Picture picture = this.buildPicture(album);

		final EntityManager em1 = PictureMappingTest.emf.createEntityManager();
		em1.getTransaction().begin();

		em1.persist(album);

		em1.persist(picture);

		em1.getTransaction().commit();

		em1.close();

		Assert.assertNotNull("Picture Id should be set !", picture.getId());
		Assert.assertNotNull("Picture Id should be set !", picture.getImage().getId());
		Assert.assertNotNull("Picture Id should be set !", picture.getThumbnail().getId());

		final EntityManager em2 = PictureMappingTest.emf.createEntityManager();

		PictureMappingTest.emf.getCache().evictAll();

		// final Picture foundPicture = em2.find(Picture.class, picture.getId());
		// Assert.assertNotNull("Found picture should not be null !", foundPicture);

		PictureMappingTest.emf.getCache().evictAll();

		final BinaryImage foundContents = em2.find(BinaryImage.class, picture.getImage().getId());
		Assert.assertNotNull("Found contents should not be null !", foundContents);
	}

	@Test
	public void testPersistProposals() throws Exception {
		final DateTime now = new DateTime();

		final Album a1 = new Album();
		a1.setCreationTime(now);
		a1.setName("a1");

		final ProposalBag pb1 = new ProposalBag();
		pb1.setCreationTime(now);
		pb1.setName("op1");

		final Session s1 = new Session();
		s1.setName("s1");

		final Picture p1 = this.buildPicture(a1);

		final CasingProposal up1 = new CasingProposal();
		up1.setCreationTime(now);
		up1.setSession(s1);
		up1.setPicture(p1);
		up1.setProposalBag(pb1);

		final EntityManager em1 = PictureMappingTest.emf.createEntityManager();
		em1.getTransaction().begin();

		em1.persist(a1);

		em1.persist(up1);

		em1.getTransaction().commit();

		em1.close();
	}

	protected Picture buildPicture(final Album album) {
		final Picture picture = new Picture();

		final Timestamp now = new Timestamp(System.currentTimeMillis());

		picture.setCreationTime(now);
		picture.setFilename("filename");
		picture.setFormat("jpg");
		picture.setHeight(200);
		picture.setName("name");
		picture.setThumbnailFormat("jpg");
		picture.setThumbnailHeight(50);
		picture.setThumbnailWidth(100);
		picture.setHash(String.valueOf(now.getTime()));
		picture.setWidth(300);

		final BinaryImage image = new BinaryImage();
		final byte[] picData = {0x00, 0x01, 0x02};
		image.setData(picData);
		image.setFormat("jpg");
		image.setHeight(200);
		image.setWidth(300);
		image.setFilename("filename");
		picture.setSize(picData.length);

		final BinaryImage thumbnail = new BinaryImage();
		final byte[] thumbnailData = {0x10, 0x11, 0x12};
		thumbnail.setData(thumbnailData);
		thumbnail.setFormat("jpg");
		thumbnail.setHeight(50);
		thumbnail.setWidth(100);
		thumbnail.setFilename("filename");
		picture.setThumbnailSize(thumbnailData.length);

		picture.setImage(image);

		picture.setThumbnail(thumbnail);

		picture.setAlbum(album);

		return picture;
	}
}
