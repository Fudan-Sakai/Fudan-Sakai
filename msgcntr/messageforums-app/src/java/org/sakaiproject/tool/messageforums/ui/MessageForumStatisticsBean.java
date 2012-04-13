/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-app/src/java/org/sakaiproject/tool/messageforums/ui/MessageForumsSynopticBean.java $
 * $Id: MessageForumsSynopticBean.java $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008, 2009 The Sakai Foundation
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
package org.sakaiproject.tool.messageforums.ui;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.Vector;

import javax.faces.event.ActionEvent;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.app.messageforums.Area;
import org.sakaiproject.api.app.messageforums.AreaManager;
import org.sakaiproject.api.app.messageforums.Attachment;
import org.sakaiproject.api.app.messageforums.DiscussionForum;
import org.sakaiproject.api.app.messageforums.DiscussionTopic;
import org.sakaiproject.api.app.messageforums.MembershipManager;
import org.sakaiproject.api.app.messageforums.Message;
import org.sakaiproject.api.app.messageforums.MessageForumsMessageManager;
import org.sakaiproject.api.app.messageforums.MessageForumsTypeManager;
import org.sakaiproject.api.app.messageforums.PrivateForum;
import org.sakaiproject.api.app.messageforums.PrivateMessage;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.api.app.messageforums.UnreadStatus;
import org.sakaiproject.api.app.messageforums.ui.DiscussionForumManager;
import org.sakaiproject.api.app.messageforums.ui.PrivateMessageManager;
import org.sakaiproject.api.app.messageforums.ui.UIPermissionsManager;
import org.sakaiproject.authz.cover.AuthzGroupService;
import org.sakaiproject.component.app.messageforums.MembershipItem;
import org.sakaiproject.component.app.messageforums.dao.hibernate.PrivateTopicImpl;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SitePage;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.tool.messageforums.ui.MessageForumSynopticBean.DecoratedCompiledMessageStats;
import org.sakaiproject.user.api.Preferences;
import org.sakaiproject.user.api.PreferencesEdit;
import org.sakaiproject.user.api.PreferencesService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.util.ResourceLoader;
import org.sakaiproject.tool.cover.SessionManager;


public class MessageForumStatisticsBean {
	
	/**
	 * Used to store Statistic information on message forum per 
	 * per user
	 */
	
	public class DecoratedCompiledMessageStatistics {
		private String siteName;
		private String siteId;
		private String siteUser;
		private String siteUserId;
		private int authoredForumsAmt;
		private int readForumsAmt;
		private int unreadForumsAmt;
		private Double percentReadForumsAmt;
		
		
		public String getSiteName(){
			return this.siteName;
		}
		
		public void setSiteName(String newValue){
			this.siteName = newValue;
		}
		
		public String getSiteId(){
			return this.siteId;
		}
		
		public void setSiteId(String newValue){
			this.siteId = newValue;
		}
		
		public String getSiteUser(){
			return this.siteUser;
		}
		
		public void setSiteUser(String newValue){
			this.siteUser = newValue;
		}
		
		public String getEscapedSiteUser(){
			return this.siteUser.replaceAll("'", "\\\\'");
		}

		public String getSiteUserId(){
			return this.siteUserId;
		}
		
		public void setSiteUserId(String newValue){
			this.siteUserId = newValue;
		}
		
		public int getAuthoredForumsAmt(){
			return this.authoredForumsAmt;
		}
		
		public void setAuthoredForumsAmt(int newValue){
			this.authoredForumsAmt = newValue;
		}
		
		public int getReadForumsAmt(){
			return this.readForumsAmt;
		}
		
		public void setReadForumsAmt(int newValue){
			this.readForumsAmt = newValue;
		}
		
		public int getUnreadForumsAmt(){
			return this.unreadForumsAmt;
		}
		
		public void setUnreadForumsAmt(int newValue){
			this.unreadForumsAmt = newValue;
		}
		
		public Double getPercentReadForumsAmt(){
			return this.percentReadForumsAmt;
		}
		
		public void setPercentReadFOrumsAmt(Double newValue){
			this.percentReadForumsAmt = newValue;
		}
	}
	/* === End DecoratedCompiledMessageStatistics === */
	
	public class DecoratedCompiledUserStatistics {
		private String siteName;
		private String siteId;
		private String siteUser;
		private String siteUserId;
		private String forumTitle;
		private String topicTitle;
		private Date forumDate;
		private String forumSubject;
		private String message;
		private String msgId;
		private String topicId;
		private Boolean msgDeleted;
		private String forumId;
		private List decoAttachmentsList;
	
		
		public String getSiteName(){
			return this.siteName;
		}
		
		public void setSiteName(String newValue){
			this.siteName = newValue;
		}
		
		public String getSiteId(){
			return this.siteId;
		}
		
		public void setSiteId(String newValue){
			this.siteId = newValue;
		}
		
		public String getSiteUser(){
			return this.siteUser;
		}
		
		public void setSiteUser(String newValue){
			this.siteUser = newValue;
		}
		
		public String getSiteUserId(){
			return this.siteUserId;
		}
		
		public void setSiteUserId(String newValue){
			this.siteUserId = newValue;
		}
		
		public String getForumTitle(){
			return this.forumTitle;
		}
		
		public void setForumTitle(String newValue){
			this.forumTitle = newValue;
		}
		
		public Date getForumDate(){
			return forumDate;
		}
		
		public void setForumDate(Date newValue){
			this.forumDate = newValue;
		}
		
		public String getForumSubject(){
			return forumSubject;
		}
		
		public void setForumSubject(String newValue){
			this.forumSubject = newValue;
		}

		public String getTopicTitle() {
			return topicTitle;
		}

