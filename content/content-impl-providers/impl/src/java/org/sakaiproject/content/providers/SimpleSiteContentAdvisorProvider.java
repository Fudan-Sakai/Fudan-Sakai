/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/content/branches/sakai-2.8.1/content-impl-providers/impl/src/java/org/sakaiproject/content/providers/SimpleSiteContentAdvisorProvider.java $
 * $Id: SimpleSiteContentAdvisorProvider.java 59674 2009-04-03 23:05:58Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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

package org.sakaiproject.content.providers;

import org.sakaiproject.content.api.providers.SiteContentAdvisor;
import org.sakaiproject.content.api.providers.SiteContentAdvisorProvider;
import org.sakaiproject.content.api.providers.SiteContentAdvisorTypeRegistry;
import org.sakaiproject.site.api.Site;

/**
 * Creates a Site simple SiteContentAdvisorProvider that is bound to the site
 * type only and generates the same SiteContentAdvisor for all sites.
 * 
 * @author ieb
 */
public class SimpleSiteContentAdvisorProvider implements SiteContentAdvisorProvider
{
	private SiteContentAdvisorTypeRegistry siteContentAdvisorTypeRegistry;

	private String siteType;

	private SiteContentAdvisor siteContentAdvisor;

	public void init()
	{
		siteContentAdvisorTypeRegistry.registerSiteContentAdvisorProvidor(this, siteType);
	}

	/**
	 * @param site
	 * @return
	 */
	public SiteContentAdvisor getContentAdvisor(Site site)
	{
		return siteContentAdvisor;
	}

	/**
	 * @return the siteContentAdvisor
	 */
	public SiteContentAdvisor getSiteContentAdvisor()
	{
		return siteContentAdvisor;
	}

	/**
	 * @param siteContentAdvisor the siteContentAdvisor to set
	 */
	public void setSiteContentAdvisor(SiteContentAdvisor siteContentAdvisor)
	{
		this.siteContentAdvisor = siteContentAdvisor;
	}

	/**
	 * @return the siteContentAdvisorTypeRegistry
	 */
	public SiteContentAdvisorTypeRegistry getSiteContentAdvisorTypeRegistry()
	{
		return siteContentAdvisorTypeRegistry;
	}

	/**
	 * @param siteContentAdvisorTypeRegistry the siteContentAdvisorTypeRegistry to set
	 */
	public void setSiteContentAdvisorTypeRegistry(
			SiteContentAdvisorTypeRegistry siteContentAdvisorTypeRegistry)
	{
		this.siteContentAdvisorTypeRegistry = siteContentAdvisorTypeRegistry;
	}

	/**
	 * @return the siteType
	 */
	public String getSiteType()
	{
		return siteType;
	}

	/**
	 * @param siteType the siteType to set
	 */
	public void setSiteType(String siteType)
	{
		this.siteType = siteType;
	}
}
