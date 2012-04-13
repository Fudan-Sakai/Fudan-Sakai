/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/portal/branches/sakai-2.8.1/portal-impl/impl/src/java/org/sakaiproject/portal/charon/handlers/ErrorReportHandler.java $
 * $Id: ErrorReportHandler.java 59680 2009-04-03 23:28:39Z arwhyte@umich.edu $
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

package org.sakaiproject.portal.charon.handlers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sakaiproject.portal.api.PortalHandlerException;
import org.sakaiproject.portal.util.ErrorReporter;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.ToolException;

/**
 * Handler for Error reports
 * 
 * @author ieb
 * @since Sakai 2.4
 * @version $Rev: 59680 $
 * 
 */
public class ErrorReportHandler extends BasePortalHandler
{

	private static final String URL_FRAGMENT = "error-report";

	public ErrorReportHandler()
	{
		setUrlFragment(ErrorReportHandler.URL_FRAGMENT);
	}

	@Override
	public int doPost(String[] parts, HttpServletRequest req, HttpServletResponse res,
			Session session) throws PortalHandlerException
	{
		return doGet(parts, req, res, session);
	}

	@Override
	public int doGet(String[] parts, HttpServletRequest req, HttpServletResponse res,
			Session session) throws PortalHandlerException
	{
		if ((parts.length >= 2) && (parts[1].equals(ErrorReportHandler.URL_FRAGMENT)))
		{
			try
			{
				doErrorReport(req, res);
				return END;
			}
			catch (Exception ex)
			{
				throw new PortalHandlerException(ex);
			}
		}
		else
		{
			return NEXT;
		}
	}

	protected void doErrorReport(HttpServletRequest req, HttpServletResponse res)
			throws ToolException, IOException
	{
		portal.setupForward(req, res, null, null);

		ErrorReporter err = new ErrorReporter();
		err.postResponse(req, res);
	}

}
