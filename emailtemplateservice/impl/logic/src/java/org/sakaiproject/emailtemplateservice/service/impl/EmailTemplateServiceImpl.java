/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/emailtemplateservice/tags/emailtemplateservice-0.5.4/impl/logic/src/java/org/sakaiproject/emailtemplateservice/service/impl/EmailTemplateServiceImpl.java $
 * $Id: EmailTemplateServiceImpl.java 98753 2011-09-29 13:45:54Z ottenhoff@longsight.com $
 ***********************************************************************************
 *
 * Copyright 2006, 2007 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
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

package org.sakaiproject.emailtemplateservice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.email.api.EmailService;
import org.sakaiproject.emailtemplateservice.dao.impl.EmailTemplateServiceDao;
import org.sakaiproject.emailtemplateservice.model.EmailTemplate;
import org.sakaiproject.emailtemplateservice.model.EmailTemplateLocaleUsers;
import org.sakaiproject.emailtemplateservice.model.RenderedTemplate;
import org.sakaiproject.emailtemplateservice.service.EmailTemplateService;
import org.sakaiproject.emailtemplateservice.util.TextTemplateLogicUtils;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.entitybroker.DeveloperHelperService;
import org.sakaiproject.genericdao.api.search.Restriction;
import org.sakaiproject.genericdao.api.search.Search;
import org.sakaiproject.i18n.InternationalizedMessages;
import org.sakaiproject.user.api.Preferences;
import org.sakaiproject.user.api.PreferencesService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.tool.api.Session;

import org.simpleframework.xml.core.Persister;

public class EmailTemplateServiceImpl implements EmailTemplateService {

   private static Log log = LogFactory.getLog(EmailTemplateServiceImpl.class);

   private EmailTemplateServiceDao dao;
   public void setDao(EmailTemplateServiceDao d) {
      dao = d;
   }

   private DeveloperHelperService developerHelperService;
   public void setDeveloperHelperService(DeveloperHelperService developerHelperService) {
      this.developerHelperService = developerHelperService;
   }

   private PreferencesService preferencesService;
   public void setPreferencesService(PreferencesService ps) {
      preferencesService = ps;
   }

   private ServerConfigurationService serverConfigurationService;
   public void setServerConfigurationService(ServerConfigurationService scs) {
      serverConfigurationService = scs;
   }

   private SessionManager sessionManager;
   public void setSessionManager(SessionManager sm) {
      sessionManager = sm;
   }

   public EmailTemplate getEmailTemplateById(Long id) {
	   if (id == null) {
		   throw new IllegalArgumentException("id cannot be null or empty");
	   }
	   EmailTemplate et = dao.findById(EmailTemplate.class, id);
	   return et;
   }

   private EmailService emailService;

   public void setEmailService(EmailService emailService) {
	   this.emailService = emailService;
   }

   private UserDirectoryService userDirectoryService;
   public void setUserDirectoryService(UserDirectoryService userDirectoryService) {
	   this.userDirectoryService = userDirectoryService;
   }


   

   public EmailTemplate getEmailTemplate(String key, Locale locale) {
      if (key == null || "".equals(key)) {
         throw new IllegalArgumentException("key cannot be null or empty");
      }
      EmailTemplate et = null;
      // TODO make this more efficient
      if (locale != null) {
         Search search = new Search("key", key);
         search.addRestriction( new Restriction("locale", locale.toString()) );
         et = dao.findOneBySearch(EmailTemplate.class, search);
         if (et == null) {
            search.addRestriction( new Restriction("locale", locale.getLanguage()) );
            et = dao.findOneBySearch(EmailTemplate.class, search);
         }
      }
      else if (et == null) {
         et = dao.findOneBySearch(EmailTemplate.class, new Search("key", key));
      }
      if (et == null) {
         log.warn("no template found for: " + key + " in locale " + locale );
      }
      return et;
   }

   
	public boolean templateExists(String key, Locale locale) {
		List<EmailTemplate> et = null;
		Search search = new Search("key", key);
		search.addRestriction( new Restriction("locale", locale.toString()) );
        et = dao.findBySearch(EmailTemplate.class, search);
        if (et != null && et.size() > 0) {
        	return true;
        }
        
		return false;
	}
   
   
   public List<EmailTemplate> getEmailTemplates(int max, int start) {
      return dao.findAll(EmailTemplate.class, start, max);
   }

