/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-api/src/java/org/sakaiproject/sitestats/api/event/EventRegistryService.java $
 * $Id: EventRegistryService.java 72280 2010-01-22 10:45:04Z nuno@ufp.edu.pt $
 *
 * Copyright (c) 2006-2009 The Sakai Foundation
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
package org.sakaiproject.sitestats.api.event;

import java.util.List;
import java.util.Map;

import org.sakaiproject.sitestats.api.parser.EventFactory;
import org.sakaiproject.sitestats.api.parser.ToolFactory;


public interface EventRegistryService {
	public final String		NOTIF_EVENT_REGISTRY_EXPIRED	= "SiteStats-EventRegistry_expired";
	
	/**
	 * Get all statisticable tool events.
	 * @return A list of event ids.
	 */
	public List<String> getEventIds();

	/**
	 * Get all anonymous tool events.
	 * @return A list of anonymous event ids.
	 */
	public List<String> getAnonymousEventIds();
	
	/**
	 * Get the statisticable event registry.
	 * Moved from StatsManager.getAllToolEventsDefinition().
	 * @return A list of ToolInfo objects.
	 * @see ToolInfo
	 */
	public List<ToolInfo> getEventRegistry();

	/**
	 * Get the statisticable event registry for a specific site.
	 * Moved from StatsManager.getSiteToolEventsDefinition(String, boolean).
	 * @param siteId The site id of the related site.
	 * @param onlyAvailableInSite If set to true, only events for tools available in site will be returned.
	 * @return A list of ToolInfo objects.
	 * @see ToolInfo
	 */
	public List<ToolInfo> getEventRegistry(String siteId, boolean onlyAvailableInSite);

	/**
	 * Get the localized event name for the specific event id.
	 * @param eventId The id of the event.
	 * @return The (localized) event name.
	 */
	public String getEventName(String eventId);

	/**
	 * Get the tool name for the specific tool id.
	 * @param toolId The id of the tool.
	 * @return The tool name.
	 */
	public String getToolName(String toolId);

	/**
	 * Get the tool icon for the specific tool id.
	 * @param toolId The id of the tool.
	 * @return The tool icon.
	 */
	public String getToolIcon(String toolId);

	/**
	 * Get the event id to tool map such as: {event id} --> {tool mapping}.
	 * @return The event id to tool map.
	 */
	public Map<String, ToolInfo> getEventIdToolMap();
	
	/** 
	 * Return an instance of the ToolFactory used to build a ToolInfo object.
	 * @see getEventRegistry()
	 */
	public ToolFactory getToolFactory();
	
	/** 
	 * Return an instance of the EventFactory used to build a EventInfo object.
	 * @see getEventRegistry()
	 */
	public EventFactory getEventFactory();

}