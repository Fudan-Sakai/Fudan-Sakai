/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/login/branches/sakai-2.8.1/login-tool/tool/src/java/org/sakaiproject/login/tool/ContainerLogin.java $
 * $Id: ContainerLogin.java 74516 2010-03-10 23:13:05Z matthew.buckett@oucs.ox.ac.uk $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2008 The Sakai Foundation
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

package org.sakaiproject.login.tool;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.event.cover.UsageSessionService;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.user.api.Authentication;
import org.sakaiproject.user.api.AuthenticationException;
import org.sakaiproject.user.api.Evidence;
import org.sakaiproject.user.cover.AuthenticationManager;
import org.sakaiproject.util.ExternalTrustedEvidence;

/**
 * <p>
 * ContainerLogin ...
 * </p>
 */
public class ContainerLogin extends HttpServlet
{
	/** Our log (commons). */
	private static Log M_log = LogFactory.getLog(ContainerLogin.class);
	
	private String defaultReturnUrl;

	/**
	 * Access the Servlet's information display.
	 * 
	 * @return servlet information.
	 */
	public String getServletInfo()
	{
		return "Sakai Container Login";
	}

	/**
	 * Initialize the servlet.
	 * 
	 * @param config
	 *        The servlet config.
	 * @throws ServletException
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		M_log.info("init()");
		defaultReturnUrl = ServerConfigurationService.getString("portalPath", "/portal"); 
	}

	/**
	 * Shutdown the servlet.
	 */
	public void destroy()
	{
		M_log.info("destroy()");

		super.destroy();
	}

	/**
	 * Respond to requests.
	 * 
	 * @param req
	 *        The servlet request.
	 * @param res
	 *        The servlet response.
	 * @throws ServletException.
	 * @throws IOException.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		// get the session
		Session session = SessionManager.getCurrentSession();

		// check the remote user for authentication
		String remoteUser = req.getRemoteUser();
		try
		{
			Evidence e = new ExternalTrustedEvidence(remoteUser);
			Authentication a = AuthenticationManager.authenticate(e);

			// login the user
			if (UsageSessionService.login(a, req))
			{
				// get the return URL
				String url = getUrl(session, Tool.HELPER_DONE_URL);

				// cleanup session
				session.removeAttribute(Tool.HELPER_MESSAGE);
				session.removeAttribute(Tool.HELPER_DONE_URL);

				// redirect to the done URL
				res.sendRedirect(res.encodeRedirectURL(url));

				return;
			}
		}
		catch (AuthenticationException ex)
		{
			M_log.warn("Authentication Failed for: "+ remoteUser+ ". "+ ex.getMessage());
		}

		// mark the session and redirect (for login failuer or authentication exception)
		session.setAttribute(LoginTool.ATTR_CONTAINER_CHECKED, LoginTool.ATTR_CONTAINER_CHECKED);
		res.sendRedirect(res.encodeRedirectURL(getUrl(session, LoginTool.ATTR_RETURN_URL)));
	}

	/**
	 * Gets a URL from the session, if not found returns the portal URL.
	 * @param session The users HTTP session.
	 * @param sessionAttribute The attribute the URL is stored under.
	 * @return The URL.
	 */
	private String getUrl(Session session, String sessionAttribute) {
		String url = (String) session.getAttribute(sessionAttribute);
		if (url == null || url.length() == 0)
		{
			M_log.debug("No "+ sessionAttribute + " URL, redirecting to portal URL.");
			url = defaultReturnUrl;
		}
		return url;
	}
}
