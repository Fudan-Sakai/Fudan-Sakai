/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-util/tool-lib/src/java/org/sakaiproject/metaobj/utils/mvc/impl/ControllerFilterManager.java $
 * $Id: ControllerFilterManager.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.sakaiproject.metaobj.utils.mvc.intf.ControllerFilter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.ModelAndView;

public class ControllerFilterManager implements ApplicationContextAware {
   private Collection filters;

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      filters = applicationContext.getBeansOfType(ControllerFilter.class, false, false).values();
   }

   public Collection getFilters() {
      return filters;
   }

   public void processFilters(Map request, Map session, Map application, ModelAndView modelAndView, String formView) {
      for (Iterator i = filters.iterator(); i.hasNext();) {
         ControllerFilter filter = (ControllerFilter) i.next();
         filter.doFilter(request, session, application, modelAndView, formView);
      }
   }
}
