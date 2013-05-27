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

package fr.mby.portal.coreimpl;

import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.springframework.util.StringUtils;

import fr.mby.portal.api.app.IPortalApp;
import fr.mby.portal.core.IPortalRenderer;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class BasicPortalRenderer implements IPortalRenderer {

	public static final Object WEB_CONTEXT_PATH_BUNDLE_HEADER = "Web-ContextPath";

	/** All IAppReferences. */
	private Collection<ServiceReference<IPortalApp>> portalAppReferences;

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		final PrintWriter writer = response.getWriter();

		writer.append("<!doctype html>\n<html>\n<head>\n</head>\n<body>\n");
		writer.append("<h1>OSGi Portal !</h1>\n");

		if (this.portalAppReferences != null) {
			for (final ServiceReference<IPortalApp> appRef : this.portalAppReferences) {
				final Bundle bundle = appRef.getBundle();

				this.renderWebAppBundle(writer, bundle);
			}
		}

		writer.append("</body>\n</html>\n");
	}

	/**
	 * @param writer
	 * @param bundle
	 */
	protected void renderWebAppBundle(final PrintWriter writer, final Bundle bundle) {
		final String webBundlePath = this.buildWebAppBundlePath(bundle);

		if (StringUtils.hasText(webBundlePath)) {
			final long bundleId = bundle.getBundleId();
			writer.append("<div id=\"bundle");
			writer.append(String.valueOf(bundleId));
			writer.append("\">\n");

			final String symbolicName = bundle.getSymbolicName();
			writer.append("<h2>");
			writer.append(symbolicName);
			writer.append("</h2>\n");

			writer.append("<iframe src=\"");
			writer.append(webBundlePath);
			writer.append("\" />\n");

			writer.append("</div>\n");
		}
	}

	/**
	 * @param webContextPathHeader
	 * @return
	 */
	protected String buildWebAppBundlePath(final Bundle bundle) {
		final String webContextPathHeader = bundle.getHeaders().get(BasicPortalRenderer.WEB_CONTEXT_PATH_BUNDLE_HEADER);
		final String webBundlePath = "/".concat(webContextPathHeader.replaceAll("[\\/\\\\]+", ""));

		return webBundlePath;
	}

	/**
	 * Getter of portalAppReferences.
	 * 
	 * @return the portalAppReferences
	 */
	public Collection<ServiceReference<IPortalApp>> getPortalAppReferences() {
		return this.portalAppReferences;
	}

	/**
	 * Setter of portalAppReferences.
	 * 
	 * @param portalAppReferences
	 *            the portalAppReferences to set
	 */
	public void setPortalAppReferences(final Collection<ServiceReference<IPortalApp>> portalAppReferences) {
		this.portalAppReferences = portalAppReferences;
	}

}
