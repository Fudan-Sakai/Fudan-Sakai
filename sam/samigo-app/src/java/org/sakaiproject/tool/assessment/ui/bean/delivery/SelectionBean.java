/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/bean/delivery/SelectionBean.java $
 * $Id: SelectionBean.java 73974 2010-02-23 00:50:51Z ktsao@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2008 The Sakai Foundation
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

package org.sakaiproject.tool.assessment.ui.bean.delivery;

import java.util.ArrayList;

import org.sakaiproject.tool.assessment.data.dao.grading.ItemGradingData;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AnswerIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemTextIfc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the JSF backing bean for delivery, used for TF and MC questions
 * 
 * $Id: SelectionBean.java 73974 2010-02-23 00:50:51Z ktsao@stanford.edu $
 */
public class SelectionBean {

	private static Log log = LogFactory.getLog(SelectionBean.class);

	private ItemContentsBean parent;

	private ItemGradingData data;

	private boolean response;

	private AnswerIfc answer;

	private String feedback;

	private String responseId;

	public ItemContentsBean getItemContentsBean() {
		return parent;
	}

	public void setItemContentsBean(ItemContentsBean bean) {
		parent = bean;
	}

	public ItemGradingData getItemGradingData() {
		return data;
	}

	public void setItemGradingData(ItemGradingData newdata) {
		data = newdata;
	}

	public boolean getResponse() {
		return response;
	}

	public void setResponse(boolean newresp) {
		// this is called with mcsc and mcmc

		response = newresp;
		if (newresp) {
			ItemTextIfc itemText = (ItemTextIfc) parent.getItemData()
					.getItemTextSet().toArray()[0];
			if (data == null) {
				data = new ItemGradingData();
				data.setPublishedItemId(parent.getItemData().getItemId());
				data.setPublishedItemTextId(itemText.getId());
				ArrayList items = parent.getItemGradingDataArray();
				items.add(data);
				parent.setItemGradingDataArray(items);
			}
			data.setPublishedAnswerId(answer.getId());
		} else if (data != null)
			data.setPublishedAnswerId(null);
	}

	public void setResponseFromCleanRadioButton() {
		response = false;
		data = null;
	}
		
	public AnswerIfc getAnswer() {
		return answer;
	}

	public void setAnswer(AnswerIfc newAnswer) {
		answer = newAnswer;
	}

	public String getFeedback() {
		if (feedback == null)
			return "";
		return feedback;
	}

	public void setFeedback(String newfb) {
		feedback = newfb;
	}

	public String getAnswerId() {
		return answer.getId().toString();
	}
}
