package org.sakaiproject.profile2.logic;


/**
 * An interface to create and resolve links to pages and views into Profile2
 * 
 * @author Steve Swinsburg (steve.swinsburg@gmail.com)
 * 
 */
public interface ProfileLinkLogic {

	/**
	 * Creates a full URL to the front page of the currently logged in user's
	 * profile page on their My Workspace.
	 * 
	 * <p>
	 * This should only be used internally by Profile2 as the URL is long and ugly.
	 * If you need to generate a URL to a user's profile, see {@link generateUrlToUserProfile}
	 * </p>
	 * 
	 * @return the url or null if they don't have the tool installed.
	 */
	public String getInternalDirectUrlToUserProfile();
	
	/**
	 * Creates a full URL to the given user's profile page viewed from within the currently logged in
	 * user's ViewProfile page in Profile2 on their My Workspace
	 * 
	 * <p>
	 * This should only be used internally by Profile2 as the URL is long and ugly.
	 * If you need to generate a URL to a user's profile, see {@link generateUrlToUserProfile}
	 * </p>
	 * 
	 * @return the url or null if they don't have the tool installed.
	 */
	public String getInternalDirectUrlToUserProfile(final String userUuid);
	
	/**
	 * Creates a full URL to the messages page (and optionally directly to a message view) of the currently logged in user's
	 * profile page on their My Workspace.
	 * 
	 * <p>
	 * This should only be used internally by Profile2 as the URL is long and ugly.
	 * If you need to generate a URL to a user's profile, see {@link getUrlToUserMessages}
	 * </p>
	 * 
	 * @param threadId	optional param if we want to link direct to a message thread view
	 * @return the url or null if they don't have the tool installed.
	 */
	public String getInternalDirectUrlToUserMessages(final String threadId);
	
	/**
	 * Creates a full URL to the connections page of the currently logged in user's
	 * profile page on their My Workspace.
	 * 
	 * <p>
	 * This should only be used internally by Profile2 as the URL is long and ugly.
	 * If you need to generate a URL to a user's profile, see {@link getUrlToUserConnections}
	 * </p>
	 * 
	 * @return the url or null if they don't have the tool installed.
	 */
	public String getInternalDirectUrlToUserConnections();
	
	/**
	 * Creates a RESTful link to the Profile2 home page for either the currently logged in user (if null param) or the given user.
	 * When followed, will pass through the ProfileLinkEntityProvider and be resolved into the real link
	 * 
	 * <p>This is used for url shortening and also to avoid generating the personalised link for each user for when sending out multiple emails.</p>
	 * 	
	 * <p>The URL is of the form: http://server.com/direct/my/profile/{userUuid}</p>
	 *
	 * @param userUuid	optional if you want to link to the profile view of another person
	 * @return
	 */
	public String getEntityLinkToProfileHome(final String userUuid);

	/**
	 * Creates a RESTful link to the Profile2 message page (and optionally directly to a thread) for any currently logged in user.
	 * When followed, will pass through the ProfileLinkEntityProvider and be resolved into the real link.
	 * 
	 * <p>Note: If that person is not a thread participant, this will be handled in the tool and just put to their message list page.</p>
	 * 
	 * <p>The URL is of the form: http://server.com/direct/my/messages or http://server.com/direct/my/messages/12345</p>
	 * 
	 * @param threadId	optionally, add the threadId to the URL
	 * @return
	 */
	public String getEntityLinkToProfileMessages(final String threadId);
	
	/**
	 * Creates a RESTful link to the Profile2 conenctions page for any currently logged in user.
	 * When followed, will pass through the ProfileLinkEntityProvider and be resolved into the real link.
	 * 
	 * <p>The URL is of the form: http://server.com/direct/my/connections</p>
	 * 
	 * @return
	 */
	public String getEntityLinkToProfileConnections();
	
	/**
	 * Generate a tiny URL for the supplied URL
	 * 
	 * @param url
	 * @return
	 */
	//public String generateTinyUrl(final String url);
	
}
