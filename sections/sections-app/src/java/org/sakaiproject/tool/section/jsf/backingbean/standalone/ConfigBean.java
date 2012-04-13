/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sections/branches/sakai-2.8.1/sections-app/src/java/org/sakaiproject/tool/section/jsf/backingbean/standalone/ConfigBean.java $
 * $Id: ConfigBean.java 59686 2009-04-03 23:37:55Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2008 The Sakai Foundation
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
package org.sakaiproject.tool.section.jsf.backingbean.standalone;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.sakaiproject.section.api.SectionManager;
import org.sakaiproject.tool.section.jsf.backingbean.CourseDependentBean;

public class ConfigBean extends CourseDependentBean {
	private static final long serialVersionUID = 1L;

	public String updateConfigAllManual() {
		getServletContext().setAttribute(SectionManager.CONFIGURATION_KEY, SectionManager.ExternalIntegrationConfig.MANUAL_MANDATORY);
		return "overview";
	}
	
	public String updateConfigAllAutomatic() {
		getServletContext().setAttribute(SectionManager.CONFIGURATION_KEY, SectionManager.ExternalIntegrationConfig.AUTOMATIC_MANDATORY);
		return "overview";
	}

	public String updateConfigOptionalAll() {
		getServletContext().setAttribute(SectionManager.CONFIGURATION_KEY, SectionManager.ExternalIntegrationConfig.AUTOMATIC_DEFAULT);
		return "overview";
	}

	public String updateConfigOptionalMultiple() {
		getServletContext().setAttribute(SectionManager.CONFIGURATION_KEY, SectionManager.ExternalIntegrationConfig.MANUAL_DEFAULT);
		return "overview";
	}

	private ServletContext getServletContext() {
		return (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
	}
}
