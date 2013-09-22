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

package fr.mby.opa.pics.web.jquery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Maxime Bossard - 2013
 * 
 */
// ignore "bytes" when return json format
@JsonIgnoreProperties({"bytes"})
public class FileMeta {

	private String error;

	private String fileName;
	private String fileSize;
	private String fileType;

	private String url;
	private String thumbnailUrl;

	private byte[] bytes;

	/**
	 * Getter of fileName.
	 * 
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * Getter of error.
	 * 
	 * @return the error
	 */
	public String getError() {
		return this.error;
	}

	/**
	 * Setter of error.
	 * 
	 * @param error
	 *            the error to set
	 */
	public void setError(final String error) {
		this.error = error;
	}

	/**
	 * Setter of fileName.
	 * 
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Getter of fileSize.
	 * 
	 * @return the fileSize
	 */
	public String getFileSize() {
		return this.fileSize;
	}

	/**
	 * Setter of fileSize.
	 * 
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(final String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * Getter of fileType.
	 * 
	 * @return the fileType
	 */
	public String getFileType() {
		return this.fileType;
	}

	/**
	 * Setter of fileType.
	 * 
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(final String fileType) {
		this.fileType = fileType;
	}

	/**
	 * Getter of bytes.
	 * 
	 * @return the bytes
	 */
	public byte[] getBytes() {
		return this.bytes;
	}

	/**
	 * Setter of bytes.
	 * 
	 * @param bytes
	 *            the bytes to set
	 */
	public void setBytes(final byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * Getter of url.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Setter of url.
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * Getter of thumbnailUrl.
	 * 
	 * @return the thumbnailUrl
	 */
	public String getThumbnailUrl() {
		return this.thumbnailUrl;
	}

	/**
	 * Setter of thumbnailUrl.
	 * 
	 * @param thumbnailUrl
	 *            the thumbnailUrl to set
	 */
	public void setThumbnailUrl(final String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

}
