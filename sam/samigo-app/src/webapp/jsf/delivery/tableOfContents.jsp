<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
  <!DOCTYPE html
   PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!--
* $Id: tableOfContents.jsp 84512 2010-11-09 21:27:31Z ktsao@stanford.edu $
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
    <title><h:outputText value="#{deliveryMessages.table_of_contents}" /></title>
    <samigo:script path="/jsf/widget/hideDivision/hideDivision.js" />
    </head>
    <body onload="hideUnhideAllDivsExceptFirst('none');;<%= request.getAttribute("html.body.onload") %>">
<!--div class="portletBody"-->

 <h:outputText value="<div class='portletBody' style='#{delivery.settings.divBgcolor};#{delivery.settings.divBackground}'>" escape="false"/>

 <!--h:outputText value="<div class='portletBody' style='background:#{delivery.settings.divBgcolor};background-image:url(http://www.w3.org/WAI/UA/TS/html401/images/test-background.gif)'>" escape="false"/-->
 
<%@ include file="/js/delivery.js" %>
 
<!-- content... -->
<script language="javascript">

function noenter(){
return!(window.event && window.event.keyCode == 13);
}

function showElements(theForm) {
  str = "Form Elements of form " + theForm.name + ": \n "
  for (i = 0; i < theForm.length; i++)
    str += theForm.elements[i].name + "\n"
  alert(str)
}

function saveTime()
{
  //showElements(document.forms[0]);
  if((typeof (document.forms[0].elements['tableOfContentsForm:elapsed'])!=undefined) && ((document.forms[0].elements['tableOfContentsForm:elapsed'])!=null) ){
  pauseTiming = 'true';
  // loaded is in 1/10th sec and elapsed is in sec, so need to divide by 10
  if (self.loaded) {
	document.forms[0].elements['tableOfContentsForm:elapsed'].value=loaded/10;
  }
 }
}

function clickSubmitForGrade(){
  var newindex = 0;
  for (i=0; i<document.links.length; i++) {
    if(document.links[i].id == "tableOfContentsForm:submitforgrade")
    {
      newindex = i;
      break;
    }
  }
  document.links[newindex].onclick();
}

</script>


<!-- DONE BUTTON FOR PREVIEW ASSESSMENT -->
<h:form id="tableOfContentsForm">

<h:panelGroup rendered="#{delivery.actionString=='previewAssessment'}">
 <f:verbatim><div class="validation"></f:verbatim>
     <h:outputText value="#{deliveryMessages.ass_preview}" />
     <h:commandButton value="#{deliveryMessages.done}" action="#{person.cleanResourceIdListInPreview}" type="submit"/>
 <f:verbatim></div></f:verbatim>
</h:panelGroup>

<h3><h:outputText value="#{delivery.assessmentTitle} " escape="false"/></h3>

<h:panelGroup rendered="#{(delivery.actionString=='takeAssessment'
                           || delivery.actionString=='takeAssessmentViaUrl') 
                        && delivery.hasTimeLimit}" >
<f:verbatim><span id="remText"></f:verbatim><h:outputText value="#{deliveryMessages.time_remaining} "/><f:verbatim></span></f:verbatim>
<f:verbatim><span id="timer"></f:verbatim><f:verbatim> </span></f:verbatim>

<f:verbatim> <span id="bar"></f:verbatim>
  <samigo:timerBar height="15" width="300"
    wait="#{delivery.timeLimit}"
    elapsed="#{delivery.timeElapse}"
	expireMessage1="#{deliveryMessages.time_expired1}"
	expireMessage2="#{deliveryMessages.time_expired2}"
	fiveMinutesMessage1="#{deliveryMessages.five_minutes_left1}"
	fiveMinutesMessage2="#{deliveryMessages.five_minutes_left2}"
    expireScript="document.forms[0].elements['tableOfContentsForm:elapsed'].value=loaded; document.forms[0].elements['tableOfContentsForm:outoftime'].value='true'; clickSubmitForGrade();" />
