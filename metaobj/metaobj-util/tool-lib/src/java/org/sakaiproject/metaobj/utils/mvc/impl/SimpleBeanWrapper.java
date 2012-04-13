/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-util/tool-lib/src/java/org/sakaiproject/metaobj/utils/mvc/impl/SimpleBeanWrapper.java $
 * $Id: SimpleBeanWrapper.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

package org.sakaiproject.metaobj.utils.mvc.impl;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Apr 30, 2004
 * Time: 9:02:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleBeanWrapper {
   private Object wrappedBean = null;

   public Object getWrappedBean() {
      return wrappedBean;
   }

   public void setWrappedBean(Object wrappedBean) {
      this.wrappedBean = wrappedBean;
   }

}
