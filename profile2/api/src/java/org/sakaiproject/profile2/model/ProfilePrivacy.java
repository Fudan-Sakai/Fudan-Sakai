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
public class ProfilePrivacy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EntityId
	private String userUuid;
	private int profileImage;
	private int basicInfo;
	private int contactInfo;
	private int businessInfo;
	private int personalInfo;
	private boolean showBirthYear;
	private int myFriends;
	private int myStatus;
	private int myPictures;
	private int messages;
	private int studentInfo;
	private int staffInfo;
	private int socialNetworkingInfo;
	private int myKudos;
	
	/** 
	 * Empty constructor
	 */
	public ProfilePrivacy(){
	}
	

	public String getUserUuid() {
		return userUuid;
	}


	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}


	public int getProfileImage() {
		return profileImage;
	}


	public void setProfileImage(int profileImage) {
		this.profileImage = profileImage;
	}


	public int getBasicInfo() {
		return basicInfo;
	}


	public void setBasicInfo(int basicInfo) {
		this.basicInfo = basicInfo;
	}


	public int getContactInfo() {
		return contactInfo;
	}


	public void setContactInfo(int contactInfo) {
		this.contactInfo = contactInfo;
	}

	public int getBusinessInfo() {
		return businessInfo;
	}
	
	public void setBusinessInfo(int businessInfo) {
		this.businessInfo = businessInfo;
	}
	
	public int getPersonalInfo() {
		return personalInfo;
	}


	public void setPersonalInfo(int personalInfo) {
		this.personalInfo = personalInfo;
	}


	public void setShowBirthYear(boolean showBirthYear) {
		this.showBirthYear = showBirthYear;
	}

	public boolean isShowBirthYear() {
		return showBirthYear;
	}


	public void setMyFriends(int myFriends) {
		this.myFriends = myFriends;
	}

	public int getMyFriends() {
		return myFriends;
	}

	public void setMyStatus(int myStatus) {
		this.myStatus = myStatus;
	}

	public int getMyStatus() {
		return myStatus;
	}
	
	public void setMyPictures(int myPictures) {
		this.myPictures = myPictures;
	}
	
	public int getMyPictures() {
		return myPictures;
	}

	public void setMessages(int messages) {
		this.messages = messages;
	}

	public int getMessages() {
		return messages;
	}

	public void setStaffInfo(int staffInfo) {
		this.staffInfo = staffInfo;
	}
	
	public int getStaffInfo() {
		return staffInfo;
	}
	
	public void setStudentInfo(int studentInfo) {
		this.studentInfo = studentInfo;
	}

	public int getStudentInfo() {
		return studentInfo;
	}

	public void setSocialNetworkingInfo(int socialNetworkingInfo) {
		this.socialNetworkingInfo = socialNetworkingInfo;
	}
	
	public int getSocialNetworkingInfo() {
		return socialNetworkingInfo;
	}
	
	public void setMyKudos(int myKudos) {
		this.myKudos = myKudos;
	}

	public int getMyKudos() {
		return myKudos;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
