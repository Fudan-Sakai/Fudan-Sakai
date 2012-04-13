/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sections/branches/sakai-2.8.1/sections-app-util/src/java/org/sakaiproject/tool/section/jsf/JsfUtil.java $
 * $Id: JsfUtil.java 88759 2011-02-23 17:41:38Z arwhyte@umich.edu $
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
package org.sakaiproject.tool.section.jsf;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.jsf.util.ConversionUtil;
import org.sakaiproject.tool.section.jsf.MessagingBean;

import org.sakaiproject.util.ResourceLoader;


/**
 * A utility to help deal with common tasks in JSF.
 *
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 *
 */
public class JsfUtil {
	private static final Log log = LogFactory.getLog(JsfUtil.class);

	/**
	 * As part of the crutch for JSF's inability to do validation on relative
	 * values in different components.  This pattern defines how times should
	 * be displayed in the Section Info UI.
	 */
	public static final String TIME_PATTERN_DISPLAY = "h:mm";


	/**
	 * As part of the crutch for JSF's inability to do validation on relative
	 * values in different components.  This pattern defines how to parse and
	 * format complete times (with hours, minutes, and am/pm marker).
	 */
	public static final String TIME_PATTERN_LONG = "h:mm a";

	/**
	 * As part of the crutch for JSF's inability to do validation on relative
	 * values in different components.  This pattern defines how to parse and
	 * format abberviated times (with only hours and am/pm marker).
	 */
	public static final String TIME_PATTERN_SHORT = "h a";


	/**
	 * To cut down on configuration noise, allow access to request-scoped beans from
	 * session-scoped beans, and so on, this method lets the caller try to find
	 * anything anywhere that Faces can look for it.
	 *
	 * WARNING: If what you're looking for is a managed bean and it isn't found,
	 * it will be created as a result of this call.
	 */
	public static final Object resolveVariable(String name) {
		FacesContext context = FacesContext.getCurrentInstance();
		Object result = context.getApplication().getVariableResolver().resolveVariable(context, name);
		if(log.isDebugEnabled()) log.debug("JSF variable " + name + " resolved to " + result);
		return result;
	}

	/**
     * Adds an error message for display on a page when the page is guaranteed
     * not to be displayed via a redirect.
	 *
	 * @param message
	 */
	public static void addErrorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
	}

	/**
     * Adds an error message for display on a component when the page is guaranteed
     * not to be displayed via a redirect.
	 *
	 * @param message
	 * @param componentId
	 */
	public static void addErrorMessage(String message, String componentId) {
		FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
	}

	/**
     * Adds an info message for display on a page when the page is guaranteed
     * not to be displayed via a redirect.
	 *
	 * @param message
	 */
	public static void addInfoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}

    /**
     * Adds an info message for display on a page even if faces sends the user
     * to the page via a redirect.
     *
     * @param message
     */
	public static void addRedirectSafeInfoMessage(String message) {
        MessagingBean mb = (MessagingBean)resolveVariable("messagingBean");
        mb.addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    /**
     * Adds a warning message for display on a page even if faces sends the user
     * to the page via a redirect.
     *
     * @param message
     */
    public static void addRedirectSafeWarnMessage(String message) {
        MessagingBean mb = (MessagingBean)resolveVariable("messagingBean");
        mb.addMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
    }

    /**
     * Gets a localized message from the message bundle.
     */
    public static String getLocalizedMessage(String key) {
		ResourceLoader rl = new ResourceLoader("sections");
		return rl.getString(key);
	}

    /**
     * Gets a localized message from the message bundle and formats it using the
     * parameter array.
     *
     * @param key
     * @param params
     * @return
     */
    public static String getLocalizedMessage(String key, String[] params) {
		ResourceLoader rl = new ResourceLoader("sections");
		return rl.getFormattedMessage(key, params);
    }


	/**
	 * Gets a value from the request parameter map, as provided by the faces
	 * context.
	 *
	 * @param string
	 * @return
	 */
    public static String getStringFromParam(String string) {
		return (String)FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap().get(string);
	}

	/**
	 * Converts a string and a boolean (am) into a java.sql.Time object.
	 *
	 * @param str
	 * @param am
	 * @return
	 */
	public static Time convertStringToTime(String str, boolean am) {
		if(StringUtils.trimToNull(str) == null) {
			return null;
		}

		// Set the am/pm flag to ensure that the time is parsed properly
		if(am) {
			str = str + " AM";
		} else {
			str = str + " PM";
		}

		String pattern = (str.indexOf(':') != -1) ? JsfUtil.TIME_PATTERN_LONG : JsfUtil.TIME_PATTERN_SHORT;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
		Date date;
		try {
			date = sdf.parse(str);
		} catch (ParseException pe) {
			throw new RuntimeException("A bad date made it through validation!  This should never happen!");
		}
		return ConversionUtil.convertDateToTime(date, am);
	}

	public static String convertTimeToString(Time time) {
		if(time == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(JsfUtil.TIME_PATTERN_DISPLAY, new ResourceLoader().getLocale());
		return sdf.format(time);
	}


	public static final  Comparator getSelectItemComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				SelectItem item1 = (SelectItem)o1;
				SelectItem item2 = (SelectItem)o2;
				return item1.getLabel().toString().compareTo(item2.getLabel().toString());
			}
		};
	}


}
