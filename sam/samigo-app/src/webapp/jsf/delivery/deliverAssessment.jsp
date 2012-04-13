<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<%@ taglib uri="http://java.sun.com/upload" prefix="corejsf" %>
<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!--
* $Id: deliverAssessment.jsp 97832 2011-09-06 16:37:49Z ktsao@stanford.edu $
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
	  <script type="text/javascript" language="JavaScript" src="/samigo-app/js/saveForm.js"></script>
	  	  
      <title> <h:outputText value="#{delivery.assessmentTitle}"/>
      </title>
      </head>
	
      <body onload="<%= request.getAttribute("html.body.onload") %>; checkRadio(); setLocation();SaveFormContentAsync('deliverAssessment', 'takeAssessmentForm', 'takeAssessmentForm:save', 'takeAssessmentForm:lastSubmittedDate1', 'takeAssessmentForm:lastSubmittedDate2',  <h:outputText value="#{delivery.autoSaveRepeatMilliseconds}"/>, <h:outputText  value="#{delivery.actionString=='takeAssessment'}"/>);" >
  
      <h:outputText value="<a name='top'></a>" escape="false" />
      
      <script type="text/javascript" language="JavaScript" src="/samigo-app/js/jquery-1.3.2.min.js"></script>
      <script type="text/javascript" language="JavaScript" src="/samigo-app/js/jquery-ui-1.7.2.custom.min.js"></script>
      <script type="text/javascript" language="JavaScript" src="/samigo-app/js/jquery.blockUI-2.31.js"></script>
      <link type="text/css" href="/samigo-app/css/ui-lightness/jquery-ui-1.7.2.custom.css" rel="stylesheet" media="all"/>
      
      <script type="text/javascript">
		$(document).ready(function(){
		
			$('input[type=submit]').click(function() { 
				$.blockUI({ message: '',
							overlayCSS: { 
								backgroundColor: '#ff0',
								opacity: 0}
				}); 

			}); 
			
			$('#timer-warning').dialog({
				autoOpen: false,
				width: 400,
				modal: true,
				resizable: false,
				draggable: false
			});
			
		});

		function showTimerWarning() {
			$('#timer-warning').dialog('open');
			return false;
		}		
      </script>
      
      <div id="timer-warning" style="display:none;">
      	<h3><h:outputText value="#{deliveryMessages.five_minutes_left1}" /></h3>
      	<p><h:outputText value="#{deliveryMessages.five_minutes_left2}" /></p>
      </div>
 
 <h:outputText value="<div class='portletBody' style='#{delivery.settings.divBgcolor};#{delivery.settings.divBackground}'>" escape="false"/>
      
<!-- content... -->
<h:form id="takeAssessmentForm" enctype="multipart/form-data"
   onsubmit="saveTime()">

<!-- JAVASCRIPT -->
<%@ include file="/js/delivery.js" %>

<script language="javascript" type="text/JavaScript">

function checkRadio()
{
  for (i=0; i<document.forms[0].elements.length; i++)
  {
    if (document.forms[0].elements[i].type == "radio")
    {
      if (document.forms[0].elements[i].defaultChecked == true)
      {
        document.forms[0].elements[i].click();
      }
    }
  }
}

var formatByQuestion = <h:outputText value="#{delivery.settings.formatByQuestion}" />;
function setLocation()
{
// reset questionindex to avoid a Safari bug
	partIndex = document.forms[0].elements['takeAssessmentForm:partIndex'].value;
	questionIndex = document.forms[0].elements['takeAssessmentForm:questionIndex'].value;
 	if (!formatByQuestion)
           document.forms[0].elements['takeAssessmentForm:questionIndex'].value = "0";

	formatByPart = document.forms[0].elements['takeAssessmentForm:formatByPart'].value;
	formatByAssessment = document.forms[0].elements['takeAssessmentForm:formatByAssessment'].value;
    //alert("partIndex = " + partIndex);
    //alert("questionIndex = " + questionIndex);
	//alert("formatByPart = " + formatByPart);
	//alert("formatByAssessment = " + formatByAssessment);
	// We don't want to set the location when the index points to fist question on the page
	// We only set the location in following cases:
	// 1. If it is formatByPart, we set the location when it is not the first question of each part
	// 2. If it is formatByAssessment, we set the location when:
	//    a. it is not the first question of the first part
	//    b. it is a question in any parts other than the first one
	if ((formatByPart == 'true' && questionIndex != 0) || (formatByAssessment == 'true' && ((partIndex == 0 && questionIndex !=0) || partIndex != 0))) {
		window.location = '#p' + ++partIndex + 'q' + ++questionIndex;
	}
}

