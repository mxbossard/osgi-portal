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

package fr.mby.opa.pics.service;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class UnsupportedPictureTypeException extends Exception {

	/** Svuid. */
	private static final long serialVersionUID = -222975810586277980L;

	private final String filename;

	/**
	 * @param filename
	 */
	public UnsupportedPictureTypeException(final String filename) {
		super();
		this.filename = filename;
	}

	/**
	 * Getter of filename.
	 * 
	 * @return the filename
	 */
	public String getFilename() {
		return this.filename;
	}

}
