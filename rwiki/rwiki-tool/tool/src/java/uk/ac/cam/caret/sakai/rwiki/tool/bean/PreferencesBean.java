/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/rwiki/branches/sakai-2.8.1/rwiki-tool/tool/src/java/uk/ac/cam/caret/sakai/rwiki/tool/bean/PreferencesBean.java $
 * $Id: PreferencesBean.java 9108 2006-05-08 14:30:57Z ian@caret.cam.ac.uk $
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

package uk.ac.cam.caret.sakai.rwiki.tool.bean;

public class PreferencesBean
{

	public static final String NOTIFICATION_PREFERENCE_PARAM = "notificationLevel";

	private String notificationLevel;

	public static final String NO_PREFERENCE = "nopreference";

	public String getNotificationLevel()
	{
		return notificationLevel;
	}

	public void setNotifcationLevel(String notificationLevel)
	{
		this.notificationLevel = notificationLevel;
	}

}
