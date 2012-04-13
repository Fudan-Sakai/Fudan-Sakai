package org.sakaiproject.profile2.tool.entityprovider;

import java.util.Locale;
import java.util.Map;

import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Describeable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Statisticable;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;
import org.sakaiproject.profile2.util.ProfileConstants;

/**
 * This is the entity provider to interact with SiteStats 
 * and provided anonymous reporting of Profile2 usage.
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 *
 */
public class ProfileEventsEntityProvider extends AbstractEntityProvider implements AutoRegisterEntityProvider, Statisticable, Describeable {

    public final static String PREFIX = "profile-events";
    public final static String TOOL_ID = "sakai.profile2";
    
    public final static String[] EVENT_KEYS = new String[] {
    	ProfileConstants.EVENT_PROFILE_VIEW_OWN,
		ProfileConstants.EVENT_PROFILE_VIEW_OTHER,
		ProfileConstants.EVENT_FRIEND_REQUEST,
		ProfileConstants.EVENT_FRIEND_CONFIRM,
		ProfileConstants.EVENT_FRIENDS_VIEW_OWN,
		ProfileConstants.EVENT_FRIENDS_VIEW_OTHER,
		ProfileConstants.EVENT_SEARCH_BY_NAME,
		ProfileConstants.EVENT_SEARCH_BY_INTEREST,
		ProfileConstants.EVENT_STATUS_UPDATE,
		ProfileConstants.EVENT_TWITTER_UPDATE,
		ProfileConstants.EVENT_MESSAGE_SENT,
	};

   
    
	public String getEntityPrefix() {
		return PREFIX;
	}

	public String getAssociatedToolId() {
		return ProfileConstants.TOOL_ID;
	}

	public String[] getEventKeys() {
		return EVENT_KEYS;
	}

	public Map<String, String> getEventNames(Locale locale) {
		return null;
	}

}
