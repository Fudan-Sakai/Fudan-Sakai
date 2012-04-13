

<%-- $Id: displayFileUploadAnswer.jsp 6874 2006-03-22 17:01:47Z hquinn@stanford.edu $
include file for displaying file upload questions
should be included in file importing DeliveryMessages
--%>
<!--
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

<h:panelGroup>
  <h:dataTable value="#{description.itemGradingArrayList}" var="itemGradingArrayList" cellpadding="10">
    <h:column>
      <h:outputText value="#{description.answer}" escape="false" rendered="#{itemGradingArrayList.mediaSize == 0}" />
	  <h:dataTable value="#{itemGradingArrayList.mediaArray}" var="media">
	    <h:column>

        <h:outputText escape="false" value="
        	<object>
        		<param name=\"autostart\" value=\"false\"/>
        		<param name=\"autoplay\" value=\"false\"/>
        		<param name=\"controller\" value=\"true\"/>         
        		<embed src=\"/samigo-app/servlet/ShowMedia?mediaId=#{media.mediaId}\" volume=\"50\" height=\"25\" width=\"300\" autostart=\"false\" autoplay=\"false\" controller=\"true\" type=\"audio/basic\"/>
        	</object>" 
        />

	  <f:verbatim><br /></f:verbatim>
      <h:outputText value="#{evaluationMessages.open_bracket}"/>
      <h:outputText value="#{media.duration} sec, recorded on " rendered="#{!media.durationIsOver}" />
      <h:outputText value="#{media.timeAllowed} sec, recorded on " rendered="#{media.durationIsOver}" />
	  <h:outputText value="#{media.createdDate}">
        <f:convertDateTime pattern="#{evaluationMessages.grading_date_no_time_format}" />
      </h:outputText>
      <h:outputText value="#{evaluationMessages.close_bracket}"/>
      <f:verbatim><br /></f:verbatim>
	  
	  <div>
	  <h:outputFormat value=" #{deliveryMessages.can_you_hear}" escape="false">
		<f:param value="<a href=\"#{author.protocol}/samigo-app/servlet/ShowMedia?mediaId=#{media.mediaId}&setMimeType=false\"/> #{deliveryMessages.can_you_hear_2}</a>" />
      </h:outputFormat>
      </div>

     </h:column>
  </h:dataTable>
       </h:column>
  </h:dataTable>
</h:panelGroup>
