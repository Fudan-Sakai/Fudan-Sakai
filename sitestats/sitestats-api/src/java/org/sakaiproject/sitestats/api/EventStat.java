/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-api/src/java/org/sakaiproject/sitestats/api/EventStat.java $
 * $Id: EventStat.java 72172 2009-09-23 00:48:53Z arwhyte@umich.edu $
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
package org.sakaiproject.sitestats.api;

/**
 * Represents a record from the SST_EVENTS table.
 * @author Nuno Fernandes
 */
public interface EventStat extends Stat {
	/** Get the the event Id (eg. 'content.read') this record refers to. */
	public String getEventId();
	/** Set the the event Id (eg. 'content.read') this record refers to. */
	public void setEventId(String eventId);
	
	/** Get the the tool Id (eg. 'sakai.chat') this record refers to. */
	public String getToolId();
	/** Set the the tool Id (eg. 'sakai.chat') this record refers to. */
	public void setToolId(String toolId);
	
	public boolean equalExceptForCount(Object other);
}
