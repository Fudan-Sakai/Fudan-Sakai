/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-api/api/src/java/org/sakaiproject/metaobj/shared/mgt/AgentManager.java $
 * $Id: AgentManager.java 77238 2010-05-07 12:02:52Z botimer@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009 The Sakai Foundation
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

package org.sakaiproject.metaobj.shared.mgt;

import java.util.List;

import org.sakaiproject.metaobj.shared.model.Agent;
import org.sakaiproject.metaobj.shared.model.Id;

public interface AgentManager {

   public String TYPE_DISPLAY_NAME = "displayName"; 
   public String TYPE_EMAIL = "email";
   public String TYPE_EID = "eid";
   
   /**
    * @param id
    * @return
    */
   public Agent getAgent(Id id);

   public Agent getAgent(String username);

   public Agent getWorksiteRole(String roleName);

   public Agent getWorksiteRole(String roleName, String siteId);

   public Agent getTempWorksiteRole(String roleName, String siteId);
   
   public Agent getRealmRole(String roleName, String realmId);   

   public Agent getAnonymousAgent();

   public Agent getAdminAgent();

   /**
    * if type is null return all records
    *
    * @param type   added typed list
    * @param object
    * @return
    */
   public List findByProperty(String type, Object object);


   /**
    * @param agent
    * @return
    */
   public Agent createAgent( String displayName, Id id );

   /**
    * @param agent
    */
   public void deleteAgent(Agent agent);

   public void updateAgent(Agent agent);

   /**
    * @param siteId
    * @return list of agents that are participants in the given siteId
    */
   public List getWorksiteAgents(String siteId);

   /**
    * @param siteId
    * @return list of roles for the given siteId.  The list is a collection of type Agent.
    */
   public List getWorksiteRoles(String siteId);

}
