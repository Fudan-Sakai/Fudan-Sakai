/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-impl/src/test/org/sakaiproject/sitestats/test/mocks/FakeToolManager.java $
 * $Id: FakeToolManager.java 79314 2010-07-10 13:10:54Z david.horwitz@uct.ac.za $
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
package org.sakaiproject.sitestats.test.mocks;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.sitestats.api.StatsManager;
import org.sakaiproject.sitestats.test.data.FakeData;
import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.api.ToolManager;
import org.w3c.dom.Document;

public class FakeToolManager implements ToolManager {

	Set<Tool> tools = new HashSet<Tool>();
	Placement currentPlacement;
	FakeTool dropbox;
	FakeTool resources;
	FakeTool chat;
	FakeTool sitestats;

	public FakeToolManager() {
		dropbox = new FakeTool();
		dropbox.setId(StatsManager.DROPBOX_TOOLID);
		dropbox.setTitle("DropBox");
		tools.add(dropbox);
		resources = new FakeTool();
		resources.setId(StatsManager.RESOURCES_TOOLID);
		resources.setTitle("Resources");
		tools.add(resources);
		chat = new FakeTool();
		chat.setId(FakeData.TOOL_CHAT);
		chat.setTitle("Chat");
		tools.add(chat);
		sitestats = new FakeTool();
		sitestats.setId(StatsManager.SITESTATS_TOOLID);
		sitestats.setTitle("SiteStats");
		tools.add(sitestats);
		
		//setDefaultPlacementContext();
		currentPlacement = new FakePlacement(resources, null);
	}
	
	public void setDefaultPlacementContext(String siteId) {
		currentPlacement = new FakePlacement(resources, siteId);
	}
	
	public void setDefaultPlacementContext() {
		currentPlacement = new FakePlacement(resources, FakeData.SITE_A_ID);
	}
	
	public Placement getDefaultPlacementContext() {
		return currentPlacement;
	}
	
	public Set<Tool> findTools(Set categories, Set keywords) {
		return tools;
	}

	public Set<Tool> getTools() {
		return tools;
	}

	public void setTools(Set<Tool> tools) {
		this.tools = tools;
	}

	public Placement getCurrentPlacement() {
		return currentPlacement;
	}

	public void setCurrentPlacement(Placement currentPlacement) {
		this.currentPlacement = currentPlacement;
	}

	public Tool getCurrentTool() {
		return currentPlacement.getTool();
	}

	public Tool getTool(String id) {
		for(Iterator<Tool> iter = tools.iterator(); iter.hasNext();) {
			Tool tool = iter.next();
			if(tool.getId().equals(id)) {
				return tool;
			}
		}
		return null;
	}

	public void register(Tool tool) {
	}

	public void register(Document toolXml) {
	}

	public void register(File toolXmlFile) {
	}

	public void register(InputStream toolXmlStream) {
	}

	public void setResourceBundle(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}



	public boolean isVisible(Site arg0, ToolConfiguration arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