   public RenderedTemplate getRenderedTemplate(String key, Locale locale, Map<String, String> replacementValues) {
      EmailTemplate temp = getEmailTemplate(key, locale);
      //if no template was found we need to return null to avoid an NPE
      if (temp == null)
    	  return null;
      
      RenderedTemplate ret = new RenderedTemplate(temp);

      //get the default current user fields
      log.debug("getting default values");

      Map<String, String> userVals = getCurrentUserFields();
      replacementValues.putAll(userVals);
      log.debug("got replacement values");

      ret.setRenderedSubject(this.processText(ret.getSubject(), replacementValues, key));
      ret.setRenderedMessage(this.processText(ret.getMessage(), replacementValues, key));
      //HTML component might be null
      if (ret.getHtmlMessage() != null)
    	  ret.setRenderedHtmlMessage(this.processText(ret.getHtmlMessage(), replacementValues, key));
      return ret;
   }

   public RenderedTemplate getRenderedTemplateForUser(String key, String userReference, Map<String, String> replacementValues) {
      log.debug("getRenderedTemplateForUser(" + key + ", " +userReference);
	  String userId = developerHelperService.getUserIdFromRef(userReference);
      Locale loc = getUserLocale(userId);
      return getRenderedTemplate(key,loc,replacementValues);
   }

   public void saveTemplate(EmailTemplate template) {
      //update the modified date
      template.setLastModified(new Date());

      dao.save(template);
      log.info("saved template: " + template.getId());
   }
   
   public void updateTemplate(EmailTemplate template) {
	   template.setLastModified(new Date());
	   dao.update(template);
	   log.info("updated template: " + template.getId());
	}

   protected Locale getUserLocale(String userId) {
      Locale loc = null;
      Preferences prefs = preferencesService.getPreferences(userId);
      ResourceProperties locProps = prefs.getProperties(InternationalizedMessages.APPLICATION_ID);
      String localeString = locProps.getProperty(InternationalizedMessages.LOCALE_KEY);

      if (localeString != null)
      {			String[] locValues = localeString.split("_");
      if (locValues.length > 1)
         loc = new Locale(locValues[0], locValues[1]); // language, country
      else if (locValues.length == 1) 
         loc = new Locale(locValues[0]); // just language
      }
      //the user has no preference set - get the system default
      if (loc == null ) {
         String lang = System.getProperty("user.language");
         String region = System.getProperty("user.region");

         if (region != null) {
            log.debug("getting system locale for: " + lang + "_" + region);
            loc = new Locale(lang,region);
         } else { 
            log.debug("getting system locale for: " + lang );
            loc = new Locale(lang);
         }
      }

      return loc;
   }


   protected String processText(String text, Map<String, String> values, String templateName) {
      return TextTemplateLogicUtils.processTextTemplate(text, values, templateName);
   }

