/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/TopicControlPermission.java $
 * $Id: TopicControlPermission.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
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

public interface TopicControlPermission {

    public Boolean getChangeSettings();

    public void setChangeSettings(Boolean changeSettings);

    public Boolean getMovePostings();

    public void setMovePostings(Boolean movePostings);

    public Boolean getNewResponse();

    public void setNewResponse(Boolean newResponse);

    public Boolean getResponseToResponse();

    public void setResponseToResponse(Boolean responseToResponse);

    public String getRole();

    public void setRole(String role);

    public Boolean getPostToGradebook();
    
    public void setPostToGradebook(Boolean postToGradebook);

}