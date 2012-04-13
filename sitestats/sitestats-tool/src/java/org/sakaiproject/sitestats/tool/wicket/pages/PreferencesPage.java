/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-tool/src/java/org/sakaiproject/sitestats/tool/wicket/pages/PreferencesPage.java $
 * $Id: PreferencesPage.java 78669 2010-06-21 13:55:23Z nuno@ufp.edu.pt $
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
package org.sakaiproject.sitestats.tool.wicket.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.sakaiproject.sitestats.api.PrefsData;
import org.sakaiproject.sitestats.api.StatsManager;
import org.sakaiproject.sitestats.api.Util;
import org.sakaiproject.sitestats.api.event.ToolInfo;
import org.sakaiproject.sitestats.api.parser.EventParserTip;
import org.sakaiproject.sitestats.tool.facade.Locator;
import org.sakaiproject.sitestats.tool.wicket.components.CSSFeedbackPanel;
import org.sakaiproject.sitestats.tool.wicket.components.EventRegistryTree;
import org.sakaiproject.sitestats.tool.wicket.components.Menus;

/**
 * @author Nuno Fernandes
 */
public class PreferencesPage extends BasePage {
	private static final long		serialVersionUID			= 1L;
	
	private String realSiteId;
	private String siteId;

	// UI
	private FeedbackPanel			feedback					= null;
	private List<String>			chartTransparencyChoices	= null;
	private EventRegistryTree 		eventRegistryTree 			= null;


	// Model
	private PrefsData				prefsdata					= null;
	
	
	public PreferencesPage() {
		this(null);
	}

	public PreferencesPage(PageParameters pageParameters) {
		realSiteId = Locator.getFacade().getToolManager().getCurrentPlacement().getContext();
		if(pageParameters != null) {
			siteId = pageParameters.getString("siteId");
		}
		if(siteId == null){
			siteId = realSiteId;
		}
		boolean allowed = Locator.getFacade().getStatsAuthz().isUserAbleToViewSiteStats(siteId);
		if(allowed) {
			setModel(new CompoundPropertyModel(this));
			renderBody();
		}else{
			setResponsePage(NotAuthorizedPage.class);
		}
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.renderJavascriptReference(JQUERYSCRIPT);
		response.renderOnDomReadyJavascript("toggleCheckboxAll();");
	}
	
