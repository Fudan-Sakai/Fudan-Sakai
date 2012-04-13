/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-archive/sam-handlers/src/java/org/sakaiproject/importer/impl/handlers/SamigoAssessmentHandler.java $
 * $Id: SamigoAssessmentHandler.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008 The Sakai Foundation
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

package org.sakaiproject.importer.impl.handlers;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osid.assessment.AssessmentException;
import org.sakaiproject.importer.api.HandlesImportable;
import org.sakaiproject.importer.api.Importable;
import org.sakaiproject.importer.impl.importables.Assessment;
import org.sakaiproject.importer.impl.importables.AssessmentAnswer;
import org.sakaiproject.importer.impl.importables.AssessmentQuestion;
import org.sakaiproject.tool.assessment.data.dao.assessment.Answer;
import org.sakaiproject.tool.assessment.data.dao.assessment.AnswerFeedback;
import org.sakaiproject.tool.assessment.data.dao.assessment.AssessmentData;
import org.sakaiproject.tool.assessment.data.dao.assessment.ItemText;
import org.sakaiproject.tool.assessment.facade.AssessmentFacade;
import org.sakaiproject.tool.assessment.facade.ItemFacade;
import org.sakaiproject.tool.assessment.facade.SectionFacade;
import org.sakaiproject.tool.assessment.services.ItemService;
import org.sakaiproject.tool.assessment.services.SectionService;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.tool.assessment.data.ifc.assessment.SectionDataIfc;

public class SamigoAssessmentHandler implements HandlesImportable {
	// Samigo identifies each question type with an int
	public static final int TRUE_FALSE = 4;
	public static final int FILL_BLANK = 8;
	public static final int MATCHING = 9;
	
	public static final String QUIZ_TYPE = "62";
	public static final String QUIZ_TEMPLATE = "3";
	
	private AssessmentService as = new AssessmentService();
	private ItemService itemService = new ItemService();

	public boolean canHandleType(String typeName) {
		return "sakai-assessment".equals(typeName);
	}

