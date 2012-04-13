/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/roster/branches/sakai-2.8.1/roster-app/src/java/org/sakaiproject/tool/roster/RosterPageBean.java $
 * $Id: RosterPageBean.java 59682 2009-04-03 23:31:05Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation
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
package org.sakaiproject.tool.roster;

import javax.faces.event.ActionEvent;

public interface RosterPageBean {
	public String getPageTitle();

	public boolean isExportablePage();
	public boolean isRenderStatusLink();
	public boolean isRenderPicturesLink();
	public boolean isRenderProfileLinks();
	
	public void export(ActionEvent event);

}