		public void setTopicTitle(String topicTitle) {
			this.topicTitle = topicTitle;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getMsgId() {
			return msgId;
		}

		public void setMsgId(String msgId) {
			this.msgId = msgId;
		}

		public String getTopicId() {
			return topicId;
		}

		public void setTopicId(String topicId) {
			this.topicId = topicId;
		}

		public Boolean getMsgDeleted() {
			return msgDeleted;
		}

		public void setMsgDeleted(Boolean msgDeleted) {
			this.msgDeleted = msgDeleted;
		}

		public String getForumId() {
			return forumId;
		}

		public void setForumId(String forumId) {
			this.forumId = forumId;
		}

		public List getDecoAttachmentsList() {
			return decoAttachmentsList;
		}

		public void setDecoAttachmentsList(List decoAttachmentsList) {
			this.decoAttachmentsList = decoAttachmentsList;
		}
	}
	/* === End DecoratedCompiledUserStatistics == */
	
	/** Decorated Bean to store stats for user **/
	public DecoratedCompiledMessageStatistics userInfo = null;
	public DecoratedCompiledUserStatistics userAuthoredInfo = null;
	
	private Map courseMemberMap;
	protected boolean ascending = true;
	protected boolean ascendingForUser = false;
	protected boolean ascendingForUser2 = false;
	protected boolean ascendingForUser3 = false;
	protected String sortBy = NAME_SORT;
	protected String sortByUser = FORUM_DATE_SORT;
	protected String sortByUser2 = FORUM_DATE_SORT2;
	protected String sortByUser3 = FORUM_DATE_SORT3;
	
	
	private static final String LIST_PAGE = "dfStatisticsList";
	private static final String NAME_SORT = "sort_by_name";
	private static final String AUTHORED_SORT = "sort_by_num_authored";
	private static final String READ_SORT = "sort_by_num_read";
	private static final String UNREAD_SORT = "sort_by_num_unread";
	private static final String PERCENT_READ_SORT = "sort_by_percent_read";
	private static final String SITE_USER_ID = "siteUserId";
	private static final String SITE_USER = "siteUser";
	private static final String FORUM_TITLE_SORT = "sort_by_forum_title";
	private static final String TOPIC_TITLE_SORT = "sort_by_topic_title";
	private static final String FORUM_DATE_SORT = "sort_by_forum_date";
	private static final String FORUM_SUBJECT_SORT = "sort_by_forum_subject_2";
	private static final String FORUM_TITLE_SORT2 = "sort_by_forum_title_2";
	private static final String TOPIC_TITLE_SORT2 = "sort_by_topic_title_2";
	private static final String FORUM_DATE_SORT2 = "sort_by_forum_date_2";
	private static final String FORUM_SUBJECT_SORT2 = "sort_by_forum_subject_2";
	private static final String FORUM_DATE_SORT3 = "sort_by_forum_date_3";
	private static final String TOPIC_TITLE_SORT3 = "sort_by_forum_subject_3";

	
	
	private static final String MESSAGECENTER_BUNDLE = "org.sakaiproject.api.app.messagecenter.bundle.Messages";
	
	private static final String FORUM_STATISTICS = "dfStatisticsList";
	private static final String FORUM_STATISTICS_USER = "dfStatisticsUser";
	private static final String FORUM_STATISTICS_ALL_AUTHORED_MSG = "dfStatisticsAllAuthoredMessageForOneUser";
	private static final String FORUM_STATISTICS_MSG = "dfStatisticsFullTextForOne";
	
	
	public String selectedSiteUserId = null;
	public String selectedSiteUser = null;
	public String selectedMsgId= null;
	public String selectedMsgSubject= null;
	public String selectedForumTitle= null;
	public String selectedTopicTitle= null;
	public String selectedTopicId= null;
		
	private String buttonUserName;
	private boolean isFirstParticipant = false;
	private boolean isLastParticipant = false;
	
	//Comparatibles
	public static Comparator NameComparatorAsc;
	public static Comparator AuthoredComparatorAsc;
	public static Comparator ReadComparatorAsc;
	public static Comparator UnreadComparatorAsc;
	public static Comparator PercentReadComparatorAsc;
	public static Comparator NameComparatorDesc;
	public static Comparator AuthoredComparatorDesc;
	public static Comparator ReadComparatorDesc;
	public static Comparator UnreadComparatorDesc;
	public static Comparator PercentReadComparatorDesc;
	public static Comparator DateComparaterDesc;
	public static Comparator ForumTitleComparatorAsc;
	public static Comparator ForumTitleComparatorDesc;
	public static Comparator TopicTitleComparatorAsc;
	public static Comparator TopicTitleComparatorDesc;
	public static Comparator ForumDateComparatorAsc;
	public static Comparator ForumDateComparatorDesc;
	public static Comparator ForumSubjectComparatorAsc;
	public static Comparator ForumSubjectComparatorDesc;

	
	public Map getCourseMemberMap(){
		return this.courseMemberMap;
	}
	
	public void setCourseMemberMap(Map newValue){
		this.courseMemberMap = newValue;
	}
	
	/** to get accces to log file */
	private static final Log LOG = LogFactory.getLog(MessageForumSynopticBean.class);
	
	/** Needed if within a site so we only need stats for this site */
	private MessageForumsMessageManager messageManager;
	
	/** Needed to get topics if tool within a site */
	private DiscussionForumManager forumManager;
	
	private MembershipManager membershipManager;
	
	
	/** Needed to determine if user has read permission of topic */
	private UIPermissionsManager uiPermissionsManager;
	
	public void setMessageManager(MessageForumsMessageManager messageManager){
		this.messageManager = messageManager;
	}
	
	public void setForumManager(DiscussionForumManager forumManager){
		this.forumManager = forumManager;
	}
	
	
	public String getSelectedSiteUserId(){
		return this.selectedSiteUserId;
	}
	
	public String getSelectedSiteUser(){
		return this.selectedSiteUser;
	}
	