	public void handle(Importable thing, String siteId) {
		Assessment importAssessment = (Assessment)thing;
		AssessmentFacade assessment = null;
		try {
			assessment = as.createAssessmentWithoutDefaultSection(
					importAssessment.getTitle(), importAssessment.getDescription(), SamigoAssessmentHandler.QUIZ_TYPE, SamigoAssessmentHandler.QUIZ_TEMPLATE);
			AssessmentData data = new AssessmentData(new Long(SamigoAssessmentHandler.QUIZ_TEMPLATE), importAssessment.getTitle(), new Date());
			data.setTypeId(new Long(SamigoAssessmentHandler.QUIZ_TYPE));
			data.setTitle(importAssessment.getTitle());
			data.setDescription(importAssessment.getDescription());
			data.setAssessmentTemplateId(new Long(SamigoAssessmentHandler.QUIZ_TEMPLATE));
			data.setCreatedBy(SessionManager.getCurrentSessionUserId());
			data.setLastModifiedBy(SessionManager.getCurrentSessionUserId());
			data.setLastModifiedDate(new Date());
			// have no idea what the magic number 30 is for, but Samigo used it when I created a question pool in the tool
			data.setStatus(new Integer(1));
			data.setIsTemplate(new Boolean(false));
			data.setCreatedDate(new Date());
			Set questionItems = new HashSet();
			questionItems.addAll(doQuestions(importAssessment.getEssayQuestions()));
			questionItems.addAll(doQuestions(importAssessment.getFillBlankQuestions()));
			questionItems.addAll(doQuestions(importAssessment.getMatchQuestions()));
			questionItems.addAll(doQuestions(importAssessment.getMultiAnswerQuestions()));
			questionItems.addAll(doQuestions(importAssessment.getMultiChoiceQuestions()));
			// Samigo doesn't have native support for ordering questions. Maybe there's a workaround?
			// questionItems.addAll(doQuestions(importPool.getOrderingQuestions()));
			questionItems.addAll(doQuestions(importAssessment.getTrueFalseQuestions()));
			
			Set sectionSet = new HashSet();
			SectionFacade section = new SectionFacade();
			section.setTypeId(new Long(21));
			section.setCreatedBy(SessionManager.getCurrentSessionUserId());
			section.setCreatedDate(new Date());
			section.setLastModifiedBy(SessionManager.getCurrentSessionUserId());
			section.setLastModifiedDate(new Date());
			section.setStatus(new Integer(1));
			section.setSequence(new Integer(1));
			section.setAssessmentId(assessment.getAssessmentId());
			section.setAssessment(assessment);
			as.saveOrUpdateSection(section);
			
			Object[] questionItemsArray = questionItems.toArray();
			Arrays.sort(questionItemsArray, new Comparator() {
			    public int compare(Object o1, Object o2) {
			      Integer i1 = ((ItemFacade)o1).getSequence();
			      Integer i2 = ((ItemFacade)o2).getSequence();
			      return i1.compareTo(i2);
			    }
			  });
			for (int i = 0;i < questionItemsArray.length; i++) {
				ItemFacade item = (ItemFacade)questionItemsArray[i];
				item.setSequence(new Integer(i + 1));
				item.setSection(section);
				section.addItem(itemService.saveItem(item));
			}
			data.setSectionSet(sectionSet);
			assessment.setData(data);
			assessment.setSectionSet(sectionSet);
			as.saveAssessment(assessment);
		} catch (Exception e) {
			// error creating this assessment
			e.printStackTrace();
		} catch (AssessmentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Collection doQuestions(List questions) {
		AssessmentQuestion importableQuestion;
		AssessmentAnswer importableAnswer;
		AssessmentAnswer importableChoice;
		Collection rv = new Vector();
		ItemFacade itemFacade = null;
		String questionTextString = null;
		ItemText text = null;
		HashSet textSet = null;
		Answer answer = null;
		HashSet answerSet = null;
		AnswerFeedback answerFeedback = null;
		HashSet answerFeedbackSet = null;
		int questionCount = 0;
		for(Iterator i = questions.iterator();i.hasNext();) {
			importableQuestion = (AssessmentQuestion)i.next();
			questionCount++;
			Set correctAnswerIDs = importableQuestion.getCorrectAnswerIDs();
			itemFacade = new ItemFacade();
			textSet = new HashSet();
			questionTextString = contextualizeUrls(importableQuestion.getQuestionText());
			if (importableQuestion.getQuestionType() == SamigoPoolHandler.MATCHING) {
				itemFacade.setInstruction(questionTextString);
				Collection answers = importableQuestion.getAnswers().values();
				Collection choices = importableQuestion.getChoices().values();
				int answerIndex = 1;
				for (Iterator j = answers.iterator();j.hasNext();) {
					importableAnswer = (AssessmentAnswer)j.next();
					text = new ItemText();
					text.setSequence(new Long(answerIndex));
					answerIndex++;
					text.setText(contextualizeUrls(importableAnswer.getAnswerText()));
					answerSet = new HashSet();
					int choiceIndex = 1;
					for (Iterator k = choices.iterator();k.hasNext();) {
						importableChoice = (AssessmentAnswer)k.next();
						answer = new Answer();
						answer.setItem(itemFacade.getData());
						answer.setItemText(text);
						answer.setSequence(new Long(choiceIndex));
						choiceIndex++;
						// set label A, B, C, D, etc. on answer based on its sequence number
						answer.setLabel(new Character((char)(64 + choiceIndex)).toString());
						answer.setText(contextualizeUrls(importableChoice.getAnswerText()));
						answer.setIsCorrect(new Boolean(importableAnswer.getChoiceId().equals(importableChoice.getAnswerId())));
						answerSet.add(answer);
					}
					text.setAnswerSet(answerSet);
					text.setItem(itemFacade.getData());
					textSet.add(text);
				}
			} else {
			
				text = new ItemText();
				text.setSequence(new Long(1));
				text.setText(questionTextString);
				
				answerSet = new HashSet();
				answerFeedbackSet = new HashSet();
				Collection answers = importableQuestion.getAnswers().values();
				StringBuilder answerBuffer = new StringBuilder();
				for (Iterator j = answers.iterator();j.hasNext();) {
					importableAnswer = (AssessmentAnswer)j.next();
					answerBuffer.append(importableAnswer.getAnswerText());
					if (j.hasNext()) answerBuffer.append("|");
					String answerId = importableAnswer.getAnswerId();
					answer = new Answer();
					answer.setItem(itemFacade.getData());
					answer.setItemText(text);
					answer.setSequence(new Long(importableAnswer.getPosition()));
					// set label A, B, C, D, etc. on answer based on its sequence number
					answer.setLabel(new Character((char)(64 + importableAnswer.getPosition())).toString());
					
					if (importableQuestion.getQuestionType() == SamigoPoolHandler.TRUE_FALSE) {
						// Samigo only understands True/False answers in lower case
						answer.setText(importableAnswer.getAnswerText().toLowerCase());
					} else if (importableQuestion.getQuestionType() == SamigoPoolHandler.FILL_BLANK) {
						if (j.hasNext()) continue;
						answer.setText(answerBuffer.toString());
						Pattern pattern = Pattern.compile("_+|<<.*>>");
						Matcher matcher = pattern.matcher(questionTextString);
						if (matcher.find()) questionTextString = questionTextString.replaceFirst(matcher.group(),"{}");
						text.setText(questionTextString);
						answer.setSequence(new Long(1));
					} else {
						answer.setText(contextualizeUrls(importableAnswer.getAnswerText()));
					}
					
					answer.setIsCorrect(new Boolean(correctAnswerIDs.contains(answerId)));
					answerSet.add(answer);
				}
				text.setAnswerSet(answerSet);
				text.setItem(itemFacade.getData());
				textSet.add(text);
			}
			itemFacade.setItemTextSet(textSet);
			itemFacade.setCorrectItemFeedback(importableQuestion.getFeedbackWhenCorrect());
			itemFacade.setInCorrectItemFeedback(importableQuestion.getFeedbackWhenIncorrect());
			itemFacade.setTypeId(new Long(importableQuestion.getQuestionType()));
			itemFacade.setScore(importableQuestion.getPointValue());
			itemFacade.setSequence(importableQuestion.getPosition());
			// status is 0=inactive or 1=active
			itemFacade.setStatus(new Integer(1));
			itemFacade.setHasRationale(Boolean.FALSE);
			itemFacade.setCreatedBy(SessionManager.getCurrentSessionUserId());
			itemFacade.setCreatedDate(new java.util.Date());
			itemFacade.setLastModifiedBy(SessionManager.getCurrentSessionUserId());
			itemFacade.setLastModifiedDate(new java.util.Date());
			// itemService.saveItem(itemFacade);
			rv.add(itemFacade);
			
		}
		return rv;
		
	}
	
	protected String contextualizeUrls(String text) {
		if (text == null) return null;
		// this regular expression is specifically looking for image urls
		// but only urls that are not absolute (i.e., do not start "http")
		String anyRelativeUrl = "src=\"(?!http)/?";
		return text.replaceAll(anyRelativeUrl, "src=\"/access/content/group/" 
				+ ToolManager.getCurrentPlacement().getContext() + "/TQimages/"); 	        } 

}
