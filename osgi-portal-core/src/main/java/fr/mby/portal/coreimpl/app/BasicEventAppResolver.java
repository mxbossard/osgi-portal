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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.mby.portal.api.action.IUserAction;
import fr.mby.portal.api.app.IEventApp;
import fr.mby.portal.core.app.IEventAppResolver;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Service
public class BasicEventAppResolver implements IEventAppResolver<IUserAction> {

	@Autowired(required = false)
	private List<IEventApp> portalEventApplications;

	@Override
	public Iterable<IEventApp> resolve(final IUserAction userAction) {
		// Return all event apps
		return this.portalEventApplications;
	}

}
