package org.sakaiproject.profile2.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.sakaiproject.profile2.dao.ProfileDao;
import org.sakaiproject.profile2.hbm.model.ProfileImageExternal;
import org.sakaiproject.profile2.hbm.model.ProfileImageOfficial;
import org.sakaiproject.profile2.hbm.model.ProfileImageUploaded;
import org.sakaiproject.profile2.model.GalleryImage;
import org.sakaiproject.profile2.model.MimeTypeByteArray;
import org.sakaiproject.profile2.model.Person;
import org.sakaiproject.profile2.model.ProfileImage;
import org.sakaiproject.profile2.model.ProfilePreferences;
import org.sakaiproject.profile2.model.ProfilePrivacy;
import org.sakaiproject.profile2.util.Messages;
import org.sakaiproject.profile2.util.ProfileConstants;
import org.sakaiproject.profile2.util.ProfileUtils;
import org.sakaiproject.user.api.User;

/**
 * Implementation of ProfileImageLogic API
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public class ProfileImageLogicImpl implements ProfileImageLogic {

	private static final Logger log = Logger.getLogger(ProfileImageLogicImpl.class);

	
	/**
 	 * {@inheritDoc}
 	 */
	public ProfileImage getProfileImage(String userUuid, ProfilePreferences prefs, ProfilePrivacy privacy, int size) {
		return getProfileImage(userUuid, prefs, privacy, size, null);
	}
	
	/**
 	 * {@inheritDoc}
 	 */
	public ProfileImage getProfileImage(String userUuid, ProfilePreferences prefs, ProfilePrivacy privacy, int size, String siteId) {
		
		ProfileImage image = new ProfileImage();
		boolean allowed = false;
		boolean isSameUser = false;
		String officialImageSource;
		
		String defaultImageUrl;
		if (ProfileConstants.PROFILE_IMAGE_THUMBNAIL == size) {
			defaultImageUrl = getUnavailableImageThumbnailURL();
		} else {
			defaultImageUrl = getUnavailableImageURL();
		}
		
		//get current user
		String currentUserUuid = sakaiProxy.getCurrentUserId();
		if(StringUtils.equals(userUuid, currentUserUuid)){
			isSameUser = true;
		}
		
		//if no current user we are not logged in (could be from entity provider)
		if(StringUtils.isBlank(currentUserUuid)){
			// this is where the logic for handling public profile images will go
			//right now we throw a security exception.
			throw new SecurityException("Must be logged in to request a profile image.");
		}
		
		//check prefs supplied was valid, if given
		if(prefs != null && !StringUtils.equals(userUuid, prefs.getUserUuid())) {
			log.error("ProfilePreferences data supplied was not for user: " + userUuid);
			image.setExternalImageUrl(defaultImageUrl);
			image.setAltText(getAltText(userUuid, isSameUser, false));
			return image;
		}
		
		//check privacy supplied was valid, if given
		if(privacy != null && !StringUtils.equals(userUuid, privacy.getUserUuid())) {
			log.error("ProfilePrivacy data supplied was not for user: " + userUuid);
			image.setExternalImageUrl(defaultImageUrl);
			image.setAltText(getAltText(userUuid, isSameUser, false));
			return image;
		}
		
		//check if same user
		if(isSameUser){
			allowed = true;
		}
		
		
		//if we have a siteId and it's not a my workspace site, check if the current user has permissions to view the image
		if(StringUtils.isNotBlank(siteId)){
			if(!sakaiProxy.isUserMyWorkspace(siteId)) {
				log.debug("checking if user: " + currentUserUuid + " has permissions in site: " + siteId);
				allowed = sakaiProxy.isUserAllowedInSite(currentUserUuid, ProfileConstants.ROSTER_VIEW_PHOTO, siteId);
			}
		}
		
		//if not allowed yet, check we have a privacy record, if not, get one
		if(!allowed && privacy == null) {
			privacy = privacyLogic.getPrivacyRecordForUser(userUuid);
			//if still null, default image
			if(privacy == null) {
				log.error("Couldn't retrieve ProfilePrivacy data for user: " + userUuid + ". Using default image.");
				image.setExternalImageUrl(defaultImageUrl);
				image.setAltText(getAltText(userUuid, isSameUser, false));
				return image;
			} 
		}
		
		//if not allowed, check privacy record
		if(!allowed) {
			boolean friend = connectionsLogic.isUserXFriendOfUserY(userUuid, currentUserUuid);
			allowed = privacyLogic.isUserXProfileImageVisibleByUserY(userUuid, privacy, currentUserUuid, friend)
					|| sakaiProxy.isAdminUser();
		}
		
		//default if still not allowed
		if(!allowed){
			image.setExternalImageUrl(defaultImageUrl);
			image.setAltText(getAltText(userUuid, isSameUser, false));
			return image;
		}
		
		//lookup global image setting, this will be used if no preferences were supplied.
		int imageType = sakaiProxy.getProfilePictureType();
		
		//if we have no prefs, try to get one, it won't be considered if it is still null.
		if(prefs == null){
			prefs = preferencesLogic.getPreferencesRecordForUser(userUuid);
		}
		
		//if we have prefs and the conditions are set for a user to be able to make a choice, get the pref.
		if(prefs != null && sakaiProxy.isUsingOfficialImageButAlternateSelectionEnabled()) {
			if(prefs.isUseOfficialImage()){
				imageType = ProfileConstants.PICTURE_SETTING_OFFICIAL;
			}
		}
		
		//get the image based on the global type/preference
		switch (imageType) {
			case ProfileConstants.PICTURE_SETTING_UPLOAD:
				MimeTypeByteArray mtba = getUploadedProfileImage(userUuid, size);
				
				//if no uploaded image, use the default image url
				if(mtba == null || mtba.getBytes() == null) {
					image.setExternalImageUrl(defaultImageUrl);
				} else {
					image.setUploadedImage(mtba.getBytes());
					image.setMimeType(mtba.getMimeType());
				}
				image.setAltText(getAltText(userUuid, isSameUser, true));
			break;
			
			case ProfileConstants.PICTURE_SETTING_URL: 
				image.setExternalImageUrl(getExternalProfileImageUrl(userUuid, size));
				image.setAltText(getAltText(userUuid, isSameUser, true));
			break;
			
			case ProfileConstants.PICTURE_SETTING_OFFICIAL: 
				officialImageSource = sakaiProxy.getOfficialImageSource();
				
				//check source and get appropriate value
				if(StringUtils.equals(officialImageSource, ProfileConstants.OFFICIAL_IMAGE_SETTING_URL)){
					image.setOfficialImageUrl(getOfficialImageUrl(userUuid));
				} else if(StringUtils.equals(officialImageSource, ProfileConstants.OFFICIAL_IMAGE_SETTING_PROVIDER)){
					String data = getOfficialImageEncoded(userUuid);
					if(StringUtils.isBlank(data)) {
						image.setExternalImageUrl(defaultImageUrl);
					}
				}
				image.setAltText(getAltText(userUuid, isSameUser, true));
			break;
			
			default:
				image.setExternalImageUrl(defaultImageUrl);
				image.setAltText(getAltText(userUuid, isSameUser, false));
			break;
				
		}
		
		return image;
	}
	
	/**
 	 * {@inheritDoc}
 	 */
	public ProfileImage getProfileImage(Person person, int size) {
		return getProfileImage(person.getUuid(), person.getPreferences(), person.getPrivacy(), size, null);
	}
	
	/**
 	 * {@inheritDoc}
 	 */
	public ProfileImage getProfileImage(Person person, int size, String siteId) {
		return getProfileImage(person.getUuid(), person.getPreferences(), person.getPrivacy(), size, siteId);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public boolean setUploadedProfileImage(String userUuid, byte[] imageBytes, String mimeType, String fileName) {
		
		//check auth and get currentUserUuid
		String currentUserUuid = sakaiProxy.getCurrentUserId();
		if(currentUserUuid == null) {
			throw new SecurityException("You must be logged in to update a user's profile image.");
		}
		
		//check admin, or the currentUser and given uuid match
		if(!sakaiProxy.isSuperUser() && !StringUtils.equals(currentUserUuid, userUuid)) {
			throw new SecurityException("Not allowed to save.");
		}
		
		//check image is actually allowed to be changed
		if(!sakaiProxy.isProfilePictureChangeEnabled()) {
			log.warn("Profile image changes are not permitted as per sakai.properties setting 'profile2.picture.change.enabled'.");
			return false;
		}
		
		/*
		 * MAIN PROFILE IMAGE
		 */
		//scale image
		imageBytes = ProfileUtils.scaleImage(imageBytes, ProfileConstants.MAX_IMAGE_XY);
		 
		//create resource ID
		String mainResourceId = sakaiProxy.getProfileImageResourcePath(userUuid, ProfileConstants.PROFILE_IMAGE_MAIN);
		
		//save, if error, log and return.
		if(!sakaiProxy.saveFile(mainResourceId, userUuid, fileName, mimeType, imageBytes)) {
			log.error("Couldn't add main image to CHS. Aborting.");
			return false;
		}

		/*
		 * THUMBNAIL PROFILE IMAGE
		 */
		//scale image
		imageBytes = ProfileUtils.scaleImage(imageBytes, ProfileConstants.MAX_THUMBNAIL_IMAGE_XY);
		 
		//create resource ID
		String thumbnailResourceId = sakaiProxy.getProfileImageResourcePath(userUuid, ProfileConstants.PROFILE_IMAGE_THUMBNAIL);
		log.debug("Profile.ChangeProfilePicture.onSubmit thumbnailResourceId: " + thumbnailResourceId);
		
		//save, if error, warn, erase thumbnail reference, and continue (we really only need the main image)
		if(!sakaiProxy.saveFile(thumbnailResourceId, userUuid, fileName, mimeType, imageBytes)) {
			log.warn("Couldn't add thumbnail image to CHS. Main image will be used instead.");
			thumbnailResourceId = null;
		}
		
		/*
		 * SAVE IMAGE RESOURCE IDS
		 */
		//save
		ProfileImageUploaded profileImage = new ProfileImageUploaded(userUuid, mainResourceId, thumbnailResourceId, true);
		if(dao.addNewProfileImage(profileImage)){
			log.info("Added a new profile image for user: " + userUuid);
			return true;
		}
		
		return false;
				
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean setExternalProfileImage(String userUuid, String url, String thumbnail) {
		
		//check auth and get currentUserUuid
		String currentUserUuid = sakaiProxy.getCurrentUserId();
		if(currentUserUuid == null) {
			throw new SecurityException("You must be logged in to update a user's profile image.");
		}
		
		//check admin, or the currentUser and given uuid match
		if(!sakaiProxy.isSuperUser() && !StringUtils.equals(currentUserUuid, userUuid)) {
			throw new SecurityException("Not allowed to save.");
		}
		
		//check image is actually allowed to be changed
		if(!sakaiProxy.isProfilePictureChangeEnabled()) {
			log.warn("Profile image changes are not permitted as per sakai.properties setting 'profile2.picture.change.enabled'.");
			return false;
		}
		
		//save
		ProfileImageExternal externalImage = new ProfileImageExternal(userUuid, url, thumbnail);
		if(dao.saveExternalImage(externalImage)) {
			log.info("Updated external image record for user: " + userUuid); 
			return true;
		} 
		
		return false;
	}
	
	/**
 	 * {@inheritDoc}
 	 */
	public boolean saveOfficialImageUrl(final String userUuid, final String url) {
		
		ProfileImageOfficial officialImage = new ProfileImageOfficial(userUuid, url);
		
		if(dao.saveOfficialImageUrl(officialImage)) {
			log.info("Updated official image record for user: " + userUuid); 
			return true;
		} 
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean addGalleryImage(String userUuid, byte[] imageBytes, String mimeType, String fileName) {

		// check auth and get currentUserUuid
		String currentUserUuid = sakaiProxy.getCurrentUserId();
		if (currentUserUuid == null) {
			throw new SecurityException("You must be logged in to add a gallery image.");
		}

		// check admin, or the currentUser and given uuid match
		if (!sakaiProxy.isSuperUser() && !StringUtils.equals(currentUserUuid, userUuid)) {
			throw new SecurityException("You are not allowed to add a gallery image.");
		}

		String imageId = sakaiProxy.createUuid();

		// create resource ID
		String mainResourcePath = sakaiProxy.getProfileGalleryImagePath(userUuid, imageId);

		byte[] scaledImageBytes = ProfileUtils.scaleImage(imageBytes, ProfileConstants.MAX_GALLERY_IMAGE_XY);
		
		// save image
		if (!sakaiProxy.saveFile(mainResourcePath, userUuid, fileName, mimeType,scaledImageBytes)) {
			log.error("Couldn't add gallery image to CHS. Aborting.");
			return false;
		}

		// create thumbnail
		byte[] thumbnailBytes = ProfileUtils.scaleImage(imageBytes, ProfileConstants.MAX_GALLERY_THUMBNAIL_IMAGE_XY);
		String thumbnailResourcePath = sakaiProxy.getProfileGalleryThumbnailPath(userUuid, imageId);
		sakaiProxy.saveFile(thumbnailResourcePath, userUuid, fileName, mimeType,thumbnailBytes);
		
		//save
		GalleryImage galleryImage = new GalleryImage(userUuid,mainResourcePath, thumbnailResourcePath, fileName);
		if(dao.addNewGalleryImage(galleryImage)){
			log.info("Added new gallery image for user: " + galleryImage.getUserUuid()); 
			return true;
		} 
			
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<GalleryImage> getGalleryImages(String userUuid) {

		// check auth and get currentUserUuid
		String currentUserUuid = sakaiProxy.getCurrentUserId();
		if (currentUserUuid == null) {
			throw new SecurityException("You must be logged in to make a request for a user's gallery images.");
		}

		return dao.getGalleryImages(userUuid);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<GalleryImage> getGalleryImagesRandomized(String userUuid) {
		
		List<GalleryImage> images = getGalleryImages(userUuid);
		Collections.shuffle(images);
		return images;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean removeGalleryImage(String userId, long imageId) {
		if(userId == null || new Long(imageId) == null){
	  		throw new IllegalArgumentException("Null argument in ProfileLogicImpl.removeGalleryImage()"); 
	  	}
		
		// check auth and get currentUserUuid
		String currentUserUuid = sakaiProxy.getCurrentUserId();
		if (currentUserUuid == null) {
			throw new SecurityException("You must be logged in to remove a gallery image.");
		}
		
		// check admin, or the currentUser and given uuid match
		if (!sakaiProxy.isSuperUser() && !StringUtils.equals(currentUserUuid, userId)) {
			throw new SecurityException("You are not allowed to remove this gallery image.");
		}
		
		GalleryImage galleryImage = dao.getGalleryImageRecord(userId, imageId);
		
		if(galleryImage == null){
			log.error("GalleryImage record does not exist for userId: " + userId + ", imageId: " + imageId);
			return false;
		}
		
		//delete main image
		if (!sakaiProxy.removeResource(galleryImage.getMainResource())) {
			log.error("Gallery image not removed: " + galleryImage.getMainResource());
		}
		
		//delete thumbnail
		if (!sakaiProxy.removeResource(galleryImage.getThumbnailResource())) {
			log.error("Gallery thumbnail not removed: " + galleryImage.getThumbnailResource());
		}
		
		if(dao.removeGalleryImage(galleryImage)){
			log.info("User: " + userId + " removed gallery image: " + imageId);
			return true;
		} 
		
		return false;
	}
	
	private String getUnavailableImageURL(String imagePath) {
		StringBuilder path = new StringBuilder();
		path.append(sakaiProxy.getServerUrl());
		path.append(imagePath);
		return path.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUnavailableImageURL() {
		return getUnavailableImageURL(ProfileConstants.UNAVAILABLE_IMAGE_FULL);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUnavailableImageThumbnailURL() {
		return getUnavailableImageURL(ProfileConstants.UNAVAILABLE_IMAGE_THUMBNAIL);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getProfileImageEntityUrl(String userUuid, int size) {
	
		StringBuilder sb = new StringBuilder();
		sb.append(sakaiProxy.getServerUrl());
		sb.append("/direct/profile/");
		sb.append(userUuid);
		sb.append("/image/");
		if(size == ProfileConstants.PROFILE_IMAGE_THUMBNAIL){
			sb.append("thumb/");
		}
		return sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getGalleryImagesCount(final String userUuid) {
		return dao.getGalleryImagesCount(userUuid);
	}
	
	
	/**
	 * Get the profile image for the given user, allowing fallback if no thumbnail exists.
	 * 
	 * @param userUuid 		the uuid of the user we are querying
	 * @param size			comes from ProfileConstants, main or thumbnail, also maps to a directory in ContentHosting
	 * @return MimeTypeByteArray or null
	 * 
	 * <p>Note: if thumbnail is requested and none exists, the main image will be returned instead. It can be scaled in the markup.</p>
	 *
	 */
	private MimeTypeByteArray getUploadedProfileImage(String userUuid, int size) {
		
		MimeTypeByteArray mtba = new MimeTypeByteArray();
		
		//get record from db
		ProfileImageUploaded profileImage = dao.getCurrentProfileImageRecord(userUuid);
		
		if(profileImage == null) {
			log.debug("ProfileLogic.getUploadedProfileImage() null for userUuid: " + userUuid);
			return null;
		}
		
		//get main image
		if(size == ProfileConstants.PROFILE_IMAGE_MAIN) {
			mtba = sakaiProxy.getResource(profileImage.getMainResource());
		}
		
		//or get thumbnail
		if(size == ProfileConstants.PROFILE_IMAGE_THUMBNAIL) {
			mtba = sakaiProxy.getResource(profileImage.getThumbnailResource());
			if(mtba.getBytes() == null) {
				mtba = sakaiProxy.getResource(profileImage.getMainResource());
			}
		}
		
		return mtba;
	}
	
	/**
	 * Get the URL to an image that a user has specified as their profile image
	 * @param userId		uuid of user
	 * @param size			comes from ProfileConstants. main or thumbnail.
	 *
	 * <p>Note: if thumbnail is requested and none exists, the main image will be returned instead. It can be scaled in the markup.</p>
	 * 
	 * @return url to the image, or a default image if none.
	 */
	private String getExternalProfileImageUrl(final String userUuid, final int size) {
	
		//get external image record for this user
		ProfileImageExternal externalImage = dao.getExternalImageRecordForUser(userUuid);
		
		//setup default
		String defaultImageUrl = getUnavailableImageURL();
		
		//if none, return null
    	if(externalImage == null) {
    		return getUnavailableImageURL();
    	}
    	
    	//else return the url for the type they requested
    	if(size == ProfileConstants.PROFILE_IMAGE_MAIN) {
    		String url = externalImage.getMainUrl();
    		if(StringUtils.isBlank(url)) {
    			return defaultImageUrl;
    		}
    		return url;
    	}
    	
    	if(size == ProfileConstants.PROFILE_IMAGE_THUMBNAIL) {
    		String url = externalImage.getThumbnailUrl();
    		if(StringUtils.isBlank(url)) {
    			url = externalImage.getMainUrl();
    			if(StringUtils.isBlank(url)) {
    				return defaultImageUrl;
    			}
    		}
    		return url;
    	}
    	
    	//no url	
    	log.info("ProfileLogic.getExternalProfileImageUrl. No URL for userId: " + userUuid + ", imageType: " + size + ". Returning default.");  
    	return defaultImageUrl;
	}

	
	
	/**
	 * Get the URL to a user's official profile image
	 * @param userUuid		uuid of user
	 * 
	 * @return url or a default image if none
	 */
	private String getOfficialImageUrl(final String userUuid) {
		
		//get external image record for this user
		ProfileImageOfficial official = dao.getOfficialImageRecordForUser(userUuid);
		
		//setup default
		String defaultImageUrl = getUnavailableImageURL();
		
		//if none, return null
    	if(official == null) {
    		return defaultImageUrl;
    	}
    	
    	if(StringUtils.isBlank(official.getUrl())) {
        	log.info("ProfileLogic.getOfficialImageUrl. No URL for userUuid: " + userUuid + ". Returning default.");  
			return defaultImageUrl;
		}
    	
    	return official.getUrl();
	}
	
	/**
	 * Get the official image data from the user properties, encoded in BASE64
	 * @param userUuid	uuid of user
	 * @return base64 encoded data, or null.
	 */
	private String getOfficialImageEncoded(final String userUuid) {
		User u = sakaiProxy.getUserById(userUuid);
		return u.getProperties().getProperty(sakaiProxy.getOfficialImageAttribute());
	}
	
	/**
	 * Helper to get the altText to be used for the image
	 * @param userUuid
	 * @param isOwner
	 * @param hasImage
	 * @return
	 */
	private String getAltText(String userUuid, boolean isOwner, boolean hasImage) {
		
		//owner and has an image
		if(isOwner && hasImage){
			return Messages.getString("profile.image.my.alt");
		}
		
		//owner and doesnt have an image
		if(isOwner && !hasImage){
			return Messages.getString("profile.image.my.none.alt");
		}
		
		//not owner so get name
		if(!isOwner) {
			String displayName = sakaiProxy.getUserDisplayName(userUuid);
			return Messages.getString("profile.image.other.alt", new Object[] { displayName });
		}
		
		return null;
	}
	
	
	
	
	
	private SakaiProxy sakaiProxy;
	public void setSakaiProxy(SakaiProxy sakaiProxy) {
		this.sakaiProxy = sakaiProxy;
	}
	
	private ProfilePrivacyLogic privacyLogic;
	public void setPrivacyLogic(ProfilePrivacyLogic privacyLogic) {
		this.privacyLogic = privacyLogic;
	}
	
	private ProfileConnectionsLogic connectionsLogic;
	public void setConnectionsLogic(ProfileConnectionsLogic connectionsLogic) {
		this.connectionsLogic = connectionsLogic;
	}
	
	private ProfilePreferencesLogic preferencesLogic;
	public void setPreferencesLogic(ProfilePreferencesLogic preferencesLogic) {
		this.preferencesLogic = preferencesLogic;
	}
	
	private ProfileDao dao;
	public void setDao(ProfileDao dao) {
		this.dao = dao;
	}	
	
}
