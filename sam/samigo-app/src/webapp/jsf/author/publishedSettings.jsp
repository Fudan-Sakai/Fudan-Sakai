<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://www.sakaiproject.org/samigo" prefix="samigo" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>

<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/webapp/jsf/author/publishedSettings.jsp $
 * $Id: publishedSettings.jsp 85589 2010-11-29 23:47:31Z ktsao@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008 Sakai Foundation
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
-->
  <f:view>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head><%= request.getAttribute("html.head") %>
      <title><h:outputText value="#{assessmentSettingsMessages.sakai_assessment_manager} #{assessmentSettingsMessages.dash} #{assessmentSettingsMessages.settings}" /></title>
      <samigo:script path="/jsf/widget/colorpicker/colorpicker.js"/>
      <samigo:script path="/jsf/widget/datepicker/datepicker.js"/>
      <samigo:script path="/jsf/widget/hideDivision/hideDivision.js"/>
	  <!-- AUTHORING -->
      <samigo:script path="/js/authoring.js"/>

      </head>
    <body onload="<%= request.getAttribute("html.body.onload") %>">

<script language="javascript" style="text/JavaScript">

function submitForm()
{
  document.forms[0].onsubmit();
  document.forms[0].submit();
}

// By convention we start all feedback JSF ids with "feedback".
var feedbackIdFlag = "assessmentSettingsAction:feedback";
var feedbackComponentOptionFlag = "assessmentSettingsAction:feedbackComponentOption";

var noFeedback = "3";

// If we select "No Feedback will be displayed to the student"
// it will disable and uncheck feedback as well as blank out text, otherwise,
// if a different radio button is selected, we reenable feedback checkboxes & text.
function disableAllFeedbackCheck(feedbackType)
{
  var feedbacks = document.getElementsByTagName('INPUT');
  for (i=0; i<feedbacks.length; i++)
  {
	  
    if (feedbacks[i].name.indexOf(feedbackIdFlag)==0)
	  {
      if (feedbackType == noFeedback)
      {
        if (feedbacks[i].type == 'checkbox')
		{
          feedbacks[i].checked = false;
		  feedbacks[i].disabled = true;
        }
        else if (feedbacks[i].type == 'text')
        {
          feedbacks[i].value = "";
          feedbacks[i].disabled = true;
        }
        else if ((feedbacks[i].type == 'radio') && (feedbacks[i].name.indexOf(feedbackComponentOptionFlag)==0))
        {
            feedbacks[i].value = 2; 
   
        }
        
      }
      else
      {
        feedbacks[i].disabled = false;
      }
    }
  }
  document.forms[0].submit();
}

function disableOtherFeedbackComponentOption(field)
{
  var fieldname = field.getAttribute("name");
  var feedbacks = document.getElementsByTagName('INPUT');
  var feedbackComponentIdFlag = "assessmentSettingsAction:feedbackCheckbox";
  for (i=0; i<feedbacks.length; i++)
  {
    if (feedbacks[i].name.indexOf(feedbackComponentIdFlag)==0)
    {
         if (feedbacks[i].type == 'checkbox')
        {
          feedbacks[i].checked = false;
          feedbacks[i].disabled = true;
        }
     }
  }

  document.forms[0].onsubmit();
  document.forms[0].submit();
}
function showHideReleaseGroups(){
  var showGroups;
  var inputList= document.getElementsByTagName("INPUT");
  for (i = 0; i <inputList.length; i++) 
  {
    if(inputList[i].type=='radio')
    {
      if(inputList[i].value.indexOf("Selected Groups")>=0) {
        showGroups=inputList[i].checked;
        break;
      }  
    }
  }
  if(showGroups) {
	document.getElementById("groupDiv").style.display = "block";
	document.getElementById("groupDiv").style.width = "80%";
  }
  else {
	document.getElementById("groupDiv").style.display = "none";
  }
}

function setBlockDivs()
{  
   //alert("setBlockDivs()");
   var divisionNo = ""; 
   var blockDivs = ""; 
   blockElements = document.getElementsByTagName("div");
   //alert("blockElements.length" + blockElements.length);
   for (i=0 ; i < blockElements.length; i++)
   {
      divisionNo = "" + blockElements[i].id;
	  //alert("divisionNo=" + divisionNo);
	  //alert("display=" + blockElements[i].style.display);
      if(divisionNo.indexOf("__hide_division_assessmentSettingsAction") >=0 && blockElements[i].style.display == "block")
      { 
         //alert("divisionNo=" + divisionNo);
         var id = divisionNo.substring(41);
		 if (blockDivs == "") {
            blockDivs = id;
         }
		 else {
			 blockDivs = blockDivs + ";" + id; 
		 }
		 //alert("blockDivs=" + blockDivs);
	  }
   }
   //document.forms[0].elements['assessmentSettingsAction:blockDivs'].value = "_id224";
   document.forms[0].elements['assessmentSettingsAction:blockDivs'].value = blockDivs;
}

function checkUncheckTimeBox(){
  var inputList= document.getElementsByTagName("INPUT");
  var timedCheckBoxId;
  var timedHourId;
  var timedMinuteId;
  for (i = 0; i <inputList.length; i++) 
  {
    if(inputList[i].type=='checkbox')
    {
      if(inputList[i].id.indexOf("selTimeAssess")>=0)
        timedCheckBoxId = inputList[i].id;
    }
  }
  inputList= document.getElementsByTagName("select");
  for (i = 0; i <inputList.length; i++) 
  {
    if(inputList[i].id.indexOf("timedHours")>=0)
      timedHourId =inputList[i].id;
    if(inputList[i].id.indexOf("timedMinutes")>=0)
      timedMinuteId =inputList[i].id;
  }
  if(document.getElementById(timedCheckBoxId) != null)
  {
    if(!document.getElementById(timedCheckBoxId).checked)
    {
      if(document.getElementById(timedHourId) != null)
      {
        for(i=0; i<document.getElementById(timedHourId).options.length; i++)
        {
          if(i==0)
            document.getElementById(timedHourId).options[i].selected = true;
          else
            document.getElementById(timedHourId).options[i].selected = false;
        }
      }
      if(document.getElementById(timedMinuteId) != null)
      {
        for(i=0; i<document.getElementById(timedMinuteId).options.length; i++)
        {
          if(i==0)
            document.getElementById(timedMinuteId).options[i].selected = true;
          else
            document.getElementById(timedMinuteId).options[i].selected = false;
        }
      }
    }
  }
}

