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

package fr.mby.opa.picsimpl.dao;

import org.springframework.stereotype.Repository;

import fr.mby.opa.pics.dao.ISessionDao;
import fr.mby.opa.pics.model.Session;

/**
 * @author Maxime Bossard - 2013
 * 
 */
@Repository
public class DbSessionDao extends AbstractPicsEntityDao<Session> implements ISessionDao {

}