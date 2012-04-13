/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-impl/src/test/org/sakaiproject/sitestats/test/mocks/FakePlacement.java $
 * $Id: FakePlacement.java 72172 2009-09-23 00:48:53Z arwhyte@umich.edu $
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

import java.util.Properties;

import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.tool.api.Tool;

public class FakePlacement implements Placement {
	Properties config;
	Properties placementConfig;
	String context;
	String id;
	String title;
	
	Tool tool;
	
	public FakePlacement(Tool tool, String context) {
		this.tool = tool;
		this.context = context;
	}
	
	public String getToolId() {
		return tool.getId();
	}

	public void save() {
	}

	public Properties getConfig() {
		return config;
	}

	public void setConfig(Properties config) {
		this.config = config;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Properties getPlacementConfig() {
		return placementConfig;
	}

	public void setPlacementConfig(Properties placementConfig) {
		this.placementConfig = placementConfig;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}
	
	public void setTool(String toolId, Tool tool) {
		this.tool = tool;
	}

}
