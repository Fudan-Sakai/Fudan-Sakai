/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sections/branches/sakai-2.8.1/sections-app/src/java/org/sakaiproject/tool/section/facade/sakai/AuthnSakaiImpl.java $
 * $Id: AuthnSakaiImpl.java 59686 2009-04-03 23:37:55Z arwhyte@umich.edu $
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
package org.sakaiproject.tool.section.facade.sakai;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.section.api.facade.manager.Authn;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.cover.SessionManager;

/**
 * Uses Sakai's SessionManager to determine the current user's uuid.
 * 
 * @author <a href="jholtzman@berkeley.edu">Josh Holtzman</a>
 */
public class AuthnSakaiImpl implements Authn {
    private static final Log log = LogFactory.getLog(AuthnSakaiImpl.class);

    /**
     * @see org.sakaiproject.section.api.facade.managers.Authn#getUserUid()
     */
    public String getUserUid(Object request) {
        Session session = SessionManager.getCurrentSession();
        String userId = session.getUserId();
        if(log.isDebugEnabled()) log.debug("current user id is " + userId);
        return userId;
    }

}
