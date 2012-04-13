<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
* $Id: removeAssessment.jsp 22618 2007-03-14 19:58:35Z ktsao@stanford.edu $
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
      <title><h:outputText value="#{authorMessages.remove_assessment_co}" /></title>
      </head>
      <body onload="<%= request.getAttribute("html.body.onload") %>">

<div class="portletBody">
 <!-- content... -->
 <h:form id="removeAssessmentForm">
   <h:inputHidden id="assessmentId" value="#{publishedassessment.assessmentId}"/>
   <h3><h:outputText  value="#{authorMessages.remove_assessment_co}" /></h3>
   <div class="validation tier1">
          <h:outputText value="#{authorMessages.cert_rem_assmt} \"#{publishedassessment.title}\" ?" />
  </div>
<p class="act">
       <h:commandButton value="#{commonMessages.remove_action}" type="submit"
         styleClass="active" action="removeAssessment" >
          <f:actionListener
            type="org.sakaiproject.tool.assessment.ui.listener.author.RemovePublishedAssessmentListener" />
       </h:commandButton>
       <h:commandButton value="#{commonMessages.cancel_action}" type="submit"
         action="author" />
    </p>
 </h:form>
 <!-- end content -->
</div>

      </body>
    </html>
  </f:view>
