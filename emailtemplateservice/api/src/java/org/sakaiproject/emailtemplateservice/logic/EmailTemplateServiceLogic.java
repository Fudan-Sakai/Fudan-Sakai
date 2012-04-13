/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/emailtemplateservice/tags/emailtemplateservice-0.5.4/api/src/java/org/sakaiproject/emailtemplateservice/logic/EmailTemplateServiceLogic.java $
 * $Id: EmailTemplateServiceLogic.java 52893 2008-09-29 16:16:30Z bkirschn@umich.edu $
 ***********************************************************************************
 *
 * Copyright 2006, 2007 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
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

package org.sakaiproject.emailtemplateservice.logic;

import java.util.List;

public interface EmailTemplateServiceLogic {

	/**
	 *  Get all the templates defined in the sytem
	 * @return a list of templates
	 */
	public List getAllTemplates();
	
	
}
