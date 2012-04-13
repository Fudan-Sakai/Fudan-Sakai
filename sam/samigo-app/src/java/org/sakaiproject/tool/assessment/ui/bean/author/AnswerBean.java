/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/bean/author/AnswerBean.java $
 * $Id: AnswerBean.java 92611 2011-05-04 22:37:29Z ktsao@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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


package org.sakaiproject.tool.assessment.ui.bean.author;

import java.io.Serializable;
import org.sakaiproject.util.ResourceLoader;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;
import org.sakaiproject.util.ResourceLoader;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.print.attribute.standard.Severity;

public class AnswerBean implements Serializable{

  private static final long serialVersionUID = 7526471155622776147L;

  private String text;
  private Long sequence;
  private String label;
  private String feedback;
  private Boolean isCorrect;
  private String partialCredit = "0";  //to incorporate partial credit
  private static ResourceLoader rb = new ResourceLoader("org.sakaiproject.tool.assessment.bundle.AuthorMessages");

  public static final String choiceLabels = rb.getString("choice_labels"); 
  public AnswerBean() {}

  public AnswerBean(String ptext, Long pseq, String plabel, String pfdbk, Boolean pcorr, String pgrade , Float pscore) {
    this.text = ptext;
    this.sequence = pseq;
    this.label = plabel;
    this.feedback= pfdbk;
    this.isCorrect = pcorr;

  }

  public String getText() {
	  if (text == null) {
		  return text;
	  }
	  String status;
	  if (text.equalsIgnoreCase("Agree"))
		  status = "st_agree";
	  else if (text.equalsIgnoreCase("Disagree"))
		  status = "st_disagree";
	  else if (text.equalsIgnoreCase("Undecided"))
		  status = "st_undecided";
	  else if (text.equalsIgnoreCase("Below Average"))
		  status = "st_below_avg";
	  else if (text.equalsIgnoreCase("Average"))
		  status = "st_avg";
	  else if (text.equalsIgnoreCase("Above Average"))
		  status = "st_above_avg";
	  else if (text.equalsIgnoreCase("Strongly Disagree"))
		  status = "st_strong_disagree";
	  else if (text.equalsIgnoreCase("Strongly agree"))
		  status = "st_strong_agree";
	  else if (text.equalsIgnoreCase("Unacceptable"))
		  status = "st_unacceptable";
	  else if (text.equalsIgnoreCase("Excellent"))
		  status = "st_excellent";
	  else status = text;
	  String str = rb.getString(status);
	  if (str.indexOf("missing key")!=-1)
		  return text;
	  else
		  return str;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Long getSequence() {
    return sequence;
  }

  public void setSequence(Long sequence) {
    this.sequence = sequence;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback= feedback;
  }

  public Boolean getIsCorrect() {
    return isCorrect;
  }

  public void setIsCorrect(Boolean isCorrect) {
    this.isCorrect = isCorrect;
  }

  public static String[] getChoiceLabels() {
	  String[] lables = choiceLabels.split(":");
	  return lables;
  }
  
  // additional constroctor for partial credit
	public AnswerBean(String ptext, Long pseq, String plabel, String pfdbk,
			Boolean pcorr, String pgrade, Float pscore, String pCredit) {
		this.text = ptext;
		this.sequence = pseq;
		this.label = plabel;
		this.feedback = pfdbk;
		this.isCorrect = pcorr;
		this.partialCredit = pCredit;
	}

	// --mustansar for partial credit
	public String getPartialCredit() {
		return partialCredit;
	}

	public void setPartialCredit(String pCredit) {
		this.partialCredit = pCredit;
	}
	
	public void validatePartialCredit(FacesContext context,  UIComponent toValidate,Object value){
		Integer pCredit = null;
		boolean isValid = true;
		if ("0.0".equals(value.toString())) {
			pCredit = 0;
		}
		else {
			try {
				pCredit = Integer.parseInt(value.toString());
			}
			catch (NumberFormatException e) {
				isValid = false;
			}
		}
		
		if(isValid && (pCredit==null || pCredit<0 || pCredit>99 )){
			isValid = false;
		}
		
		if (!isValid) {
			((UIInput)toValidate).setValid(false);
			FacesMessage message=new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			String summary=ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages","partial_credit_limit_summary");
			String detail =ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.AuthorMessages","partial_credit_limit_detail"); 
			message.setSummary(summary) ;
			message.setDetail(detail);   
			context.addMessage(toValidate.getClientId(context), message);
		}
	}
}
