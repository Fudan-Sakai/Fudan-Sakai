/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/tags/msgcntr-2.8.2/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/MembershipManager.java $
 * $Id: MembershipManager.java 59677 2009-04-03 23:19:29Z arwhyte@umich.edu $
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
package org.sakaiproject.api.app.messageforums;

import java.util.List;
import java.util.Map;

public interface MembershipManager {
           
  /**
   * Get filtered members for course all/user/role/group<br>
   * Return hash map for direct access to members via id
   * (used in UI when for selected list items)<br>
   * Filter roles/groups which do not have members
   * @param filterFerpa
   * @return map of members
   */
  public Map getFilteredCourseMembers(boolean filterFerpa);
  
    
  /**
   * Get members for course all/user/role/group<br>
   * Return hash map for direct access to members via id
   * (used in UI when for selected list items)<br>
   * @param filterFerpa
   * @param includeRoles
   * @param includeAllParticipantsMember
   * @return map of members
   */
  public Map getAllCourseMembers(boolean filterFerpa, boolean includeRoles, boolean includeAllParticipantsMember);
  
  /**
   * get all users for course w/o filtering of FERPA enabled members
   * @return list of MembershipItems
   */
  public List getAllCourseUsers();
  
  /**
   * returns a list for UI
   * @param memberMap
   * @return list of members
   */
  public List convertMemberMapToList(Map memberMap);
  
}