function noenter(){
return!(window.event && window.event.keyCode == 13);
}

function saveTime()
{
  if((typeof (document.forms[0].elements['takeAssessmentForm:assessmentDeliveryHeading:elapsed'])!=undefined) && ((document.forms[0].elements['takeAssessmentForm:assessmentDeliveryHeading:elapsed'])!=null) ){
  pauseTiming = 'false';
  document.forms[0].elements['takeAssessmentForm:assessmentDeliveryHeading:elapsed'].value=loaded/10;
 }
}

function disableRationale(){
	var textAreas = document.getElementsByTagName("textarea");
	//alert(textAreas[0].id);
	//alert(textAreas[0].id.endsWith('rationale'));
	if (textAreas.length == 1 && textAreas[0].id.endsWith('rationale')) {
		textAreas[0].disabled = true;
	}
}

function enableRationale(){
	var textAreas = document.getElementsByTagName("textarea");
	//alert(textAreas[0].id);
	//alert(textAreas[0].id.endsWith('rationale'));
	if (textAreas.length == 1 && textAreas[0].id.endsWith('rationale')) {
		textAreas[0].disabled = false;
	}

	/* Somehow the following for-loop becomes an infinite look of enableRationale(). No time to look into this now. Use above work around. 
	   Should come back later to figure out the reason.
	for(i=0; i < textAreas.length; i++){
		alert(i);
		if (textAreas[i].id.endsWith('rationale')) {
        textAreas[i].disabled = false;
		return;
		}
    }
	*/
}

// modified from tompuleo.com
String.prototype.endsWith = function(txt)
{
  var rgx;
  rgx = new RegExp(txt+"$");

  return this.match(rgx) != null; 
}

</script>


<h:inputHidden id="partIndex" value="#{delivery.partIndex}"/>
<h:inputHidden id="questionIndex" value="#{delivery.questionIndex}"/>
<h:inputHidden id="formatByPart" value="#{delivery.settings.formatByPart}"/>
<h:inputHidden id="formatByAssessment" value="#{delivery.settings.formatByAssessment}"/>
<h:inputHidden id="lastSubmittedDate1" value="#{delivery.assessmentGrading.submittedDate.time}" 
   rendered ="#{delivery.assessmentGrading.submittedDate!=null}"/>
<h:inputHidden id="lastSubmittedDate2" value="0"
   rendered ="#{delivery.assessmentGrading.submittedDate==null}"/>

<!-- DONE BUTTON FOR PREVIEW -->
<h:panelGroup rendered="#{delivery.actionString=='previewAssessment'}">
 <f:verbatim><div class="validation"></f:verbatim>
     <h:outputText value="#{deliveryMessages.ass_preview}" />
     <h:commandButton id="done" value="#{deliveryMessages.done}" action="#{person.cleanResourceIdListInPreview}" type="submit"/>
 <f:verbatim></div></f:verbatim>
</h:panelGroup>


<!-- HEADING -->
<f:subview id="assessmentDeliveryHeading">
<%@ include file="/jsf/delivery/assessmentDeliveryHeading.jsp" %>
</f:subview>

<!-- FORM ... note, move these hiddens to whereever they are needed as fparams-->
<h:messages infoClass="validation" warnClass="validation" errorClass="validation" fatalClass="validation"/>
<h:inputHidden id="assessmentID" value="#{delivery.assessmentId}"/>
<h:inputHidden id="assessTitle" value="#{delivery.assessmentTitle}" />
<!-- h:inputHidden id="ItemIdent" value="#{item.ItemIdent}"/ -->
<!-- h:inputHidden id="ItemIdent2" value="#{item.itemNo}"/ -->
<!-- h:inputHidden id="currentSection" value="#{item.currentSection}"/ -->
<!-- h:inputHidden id="insertPosition" value="#{item.insertPosition}"/ -->
<%-- PART/ITEM DATA TABLES --%>

<h:panelGrid columns="1" width="100%" rendered="#{delivery.pageContents.isNoParts && delivery.navigation eq '1'}" border="0">
      <h:outputText value="#{deliveryMessages.linear_no_contents_warning_1}"/>
      <h:outputText value="#{deliveryMessages.linear_no_contents_warning_2}" escape="false"/>
      <h:outputText value="#{deliveryMessages.linear_no_contents_warning_3}" escape="false"/>
