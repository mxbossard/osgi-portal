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
package fr.mby.portal.core.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.mby.portal.action.IUserAction;
import fr.mby.portal.action.IUserActionFactory;
import fr.mby.portal.core.IPortalManager;
import fr.mby.portal.core.IUserActionDispatcher;

/**
 * @author Maxime Bossard - 2013
 *
 */
public class BasicPortalManager implements IPortalManager {

	private IUserActionFactory userActionFactory;
	
	private IUserActionDispatcher userActionDispatcher;
	
	@Override
	public void dispatchUserAction(final HttpServletRequest request,
			final HttpServletResponse response) {
		final IUserAction userAction = this.userActionFactory.build(request);
		this.userActionDispatcher.dispatch(userAction);
	}

}
