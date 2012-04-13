/**********************************************************************************
 * $URL:  $
 * $Id:  $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 The Sakai Foundation
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
package org.sakaiproject.assignment.api.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * To provide sample answers to student
 * @author zqian
 *
 */
public class AssignmentModelAnswerItem extends AssignmentSupplementItemWithAttachment {

	/************* constructors ***********************/
	public AssignmentModelAnswerItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/*************** attributes and methods *************/
	
	/** assignment id **/
	private String assignmentId;

	public String getAssignmentId()
	{
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId)
	{
		this.assignmentId = assignmentId;
	}
	
	/** text **/
	public String text;
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	
	/** when to show the model answer to student **/
	private int showTo;

	public int getShowTo() {
		return showTo;
	}
	public void setShowTo(int showTo) {
		this.showTo = showTo;
	}
}
