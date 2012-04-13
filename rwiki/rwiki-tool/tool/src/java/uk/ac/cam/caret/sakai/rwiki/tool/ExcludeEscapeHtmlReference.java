/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/rwiki/branches/sakai-2.8.1/rwiki-tool/tool/src/java/uk/ac/cam/caret/sakai/rwiki/tool/ExcludeEscapeHtmlReference.java $
 * $Id: ExcludeEscapeHtmlReference.java 22722 2007-03-16 01:21:13Z ian@caret.cam.ac.uk $
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

package uk.ac.cam.caret.sakai.rwiki.tool;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.sakaiproject.util.Web;

/**
 * @author ieb
 *
 */
public class ExcludeEscapeHtmlReference implements ReferenceInsertionEventHandler
{

	private static final Log log = LogFactory.getLog(ExcludeEscapeHtmlReference.class);
	private static Map<String,String> ignore = new HashMap<String, String>();

	static{
		ignore.put("renderedPage", "renderedPage");
		ignore.put("colorDiffTable", "colorDiffTable");
		ignore.put("previewPage","previewPage");
		ignore.put("footerScript","footerScript");
		ignore.put("${request.getAttribute(\"sakai.html.head\")}","1");
		ignore.put("${util.escapeHtml(${viewBean.rssAccessUrl})}","1");
	}
	/* (non-Javadoc)
	 * @see org.apache.velocity.app.event.ReferenceInsertionEventHandler#referenceInsert(java.lang.String, java.lang.Object)
	 */
	public Object referenceInsert(String name, Object value)
	{
		if ( ignore .get(name) != null || name.contains("rlb.") ||
				name.contains("util.escapeHtml") ||
				name.contains("resourceLoaderBean.getString")) {
			log.warn("Ignoring "+name);
			return value;
		} else {
			log.warn("Escaping "+name);
			return Web.escapeHtml(String.valueOf(value));
		}
		
	}

}
