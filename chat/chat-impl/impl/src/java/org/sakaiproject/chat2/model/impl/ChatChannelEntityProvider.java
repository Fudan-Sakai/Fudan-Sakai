package org.sakaiproject.chat2.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.chat2.model.ChatChannel;
import org.sakaiproject.chat2.model.ChatManager;
import org.sakaiproject.courier.api.CourierService;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.EntityView;
import org.sakaiproject.entitybroker.entityprovider.annotations.EntityCustomAction;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.ActionsExecutable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.CollectionResolvable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.Describeable;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.search.Restriction;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.cover.SessionManager;

@SuppressWarnings("deprecation")
public class ChatChannelEntityProvider implements CoreEntityProvider, AutoRegisterEntityProvider, RESTful,
	Describeable, CollectionResolvable, ActionsExecutable {

	private ChatManager chatManager;
	  
	public final static String ENTITY_PREFIX = "chat-channel";

	protected final Log LOG = LogFactory.getLog(getClass());
	
	public class SimpleChatChannel {

		/** The channel id **/
		private String id;
		
		/** The channel's tool placement **/
		private String placement;
		
		/** The channel context (typically site ID) **/
		private String context;
		
		/** Channel title **/
		private String title;
		
		/** Channel description **/
		private String description;
		
		/** True if this is the default channel for this context **/
		private boolean defaultForContext;
		
		public SimpleChatChannel()
		{
		}
		
		public SimpleChatChannel(ChatChannel channel)
		{
			this.id = channel.getId();
			this.placement = channel.getPlacement();
			this.context = channel.getContext();
			this.title = channel.getTitle();
			this.description = channel.getDescription();
			this.defaultForContext = false;
		}
		
		public String getId() {
			return id;
		}
		public void setPlacement(String placement) {
			this.placement = placement;
		}
		public String getPlacement() {
			return placement;
		}
		public void setContext(String context) {
			this.context = context;
		}
		public String getContext() {
			return context;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getTitle() {
			return title;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDescription() {
			return description;
		}

		public void setDefaultForContext(boolean defaultForContext) {
			this.defaultForContext = defaultForContext;
		}

		public boolean isDefaultForContext() {
			return defaultForContext;
		}

	}
	
	public boolean entityExists(String id) {

		if (id == null) {
			return false;
		}
		
		if ("".equals(id))
			return false;
		
		return (chatManager.getChatChannel(id) != null);
	}

	public Object getEntity(EntityReference ref) {

		String id = ref.getId();
		
		if (id == null || "".equals(id)) {
		         return new SimpleChatChannel();
		}
		  
		ChatChannel channel = chatManager.getChatChannel(id);

		if (channel == null) {
			throw new IllegalArgumentException("Channel not found");
		}
		
		if (!chatManager.getCanReadMessage(channel)) {
			throw new SecurityException("You do not have permission to access this chat channel");
		}
		
		return new SimpleChatChannel(channel);
	}

	public Object getSampleEntity() {
		return new SimpleChatChannel();
	}

	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}

	public ChatManager getChatManager() {
		return chatManager;
	}
	
	public void setChatManager(ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public String[] getHandledOutputFormats() {
	    return new String[] { Formats.HTML, Formats.XML, Formats.JSON, Formats.FORM };
	}

	public String[] getHandledInputFormats() {
	    return new String[] { Formats.HTML, Formats.XML, Formats.JSON, Formats.FORM };
	}

	public List<SimpleChatChannel> getEntities(EntityReference ref, Search search) {

		List<SimpleChatChannel> channels = new ArrayList<SimpleChatChannel>();
		
		Restriction locRes = search.getRestrictionByProperty(CollectionResolvable.SEARCH_LOCATION_REFERENCE);
		
        if (locRes != null) {
        	String location = locRes.getStringValue();
        	String context = new EntityReference(location).getId();
            
        	ChatChannel defaultChannel = getChatManager().getDefaultChannel(context, null);
        	
			List<ChatChannel> contextChannels = getChatManager().getContextChannels(context, true);
		
			for (ChatChannel c : contextChannels) {
	        	if (getChatManager().getCanReadMessage(c)) {
	        		SimpleChatChannel sc = new SimpleChatChannel(c);
	        		if (defaultChannel != null && sc.getId().equals(defaultChannel.getId())) {
	        			sc.setDefaultForContext(true);
	        		}
	        		channels.add(sc);
	        	}
			}
        }
        
		return channels;
	}

	/**
	 *  Custom action to start listening to a channel
	 * @return true if a listener is started. 
	 */
	 @EntityCustomAction(action="listen",viewKey=EntityView.VIEW_EDIT)
	 public boolean listen(EntityReference ref, Map<String, Object> params) {

		String id = ref.getId();
		
		if (id == null || "".equals(id)) {
		         return false;
		}
		  
		ChatChannel channel = chatManager.getChatChannel(id);

		if (channel == null) {
			throw new IllegalArgumentException("Channel not found");
		}
		
		if (!chatManager.getCanReadMessage(channel)) {
			throw new SecurityException("You do not have permission to access this chat channel");
		}
		
	    Session session = SessionManager.getCurrentSession();
	    String sessionId = session.getId();
	      
	    CourierService courier = org.sakaiproject.courier.cover.CourierService.getInstance();
	    
		ChatRestListener listener = new ChatRestListener(chatManager, courier, sessionId, channel); 

		chatManager.addRoomListener(listener, channel.getId());
		
		return true;
	}

	
	/**
	 * Create a new chat channel for a given context
	 * @return the id of the new channel 
	 */
	public String createEntity(EntityReference ref, Object entity,
			Map<String, Object> params) {

		SimpleChatChannel newchannel = (SimpleChatChannel) entity;
		
		ChatChannel channel = null;
		
		try {
			channel = chatManager.createNewChannel(newchannel.context, newchannel.title, newchannel.defaultForContext, true, null);
			channel.setDescription(newchannel.description);
			chatManager.updateChannel(channel, false);
		} catch (PermissionException e) {
			throw new SecurityException("You do not have permission to create a chat channel in the given context (" + 
					newchannel.context + ")");
		}
		
		return channel.getId();
	}

	public void updateEntity(EntityReference ref, Object entity,
			Map<String, Object> params) {

		String channelId = ref.getId();	
		SimpleChatChannel updchannel = (SimpleChatChannel) entity;

		ChatChannel channel = chatManager.getChatChannel(channelId);
		
		if (channel == null) {
			throw new IllegalStateException("The specified chat channel (" + channelId + ") does not exist");
		}
		
		channel.setTitle(updchannel.getTitle());
		channel.setDescription(updchannel.getDescription());
		try {
			chatManager.updateChannel(channel, true);
		} catch (PermissionException e) {
			throw new SecurityException("You do not have permission to update this channel");
		}
	}

	public void deleteEntity(EntityReference ref, Map<String, Object> params) {

		String channelId = ref.getId();
		ChatChannel channel = chatManager.getChatChannel(channelId);
		
		if (channel == null) {
			throw new IllegalStateException("The specified chat channel (" + channelId + ") does not exist");
		}
		
		try {
			chatManager.deleteChannel(channel);
		} catch (PermissionException e) {
			throw new SecurityException("You do not have permission to delete this channel");
		}
		
		return;
	}
}