function validateUrl(){
  var list =document.getElementsByTagName("input");
  for (var i=0; i<list.length; i++){
    if (list[i].id.indexOf("finalPageUrl") >= 0){			
      var finalPageUrl = list[i].value;
	  if (finalPageUrl.substring(0,4).toLowerCase().indexOf("http") == -1)
	  {
		finalPageUrl = "http://" + finalPageUrl;
	  }
	  //alert(finalPageUrl);
      window.open(finalPageUrl,'validateUrl');
    }
  }
}

function uncheckOther(field){
 var fieldname = field.getAttribute("name");
 var inputList = document.getElementsByTagName("INPUT");

 for(i = 0; i < inputList.length; i++){
    if((inputList[i].name.indexOf("background")>=0)&&(inputList[i].name != fieldname))
         inputList[i].checked=false;
      
 }
}
</script>

<f:verbatim><div class="portletBody"></f:verbatim>
<!-- content... -->
<h:form id="assessmentSettingsAction" onsubmit="return editorCheck();">
  <h:inputHidden id="assessmentId" value="#{publishedSettings.assessmentId}"/>
  <h:inputHidden id="blockDivs"value="#{publishedSettings.blockDivs}"/>

  <!-- HEADINGS -->
  <%@ include file="/jsf/author/allHeadings.jsp" %>

<p>
  <h:messages infoClass="validation" warnClass="messageValidation" errorClass="validation" fatalClass="validation"/>
</p>

    <h3>
     <h:outputText id="x1" value="#{assessmentSettingsMessages.settings} #{assessmentSettingsMessages.dash} #{publishedSettings.title}"/>
    </h3>

<f:verbatim><div class="tier1"></f:verbatim>
<!-- *** GENERAL TEMPLATE INFORMATION *** -->
<h:outputText value="#{templateMessages.allMenus0}"/>
<h:outputLink value="#" title="#{templateMessages.t_showDivs}" onclick="showDivs();" onkeypress="showDivs();">
<h:outputText value="#{templateMessages.open}"/>
</h:outputLink>
<h:outputText value=" | " />
<h:outputLink value="#" title="#{templateMessages.t_hideDivs}" onclick="hideDivs();" onkeypress="hideDivs();">
<h:outputText value="#{templateMessages.close}"/>
</h:outputLink>
<h:outputText value="#{templateMessages.allMenus}"/>

  <samigo:hideDivision id="div1" title="#{assessmentSettingsMessages.t_assessmentIntroduction}" >
<f:verbatim><div class="tier2"></f:verbatim>
    <h:panelGrid columns="2" columnClasses="shorttext"
      summary="#{templateMessages.enter_template_info_section}">
        <h:outputLabel value="#{assessmentSettingsMessages.assessment_title}"/>
        <h:inputText id="assessment_title" size="80" value="#{publishedSettings.title}" />

        <h:outputLabel value="#{assessmentSettingsMessages.assessment_creator}"  rendered="#{publishedSettings.valueMap.assessmentAuthor_isInstructorEditable==true}"/>

        <h:outputText value="#{publishedSettings.creator}"  rendered="#{publishedSettings.valueMap.assessmentAuthor_isInstructorEditable==true}"/>

        <h:outputLabel for="assessment_author" rendered="#{publishedSettings.valueMap.assessmentAuthor_isInstructorEditable==true}" value="#{assessmentSettingsMessages.assessment_authors}"/>

        <h:inputText id="assessment_author" size="80" value="#{publishedSettings.authors}"
          rendered="#{publishedSettings.valueMap.assessmentAuthor_isInstructorEditable==true}"/>

        <h:outputLabel value="#{assessmentSettingsMessages.assessment_description}" rendered="#{publishedSettings.valueMap.description_isInstructorEditable==true}"/>

        <h:panelGrid rendered="#{publishedSettings.valueMap.description_isInstructorEditable==true}">
          <samigo:wysiwyg rows="140" value="#{publishedSettings.description}" hasToggle="yes" >
           <f:validateLength maximum="4000"/>
         </samigo:wysiwyg>
        </h:panelGrid>

       <!-- ASSESSMENT ATTACHMENTS -->
       <h:panelGroup>
         <h:panelGrid columns="1">
           <%@ include file="/jsf/author/publishedSettings_attachment.jsp" %>
         </h:panelGrid>
       </h:panelGroup>

    </h:panelGrid>
