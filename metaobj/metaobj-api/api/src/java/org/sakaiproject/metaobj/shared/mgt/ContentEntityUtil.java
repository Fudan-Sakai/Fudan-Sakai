/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-api/api/src/java/org/sakaiproject/metaobj/shared/mgt/ContentEntityUtil.java $
 * $Id: ContentEntityUtil.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

package org.sakaiproject.metaobj.shared.mgt;

import org.sakaiproject.entity.api.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Nov 14, 2005
 * Time: 6:13:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContentEntityUtil {

   private static ContentEntityUtil instance = new ContentEntityUtil();

   public String buildRef(String producer, String siteId, String contextId, String reference) {
      return Entity.SEPARATOR + producer +
            Entity.SEPARATOR + siteId + Entity.SEPARATOR + contextId + reference;
   }

   public static ContentEntityUtil getInstance() {
      return instance;
   }
}
