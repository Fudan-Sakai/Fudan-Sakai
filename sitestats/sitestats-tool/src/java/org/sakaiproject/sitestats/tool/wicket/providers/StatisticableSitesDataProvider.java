/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-tool/src/java/org/sakaiproject/sitestats/tool/wicket/providers/StatisticableSitesDataProvider.java $
 * $Id: StatisticableSitesDataProvider.java 72176 2009-09-24 13:51:01Z nuno@ufp.edu.pt $
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
package org.sakaiproject.sitestats.tool.wicket.providers;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.IModel;
import org.sakaiproject.javax.PagingPosition;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService.SelectionType;
import org.sakaiproject.site.api.SiteService.SortType;
import org.sakaiproject.sitestats.tool.facade.Locator;
import org.sakaiproject.sitestats.tool.wicket.models.SiteModel;


public class StatisticableSitesDataProvider extends SortableSearchableDataProvider {
	private static final long		serialVersionUID	= 1L;
	public final static String		COL_TITLE			= "title";
	public final static String		COL_TYPE			= "type";
	public final static String		COL_STATUS			= "published";
	public final static String		SITE_TYPE_ALL		= "all";
	
	private String					siteType			= SITE_TYPE_ALL;

	public StatisticableSitesDataProvider() {
		InjectorHolder.getInjector().inject(this);
		
        // set default sort
        setSort(COL_TITLE, true);
	}

	public Iterator iterator(int first, int count) {
		// pager
		int start = first + 1;
		int end = start + count - 1;
		PagingPosition pp = new PagingPosition(start, end);

		String type = SITE_TYPE_ALL.equals(getSiteType()) ? null : getSiteType();
		return Locator.getFacade().getSiteService().getSites(SelectionType.NON_USER, type, getSearchKeyword(), null, getSSSortType(), pp).iterator();
	}
	
	private SortType getSSSortType() {
		SortParam sp = getSort();
		
		if(sp.getProperty().equals(COL_TITLE)){
			if(sp.isAscending()) {
				return SortType.TITLE_ASC;
			}else{
				return SortType.TITLE_DESC;
			}
		}else if(sp.getProperty().equals(COL_TYPE)){
			if(sp.isAscending()){
				return SortType.TYPE_ASC;
			}else{
				return SortType.TYPE_DESC;
			}
		}else if(sp.getProperty().equals(COL_STATUS)){
			if(sp.isAscending()){
				return SortType.PUBLISHED_ASC;
			}else{
				return SortType.PUBLISHED_DESC;
			}
		}else{
			return SortType.TITLE_ASC;
		}
	}

	public IModel model(Object object) {
		return new SiteModel((Site) object);
	}

	public int size() {
		String type = SITE_TYPE_ALL.equals(getSiteType()) ? null : getSiteType();
		return Locator.getFacade().getSiteService().countSites(SelectionType.NON_USER, type, getSearchKeyword(), null);
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getSiteType() {
		return siteType;
	}

}
