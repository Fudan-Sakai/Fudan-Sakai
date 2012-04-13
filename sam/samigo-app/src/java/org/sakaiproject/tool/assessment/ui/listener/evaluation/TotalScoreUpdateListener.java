/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/listener/evaluation/TotalScoreUpdateListener.java $
 * $Id: TotalScoreUpdateListener.java 93733 2011-06-10 19:35:08Z ktsao@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009 The Sakai Foundation
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



package org.sakaiproject.tool.assessment.ui.listener.evaluation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.tool.assessment.data.dao.grading.AssessmentGradingData;
import org.sakaiproject.tool.assessment.data.ifc.assessment.EvaluationModelIfc;
import org.sakaiproject.tool.assessment.data.ifc.grading.AssessmentGradingIfc;
import org.sakaiproject.tool.assessment.facade.AgentFacade;
import org.sakaiproject.tool.assessment.services.GradebookServiceException;
import org.sakaiproject.tool.assessment.services.GradingService;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.AgentResults;
import org.sakaiproject.tool.assessment.ui.bean.evaluation.TotalScoresBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.tool.assessment.util.TextFormat;

/**
 * <p>
 * This handles the updating of the Total Score page.
 *  </p>
 * <p>Description: Action Listener Evaluation Updating Total Score front door</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organization: Sakai Project</p>
 * @author Ed Smiley
 * @version $Id: TotalScoreUpdateListener.java 93733 2011-06-10 19:35:08Z ktsao@stanford.edu $
 */

