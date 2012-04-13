/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-api/src/java/org/sakaiproject/tool/assessment/data/ifc/grading/AssessmentGradingIfc.java $
 * $Id: AssessmentGradingIfc.java 65883 2009-08-17 18:44:33Z ktsao@stanford.edu $
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



package org.sakaiproject.tool.assessment.data.ifc.grading;
import java.util.Date;
import java.util.Set;

public interface AssessmentGradingIfc
    extends java.io.Serializable{

	// status = 0: begin a new assessment
	// status = 1: submit but not grade yet
	// status = 2: grader has went to total score page and graded + AUTO_GRADED
	// status = 3: grader has went to total score page and graded + at least one question NEED_HUMAN_ATTENTION
	// status = 4: the assessment has be republished. This assessment has been submitted. Therefore, this it needs to be resubmit
	// status = 5: there is no submission but grader update something in the score page
	// status = 6: the assessment has be republished. This assessment has begun but not yet been submitted (saved/in progress). Therefore, just warn the student about the update ("resubmit" is not applicable here).

    // Because of SAK-16456, we no longer need to show the auto/human graded status per submission, I don't think we 
	// need to distinguish status 2 and 3 anymore. But I just leave them here...
	public static final Integer SUBMITTED = Integer.valueOf(1);
	public static final Integer AUTO_GRADED = Integer.valueOf(2);
	public static final Integer NEED_HUMAN_ATTENTION = Integer.valueOf(3);
	public static final Integer ASSESSMENT_UPDATED_NEED_RESUBMIT = Integer.valueOf(4);
	public static final Integer NO_SUBMISSION = Integer.valueOf(5);
	public static final Integer ASSESSMENT_UPDATED = Integer.valueOf(6);

  Long getAssessmentGradingId();

  void setAssessmentGradingId(Long assessmentGradingId);

  Long getPublishedAssessmentId();

  void setPublishedAssessmentId(Long publishedAssessmentId);

  String getAgentId();

  void setAgentId(String agentId);

  //AgentIfc getAgent();

  Date getSubmittedDate();

  void setSubmittedDate(Date submittedDate);

  // Is isLate determined by comparing the submitted date with the duedate
  // of published assessment or core assessment?
  // if the former, then we need to store the duedate info in DB
  // if latter, isLate is determined on the fly -
  // 'cos core assessment due date can be changed.
  Boolean getIsLate();

  void setIsLate(Boolean isLate);

  Boolean getForGrade();

  void setForGrade(Boolean forGrade);

  // sum of item score through auto scoring
  Float getTotalAutoScore();

  void setTotalAutoScore(Float totalAutoScore);

  // sum of item score through instructor grading
  Float getTotalOverrideScore();

  void setTotalOverrideScore(Float totalOverrideScore);

  // grader can override the total score with a final score
  Float getFinalScore();

  void setFinalScore(Float finalScore);

  String getComments();

  void setComments(String comments);

  String getGradedBy();

  void setGradedBy(String GradedBy);

  Date getGradedDate();

  void setGradedDate(Date GradedDate);

  Integer getStatus();

  void setStatus(Integer status);

  Set getItemGradingSet();

  void setItemGradingSet(Set itemGradingSet);

  Date getAttemptDate();

  void setAttemptDate(Date attemptDate);

  Integer getTimeElapsed();

  void setTimeElapsed(Integer timeElapsed);

}
