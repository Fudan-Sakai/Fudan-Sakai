/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/web/branches/sakai-2.8.1/news-api/api/src/java/org/sakaiproject/news/cover/NewsService.java $
 * $Id: NewsService.java 59692 2009-04-03 23:47:17Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 The Sakai Foundation
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

package org.sakaiproject.news.cover;

import org.sakaiproject.component.cover.ComponentManager;

/**
 * <p>
 * NewsService is a static Cover for the {@link org.sakaiproject.news.api.NewsService NewsService}; see that interface for usage details.
 * </p>
 * 
 * @author University of Michigan, Sakai Software Development Team
 * @version $Revision: 59692 $
 */
public class NewsService
{
	/**
	 * Access the component instance: special cover only method.
	 * 
	 * @return the component instance.
	 */
	public static org.sakaiproject.news.api.NewsService getInstance()
	{
		if (ComponentManager.CACHE_COMPONENTS)
		{
			if (m_instance == null)
				m_instance = (org.sakaiproject.news.api.NewsService) ComponentManager
						.get(org.sakaiproject.news.api.NewsService.class);
			return m_instance;
		}
		else
		{
			return (org.sakaiproject.news.api.NewsService) ComponentManager.get(org.sakaiproject.news.api.NewsService.class);
		}
	}

	private static org.sakaiproject.news.api.NewsService m_instance = null;

	public static final java.lang.String SERVICE_NAME = org.sakaiproject.news.api.NewsService.SERVICE_NAME;

	public static final java.lang.String REFERENCE_ROOT = org.sakaiproject.news.api.NewsService.REFERENCE_ROOT;

	public static java.util.List getChannels()
	{
		org.sakaiproject.news.api.NewsService service = getInstance();
		if (service == null) return null;

		return service.getChannels();
	}

	public static void removeChannel(java.lang.String param0)
	{
		org.sakaiproject.news.api.NewsService service = getInstance();
		if (service == null) return;

		service.removeChannel(param0);
	}

	public static java.util.List getNewsitems(java.lang.String param0) throws org.sakaiproject.news.api.NewsConnectionException,
			org.sakaiproject.news.api.NewsFormatException
	{
		org.sakaiproject.news.api.NewsService service = getInstance();
		if (service == null) return null;

		return service.getNewsitems(param0);
	}

	public static java.util.List getNewsitems(java.lang.String param0, org.sakaiproject.javax.Filter param1)
			throws org.sakaiproject.news.api.NewsConnectionException, org.sakaiproject.news.api.NewsFormatException
	{
		org.sakaiproject.news.api.NewsService service = getInstance();
		if (service == null) return null;

		return service.getNewsitems(param0, param1);
	}

	public static boolean isUpdateAvailable(java.lang.String param0)
	{
		org.sakaiproject.news.api.NewsService service = getInstance();
		if (service == null) return false;

		return service.isUpdateAvailable(param0);
	}

	public static org.sakaiproject.news.api.NewsChannel getChannel(java.lang.String param0)
			throws org.sakaiproject.news.api.NewsConnectionException, org.sakaiproject.news.api.NewsFormatException
	{
		org.sakaiproject.news.api.NewsService service = getInstance();
		if (service == null) return null;

		return service.getChannel(param0);
	}
}