<f:verbatim>  </span></f:verbatim>

<h:commandButton type="button" onclick="document.getElementById('remText').style.display=document.getElementById('remText').style.display=='none' ? '': 'none';document.getElementById('timer').style.display=document.getElementById('timer').style.display=='none' ? '': 'none';document.getElementById('bar').style.display=document.getElementById('bar').style.display=='none' ? '': 'none'" onkeypress="document.getElementById('remText').style.display=document.getElementById('remText').style.display=='none' ? '': 'none';document.getElementById('timer').style.display=document.getElementById('timer').style.display=='none' ? '': 'none';document.getElementById('bar').style.display=document.getElementById('bar').style.display=='none' ? '': 'none'" value="#{deliveryMessages.hide_show}" />
</h:panelGroup>

<h:panelGroup rendered="#{delivery.actionString=='previewAssessment'&& delivery.hasTimeLimit}" >
	<h:graphicImage height="60" width="300" url="/images/delivery/TimerPreview.png"/>
</h:panelGroup>

<div class="tier1">
  <f:verbatim><b></f:verbatim><h:outputText value="#{deliveryMessages.warning}#{deliveryMessages.column} "/><f:verbatim></b></f:verbatim>
  <h:outputText value="#{deliveryMessages.instruction_submitGrading}" />
</div>

<div class="tier1">
  <h4>
    <h:outputText value="#{deliveryMessages.table_of_contents} " />
    <h:outputText styleClass="tier10" value="#{deliveryMessages.tot_score} " />
    <h:outputText value="#{delivery.tableOfContents.maxScore}">
      <f:convertNumber maxFractionDigits="2"/>
    </h:outputText>
    <h:outputText value="#{deliveryMessages.pt}" />
  </h4>
 
</div>

<div class="tier2">
  <h5>
    <h:outputLabel value="#{deliveryMessages.key}"/>
  </h5>
  <h:graphicImage  alt="#{deliveryMessages.alt_unans_q}" url="/images/tree/blank.gif" />
  <h:outputText value="#{deliveryMessages.unans_q}" /><br/>
  <h:graphicImage  alt="#{deliveryMessages.alt_q_marked}" url="/images/tree/marked.gif" rendered="#{delivery.displayMardForReview}" />
  <h:outputText value="#{deliveryMessages.q_marked}" rendered="#{delivery.displayMardForReview}"/>

<h:inputHidden id="assessmentID" value="#{delivery.assessmentId}"/>
<h:inputHidden id="assessTitle" value="#{delivery.assessmentTitle}" />
<h:inputHidden id="elapsed" value="#{delivery.timeElapse}" />
<h:inputHidden id="outoftime" value="#{delivery.timeOutSubmission}"/>
<h:commandLink id="submitforgrade" action="#{delivery.submitForGrade}" value="" />

    <h:messages infoClass="validation" warnClass="validation" errorClass="validation" fatalClass="validation"/>
    <p style="margin-bottom:0"><h:outputText value="#{deliveryMessages.seeOrHide}" /> </p>
    <h:dataTable value="#{delivery.tableOfContents.partsContents}" var="part">
      <h:column>
      <h:panelGroup>
        <samigo:hideDivision id="part" title = "#{deliveryMessages.p} #{part.number} - #{part.nonDefaultText}  -
       #{part.questions-part.unansweredQuestions}/#{part.questions} #{deliveryMessages.ans_q}, #{part.pointsDisplayString}#{part.roundedMaxPoints} #{deliveryMessages.pt}" > 
        <h:dataTable value="#{part.itemContents}" var="question">
          <h:column>
            <f:verbatim><div class="tier3"></f:verbatim>
            <h:panelGroup>
            <h:graphicImage alt="#{deliveryMessages.alt_unans_q}" 
               url="/images/tree/blank.gif" rendered="#{question.unanswered}"/>
            <h:graphicImage alt="#{deliveryMessages.alt_q_marked}"
               url="/images/tree/marked.gif"  rendered="#{question.review}"/>
              <h:commandLink title="#{deliveryMessages.t_takeAssessment}" immediate="true" action="takeAssessment"> 
                <h:outputText escape="false" value="#{question.sequence}#{deliveryMessages.dot} #{question.strippedText} (#{question.pointsDisplayString}#{question.roundedMaxPoints} #{deliveryMessages.pt})">