	public void setUiPermissionsManager(UIPermissionsManager uiPermissionsManager){
		this.uiPermissionsManager = uiPermissionsManager;
	}
	
	public void setMembershipManager(MembershipManager membershipManager)
	{
		this.membershipManager = membershipManager;
	}
	
	
	public DecoratedCompiledMessageStatistics getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(DecoratedCompiledMessageStatistics userInfo) {
		this.userInfo = userInfo;
	}
	
	
	public List getAllUserStatistics(){

		int totalForum = messageManager.findMessageCountTotal();
		Map<String, DecoratedCompiledMessageStatistics> tmpStatistics = new TreeMap<String, DecoratedCompiledMessageStatistics>();

		// process the returned read statistics for the students to get them sorted by user id
		List<Object[]> studentReadStats = messageManager.findReadMessageCountForAllStudents();
		for (Object[] readStat: studentReadStats) {
			DecoratedCompiledMessageStatistics userStats = tmpStatistics.get(readStat[0]);
			if (userStats == null) {
				userStats = new DecoratedCompiledMessageStatistics();
				tmpStatistics.put((String)readStat[0], userStats);
			}
			
			if (totalForum > 0) {
				userStats.setReadForumsAmt((Integer)readStat[1]);
			} else {
				userStats.setReadForumsAmt(0);				
			}
		}
		
		// process the returned authored statistics for the students to get them sorted by user id
		List<Object[]> studentAuthoredStats = messageManager.findAuthoredMessageCountForAllStudents();
		for (Object[] authoredStat: studentAuthoredStats) {
			DecoratedCompiledMessageStatistics userStats = tmpStatistics.get(authoredStat[0]);
			if (userStats == null) {
				userStats = new DecoratedCompiledMessageStatistics();
				tmpStatistics.put((String)authoredStat[0], userStats);
			}
			
			if (totalForum > 0) {
				userStats.setAuthoredForumsAmt((Integer)authoredStat[1]);
			} else {
				userStats.setAuthoredForumsAmt(0);
			}
		}

		// now process the users from the list of site members to add display information
		// this will also prune the list of members so only the papropriate ones are displayed
		courseMemberMap = membershipManager.getAllCourseMembers(true,false,false);
		List members = membershipManager.convertMemberMapToList(courseMemberMap);
		final List<DecoratedCompiledMessageStatistics> statistics = new ArrayList<DecoratedCompiledMessageStatistics>();

		for (Iterator i = members.iterator(); i.hasNext();) {
			MembershipItem item = (MembershipItem) i.next();
			
			if (null != item.getUser()) {
				userInfo = tmpStatistics.get(item.getUser().getId());
				if (userInfo == null) {
					userInfo = new DecoratedCompiledMessageStatistics();
					userInfo.setReadForumsAmt(0);
					userInfo.setAuthoredForumsAmt(0);
				}
				
				userInfo.setSiteUserId(item.getUser().getId());
				userInfo.setSiteUser(item.getName());

				if (totalForum > 0) {
					userInfo.setUnreadForumsAmt(totalForum - userInfo.getReadForumsAmt());
					userInfo.setPercentReadFOrumsAmt((double)userInfo.getReadForumsAmt() / (double)totalForum);
				} else {
					userInfo.setUnreadForumsAmt(0);
					userInfo.setPercentReadFOrumsAmt((double)0);
				}

				statistics.add(userInfo);
			}
		}

		sortStatistics(statistics);
		return statistics;
	}

	public List getUserAuthoredStatistics(){
		final List<DecoratedCompiledUserStatistics> statistics = new ArrayList<DecoratedCompiledUserStatistics>();

		List<Message> messages = messageManager.findAuthoredMessagesForStudent(selectedSiteUserId);
		if (messages == null) return statistics;

		for (Message msg: messages) {
			userAuthoredInfo = new DecoratedCompiledUserStatistics();
			userAuthoredInfo.setSiteUserId(selectedSiteUserId);
			userAuthoredInfo.setForumTitle(msg.getTopic().getOpenForum().getTitle());
			userAuthoredInfo.setTopicTitle(msg.getTopic().getTitle());
			userAuthoredInfo.setForumDate(msg.getCreated());
			userAuthoredInfo.setForumSubject(msg.getTitle());
			userAuthoredInfo.setMsgId(Long.toString(msg.getId()));
			userAuthoredInfo.setTopicId(Long.toString(msg.getTopic().getId()));
			userAuthoredInfo.setForumId(Long.toString(msg.getTopic().getOpenForum().getId()));
			statistics.add(userAuthoredInfo);
		}

		sortStatisticsByUser(statistics);
		return statistics;
	}
		
	public List getUserAuthoredStatistics2(){
		final List<DecoratedCompiledUserStatistics> statistics = new ArrayList<DecoratedCompiledUserStatistics>();

		List<Message> messages = messageManager.findAuthoredMessagesForStudent(selectedSiteUserId);
		if (messages == null) return statistics;

		for (Message msg: messages) {
			Message mesWithAttach = (Message)messageManager.getMessageByIdWithAttachments(msg.getId());
			List decoAttachList = new ArrayList();
			List attachList = mesWithAttach.getAttachments();
			if(attachList != null ) {
				for(int i=0; i<attachList.size(); i++)
				{
					DecoratedAttachment decoAttach = new DecoratedAttachment((Attachment)attachList.get(i));
					decoAttachList.add(decoAttach);
				}
			}
			
			userAuthoredInfo = new DecoratedCompiledUserStatistics();
			userAuthoredInfo.setSiteUserId(selectedSiteUserId);
			userAuthoredInfo.setForumTitle(msg.getTopic().getOpenForum().getTitle());
			userAuthoredInfo.setTopicTitle(msg.getTopic().getTitle());
			userAuthoredInfo.setForumDate(msg.getCreated());
			userAuthoredInfo.setForumSubject(msg.getTitle());
			userAuthoredInfo.setMessage(msg.getBody());
			userAuthoredInfo.setMsgId(Long.toString(msg.getId()));
			userAuthoredInfo.setTopicId(Long.toString(msg.getTopic().getId()));
			userAuthoredInfo.setForumId(Long.toString(msg.getTopic().getOpenForum().getId()));
			userAuthoredInfo.setMsgDeleted(msg.getDeleted());
			userAuthoredInfo.setDecoAttachmentsList(decoAttachList);
						
			messageManager.markMessageReadForUser(msg.getTopic().getId(), msg.getId(), true, getCurrentUserId());
			
			statistics.add(userAuthoredInfo);
		}

		sortStatisticsByUser3(statistics);
		return statistics;
	}
	
