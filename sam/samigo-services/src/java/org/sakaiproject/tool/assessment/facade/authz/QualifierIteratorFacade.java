/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/component/src/java/org/sakaiproject/tool/assessment/facade/authz/QualifierIteratorFacade.java $
 * $Id: QualifierIteratorFacade.java 9273 2006-05-10 22:34:28Z daisyf@stanford.edu $
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

package org.sakaiproject.tool.assessment.facade.authz;

import java.util.Collection;
import java.util.Iterator;

import org.sakaiproject.tool.assessment.data.ifc.authz.QualifierIfc;
import org.sakaiproject.tool.assessment.data.ifc.authz.QualifierIteratorIfc;
import org.sakaiproject.tool.assessment.facade.DataFacadeException;

public class QualifierIteratorFacade
  implements QualifierIteratorIfc
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -8412047177198953937L;
private Iterator qualifierIter;

  public QualifierIteratorFacade(Collection c)
  {
    qualifierIter = c.iterator();
  }

  public boolean hasNextQualifier()
    throws DataFacadeException
  {
    try{
      return qualifierIter.hasNext();
    }
    catch(Exception e){
      throw new DataFacadeException(e.getMessage());
    }
  }

  public QualifierIfc nextQualifier()
    throws DataFacadeException
  {
    try {
      return (QualifierIfc)qualifierIter.next();
    }
    catch (Exception e) {
      throw new DataFacadeException(e.getMessage());
    }
  }

}
