/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/listener/author/SortPublishedAssessmentListener.java $
 * $Id: SortPublishedAssessmentListener.java 65883 2009-08-17 18:44:33Z ktsao@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008, 2009 The Sakai Foundation
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

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.facade.AgentFacade;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacade;
import org.sakaiproject.tool.assessment.facade.PublishedAssessmentFacadeQueries;
import org.sakaiproject.tool.assessment.services.GradingService;
import org.sakaiproject.tool.assessment.services.assessment.PublishedAssessmentService;
import org.sakaiproject.tool.assessment.ui.bean.author.AuthorBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

/**
 * <p>Description: SortPublishedAssessmentListener</p>
 */

public class SortPublishedAssessmentListener
    implements ActionListener
{
  private static Log log = LogFactory.getLog(SortPublishedAssessmentListener.class);

  public SortPublishedAssessmentListener()
  {
  }

  public void processAction(ActionEvent ae) throws AbortProcessingException
  {
    // get service and managed bean
    PublishedAssessmentService publishedAssessmentService = new PublishedAssessmentService();
    AuthorBean author = (AuthorBean) ContextUtil.lookupBean(
                       "author");

    processSortInfo(author);
    
    // Refresh the inactive published assessment list.
    AuthorActionListener authorActionListener = new AuthorActionListener();
    GradingService gradingService = new GradingService();

    ArrayList publishedAssessmentList = publishedAssessmentService.getBasicInfoOfAllPublishedAssessments2(
 		   this.getPublishedOrderBy(author), author.isPublishedAscending(), AgentFacade.getCurrentSiteId());
    HashMap startedCounts = gradingService.getInProgressCounts(AgentFacade.getCurrentSiteId());
	HashMap submittedCounts = gradingService.getSubmittedCounts(AgentFacade.getCurrentSiteId());
	ArrayList dividedPublishedAssessmentList = authorActionListener.getTakeableList(publishedAssessmentList);
    authorActionListener.prepareActivePublishedAssessmentsList(author, (ArrayList) dividedPublishedAssessmentList.get(0), startedCounts, submittedCounts);
  }

  /**
   * get orderby parameter for takable table
   * @param select the SelectAssessment bean
   * @return
   */
  private String getPublishedOrderBy(AuthorBean author) {
    String sort = author.getPublishedAssessmentOrderBy();
    String returnType =  PublishedAssessmentFacadeQueries.TITLE;
    if (sort == null) {
    	return returnType;
    }
    else {
    	if("releaseTo".equals(sort))
    	{
    		returnType = PublishedAssessmentFacadeQueries.PUB_RELEASETO;
    	}
    	else if ("startDate".equals(sort))
    	{
    		returnType = PublishedAssessmentFacadeQueries.PUB_STARTDATE;
    	}
    	else if ("dueDate".equals(sort))
    	{
    		returnType = PublishedAssessmentFacadeQueries.PUB_DUEDATE;
    	}
    }

    return returnType;
  }

/**
   * look at sort info from post and set bean accordingly
   * @param bean the select index managed bean
   */
  private void processSortInfo(AuthorBean bean) {
    String pubOrder = ContextUtil.lookupParam("pubSortType");
    String publishedAscending = ContextUtil.lookupParam("publishedAscending");

    if (pubOrder != null && !pubOrder.trim().equals("")) {
      bean.setPublishedAssessmentOrderBy(pubOrder);
    }

    if (publishedAscending != null && !publishedAscending.trim().equals("")) {
      try {
        bean.setPublishedAscending((Boolean.valueOf(publishedAscending)).booleanValue());
      }
      catch (Exception ex) { //skip
        log.warn(ex.getMessage());
      }
    }
    else
    {
	bean.setPublishedAscending(true);
    }

  }
}
