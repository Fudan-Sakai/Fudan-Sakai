/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/rwiki/branches/sakai-2.8.1/rwiki-tool/tool/src/java/uk/ac/cam/caret/sakai/rwiki/tool/bean/helper/ResourceLoaderHelperBean.java $
 * $Id: ResourceLoaderHelperBean.java 20355 2007-01-17 14:06:29Z ian@caret.cam.ac.uk $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package uk.ac.cam.caret.sakai.rwiki.tool.bean.helper;

import javax.servlet.http.HttpServletRequest;

import uk.ac.cam.caret.sakai.rwiki.tool.RequestScopeSuperBean;
import uk.ac.cam.caret.sakai.rwiki.tool.bean.ResourceLoaderBean;

/**
 * @author ieb
 *
 */
public class ResourceLoaderHelperBean
{
	/**
	 * gets the current resourceLoaderBean bound to he request
	 * @param request
	 * @return
	 */
	public static ResourceLoaderBean getResourceLoader(HttpServletRequest request) {
		RequestScopeSuperBean rssb = RequestScopeSuperBean.getFromRequest(request);
		return rssb.getResourceLoaderBean();
	}

	/**
	 * @return
	 */
	public static ResourceLoaderBean getResourceLoaderBean()
	{
		RequestScopeSuperBean rssb = RequestScopeSuperBean.getInstance();
		return rssb.getResourceLoaderBean();
	}

}
