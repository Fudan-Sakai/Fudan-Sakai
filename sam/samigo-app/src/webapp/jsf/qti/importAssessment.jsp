<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<%@ taglib uri="http://java.sun.com/upload" prefix="corejsf" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- $Id: importAssessment.jsp 84754 2010-11-15 20:17:34Z ktsao@stanford.edu $
<%--
***********************************************************************************
*
* Copyright (c) 2004, 2005, 2006 The Sakai Foundation.
*
* Licensed under the Educational Community License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.osedu.org/licenses/ECL-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License. 
*
**********************************************************************************/
--%>
-->
  <f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{authorImportExport.import_a}" /></title>
      </head>
      <body onload="<%= request.getAttribute("html.body.onload") %>">
 <div class="portletBody">
 <!-- content... -->
 <h:form id="importAssessmentForm" enctype="multipart/form-data">
    <h3><h:outputText  value="#{authorImportExport.import_a}" /></h3>
    <div class="tier1">
     <div class="form_label">
      <h:messages infoClass="validation" warnClass="validation" errorClass="validation" fatalClass="validation"/>
      <h:outputText value="#{authorImportExport.import_instructions}"/>
    </div>
    <br />
   <div class="tier2">
   <h:panelGrid columns="1">
     <h:panelGroup>
       <h:outputLabel  styleClass="form_label" value="#{authorImportExport.choose_file} "/>
       <%-- target represents location where import will be temporarily stored
            check valueChangeListener for final destination --%>
       <corejsf:upload target="jsf/upload_tmp/qti_imports/#{person.id}" valueChangeListener="#{xmlImport.importAssessment}"/>
     </h:panelGroup>
     <f:verbatim>&nbsp;</f:verbatim>
     <h:panelGrid columns="1">
       <h:outputText value="#{authorImportExport.file_is}"/>
       <h:panelGrid columns="2">
         <f:verbatim>&nbsp;</f:verbatim>
         <h:selectOneRadio id="sourceType" layout="pageDirection" value="1">
           <f:selectItem itemLabel="#{authorImportExport.from_samigo}" itemValue="1"/>
           <f:selectItem itemLabel="#{authorImportExport.from_respondus}" itemValue="2"/>
         </h:selectOneRadio>
       </h:panelGrid>
     </h:panelGrid>
   </h:panelGrid>
   </div>
    <br/>
    <br/>
     <%-- activates the valueChangeListener --%>
     <h:commandButton value="#{authorImportExport.import_action}" type="submit" style="act" action="author"/>
     <%-- immediate=true bypasses the valueChangeListener --%>
     <h:commandButton value="#{commonMessages.cancel_action}" type="submit"
       style="act" action="author" immediate="true"/>
  </div>

 </h:form>
</div>
 <!-- end content -->
      </body>
    </html>
  </f:view>
  
