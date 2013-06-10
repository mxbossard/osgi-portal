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
 * Represent an App to render in the Portal.
 * 
 * @author Maxime Bossard - 2013
 * 
 */
public interface IApp {

	/**
	 * The IAppConfig of this IApp.
	 * 
	 * @return the IAppConfig
	 */
	IAppConfig getConfig();

	/**
	 * The namespace is an identifier which can be use to uniquely identify this IApp among others IApp rendered in the
	 * portal.
	 * 
	 * @return the App namespace
	 */
	String getNamespace();

	/**
	 * The signature is the information which authorize this IApp to register to the Portal on client side.
	 * 
	 * @return the App signature
	 */
	String getSignature();

	/**
	 * The width of this IApp when rendered.
	 * 
	 * @return the IApp width
	 */
	String getWidth();

	/**
	 * The height of this IApp when rendered.
	 * 
	 * @return the IApp height
	 */
	String getHeight();

	/**
	 * The title of this IApp to display.
	 * 
	 * @return the IApp title
	 */
	String getTitle();
}
