/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/tags/msgcntr-2.8.2/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/MessageForumsUserManager.java $
 * $Id: MessageForumsUserManager.java 59677 2009-04-03 23:19:29Z arwhyte@umich.edu $
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

public interface MessageForumsUserManager {
      
  /**
   * get forum user and create if necessary
   * @param userId to associate with
 * @throws UserNotDefinedException 
   */
  public MessageForumsUser getForumUser(String userId);
    
    
  /**
   * Save a forum user
   * @param user to save
   */
  public void saveForumUser(MessageForumsUser user);


}