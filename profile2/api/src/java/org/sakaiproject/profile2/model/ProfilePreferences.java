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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.sakaiproject.entitybroker.entityprovider.annotations.EntityId;


/**
 * Hibernate and EntityProvider model
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 *
 */
public class ProfilePreferences implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EntityId
	private String userUuid;
	private boolean requestEmailEnabled;
	private boolean confirmEmailEnabled;
	private boolean messageNewEmailEnabled;
	private boolean messageReplyEmailEnabled;
	private boolean useOfficialImage;
	private boolean showKudos;
	private boolean showGalleryFeed;

	/** 
	 * Empty constructor
	 */
	public ProfilePreferences(){
	}
	
	
	public String getUserUuid() {
		return userUuid;
	}


	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public void setRequestEmailEnabled(boolean requestEmailEnabled) {
		this.requestEmailEnabled = requestEmailEnabled;
	}

	public boolean isRequestEmailEnabled() {
		return requestEmailEnabled;
	}

	public void setConfirmEmailEnabled(boolean confirmEmailEnabled) {
		this.confirmEmailEnabled = confirmEmailEnabled;
	}

	public boolean isConfirmEmailEnabled() {
		return confirmEmailEnabled;
	}
	public void setMessageNewEmailEnabled(boolean messageNewEmailEnabled) {
		this.messageNewEmailEnabled = messageNewEmailEnabled;
	}

	public boolean isMessageNewEmailEnabled() {
		return messageNewEmailEnabled;
	}
	public void setMessageReplyEmailEnabled(boolean messageReplyEmailEnabled) {
		this.messageReplyEmailEnabled = messageReplyEmailEnabled;
	}
	
	public boolean isMessageReplyEmailEnabled() {
		return messageReplyEmailEnabled;
	}

	public boolean isUseOfficialImage() {
		return useOfficialImage;
	}

	public void setUseOfficialImage(boolean useOfficialImage) {
		this.useOfficialImage = useOfficialImage;
	}
	
	public boolean isShowKudos() {
		return showKudos;
	}

	public void setShowKudos(boolean showKudos) {
		this.showKudos = showKudos;
	}
	
	public boolean isShowGalleryFeed() {
		return showGalleryFeed;
	}


	public void setShowGalleryFeed(boolean showGalleryFeed) {
		this.showGalleryFeed = showGalleryFeed;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	

	

}