	@SuppressWarnings("serial")
	private void renderBody() {
		add(new Menus("menu", siteId));
		
		Form form = new Form("prefsForm");
		form.setOutputMarkupId(true);
		form.setMarkupId("prefsForm");
		add(form);
		feedback = new CSSFeedbackPanel("messages");
		form.add(feedback);
		
		
		// Section: General
		CheckBox listToolEventsOnlyAvailableInSite = new CheckBox("listToolEventsOnlyAvailableInSite");
		form.add(listToolEventsOnlyAvailableInSite);
		
		
		// Section: Chart
		WebMarkupContainer chartPrefs = new WebMarkupContainer("chartPrefs");
		boolean chartPrefsVisible = Locator.getFacade().getStatsManager().isEnableSiteVisits() || Locator.getFacade().getStatsManager().isEnableSiteActivity();
		chartPrefs.setVisible(chartPrefsVisible);
		form.add(chartPrefs);
		//CheckBox chartIn3D = new CheckBox("chartIn3D");
		//chartPrefs.add(chartIn3D);
		CheckBox itemLabelsVisible = new CheckBox("itemLabelsVisible");
		chartPrefs.add(itemLabelsVisible);
		chartTransparencyChoices = new ArrayList<String>();
		for(int i=100; i>=10; i-=10) {
			chartTransparencyChoices.add(Integer.toString(i));
		}
		DropDownChoice chartTransparency = new DropDownChoice("chartTransparency", chartTransparencyChoices, new IChoiceRenderer() {
			public Object getDisplayValue(Object object) {
				return (String) object + "%";
			}
			public String getIdValue(Object object, int index) {
				return (String) object;
			}			
		});
		chartPrefs.add(chartTransparency);
		
		
		// Section: Activity Definition
		CheckBox useAllTools = new CheckBox("useAllTools");
		useAllTools.add(new SimpleAttributeModifier("onclick", "toggleCheckboxAll();"));
		useAllTools.setOutputMarkupId(true);
		useAllTools.setMarkupId("useAllTools");
		form.add(useAllTools);
		eventRegistryTree = new EventRegistryTree("eventRegistryTree", getPrefsdata().getToolEventsDef()) {
			@Override
			public boolean isToolSuported(final ToolInfo toolInfo) {
				if(Locator.getFacade().getStatsManager().isEventContextSupported()){
					return true;
				}else{
					List<ToolInfo> siteTools = Locator.getFacade().getEventRegistryService().getEventRegistry(siteId, getPrefsdata().isListToolEventsOnlyAvailableInSite());
					Iterator<ToolInfo> i = siteTools.iterator();
					while (i.hasNext()){
						ToolInfo t = i.next();
						if(t.getToolId().equals(toolInfo.getToolId())){
							EventParserTip parserTip = t.getEventParserTip();
							if(parserTip != null && parserTip.getFor().equals(StatsManager.PARSERTIP_FOR_CONTEXTID)){
								return true;
							}
						}
					}
				}
				return false;
			}
		};
		form.add(eventRegistryTree);
		
		
		// Bottom Buttons
		Button update = new Button("update") {
			@Override
			public void onSubmit() {
				savePreferences();
				prefsdata = null;
				super.onSubmit();
			}
		};
		update.setDefaultFormProcessing(true);
		form.add(update);
		Button cancel = new Button("cancel") {
			@Override
			public void onSubmit() {
				prefsdata = null;
				super.onSubmit();
			}
		};
		cancel.setDefaultFormProcessing(false);
		form.add(cancel);
	}
	
	private PrefsData getPrefsdata() {
		if(prefsdata == null) {
			prefsdata = Locator.getFacade().getStatsManager().getPreferences(siteId, true);
		}
		return prefsdata;
	}

	private void savePreferences() {
		if(isUseAllTools()) {
			getPrefsdata().setToolEventsDef(Locator.getFacade().getEventRegistryService().getEventRegistry(siteId, isListToolEventsOnlyAvailableInSite()));
		}else{
			getPrefsdata().setToolEventsDef((List<ToolInfo>) eventRegistryTree.getEventRegistry());
		}
		boolean opOk = Locator.getFacade().getStatsManager().setPreferences(siteId, getPrefsdata());		
		if(opOk){
			info((String) new ResourceModel("prefs_updated").getObject());
		}else{
			error((String) new ResourceModel("prefs_not_updated").getObject());
		}
	}

	public void setListToolEventsOnlyAvailableInSite(boolean listToolEventsOnlyAvailableInSite) {
		prefsdata.setListToolEventsOnlyAvailableInSite(listToolEventsOnlyAvailableInSite);
	}

	public boolean isListToolEventsOnlyAvailableInSite() {
		return getPrefsdata().isListToolEventsOnlyAvailableInSite();
	}
	
	public void setChartIn3D(boolean chartIn3D) {
		prefsdata.setChartIn3D(chartIn3D);
	}

	public boolean isChartIn3D() {
		return getPrefsdata().isChartIn3D();
	}
	
	public void setUseAllTools(boolean useAllTools) {
		prefsdata.setUseAllTools(useAllTools);
	}

	public boolean isUseAllTools() {
		return getPrefsdata().isUseAllTools();
	}

	public void setItemLabelsVisible(boolean itemLabelsVisible) {
		prefsdata.setItemLabelsVisible(itemLabelsVisible);
	}

	public boolean isItemLabelsVisible() {
		return getPrefsdata().isItemLabelsVisible();
	}
	
	public void setChartTransparency(String value) {
		float converted = (float) Util.round(Double.parseDouble(value)/100,1);
		getPrefsdata().setChartTransparency(converted);
	}
	
	public String getChartTransparency() {
		return Integer.toString((int) Util.round(getPrefsdata().getChartTransparency()*100,0) );
	}
}

