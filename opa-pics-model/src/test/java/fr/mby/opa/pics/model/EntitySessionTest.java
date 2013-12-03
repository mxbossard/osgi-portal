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
import java.util.Objects;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class EntitySessionTest extends AbstractEntityTest<Session> {

	@Override
	Session buildEntity() {
		final Session newSession = new Session();

		newSession.setName("SessionName");
		newSession.setCreationTime(new Timestamp(System.currentTimeMillis()));

		return newSession;
	}

	@Override
	void updateEntity(final Session entity) {
		entity.setName("SessionName updated");
		entity.setDescription("Session description");
		entity.setStartTime(new Timestamp(System.currentTimeMillis()));
		entity.setEndTime(new Timestamp(System.currentTimeMillis()));
	}

	@Override
	Object getEntityId(final Session entity) {
		return entity.getId();
	}

	@Override
	boolean areEquals(final Session entity1, final Session entity2) {
		return Objects.equals(entity1.getCreationTime(), entity2.getCreationTime())
				&& Objects.equals(entity1.getStartTime(), entity2.getStartTime())
				&& Objects.equals(entity1.getEndTime(), entity2.getEndTime())
				&& Objects.equals(entity1.getId(), entity2.getId())
				&& Objects.equals(entity1.getName(), entity2.getName())
				&& Objects.equals(entity1.getDescription(), entity2.getDescription());
	}

}
