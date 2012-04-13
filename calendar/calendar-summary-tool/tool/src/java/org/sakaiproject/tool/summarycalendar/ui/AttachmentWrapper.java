/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/calendar/branches/sakai-2.8.1/calendar-summary-tool/tool/src/java/org/sakaiproject/tool/summarycalendar/ui/AttachmentWrapper.java $
 * $Id: AttachmentWrapper.java 60189 2009-04-17 03:30:51Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2008 The Sakai Foundation
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

package org.sakaiproject.tool.summarycalendar.ui;

public class AttachmentWrapper {
	private String displayName;
	private String url;
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
