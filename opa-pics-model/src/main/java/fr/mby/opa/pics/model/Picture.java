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

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.joda.time.ReadableDateTime;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Entity
@Table(name = "picture")
public class Picture {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Basic(optional = false)
	@Column(name = "uniqueHash")
	private String uniqueHash;

	@Basic(optional = false)
	@Column(name = "name")
	private String name;

	@Basic(optional = false)
	@Column(name = "creationTime", columnDefinition = "TIMESTAMP")
	@Convert(converter = JodaDateTimeConverter.class)
	private ReadableDateTime creationTime;

	@Lob
	@Basic(optional = false, fetch = FetchType.LAZY)
	@Column(name = "picture")
	private byte[] picture;

	@Lob
	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "thumbnail")
	private byte[] thumbnail;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tagsOnPicture")
	private Collection<Tag> tags;

}
