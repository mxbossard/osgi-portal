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

package fr.mby.portal.api.app;

/**
 * The IAppContext interface makes resources available to the App. Using the context, an App can access the app log, and
 * obtain URL references to resources. There is one context per application per Java Virtual Machine. As a web
 * application, an application also has a servlet context. The app context leverages most of its functionality from the
 * servlet context of the application. Attributes stored in the context are global for all users and all components in
 * the application. In the case of a web application marked "distributed" in its deployment descriptor, there will be
 * one context instance for each virtual machine. In this situation, the context cannot be used as a location to share
 * global information (because the information is not truly global). Use an external resource, such as a database to
 * achieve sharing on a global scope.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public interface IAppContext {

	void log(String message, Object[]... objects);

	void log(String message, Throwable throwable, Object[]... objects);

	long getBundleId();

	String getWebContextPath();

}
