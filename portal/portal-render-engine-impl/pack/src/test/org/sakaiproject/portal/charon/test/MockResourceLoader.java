/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/portal/branches/sakai-2.8.1/portal-render-engine-impl/pack/src/test/org/sakaiproject/portal/charon/test/MockResourceLoader.java $
 * $Id: MockResourceLoader.java 75025 2010-03-23 12:37:19Z matthew.buckett@oucs.ox.ac.uk $
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
package org.sakaiproject.portal.charon.test;

import java.util.HashMap;

/**
 * Created on 28 Aug 2007 Antranig Basman
 */
public class MockResourceLoader extends HashMap<String, String>
{

	public String get(String key)
	{
		return "Message for key " + key;
	}
	
	public String getFormattedMessage(String key, Object[] args)
	{
		return "Message for key " + key + " with args "+ args;
	}

}
