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

package fr.mby.opa.pics.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class UplaodServlet implements Servlet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(final ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#getServletConfig()
	 */
	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void service(final ServletRequest req, final ServletResponse res) throws ServletException, IOException {
		if (req instanceof HttpServletRequest) {
			final HttpServletRequest request = (HttpServletRequest) req;

			final DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			fileItemFactory.setSizeThreshold(1_024_000_000);
			final ServletFileUpload sfu = new ServletFileUpload(fileItemFactory);

			try {
				final List<FileItem> fileItems = sfu.parseRequest(request);
				if (fileItems != null) {
					for (final FileItem fileItem : fileItems) {
						final InputStream stream = fileItem.getInputStream();
					}
				}
			} catch (final FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#getServletInfo()
	 */
	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
