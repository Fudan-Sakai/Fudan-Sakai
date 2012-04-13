<!--
* $Id: assessment_attachment.jsp 6874 2006-03-22 17:01:47Z hquinn@stanford.edu $
<%--
***********************************************************************************
*
* Copyright (c) 2006 The Sakai Foundation.
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
***********************************************************************************/
--%>
-->
<!-- 2a ATTACHMENTS -->
 <f:verbatim><div class="tier1"></f:verbatim>
 <h:panelGroup rendered="#{delivery.hasAttachment}">
 	<f:verbatim><p></p></f:verbatim>
 </h:panelGroup>
 <h:outputLabel value="#{deliveryMessages.attachments}" rendered="#{delivery.hasAttachment}"/>
  <f:verbatim><br/></f:verbatim>
  <h:panelGroup rendered="#{delivery.hasAttachment}">
    <h:dataTable value="#{delivery.attachmentList}" var="attach" border="0">
      <h:column rendered="#{!attach.isMedia}">
        <%@ include file="/jsf/shared/mimeicon.jsp" %>
      </h:column>
	  <h:column>
        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
        <h:outputText escape="false" value="
	      <embed src=\"#{delivery.protocol}/samigo-app/servlet/ShowAttachmentMedia?actionMode=preview&resourceId=escape(#{attach.encodedResourceId})&mimeType=#{attach.mimeType}&filename=#{attach.filename}\" volume=\"50\" height=\"350\" width=\"400\" autostart=\"false\"/>" rendered="#{attach.isInlineVideo}"/>
        <h:outputText escape="false" value="
	      <embed src=\"#{delivery.protocol}/samigo-app/servlet/ShowAttachmentMedia?actionMode=preview&resourceId=#{attach.encodedResourceId}&mimeType=#{attach.mimeType}&filename=#{attach.filename}\" height=\"350\" width=\"400\"/>" rendered="#{attach.isInlineFlash}"/>
	    <h:outputText escape="false" value="
	      <img src=\"#{delivery.protocol}/samigo-app/servlet/ShowAttachmentMedia?actionMode=preview&resourceId=#{attach.encodedResourceId}&mimeType=#{attach.mimeType}&filename=#{attach.filename}\" />" rendered="#{attach.isInlineImage}"/>
        <h:outputLink value="#{attach.location}" target="new_window" rendered="#{!attach.isMedia}">
          <h:outputText escape="false" value="#{attach.filename}" />
        </h:outputLink>
      </h:column>
      <h:column>
        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
        <h:outputText escape="false" value="#{attach.fileSize} #{generalMessages.kb}" rendered="#{!attach.isLink && !attach.isMedia}"/>
      </h:column>
    </h:dataTable>
  </h:panelGroup>

<f:verbatim></div></f:verbatim>

