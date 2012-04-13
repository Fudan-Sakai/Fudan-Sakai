<%@ page import="java.util.*, javax.faces.context.*, javax.faces.application.*,
                 javax.faces.el.*, org.sakaiproject.tool.messageforums.*"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/messageforums" prefix="mf" %>
<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.api.app.messagecenter.bundle.Messages"/>
</jsp:useBean>
<f:view>
	<sakai:view title="#{msgs.cdfm_discussion_forum_settings}" toolCssHref="/messageforums-tool/css/msgcntr.css">
	<script type="text/javascript" src="/library/js/jquery.js"></script>
	<sakai:script contextBase="/messageforums-tool" path="/js/sak-10625.js"/>      
	<script type="text/javascript" src="/messageforums-tool/js/jquery.charcounter.js"> </script>
	<sakai:script contextBase="/messageforums-tool" path="/js/permissions_header.js"/>
	<sakai:script contextBase="/messageforums-tool" path="/js/forum.js"/>

	<sakai:script contextBase="/messageforums-tool" path="/js/datetimepicker.js"/>
	<%
	  	String thisId = request.getParameter("panel");
  		if (thisId == null) 
  		{
    		thisId = "Main" + org.sakaiproject.tool.cover.ToolManager.getCurrentPlacement().getId();
  		}
	%>
	<script type="text/javascript">
		function resize(){
  			mySetMainFrameHeight('<%= org.sakaiproject.util.Web.escapeJavascript(thisId)%>');
  		}
	</script> 
	<script type="text/javascript">
	function setDatesEnabled(radioButton){
		$(".openDateSpan").toggle();
		$(".closeDateSpan").toggle();
		resize();		
	}

	function openDateCal(){
			NewCal('revise:openDate','MMDDYYYY',true,12);
	}

	function closeDateCal(){
			NewCal('revise:closeDate','MMDDYYYY',true,12);
	}
	</script>

  <!-- Y:\msgcntr\messageforums-app\src\webapp\jsp\dfReviseForumSettingsAttach.jsp -->
    <h:form id="revise">
		  <script type="text/javascript">
            $(document).ready(function(){
				var charRemFormat = $('.charRemFormat').text();
				$(".forum_shortDescriptionClass").charCounter(255, {
					container: ".charsRemaining",
					format: charRemFormat
				 });
			 });				 
        </script>
      <sakai:tool_bar_message value="#{msgs.cdfm_discussion_forum_settings}" />
		<div class="instruction">
		  <h:outputText id="instruction"  value="#{msgs.cdfm_settings_instruction}"/>
		  <h:outputText value="#{msgs.cdfm_info_required_sign}" styleClass="reqStarInline" />
		</div>
			<h:messages styleClass="messageAlert" id="errorMessages"  /> 
     
			<h:panelGrid columns="1" styleClass="jsfFormTable" columnClasses="shorttext">
				<h:panelGroup>	
					<%-- //designNote: does this text input need a maxlength attribute ? --%>
					<h:outputLabel id="outputLabel" for="forum_title" styleClass="block" style="padding-bottom:.3em;display:block;clear:both;float:none">
					<h:outputText id="req_star"  value="#{msgs.cdfm_info_required_sign}" styleClass="reqStar"/>	
						<h:outputText  value="#{msgs.cdfm_forum_title}" />
					</h:outputLabel>	
					<h:inputText size="50" id="forum_title"  maxlength="250" value="#{ForumTool.selectedForum.forum.title}">
						<f:validateLength minimum="1" maximum="255"/>
					</h:inputText>
				</h:panelGroup>	
			</h:panelGrid>
			<h:panelGrid columns="1"  columnClasses="longtext">
				<h:panelGroup>
					<h:outputText value="" />
					<%-- //designNote: this label should alert that textarea has a 255 max chars limit --%>
					<h:outputLabel id="outputLabel1" for="forum_shortDescription"  value="#{msgs.cdfm_shortDescription}"/>	
							<h:outputText value="#{msgs.cdfm_shortDescriptionCharsRem}"  styleClass="charRemFormat" style="display:none"/>
							<%--
						
							<h:outputText value="%1 chars remain"  styleClass="charRemFormat" style="display:none"/>
						--%>
							<h:outputText value="" styleClass="charsRemaining" style="padding-left:3em;font-size:.85em;"/>
							<h:outputText value=""  style="display:block"/>
					
					<h:inputTextarea rows="3" cols="45" id="forum_shortDescription"  value="#{ForumTool.selectedForum.forum.shortDescription}" styleClass="forum_shortDescriptionClass" style="float:none"/>
					<h:outputText value="" />
				</h:panelGroup>	
      	</h:panelGrid>
      		
			<%--RTEditor area - if enabled--%>
			<h:panelGroup rendered="#{! ForumTool.disableLongDesc}">
				<h:outputText id="outputLabel2" value="#{msgs.cdfm_fullDescription}" style="display:block;padding:.5em 0"/>
			<sakai:inputRichText textareaOnly="#{PrivateMessagesTool.mobileSession}" rows="12" cols="120" id="df_compose_description" value="#{ForumTool.selectedForum.forum.extendedDescription}">
				<f:validateLength maximum="65000"/>
			</sakai:inputRichText>
	      	</h:panelGroup>
	      	
			
			<%--Attachment area  --%>
	      <h4>
		        <h:outputText value="#{msgs.cdfm_att}"/>
	      </h4>
			<div style="padding-left:1em">
				<%--designNote: would be nice to make this an include, as well as a more comprehensive MIME type check  --%> 
			<h:dataTable styleClass="attachPanel" id="attmsg"  value="#{ForumTool.attachments}" var="eachAttach"  cellpadding="0" cellspacing="0" columnClasses="attach,bogus,specialLink,bogus,bogus" rendered="#{!empty ForumTool.attachments}">
				<h:column>
					<f:facet name="header">   <h:outputText value=" "/>                                          
						</f:facet>
						<sakai:contentTypeMap fileType="#{eachAttach.attachment.attachmentType}" mapType="image" var="imagePath" pathPrefix="/library/image/"/>									
						<h:graphicImage id="exampleFileIcon" value="#{imagePath}" />							
						</h:column>
						<h:column>
						<f:facet name="header">
							<h:outputText value="#{msgs.cdfm_title}"/>
						</f:facet>
							<h:outputText value="#{eachAttach.attachment.attachmentName}"/>
					</h:column>
					<h:column>
					<f:facet name="header">
						<h:outputText value=" "/>
					</f:facet>
						<h:commandLink action="#{ForumTool.processDeleteAttachSetting}" 
								immediate="true"
								onfocus="document.forms[0].onsubmit();"
								title="#{msgs.cdfm_remove}">
							<h:outputText value="#{msgs.cdfm_remove}"/>
								<f:param value="#{eachAttach.attachment.attachmentId}" name="dfmsg_current_attach"/>
							</h:commandLink>
				  </h:column>
					<h:column rendered="#{!empty ForumTool.attachments}">
						<f:facet name="header">
							<h:outputText value="#{msgs.cdfm_attsize}" />
						</f:facet>
						<h:outputText value="#{eachAttach.attachment.attachmentSize}"/>
					</h:column>
					<h:column rendered="#{!empty ForumTool.attachments}">
						<f:facet name="header">
		  			  <h:outputText value="#{msgs.cdfm_atttype}" />
						</f:facet>
					<%--//designNote: do we really need this info if the lookup has worked? I Suppose till the MIME type check is more comprehensive, yes --%>
						<h:outputText value="#{eachAttach.attachment.attachmentType}"/>
					</h:column>
					</h:dataTable>   

			<div class="instruction">	        
				<h:outputText value="#{msgs.cdfm_no_attachments}" rendered="#{empty ForumTool.attachments}"/>
			</div>
			<p class="act" style="padding:0 0 1em 0;">
				<h:commandButton  action="#{ForumTool.processAddAttachmentRedirect}"
					value="#{msgs.cdfm_button_bar_add_attachment_more_redirect}" 
					immediate="true" 
					style="font-size:96%"
					rendered="#{!empty ForumTool.attachments}"/>
				<h:commandButton  action="#{ForumTool.processAddAttachmentRedirect}"
					value="#{msgs.cdfm_button_bar_add_attachment_redirect}" 
					immediate="true" 
					style="font-size:96%"
					rendered="#{empty ForumTool.attachments}"
					/>
			</p>	
			</div>		
			<%--general posting  forum settings --%>
			<h4 style="margin:0"><h:outputText  value="#{msgs.cdfm_forum_posting}"/></h4>
			<div style="padding-left:1em">
			<h:panelGrid columns="2" styleClass="jsfFormTable" style="margin-top:0">
   			<h:panelGroup>
					<h:outputText  value="#{msgs.cdfm_lock_forum}" />	
				</h:panelGroup>
				<h:panelGroup>
					<h:selectOneRadio layout="lineDirection"  id="forum_locked"  value="#{ForumTool.selectedForum.locked}"  styleClass="selectOneRadio">
    					<f:selectItem itemValue="true" itemLabel="#{msgs.cdfm_yes}"/>
    					<f:selectItem itemValue="false" itemLabel="#{msgs.cdfm_no}"/>
  					</h:selectOneRadio>
				</h:panelGroup>
   			<h:panelGroup styleClass="shorttext">
					<h:outputText   value="#{msgs.cdfm_moderate_forum}" />	
				</h:panelGroup>
				<h:panelGroup>
					<h:selectOneRadio layout="lineDirection"  id="moderated"  value="#{ForumTool.selectedForum.moderated}" 
						onclick="javascript:disableOrEnableModeratePerm();"  styleClass="selectOneRadio">
    					<f:selectItem itemValue="true" itemLabel="#{msgs.cdfm_yes}"/>
    					<f:selectItem itemValue="false" itemLabel="#{msgs.cdfm_no}"/>
  					</h:selectOneRadio>
				</h:panelGroup>
			</h:panelGrid>
			</div>
			
			<h4><h:outputText  value="#{msgs.cdfm_forum_availability}" /></h4>
			
			<div style="padding-left:1em">
			<h:panelGrid columns="1" columnClasses="longtext,checkbox">
              <h:panelGroup>
                 <h:selectOneRadio layout="pageDirection" onclick="this.blur()" onchange="setDatesEnabled(this);" disabled="#{not ForumTool.editMode}" id="availabilityRestricted"  value="#{ForumTool.selectedForum.availabilityRestricted}">
                  <f:selectItem itemValue="false" itemLabel="#{msgs.cdfm_forum_avail_show}"/>
                  <f:selectItem itemValue="true" itemLabel="#{msgs.cdfm_forum_avail_date}"/>
               </h:selectOneRadio>
               </h:panelGroup>
               <h:panelGroup id="openDateSpan" styleClass="indnt2 openDateSpan" style="display: #{ForumTool.selectedForum.availabilityRestricted ? '' : 'none'}">
               	   <h:outputText value="#{msgs.openDate}: "/>
	               <h:inputText id="openDate" value="#{ForumTool.selectedForum.openDate}"/>
	               <f:verbatim>
	               	<a id="openCal" href="javascript:openDateCal();">
	               </f:verbatim>
	               <h:graphicImage url="/images/calendar.png" title="#{msgs.pickDate}" alt="#{msgs.pickDate}"/>
	               <f:verbatim>
	               </a>
	               </f:verbatim>
              	</h:panelGroup>
              	<h:panelGroup id="closeDateSpan" styleClass="indnt2 closeDateSpan" style="display: #{ForumTool.selectedForum.availabilityRestricted ? '' : 'none'}">
              		<h:outputText value="#{msgs.closeDate}: "/>
	               <h:inputText id="closeDate" value="#{ForumTool.selectedForum.closeDate}"/>
	               <f:verbatim>
	               	<a id="closeCal" href="javascript:closeDateCal();">
	               </f:verbatim>
	               <h:graphicImage url="/images/calendar.png" title="#{msgs.pickDate}" alt="#{msgs.pickDate}"/>
	               <f:verbatim>
	               </a>
	               </f:verbatim>
              	</h:panelGroup>
           </h:panelGrid>
 		</div>

		<div class="instruction" style="padding: 0.5em; margin-top:0.8em;"><h4>
       			<h:outputText value="#{msgs.cdfm_forum_mark_read}"/>
       		</h4></div>
         	<h:panelGrid columns="2" >
         		<h:panelGroup styleClass="shorttext">
           			<h:outputLabel for="autoMarkThreadsRead" value="#{msgs.cdfm_auto_mark_threads_read}" styleClass="shorttext"/>
         		</h:panelGroup>
         		<h:panelGroup>
           			<h:selectOneRadio layout="lineDirection" id="autoMarkThreadsRead" value="#{ForumTool.selectedForum.autoMarkThreadsRead}" styleClass="checkbox inlineForm">
             				<f:selectItem itemValue="true" itemLabel="#{msgs.cdfm_yes}"/>
             				<f:selectItem itemValue="false" itemLabel="#{msgs.cdfm_no}"/>
           			</h:selectOneRadio>
         		</h:panelGroup>
	       </h:panelGrid>

 	 	<%@include file="/jsp/discussionForum/permissions/permissions_include.jsp"%>
	      
      <div class="act">
          <h:commandButton action="#{ForumTool.processActionSaveForumSettings}" value="#{msgs.cdfm_button_bar_save_setting}"
          								 rendered="#{!ForumTool.selectedForum.markForDeletion}" accesskey="s"> 
    	 	  	<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>         
          </h:commandButton>
				<h:commandButton action="#{ForumTool.processActionSaveForumAndAddTopic}" value="#{msgs.cdfm_button_bar_save_setting_add_topic}" accesskey="t"
          								 rendered = "#{!ForumTool.selectedForum.markForDeletion}">
	        	<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
          </h:commandButton>  
				<h:commandButton action="#{ForumTool.processActionSaveForumAsDraft}" value="#{msgs.cdfm_button_bar_save_draft}" accesskey="v"
          								 rendered = "#{!ForumTool.selectedForum.markForDeletion}">
	        	<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
          </h:commandButton>
				<%-- // designNote: these next 2 actions  should be available in the list view instead of here --%>
          <h:commandButton id="delete_confirm" action="#{ForumTool.processActionDeleteForumConfirm}" 
                           value="#{msgs.cdfm_button_bar_delete}" rendered="#{!ForumTool.selectedForum.markForDeletion && ForumTool.displayForumDeleteOption}"
                           accesskey="d">
	        	<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
          </h:commandButton>
          
          <h:commandButton id="delete" action="#{ForumTool.processActionDeleteForum}" 
                           value="#{msgs.cdfm_button_bar_delete}" rendered="#{ForumTool.selectedForum.markForDeletion}"
                           accesskey="d">
	        	<f:param value="#{ForumTool.selectedForum.forum.id}" name="forumId"/>
          </h:commandButton>
          <h:commandButton  action="#{ForumTool.processActionHome}" value="#{msgs.cdfm_button_bar_cancel}" accesskey="x" />
       </div>
	 </h:form>
    </sakai:view>
</f:view>
