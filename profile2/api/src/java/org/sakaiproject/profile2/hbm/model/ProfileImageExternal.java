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

package org.sakaiproject.profile2.hbm.model;

import java.io.Serializable;


/**
 * Hibernate model for an external (URL) profile image
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 *
 */
public class ProfileImageExternal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userUuid;
	private String mainUrl;
	private String thumbnailUrl; 

	
	/** 
	 * Empty constructor
	 */
	public ProfileImageExternal(){
	}
	
	/**
	 * Full constructor
	 */
	public ProfileImageExternal(String userUuid, String mainUrl, String thumbnailUrl){
		this.userUuid=userUuid;
		this.mainUrl=mainUrl;
		this.thumbnailUrl=thumbnailUrl;
	}
	
	/**
	 * Minimal constructor for when only one URL
	 */
	public ProfileImageExternal(String userUuid, String mainUrl){
		this.userUuid=userUuid;
		this.mainUrl=mainUrl;
	}
	
	

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getMainUrl() {
		return mainUrl;
	}

	public void setMainUrl(String mainUrl) {
		this.mainUrl = mainUrl;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	
}