<f:verbatim></div></f:verbatim>
  </samigo:hideDivision>


  <!-- *** DELIVERY DATES *** -->
  <samigo:hideDivision id="div2" title="#{assessmentSettingsMessages.t_deliveryDates}" >
    <f:verbatim><div class="tier2"></f:verbatim>
    <h:panelGrid columns="2" columnClasses="shorttext" summary="#{templateMessages.delivery_dates_sec}" border="0">

      <h:outputLabel value="#{assessmentSettingsMessages.assessment_available_date}" />
      <samigo:datePicker value="#{publishedSettings.startDateString}" size="25" id="startDate" />
      <h:outputText value="" />
      <h:outputText value="#{assessmentSettingsMessages.available_date_note}" />
	
	<!-- For formatting -->
	<h:outputText value="" />
	<h:outputText value="" />
	<h:outputText value="" />
	<h:outputText value="" />
	  
      <h:outputLabel value="#{assessmentSettingsMessages.assessment_due_date}" />
      <samigo:datePicker value="#{publishedSettings.dueDateString}" size="25" id="endDate"/>
      <h:outputText value="" />
	  <h:outputText value="#{assessmentSettingsMessages.assessment_due_date_note}" />
	  	
	<!-- For formatting -->
	<h:outputText value="" />
	<h:outputText value="" />
	<h:outputText value="" />
	<h:outputText value="" />
	  
      <h:outputLabel value="#{assessmentSettingsMessages.assessment_retract_date}"/>
  	  <h:panelGroup>
      <samigo:datePicker value="#{publishedSettings.retractDateString}" size="25" id="retractDate" />
      <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
	  <h:outputText value="#{assessmentSettingsMessages.word_or}"/>
	  <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
      <h:commandButton type="submit" value="#{assessmentSettingsMessages.button_retract_now}" action="confirmAssessmentRetract"  styleClass="active" />
      </h:panelGroup>
      <h:outputText value="" />
      <h:outputText value="#{assessmentSettingsMessages.assessment_retract_date_note}" />

    </h:panelGrid>
    <f:verbatim></div></f:verbatim>
  </samigo:hideDivision>

  <!-- *** RELEASED TO *** -->
<samigo:hideDivision title="#{assessmentSettingsMessages.t_releasedTo}" id="div3">
  <f:verbatim><div class="tier2"></f:verbatim>
    <h:panelGrid   summary="#{templateMessages.released_to_info_sec}">
      <h:selectOneRadio disabled="true" layout="pagedirection" value="#{publishedSettings.firstTargetSelected}">
        <f:selectItems value="#{assessmentSettings.publishingTargets}" />
      </h:selectOneRadio>
    </h:panelGrid>
      
  <f:verbatim><div id="groupDiv" class="tier3"></f:verbatim>
  <f:verbatim><table bgcolor="#CCCCCC"><tr><td></f:verbatim>  
  
  <f:verbatim></td><td></f:verbatim>
  <h:outputText value="#{assessmentSettingsMessages.title_description}" />
  <f:verbatim></td></tr></table></f:verbatim>
  
  <h:selectManyCheckbox disabled="true" id="groupsForSite" layout="pagedirection" value="#{publishedSettings.groupsAuthorized}">
     <f:selectItems value="#{publishedSettings.groupsForSite}" />
  </h:selectManyCheckbox>
  <f:verbatim></div></f:verbatim>

      
      <h:panelGroup styleClass="longtext">
    <h:outputLabel value="#{assessmentSettingsMessages.published_assessment_url}: " />
        <h:outputText value="#{publishedSettings.publishedUrl}" />
      </h:panelGroup>
    
  <f:verbatim></div></f:verbatim>
</samigo:hideDivision>

  <!-- *** HIGH SECURITY *** -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.ipAccessType_isInstructorEditable==true or publishedSettings.valueMap.passwordRequired_isInstructorEditable==true}" >
  <samigo:hideDivision title="#{assessmentSettingsMessages.t_highSecurity}" id="div4">
	<f:verbatim><div class="tier2"></f:verbatim>
    <h:panelGrid border="0" columns="2" columnClasses="longtext"
        summary="#{templateMessages.high_security_sec}">
      <h:outputText value="#{assessmentSettingsMessages.high_security_allow_only_specified_ip}" rendered="#{publishedSettings.valueMap.ipAccessType_isInstructorEditable==true}"/>
	  <h:panelGroup rendered="#{publishedSettings.valueMap.ipAccessType_isInstructorEditable==true}">
      <h:inputTextarea value="#{publishedSettings.ipAddresses}" cols="40" rows="5"/>
 	  <h:outputText escape="false" value="<br/>#{assessmentSettingsMessages.ip_note} <br/>#{assessmentSettingsMessages.ip_example}#{assessmentSettingsMessages.ip_ex}<br/>"/> 
      </h:panelGroup>

      <h:outputText value="#{assessmentSettingsMessages.high_security_secondary_id_pw}" rendered="#{publishedSettings.valueMap.passwordRequired_isInstructorEditable==true}"/>
      <h:panelGrid border="0" columns="2"  columnClasses="longtext"
        rendered="#{publishedSettings.valueMap.passwordRequired_isInstructorEditable==true}">
        <h:outputLabel value="#{assessmentSettingsMessages.high_security_username}"/>
        <h:inputText size="20" value="#{publishedSettings.username}"/>

        <h:outputLabel value="#{assessmentSettingsMessages.high_security_password}"/>
        <h:inputText size="20" value="#{publishedSettings.password}"/>
      </h:panelGrid>
    </h:panelGrid>
<f:verbatim></div></f:verbatim>
  </samigo:hideDivision>
</h:panelGroup>

  <!-- *** TIMED *** -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.timedAssessment_isInstructorEditable==true}" >
  <samigo:hideDivision id="div5" title="#{assessmentSettingsMessages.t_timedAssessment}">
