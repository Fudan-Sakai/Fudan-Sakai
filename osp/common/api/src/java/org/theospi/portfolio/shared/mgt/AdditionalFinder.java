/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/osp/branches/sakai-2.8.1/common/api/src/java/org/theospi/portfolio/shared/mgt/AdditionalFinder.java $
* $Id: AdditionalFinder.java 59678 2009-04-03 23:20:50Z arwhyte@umich.edu $
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
package org.theospi.portfolio.shared.mgt;

import java.util.Map;

import org.sakaiproject.metaobj.shared.ArtifactFinderManager;

public class AdditionalFinder {


   private Map additionalFinders;
   private ArtifactFinderManager artifactFinderManager;

   public Map getAdditionalFinders() {
      return additionalFinders;
   }

   public void setAdditionalFinders(Map additionalFinders) {
      this.additionalFinders = additionalFinders;
   }

   public ArtifactFinderManager getArtifactFinderManager() {
      return artifactFinderManager;
   }

   public void setArtifactFinderManager(ArtifactFinderManager artifactFinderManager) {
      this.artifactFinderManager = artifactFinderManager;
   }

   public void init() {
      getArtifactFinderManager().getFinders().putAll(getAdditionalFinders());
   }

}

