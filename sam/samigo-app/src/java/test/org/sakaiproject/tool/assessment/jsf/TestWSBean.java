/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/test/org/sakaiproject/tool/assessment/jsf/TestWSBean.java $
* $Id: TestWSBean.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
***********************************************************************************
*
 * Copyright (c) 2005, 2006, 2008 The Sakai Foundation
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
package test.org.sakaiproject.tool.assessment.jsf;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sakaiproject.tool.assessment.facade.ItemFacade;
import org.sakaiproject.tool.assessment.services.ItemService;
import org.sakaiproject.tool.assessment.ui.bean.delivery.ItemContentsBean;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;


/**
 * <p>Description: Test Bean with some properties</p>
 */

public class TestWSBean
  implements Serializable
{
  private static Log log = LogFactory.getLog(TestWSBean.class);
  private String itemid;
  private String itembankxml;

  public TestWSBean()
  {
    itemid = "itemid";
  }

  public String getItemid()
  {
    ItemContentsBean itemContentsBean = (ItemContentsBean) ContextUtil.lookupBean("itemContents");
    String itemId = (String)  FacesContext.getCurrentInstance().
        getExternalContext().getRequestParameterMap().get("itemid");
     log.debug("Getting Item Id=  "+ itemId);
    if (itemId != null && !itemId.equals(""))
    {
     ItemService itemService = new ItemService();
     ItemFacade item = itemService.getItem(itemId);
     itemContentsBean.setItemData(item.getData());
    }
    return itemid;
  }

  public void setItemid(String p)
  {
    itemid= p;
  }

  public void setItembankxml(String p)
  {
    itembankxml= p;
  }


}
