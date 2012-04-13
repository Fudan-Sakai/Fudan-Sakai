/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-api/src/java/org/sakaiproject/tool/assessment/data/ifc/assessment/AnswerIfc.java $
 * $Id: AnswerIfc.java 69050 2009-11-16 23:16:32Z ktsao@stanford.edu $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public  interface AnswerIfc
    extends java.io.Serializable
{
  Long getId();

  void setId(Long id);

  ItemTextIfc getItemText();

  void setItemText(ItemTextIfc itemText);

  ItemDataIfc getItem();

  void setItem(ItemDataIfc item) ;

  String getText();

  void setText(String text);

  Long getSequence();

  void setSequence(Long sequence);

  String getLabel();

  void setLabel(String label);

  Boolean getIsCorrect();

  void setIsCorrect(Boolean isCorrect);

  Float getDiscount();
  
  void setDiscount(Float discount);
  
  String getGrade();

  void setGrade(String grade);

  Float getScore();

  void setScore(Float score);
  
  // to incorporate partial credit we need to add field for partial credit
  void setPartialCredit(Float partialCredit);
  
  Float getPartialCredit();

  Set getAnswerFeedbackSet();

  ArrayList getAnswerFeedbackArray();

  void setAnswerFeedbackSet(Set answerFeedbackSet);

  String getAnswerFeedback(String typeId);

  HashMap getAnswerFeedbackMap();

  String getCorrectAnswerFeedback();

  String getInCorrectAnswerFeedback();

  String getGeneralAnswerFeedback();

}
