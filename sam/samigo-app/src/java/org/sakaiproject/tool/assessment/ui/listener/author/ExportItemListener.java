/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/listener/author/ExportItemListener.java $
 * $Id: ExportItemListener.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
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



package org.sakaiproject.tool.assessment.ui.listener.author;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.tool.assessment.ui.bean.qti.XMLController;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

/**
 * <p>Title: Samigo</p>
 * <p>Description: Sakai Assessment Manager</p>
 * @author Ed Smiley
 * @version $Id: ExportItemListener.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 */

public class ExportItemListener implements ActionListener
{
  private static Log log = LogFactory.getLog(ExportItemListener.class);
  private static ContextUtil cu;

  public ExportItemListener()
  {
  }

  public void processAction(ActionEvent ae) throws AbortProcessingException
  {
    log.info("Listener=ExportItemListener");
    String itemId = (String) cu.lookupParam("itemId");
    log.info("** item = "+itemId);
    XMLController xmlController = (XMLController) cu.lookupBean(
                                          "xmlController");
    xmlController.setId(itemId);
    xmlController.setQtiVersion(1);
    xmlController.displayItemXml();
  }

}


