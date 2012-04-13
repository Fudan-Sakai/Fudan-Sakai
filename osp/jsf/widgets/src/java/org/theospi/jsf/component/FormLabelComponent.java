/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/osp/branches/sakai-2.8.1/jsf/widgets/src/java/org/theospi/jsf/component/FormLabelComponent.java $
* $Id: FormLabelComponent.java 59678 2009-04-03 23:20:50Z arwhyte@umich.edu $
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
package org.theospi.jsf.component;

import javax.faces.component.UIOutput;

public class FormLabelComponent extends UIOutput {
   
   private String valueRequired = "false";
   
   public FormLabelComponent()
   {
      super();
      this.setRendererType("org.theospi.FormLabel");
   }

   public String getValueRequired() {
      return valueRequired;
   }

   public void setValueRequired(String valueRequired) {
      this.valueRequired = valueRequired;
   }

}