<f:verbatim><div class="tier2"></f:verbatim>
    <h:panelGrid summary="#{templateMessages.timed_assmt_sec}">
	  <h:panelGroup>
        <h:selectBooleanCheckbox id="selTimeAssess" onclick="checkUncheckTimeBox();setBlockDivs();document.forms[0].onsubmit();document.forms[0].submit();"
         value="#{publishedSettings.valueMap.hasTimeAssessment}">
		</h:selectBooleanCheckbox>
        <h:outputText value="#{assessmentSettingsMessages.timed_assessment} " />
		<h:selectOneMenu id="timedHours" value="#{publishedSettings.timedHours}" disabled="#{!publishedSettings.valueMap.hasTimeAssessment}" >
		  <f:selectItems value="#{publishedSettings.hours}" />
        </h:selectOneMenu>
        <h:outputText value="#{assessmentSettingsMessages.timed_hours} " />
        <h:selectOneMenu id="timedMinutes" value="#{publishedSettings.timedMinutes}" disabled="#{!publishedSettings.valueMap.hasTimeAssessment}">
          <f:selectItems value="#{publishedSettings.mins}" />
        </h:selectOneMenu>
        <h:outputText value="#{assessmentSettingsMessages.timed_minutes} " />
       <f:verbatim><br/></f:verbatim>
        <h:outputText value="#{assessmentSettingsMessages.auto_submit_description}" />
      </h:panelGroup>
    </h:panelGrid>
<f:verbatim></div></f:verbatim>
  </samigo:hideDivision>
  </h:panelGroup>

  <!-- *** ASSESSMENT ORGANIZATION *** -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.itemAccessType_isInstructorEditable==true or publishedSettings.valueMap.displayChunking_isInstructorEditable==true or publishedSettings.valueMap.displayNumbering_isInstructorEditable==true }" >

  <samigo:hideDivision id="div6" title="#{assessmentSettingsMessages.t_assessmentOrganization}" >
  <f:verbatim><div class="tier2"></f:verbatim>

<%--     DEBUGGING:  Layout= <h:outputText value="#{publishedSettings.assessmentFormat}" /> ;
     navigation= <h:outputText value="#{publishedSettings.itemNavigation}" /> ;
     numbering= <h:outputText value="#{publishedSettings.itemNumbering}" />
--%>
  <!-- NAVIGATION -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.itemAccessType_isInstructorEditable==true}">
  <f:verbatim> <div class="longtext"></f:verbatim> <h:outputLabel for="itemNavigation" value="#{assessmentSettingsMessages.navigation}" />
  <f:verbatim></div><div class="tier3"></f:verbatim>
    <h:panelGrid columns="2">
      <h:selectOneRadio id="itemNavigation" value="#{publishedSettings.itemNavigation}"  layout="pageDirection" 
		onclick="setBlockDivs();submitForm();">
        <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.linear_access}"/>
        <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.random_access}"/>
      </h:selectOneRadio>
    </h:panelGrid>
  <f:verbatim></div></f:verbatim>
  </h:panelGroup>
    
    <!-- QUESTION LAYOUT -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.displayChunking_isInstructorEditable==true}">
    <f:verbatim><div class="longtext"></f:verbatim>
	<h:outputLabel value="#{assessmentSettingsMessages.question_layout}" />
	<f:verbatim></div><div class="tier3"></f:verbatim>

      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="assessmentFormat" value="#{publishedSettings.assessmentFormat}"  layout="pageDirection"  rendered="#{publishedSettings.itemNavigation!=1}">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.layout_by_question}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.layout_by_part}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.layout_by_assessment}"/>
        </h:selectOneRadio>

        <h:selectOneRadio id="assessmentFormat2"  disabled="true"
            value="#{publishedSettings.assessmentFormat}"  layout="pageDirection" rendered="#{publishedSettings.itemNavigation == 1}">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.layout_by_question}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.layout_by_part}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.layout_by_assessment}"/>
        </h:selectOneRadio>
      </h:panelGrid>
    <f:verbatim></div></f:verbatim>
  </h:panelGroup>

    <!-- NUMBERING -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.displayNumbering_isInstructorEditable==true}">
    <f:verbatim><div class="longtext"></f:verbatim>
	<h:outputLabel value="#{assessmentSettingsMessages.numbering}" />
	<f:verbatim></div><div class="tier3"></f:verbatim>

       <h:panelGrid columns="2"  >
         <h:selectOneRadio id="itemNumbering"
             value="#{publishedSettings.itemNumbering}"  layout="pageDirection">
           <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.continous_numbering}"/>
           <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.part_numbering}"/>
         </h:selectOneRadio>
      </h:panelGrid>
    <f:verbatim></div></f:verbatim>
  </h:panelGroup>
  <f:verbatim></div></f:verbatim>
  </samigo:hideDivision>
  </h:panelGroup>

<!-- *** MARK FOR REVIEW *** -->
<h:panelGroup rendered="#{publishedSettings.valueMap.markForReview_isInstructorEditable==true}">
  <samigo:hideDivision title="#{assessmentSettingsMessages.mark_for_review}">
    <f:verbatim><div class="tier2"></f:verbatim>
    <h:panelGrid columns="1">
      <h:panelGroup>
        <h:selectBooleanCheckbox id="markForReview" value="#{publishedSettings.isMarkForReview}" />
        <h:outputLabel value="#{assessmentSettingsMessages.mark_for_review_label}"/>
		<h:outputLink title="#{assessmentSettingsMessages.whats_this_link}" value="#" onclick="javascript:window.open('markForReviewPopUp.faces','MarkForReview','width=250,height=220,scrollbars=yes, resizable=yes');" onkeypress="javascript:window.open('markForReviewTipText.faces','MarkForReview','width=250,height=220,scrollbars=yes, resizable=yes');" >
          <h:outputText  value=" #{assessmentSettingsMessages.whats_this_link}"/>
        </h:outputLink>
      </h:panelGroup>
      <h:outputText value="#{assessmentSettingsMessages.mark_for_review_text_1}" />
	  <h:outputText value="#{assessmentSettingsMessages.mark_for_review_text_2}" />
    </h:panelGrid>
	<f:verbatim></div></f:verbatim>
  </samigo:hideDivision>
