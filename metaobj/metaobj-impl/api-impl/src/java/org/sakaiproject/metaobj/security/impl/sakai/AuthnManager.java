/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-impl/api-impl/src/java/org/sakaiproject/metaobj/security/impl/sakai/AuthnManager.java $
 * $Id: AuthnManager.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

package org.sakaiproject.metaobj.security.impl.sakai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.metaobj.security.AnonymousAgent;
import org.sakaiproject.metaobj.security.AuthenticationManager;
import org.sakaiproject.metaobj.shared.model.Agent;
import org.sakaiproject.user.cover.UserDirectoryService;

public class AuthnManager extends SecurityBase implements AuthenticationManager {
   protected final transient Log logger = LogFactory.getLog(getClass());

   /**
    * @return current agent
    */
   public Agent getAgent() {
      String userId = UserDirectoryService.getCurrentUser().getId();
      if (userId == null || userId.equals("")) {
         return new AnonymousAgent();
      }
      return morphAgent(UserDirectoryService.getCurrentUser());
   }

   public void logout() {
      UserDirectoryService.destroyAuthentication();
   }

}
