/**
 * Copyright (c) 2008-2010 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sakaiproject.profile2.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Friend.java
 * 
 * This is a model for storing information about a friend of a user. 
 * It has a limited number of fields and is only ever populated when a List of friends for a given user is required
 * with each friend having one of these objects for fast access to the info required.
 */


public class Friend implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userUuid;
	private String displayName;
	private String statusMessage;
	private Date statusDate;
	private boolean confirmed;
	private Date requestedDate;

	
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	public Date getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	
	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}
	public Date getRequestedDate() {
		return requestedDate;
	}

}
