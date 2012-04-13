/**
 * Copyright (c) 2008-2010 The Sakai Foundation
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

package org.sakaiproject.profile2.tool.pages;


import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.sakaiproject.profile2.exception.ProfilePreferencesNotDefinedException;
import org.sakaiproject.profile2.model.ProfilePreferences;
import org.sakaiproject.profile2.tool.components.IconWithClueTip;
import org.sakaiproject.profile2.tool.pages.panels.TwitterPrefsPane;
import org.sakaiproject.profile2.util.ProfileConstants;

public class MyPreferences extends BasePage{

	private static final Logger log = Logger.getLogger(MyPreferences.class);
	private transient ProfilePreferences profilePreferences;

	public MyPreferences() {
		
		log.debug("MyPreferences()");
		
		disableLink(preferencesLink);
		
		//get current user
		final String userUuid = sakaiProxy.getCurrentUserId();

		//get the prefs record for this user from the database, or a default if none exists yet
		profilePreferences = preferencesLogic.getPreferencesRecordForUser(userUuid, false);
		
		//if null, throw exception
		if(profilePreferences == null) {
			throw new ProfilePreferencesNotDefinedException("Couldn't create default preferences record for " + userUuid);
		}
		
		//get email address for this user
		String emailAddress = sakaiProxy.getUserEmail(userUuid);
		//if no email, set a message into it fo display
		if(emailAddress == null || emailAddress.length() == 0) {
			emailAddress = new ResourceModel("preferences.email.none").getObject().toString();
		}
		
				
		Label heading = new Label("heading", new ResourceModel("heading.preferences"));
		add(heading);
		
		//feedback for form submit action
		final Label formFeedback = new Label("formFeedback");
		formFeedback.setOutputMarkupPlaceholderTag(true);
		final String formFeedbackId = formFeedback.getMarkupId();
		add(formFeedback);
		
				
		//create model
		CompoundPropertyModel<ProfilePreferences> preferencesModel = new CompoundPropertyModel<ProfilePreferences>(profilePreferences);
		
		//setup form		
		Form<ProfilePreferences> form = new Form<ProfilePreferences>("form", preferencesModel);
		form.setOutputMarkupId(true);
		
	
		//EMAIL SECTION
		
		//email settings
		form.add(new Label("emailSectionHeading", new ResourceModel("heading.section.email")));
		form.add(new Label("emailSectionText", new StringResourceModel("preferences.email.message", null, new Object[] { emailAddress })).setEscapeModelStrings(false));
	
		//on/off labels
		form.add(new Label("prefOn", new ResourceModel("preference.option.on")));
		form.add(new Label("prefOff", new ResourceModel("preference.option.off")));

		//request emails
		final RadioGroup<Boolean> emailRequests = new RadioGroup<Boolean>("requestEmailEnabled", new PropertyModel<Boolean>(preferencesModel, "requestEmailEnabled"));
		emailRequests.add(new Radio<Boolean>("requestsOn", new Model<Boolean>(new Boolean(true))));
		emailRequests.add(new Radio<Boolean>("requestsOff", new Model<Boolean>(new Boolean(false))));
		emailRequests.add(new Label("requestsLabel", new ResourceModel("preferences.email.requests")));
		form.add(emailRequests);
		
		//updater
		emailRequests.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
            	target.appendJavascript("$('#" + formFeedbackId + "').fadeOut();");
            }
        });
		
		//confirm emails
		final RadioGroup<Boolean> emailConfirms = new RadioGroup<Boolean>("confirmEmailEnabled", new PropertyModel<Boolean>(preferencesModel, "confirmEmailEnabled"));
		emailConfirms.add(new Radio<Boolean>("confirmsOn", new Model<Boolean>(new Boolean(true))));
		emailConfirms.add(new Radio<Boolean>("confirmsOff", new Model<Boolean>(new Boolean(false))));
		emailConfirms.add(new Label("confirmsLabel", new ResourceModel("preferences.email.confirms")));
		form.add(emailConfirms);
		
		//updater
		emailConfirms.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
            	target.appendJavascript("$('#" + formFeedbackId + "').fadeOut();");
            }
        });
		
		//new message emails
		final RadioGroup<Boolean> emailNewMessage = new RadioGroup<Boolean>("messageNewEmailEnabled", new PropertyModel<Boolean>(preferencesModel, "messageNewEmailEnabled"));
		emailNewMessage.add(new Radio<Boolean>("messageNewOn", new Model<Boolean>(new Boolean(true))));
		emailNewMessage.add(new Radio<Boolean>("messageNewOff", new Model<Boolean>(new Boolean(false))));
		emailNewMessage.add(new Label("messageNewLabel", new ResourceModel("preferences.email.message.new")));
		form.add(emailNewMessage);
		
		//updater
		emailNewMessage.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
            	target.appendJavascript("$('#" + formFeedbackId + "').fadeOut();");
            }
        });
		
		//message reply emails
		final RadioGroup<Boolean> emailReplyMessage = new RadioGroup<Boolean>("messageReplyEmailEnabled", new PropertyModel<Boolean>(preferencesModel, "messageReplyEmailEnabled"));
		emailReplyMessage.add(new Radio<Boolean>("messageReplyOn", new Model<Boolean>(new Boolean(true))));
		emailReplyMessage.add(new Radio<Boolean>("messageReplyOff", new Model<Boolean>(new Boolean(false))));
		emailReplyMessage.add(new Label("messageReplyLabel", new ResourceModel("preferences.email.message.reply")));
		form.add(emailReplyMessage);
		
		//updater
		emailReplyMessage.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
            	target.appendJavascript("$('#" + formFeedbackId + "').fadeOut();");
            }
        });
        
		
		// TWITTER SECTION

		//headings
		WebMarkupContainer twitterSectionHeadingContainer = new WebMarkupContainer("twitterSectionHeadingContainer");
		twitterSectionHeadingContainer.add(new Label("twitterSectionHeading", new ResourceModel("heading.section.twitter")));
		twitterSectionHeadingContainer.add(new Label("twitterSectionText", new ResourceModel("preferences.twitter.message")));
		form.add(twitterSectionHeadingContainer);
		
		//panel
		if(sakaiProxy.isTwitterIntegrationEnabledGlobally()) {
			form.add(new AjaxLazyLoadPanel("twitterPanel"){
				private static final long serialVersionUID = 1L;
	
				@Override
				public Component getLazyLoadComponent(String markupId) {
					return new TwitterPrefsPane(markupId, userUuid);
				}
			});
		} else {
			form.add(new EmptyPanel("twitterPanel"));
			twitterSectionHeadingContainer.setVisible(false);
		}
		
		
		// OFFICIAL IMAGE SECTION
		WebMarkupContainer is = new WebMarkupContainer("imageSettingsContainer");
		is.setOutputMarkupId(true);
				
		//official photo settings
		is.add(new Label("imageSettingsHeading", new ResourceModel("heading.section.image")));
		is.add(new Label("imageSettingsText", new ResourceModel("preferences.image.message")));

		//checkbox
		WebMarkupContainer officialImageContainer = new WebMarkupContainer("officialImageContainer");
		officialImageContainer.add(new Label("officialImageLabel", new ResourceModel("preferences.image.official")));
		CheckBox officialImage = new CheckBox("officialImage", new PropertyModel<Boolean>(preferencesModel, "useOfficialImage"));
		officialImageContainer.add(officialImage);

		//updater
		officialImage.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
            	target.appendJavascript("$('#" + formFeedbackId + "').fadeOut();");
            }
        });
		is.add(officialImageContainer);
		
		//if using official images but alternate choice isn't allowed
		if(!sakaiProxy.isUsingOfficialImageButAlternateSelectionEnabled()) {
			profilePreferences.setUseOfficialImage(false); //set the model false to clear data as well (doesnt really need to do this but we do it to keep things in sync)
			is.setVisible(false);
		}
		
		form.add(is);
		
		
		
		// WIDGET SECTION
		WebMarkupContainer ws = new WebMarkupContainer("widgetSettingsContainer");
		ws.setOutputMarkupId(true);
		
		//widget settings
		ws.add(new Label("widgetSettingsHeading", new ResourceModel("heading.section.widget")));
		ws.add(new Label("widgetSettingsText", new ResourceModel("preferences.widget.message")));

		//kudos
		WebMarkupContainer kudosContainer = new WebMarkupContainer("kudosContainer");
		kudosContainer.add(new Label("kudosLabel", new ResourceModel("preferences.widget.kudos")));
		CheckBox kudosSetting = new CheckBox("kudosSetting", new PropertyModel<Boolean>(preferencesModel, "showKudos"));
		kudosContainer.add(kudosSetting);
		//tooltip
		kudosContainer.add(new IconWithClueTip("kudosToolTip", ProfileConstants.INFO_IMAGE, new ResourceModel("preferences.widget.kudos.tooltip")));
		

		//updater
		kudosSetting.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
            	target.appendJavascript("$('#" + formFeedbackId + "').fadeOut();");
            }
        });
		ws.add(kudosContainer);
		
		//gallery feed
		WebMarkupContainer galleryFeedContainer = new WebMarkupContainer("galleryFeedContainer");
		galleryFeedContainer.add(new Label("galleryFeedLabel", new ResourceModel("preferences.widget.gallery")));
		CheckBox galleryFeedSetting = new CheckBox("galleryFeedSetting", new PropertyModel<Boolean>(preferencesModel, "showGalleryFeed"));
		galleryFeedContainer.add(galleryFeedSetting);
		//tooltip
		galleryFeedContainer.add(new IconWithClueTip("galleryFeedToolTip", ProfileConstants.INFO_IMAGE, new ResourceModel("preferences.widget.gallery.tooltip")));
		
		//updater
		galleryFeedSetting.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			protected void onUpdate(AjaxRequestTarget target) {
            	target.appendJavascript("$('#" + formFeedbackId + "').fadeOut();");
            }
        });
		ws.add(galleryFeedContainer);
		galleryFeedContainer.setVisible(sakaiProxy.isProfileGalleryEnabledGlobally());
		
		form.add(ws);
		
		//submit button
		IndicatingAjaxButton submitButton = new IndicatingAjaxButton("submit", form) {
			private static final long serialVersionUID = 1L;
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				
				//get the backing model
				ProfilePreferences profilePreferences = (ProfilePreferences) form.getModelObject();
				
				formFeedback.setDefaultModel(new ResourceModel("success.preferences.save.ok"));
				formFeedback.add(new AttributeModifier("class", true, new Model<String>("success")));
				
				//save
				if(preferencesLogic.savePreferencesRecord(profilePreferences)) {
					formFeedback.setDefaultModel(new ResourceModel("success.preferences.save.ok"));
					formFeedback.add(new AttributeModifier("class", true, new Model<String>("success")));
					
					//post update event
					sakaiProxy.postEvent(ProfileConstants.EVENT_PREFERENCES_UPDATE, "/profile/"+userUuid, true);
					
				} else {
					formFeedback.setDefaultModel(new ResourceModel("error.preferences.save.failed"));
					formFeedback.add(new AttributeModifier("class", true, new Model<String>("alertMessage")));	
				}
				
				//resize iframe
				target.appendJavascript("setMainFrameHeight(window.name);");
				
				target.addComponent(formFeedback);
            }
			
			
		};
		submitButton.setModel(new ResourceModel("button.save.settings"));
		submitButton.setDefaultFormProcessing(false);
		form.add(submitButton);
		
        add(form);
		
	}
	
	
	
	
}



