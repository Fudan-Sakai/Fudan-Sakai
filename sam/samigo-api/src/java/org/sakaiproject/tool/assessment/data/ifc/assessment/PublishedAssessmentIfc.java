/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-api/src/java/org/sakaiproject/tool/assessment/data/ifc/assessment/PublishedAssessmentIfc.java $
 * $Id: PublishedAssessmentIfc.java 65883 2009-08-17 18:44:33Z ktsao@stanford.edu $
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



package org.sakaiproject.tool.assessment.data.ifc.assessment;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Rachel Gollub
 * @version 1.0
 */
public interface PublishedAssessmentIfc
    extends Serializable, AssessmentIfc
{
  Long getPublishedAssessmentId();

  void setPublishedAssessmentId(Long publishedAssessmentId);

  //AssessmentIfc getAssessment();
  //void setAssessment(AssessmentIfc assessment);

  public Long getAssessmentId();

  public void setAssessmentId(Long assessmentId);

  Float getTotalScore();
  
  Date getLastNeedResubmitDate();

  void setLastNeedResubmitDate(Date lastNeedResubmitDate);
  
}