public class TotalScoreUpdateListener
  implements ActionListener
{
  private static Log log = LogFactory.getLog(TotalScoreUpdateListener.class);
  
  /**
   * Standard process action method.
   * @param ae ActionEvent
   * @throws AbortProcessingException
   */
  public void processAction(ActionEvent ae) throws
    AbortProcessingException
  {
    log.debug("Total Score Update LISTENER.");
    TotalScoresBean bean = (TotalScoresBean) ContextUtil.lookupBean("totalScores");
    log.debug("Calling saveTotalScores.");
    if (!saveTotalScores(bean))
    {
        throw new RuntimeException("failed to call saveTotalScores.");
    }
 

  }

  private HashMap prepareAssessmentGradingHash(ArrayList assessmentGradingList){
    HashMap map = new HashMap();
    for (int i=0; i<assessmentGradingList.size(); i++){
      AssessmentGradingIfc a = (AssessmentGradingIfc)assessmentGradingList.get(i);
      map.put(a.getAssessmentGradingId(), a);
    }
    return map;
  }

  /**
   * Persist the results from the ActionForm in the total page.
   * @todo Some of this code will change when we move this to Hibernate persistence.
   * @param bean TotalScoresBean bean
   * @return true if successful
   */
  public boolean saveTotalScores(TotalScoresBean bean)
  {

      ArrayList assessmentGradingList = bean.getAssessmentGradingList();
      HashMap map = prepareAssessmentGradingHash(assessmentGradingList);
      Collection agents = bean.getAgents();
      Iterator iter = agents.iterator();
      ArrayList grading = new ArrayList();
      boolean hasNumberFormatException = false;
      StringBuffer idList = new StringBuffer(" ");
  	  String err = "";
  	  boolean isAnonymousGrading = false;

  	  if (bean.getPublishedAssessment() != null 
  			  && bean.getPublishedAssessment().getEvaluationModel() != null
  			  && bean.getPublishedAssessment().getEvaluationModel().getAnonymousGrading() != null
  			  && bean.getPublishedAssessment().getEvaluationModel().getAnonymousGrading().equals(EvaluationModelIfc.ANONYMOUS_GRADING)) {
  		  isAnonymousGrading = true;
  		  err = (String) ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.EvaluationMessages", "number_format_error_submission_id");
  	  }
  	  else {
  		  err = (String) ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.EvaluationMessages", "number_format_error_user_id");
  	  }
  	  ArrayList badAdjList = new ArrayList();
  	  
  	  while (iter.hasNext())
  	  {
  		  AgentResults agentResults = (AgentResults) iter.next();
  		  StringBuilder newScoreString = new StringBuilder();
  		  boolean update = false;
  		  try {
  			  update = needUpdate(agentResults, map, newScoreString);     
  		  }
  		  catch (NumberFormatException e) {
  			  hasNumberFormatException = true;
  			  update = false;

  			  if (isAnonymousGrading) {
  				  badAdjList.add(agentResults.getAssessmentGradingId());
  			  }
  			  else {
  				  badAdjList.add(agentResults.getAgentEid());
  			  }
  		  }
  		  
        if (update){
        	log.debug("update is true");
        	Float newScore = new Float(0f);
        	AssessmentGradingData data = new AssessmentGradingData();
        	try {
        		if (!agentResults.getAssessmentGradingId().equals(Long.valueOf(-1)) ) {
        			// these are students who have submitted for grades.
        			// Add up new score
        			newScore = Float.valueOf(newScoreString.toString());
        			agentResults.setFinalScore(newScore+"");
        			BeanUtils.copyProperties(data, agentResults);
        			data.setPublishedAssessmentId(bean.getPublishedAssessment().getPublishedAssessmentId());
        			if ("-".equals(agentResults.getTotalAutoScore())) {
        				data.setTotalAutoScore(Float.valueOf(0f));
        			}
        			else {
        				data.setTotalAutoScore(Float.valueOf(agentResults.getTotalAutoScore()));
        			}
        			data.setTotalOverrideScore(Float.valueOf(agentResults.getTotalOverrideScore()));
        			data.setFinalScore(Float.valueOf(agentResults.getFinalScore()));
        			data.setIsLate(agentResults.getIsLate());
        			data.setComments(agentResults.getComments());
        			data.setGradedBy(AgentFacade.getAgentString());
        			data.setGradedDate(new Date());
        			grading.add(data);
        		}
        		else {
        			// these are students who have not submitted for grades and instructor made adjustment to their scores
        			// Add up new score
        			newScore = Float.valueOf(newScoreString.toString());
        			agentResults.setFinalScore(newScore+"");

        			BeanUtils.copyProperties(data, agentResults);
        			data.setAgentId(agentResults.getIdString());
        			data.setForGrade(Boolean.FALSE);
        			//data.setStatus(Integer.valueOf(1));
        			data.setIsLate(Boolean.FALSE);
        			data.setItemGradingSet(new HashSet());
        			data.setPublishedAssessmentId(bean.getPublishedAssessment().getPublishedAssessmentId());
        			// tell hibernate this is a new record
        			data.setAssessmentGradingId(Long.valueOf(0));
        			data.setSubmittedDate(null);
        			data.setTotalAutoScore(Float.valueOf(0f));
        			data.setTotalOverrideScore(Float.valueOf(agentResults.getTotalOverrideScore()));
        			data.setFinalScore(Float.valueOf(agentResults.getFinalScore()));
        			data.setComments(agentResults.getComments());
        			data.setGradedBy(AgentFacade.getAgentString());
        			data.setGradedDate(new Date());
        			// note that I am not sure if we should set this people as late or what?
        			grading.add(data);
        		}

        	}
        	catch (IllegalAccessException e) {
        		log.error("IllegalAccessException: " + e);
        		return false;
        	} catch (InvocationTargetException e) {
        		log.error("InvocationTargetException: " + e);
        		return false;
        	}
        }
      }

      if (hasNumberFormatException) {
    	  if (bean.getPublishedAssessment() != null 
    			  && bean.getPublishedAssessment().getEvaluationModel() != null
    			  && bean.getPublishedAssessment().getEvaluationModel().getAnonymousGrading() != null
    			  && bean.getPublishedAssessment().getEvaluationModel().getAnonymousGrading().equals(EvaluationModelIfc.ANONYMOUS_GRADING)) {
    		  err = (String) ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.EvaluationMessages", "number_format_error_submission_id");
    	  }
    	  else {
    		  err = (String) ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.EvaluationMessages", "number_format_error_user_id");
    	  }
    	  for (int i = 0; i < badAdjList.size(); i++) {
    		  idList.append(badAdjList.get(i));
    		  if (i != badAdjList.size() - 1) {
    			  idList.append(", ");
    		  }
    	  }
    	  idList.append(".");

    	  FacesContext context = FacesContext.getCurrentInstance();
    	  context.addMessage(null, new FacesMessage(err + idList.toString()));
      }
      
      GradingService delegate = new GradingService();
      try {
    	  delegate.saveTotalScores(grading, bean.getPublishedAssessment());
    	  log.debug("Saved total scores.");
      } catch (GradebookServiceException ge) {
    	  FacesContext context = FacesContext.getCurrentInstance();
    	  String error=(String)ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages", "gradebook_exception_error");
    	  context.addMessage(null, new FacesMessage(error));
    	  // scores are saved in Samigo, still return true, but display error to user.
    	  return true;
      }

    return true;
  }

  private boolean needUpdate(AgentResults agentResults, HashMap map, StringBuilder newScoreString) throws NumberFormatException{
    boolean update = true;
    String newComments = TextFormat.convertPlaintextToFormattedTextNoHighUnicode(log, agentResults.getComments());
    agentResults.setComments(newComments);
    log.debug("newComments = " + newComments);

    float totalAutoScore = 0; 
    if (agentResults.getTotalAutoScore()!=null && !("").equals(agentResults.getTotalAutoScore())){
      try{
        totalAutoScore = Float.valueOf(agentResults.getTotalAutoScore()).floatValue();
      }
      catch (NumberFormatException e){
        totalAutoScore = 0;
      }
    }

    float totalOverrideScore = 0; 
    Boolean newIsLate = agentResults.getIsLate(); // if the duedate were postpond, we need to adjust this
    // we will check if there is change of grade. if so, add up new score
    // else skip
    AssessmentGradingIfc old = (AssessmentGradingIfc)map.get(agentResults.getAssessmentGradingId());
    if (old != null){
        if (agentResults.getTotalOverrideScore()!=null && !("").equals(agentResults.getTotalOverrideScore())){
        	try{
        		totalOverrideScore = Float.valueOf(agentResults.getTotalOverrideScore()).floatValue();
        	}
        	catch (NumberFormatException e){
        		log.warn("Adj has wrong input type" + e);
        		throw e;
        	}
        }

      float newScore = totalAutoScore + totalOverrideScore;
      newScoreString.append(Float.valueOf(newScore));
	  float oldScore = 0;
      if (old.getFinalScore()!=null){
        oldScore = old.getFinalScore().floatValue();
      }
      Boolean oldIsLate=old.getIsLate();
      
      String oldComments = old.getComments();
      log.debug("***oldScore = " + oldScore);
      log.debug("***newScore = " + newScore);
      log.debug("***oldIsLate = " + oldIsLate);
      log.debug("***newIsLate = " + newIsLate);
      log.debug("***oldComments = " + oldComments);
      log.debug("***newComments = " + newComments);
      if (oldScore==newScore && newIsLate.equals(oldIsLate) && 
    		  ((newComments!=null && newComments.equals(oldComments)) 
        		   || (newComments==null && oldComments==null)
        		   // following condition will happen when there is no comments (null) and user clicks on SubmissionId.
        		   // getComments() in AgentResults calls Validator.check(comments, "") so the null comment gets set to ""
        		   // there is nothing updated. update flag should be false
        		   || ((newComments!=null && newComments.equals("")) && oldComments==null)) 
        		   ) {
        update = false;
      }
    }
    else{ // no assessmentGradingData exists
    	boolean noOverrideScore =  false;
    	boolean noComment =  false;
    	String score = agentResults.getTotalOverrideScore();
    	if (score != null) {
    		if (!("").equals(score.trim()) && !("-").equals(score.trim())) {
    			try{
    				totalOverrideScore = Float.valueOf(agentResults.getTotalOverrideScore()).floatValue();
    				noOverrideScore = false;
    			}
    			catch (NumberFormatException e){
    				log.warn("Adj has wrong input type" + e);
    				throw e;
    			}
    		}
    		else {
    			noOverrideScore = true;
    			totalAutoScore = 0;
    		}
    	}
    	else {
    		noOverrideScore = true;
    		totalAutoScore = 0;
    	}
		float newScore = totalAutoScore + totalOverrideScore;
		newScoreString.append(Float.valueOf(newScore));
	    
    	if ("".equals(agentResults.getComments().trim()))
    		noComment = true;

    	if (noOverrideScore && noComment) 
    		update = false;
    }
    return update;
  }

}
