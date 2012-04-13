/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/syllabus/branches/sakai-2.8.1/syllabus-api/src/java/org/sakaiproject/api/app/syllabus/SyllabusService.java $
 * $Id: SyllabusService.java 59687 2009-04-03 23:44:40Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.api.app.syllabus;

import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.EntityProducer;

import java.util.List;

public interface SyllabusService extends EntityProducer
{
  /** This string can be used to find the service in the service manager. */
	public static final String APPLICATION_ID = "sakai:syllabus";
	
	public static final String EVENT_SYLLABUS_POST_NEW = "syllabus.post.new";
	
	public static final String EVENT_SYLLABUS_POST_CHANGE = "syllabus.post.change";
	
//for adding more logging info and not send out email notification
	//public static final String EVENT_SYLLABUS_DELETE_POST = "syllabus.delete.posted";
	public static final String EVENT_SYLLABUS_DELETE_POST = "syllabus.delete";
	
	public static final String EVENT_SYLLABUS_READ = "syllabus.read";

	public static final String EVENT_SYLLABUS_DRAFT_NEW = "syllabus.draft.new";
	
	public static final String EVENT_SYLLABUS_DRAFT_CHANGE = "syllabus.draft.change";

	public static final String REFERENCE_ROOT = Entity.SEPARATOR + "syllabus";
	
	public static final String SYLLABUS_SERVICE_NAME = "org.sakaiproject.api.app.syllabus.SyllabusService";
	
	//permission convert
	public static final String PERMISSION_UPDATE = "syllabus.update"; 
		
	public void postNewSyllabus(SyllabusData data);
	
	public void postChangeSyllabus(SyllabusData data);
	
	public void deletePostedSyllabus(SyllabusData data);
	
	public void readSyllabus(SyllabusData data);
	
	public void draftNewSyllabus(SyllabusData data);
	
	public void draftChangeSyllabus(SyllabusData data);
	
	public List getMessages(String id);
	
	public void importEntities(String fromSiteId, String toSiteId, List resourceIds);
	
	//permission convert
	public String getEntityReference(SyllabusData sd, String thisSiteId);
	
	public String getSyllabusApplicationSiteReference(String thisSiteId);
	
	public boolean checkPermission(String lock, String reference);

}
