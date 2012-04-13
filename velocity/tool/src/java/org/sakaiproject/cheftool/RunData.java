/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/velocity/branches/sakai-2.8.1/tool/src/java/org/sakaiproject/cheftool/RunData.java $
 * $Id: RunData.java 59691 2009-04-03 23:46:45Z arwhyte@umich.edu $
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

package org.sakaiproject.cheftool;

import javax.servlet.http.HttpServletRequest;

import org.sakaiproject.util.ParameterParser;

public class RunData
{
	protected ParameterParser m_params = null;

	protected HttpServletRequest m_req = null;

	public RunData(HttpServletRequest req, ParameterParser params)
	{
		m_req = req;
		m_params = params;
	}

	public ParameterParser getParameters()
	{
		return m_params;
	}

	public HttpServletRequest getRequest()
	{
		return m_req;
	}
}
