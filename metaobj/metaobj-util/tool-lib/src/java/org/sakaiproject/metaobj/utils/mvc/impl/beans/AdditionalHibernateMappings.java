/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-util/tool-lib/src/java/org/sakaiproject/metaobj/utils/mvc/impl/beans/AdditionalHibernateMappings.java $
 * $Id: AdditionalHibernateMappings.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

package org.sakaiproject.metaobj.utils.mvc.impl.beans;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class AdditionalHibernateMappings {
   protected final transient Log logger = LogFactory.getLog(getClass());

   private Resource[] mappingLocations;

   public void setMappingResources(String[] mappingResources) {
      this.mappingLocations = new Resource[mappingResources.length];
      for (int i = 0; i < mappingResources.length; i++) {
         this.mappingLocations[i] = new ClassPathResource(mappingResources[i].trim());
      }
   }

   public Resource[] getMappingLocations() {
      return mappingLocations;
   }

   public void processConfig(Configuration config) throws IOException, MappingException {
      for (int i = 0; i < this.mappingLocations.length; i++) {
         config.addInputStream(this.mappingLocations[i].getInputStream());
      }
   }
}
