
/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/osp/branches/sakai-2.8.1/jsf/widgets/src/java/org/theospi/jsf/util/ConfigurationResource.java $
* $Id: ConfigurationResource.java 59678 2009-04-03 23:20:50Z arwhyte@umich.edu $
***********************************************************************************
*
 * Copyright (c) 2005, 2006, 2008 The Sakai Foundation
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
package org.theospi.jsf.util;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Thin wrapper for lookup of configuration of resources.
 * @author Ed Smiley
 * @version $Id: ConfigurationResource.java 59678 2009-04-03 23:20:50Z arwhyte@umich.edu $
 */
public class ConfigurationResource
{
  private ResourceBundle configurationBundle;
  private final String CONFIG_PACKAGE = "org.theospi.jsf";

  /**
   * Get resources for default locale.
   */
  public ConfigurationResource()
  {
    configurationBundle = ResourceBundle.getBundle(CONFIG_PACKAGE + "." + "Configuration");
  }

  /**
   * Get resources for specific locale.
   * @param locale Locale
   */
  public ConfigurationResource(Locale locale)
  {
    configurationBundle = ResourceBundle.getBundle(CONFIG_PACKAGE + "." +
      "Configuration", locale);
  }

  /**
   * Look up key/value
   * @param key String
   * @return String value for key, or empty string if not found
   */
  public String get(String key)
  {
    try
    {
      return configurationBundle.getString(key);
    }
    catch (Exception ex)
    {
      return "";
    }
  }

  /**
   * Return true only if this key exists.
   * @param key String
   * @return boolean
   */
  public boolean exists(String key)
  {
    try {
      configurationBundle.getString(key);
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }


  public static void main(String[] args)
  {
    ConfigurationResource cr = new ConfigurationResource();
    Enumeration enumeration = cr.configurationBundle.getKeys();
    while (enumeration.hasMoreElements())
    {
      String key = (String) enumeration.nextElement();
      String value = cr.get(key);
      System.out.println(key + "=" + value);
    }
    System.out.println("xxx exists" + "=" + cr.exists("xxx"));
    System.out.println("xxx" + "=" + cr.get("xxx"));
    System.out.println("inputRichText_none exists" + "=" + cr.exists("inputRichText_none"));
    System.out.println("inputRichText_none" + "=" + cr.get("inputRichText_none"));
    System.out.println("inputRichText_small exists" + "=" + cr.exists("inputRichText_small"));
    System.out.println("inputRichText_small" + "=" + cr.get("inputRichText_small"));
  }

}
