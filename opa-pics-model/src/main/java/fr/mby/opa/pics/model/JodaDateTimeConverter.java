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

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Converter
public class JodaDateTimeConverter implements AttributeConverter<ReadableDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(final ReadableDateTime attribute) {
		return new Timestamp(attribute.getMillis());
	}

	@Override
	public ReadableDateTime convertToEntityAttribute(final Timestamp dbData) {
		return new DateTime(dbData.getTime());
	}

}