/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-api/api/src/java/org/sakaiproject/metaobj/worksite/mgt/WorksiteManager.java $
 * $Id: WorksiteManager.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008 The Sakai Foundation
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

package org.sakaiproject.metaobj.worksite.mgt;

import java.util.List;
import java.util.Map;

import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.ToolConfiguration;

public interface WorksiteManager {

   public static final String WORKSITE_MAINTAIN = "maintain";

   /**
    * @return list of all sites the current user belongs to
    */
   public List getUserSites();
   
   /**
    * @return list of all sites the current user belongs to
    */
   public List getUserSites(Map properties, List siteTypes);

   public Id getCurrentWorksiteId();

   /**
    * get all the tools for a site that have a type of "toolId"
    * if tool id is null, get all tools for a site
    *
    * @param toolId
    * @param site
    * @return
    */
   public List getSiteTools(String toolId, Site site);

   public Site getSite(String siteId);

   public AuthzGroup getSiteRealm(String siteId);

   public ToolConfiguration getTool(String id);

   public boolean isUserInSite(String siteId);

}
