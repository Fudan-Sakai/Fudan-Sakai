/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-api/src/java/org/sakaiproject/sitestats/api/SummaryVisitsChartData.java $
 * $Id: SummaryVisitsChartData.java 72172 2009-09-23 00:48:53Z arwhyte@umich.edu $
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
import java.util.List;



public interface SummaryVisitsChartData {

	public List<SiteVisits> getSiteVisits();

	public void setSiteVisits(List<SiteVisits> siteVisits);

	public long[] getUniqueVisits();

	public long[] getVisits();

	public Date getFirstDay();

}