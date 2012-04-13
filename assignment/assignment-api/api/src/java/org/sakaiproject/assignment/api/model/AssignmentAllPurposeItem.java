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
 * The AssignmentSupplementItem is to store additional information for the assignment. Candidates include model answers, instructor notes, grading guidelines, etc.
 * @author zqian
 *
 */
public class AssignmentAllPurposeItem extends AssignmentSupplementItemWithAttachment{
	
	private String assignmentId;
	private String title;
	private String text;
	private Date releaseDate;	// the start showing date
	private Date retractDate; // the end showing date
	private boolean hide;

	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Date getRetractDate() {
		return retractDate;
	}
	public void setRetractDate(Date retractDate) {
		this.retractDate = retractDate;
	}
	public boolean getHide()
	{
		return hide;
	}
	public void setHide(boolean hide) {
		this.hide = hide;
	}
	
	public AssignmentAllPurposeItem() {
		super();
	}
	
	/** access **/
	private Set<AssignmentAllPurposeItemAccess> accessSet;	// the access set
	public Set<AssignmentAllPurposeItemAccess> getAccessSet() {
		return accessSet;
	}
	public void setAccessSet(
			Set<AssignmentAllPurposeItemAccess> accessSet) {
		this.accessSet = accessSet;
	}
}
