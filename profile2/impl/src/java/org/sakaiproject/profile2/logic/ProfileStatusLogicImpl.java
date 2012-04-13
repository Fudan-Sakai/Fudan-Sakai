package org.sakaiproject.profile2.logic;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.sakaiproject.profile2.dao.ProfileDao;
import org.sakaiproject.profile2.model.ProfilePrivacy;
import org.sakaiproject.profile2.model.ProfileStatus;
import org.sakaiproject.profile2.util.ProfileUtils;

/**
 * Implementation of ProfileStatusLogic API
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public class ProfileStatusLogicImpl implements ProfileStatusLogic {

	private static final Logger log = Logger.getLogger(ProfileStatusLogicImpl.class);

	
	/**
 	 * {@inheritDoc}
 	 */
	public ProfileStatus getUserStatus(final String userUuid, ProfilePrivacy privacy) {
		
		//check privacy
		if(privacy == null){
			return null;
		}
		
		String currentUserUuid = sakaiProxy.getCurrentUserId();

		//if not same, check privacy
        if(!StringUtils.equals(userUuid, currentUserUuid)) {
		
        	//friend?
        	boolean friend = connectionsLogic.isUserXFriendOfUserY(userUuid, currentUserUuid);
		
        	//check allowed
        	if(!privacyLogic.isUserXStatusVisibleByUserY(userUuid, privacy, currentUserUuid, friend)){
        		return null;
        	}
        }
		
		// compute oldest date for status 
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DAY_OF_YEAR, -7); 
		final Date oldestStatusDate = cal.getTime(); 
		
		//get data
		ProfileStatus status = dao.getUserStatus(userUuid, oldestStatusDate);
		if(status == null){
			return null;
		}
		
		//format the date field
		if(status.getDateAdded() != null){
			status.setDateFormatted(ProfileUtils.convertDateForStatus(status.getDateAdded()));
		}
		
		return status;
	}
	
	/**
 	 * {@inheritDoc}
 	 */
	public ProfileStatus getUserStatus(final String userUuid) {
		return getUserStatus(userUuid, privacyLogic.getPrivacyRecordForUser(userUuid));
	}
	
	
	
	/**
 	 * {@inheritDoc}
 	 */
	public boolean setUserStatus(String userId, String status) {
		
		//create object
		ProfileStatus profileStatus = new ProfileStatus(userId,status,new Date());
		
		return setUserStatus(profileStatus);
	}
	
	
	/**
 	 * {@inheritDoc}
 	 */
	public boolean setUserStatus(ProfileStatus profileStatus) {
		
		//current user must be the user making the request
		if(!StringUtils.equals(sakaiProxy.getCurrentUserId(), profileStatus.getUserUuid())) {
			throw new SecurityException("You are not authorised to perform that action.");
		}
		
		//PRFL-588 ensure size limit. Column size is 255.
		String tMessage = ProfileUtils.truncate(profileStatus.getMessage(), 255, false);
		profileStatus.setMessage(tMessage);
		
		if(dao.setUserStatus(profileStatus)){
			log.info("Updated status for user: " + profileStatus.getUserUuid()); 
			return true;
		} 
		
		return false;
	}
	
	
	/**
 	 * {@inheritDoc}
 	 */
	public boolean clearUserStatus(String userId) {
		
		ProfileStatus profileStatus = getUserStatus(userId);
		
		if(profileStatus == null){
			log.error("ProfileStatus null for userId: " + userId); 
			return false;
		}
		
		//current user must be the user making the request
		if(!StringUtils.equals(sakaiProxy.getCurrentUserId(), profileStatus.getUserUuid())) {
			throw new SecurityException("You are not authorised to perform that action.");
		}
				
		if(dao.clearUserStatus(profileStatus)) {
			log.info("User: " + userId + " cleared status");  
			return true;
		}
		
		return false;
	}

	/**
 	 * {@inheritDoc}
 	 */
	public int getStatusUpdatesCount(final String userUuid) {
		return dao.getStatusUpdatesCount(userUuid);
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
	
	private ProfileDao dao;
	public void setDao(ProfileDao dao) {
		this.dao = dao;
	}
}