	private String getCurrentUserId() {
		String currentUserId = SessionManager.getCurrentSessionUserId();;
		return currentUserId;
	}

	public List getUserSubjectMsgBody(){
		final List statistics = new ArrayList();

		if(selectedMsgId != null){
			try{
				Long msgId = Long.valueOf(selectedMsgId);			

				Message mesWithAttach = (Message)messageManager.getMessageByIdWithAttachments(msgId);
				Topic t = forumManager.getTopicById(mesWithAttach.getTopic().getId());
				DiscussionForum d = forumManager.getForumById(t.getOpenForum().getId());


				List decoAttachList = new ArrayList();
				List attachList = mesWithAttach.getAttachments();
				if(attachList != null ) {
					for(int i=0; i<attachList.size(); i++)
					{
						DecoratedAttachment decoAttach = new DecoratedAttachment((Attachment)attachList.get(i));
						decoAttachList.add(decoAttach);
					}
				}							
				userAuthoredInfo = new DecoratedCompiledUserStatistics();
				userAuthoredInfo.setSiteUserId(selectedSiteUserId);
				userAuthoredInfo.setForumTitle(d.getTitle());
				userAuthoredInfo.setTopicTitle(t.getTitle());
				userAuthoredInfo.setForumDate(mesWithAttach.getCreated());
				userAuthoredInfo.setForumSubject(mesWithAttach.getTitle());
				userAuthoredInfo.setMessage(mesWithAttach.getBody());
				userAuthoredInfo.setMsgId(selectedMsgId);
				userAuthoredInfo.setTopicId(Long.toString(t.getId()));
				userAuthoredInfo.setForumId(Long.toString(d.getId()));
				userAuthoredInfo.setMsgDeleted(mesWithAttach.getDeleted());
				userAuthoredInfo.setDecoAttachmentsList(decoAttachList);

				messageManager.markMessageReadForUser(t.getId(), mesWithAttach.getId(), true, getCurrentUserId());

				statistics.add(userAuthoredInfo);
			}catch(Exception e){
				LOG.error("MessageForumsStatisticsBean: getUserSubjectMsgBody: selected message Id was not of type Long");
			}
		}
		return statistics;
	}

	public List getUserReadStatistics(){
		final List<DecoratedCompiledUserStatistics> statistics = new ArrayList();

		List<Message> messages = messageManager.findReadMessagesForStudent(selectedSiteUserId);
		if (messages == null) return statistics;

		for (Message msg: messages) {
			userAuthoredInfo = new DecoratedCompiledUserStatistics();
			userAuthoredInfo.setSiteUserId(selectedSiteUserId);
			userAuthoredInfo.setForumTitle(msg.getTopic().getOpenForum().getTitle());
			userAuthoredInfo.setTopicTitle(msg.getTopic().getTitle());
			userAuthoredInfo.setForumDate(msg.getCreated());
			userAuthoredInfo.setForumSubject(msg.getTitle());
			statistics.add(userAuthoredInfo);
		}

		sortStatisticsByUser2(statistics);
		return statistics;
	}
	
	public class dMessageStatusInfo{
		private String forumTitle;
		private String topicTitle;
		private Long messageId;
		private Date messageCreated;
		private String messageTitle;
		
		public dMessageStatusInfo(String forumTitle, String topicTitle, Long messageId, Date messageCreated, String messageTitle){
			this.forumTitle = forumTitle;
			this.topicTitle = topicTitle;
			this.messageId = messageId;
			this.messageCreated = messageCreated;
			this.messageTitle = messageTitle;
		}

		public String getForumTitle() {
			return forumTitle;
		}

		public String getTopicTitle() {
			return topicTitle;
		}

		public Long getMessageId() {
			return messageId;
		}

		public Date getMessageCreated() {
			return messageCreated;
		}

		public String getMessageTitle() {
			return messageTitle;
		}

	}
	
	/**
	 * Sorting Utils
	 */
	
	private List sortStatistics(List statistics){
		Comparator comparator = determineComparator();
		Collections.sort(statistics, comparator);
		return statistics;
	}
	
	private List sortStatisticsByUser(List statistics){
		Comparator comparator = determineComparatorByUser();
		Collections.sort(statistics, comparator);
		return statistics;
	}
	
	private List sortStatisticsByUser2(List statistics){
		Comparator comparator = determineComparatorByUser2();
		Collections.sort(statistics, comparator);
		return statistics;
	}
	
	private List sortStatisticsByUser3(List statistics){
		Comparator comparator = determineComparatorByUser3();
		Collections.sort(statistics, comparator);
		return statistics;
	}
	
	
	public void toggleSort(String sortByType) {
		if (sortBy.equals(sortByType)) {
	       if (ascending) {
	    	   ascending = false;
	       } else {
	    	   ascending = true;
	       }
	    } else {
	    	sortBy = sortByType;
	    	ascending = true;
	    }
	}
	
