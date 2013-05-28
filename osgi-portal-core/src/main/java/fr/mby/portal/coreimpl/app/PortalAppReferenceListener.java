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

package fr.mby.portal.coreimpl.app;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.gemini.blueprint.service.importer.ImportedOsgiServiceProxy;
import org.osgi.framework.ServiceReference;

import fr.mby.portal.api.app.IPortalApp;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class PortalAppReferenceListener {

	/** All IAppReferences. */
	private final Set<ServiceReference> portalAppReferences = Collections
			.synchronizedSet(new HashSet<ServiceReference>(16));

	public void onBind(final IPortalApp portalApp) {
		if (portalApp != null) {
			final ImportedOsgiServiceProxy serviceProxy = (ImportedOsgiServiceProxy) portalApp;
			this.portalAppReferences.add(serviceProxy.getServiceReference());
		}
	}

	public void onUnbind(final IPortalApp portalApp) {
		if (portalApp != null) {
			final ImportedOsgiServiceProxy serviceProxy = (ImportedOsgiServiceProxy) portalApp;
			this.portalAppReferences.remove(serviceProxy.getServiceReference());
		}
	}

	/**
	 * Getter of portalAppReferences.
	 * 
	 * @return the portalAppReferences
	 */
	public Set<ServiceReference> getPortalAppReferences() {
		return Collections.unmodifiableSet(this.portalAppReferences);
	}

}