</h:panelGroup>

  <!-- *** SUBMISSIONS *** -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.submissionModel_isInstructorEditable==true or publishedSettings.valueMap.lateHandling_isInstructorEditable==true or publishedSettings.valueMap.autoSave_isInstructorEditable==true}" >
  <samigo:hideDivision id="div7" title="#{assessmentSettingsMessages.t_submissions}" >
<%--     DEBUGGING:
     Unlimited= <h:outputText value="#{publishedSettings.unlimitedSubmissions}" /> ;
     Submissions= <h:outputText value="#{publishedSettings.submissionsAllowed}" /> ;
     lateHandling= <h:outputText value="#{publishedSettings.lateHandling}" />
--%>
<f:verbatim><div class="tier2"></f:verbatim>
    <!-- NUMBER OF SUBMISSIONS -->
    <h:panelGroup rendered="#{publishedSettings.valueMap.submissionModel_isInstructorEditable==true}">
     <f:verbatim><div class="longtext"></f:verbatim>
	 <h:outputLabel value="#{assessmentSettingsMessages.submissions}" />
	 <f:verbatim></div><div class="tier3"></f:verbatim>
	 <f:verbatim><table><tr><td></f:verbatim>

        <h:selectOneRadio id="unlimitedSubmissions1" rendered="#{publishedSettings.itemNavigation!=1}"
            value="#{publishedSettings.unlimitedSubmissions}" layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.unlimited_submission}"/>
          <f:selectItem itemValue="0" itemLabel="#{assessmentSettingsMessages.only}" />
        </h:selectOneRadio>
        <h:selectOneRadio id="unlimitedSubmissions2" disabled="true" rendered="#{publishedSettings.itemNavigation==1}"
            value="0" layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.unlimited_submission}"/>
          <f:selectItem itemValue="0" itemLabel="#{assessmentSettingsMessages.only}" />
        </h:selectOneRadio>
        <f:verbatim></td><td valign="bottom"></f:verbatim>
        
        <h:panelGroup rendered="#{publishedSettings.itemNavigation!=1}">
          <h:inputText size="5" value="#{publishedSettings.submissionsAllowed}"/>
          <h:outputLabel value="#{assessmentSettingsMessages.limited_submission}" />
        </h:panelGroup>
        <h:panelGroup rendered="#{publishedSettings.itemNavigation==1}">
          <h:inputText size="5" value="1" disabled="true"/>
          <h:outputLabel value="#{assessmentSettingsMessages.limited_submission}" />
        </h:panelGroup>

    <f:verbatim></td></tr></table></f:verbatim>
     <f:verbatim></div></f:verbatim>
   </h:panelGroup>

	<!-- LATE HANDLING -->
   <h:panelGroup rendered="#{publishedSettings.valueMap.lateHandling_isInstructorEditable==true}">
   <f:verbatim><div class="longtext"></f:verbatim>
   <h:outputLabel value="#{assessmentSettingsMessages.late_handling}" />
   <f:verbatim></div><div class="tier3"></f:verbatim>
      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="lateHandling" 
            value="#{publishedSettings.lateHandling}"  layout="pageDirection">
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.not_accept_latesubmission}"/>
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.accept_latesubmission}"/>
        </h:selectOneRadio>
      </h:panelGrid>
    <f:verbatim></div></f:verbatim>
   </h:panelGroup>

    <!-- AUTOMATIC SUBMISSION -->
    <h:panelGroup rendered="#{publishedSettings.valueMap.automaticSubmission_isInstructorEditable==true}">
      <f:verbatim> <div class="longtext"> </f:verbatim> 
      <h:outputLabel value="#{assessmentSettingsMessages.automatic_submission}"/>
      <f:verbatim> </div></f:verbatim>
      <f:verbatim><div class="tier3"></f:verbatim>
      <h:panelGrid columns="1" border="0">
	    <h:panelGroup>
	      <h:selectBooleanCheckbox id="automaticSubmission" value="#{publishedSettings.autoSubmit}"/>
          <h:outputLabel value="#{assessmentSettingsMessages.auto_submit}"/>
        </h:panelGroup>
		<h:panelGroup>
          <f:verbatim>&nbsp;</f:verbatim>
          <h:outputText value="#{assessmentSettingsMessages.automatic_submission_note_1}"/>
		</h:panelGroup>
      </h:panelGrid>
      <f:verbatim> </div> </f:verbatim>
   </h:panelGroup>

    <!-- AUTOSAVE -->
<%-- hide for 1.5 release SAM-148
    <div class="longtext"><h:outputLabel value="#{assessmentSettingsMessages.auto_save}" /></div>
    <f:verbatim><div class="tier3"></f:verbatim>
      <h:panelGrid columns="2"  >
        <h:selectOneRadio id="autoSave"  disabled="true"
            value="#{publishedSettings.submissionsSaved}"  layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.user_click_save}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.save_automatically}"/>
        </h:selectOneRadio>
      </h:panelGrid>
    </div>
--%>
<f:verbatim></div></f:verbatim>
  </samigo:hideDivision>
  </h:panelGroup>

  <!-- *** SUBMISSION MESSAGE *** -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.submissionMessage_isInstructorEditable==true or publishedSettings.valueMap.finalPageURL_isInstructorEditable==true}" >
  <samigo:hideDivision id="div8" title="#{assessmentSettingsMessages.t_submissionMessage}" >
    <f:verbatim><div class="tier2"><div class="longtext"></f:verbatim>
    <h:panelGrid rendered="#{publishedSettings.valueMap.submissionMessage_isInstructorEditable==true}">
      <h:outputLabel value="#{assessmentSettingsMessages.submission_message}" />
        <samigo:wysiwyg rows="140" value="#{publishedSettings.submissionMessage}" hasToggle="yes" >
         <f:validateLength maximum="4000"/>
        </samigo:wysiwyg>
	</h:panelGrid>
	 <f:verbatim><br/></f:verbatim>
 <f:verbatim></div></f:verbatim>
  <f:verbatim><div class="longtext"></f:verbatim>
      <h:panelGroup rendered="#{publishedSettings.valueMap.finalPageURL_isInstructorEditable==true}">
      <h:outputLabel value="#{assessmentSettingsMessages.submission_final_page_url}" /><f:verbatim><br/></f:verbatim>
      <h:inputText size="80" id="finalPageUrl" value="#{publishedSettings.finalPageUrl}" />
      <h:commandButton value="#{assessmentSettingsMessages.validateURL}" type="button" onclick="javascript:validateUrl();"/>
      </h:panelGroup>