	public void toggleSortByUser(String sortByType) {
		if (sortByUser.equals(sortByType)) {
	       if (ascendingForUser) {
	    	   ascendingForUser = false;
	       } else {
	    	   ascendingForUser = true;
	       }
	    } else {
	    	sortByUser = sortByType;
	    	ascendingForUser = true;
	    }
	}
	
	public void toggleSortByUser2(String sortByType) {
		if (sortByUser2.equals(sortByType)) {
	       if (ascendingForUser2) {
	    	   ascendingForUser2 = false;
	       } else {
	    	   ascendingForUser2 = true;
	       }
	    } else {
	    	sortByUser2 = sortByType;
	    	ascendingForUser2 = true;
	    }
	}
	
	public void toggleSortByUser3(String sortByType) {
		if (sortByUser3.equals(sortByType)) {
	       if (ascendingForUser3) {
	    	   ascendingForUser3 = false;
	       } else {
	    	   ascendingForUser3 = true;
	       }
	    } else {
	    	sortByUser3 = sortByType;
	    	ascendingForUser3 = true;
	    }
	}
	
	public String toggleNameSort()	{
		toggleSort(NAME_SORT);
		return LIST_PAGE;
	}
	
	public String toggleAuthoredSort()	{
		toggleSort(AUTHORED_SORT);
		return LIST_PAGE;
	}
	
	public String toggleReadSort()	{    
		toggleSort(READ_SORT);
		return LIST_PAGE;
	}
	    
	public String toggleUnreadSort()	{    
		toggleSort(UNREAD_SORT);
		return LIST_PAGE;
	}
	
	public String togglePercentReadSort()	{    
		toggleSort(PERCENT_READ_SORT);	    
		return LIST_PAGE;
	}
	
	
	public String toggleForumTitleSort()	{    
		toggleSortByUser(FORUM_TITLE_SORT);	    
		return FORUM_STATISTICS_USER;
	}

	public boolean isForumTitleSort() {
		if (sortByUser.equals(FORUM_TITLE_SORT))
			return true;
		return false;
	}
	
	public String toggleTopicTitleSort()	{    
		toggleSortByUser(TOPIC_TITLE_SORT);	    
		return FORUM_STATISTICS_USER;
	}

	public boolean isTopicTitleSort() {
		if (sortByUser.equals(TOPIC_TITLE_SORT))
			return true;
		return false;
	}	
	
	public String toggleDateSort()	{    
		toggleSortByUser(FORUM_DATE_SORT);	    
		return FORUM_STATISTICS_USER;
	}

	public boolean isForumDateSort() {
		if (sortByUser.equals(FORUM_DATE_SORT))
			return true;
		return false;
	}	
	
	public String toggleSubjectSort()	{    
		toggleSortByUser(FORUM_SUBJECT_SORT);	    
		return FORUM_STATISTICS_USER;
	}

	public boolean isForumSubjectSort() {
		if (sortByUser.equals(FORUM_SUBJECT_SORT))
			return true;
		return false;
	}
	
	public String toggleForumTitleSort2()	{    
		toggleSortByUser2(FORUM_TITLE_SORT2);	    
		return FORUM_STATISTICS_USER;
	}

	public boolean isForumTitleSort2() {
		if (sortByUser2.equals(FORUM_TITLE_SORT2))
			return true;
		return false;
	}	
	
	public String toggleTopicTitleSort2()	{    
		toggleSortByUser2(TOPIC_TITLE_SORT2);	    	
		return FORUM_STATISTICS_USER;
	}

	public boolean isTopicTitleSort2() {
		if (sortByUser2.equals(TOPIC_TITLE_SORT2))
			return true;
		return false;
	}	
	
	public String toggleDateSort2()	{    
		toggleSortByUser2(FORUM_DATE_SORT2);	    
		return FORUM_STATISTICS_USER;
	}

	public boolean isForumDateSort2() {
		if (sortByUser2.equals(FORUM_DATE_SORT2))
			return true;
		return false;
	}
	
	public String toggleSubjectSort2()	{    
		toggleSortByUser2(FORUM_SUBJECT_SORT2);	    
		return FORUM_STATISTICS_USER;
	}

	public boolean isForumSubjectSort2() {
		if (sortByUser2.equals(FORUM_SUBJECT_SORT2))
			return true;
		return false;
	}
	
	public String toggleDateSort3()	{    
		toggleSortByUser3(FORUM_DATE_SORT3);	    
		return FORUM_STATISTICS_ALL_AUTHORED_MSG;
	}

	public boolean isForumDateSort3() {
		if (sortByUser3.equals(FORUM_DATE_SORT3))
			return true;
		return false;
	}	
	
	public String toggleTopicTitleSort3()	{    
		toggleSortByUser3(TOPIC_TITLE_SORT3);	    	
		return FORUM_STATISTICS_ALL_AUTHORED_MSG;
	}

	public boolean isTopicTitleSort3() {
		if (sortByUser3.equals(TOPIC_TITLE_SORT3))
			return true;
		return false;
	}	
	
	public boolean isNameSort() {
		if (sortBy.equals(NAME_SORT))
			return true;
		return false;
	}
		
	public boolean isAuthoredSort() {
		if (sortBy.equals(AUTHORED_SORT))
			return true;
		return false;
	}
		
	public boolean isReadSort() {
		if (sortBy.equals(READ_SORT))
			return true;
		return false;
	}
	
	public boolean isUnreadSort() {
		if (sortBy.equals(UNREAD_SORT))
			return true;
		return false;
	}
	
	public boolean isPercentReadSort() {
		if (sortBy.equals(PERCENT_READ_SORT))
			return true;
		return false;
	}
	
	public boolean isAscending() {
		return ascending;
	}
	
	public boolean isAscendingForUser() {
		return ascendingForUser;
	}
	
	public boolean isAscendingForUser2() {
		return ascendingForUser2;
	}
	
	public boolean isAscendingForUser3() {
		return ascendingForUser3;
	}	
	
