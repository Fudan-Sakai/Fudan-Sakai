<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- $Id: reviewAssessment.jsp 59363 2009-03-31 17:48:38Z arwhyte@umich.edu $
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
      <title><h:outputText value="#{authorMessages.review_assmt}" /></title>
      </head>
      <body onload="<%= request.getAttribute("html.body.onload") %>">
<!-- content... -->
<!-- simplified somewhat on logic of displays, and no feedback yet -->
<h3><h:outputText value="#{authorMessages.review_assmt}" /></h3>
<div class="tier1">
  <h3 style="insColor insBak">
   <h:outputText  value="#{authorMessages.no_feedback_assessmt}"
     rendered="#{delivery.showNoFeedback}"/>
   <h:outputText  value="#{authorMessages.feedback_avail_on}"
     rendered="#{delivery.showDateFeedback}"/>
   <h:outputText  value="This is immediate feedback."
     rendered="#{delivery.showImmediateFeedback}"/>
  </h3>
  <h:form id="reviewForm">
   <h:commandButton value="#{authorMessages.button_return}" type="submit"
     style="act" action="select" />
  </h:form>
</div>
<!-- end content -->

      </body>
    </html>
  </f:view>