   protected Map<String, String> getCurrentUserFields() {
      Map<String, String> rv = new HashMap<String, String>();
      String userRef = developerHelperService.getCurrentUserReference();
      if (userRef != null) {
         User user = (User) developerHelperService.fetchEntity(userRef);
         try {
            String email = user.getEmail();
            if (email == null)
               email = "";
            String first = user.getFirstName();
            if (first == null)
               first = "";
            String last = user.getLastName();
            if (last == null)
               last ="";
   
            rv.put(CURRENT_USER_EMAIL, email);
            rv.put(CURRENT_USER_FIRST_NAME, first);
            rv.put(CURRENT_USER_LAST_NAME, last);
            rv.put(CURRENT_USER_DISPLAY_NAME, user.getDisplayName());
            rv.put(CURRENT_USER_DISPLAY_ID, user.getDisplayId());
            rv.put("currentUserDispalyId", user.getDisplayId());
            
         } catch (Exception e) {
            log.warn("Failed to get current user replacements: " + userRef, e);
         }
      }
      /*NoN user fields */
      rv.put(LOCAL_SAKAI_NAME, serverConfigurationService.getString("ui.service", "Sakai"));
      rv.put(LOCAL_SAKAI_SUPPORT_MAIL,serverConfigurationService.getString("support.email","help@"+ serverConfigurationService.getServerUrl()));
      rv.put(LOCAL_SAKAI_URL,serverConfigurationService.getServerUrl());

      
      return rv;
   }


public Map<EmailTemplateLocaleUsers, RenderedTemplate> getRenderedTemplates(
		String key, List<String> userReferences, Map<String, String> replacementValues) {
	
	List<Locale> foundLocales = new ArrayList<Locale>();
	Map<Locale, EmailTemplateLocaleUsers> mapStore = new HashMap<Locale, EmailTemplateLocaleUsers>();
	
	for (int i = 0; i < userReferences.size(); i++) {
		String userReference = userReferences.get(i);
		String userId = developerHelperService.getUserIdFromRef(userReference);
        Locale loc = getUserLocale(userId);
        //have we found this locale?
        if (! foundLocales.contains(loc)) {
        	//create a new EmailTemplateLocalUser
        	EmailTemplateLocaleUsers etlu = new EmailTemplateLocaleUsers();
        	log.debug("adding users " + userReference + " to new object");
        	etlu.setLocale(loc);
        	etlu.addUser(userReference);
        	mapStore.put(loc, etlu);
        	foundLocales.add(loc);
        } else {
        	EmailTemplateLocaleUsers etlu = mapStore.get(loc);
        	log.debug("adding users " + userReference + " to existing object");
        	etlu.addUser(userReference);
        	mapStore.remove(loc);
        	mapStore.put(loc, etlu);
        }
	}
	
	Map<EmailTemplateLocaleUsers, RenderedTemplate> ret = new HashMap<EmailTemplateLocaleUsers, RenderedTemplate>();
	
	//now for each locale we need a rendered template
	Set<Entry<Locale, EmailTemplateLocaleUsers>> es = mapStore.entrySet();
	Iterator<Entry<Locale, EmailTemplateLocaleUsers>> it = es.iterator();
	while (it.hasNext()) {
		Entry<Locale, EmailTemplateLocaleUsers> entry = it.next();
		Locale loc = entry.getKey();
		RenderedTemplate rt = this.getRenderedTemplate(key, loc, replacementValues);
		if (rt != null) {
			ret.put(entry.getValue(), rt);
		} else {
			log.error("No template found for key: " + key + " in locale: " + loc);
		}
		
	}
	return ret;
}

private final String MULTIPART_BOUNDARY = "======sakai-multi-part-boundary======";
private final String BOUNDARY_LINE = "\n\n--"+MULTIPART_BOUNDARY+"\n";
private final String TERMINATION_LINE = "\n\n--"+MULTIPART_BOUNDARY+"--\n\n";
private final String MIME_ADVISORY = "This message is for MIME-compliant mail readers.";


public void sendRenderedMessages(String key, List<String> userReferences,
		Map<String, String> replacementValues, String fromEmail, String fromName) {

	Map<EmailTemplateLocaleUsers, RenderedTemplate> tMap = this.getRenderedTemplates(key, userReferences, replacementValues);
	Set<Entry<EmailTemplateLocaleUsers, RenderedTemplate>> set = tMap.entrySet();
	Iterator<Entry<EmailTemplateLocaleUsers, RenderedTemplate>> it = set.iterator();
	
	while (it.hasNext()) {
	
			
				Entry<EmailTemplateLocaleUsers, RenderedTemplate> entry = it.next();
				RenderedTemplate rt = entry.getValue();
				EmailTemplateLocaleUsers etlu = entry.getKey();
				List<User> toAddress = getUsersEmail(etlu.getUserIds());
				log.info("sending template " + key + " for locale " + etlu.getLocale().toString() + " to " + toAddress.size() + " users");
				StringBuilder message = new StringBuilder();
				message.append(MIME_ADVISORY);
				if (rt.getRenderedMessage() != null) {
					message.append(BOUNDARY_LINE);
					message.append("Content-Type: text/plain; charset=iso-8859-1\n");
					message.append(rt.getRenderedMessage());
				}
				if (rt.getRenderedHtmlMessage() != null) {
					//append the HMTL part
					message.append(BOUNDARY_LINE);
					message.append("Content-Type: text/html; charset=iso-8859-1\n");
					message.append(rt.getRenderedHtmlMessage());
				}
			
				message.append(TERMINATION_LINE);
				
				// we need to manually contruct the headers
				List<String> headers = new ArrayList<String>();
				//the template may specify a from address
				if (rt.getFrom() != null) {
					headers.add("From: \"" + rt.getFrom() );
				} else {
					headers.add("From: \"" + fromName + "\" <" + fromEmail + ">" );
				}
				// Add a To: header of either the recipient (if only 1), or the sender (if multiple)
				String toName = fromName;
				String toEmail = fromEmail;
				
				if (toAddress.size() == 1) {
					User u = toAddress.get(0);
					toName = u.getDisplayName();
					toEmail = u.getEmail();
				} 
				
				headers.add("To: \"" + toName + "\" <" + toEmail + ">" );
				
				headers.add("Subject: " + rt.getSubject());
				headers.add("Content-Type: multipart/alternative; boundary=\"" + MULTIPART_BOUNDARY + "\"");
				headers.add("Mime-Version: 1.0");
				headers.add("Precedence: bulk");
				
				String body = message.toString();
				log.debug("message body " + body);
				emailService.sendToUsers(toAddress, headers, body);
				
	}
}


private List<User> getUsersEmail(List<String> userIds) {
	//we have a group of referenc
	List<String> ids = new ArrayList<String>(); 
	
	for (int i = 0; i < userIds.size(); i++) {
		String userReference = userIds.get(i);
		String userId = developerHelperService.getUserIdFromRef(userReference);
		ids.add(userId);
	}
	return userDirectoryService.getUsers(ids);
}
	public void processEmailTemplates(List<String> templatePaths) {

		final String ADMIN = "admin";

		Persister persister = new Persister();
		for(String templatePath : templatePaths) {
			
			log.debug("Processing template: " + templatePath);
			
			InputStream in = getClass().getClassLoader().getResourceAsStream(templatePath);

			if(in == null) {
				log.warn("Could not load resource from '" + templatePath + "'. Skipping ...");
				continue;
			}

			EmailTemplate template = null;
			try {
				template = persister.read(EmailTemplate.class,in);
			}
			catch(Exception e) {
				log.warn("Error processing template: '" + templatePath + "', " + e.getClass() + ":" + e.getMessage() + ". Skipping ...");
				continue;
			}

			//check if we have an existing template of this key and locale
			//its possible the template has no locale set
			Locale loc = null;
			if (template.getLocale() != null) {
				loc = new Locale(template.getLocale()); 
			}
			
			EmailTemplate existingTemplate = getEmailTemplate(template.getKey(), loc);
			if(existingTemplate == null) {
				//no existing, save this one
				Session sakaiSession = sessionManager.getCurrentSession();
				sakaiSession.setUserId(ADMIN);
				sakaiSession.setUserEid(ADMIN);
				saveTemplate(template);
				sakaiSession.setUserId(null);
				sakaiSession.setUserId(null);
				log.info("Saved email template: " + template.getKey() + " with locale: " + template.getLocale());
				continue; //skip to next
			} 
		
			//check version, if local one newer than persisted, update it - SAK-17679
			//also update the locale - SAK-20987
			int existingTemplateVersion = existingTemplate.getVersion() != null ? existingTemplate.getVersion().intValue() : 0;
			if(template.getVersion() > existingTemplateVersion) {
				existingTemplate.setSubject(template.getSubject());
				existingTemplate.setMessage(template.getMessage());
				existingTemplate.setHtmlMessage(template.getHtmlMessage());
				existingTemplate.setVersion(template.getVersion());
				existingTemplate.setOwner(template.getOwner());
				existingTemplate.setLocale(template.getLocale());

				Session sakaiSession = sessionManager.getCurrentSession();
				sakaiSession.setUserId(ADMIN);
				sakaiSession.setUserEid(ADMIN);
				updateTemplate(existingTemplate);
				sakaiSession.setUserId(null);
				sakaiSession.setUserId(null);
				log.info("Updated email template: " + template.getKey() + " with locale: " + template.getLocale());
			}
		}
	}


}
