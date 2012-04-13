/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-api/src/java/org/sakaiproject/sitestats/api/SiteActivity.java $
 * $Id: SiteActivity.java 72172 2009-09-23 00:48:53Z arwhyte@umich.edu $
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

import java.util.Date;

public interface SiteActivity {
	/** Get the db row id. */
	public long getId();
	
	/** Set the db row id. */
	public void setId(long id);
	
	/** Get the context (site id) this record refers to. */
	public String getSiteId();
	
	/** Set the context (site id) this record refers to. */
	public void setSiteId(String siteId);
	
	/** Get the date this record refers to. */
	public Date getDate();
	
	/** Set the date this record refers to. */
	public void setDate(Date date);

	/** Get the event this record refers to. */
	public String getEventId();
	
	/** Set the event this record refers to. */
	public void setEventId(String eventId);
	
	/** Get the total times this event was generated on this context and date. */
	public long getCount();
	
	/** Set the total times this event was generated on this context and date. */
	public void setCount(long count);
}
