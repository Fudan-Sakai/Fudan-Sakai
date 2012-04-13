/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sections/branches/sakai-2.8.1/sections-app/src/java/org/sakaiproject/tool/section/jsf/JsfTool.java $
 * $Id: JsfTool.java 59686 2009-04-03 23:37:55Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2008 The Sakai Foundation
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
package org.sakaiproject.tool.section.jsf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.section.api.facade.manager.Authn;
import org.sakaiproject.section.api.facade.manager.Authz;
import org.sakaiproject.section.api.facade.manager.Context;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

/**
 * Extension to the Sakai JsfTool servlet, computing the default page view
 * depending on the user's role (permissions).
 * 
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 *
 */
public class JsfTool extends org.sakaiproject.jsf.util.JsfTool {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(JsfTool.class);
	
	/**
	 * @inheritDoc
	 */
	protected String computeDefaultTarget() {
        if(log.isDebugEnabled()) log.debug("Entering sections tool... determining role appropriate view");

        ApplicationContext ac = (ApplicationContext)getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        Authn authnService = (Authn)ac.getBean("org.sakaiproject.section.api.facade.manager.Authn");
        Authz authzService = (Authz)ac.getBean("org.sakaiproject.section.api.facade.manager.Authz");
        Context contextService = (Context)ac.getBean("org.sakaiproject.section.api.facade.manager.Context");

        String userUid = authnService.getUserUid(null);
        String siteContext = contextService.getContext(null);
        
        boolean viewAllSections = authzService.isViewAllSectionsAllowed(userUid, siteContext);
        boolean viewOwnSections = authzService.isViewOwnSectionsAllowed(userUid, siteContext);

        String target;
        if(viewAllSections) {
            if(log.isDebugEnabled()) log.debug("Sending user to the overview page");
            target = "/overview";
        } else if (viewOwnSections) {
            if(log.isDebugEnabled()) log.debug("Sending user to the student view page");
            target = "/studentView";
        } else {
            // The role filter has not been invoked yet, so this could happen here
            throw new RuntimeException("User " + userUid + " attempted to access sections in site " +
            		siteContext + " without any role");
        }
        return target;
    }
}
