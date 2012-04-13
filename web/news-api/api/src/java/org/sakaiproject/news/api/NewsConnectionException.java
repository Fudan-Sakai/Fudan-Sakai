/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/web/branches/sakai-2.8.1/news-api/api/src/java/org/sakaiproject/news/api/NewsConnectionException.java $
 * $Id: NewsConnectionException.java 59692 2009-04-03 23:47:17Z arwhyte@umich.edu $
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

package org.sakaiproject.news.api;

/**
 * <p>
 * NewsConnectionException is thrown whenever the NewsService is unable to make a connection or obtain a file specified by a source URL.
 * </p>
 */
public class NewsConnectionException extends Exception
{
	public NewsConnectionException(String message)
	{
		super(message);
	}
}