<f:verbatim></div></div></f:verbatim>
  </samigo:hideDivision>
  </h:panelGroup>


  <!-- *** FEEDBACK *** -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable==true or publishedSettings.valueMap.feedbackType_isInstructorEditable==true or publishedSettings.valueMap.feedbackComponents_isInstructorEditable==true}" >
  <samigo:hideDivision id="div9" title="#{commonMessages.feedback}" >
 
 <!-- FEEDBACK AUTHORING -->
  <f:verbatim><div class="tier2"></f:verbatim>
   <h:panelGroup rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable==true}">
    <f:verbatim><div class="longtext"></f:verbatim>
	<h:outputLabel value="#{commonMessages.feedback_authoring}" />
	<f:verbatim></div><div class="tier3"></f:verbatim>
    <h:panelGroup>
      <h:panelGrid columns="1"  >
        <h:selectOneRadio id="feedbackAuthoring" 
             value="#{publishedSettings.feedbackAuthoring}"
           layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{commonMessages.question_level_feedback}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.sectionlevel_feedback}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.both_feedback}"/>
        </h:selectOneRadio>
      </h:panelGrid>
   </h:panelGroup>
  <f:verbatim></div></f:verbatim>
  </h:panelGroup>
  
	<h:panelGroup rendered="#{publishedSettings.valueMap.feedbackType_isInstructorEditable==true}">
    <f:verbatim><div class="longtext"></f:verbatim>
	<h:outputLabel value="#{commonMessages.feedback_delivery}" />
	<f:verbatim></div><div class="tier3"></f:verbatim>
    <h:panelGroup>
      <h:panelGrid columns="1" rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable!=true}" >
        <h:selectOneRadio id="feedbackDelivery1"  disabled="true" 
             value="#{publishedSettings.feedbackDelivery}"
           layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.immediate_feedback}"/>
          <f:selectItem itemValue="4" itemLabel="#{commonMessages.feedback_on_submission}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.no_feedback}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.feedback_by_date}"/>
        </h:selectOneRadio>

        <h:panelGroup>
        <f:verbatim>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</f:verbatim>
		<h:inputText value="#{publishedSettings.feedbackDateString}" size="25" disabled="true"/>
        </h:panelGroup>
      </h:panelGrid>
    </h:panelGroup>

      <h:panelGrid columns="1" rendered="#{publishedSettings.valueMap.feedbackType_isInstructorEditable==true}" >
  		<h:selectOneRadio id="feedbackDelivery2" rendered="#{publishedSettings.valueMap.feedbackAuthoring_isInstructorEditable==true}"
             value="#{publishedSettings.feedbackDelivery}"
           layout="pageDirection" onclick="setBlockDivs();disableAllFeedbackCheck(this.value);">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.immediate_feedback}"/>
		  <f:selectItem itemValue="4" itemLabel="#{commonMessages.feedback_on_submission}"/>
          <f:selectItem itemValue="3" itemLabel="#{assessmentSettingsMessages.no_feedback}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.feedback_by_date}"/>
        </h:selectOneRadio>

	    <h:panelGrid columns="7" >
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
          <samigo:datePicker value="#{publishedSettings.feedbackDateString}" size="25" id="feedbackDate" >
            <f:convertDateTime pattern="#{generalMessages.output_date_picker}" />
          </samigo:datePicker>
        </h:panelGrid>

	    <h:panelGrid columns="7" >
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
		  <h:outputText value=" "/>
          <h:outputText value="#{assessmentSettingsMessages.gradebook_note_f}" />
        </h:panelGrid>
      </h:panelGrid>
    </h:panelGroup>
