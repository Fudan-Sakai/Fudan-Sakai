/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/emailtemplateservice/tags/emailtemplateservice-0.5.4/api/src/java/org/sakaiproject/emailtemplateservice/model/RenderedTemplate.java $
 * $Id: RenderedTemplate.java 79120 2010-07-07 07:52:30Z david.horwitz@uct.ac.za $
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

package org.sakaiproject.emailtemplateservice.model;

public class RenderedTemplate extends EmailTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6279976323700630617L;
	private String renderedSubject;
	private String renderedMessage;
	private String renderedHtmlMessage;

	public RenderedTemplate(EmailTemplate template) {
		this.setId(template.getId());
		this.setSubject(template.getSubject());
		this.setMessage(template.getMessage());
		this.setKey(template.getKey());
		this.setLocale(template.getLocale());
		this.setHtmlMessage(template.getHtmlMessage());

	}

	public String getRenderedMessage() {
		return renderedMessage;
	}

	public void setRenderedMessage(String renderedMessage) {
		this.renderedMessage = renderedMessage;
	}

	public String getRenderedSubject() {
		return renderedSubject;
	}

	public void setRenderedSubject(String renderedSubject) {
		this.renderedSubject = renderedSubject;
	}

	public String getRenderedHtmlMessage() {
		return renderedHtmlMessage;
	}

	public void setRenderedHtmlMessage(String renderedHtmlMessage) {
		this.renderedHtmlMessage = renderedHtmlMessage;
	}

}
