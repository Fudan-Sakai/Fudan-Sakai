/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/portal/branches/sakai-2.8.1/portal-portlet-tool/tool/src/java/org/sakaiproject/portal/portlet/velocity/VelocityPortletRenderContext.java $
 * $Id: VelocityPortletRenderContext.java 59680 2009-04-03 23:28:39Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2007, 2008 The Sakai Foundation
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

package org.sakaiproject.portal.portlet.velocity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.sakaiproject.portal.portlet.PortletRenderContext;

/**
 * A render context based on the velocity context
 * 
 * @author ieb
 */
public class VelocityPortletRenderContext implements PortletRenderContext
{
	private static final Log log = LogFactory.getLog(VelocityPortletRenderContext.class);

	private Context vcontext = new VelocityContext();

	private boolean debug = false;

	private Map options = null;

	public boolean isDebug()
	{
		return debug;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public void put(String key, Object value)
	{
		vcontext.put(key, value);
	}

	public Context getVelocityContext()
	{
		return vcontext;
	}

	public String dump()
	{
		if (debug)
		{
			Object[] keys = vcontext.getKeys();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < keys.length; i++)
			{
				Object o = vcontext.get((String) keys[i]);
				dumpObject(sb, keys[i], o);
			}
			return sb.toString();
		}
		else
		{
			return "";
		}
	}

	private void dumpObject(StringBuilder sb, Object key, Object o)
	{
		if (o instanceof Map)
		{
			sb.append("Property ").append(key).append(":").append(o).append(
					"\n");
			dumpMap(sb, key, (Map) o);
		}
		else if (o instanceof Collection)
		{
			sb.append("Property ").append(key).append(":").append(o).append(
					"\n");
			dumpCollection(sb, key, (Collection) o);
		}
		else
		{
			sb.append("Property ").append(key).append(":").append(o).append(
					"\n");
		}
	}

	private void dumpCollection(StringBuilder sb, Object key,
			Collection collection)
	{
		int n = 0;
		for (Iterator i = collection.iterator(); i.hasNext();)
		{
			String keyn = key.toString() + "." + String.valueOf(n);
			dumpObject(sb, keyn, i.next());
			n++;
		}
	}

	private void dumpMap(StringBuilder sb, Object key, Map map)
	{
		for (Iterator i = map.keySet().iterator(); i.hasNext();)
		{
			Object keyn = i.next();
			dumpObject(sb, key + "." + keyn, map.get(keyn));
		}
	}

	public boolean uses(String includeOption)
	{
		
		if (options == null || includeOption == null )
		{
			return true;
		}
		return "true".equals(options.get(includeOption));
	}

	public Map getOptions()
	{
		return options;
	}

	public void setOptions(Map options)
	{
		this.options = options;
	}

}
