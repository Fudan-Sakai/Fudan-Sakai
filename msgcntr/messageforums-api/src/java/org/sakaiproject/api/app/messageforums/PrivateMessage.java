/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-api/src/java/org/sakaiproject/api/app/messageforums/PrivateMessage.java $
 * $Id: PrivateMessage.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
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
 
public interface PrivateMessage extends Message {

    public Boolean getExternalEmail();

    public void setExternalEmail(Boolean externalEmail);

    public String getExternalEmailAddress();

    public void setExternalEmailAddress(String externalEmailAddress);

    public List getRecipients();

    public void setRecipients(List recipients);
    
    public void addRecipient(MessageForumsUser user);
    
    public void removeRecipient(MessageForumsUser user);
    
    public String getRecipientsAsText() ;
    
    public void setRecipientsAsText(String recipientsAsText) ;
        
        

}