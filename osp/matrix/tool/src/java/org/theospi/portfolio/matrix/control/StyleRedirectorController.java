/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/osp/branches/sakai-2.8.1/matrix/tool/src/java/org/theospi/portfolio/matrix/control/StyleRedirectorController.java $
* $Id: StyleRedirectorController.java 59678 2009-04-03 23:20:50Z arwhyte@umich.edu $
***********************************************************************************
*
 * Copyright (c) 2006, 2008 The Sakai Foundation
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

package org.theospi.portfolio.matrix.control;

import java.util.Map;

import org.sakaiproject.metaobj.shared.mgt.IdManager;
import org.sakaiproject.metaobj.utils.mvc.intf.LoadObjectController;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.theospi.portfolio.matrix.MatrixManager;
import org.theospi.portfolio.matrix.model.ScaffoldingCell;
import org.theospi.portfolio.matrix.model.WizardPageDefinition;
import org.theospi.portfolio.style.StyleHelper;
import org.theospi.portfolio.style.model.Style;

public class StyleRedirectorController implements LoadObjectController {

   private MatrixManager matrixManager;
   private IdManager idManager = null;

   public ModelAndView handleRequest(Object requestModel, Map request, Map session, Map application, Errors errors) {
      String stylePickerAction = (String) request.get("stylePickerAction");
      String pageId = (String) session.get("pageDef_id");
      if (pageId == null) {
         pageId = (String) request.get("pageDef_id");
         session.put("pageDef_id", pageId);
      }      
      
      if (stylePickerAction != null) {
         String currentStyleId = (String)request.get("currentStyleId");
         if (currentStyleId != null)
            session.put(StyleHelper.CURRENT_STYLE_ID, currentStyleId);
         else
            session.remove(StyleHelper.CURRENT_STYLE_ID);
         
         session.put(StyleHelper.STYLE_SELECTABLE, "true");
         session.put("styleReturnView", request.get("styleReturnView"));
         
         return new ModelAndView("styleRedirector");
      }

      session.put(EditedScaffoldingStorage.STORED_SCAFFOLDING_FLAG, "true");
      String retView = (String)session.get("styleReturnView");
      session.remove("styleReturnView");
      return new ModelAndView(retView);
   }

   public Object fillBackingObject(Object incomingModel, Map request, Map session, Map application) throws Exception {
      String pageId = (String) request.get("pageDef_id");
      if (pageId == null) {
         pageId = (String) session.get("pageDef_id");
         //session.remove("page_id");
      }
      
      EditedScaffoldingStorage sessionBean = (EditedScaffoldingStorage)session.get(
            EditedScaffoldingStorage.EDITED_SCAFFOLDING_STORAGE_SESSION_KEY);
      ScaffoldingCell scaffoldingCell = sessionBean.getScaffoldingCell();
      WizardPageDefinition pageDef = scaffoldingCell.getWizardPageDefinition();
      
      if (session.get(StyleHelper.CURRENT_STYLE) != null) {
         Style style = (Style)session.get(StyleHelper.CURRENT_STYLE);
         pageDef.setStyle(style);
         session.remove(StyleHelper.CURRENT_STYLE);
      }
      else if (session.get(StyleHelper.UNSELECTED_STYLE) != null) {
         pageDef.setStyle(null);
         session.remove(StyleHelper.UNSELECTED_STYLE);
      }
      
      return null;
   }

   public IdManager getIdManager() {
      return idManager;
   }

   public void setIdManager(IdManager idManager) {
      this.idManager = idManager;
   }

   public MatrixManager getMatrixManager() {
      return matrixManager;
   }

   public void setMatrixManager(MatrixManager matrixManager) {
      this.matrixManager = matrixManager;
   }

}
