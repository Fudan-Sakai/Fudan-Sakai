/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-api/src/java/org/sakaiproject/tool/assessment/data/ifc/assessment/AssessmentMetaDataIfc.java $
 * $Id: AssessmentMetaDataIfc.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
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

public interface AssessmentMetaDataIfc
    extends java.io.Serializable
{
  public static String AUTHORS = "ASSESSMENT_AUTHORS";
  public static String KEYWORDS = "ASSESSMENT_KEYWORDS";
  public static String OBJECTIVES = "ASSESSMENT_OBJECTIVES";
  public static String RUBRICS = "ASSESSMENT_RUBRICS";
  public static String BGCOLOR = "ASSESSMENT_BGCOLOR";
  public static String BGIMAGE = "ASSESSMENT_BGIMAGE";
  public static String ALIAS = "ALIAS";

  Long getId();

  void setId(Long id);

  AssessmentBaseIfc getAssessment();

  void setAssessment(AssessmentBaseIfc assessment);

  String getLabel();

  void setLabel(String label);

  String getEntry();

  void setEntry(String entry);

}