	private Comparator determineComparator(){
		if(ascending){
			if (sortBy.equals(NAME_SORT)){
				return NameComparatorAsc;
			}else if (sortBy.equals(AUTHORED_SORT)){
				return AuthoredComparatorAsc;
			}else if (sortBy.equals(READ_SORT)){
				return ReadComparatorAsc;
			}else if (sortBy.equals(UNREAD_SORT)){
				return UnreadComparatorAsc;
			}else if (sortBy.equals(PERCENT_READ_SORT)){
				return PercentReadComparatorAsc;
			}
		}else{
			if (sortBy.equals(NAME_SORT)){
				return NameComparatorDesc;
			}else if (sortBy.equals(AUTHORED_SORT)){
				return AuthoredComparatorDesc;
			}else if (sortBy.equals(READ_SORT)){
				return ReadComparatorDesc;
			}else if (sortBy.equals(UNREAD_SORT)){
				return UnreadComparatorDesc;
			}else if (sortBy.equals(PERCENT_READ_SORT)){
				return PercentReadComparatorDesc;
			}
		}
		//default return NameComparatorAsc
		return NameComparatorAsc;
	}
	
	private Comparator determineComparatorByUser(){
		if(ascendingForUser){
			if (sortByUser.equals(FORUM_TITLE_SORT)){
				return ForumTitleComparatorAsc;
			}else if (sortByUser.equals(FORUM_DATE_SORT)){
				return ForumDateComparatorAsc;
			}else if (sortByUser.equals(FORUM_SUBJECT_SORT)){
				return ForumSubjectComparatorAsc;
			}else if (sortByUser.equals(TOPIC_TITLE_SORT)){
				return TopicTitleComparatorAsc;
			}
		}else{
			if (sortByUser.equals(FORUM_TITLE_SORT)){
				return ForumTitleComparatorDesc;
			}else if (sortByUser.equals(FORUM_DATE_SORT)){
				return ForumDateComparatorDesc;
			}else if (sortByUser.equals(FORUM_SUBJECT_SORT)){
				return ForumSubjectComparatorDesc;
			}else if (sortByUser.equals(TOPIC_TITLE_SORT)){
				return TopicTitleComparatorDesc;
			}
		}
		//default return NameComparatorAsc
		return ForumDateComparatorDesc;
	}
	
	private Comparator determineComparatorByUser2(){
		if(ascendingForUser2){
			if (sortByUser2.equals(FORUM_TITLE_SORT2)){
				return ForumTitleComparatorAsc;
			}else if (sortByUser2.equals(FORUM_DATE_SORT2)){
				return ForumDateComparatorAsc;
			}else if (sortByUser2.equals(FORUM_SUBJECT_SORT2)){
				return ForumSubjectComparatorAsc;
			}else if (sortByUser2.equals(TOPIC_TITLE_SORT2)){
				return TopicTitleComparatorAsc;
			}
		}else{
			if (sortByUser2.equals(FORUM_TITLE_SORT2)){
				return ForumTitleComparatorDesc;
			}else if (sortByUser2.equals(FORUM_DATE_SORT2)){
				return ForumDateComparatorDesc;
			}else if (sortByUser2.equals(FORUM_SUBJECT_SORT2)){
				return ForumSubjectComparatorDesc;
			}else if (sortByUser2.equals(TOPIC_TITLE_SORT2)){
				return TopicTitleComparatorDesc;
			}
		}
		//default return NameComparatorAsc
		return ForumDateComparatorDesc;
	}
	
	private Comparator determineComparatorByUser3(){
		if(ascendingForUser3){
			if (sortByUser3.equals(TOPIC_TITLE_SORT3)){
				return TopicTitleComparatorAsc;
			}else if (sortByUser3.equals(FORUM_DATE_SORT3)){
				return ForumDateComparatorAsc;
			}
		}else{
			if (sortByUser3.equals(TOPIC_TITLE_SORT3)){
				return TopicTitleComparatorDesc;
			}else if (sortByUser3.equals(FORUM_DATE_SORT3)){
				return ForumDateComparatorDesc;
			}
		}
		//default return NameComparatorAsc
		return ForumDateComparatorDesc;
	}

	
	static {
		/**
		 * Comparators for DecoratedCompileMessageStatistics
		 */
		NameComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				String name1 = ((DecoratedCompiledMessageStatistics) item).getSiteUser().toUpperCase();
				String name2 = ((DecoratedCompiledMessageStatistics) anotherItem).getSiteUser().toUpperCase();
				return name1.compareTo(name2);
			}
		};
		
		AuthoredComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				int authored1 = ((DecoratedCompiledMessageStatistics) item).getAuthoredForumsAmt();
				int authored2 = ((DecoratedCompiledMessageStatistics) anotherItem).getAuthoredForumsAmt();
				return authored1 - authored2;
			}
		};
		
		ReadComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				int read1 = ((DecoratedCompiledMessageStatistics) item).getReadForumsAmt();
				int read2 = ((DecoratedCompiledMessageStatistics) anotherItem).getReadForumsAmt();
				return read1 - read2;
			}
		};
		
		UnreadComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				int unread1 = ((DecoratedCompiledMessageStatistics) item).getUnreadForumsAmt();
				int unread2 = ((DecoratedCompiledMessageStatistics) anotherItem).getUnreadForumsAmt();
				return unread1 - unread2;
			}
		};
		
		PercentReadComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				double percentRead1 = ((DecoratedCompiledMessageStatistics) item).getPercentReadForumsAmt();
				double percentRead2 = ((DecoratedCompiledMessageStatistics) anotherItem).getPercentReadForumsAmt();
				if(percentRead1 == percentRead2){
					return 0;
				}
				else if(percentRead1 < percentRead2){
					return -1;
				}
				else {
					return 1;
				}
			}
		};
		NameComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				String name1 = ((DecoratedCompiledMessageStatistics) item).getSiteUser().toUpperCase();
				String name2 = ((DecoratedCompiledMessageStatistics) anotherItem).getSiteUser().toUpperCase();
				return name2.compareTo(name1);
			}
		};
		
		ForumTitleComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				String title1 = ((DecoratedCompiledUserStatistics) item).getForumTitle().toUpperCase();
				String title2 = ((DecoratedCompiledUserStatistics) anotherItem).getForumTitle().toUpperCase();
				return title1.compareTo(title2);
			}
		};
		
		TopicTitleComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				String title1 = ((DecoratedCompiledUserStatistics) item).getTopicTitle().toUpperCase();
				String title2 = ((DecoratedCompiledUserStatistics) anotherItem).getTopicTitle().toUpperCase();
				return title1.compareTo(title2);
			}
		};
		
		ForumDateComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				Date date1 = ((DecoratedCompiledUserStatistics) item).getForumDate();
				Date date2 = ((DecoratedCompiledUserStatistics) anotherItem).getForumDate();
				return date1.compareTo(date2);
			}
		};
		
		ForumSubjectComparatorAsc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				String subject1 = ((DecoratedCompiledUserStatistics) item).getForumSubject().toUpperCase();
				String subject2 = ((DecoratedCompiledUserStatistics) anotherItem).getForumSubject().toUpperCase();
				return subject1.compareTo(subject2);
			}
		};

		AuthoredComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				int authored1 = ((DecoratedCompiledMessageStatistics) item).getAuthoredForumsAmt();
				int authored2 = ((DecoratedCompiledMessageStatistics) anotherItem).getAuthoredForumsAmt();
				return authored2 - authored1;
			}
		};
		
		ReadComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				int read1 = ((DecoratedCompiledMessageStatistics) item).getReadForumsAmt();
				int read2 = ((DecoratedCompiledMessageStatistics) anotherItem).getReadForumsAmt();
				return read2 - read1;
			}
		};
		
		UnreadComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				int unread1 = ((DecoratedCompiledMessageStatistics) item).getUnreadForumsAmt();
				int unread2 = ((DecoratedCompiledMessageStatistics) anotherItem).getUnreadForumsAmt();
				return unread2 - unread1;
			}
		};
		
		PercentReadComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				double percentRead1 = ((DecoratedCompiledMessageStatistics) item).getPercentReadForumsAmt();
				double percentRead2 = ((DecoratedCompiledMessageStatistics) anotherItem).getPercentReadForumsAmt();
				if(percentRead1 == percentRead2){
					return 0;
				}
				else if(percentRead1 < percentRead2){
					return 1;
				}
				else {
					return -1;
				}
			}
		};
		
		/**
		 * Comparator for DecoratedCompiledUserStatistics
		 */
		
		DateComparaterDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				Date date1 = ((DecoratedCompiledUserStatistics) item).getForumDate();
				Date date2 = ((DecoratedCompiledUserStatistics) anotherItem).getForumDate();
				return date2.compareTo(date1);
			}
		};
		
		ForumTitleComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				String title1 = ((DecoratedCompiledUserStatistics) item).getForumTitle().toUpperCase();
				String title2 = ((DecoratedCompiledUserStatistics) anotherItem).getForumTitle().toUpperCase();
				return title2.compareTo(title1);
			}
		};
		
		TopicTitleComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				String title1 = ((DecoratedCompiledUserStatistics) item).getTopicTitle().toUpperCase();
				String title2 = ((DecoratedCompiledUserStatistics) anotherItem).getTopicTitle().toUpperCase();
				return title2.compareTo(title1);
			}
		};
		
		ForumDateComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				Date date1 = ((DecoratedCompiledUserStatistics) item).getForumDate();
				Date date2 = ((DecoratedCompiledUserStatistics) anotherItem).getForumDate();
				return date2.compareTo(date1);
			}
		};
		
		ForumSubjectComparatorDesc = new Comparator(){
			public int compare(Object item, Object anotherItem){
				String subject1 = ((DecoratedCompiledUserStatistics) item).getForumSubject().toUpperCase();
				String subject2 = ((DecoratedCompiledUserStatistics) anotherItem).getForumSubject().toUpperCase();
				return subject2.compareTo(subject1);
			}
		};
	}
	
	/**
	 * Actions
	 */
	
	/**
	   * @return
	   */
	public String processActionStatisticsUser()
	{
		LOG.debug("processActionStatisticsUser");
		
		selectedSiteUserId = getExternalParameterByKey(SITE_USER_ID);
		selectedSiteUser = getUserName(selectedSiteUserId);
		
		isLastParticipant = false;
		isFirstParticipant = false;
		
		Map<String, String> userIdName = getUserIdName();
		Set<String> userIdSet = userIdName.keySet();
		String[] userIdArray =(String[]) userIdSet.toArray(new String[userIdSet.size()]);
		int currentPosition = getCurrentPosition(userIdArray, selectedSiteUserId);
		
		if(currentPosition == 0) {
			isFirstParticipant = true;
		}
		if(currentPosition == userIdArray.length - 1) {
			isLastParticipant = true;
		}
		
		return FORUM_STATISTICS_USER;
	}


	private String getUserName(String userId) {
		String userName="";
		try
		{
			User user=UserDirectoryService.getUser(userId) ;
			if (ServerConfigurationService.getBoolean("msg.displayEid", true)) {
				if(user != null) {
					userName= user.getLastName() + ", " + user.getFirstName() + " (" + user.getDisplayId() + ")" ;
				}
			} else {
				if(user != null) {
					userName = user.getLastName() + ", " + user.getFirstName();
				}
			}
		}
		catch (UserNotDefinedException e) {

			e.printStackTrace();
		}
		
		return userName;
	}
	
	public String processActionBackToUser() {
		return FORUM_STATISTICS_USER;
	}
	


	// **************************************** helper methods**********************************

	private String getExternalParameterByKey(String parameterId)
	{    
		ExternalContext context = FacesContext.getCurrentInstance()
		.getExternalContext();
		Map paramMap = context.getRequestParameterMap();

		return (String) paramMap.get(parameterId);    
	}


	public String getButtonUserName() {
		String userName;
		String firstName;
		String lastName;
		int firstIndex = 0;
		int secondIndex = 0;
		if (ServerConfigurationService.getBoolean("msg.displayEid", true)) {
			firstIndex = selectedSiteUser.indexOf(",");
			secondIndex = selectedSiteUser.indexOf("(");
			lastName = selectedSiteUser.substring(0, firstIndex);
			firstName = selectedSiteUser.substring(firstIndex + 2, secondIndex-1);
			userName = firstName + " " + lastName;	
		} else {
			firstIndex = selectedSiteUser.indexOf(",");
			lastName = selectedSiteUser.substring(0, firstIndex);
			firstName = selectedSiteUser.substring(firstIndex+2);		
			userName = firstName + " " + lastName;			
		}
		buttonUserName = getResourceBundleString("return_to_statistics" , new Object[] {userName}) ;
		return buttonUserName;
	}

	public void setButtonUserName(String buttonUserName) {
		this.buttonUserName = buttonUserName;
	}

	public static String getResourceBundleString(String key) 
	{
		final ResourceLoader rb = new ResourceLoader(MESSAGECENTER_BUNDLE);
		return rb.getString(key);
	}

	public static String getResourceBundleString(String key, Object[] args) {
		final ResourceLoader rb = new ResourceLoader(MESSAGECENTER_BUNDLE);
		return rb.getFormattedMessage(key, args);
	}

	public String getSelectedMsgId() {
		return selectedMsgId;
	}

	public void setSelectedMsgId(String selectedMsgId) {
		this.selectedMsgId = selectedMsgId;
	}

	public String getSelectedMsgSubject() {
		return selectedMsgSubject;
	}

	public void setSelectedMsgSubject(String selectedMsgSubject) {
		this.selectedMsgSubject = selectedMsgSubject;
	}
	
	public String processActionDisplayMsgBody() {
		LOG.debug("processActionDisplayMsgBody");

		selectedMsgId = getExternalParameterByKey("msgId");
		Message message =(Message) messageManager.getMessageById(Long.parseLong(selectedMsgId));
		selectedMsgSubject = message.getTitle();
		
		return FORUM_STATISTICS_MSG;
	}

	public String getSelectedForumTitle() {
		return selectedForumTitle;
	}

	public void setSelectedForumTitle(String selectedForumTitle) {
		this.selectedForumTitle = selectedForumTitle;
	}

	public String getSelectedTopicTitle() {
		return selectedTopicTitle;
	}

	public void setSelectedTopicTitle(String selectedTopicTitle) {
		this.selectedTopicTitle = selectedTopicTitle;
	}
	
	public Map<String, String> getUserIdName() {
		Map<String, String> idNameMap = new LinkedHashMap<String, String>();
		List allUserInfo = getAllUserStatistics();
		Iterator allUserInfoIter = allUserInfo.iterator();

		while(allUserInfoIter.hasNext() ) {
			DecoratedCompiledMessageStatistics userInfo = (DecoratedCompiledMessageStatistics) allUserInfoIter.next();

			idNameMap.put(userInfo.getSiteUserId(), userInfo.getSiteUser());
		}
		return idNameMap;
	}

	private int getCurrentPosition(Object[] userIdArray, String userId)  {
		int currentPosition = -1;
		for (int i =0; i < userIdArray.length; i++) {
			if(userIdArray[i].equals(userId)) {
				currentPosition = i;
				break;
			}
		}
		return currentPosition;
	}
	
	public String processDisplayNextParticipant() {
		isLastParticipant = false;
		isFirstParticipant = false;
		
		Map<String, String> userIdName = getUserIdName();
		Set<String> userIdSet = userIdName.keySet();
		String[] userIdArray =(String[]) userIdSet.toArray(new String[userIdSet.size()]);
		int currentPosition = getCurrentPosition(userIdArray, selectedSiteUserId);
		
		if(currentPosition < userIdArray.length-1) {
			selectedSiteUserId = userIdArray[currentPosition+1];
			selectedSiteUser = (String)userIdName.get(selectedSiteUserId);
		}
		
		if(currentPosition == userIdArray.length - 2) {
			isLastParticipant = true;
		}
		return FORUM_STATISTICS_USER;
		
	}
	
	public String processDisplayPreviousParticipant() {		
		isLastParticipant = false;
		isFirstParticipant = false;
		
		Map<String, String> userIdName = getUserIdName();
		Set<String> userIdSet = userIdName.keySet();
		String[] userIdArray =(String[]) userIdSet.toArray(new String[userIdSet.size()]);
		int currentPosition = getCurrentPosition(userIdArray, selectedSiteUserId);
		
		if(currentPosition > 0) {
			selectedSiteUserId = userIdArray[currentPosition -1];
			selectedSiteUser = (String)userIdName.get(selectedSiteUserId);
		}
		
		if(currentPosition == 1) {
			isFirstParticipant = true;
		}
		return FORUM_STATISTICS_USER;
	}
			

	public boolean getIsFirstParticipant() {
		return isFirstParticipant;
	}

	public void setFirstParticipant(boolean isFirstParticipant) {
		this.isFirstParticipant = isFirstParticipant;
	}

	public boolean getIsLastParticipant() {
		return isLastParticipant;
	}

	public void setLastParticipant(boolean isLastParticipant) {
		this.isLastParticipant = isLastParticipant;
	}
}
