/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-tool/src/java/org/sakaiproject/sitestats/tool/wicket/SiteStatsApplication.java $
 * $Id: SiteStatsApplication.java 72176 2009-09-24 13:51:01Z nuno@ufp.edu.pt $
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
package org.sakaiproject.sitestats.tool.wicket;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.coding.AbstractRequestTargetUrlCodingStrategy;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.locator.ResourceStreamLocator;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.sitestats.tool.facade.SakaiFacade;
import org.sakaiproject.sitestats.tool.wicket.pages.OverviewPage;
import org.sakaiproject.util.ResourceLoader;


public class SiteStatsApplication extends WebApplication {
	private boolean					debug	= false;
	
	private transient SakaiFacade	facade;

	protected void init() {
		super.init();

		// Configure general wicket application settings
		addComponentInstantiationListener(new SpringComponentInjector(this));
		getResourceSettings().setThrowExceptionOnMissingResource(false);
		getMarkupSettings().setStripWicketTags(true);
		getResourceSettings().addStringResourceLoader(new SiteStatsStringResourceLoader());
		getResourceSettings().addResourceFolder("html");
		getResourceSettings().setResourceStreamLocator(new SiteStatsResourceStreamLocator());
		getDebugSettings().setAjaxDebugModeEnabled(debug);

		// Home page
		mountBookmarkablePage("/home", OverviewPage.class);
		// Prevent bad requests from: url(#default#VML)
		mount(new PassThroughUrlCodingStrategy("/home/panel/Main/none"));
		
		// On wicket session timeout, redirect to main page
		getApplicationSettings().setPageExpiredErrorPage(OverviewPage.class);
		getApplicationSettings().setAccessDeniedPage(OverviewPage.class);

		// Debugging
		debug = ServerConfigurationService.getBoolean("sitestats.debug", false);
		if(debug) {
			getDebugSettings().setComponentUseCheck(true);
			getDebugSettings().setAjaxDebugModeEnabled(true);
		    getDebugSettings().setLinePreciseReportingOnAddComponentEnabled(true);
		    getDebugSettings().setLinePreciseReportingOnNewComponentEnabled(true);
		    getDebugSettings().setOutputComponentPath(true);
		    getDebugSettings().setOutputMarkupContainerClassName(true);
		    getMarkupSettings().setStripWicketTags(false);
			getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_EXCEPTION_PAGE);
		}
	}

	@SuppressWarnings("unchecked")
	public Class getHomePage() {
		return OverviewPage.class;
	}

	public SakaiFacade getFacade() {
		return facade;
	}

	public void setFacade(final SakaiFacade facade) {
		this.facade = facade;
	}

	@Override
	public RequestCycle newRequestCycle(Request request, Response response) {
		if(!debug) {
			return new WebRequestCycle(this, (WebRequest)request, (WebResponse)response) {
				@Override
				public Page onRuntimeException(Page page, RuntimeException e) {
					// Let Sakai ErrorReportHandler (BugReport) handle errors
					throw e;
				}
			};
		}else{
			return super.newRequestCycle(request, response);
		}
	}

	/**
	 * Custom bundle loader to pickup bundles from sitestats-bundles/
	 * @author Nuno Fernandes
	 */
	private static class SiteStatsStringResourceLoader implements IStringResourceLoader {
		private ResourceLoader	messages	= new ResourceLoader("Messages");
		private ResourceLoader	events		= new ResourceLoader("Events");
		private ResourceLoader	nav			= new ResourceLoader("Navigator");

		public String loadStringResource(Component component, String key) {
			String value = null;
			if(messages.containsKey(key)) {
				value = messages.getString(key, null);
			}
			if(value == null && events.containsKey(key)){
				value = events.getString(key, null);
			}
			if(value == null && nav.containsKey(key)){
				value = nav.getString(key, null);
			}
			if(value == null){
				value = key;
			}
			return value;
		}

		public String loadStringResource(Class clazz, String key, Locale locale, String style) {
			ResourceLoader msgs = new ResourceLoader("Messages");
			msgs.setContextLocale(locale);
			String value = msgs.getString(key, null);
			if(value == null){
				ResourceLoader evnts = new ResourceLoader("Events");
				evnts.setContextLocale(locale);
				value = evnts.getString(key, null);
			}
			if(value == null){
				ResourceLoader nav = new ResourceLoader("Navigator");
				nav.setContextLocale(locale);
				value = nav.getString(key, null);
			}
			if(value == null){
				value = key;
			}
			return value;
		}

	}
	
	/**
	 * Custom loader for .html files
	 * @author Nuno Fernandes
	 */
	private static class SiteStatsResourceStreamLocator extends ResourceStreamLocator {

		public SiteStatsResourceStreamLocator() {
		}

		public IResourceStream locate(final Class clazz, final String path) {
			IResourceStream located = super.locate(clazz, trimFolders(path));
			if(located != null){
				return located;
			}
			return super.locate(clazz, path);
		}

		private String trimFolders(String path) {
			String wicketPackage = "/wicket/";
			return path.substring(path.lastIndexOf(wicketPackage) + wicketPackage.length());
		}
	}
	
	
	/** Pass-through coding strategy to unmount paths from Wicket */
	private class PassThroughUrlCodingStrategy extends AbstractRequestTargetUrlCodingStrategy {
		public PassThroughUrlCodingStrategy(final String mountPath) {
			super(mountPath);
		}

		public IRequestTarget decode(RequestParameters requestParameters) {
			return null;
		}

		public CharSequence encode(IRequestTarget requestTarget) {
			return null;
		}

		public boolean matches(IRequestTarget requestTarget) {
			return false;
		}
	}
}
