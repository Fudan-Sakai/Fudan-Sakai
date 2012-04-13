/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/portal/branches/sakai-2.8.1/portal-tool/tool/src/java/org/sakaiproject/portal/tool/ToolPortal.java $
 * $Id: ToolPortal.java 81610 2010-08-22 18:06:35Z csev@umich.edu $
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

package org.sakaiproject.portal.tool;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.portal.util.ErrorReporter;
import org.sakaiproject.portal.util.ToolURLManagerImpl;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.tool.api.ActiveTool;
import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.api.ToolException;
import org.sakaiproject.tool.api.ToolSession;
import org.sakaiproject.tool.api.ToolURL;
import org.sakaiproject.tool.cover.ActiveToolManager;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.util.StringUtil;
import org.sakaiproject.util.Web;

/**
 * @author ieb
 * @since Sakai 2.4
 * @version $Rev: 81610 $
 */

public class ToolPortal extends HttpServlet
{
	/** Our log (commons). */
	private static Log M_log = LogFactory.getLog(ToolPortal.class);

	/**
	 * Access the Servlet's information display.
	 * 
	 * @return servlet information.
	 */
	@Override
	public String getServletInfo()
	{
		return "Sakai Tool Portal";
	}

	/**
	 * Initialize the servlet.
	 * 
	 * @param config
	 *        The servlet config.
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		M_log.info("init()");
	}

	/**
	 * Shutdown the servlet.
	 */
	@Override
	public void destroy()
	{
		M_log.info("destroy()");

		super.destroy();
	}

	/**
	 * Respond to navigation / access requests.
	 * 
	 * @param req
	 *        The servlet request.
	 * @param res
	 *        The servlet response.
	 * @throws ServletException.
	 * @throws IOException.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		try
		{
			// get the Sakai session
			Session session = SessionManager.getCurrentSession();

			// our path is /placement-id/tool-destination, but we want to
			// include anchors and parameters in the destination...
			String path = req.getPathInfo();
			if ((path == null) || (path.length() <= 1))
			{
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			// get the placement id, ignoring the first "/"
			String[] parts = StringUtil.splitFirst(path.substring(1), "/");
			String placementId = parts[0];

			// get the toolPath if specified
			String toolPath = null;
			if (parts.length == 2) toolPath = "/" + parts[1];

			boolean success = doTool(req, res, session, placementId, req.getContextPath()
					+ req.getServletPath() + "/" + placementId, toolPath);

		}
		catch (Exception t)
		{
			doError(req, res, t);
		}
	}

	/**
	 * Process a tool request
	 * 
	 * @param req
	 * @param res
	 * @param session
	 * @param placementId
	 * @param toolContextPath
	 * @param toolPathInfo
	 * @return true if the processing was successful, false if nt
	 * @throws ToolException
	 * @throws IOException
	 */
	protected boolean doTool(HttpServletRequest req, HttpServletResponse res,
			Session session, String placementId, String toolContextPath,
			String toolPathInfo) throws ToolException, IOException
	{
		// find the tool from some site
		// TODO: all placements are from sites? -ggolden
		ToolConfiguration siteTool = SiteService.findTool(placementId);
		if (siteTool == null) return false;

		// find the tool registered for this
		ActiveTool tool = ActiveToolManager.getActiveTool(siteTool.getToolId());
		if (tool == null) return false;

		// permission check - visit the site (unless the tool is configured to
		// bypass)
		if (tool.getAccessSecurity() == Tool.AccessSecurity.PORTAL)
		{
			Site site = null;
			try
			{
				site = SiteService.getSiteVisit(siteTool.getSiteId());
			}
			catch (IdUnusedException e)
			{
				return false;
			}
			catch (PermissionException e)
			{
				// TODO: login here?
				return false;
			}
		}

		// if the path is not set, and we are expecting one, we need to compute
		// the path and redirect
		// we expect a path only if the tool has a registered home -ggolden
		if ((toolPathInfo == null) && (tool.getHome() != null))
		{
			// what path? The one last visited, or home
			ToolSession toolSession = SessionManager.getCurrentSession().getToolSession(
					placementId);
			String redirectPath = (String) toolSession
					.getAttribute(ActiveTool.TOOL_ATTR_CURRENT_DESTINATION);
			if (redirectPath == null)
			{
				redirectPath = tool.getHome();
			}

			// redirect with this tool path
			String redirectUrl = ServerConfigurationService.getServerUrl()
					+ toolContextPath + redirectPath;
			res.sendRedirect(res.encodeRedirectURL(redirectUrl));
			return true;
		}

		// store the path as the current path, if we are doing this
		if (tool.getHome() != null)
		{
			ToolSession toolSession = SessionManager.getCurrentSession().getToolSession(
					placementId);
			toolSession.setAttribute(ActiveTool.TOOL_ATTR_CURRENT_DESTINATION,
					toolPathInfo);
		}

		// prepare for the forward
		setupForward(req, res, siteTool, siteTool.getSkin());
		req.setAttribute(ToolURL.MANAGER, new ToolURLManagerImpl(res));

		// let the tool do the the work (forward)
		tool.forward(req, res, siteTool, toolContextPath, toolPathInfo);

		return true;
	}

	protected void doError(HttpServletRequest req, HttpServletResponse res, Throwable t)
	{
		ErrorReporter err = new ErrorReporter();
		err.report(req, res, t);
	}

	/**
	 * Respond to data posting requests.
	 * 
	 * @param req
	 *        The servlet request.
	 * @param res
	 *        The servlet response.
	 * @throws ServletException.
	 * @throws IOException.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{
		doGet(req, res);
	}

	/**
	 * Setup the request attributes with information used by the tools in their
	 * response.
	 * 
	 * @param req
	 * @param res
	 * @param p
	 * @param skin
	 * @throws ToolException
	 */
	// NOTE: This code is duplicated in SkinnableCharonPortal.java
	// make sure to change code both places
	protected void setupForward(HttpServletRequest req, HttpServletResponse res,
			Placement p, String skin) throws ToolException
	{
		// setup html information that the tool might need (skin, body on load,
		// js includes, etc).
		if (skin == null || skin.length() == 0)
			skin = ServerConfigurationService.getString("skin.default");
		String skinRepo = ServerConfigurationService.getString("skin.repo");
		String headCssToolBase = "<link href=\""
				+ skinRepo
				+ "/tool_base.css\" type=\"text/css\" rel=\"stylesheet\" media=\"all\" />\n";
		String headCssToolSkin = "<link href=\"" + skinRepo + "/" + skin
				+ "/tool.css\" type=\"text/css\" rel=\"stylesheet\" media=\"all\" />\n";
		String headCss = headCssToolBase + headCssToolSkin;
		String headJs = "<script type=\"text/javascript\" language=\"JavaScript\" src=\"/library/js/headscripts.js\"></script>\n";
		String head = headCss + headJs;
		StringBuilder bodyonload = new StringBuilder();
		if (p != null)
		{
			String element = Web.escapeJavascript("Main" + p.getId());
			bodyonload.append("setMainFrameHeight('" + element + "');");
		}
		bodyonload.append("setFocus(focus_path);");

		req.setAttribute("sakai.html.head", head);
		req.setAttribute("sakai.html.head.css", headCss);
		req.setAttribute("sakai.html.head.css.base", headCssToolBase);
		req.setAttribute("sakai.html.head.css.skin", headCssToolSkin);
		req.setAttribute("sakai.html.head.js", headJs);
		req.setAttribute("sakai.html.body.onload", bodyonload.toString());
	}
}