</h:panelGrid>

<h:panelGroup rendered="#{!delivery.pageContents.isNoParts || delivery.navigation ne '1'}">
<f:verbatim><div class="tier1"></f:verbatim>
  <h:dataTable width="100%" value="#{delivery.pageContents.partsContents}" var="part" border="0">
    <h:column>
     <!-- f:subview id="parts" -->
      <f:verbatim><h4></f:verbatim>
      <h:panelGrid columns="2" width="100%" columnClasses="navView,navList">
       <h:panelGroup>
      <h:outputText value="#{deliveryMessages.p} #{part.number} #{deliveryMessages.of} #{part.numParts}" />
      <h:outputText value=" #{deliveryMessages.dash} #{part.nonDefaultText}" escape="false"/>
         </h:panelGroup>
      <!-- h:outputText value="#{part.unansweredQuestions}/#{part.questions} " / -->
      <!-- h:outputText value="#{deliveryMessages.ans_q}, " / -->
      <h:outputText value="#{part.pointsDisplayString} #{part.maxPoints} #{deliveryMessages.pt}" 
         rendered="#{delivery.actionString=='reviewAssessment'}"/>
</h:panelGrid>
      <f:verbatim></h4></f:verbatim>
      <h:outputText value="#{part.description}" escape="false"/>

  <!-- PART ATTACHMENTS -->
  <%@ include file="/jsf/delivery/part_attachment.jsp" %>
   <f:verbatim><div class="tier2"></f:verbatim>

   <h:outputText value="#{deliveryMessages.no_question}" escape="false" rendered="#{part.noQuestions}"/>

      <h:dataTable width="100%" value="#{part.itemContents}" var="question">
        <h:column>
<f:verbatim><h5></f:verbatim>
<h:panelGrid columns="2" width="100%" columnClasses="navView,navList">
         <h:panelGroup>
           <h:outputText value="<a name='p#{part.number}q#{question.number}'></a>" escape="false" />

        <h:outputText value="#{deliveryMessages.q} #{question.sequence} #{deliveryMessages.of} #{part.numbering}"/>
</h:panelGroup>
<h:panelGroup>
<h:outputText value=" #{question.pointsDisplayString} #{question.maxPoints} #{deliveryMessages.pt}" rendered="#{delivery.actionString=='reviewAssessment'}"/>

        <h:outputText value="#{question.maxPoints} #{deliveryMessages.pt}" rendered="#{delivery.actionString!='reviewAssessment'}" />
</h:panelGroup>
</h:panelGrid>
        
          <f:verbatim><div class="tier3"></f:verbatim>
          <h:panelGroup rendered="#{question.itemData.typeId == 7}">
           <f:subview id="deliverAudioRecording">
           <%@ include file="/jsf/delivery/item/deliverAudioRecording.jsp" %>
           </f:subview>
          </h:panelGroup>
          <h:panelGroup rendered="#{question.itemData.typeId == 6}">
           <f:subview id="deliverFileUpload">
           <%@ include file="/jsf/delivery/item/deliverFileUpload.jsp" %>
           </f:subview>
          </h:panelGroup>
          <h:panelGroup rendered="#{question.itemData.typeId == 11}">
	       <f:subview id="deliverFillInNumeric">
	       <%@ include file="/jsf/delivery/item/deliverFillInNumeric.jsp" %>
	       </f:subview>
          </h:panelGroup>
          <h:panelGroup rendered="#{question.itemData.typeId == 8}">
           <f:subview id="deliverFillInTheBlank">
           <%@ include file="/jsf/delivery/item/deliverFillInTheBlank.jsp" %>
           </f:subview>
          </h:panelGroup>
          <h:panelGroup rendered="#{question.itemData.typeId == 9}">
           <f:subview id="deliverMatching">
            <%@ include file="/jsf/delivery/item/deliverMatching.jsp" %>
           </f:subview>
          </h:panelGroup>
          <h:panelGroup
            rendered="#{question.itemData.typeId == 1 || question.itemData.typeId == 3 || question.itemData.typeId == 12}">
           <f:subview id="deliverMultipleChoiceSingleCorrect">
           <%@ include file="/jsf/delivery/item/deliverMultipleChoiceSingleCorrect.jsp" %>
           </f:subview>
          </h:panelGroup>
          <h:panelGroup rendered="#{question.itemData.typeId == 2}">
           <f:subview id="deliverMultipleChoiceMultipleCorrect">
           <%@ include file="/jsf/delivery/item/deliverMultipleChoiceMultipleCorrect.jsp" %>
           </f:subview>
          </h:panelGroup>
          <h:panelGroup rendered="#{question.itemData.typeId == 5}">
           <f:subview id="deliverShortAnswer">
           <%@ include file="/jsf/delivery/item/deliverShortAnswer.jsp" %>
           </f:subview>
          </h:panelGroup>
          <h:panelGroup rendered="#{question.itemData.typeId == 4}">
           <f:subview id="deliverTrueFalse">
           <%@ include file="/jsf/delivery/item/deliverTrueFalse.jsp" %>
           </f:subview>

           <f:verbatim></div></f:verbatim>

          </h:panelGroup>

        </h:column>
      </h:dataTable>
