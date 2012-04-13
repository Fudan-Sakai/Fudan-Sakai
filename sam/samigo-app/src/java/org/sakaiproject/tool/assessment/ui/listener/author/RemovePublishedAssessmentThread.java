/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/listener/author/RemovePublishedAssessmentThread.java $
 * $Id: RemovePublishedAssessmentThread.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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



package org.sakaiproject.tool.assessment.ui.listener.author;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;

/**
 * <p>Title: Samigo</p>
 * <p>Description: Sakai Assessment Manager</p>
 * @author Qingru Zhang
 * @version $Id: RemovePublishedAssessmentThread.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 */

public class RemovePublishedAssessmentThread extends Thread
{

  private static Log log = LogFactory.getLog(RemovePublishedAssessmentThread.class);
  private String assessmentId;
  private String action;
  public RemovePublishedAssessmentThread(String assessmentId){
    this.assessmentId = assessmentId;
  }
  
  public RemovePublishedAssessmentThread(String assessmentId, String action){
	    this.assessmentId = assessmentId;
	    this.action = action;
  }

  public void run(){
    PublishedAssessmentService assessmentService = new PublishedAssessmentService();
    log.debug("** remove assessmentId= "+this.assessmentId);
    assessmentService.removeAssessment(this.assessmentId, this.action);
  }

}
