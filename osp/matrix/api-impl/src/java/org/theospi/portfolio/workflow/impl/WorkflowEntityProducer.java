/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/osp/branches/sakai-2.8.1/matrix/api-impl/src/java/org/theospi/portfolio/workflow/impl/WorkflowEntityProducer.java $
* $Id: WorkflowEntityProducer.java 59678 2009-04-03 23:20:50Z arwhyte@umich.edu $
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
package org.theospi.portfolio.workflow.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.metaobj.shared.mgt.EntityProducerBase;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Nov 14, 2005
 * Time: 6:03:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowEntityProducer extends EntityProducerBase {

   protected final Log logger = LogFactory.getLog(getClass());
   public static final String WORKFLOW_PRODUCER = "ospWorkflow";

   public String getLabel() {
      return WORKFLOW_PRODUCER;
   }

   public void init() {
      logger.info("init()");
      try {
         getEntityManager().registerEntityProducer(this, Entity.SEPARATOR + WORKFLOW_PRODUCER);
      }
      catch (Exception e) {
         logger.warn("Error registering Workflow Entity Producer", e);
      }
   }

}
