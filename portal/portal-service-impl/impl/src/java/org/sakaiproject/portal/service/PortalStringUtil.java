/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/portal/branches/sakai-2.8.1/portal-service-impl/impl/src/java/org/sakaiproject/portal/service/PortalStringUtil.java $
 * $Id: PortalStringUtil.java 59680 2009-04-03 23:28:39Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008 The Sakai Foundation
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

package org.sakaiproject.portal.service;

/**
 * @author ieb
 * @since Sakai 2.4
 * @version $Rev: 59680 $
 */

public class PortalStringUtil
{
	/**
	 * replaces the first occurance of a string without reverting to regex or
	 * creating arrays/vectors etc could also have used StringUtil for this
	 * perpose, but wanted something simpler.
	 * 
	 * @param path
	 * @param marker
	 * @param replacement
	 * @return
	 */
	public static String replaceFirst(String path, String marker, String replacement)
	{
		if (path == null)
		{
			return path;
		}
		int i = path.indexOf(marker);
		if (i >= 0)
		{
			String before = path.substring(0, i);
			String after = path.substring(i + marker.length());
			return before + replacement + after;
		}
		return path;
	}
}