<f:verbatim></div></f:verbatim>
     <!-- /f:subview -->

    </h:column>
  </h:dataTable>
<f:verbatim></div></f:verbatim>
</h:panelGroup>

  <f:verbatim><br/></f:verbatim>

<!-- 1. special case: linear + no question to answer -->
<h:panelGrid columns="2" border="0" rendered="#{delivery.pageContents.isNoParts && delivery.navigation eq '1'}">
  <h:panelGrid columns="1" width="100%" border="0" columnClasses="act">
  <h:commandButton type="submit" value="#{deliveryMessages.button_submit_grading}"
      action="#{delivery.confirmSubmit}"  id="submitForm3" styleClass="active"
      rendered="#{(delivery.actionString=='takeAssessment'
                   || delivery.actionString=='takeAssessmentViaUrl'
				   || delivery.actionString=='previewAssessment')
				   && delivery.navigation eq '1' && !delivery.continue}" 
      onclick="pauseTiming='false'; disableSubmit()" onkeypress="pauseTiming='false'"/>
  </h:panelGrid>

  <h:panelGrid columns="1" width="100%" border="0">
  <h:commandButton value="#{commonMessages.cancel_action}" type="submit"
     action="select" rendered="#{delivery.pageContents.isNoParts && delivery.navigation eq '1'}" />
  </h:panelGrid>
</h:panelGrid>