<f:verbatim></div><div class="longtext"></f:verbatim>

       <h:panelGrid columns="2"  >
        <h:selectOneRadio id="feedbackComponentOption" value="#{publishedSettings.feedbackComponentOption}"
        onclick="setBlockDivs();disableOtherFeedbackComponentOption(this);"  layout="pageDirection">
          <f:selectItem itemValue="1" itemLabel="#{templateMessages.feedback_components_totalscore_only}"/>
          <f:selectItem itemValue="2" itemLabel="#{templateMessages.feedback_components_select}"/>
        </h:selectOneRadio>
      </h:panelGrid>
  
   <f:verbatim></div><div class="tier3"></f:verbatim>
    <h:panelGroup rendered="#{publishedSettings.valueMap.feedbackComponents_isInstructorEditable!=true}">
      <h:panelGrid columns="2"  >
       <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox11"
              value="#{publishedSettings.showStudentResponse}"/>
          <h:outputText value="#{commonMessages.student_response}" />
        </h:panelGroup>
       <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox12"
              value="#{publishedSettings.showQuestionLevelFeedback}"/>
          <h:outputText value="#{commonMessages.question_level_feedback}" />
       </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox13"
              value="#{publishedSettings.showCorrectResponse}"/>
          <h:outputText value="#{commonMessages.correct_response}" />
        </h:panelGroup>

       <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox14"
             value="#{publishedSettings.showSelectionLevelFeedback}"/>
          <h:outputText value="#{commonMessages.selection_level_feedback}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox15"
              value="#{publishedSettings.showStudentScore}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_assessment_score}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox16"
              value="#{publishedSettings.showGraderComments}"/>
          <h:outputText value="#{assessmentSettingsMessages.grader_comments}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox17"
              value="#{publishedSettings.showStudentQuestionScore}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_question_score}" />
        </h:panelGroup>
       
        <h:panelGroup>
          <h:selectBooleanCheckbox  disabled="true" id="feedbackCheckbox18"
              value="#{publishedSettings.showStatistics}"/>
          <h:outputText value="#{commonMessages.statistics_and_histogram}" />
        </h:panelGroup>
   
      </h:panelGrid>
    </h:panelGroup>
	
	<h:panelGroup rendered="#{publishedSettings.valueMap.feedbackComponents_isInstructorEditable==true}">
      <h:panelGrid columns="2"  >
       <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox21" disabled="#{publishedSettings.feedbackDelivery==3 || publishedSettings.feedbackComponentOption ==1}"
              value="#{publishedSettings.showStudentResponse}"/>
          <h:outputText value="#{commonMessages.student_response}" />
        </h:panelGroup>
       <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox22" disabled="#{publishedSettings.feedbackDelivery==3 || publishedSettings.feedbackComponentOption ==1}"
              value="#{publishedSettings.showQuestionLevelFeedback}"/>
          <h:outputText value="#{commonMessages.question_level_feedback}" />
       </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox23" disabled="#{publishedSettings.feedbackDelivery==3 || publishedSettings.feedbackComponentOption ==1}"
              value="#{publishedSettings.showCorrectResponse}"/>
          <h:outputText value="#{commonMessages.correct_response}" />
        </h:panelGroup>

       <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox24" disabled="#{publishedSettings.feedbackDelivery==3 || publishedSettings.feedbackComponentOption ==1}"
             value="#{publishedSettings.showSelectionLevelFeedback}"/>
          <h:outputText value="#{commonMessages.selection_level_feedback}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox25" disabled="#{publishedSettings.feedbackDelivery==3 || publishedSettings.feedbackComponentOption ==1}"
              value="#{publishedSettings.showStudentScore}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_assessment_score}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox26" disabled="#{publishedSettings.feedbackDelivery==3 || publishedSettings.feedbackComponentOption ==1}"
              value="#{publishedSettings.showGraderComments}"/>
          <h:outputText value="#{assessmentSettingsMessages.grader_comments}" />
        </h:panelGroup>

        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox27" disabled="#{publishedSettings.feedbackDelivery==3 || publishedSettings.feedbackComponentOption ==1}"
              value="#{publishedSettings.showStudentQuestionScore}"/>
          <h:outputText value="#{assessmentSettingsMessages.student_question_score}" />
        </h:panelGroup>
       
        <h:panelGroup>
          <h:selectBooleanCheckbox id="feedbackCheckbox28" disabled="#{publishedSettings.feedbackDelivery==3 || publishedSettings.feedbackComponentOption ==1}"
              value="#{publishedSettings.showStatistics}"/>
          <h:outputText value="#{commonMessages.statistics_and_histogram}" />
        </h:panelGroup>
   
      </h:panelGrid>
    </h:panelGroup>
	<f:verbatim></div></div></f:verbatim>
  </samigo:hideDivision>
  </h:panelGroup>

  <!-- *** GRADING *** -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.testeeIdentity_isInstructorEditable==true or publishedSettings.valueMap.toGradebook_isInstructorEditable==true or publishedSettings.valueMap.recordedScore_isInstructorEditable==true}" >
  <samigo:hideDivision id="div10" title="#{assessmentSettingsMessages.t_grading}" >
  <f:verbatim><div class="tier2"></f:verbatim>
  <h:panelGroup rendered="#{publishedSettings.valueMap.testeeIdentity_isInstructorEditable==true}"> <f:verbatim> <div class="longtext"></f:verbatim>  <h:outputLabel value="#{assessmentSettingsMessages.student_identity}" />
  <f:verbatim></div><div class="tier3"> </f:verbatim>
        <h:panelGrid columns="2" rendered="#{publishedSettings.firstTargetSelected != 'Anonymous Users'}">
          <h:selectOneRadio id="anonymousGrading1" value="#{publishedSettings.anonymousGrading}"  layout="pageDirection" disabled="#{publishedSettings.editPubAnonyGradingRestricted}">
            <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.not_anonymous}"/>
            <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.anonymous}"/>
          </h:selectOneRadio>
        </h:panelGrid>
        <h:panelGrid columns="2" rendered="#{publishedSettings.firstTargetSelected == 'Anonymous Users'}">
          <h:selectOneRadio id="anonymousGrading2" value="1"  layout="pageDirection" disabled="true">
            <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.not_anonymous}"/>
            <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.anonymous}"/>
          </h:selectOneRadio>
        </h:panelGrid>
        <f:verbatim></div></f:verbatim>
      </h:panelGroup>

    <!-- GRADEBOOK OPTIONS -->
    <h:panelGroup rendered="#{publishedSettings.valueMap.toGradebook_isInstructorEditable==true && publishedSettings.gradebookExists==true}">
     <f:verbatim> <div class="longtext"></f:verbatim> <h:outputLabel value="#{assessmentSettingsMessages.gradebook_options}" />
	 <f:verbatim></div><div class="tier3"></f:verbatim>
      <h:panelGrid columns="2" rendered="#{publishedSettings.firstTargetSelected != 'Anonymous Users'}">
        <h:selectOneRadio id="toDefaultGradebook1"
            value="#{publishedSettings.toDefaultGradebook}"  layout="pageDirection">
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.to_no_gradebook}"/>
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.to_default_gradebook}"/>
        </h:selectOneRadio>
      </h:panelGrid>

      <h:panelGrid columns="2" rendered="#{publishedSettings.firstTargetSelected == 'Anonymous Users'}">
        <h:selectOneRadio id="toDefaultGradebook2" disabled="true" value="2"  layout="pageDirection">
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.to_no_gradebook}"/>
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.to_default_gradebook}"/>
        </h:selectOneRadio>
      </h:panelGrid>

	<f:verbatim></div></f:verbatim>
    </h:panelGroup>

    <!-- RECORDED SCORE AND MULTIPLES -->
    <h:panelGroup rendered="#{publishedSettings.valueMap.recordedScore_isInstructorEditable==true}">
   <f:verbatim> <div class="longtext"></f:verbatim> <h:outputLabel value="#{assessmentSettingsMessages.recorded_score}" />
   <f:verbatim></div><div class="tier3"></f:verbatim>
      <h:panelGrid columns="2"  >
        <h:selectOneRadio value="#{publishedSettings.scoringType}"  layout="pageDirection" rendered="#{author.canRecordAverage}">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.highest_score}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.last_score}"/>
          <f:selectItem itemValue="4" itemLabel="#{assessmentSettingsMessages.average_score}"/>
        </h:selectOneRadio>
        <h:selectOneRadio value="#{publishedSettings.scoringType}"  layout="pageDirection" rendered="#{!author.canRecordAverage}">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.highest_score}"/>
          <f:selectItem itemValue="2" itemLabel="#{assessmentSettingsMessages.last_score}"/>
        </h:selectOneRadio>
      </h:panelGrid>
	  <f:verbatim></div></f:verbatim>
    </h:panelGroup>
  <f:verbatim></div></f:verbatim>
  </samigo:hideDivision>
  </h:panelGroup>

  <!-- *** COLORS AND GRAPHICS	*** -->
  <h:panelGroup rendered="#{publishedSettings.valueMap.bgColor_isInstructorEditable==true}" >
  <samigo:hideDivision id="div11" title="#{assessmentSettingsMessages.t_graphics}" >
	<f:verbatim><div class="tier2"></f:verbatim>
        <h:selectOneRadio onclick="uncheckOther(this)" id="background_color" value="#{publishedSettings.bgColorSelect}">
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.background_color}"/>
       </h:selectOneRadio>

      <samigo:colorPicker value="#{publishedSettings.bgColor}" size="10" id="pickColor"/>
       <h:selectOneRadio onclick="uncheckOther(this)" id="background_image" value="#{publishedSettings.bgImageSelect}"  >
          <f:selectItem itemValue="1" itemLabel="#{assessmentSettingsMessages.background_image}"/>
       </h:selectOneRadio>  
   
       <h:inputText size="80" value="#{publishedSettings.bgImage}"/>
   <f:verbatim></div></f:verbatim>
  </samigo:hideDivision>
  </h:panelGroup>

 <!-- *** META *** -->
