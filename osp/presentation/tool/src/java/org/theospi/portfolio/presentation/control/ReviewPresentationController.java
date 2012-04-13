/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/osp/branches/sakai-2.8.1/presentation/tool/src/java/org/theospi/portfolio/presentation/control/ReviewPresentationController.java $
* $Id: ReviewPresentationController.java 71426 2010-01-13 17:25:00Z bkirschn@umich.edu $
***********************************************************************************
*
 * Copyright (c) 2009 The Sakai Foundation
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

/**
 * 
 */
package org.theospi.portfolio.presentation.control;

import java.util.Map;

import org.sakaiproject.metaobj.shared.model.Agent;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.theospi.portfolio.presentation.support.PresentationService;
import org.theospi.portfolio.presentation.model.Presentation;

/**
 ** This class supports setting/unsetting the Presentation.isDefault flag
 ** to indicate whether it has been reviewed or vetted. 
 **/
public class ReviewPresentationController extends ListPresentationController {
   
   /* (non-Javadoc)
    * @see org.sakaiproject.metaobj.utils.mvc.intf.Controller#handleRequest(java.lang.Object, java.util.Map, java.util.Map, java.util.Map, org.springframework.validation.Errors)
    */
   public ModelAndView handleRequest(Object requestModel, Map request, Map session, Map application, Errors errors) {
      Agent current = getAuthManager().getAgent();
      String reviewAction = (String)request.get("review");
      String presentationId = (String)request.get("id");
		Presentation presentation = presentationService.getPresentation(presentationId, false);
      
      if ("true".equals(reviewAction)) {
         presentation.setIsDefault( true );
      }
      else {
         presentation.setIsDefault( false );
      }
      
      presentationService.savePresentation( presentation, false, false );
      
      return super.handleRequest(requestModel, request, session, 
                                 application, errors);
   }
}