<!-- 2. normal flow -->
<h:panelGrid columns="6" border="0" rendered="#{!(delivery.pageContents.isNoParts && delivery.navigation eq '1')}">
  <%-- PREVIOUS --%>
  <h:panelGrid columns="1" border="0">
	<h:commandButton id="previous" type="submit" value="#{deliveryMessages.previous}"
    action="#{delivery.previous}"
    disabled="#{!delivery.previous}" 
    onclick="disablePrevious()" onkeypress="" 
	rendered="#{(delivery.actionString=='previewAssessment'
                 || delivery.actionString=='takeAssessment'
                 || delivery.actionString=='takeAssessmentViaUrl')
              && delivery.navigation ne '1' && ((delivery.previous && delivery.continue) || (!delivery.previous && delivery.continue) || (delivery.previous && !delivery.continue))}" />
  </h:panelGrid>

  <%-- NEXT --%>
  <h:panelGrid columns="1" border="0" columnClasses="act">
    <h:commandButton id="next1" type="submit" value="#{commonMessages.action_next}"
    action="#{delivery.next_page}" disabled="#{!delivery.continue}"
    onclick="disableNext()" onkeypress="" 
	rendered="#{(delivery.actionString=='previewAssessment'
                 || delivery.actionString=='takeAssessment'
                 || delivery.actionString=='takeAssessmentViaUrl')
              && (delivery.previous && !delivery.continue)}" />

    <h:commandButton id="next" type="submit" value="#{commonMessages.action_next}"
    action="#{delivery.next_page}" styleClass="active"
    onclick="disableNext()" onkeypress="" 
	rendered="#{(delivery.actionString=='previewAssessment'
                 || delivery.actionString=='takeAssessment'
                 || delivery.actionString=='takeAssessmentViaUrl')
              && delivery.continue}" />

  </h:panelGrid>


  <h:panelGrid columns="1" border="0">
           <h:outputText value="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" escape="false" />
  </h:panelGrid>

  <%-- SAVE --%>
  <h:panelGrid columns="1" border="0" >
  <h:commandButton id="save" type="submit" value="#{deliveryMessages.button_save}"
    action="#{delivery.save_work}" onclick="disableSave();" onkeypress="disableSave()" rendered="#{delivery.actionString=='previewAssessment'
                 || delivery.actionString=='takeAssessment'
                 || delivery.actionString=='takeAssessmentViaUrl'}" />
  </h:panelGrid>

  <h:panelGrid columns="1"  border="0">
  <%-- EXIT --%>
  <h:commandButton type="submit" value="#{deliveryMessages.button_exit}"
    action="#{delivery.saveAndExit}" id="saveAndExit"
    rendered="#{(delivery.actionString=='previewAssessment'  
                 || delivery.actionString=='takeAssessment')
              && delivery.navigation ne '1' && !delivery.hasTimeLimit}"  
    onclick="pauseTiming='false'; disableSaveAndExit();" onkeypress="pauseTiming='false'; disableSaveAndExit();" />

  <%-- SAVE AND EXIT DURING PAU WITH ANONYMOUS LOGIN--%>
  <h:commandButton  type="submit" value="#{deliveryMessages.button_exit}"
    action="#{delivery.saveAndExit}" id="quit"
    rendered="#{(delivery.actionString=='takeAssessmentViaUrl' && delivery.anonymousLogin) && !delivery.hasTimeLimit}"
    onclick="pauseTiming='false'; disableQuit()" onkeypress="pauseTiming='false'; disableQuit()"  /> 

  <%-- SAVE AND EXIT FOR LINEAR ACCESS --%>
  <h:commandButton type="submit" value="#{deliveryMessages.button_exit}"
    action="#{delivery.saveAndExit}" id="saveAndExit2"
    rendered="#{(delivery.actionString=='previewAssessment'  
                 ||delivery.actionString=='takeAssessment')
            && delivery.navigation eq '1' && delivery.continue && !delivery.hasTimeLimit}"
    onclick="disableSaveAndExit2();" onkeypress="disableSaveAndExit2();"/>
  </h:panelGrid>

  <h:panelGrid columns="1" width="100%" border="0" columnClasses="act">
  <%-- SUBMIT FOR GRADE --%>
  <h:commandButton id="submitForGrade" type="submit" value="#{deliveryMessages.button_submit_grading}"
    action="#{delivery.confirmSubmit}" styleClass="active"
    rendered="#{(delivery.actionString=='takeAssessment' ||delivery.actionString=='takeAssessmentViaUrl' || delivery.actionString=='previewAssessment') 
             && delivery.navigation ne '1' 
             && !delivery.continue}"
    onclick="disableSubmitForGrade()" onkeypress="disableSubmitForGrade()"/>

  <%-- SUBMIT FOR GRADE DURING PAU --%>
  <h:commandButton type="submit" value="#{deliveryMessages.button_submit}"
    action="#{delivery.confirmSubmit}"  id="submitForm1" styleClass="active"
    rendered="#{delivery.actionString=='takeAssessmentViaUrl' && delivery.continue}"
    onclick="pauseTiming='false'; disableSubmit1();" onkeypress="pauseTiming='false';disableSubmit1()"/>

  <%-- SUBMIT FOR GRADE FOR LINEAR ACCESS --%>
  <h:commandButton type="submit" value="#{deliveryMessages.button_submit_grading}"
      action="#{delivery.confirmSubmit}"  id="submitForm" styleClass="active"
      rendered="#{(delivery.actionString=='takeAssessment'
                   || delivery.actionString=='takeAssessmentViaUrl'
				   || delivery.actionString=='previewAssessment')
				   && delivery.navigation eq '1' && !delivery.continue}" 
      onclick="pauseTiming='false'; disableSubmit()" onkeypress="pauseTiming='false';disableSubmit()"/>

  </h:panelGrid>
</h:panelGrid>

	<h:commandLink id="hiddenReloadLink" action="#{delivery.same_page}" value="">
	</h:commandLink>

<f:verbatim></p></f:verbatim>

<!-- DONE BUTTON IN PREVIEW -->
<h:panelGroup rendered="#{delivery.actionString=='previewAssessment'}">
 <f:verbatim><div class="validation"></f:verbatim>
     <h:outputText value="#{deliveryMessages.ass_preview}" />
     <h:commandButton value="#{deliveryMessages.done}" action="#{person.cleanResourceIdListInPreview}" type="submit"/>
 <f:verbatim></div></f:verbatim>
</h:panelGroup>
</h:form>
<!-- end content -->
<f:verbatim></div></f:verbatim>
    </body>
  </html>
</f:view>
