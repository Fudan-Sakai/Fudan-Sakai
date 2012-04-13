/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/component/src/java/org/sakaiproject/tool/assessment/facade/AssessmentTemplateIteratorFacade.java $
 * $Id: AssessmentTemplateIteratorFacade.java 9273 2006-05-10 22:34:28Z daisyf@stanford.edu $
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

package org.sakaiproject.tool.assessment.facade;

import java.util.Collection;
import java.util.Iterator;

/**
 * A AssessmentTemplate iterator implementation.
 *
 * @author Rachel Gollub <rgollub@stanford.edu>
 */
public class AssessmentTemplateIteratorFacade
  //implements AssessmentTemplateIterator
{
  private Iterator assessmentTemplateIter;
  private int size = 0;

  /**
   * Creates a new AssessmentTemplateIteratorImpl object.
   *
   * @param passessmentTemplates DOCUMENTATION PENDING
   */
  public AssessmentTemplateIteratorFacade(Collection passessmentTemplates)
  {
    assessmentTemplateIter = passessmentTemplates.iterator();
    this.size = passessmentTemplates.size();
  }

  /**
   * DOCUMENTATION PENDING
   *
   * @return DOCUMENTATION PENDING
   *
   * @throws DataFacadeException DOCUMENTATION PENDING
   */
  public boolean hasNextAssessmentTemplate()
    throws DataFacadeException
  {
    try{
      return assessmentTemplateIter.hasNext();
    }
    catch(Exception e){
      throw new DataFacadeException("No objects to return.");
    }
  }

  /**
   * DOCUMENTATION PENDING
   *
   * @return DOCUMENTATION PENDING
   *
   * @throws DataFacadeException DOCUMENTATION PENDING
   */
  public AssessmentTemplateFacade nextAssessmentTemplate()
    throws DataFacadeException
  {
    try
    {
      return (AssessmentTemplateFacade) assessmentTemplateIter.next();
    }
    catch(Exception e)
    {
      throw new DataFacadeException("No objects to return.");
    }
  }

  public int getSize(){
    return size;
  }

}
