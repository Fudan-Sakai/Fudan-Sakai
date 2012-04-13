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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the model for a user's profile
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 *
 */
public class UserProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userUuid;
	private String displayName;
	private String nickname;
	private Date dateOfBirth;
	private String birthday;
	private String birthdayDisplay;
	private String email;
	private String position;
	private String department;
	private String school;
	private String room;
	private String homepage;
	private String workphone;
	private String homephone;
	private String mobilephone;
	private String facsimile;
	private String favouriteBooks;
	private String favouriteTvShows;
	private String favouriteMovies;
	private String favouriteQuotes;
	private String personalSummary;
	private String course;
	private String subjects;
	private String staffProfile;
	private String universityProfileUrl; 
	private String academicProfileUrl; 
	private String publications;
	private String businessBiography;
	
	private boolean locked;
	
	private ProfileStatus status;
	private List<CompanyProfile> companyProfiles;
	private SocialNetworkingInfo socialInfo;
	
	/* 
	 * This is an EntityBroker URL that can be used to get directly to a user's profile image. URL is open, but privacy is still checked.
	 */
	private String imageUrl; 
	private String imageThumbUrl; 
	
	private Map<String, String> props;

	

	/**
	 * Basic constructor
	 */
	public UserProfile() {
	}
	
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthdayDisplay() {
		return birthdayDisplay;
	}

	public void setBirthdayDisplay(String birthdayDisplay) {
		this.birthdayDisplay = birthdayDisplay;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getWorkphone() {
		return workphone;
	}

	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}

	public String getHomephone() {
		return homephone;
	}

	public void setHomephone(String homephone) {
		this.homephone = homephone;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public void setFacsimile(String facsimile) {
		this.facsimile = facsimile;
	}

	public String getFacsimile() {
		return facsimile;
	}

	public String getFavouriteBooks() {
		return favouriteBooks;
	}

	public void setFavouriteBooks(String favouriteBooks) {
		this.favouriteBooks = favouriteBooks;
	}

	public String getFavouriteTvShows() {
		return favouriteTvShows;
	}

	public void setFavouriteTvShows(String favouriteTvShows) {
		this.favouriteTvShows = favouriteTvShows;
	}

	public String getFavouriteMovies() {
		return favouriteMovies;
	}

	public void setFavouriteMovies(String favouriteMovies) {
		this.favouriteMovies = favouriteMovies;
	}

	public String getFavouriteQuotes() {
		return favouriteQuotes;
	}

	public void setFavouriteQuotes(String favouriteQuotes) {
		this.favouriteQuotes = favouriteQuotes;
	}

	/**
	 * Please use <code>getPersonalSummary</code> for this field.
	 * 
	 * @deprecated
	 */
	public String getOtherInformation() {
		return personalSummary;
	}
	
	/**
	 * Please use <code>setPersonalSummary</code> for this field.
	 * 
	 * @deprecated
	 */
	public void setOtherInformation(String otherInformation) {
		this.personalSummary = otherInformation;
	}
	
	public String getPersonalSummary() {
		return personalSummary;
	}

	public void setPersonalSummary(String personalSummary) {
		this.personalSummary = personalSummary;
	}
	
	public String getCourse() {
		return course;
	}
	
	public void setCourse(String course) {
		this.course = course;
	}
	
	public String getSubjects() {
		return subjects;
	}
	
	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getStaffProfile() {
		return staffProfile;
	}

	public void setStaffProfile(String staffProfile) {
		this.staffProfile = staffProfile;
	}

	public String getUniversityProfileUrl() {
		return universityProfileUrl;
	}

	public void setUniversityProfileUrl(String universityProfileUrl) {
		this.universityProfileUrl = universityProfileUrl;
	}

	public String getAcademicProfileUrl() {
		return academicProfileUrl;
	}

	public void setAcademicProfileUrl(String academicProfileUrl) {
		this.academicProfileUrl = academicProfileUrl;
	}

	public String getPublications() {
		return publications;
	}

	public void setPublications(String publications) {
		this.publications = publications;
	}
	
	public String getBusinessBiography() {
		return businessBiography;
	}
	
	public void setBusinessBiography(String businessBiography) {
		this.businessBiography = businessBiography;
	}
	
	public void addCompanyProfile(CompanyProfile companyProfile) {
		companyProfiles.add(companyProfile);
	}
	
	public List<CompanyProfile> getCompanyProfiles() {
		return companyProfiles;
	}
	
	public void removeCompanyProfile(CompanyProfile companyProfile) {
		companyProfiles.remove(companyProfile);
	}
	
	public void setCompanyProfiles(List<CompanyProfile> companyProfiles) {
		this.companyProfiles = companyProfiles;
	}
	
	public void setSocialInfo(SocialNetworkingInfo socialInfo) {
		this.socialInfo = socialInfo;
	}

	public SocialNetworkingInfo getSocialInfo() {
		return socialInfo;
	}

	

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setStatus(ProfileStatus status) {
		this.status = status;
	}

	public ProfileStatus getStatus() {
		return status;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setImageThumbUrl(String imageThumbUrl) {
		this.imageThumbUrl = imageThumbUrl;
	}

	public String getImageThumbUrl() {
		return imageThumbUrl;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}

	public Map<String, String> getProps() {
		return props;
	}
	
	/* for setting properties into the props Map */
	public void setProperty(String key, String value) {
        if (props == null) {
            props = new HashMap<String, String>();
        }
        props.put(key, value);
    }

    public String getProperty(String key) {
        if (props == null) {
            return null;
        }
        return props.get(key);
    }
	
	
	
	
	
}
