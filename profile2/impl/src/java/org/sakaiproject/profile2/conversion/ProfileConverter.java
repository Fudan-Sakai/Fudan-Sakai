package org.sakaiproject.profile2.conversion;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.authz.api.SecurityAdvisor.SecurityAdvice;
import org.sakaiproject.profile2.dao.ProfileDao;
import org.sakaiproject.profile2.exception.ProfileNotDefinedException;
import org.sakaiproject.profile2.hbm.model.ProfileImageExternal;
import org.sakaiproject.profile2.hbm.model.ProfileImageUploaded;
import org.sakaiproject.profile2.logic.ProfileImageLogic;
import org.sakaiproject.profile2.logic.SakaiProxy;
import org.sakaiproject.profile2.model.ImportableUserProfile;
import org.sakaiproject.profile2.model.UserProfile;
import org.sakaiproject.profile2.util.ProfileConstants;
import org.sakaiproject.profile2.util.ProfileUtils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * Handles the conversion and import of profiles and images. This is not part of the public API.
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public class ProfileConverter {

	private static final Logger log = Logger.getLogger(ProfileConverter.class);
	
	@Setter
	private SakaiProxy sakaiProxy;
	
	@Setter
	private ProfileDao dao;
	
	@Setter
	private SecurityService securityService;
	
	@Setter
	private ProfileImageLogic imageLogic;
	
	public void init() {
		log.info("Profile2: ==============================="); 
		log.info("Profile2: Conversion utility starting up."); 
		log.info("Profile2: ==============================="); 
	}
	
	/**
	 * Convert profile images
	 */
	public void convertProfileImages() {
		
		//get list of users
		List<String> allUsers = new ArrayList<String>(dao.getAllSakaiPersonIds());
		
		if(allUsers.isEmpty()){
			log.warn("Profile2 image converter: No SakaiPersons to process. Nothing to do!");
			return;
		}
		//for each, do they have a profile image record. if so, skip (perhaps null the SakaiPerson JPEG_PHOTO bytes?)
		for(Iterator<String> i = allUsers.iterator(); i.hasNext();) {
			String userUuid = (String)i.next();
			
			//get image record from dao directly, we don't need privacy/prefs here
			ProfileImageUploaded uploadedProfileImage = dao.getCurrentProfileImageRecord(userUuid);
			
			if(uploadedProfileImage != null) {
				log.info("Profile2 image converter: ProfileImage record exists for " + userUuid + ". Nothing to do here, skipping to next section...");
			} else {
				log.info("Profile2 image converter: No existing ProfileImage record for " + userUuid + ". Processing...");
				
				//get photo from SakaiPerson
				byte[] image = sakaiProxy.getSakaiPersonJpegPhoto(userUuid);
				
				//if none, nothing to do
				if(image == null || image.length == 0) {
					log.info("Profile2 image converter: No image binary to convert for " + userUuid + ". Skipping to next section...");
				} else {
					
					//set some defaults for the image we are adding to ContentHosting
					String fileName = "Profile Image";
					String mimeType = "image/jpeg";
					
					//scale the main image
					byte[] imageMain = ProfileUtils.scaleImage(image, ProfileConstants.MAX_IMAGE_XY);
					
					//create resource ID
					String mainResourceId = sakaiProxy.getProfileImageResourcePath(userUuid, ProfileConstants.PROFILE_IMAGE_MAIN);
					log.info("Profile2 image converter: mainResourceId: " + mainResourceId);
					
					//save, if error, log and return.
					if(!sakaiProxy.saveFile(mainResourceId, userUuid, fileName, mimeType, imageMain)) {
						log.error("Profile2 image converter: Saving main profile image failed.");
						continue;
					}
	
					/*
					 * THUMBNAIL PROFILE IMAGE
					 */
					//scale image
					byte[] imageThumbnail = ProfileUtils.scaleImage(image, ProfileConstants.MAX_THUMBNAIL_IMAGE_XY);
					 
					//create resource ID
					String thumbnailResourceId = sakaiProxy.getProfileImageResourcePath(userUuid, ProfileConstants.PROFILE_IMAGE_THUMBNAIL);
	
					log.info("Profile2 image converter: thumbnailResourceId:" + thumbnailResourceId);
					
					//save, if error, log and return.
					if(!sakaiProxy.saveFile(thumbnailResourceId, userUuid, fileName, mimeType, imageThumbnail)) {
						log.warn("Profile2 image converter: Saving thumbnail profile image failed. Main image will be used instead.");
						thumbnailResourceId = null;
					}
	
					/*
					 * SAVE IMAGE RESOURCE IDS
					 */
					uploadedProfileImage = new ProfileImageUploaded(userUuid, mainResourceId, thumbnailResourceId, true);
					if(dao.addNewProfileImage(uploadedProfileImage)){
						log.info("Profile2 image converter: Binary image converted and saved for " + userUuid);
					} else {
						log.warn("Profile2 image converter: Binary image conversion failed for " + userUuid);
					}					
					
				}
			} 
			
			//process any image URLs, if they don't already have a valid record.
			ProfileImageExternal externalProfileImage = dao.getExternalImageRecordForUser(userUuid);
			if(externalProfileImage != null) {
				log.info("Profile2 image converter: ProfileImageExternal record exists for " + userUuid + ". Nothing to do here, skipping...");
			} else {
				log.info("Profile2 image converter: No existing ProfileImageExternal record for " + userUuid + ". Processing...");
				
				String url = sakaiProxy.getSakaiPersonImageUrl(userUuid);
				
				//if none, nothing to do
				if(StringUtils.isBlank(url)) {
					log.info("Profile2 image converter: No url image to convert for " + userUuid + ". Skipping...");
				} else {
					externalProfileImage = new ProfileImageExternal(userUuid, url, null);
					if(dao.saveExternalImage(externalProfileImage)) {
						log.info("Profile2 image converter: Url image converted and saved for " + userUuid);
					} else {
						log.warn("Profile2 image converter: Url image conversion failed for " + userUuid);
					}
				}
				
			}
			
			log.info("Profile2 image converter: Finished converting user profile for: " + userUuid);
			//go to next user
		}
		
		return;
	}
	
	/**
	 * Import profiles from the given CSV file
	 * 
	 * <p>The CSV file may contain any of the following headings, in any order:
	 *  
	 *  <ul>
	 *  <li>eid</li>
	 *  <li>nickname</li>
	 *  <li>position</li>
	 *  <li>department</li>
	 *  <li>school</li>
	 *  <li>room</li>
	 *  <li>web site</li>
	 *  <li>work phone</li>
	 *  <li>home phone</li>
	 *  <li>mobile phone</li>
	 *  <li>fax</li>
	 *  <li>books</li>
	 *  <li>tv</li>
	 *  <li>movies</li>
	 *  <li>quotes</li>
	 *  <li>summary</li>
	 *  <li>course</li>
	 *  <li>subjects</li>
	 *  <li>staff profile</li>
	 *  <li>uni profile url</li>
	 *  <li>academic profile url</li>
	 *  <li>publications</li>
	 *  <li>official image url</li>
	 *  </ul>
	 * 
	 * <p>Column headings must match EXACTLY the list above. They do not need to be in the same order, or even all present.
	 * 
	 * <p>Fields must be comma separated and each field surrounded with double quotes. There must be no spaces between fields.
	 * 
	 * <p>Only users that do not currently have a profile will be imported.
	 * 
	 * @param path	path to CSV file on the server
	 */
	public void importProfiles(String path) {
		
		if(StringUtils.isBlank(path)) {
			log.warn("Profile2 importer: invalid path to CSV file. Aborting.");
			return;
		}
		
        HeaderColumnNameTranslateMappingStrategy<ImportableUserProfile> strat = new HeaderColumnNameTranslateMappingStrategy<ImportableUserProfile>();
        strat.setType(ImportableUserProfile.class);
        
        //map the column headers to the field names in the UserProfile class
        //this mapping is not exhaustive and can be added to at any time since we are mapping
        //on column name not position
        Map<String, String> map = new HashMap<String, String>();
        map.put("eid", "eid");
        map.put("nickname", "nickname");
        map.put("position", "position");
        map.put("department", "department");
        map.put("school", "school");
        map.put("room", "room");
        map.put("web site", "homepage");
        map.put("work phone", "workphone");
        map.put("home phone", "homephone");
        map.put("mobile phone", "mobilephone");
        map.put("fax", "facsimile");
        map.put("books", "favouriteBooks");
        map.put("tv", "favouriteTvShows");
        map.put("movies", "favouriteMovies");
        map.put("quotes", "favouriteQuotes");
        map.put("summary", "personalSummary");
        map.put("course", "course");
        map.put("subjects", "subjects");
        map.put("staff profile", "staffProfile");
        map.put("uni profile url", "universityProfileUrl");
        map.put("academic profile url", "academicProfileUrl");
        map.put("publications", "publications");
        map.put("official image url", "officialImageUrl");
        
        strat.setColumnMapping(map);

        CsvToBean<ImportableUserProfile> csv = new CsvToBean<ImportableUserProfile>();
        List<ImportableUserProfile> list = new ArrayList<ImportableUserProfile>();
        try {
			list = csv.parse(strat, new CSVReader(new FileReader(path)));
		} catch (FileNotFoundException fnfe) {
			log.error("Profile2 importer: Couldn't find file: " + fnfe.getClass() + " : " + fnfe.getMessage());
		}
        
		//setup a security advisor so we can save profiles
		SecurityAdvisor securityAdvisor = new SecurityAdvisor(){
			public SecurityAdvice isAllowed(String userId, String function, String reference){
				  return SecurityAdvice.ALLOWED;
			}
		};
		enableSecurityAdvisor(securityAdvisor);
		
        //process each
        for(ImportableUserProfile profile: list) {

        	log.info("Processing user: " + profile.getEid());
        	
        	//get uuid
        	String uuid = sakaiProxy.getUserIdForEid(profile.getEid());
        	if(StringUtils.isBlank(uuid)) {
        		log.error("Invalid user: " + profile.getEid() + ". Skipping...");
        		continue;
        	}
        	
        	profile.setUserUuid(uuid);
        	        	
        	//check if user already has a profile. Skip if so.
        	if(hasPersistentProfile(uuid)) {
        		log.warn("User: " + profile.getEid() + " already has a profile. Skipping...");
        		continue;
        	}
        	
        	//persist user profile
        	try {
        		SakaiPerson sp = transformUserProfileToSakaiPerson(profile);
        		
        		if(sp == null){
        			//already logged
        			continue;
        		}
        		
        		if(sakaiProxy.updateSakaiPerson(sp)) {
        			log.info("Profile saved for user: " + profile.getEid());
        		} else {
        			log.error("Couldn't save profile for user: " + profile.getEid());
        			continue;
        		}
        	} catch (ProfileNotDefinedException pnde) {
        		//already logged
        		continue;
        	}
        	
        	//add/update official image, if supplied in the CSV
        	if(StringUtils.isNotBlank(profile.getOfficialImageUrl())) {
        		if(imageLogic.saveOfficialImageUrl(uuid, profile.getOfficialImageUrl())) {
        			log.info("Official image saved for user: " + profile.getEid());
        		} else {
        			log.error("Couldn't save official image for user: " + profile.getEid());
        		}
        	}
        }
        disableSecurityAdvisor(securityAdvisor);
		
	}
	
	/**
	 * Does the given user already have a <b>persistent</b> user profile?
	 * 
	 * @param userUuid	uuid of the user
	 * @return
	 */
	private boolean hasPersistentProfile(String userUuid) {
				
		SakaiPerson sp = sakaiProxy.getSakaiPerson(userUuid);
		if(sp != null){
			return true;
		} 
		return false;
	}
	
	
	/**
	 * Convenience method to map a UserProfile object onto a SakaiPerson object for persisting
	 * 
	 * @param up 		input UserProfile
	 * @return			returns a SakaiPerson representation of the UserProfile object which can be persisted
	 */
	private SakaiPerson transformUserProfileToSakaiPerson(UserProfile up) {
	
		log.info("Transforming: " + up.toString());
		
		String userUuid = up.getUserUuid();
		
		if(StringUtils.isBlank(userUuid)) {
			log.error("Profile was invalid (missing uuid), cannot transform.");
			return null;
		}
		
		//get SakaiPerson
		SakaiPerson sakaiPerson = sakaiProxy.getSakaiPerson(userUuid);
		
		//if null, create one 
		if(sakaiPerson == null) {
			sakaiPerson = sakaiProxy.createSakaiPerson(userUuid);
			//if its still null, throw exception
			if(sakaiPerson == null) {
				throw new ProfileNotDefinedException("Couldn't create a SakaiPerson for " + userUuid);
			}
		} 
		
		//map fields from UserProfile to SakaiPerson
		
		//basic info
		sakaiPerson.setNickname(up.getNickname());
		sakaiPerson.setDateOfBirth(up.getDateOfBirth());
		
		//contact info
		sakaiPerson.setLabeledURI(up.getHomepage());
		sakaiPerson.setTelephoneNumber(up.getWorkphone());
		sakaiPerson.setHomePhone(up.getHomephone());
		sakaiPerson.setMobile(up.getMobilephone());
		sakaiPerson.setFacsimileTelephoneNumber(up.getFacsimile());
		
		//staff info
		sakaiPerson.setOrganizationalUnit(up.getDepartment());
		sakaiPerson.setTitle(up.getPosition());
		sakaiPerson.setCampus(up.getSchool());
		sakaiPerson.setRoomNumber(up.getRoom());
		sakaiPerson.setStaffProfile(up.getStaffProfile());
		sakaiPerson.setUniversityProfileUrl(up.getUniversityProfileUrl());
		sakaiPerson.setAcademicProfileUrl(up.getAcademicProfileUrl());
		sakaiPerson.setPublications(up.getPublications());
		
		// student info
		sakaiPerson.setEducationCourse(up.getCourse());
		sakaiPerson.setEducationSubjects(up.getSubjects());
				
		//personal info
		sakaiPerson.setFavouriteBooks(up.getFavouriteBooks());
		sakaiPerson.setFavouriteTvShows(up.getFavouriteTvShows());
		sakaiPerson.setFavouriteMovies(up.getFavouriteMovies());
		sakaiPerson.setFavouriteQuotes(up.getFavouriteQuotes());
		sakaiPerson.setNotes(up.getPersonalSummary());
		
		return sakaiPerson;
	}
	
	/**
	 * Add the supplied security advisor to the stack for this transaction
	 */
	private void enableSecurityAdvisor(SecurityAdvisor securityAdvisor) {
		securityService.pushAdvisor(securityAdvisor);
	}

	/**
	 * Remove security advisor from the stack
	 */
	private void disableSecurityAdvisor(SecurityAdvisor advisor){
		securityService.popAdvisor(advisor);
	}
	
}
