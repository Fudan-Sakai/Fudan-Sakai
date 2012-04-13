/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/rwiki/branches/sakai-2.8.1/rwiki-api/api/src/java/uk/ac/cam/caret/sakai/rwiki/service/api/model/DataMigrationAgent.java $
 * $Id: DataMigrationAgent.java 9644 2006-05-18 11:21:28Z ian@caret.cam.ac.uk $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package uk.ac.cam.caret.sakai.rwiki.service.api.model;

/**
 * Migrates data between different versions of the database
 * 
 * @author ieb
 */
// FIXME: Service
public interface DataMigrationAgent
{
	String migrate(String current, String target, boolean newdb) throws Exception;

}
