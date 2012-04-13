/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/syllabus/branches/sakai-2.8.1/syllabus-impl/src/java/org/sakaiproject/syllabus/user/prefs/SyllabusUserNotificationPreferencesRegistrationImpl.java $
 * $Id: SyllabusUserNotificationPreferencesRegistrationImpl.java 83343 2010-10-18 17:51:38Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2010 The Sakai Foundation
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

package org.sakaiproject.syllabus.user.prefs;

import org.sakaiproject.util.UserNotificationPreferencesRegistrationImpl;
import org.sakaiproject.util.ResourceLoader;

public class SyllabusUserNotificationPreferencesRegistrationImpl extends UserNotificationPreferencesRegistrationImpl {

	public ResourceLoader getResourceLoader(String location) {
		return new ResourceLoader(location);
	}

}
