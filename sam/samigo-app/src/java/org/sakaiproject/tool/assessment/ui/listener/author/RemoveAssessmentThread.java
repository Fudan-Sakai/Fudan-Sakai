/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/listener/author/RemoveAssessmentThread.java $
 * $Id: RemoveAssessmentThread.java 76735 2010-04-27 00:09:33Z ktsao@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008 The Sakai Foundation
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

package org.sakaiproject.tool.assessment.ui.listener.author;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.event.cover.EventTrackingService;
import org.sakaiproject.thread_local.cover.ThreadLocalManager;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.tool.cover.SessionManager;

/**
 * Remove an assessment on a background thread so it's not taking time from the initiating end-user request thread.
 * 
 * @author Ed Smiley
 */
public class RemoveAssessmentThread extends Thread
{
	private static Log log = LogFactory.getLog(RemoveAssessmentThread.class);

	private String assessmentId;

	private String userId;

	public RemoveAssessmentThread(String assessmentId, String userId)
	{
		this.assessmentId = assessmentId;
		this.userId = userId;
	}

	public void run()
	{
		try
		{
			// bind the current user to the thread's session (created if needed) so security etc. works if called
			Session s = SessionManager.getCurrentSession();
			if (s != null)
			{
				s.setUserId(userId);
			}
			else
			{
				log.warn("run - no SessionManager.getCurrentSession, cannot set user");
			}

			AssessmentService assessmentService = new AssessmentService();
			log.info("** remove assessmentId= " + this.assessmentId);
			assessmentService.removeAssessment(this.assessmentId);
			EventTrackingService.post(EventTrackingService.newEvent("sam.assessment.remove", "assessmentId=" + assessmentId, true));
		      
		}
		finally
		{
			// clear out any current thread bound objects
			ThreadLocalManager.clear();
		}
	}
}
