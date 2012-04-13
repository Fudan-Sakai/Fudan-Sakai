/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/calendar/branches/sakai-2.8.1/calendar-util/util/src/java/org/sakaiproject/util/CalendarReferenceToChannelConverter.java $
 * $Id: CalendarReferenceToChannelConverter.java 59673 2009-04-03 23:02:03Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008, 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.util;

import org.sakaiproject.calendar.cover.CalendarService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;

/**
 * Used by callback to convert channel references to channels.
 */
public final class CalendarReferenceToChannelConverter implements MergedListEntryProviderFixedListWrapper.ReferenceToChannelConverter
{
    public Object getChannel(String channelReference)
    {
        try
        {
            return CalendarService.getCalendar(channelReference); 
        }
        catch (IdUnusedException e)
        {
            return null;
        }
        catch (PermissionException e)
        {
            return null;
        }
    }
}