<h:panelGroup rendered="#{publishedSettings.valueMap.metadataAssess_isInstructorEditable==true}">
  <samigo:hideDivision title="#{assessmentSettingsMessages.t_metadata}" id="div13">
	<f:verbatim><div class="tier2"><div class="longtext"></f:verbatim>
	<h:outputLabel value="#{assessmentSettingsMessages.assessment_metadata}" />
	<f:verbatim></div><div class="tier3"></f:verbatim>
    <h:panelGrid columns="2" columnClasses="shorttext">
      <h:outputLabel value="#{assessmentSettingsMessages.metadata_keywords}"/>
      <h:inputText size="80" value="#{publishedSettings.keywords}"/>

    <h:outputLabel value="#{assessmentSettingsMessages.metadata_objectives}"/>
      <h:inputText size="80" value="#{publishedSettings.objectives}"/>

      <h:outputLabel value="#{assessmentSettingsMessages.metadata_rubrics}"/>
      <h:inputText size="80" value="#{publishedSettings.rubrics}"/>
    </h:panelGrid>
	<f:verbatim></div></f:verbatim>

    <f:verbatim><div class="longtext"></f:verbatim> 
	<h:outputLabel value="#{assessmentSettingsMessages.record_metadata}" />
	<f:verbatim></div><div class="tier3"></f:verbatim>
    <h:panelGrid columns="2"  >
     <h:selectBooleanCheckbox value="#{publishedSettings.valueMap.hasMetaDataForQuestions}"/>
     <h:outputText value="#{assessmentSettingsMessages.metadata_questions}" />
    </h:panelGrid>
<f:verbatim></div></div></f:verbatim>
  </samigo:hideDivision>
  </h:panelGroup>

<f:verbatim></div></f:verbatim>

<p class="act">

  <!-- Save button -->
  <h:commandButton type="submit" value="#{assessmentSettingsMessages.button_save_settings}" action="#{publishedSettings.getOutcome}"  styleClass="active" onclick="setBlockDivs();" >
      <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.author.SavePublishedSettingsListener" />
  </h:commandButton>
  
  <!-- Cancel button -->
  <h:commandButton value="#{commonMessages.cancel_action}" type="submit" action="#{author.getFromPage}" rendered="#{author.fromPage != 'editAssessment'}">
        <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.author.ResetPublishedAssessmentAttachmentListener" />
  </h:commandButton>

  <h:commandButton value="#{commonMessages.cancel_action}" type="submit" action="editAssessment" rendered="#{author.fromPage == 'editAssessment'}">
      <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.author.ResetPublishedAssessmentAttachmentListener" />
	  <f:actionListener type="org.sakaiproject.tool.assessment.ui.listener.author.EditAssessmentListener" />
  </h:commandButton>

</p>
</h:form>
<!-- end content -->
<f:verbatim></div></f:verbatim>

        <script language="javascript" style="text/JavaScript">retainHideUnhideStatus('none');showHideReleaseGroups();</script>

      </body>
    </html>
  </f:view>
