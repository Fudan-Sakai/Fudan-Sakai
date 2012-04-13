/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/bean/evaluation/HistogramBarBean.java $
 * $Id: HistogramBarBean.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
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



package org.sakaiproject.tool.assessment.ui.bean.evaluation;

import java.io.Serializable;

import org.sakaiproject.tool.assessment.ui.bean.util.Validator;

/**
 * @version $Id: HistogramBarBean.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 * @author Ed Smiley
 */
public class HistogramBarBean
  implements Serializable
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -4501893986013465144L;
private int numStudents;
  private String numStudentsText;
  private String columnHeight;
  private String rangeInfo;
  private String label;
  private Boolean isCorrect;

  /**
    *
    * @param numStudents int
    */
   public void setNumStudents(int numStudents)
   {
     this.numStudents = numStudents;
   }


   /**
    *
    * @return int
    */
   public int getNumStudents()
   {
     return this.numStudents;
   }

   /**
    *
    * @param pStudents String
    */
   public void setNumStudentsText(String pStudents)
   {
     numStudentsText = pStudents;
   }

   /**
    *
    * @return String
    */
   public String getNumStudentsText()
   {
     return Validator.check(numStudentsText, "N/A");
   }

   /**
    *
    * @param columnHeight String
    */
   public void setColumnHeight(String columnHeight)
   {
     this.columnHeight = columnHeight;
   }

   /**
    *
    * @return String
    */
   public String getColumnHeight()
   {
     return Validator.check(this.columnHeight, "0");
   }

   /**
    *
    * @param range String
    */
   public void setRangeInfo(String range){
     this.rangeInfo = range;
   }

   /**
    *
    * @return String
    */
   public String getRangeInfo()
   {
     return Validator.check(this.rangeInfo, "N/A");
   }

   /**
    *
    * @param range String
    */
   public void setLabel(String plabel){
     label = plabel;
   }

   /**
    *
    * @return String
    */
   public String getLabel()
   {
     return Validator.check(label, "N/A");
   }

   /**
    * @param range String
    */

   public void setIsCorrect(Boolean pcorrect){
     isCorrect = pcorrect;
   }

   /**
    *
    * @return String
    */
   public Boolean getIsCorrect()
   {
     return Validator.bcheck(isCorrect, false);
   }


}
