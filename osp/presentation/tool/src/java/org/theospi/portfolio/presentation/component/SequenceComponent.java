/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/presentation/tool/src/java/org/theospi/portfolio/presentation/component/SequenceComponent.java $
* $Id:SequenceComponent.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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
package org.theospi.portfolio.presentation.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.theospi.jsf.intf.XmlDocumentContainer;
import org.theospi.portfolio.presentation.tool.DecoratedRegion;
import org.theospi.portfolio.presentation.tool.RegionSequenceMap;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Jan 1, 2006
 * Time: 5:48:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SequenceComponent extends HtmlDataTable implements XmlDocumentContainer {

   public static final String COMPONENT_TYPE = "org.theospi.presentation.SequenceComponent";

   private List childRegions = new ArrayList();

   public String getVariableName() {
      return getVar();
   }

   public void addRegion(ValueBinding region) {
      childRegions.add(region);
   }

   public Object getRowData() {
      RegionSequenceMap map = (RegionSequenceMap) super.getRowData();
      map.setChildRegions(childRegions);      
      return map;
   }

   public void addToSequence() {
      for (Iterator i=childRegions.iterator();i.hasNext();) {
         ValueBinding binding = (ValueBinding) i.next();
         DecoratedRegion region = (DecoratedRegion) binding.getValue(FacesContext.getCurrentInstance());
         region.getBase().addBlank();
         region.initRegionList();
      }
   }

}