<f:convertNumber maxFractionDigits="2"/>
        </h:outputText>
                <f:param name="partnumber" value="#{part.number}" />
                <f:param name="questionnumber" value="#{question.number}" />
                <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.delivery.UpdateTimerFromTOCListener" />
                <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.delivery.DeliveryActionListener" />
              </h:commandLink>
            </h:panelGroup>
            <f:verbatim></div></f:verbatim> 
          </h:column>
        </h:dataTable>
       </samigo:hideDivision>
      </h:panelGroup>
      </h:column>
    </h:dataTable>
</div>

<p class="act">
<!-- SUBMIT FOR GRADE BUTTON FOR TAKE ASSESSMENT AND PREVIEW ASSESSMENT -->
  <!-- check permisison to determine if the button should be displayed -->
  <h:panelGroup rendered="#{delivery.actionString=='previewAssessment'
                         || (delivery.actionString=='takeAssessment' 
                             && authorization!=null 
                             && authorization.takeAssessment 
                             && authorization.submitAssessmentForGrade)}">
    <h:commandButton id="submitForGradeTOC1" type="submit" value="#{deliveryMessages.button_submit_grading}"
      action="#{delivery.confirmSubmitTOC}" styleClass="active"  
      onclick="disableSubmitForGradeTOC1();javascript:saveTime()" onkeypress="disableSubmitForGradeTOC1();javascript:saveTime()"
      disabled="#{delivery.actionString=='previewAssessment'}" />
  </h:panelGroup>

<!-- SUBMIT BUTTON FOR TAKE ASSESSMENT VIA URL ONLY -->
  <h:commandButton id="submitForGradeTOC2" type="submit" value="#{deliveryMessages.button_submit}"
    action="#{delivery.confirmSubmitTOC}" styleClass="active" onclick="disableSubmitForGradeTOC2();"
    rendered="#{delivery.actionString=='takeAssessmentViaUrl'}" />

<!-- SAVE AND EXIT BUTTON FOR TAKE ASSESMENT AND PREVIEW ASSESSMENT-->
  <h:commandButton id="exitTOC1" type="submit" value="#{deliveryMessages.button_exit}"
    action="#{delivery.saveAndExit}"
    onclick="disableExitTOC1();javascript:saveTime()" onkeypress="disableExitTOC1();javascript:saveTime()"
    rendered="#{(delivery.actionString=='takeAssessment'
             || delivery.actionString=='previewAssessment') && !delivery.hasTimeLimit}" 
    disabled="#{delivery.actionString=='previewAssessment'}" />

<!-- QUIT BUTTON FOR TAKE ASSESSMENT VIA URL -->
  <h:commandButton id="exitTOC2" type="submit" value="#{deliveryMessages.button_exit}"
    action="#{delivery.saveAndExit}"
    onclick="disableExitTOC2();javascript:saveTime()" onkeypress="disableExitTOC2();javascript:saveTime()"
    rendered="#{delivery.actionString=='takeAssessmentViaUrl' && !delivery.hasTimeLimit}" >
  </h:commandButton>
</p>

<!-- DONE BUTTON FOR PREVIEW ASSESSMENT ONLY -->
<h:panelGroup rendered="#{delivery.actionString=='previewAssessment'}">
 <f:verbatim><div class="validation"></f:verbatim>
     <h:outputText value="#{deliveryMessages.ass_preview}" />
     <h:commandButton value="#{deliveryMessages.done}" action="#{person.cleanResourceIdListInPreview}" type="submit"/>
 <f:verbatim></div></f:verbatim>
</h:panelGroup>

</h:form>
<!-- end content -->
</div>
    </body>
  </html>
</f:view>

