/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/bean/evaluation/AgentResults.java $
 * $Id: AgentResults.java 76242 2010-04-15 19:30:40Z ktsao@stanford.edu $
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


package org.sakaiproject.tool.assessment.ui.bean.evaluation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.FilePickerHelper;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.tool.assessment.data.dao.grading.ItemGradingData;
import org.sakaiproject.tool.assessment.data.ifc.assessment.PublishedAssessmentIfc;
import org.sakaiproject.tool.assessment.ui.bean.util.Validator;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.tool.assessment.util.AttachmentUtil;
import org.sakaiproject.tool.cover.SessionManager;


/**
 * A set of information for an agent.  This contains both totalScores
 * and questionScores information.
*/

public class AgentResults
    implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 2820488402465439395L;
	private static Log log = LogFactory.getLog(AgentResults.class);
	
  private Long assessmentGradingId;
  private Long itemGradingId;
  private String agentId;
  private String agentEid;
  private String firstName;
  private String lastName;
  private String lastInitial;
  private String email;
  private String idString;
  private String role;
  private PublishedAssessmentIfc publishedAssessment;
  private Date submittedDate;
  private Date attemptDate;
  private Boolean isLate;
  private Boolean forGrade;
  private String totalAutoScore;
  private String totalOverrideScore;
  private String finalScore; // final total score
  private String answer; // The abbreviated text or link of the answer
  private String fullAnswer=""; // The full text or link of the answer
  private String comments;
  private Integer status;
  private String gradedBy;
  private Date gradedDate;
  private Set itemGradingSet;
  private ArrayList itemGradingArrayList;
  private String rationale="";
  private boolean retakeAllowed;
  private boolean isAutoSubmitted;
  private boolean isAttemptDateAfterDueDate;
  private ItemGradingData itemGrading;
  private List itemGradingAttachmentList;
  private Integer timeElapsed;
  private int submissionCount=0;
  private Float scoreSummation=new Float("0");
  private Float averageScore= new Float("0");
  
  public AgentResults() {
  }

  public Float getScoreSummation(){
	  return scoreSummation;
  }

  public void setScoreSummation(Float scoreSummation){
	  this.scoreSummation= scoreSummation;
  }

  public int getSubmissionCount(){
	  return submissionCount;
  }

  public void  setSubmissionCount(int submissionCount){
	  this.submissionCount=submissionCount;
  }

  public Long getAssessmentGradingId() {
    return assessmentGradingId;
  }
  public void setAssessmentGradingId(Long assessmentGradingId) {
    this.assessmentGradingId = assessmentGradingId;
  }
  public Long getItemGradingId() {
    return itemGradingId;
  }
  public void setItemGradingId(Long itemGradingId) {
    this.itemGradingId = itemGradingId;
  }
  public PublishedAssessmentIfc getPublishedAssessment() {
    return publishedAssessment;
  }
  public void setPublishedAssessment(PublishedAssessmentIfc publishedAssessment) {
    this.publishedAssessment = publishedAssessment;
  }
  public String getAgentId() {
    return Validator.check(agentId, "N/A");
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

  public String getAgentEid() {
    return Validator.check(agentEid, "N/A");
  }

  public void setAgentEid(String agentEid) {
    this.agentEid = agentEid;
  }

  public String getFirstName() {
    return Validator.check(firstName, "");
  }

  public void setFirstName(String name) {
    firstName = name;
  }

  public String getLastName() {
    return Validator.check(lastName, "");
  }

  public void setLastName(String name) {
    lastName = name;
  }

  public String getLastInitial() {
    return Validator.check(lastInitial, "A");
  }

  public void setLastInitial(String init) {
    lastInitial = init;
  }

  public String getEmail() {
    return Validator.check(email,"");
  }
  
  public void setEmail(String email) {
	this.email = email;
  }
  
  public String getIdString() {
    String escapedIdString =  ContextUtil.escapeApostrophe(idString);
    return Validator.check(escapedIdString, "N/A");
  }

  public void setIdString(String id) {
    idString = id;
  }

  public String getRole() {
    return Validator.check(role, "N/A");
  }

  public void setRole(String newrole) {
    role = newrole;
  }

  public Date getSubmittedDate() {
    return submittedDate;
  }
  public void setSubmittedDate(Date submittedDate) {
    this.submittedDate = submittedDate;
  }

  public Date getAttemptDate() {
    return attemptDate;
  }
  public void setAttemptDate(Date attemptDate) {
    this.attemptDate = attemptDate;
  }

  public Boolean getIsLate() {
    return Validator.bcheck(isLate, false);
  }
  public void setIsLate(Boolean isLate) {
    this.isLate = isLate;
  }
  public Boolean getForGrade() {
    return Validator.bcheck(forGrade, true);
  }
  public void setForGrade(Boolean forGrade) {
    this.forGrade = forGrade;
  }
  public String getTotalAutoScore() {
    return getRoundedTotalAutoScore();
  }
  
  public String getExactTotalAutoScore() {
	    return Validator.check(totalAutoScore, "0").replace(',', '.');
  }

  public String getRoundedTotalAutoScore() {
   if (totalAutoScore!= null){	  
	   try {
		   String newscore = ContextUtil.getRoundedValue(totalAutoScore.replace(',', '.'), 2);
		   return Validator.check(newscore, "N/A").replace(',', '.');
	   }
	   catch (Exception e) {
		   // encountered some weird number format/locale
		   return Validator.check(totalAutoScore, "0").replace(',', '.');
	   }
    }
   else {
	   return Validator.check(totalAutoScore, "0").replace(',', '.');
   }
  }
  
  public void setTotalAutoScore(String totalAutoScore) {
	  if (totalAutoScore!= null){
		  this.totalAutoScore = totalAutoScore.replace(',', '.');
	  }
	  else {
		  this.totalAutoScore = null;
	  }
  }
  
  public String getTotalOverrideScore() {
    return Validator.check(totalOverrideScore, "0").replace(',', '.');
  }

  public String getRoundedTotalOverrideScore() {
		if (totalOverrideScore != null) {
			try {
				String newscore = ContextUtil.getRoundedValue(
						totalOverrideScore.replace(',', '.'), 2);
				return Validator.check(newscore, "N/A").replace(',', '.');
			} catch (Exception e) {
				// encountered some weird number format/locale
				return Validator.check(totalOverrideScore, "0").replace(',', '.');
			}
		} else {
			return Validator.check(totalOverrideScore, "0").replace(',', '.');
		}
	}
  
  public void setTotalOverrideScore(String totalOverrideScore) {
    if (totalOverrideScore!= null){
    	this.totalOverrideScore = totalOverrideScore.replace(',', '.');
    } else {
    	this.totalOverrideScore = null;
    }
  }

  public String getFinalScore() {
	  return Validator.check(finalScore, "0").replace(',', '.');
  }
  public String getRoundedFinalScore() {
	  if (finalScore!= null){
		  try {
			  String newscore = ContextUtil.getRoundedValue(finalScore.replace(',', '.'), 2);
			  return Validator.check(newscore, "N/A").replace(',', '.');
		  }
		  catch (Exception e) {
			  // encountered some weird number format/locale
			  return Validator.check(finalScore, "0").replace(',', '.');
		  }
	  }
	  else {
    	return Validator.check(finalScore, "0").replace(',', '.');
	  }
  }
  public void setFinalScore(String finalScore) {
	  if (finalScore!= null){
		  this.finalScore = finalScore.replace(',', '.');
	  }
	  else {
		    this.finalScore = null;
	  }
  }
  public String getAnswer() {
    return Validator.check(answer, "");
  }
  public void setAnswer(String answer) {
    this.answer = answer;
  }
  public String getComments() {
    return Validator.check(comments, "");
  }
  public void setComments(String comments) {
    this.comments = comments;
  }
  public String getGradedBy() {
    return Validator.check(gradedBy, "");
  }
  public void setGradedBy(String gradedBy) {
    this.gradedBy = gradedBy;
  }
  public Date getGradedDate() {
    return gradedDate;
  }
  public void setGradedDate(Date gradedDate) {
    this.gradedDate = gradedDate;
  }

  /**
   * In some cases, students are allowed to submit multiple assessment
   * for grading. However, the grader has the choice to select one to
   * represent how well the student does overall. status = 1 means
   * this submitted assessment is selected.
   */
  public Integer getStatus() {
    return status;
  }
  public void setStatus(Integer status) {
    this.status = status;
  }

  public Set getItemGradingSet() {
    return itemGradingSet;
  }

  public void setItemGradingSet(Set itemGradingSet) {
    this.itemGradingSet = itemGradingSet;
  }

  // added by daisy to support to display answers to file upload question
  public ArrayList getItemGradingArrayList() {
    return itemGradingArrayList;
  }

  public void setItemGradingArrayList(ArrayList itemGradingArrayList) {
    this.itemGradingArrayList = itemGradingArrayList;
  }


  public String getFullAnswer() {
    return Validator.check(fullAnswer,"");
    //return Validator.check(escFullAnswer, "");
  }
  public void setFullAnswer(String answer) {
    this.fullAnswer = answer;
  }

  public String getRationale() {
      // String unicodeRationale= ContextUtil.getStringInUnicode(rationale);
    return Validator.check(rationale,"");
    // return Validator.check(unicodeRationale, "");
  }
  public void setRationale(String param) {
    this.rationale= param;
  }

	public boolean getRetakeAllowed() {
		return this.retakeAllowed;
	}
	public void setRetakeAllowed(boolean retakeAllowed) {
		this.retakeAllowed = retakeAllowed;
	}
	
	public boolean getIsAutoSubmitted() {
		return this.isAutoSubmitted;
	}
	
	public void setIsAutoSubmitted(boolean isAutoSubmitted) {
		this.isAutoSubmitted = isAutoSubmitted;
	}
	
	public boolean getIsAttemptDateAfterDueDate() {
		return this.isAttemptDateAfterDueDate;
	}
	
	public void setIsAttemptDateAfterDueDate(boolean isAttemptDateAfterDueDate) {
		this.isAttemptDateAfterDueDate = isAttemptDateAfterDueDate;
	}

	public ItemGradingData getItemGrading() {
		return this.itemGrading;
	}
	public void setItemGrading(ItemGradingData itemGrading) {
		this.itemGrading = itemGrading;
	}
	
	public List getItemGradingAttachmentList() {
		return itemGradingAttachmentList;
	}

	public void setItemGradingAttachmentList(List attachmentList)
	{
		this.itemGradingAttachmentList = attachmentList;
	}

	private boolean hasItemGradingAttachment = false;
	public boolean getHasItemGradingAttachment(){
		if (itemGradingAttachmentList!=null && itemGradingAttachmentList.size() >0)
			this.hasItemGradingAttachment = true;
		return this.hasItemGradingAttachment;
	}
	
	public String addAttachmentsRedirect() {
		  // 1. redirect to add attachment
		  try	{
			  List filePickerList = new ArrayList();
			  if (itemGradingAttachmentList != null){
				  AttachmentUtil attachmentUtil = new AttachmentUtil();
				  filePickerList = attachmentUtil.prepareReferenceList(itemGradingAttachmentList);
			  }
			  ToolSession currentToolSession = SessionManager.getCurrentToolSession();
			  currentToolSession.setAttribute(FilePickerHelper.FILE_PICKER_ATTACHMENTS, filePickerList);
			  
			  currentToolSession.setAttribute("itemGradingId", itemGradingId);
			  ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			  context.redirect("sakai.filepicker.helper/tool");
		  }
		  catch(Exception e){
			  log.error("fail to redirect to attachment page: " + e.getMessage());
		  }
		  return "studentScores";
	  }
	
	public Integer getTimeElapsed() {
		return this.timeElapsed;
	}

	public void setTimeElapsed(Integer timeElapsed) {
		this.timeElapsed = timeElapsed;
	}
	
	public String getFormatedTimeElapsed() {
	    String timeElapsedInString = "n/a";
	    if (this.timeElapsed!=null && this.timeElapsed.intValue() >0)
	    {
	      int totalSec = this.timeElapsed.intValue();
	      int hr = totalSec / 3600;
	      int min = (totalSec % 3600)/60;
	      int sec = (totalSec % 3600)%60;
	      timeElapsedInString = "";
	      if (hr > 0) timeElapsedInString = hr + " hr ";
	      if (min > 0) timeElapsedInString = timeElapsedInString + min + " min ";
	      if (sec > 0) timeElapsedInString = timeElapsedInString + sec + " sec ";
	    }
	    return timeElapsedInString;	
	}	
}
