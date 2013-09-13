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

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.DateTime;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class JodaDateTimeConverter implements Converter {

	/** Svuid. */
	private static final long serialVersionUID = -6865762913757496067L;

	@Override
	public Object convertDataValueToObjectValue(final Object obj, final Session session) {
		Object result = null;

		if (obj != null) {
			if (obj instanceof String) {
				result = new DateTime(Timestamp.valueOf(((String) obj)).getTime());
			} else if (obj instanceof Timestamp) {
				result = new DateTime(((Timestamp) obj).getTime());
			}
		}

		return result;
	}

	@Override
	public Object convertObjectValueToDataValue(final Object obj, final Session session) {
		Object result = null;

		if (obj != null) {
			result = new Timestamp(((DateTime) obj).getMillis());
		}

		return result;
	}

	@Override
	public void initialize(final DatabaseMapping obj, final Session session) {
		// Nothhig to do
	}

	@Override
	public boolean isMutable() {
		return false;
	}

}