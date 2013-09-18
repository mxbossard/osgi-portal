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

package fr.mby.opa.web.servlet.tag;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import fr.mby.opa.web.servlet.request.OsgiPortalAppRequest;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class OpaMetaTag extends TagSupport {

	/** Svuid. */
	private static final long serialVersionUID = 4641048283845439719L;

	/** OPA signature placeholder. */
	private static final String OPA_SIGNATURE_PH = "{opaSignature}";

	private static final String OPA_REGISTERING_JS = "<script type='text/javascript'>\n" + "\t\tvar opaSignature = '"
			+ OpaMetaTag.OPA_SIGNATURE_PH + "';\n" + "\t\tvar osgiPortal = window.parent.OsgiPortal.getInstance();\n"
			+ "\t\twindow.appClient = osgiPortal.registerPortalApplication(opaSignature);\n" + "\t</script>\n";

	@Override
	public int doStartTag() throws JspException {
		final ServletRequest request = this.pageContext.getRequest();
		if (OsgiPortalAppRequest.class.isAssignableFrom(request.getClass())) {
			// If we are in an OPA context
			final OsgiPortalAppRequest opaRequest = (OsgiPortalAppRequest) request;

			final String opaSignature = opaRequest.getOpaSignature();

			final JspWriter writer = this.pageContext.getOut();

			try {
				writer.write(OpaMetaTag.OPA_REGISTERING_JS.replace(OpaMetaTag.OPA_SIGNATURE_PH, opaSignature));
			} catch (final IOException e) {
				throw new JspException(e);
			}
		}

		return Tag.SKIP_BODY;

	}
}
