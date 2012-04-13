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

package org.sakaiproject.profile2.tool.models;

import java.io.Serializable;

import org.apache.wicket.Component;

/**
 * Simple model to back the action behind adding/removing/confirming/ignoring friend requests
 * Given to the modal windows, they then set the attributes and the calling page knows what to do based on these attributes.
 * To be used ONLY by the Profile2 tool.
 * 
 * <p>DO NOT USE THIS YOURSELF.</p>
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 */

public class FriendAction implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean requested;
	private boolean confirmed;
	private boolean removed;
	private boolean ignored;
	private Component updateThisComponentOnSuccess;
	
	public boolean isRequested() {
		return requested;
	}
	public void setRequested(boolean requested) {
		this.requested = requested;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
	
	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}
	public boolean isIgnored() {
		return ignored;
	}
	
	
	public Component getUpdateThisComponentOnSuccess() {
		return updateThisComponentOnSuccess;
	}
	public void setUpdateThisComponentOnSuccess(
			Component updateThisComponentOnSuccess) {
		this.updateThisComponentOnSuccess = updateThisComponentOnSuccess;
	}
	/**
	 * Default constructor
	 */
	public FriendAction() {
	
	}
	
	
	
}	