/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/bean/util/Validator.java $
 * $Id: Validator.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
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



package org.sakaiproject.tool.assessment.ui.bean.util;

/**
 * This class holds methods to make sure the JSF validator is happy.
 */
public class Validator {

  public static String check(String mytest, String mydefault)
  {
    if (mytest == null || mytest.trim().equals(""))
      return mydefault;
    return mytest;
  }

  public static Boolean bcheck(Boolean mytest, boolean mydefault)
  {
    if (mytest == null)
      return Boolean.valueOf(mydefault);
    return mytest;
  }
}
