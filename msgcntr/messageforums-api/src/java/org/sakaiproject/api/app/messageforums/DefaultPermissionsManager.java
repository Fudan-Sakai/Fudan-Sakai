/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/DefaultPermissionsManager.java $
 * $Id: DefaultPermissionsManager.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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

/**
 * @author <a href="mailto:rshastri@iupui.edu">Rashmi Shastri</a>
 */
public interface DefaultPermissionsManager
{
  public static final String FUNCTION_NEW_FORUM="messagecenter.newForum";
  public static final String FUNCTION_NEW_TOPIC="messagecenter.newTopic";
  public static final String FUNCTION_NEW_RESPONSE="messagecenter.newResponse";
  public static final String FUNCTION_NEW_RESPONSE_TO_RESPONSE="messagecenter.newResponseToResponse";
  public static final String FUNCTION_MOVE_POSTINGS="messagecenter.movePostings";
  public static final String FUNCTION_CHANGE_SETTINGS="messagecenter.changeSettings";
  public static final String FUNCTION_POST_TO_GRADEBOOK="messagecenter.postToGradebook";
 
  public static final String FUNCTION_READ="messagecenter.read";
  public static final String FUNCTION_REVISE_ANY="messagecenter.reviseAny";
  public static final String FUNCTION_REVISE_OWN="messagecenter.reviseOwn";
  public static final String FUNCTION_DELETE_ANY="messagecenter.deleteAny";
  public static final String FUNCTION_DELETE_OWN="messagecenter.deleteOwn";
  public static final String FUNCTION_MARK_AS_READ="messagecenter.markAsRead";
  
  public static final String MESSAGE_FUNCTION_PREFIX="msg.";
  public static final String MESSAGE_FUNCTION_EMAIL= MESSAGE_FUNCTION_PREFIX +"emailout";
  
  
  // control permissions
  public boolean isNewForum(String role);

  public boolean isNewTopic(String role);

  public boolean isNewResponse(String role);

  public boolean isResponseToResponse(String role);

  public boolean isMovePostings(String role);

  public boolean isChangeSettings(String role);

  public boolean isPostToGradebook(String role);

  // message permissions
  public boolean isRead(String role);

  public boolean isReviseAny(String role);

  public boolean isReviseOwn(String role);

  public boolean isDeleteAny(String role);

  public boolean isDeleteOwn(String role);

  public boolean isMarkAsRead(String role);

}
