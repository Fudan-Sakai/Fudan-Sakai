/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/syllabus/branches/sakai-2.8.1/syllabus-app/src/java/org/sakaiproject/jsf/syllabus/SyllabusShowAreaComponent.java $
 * $Id: SyllabusShowAreaComponent.java 59687 2009-04-03 23:44:40Z arwhyte@umich.edu $
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
package org.sakaiproject.jsf.syllabus;

import javax.faces.component.UIComponentBase;

public class SyllabusShowAreaComponent extends UIComponentBase
{
	public SyllabusShowAreaComponent()
	{
		super();
		this.setRendererType("SyllabusShowAreaRender");
	}

	public String getFamily()
	{
		return "SyllabusShowArea";
	}
}



