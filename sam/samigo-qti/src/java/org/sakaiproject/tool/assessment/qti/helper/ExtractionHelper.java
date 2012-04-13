/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/component/src/java/org/sakaiproject/tool/assessment/qti/helper/ExtractionHelper.java $
 * $Id: ExtractionHelper.java 9274 2006-05-10 22:50:48Z daisyf@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2007, 2008, 2009 The Sakai Foundation
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



package org.sakaiproject.tool.assessment.qti.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.activation.MimetypesFileTypeMap;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.tool.assessment.data.dao.assessment.Answer;
import org.sakaiproject.tool.assessment.data.dao.assessment.AnswerFeedback;
import org.sakaiproject.tool.assessment.data.dao.assessment.AssessmentAccessControl;
import org.sakaiproject.tool.assessment.data.dao.assessment.AssessmentFeedback;
import org.sakaiproject.tool.assessment.data.dao.assessment.EvaluationModel;
import org.sakaiproject.tool.assessment.data.dao.assessment.ItemText;
import org.sakaiproject.tool.assessment.data.dao.assessment.SecuredIPAddress;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AnswerFeedbackIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentAccessControlIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentBaseIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.SectionAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.shared.TypeIfc;
import org.sakaiproject.tool.assessment.facade.AssessmentFacade;
import org.sakaiproject.tool.assessment.facade.ItemFacade;
import org.sakaiproject.tool.assessment.facade.QuestionPoolFacade;
import org.sakaiproject.tool.assessment.facade.SectionFacade;
import org.sakaiproject.tool.assessment.qti.asi.ASIBaseClass;
import org.sakaiproject.tool.assessment.qti.asi.Assessment;
import org.sakaiproject.tool.assessment.qti.asi.Item;
import org.sakaiproject.tool.assessment.qti.asi.Section;
import org.sakaiproject.tool.assessment.qti.constants.AuthoringConstantStrings;
import org.sakaiproject.tool.assessment.qti.constants.QTIVersion;
import org.sakaiproject.tool.assessment.qti.exception.Iso8601FormatException;
import org.sakaiproject.tool.assessment.qti.exception.RespondusMatchingException;
import org.sakaiproject.tool.assessment.qti.helper.item.ItemTypeExtractionStrategy;
import org.sakaiproject.tool.assessment.qti.util.Iso8601DateFormat;
import org.sakaiproject.tool.assessment.qti.util.Iso8601TimeInterval;
import org.sakaiproject.tool.assessment.qti.util.XmlMapper;
import org.sakaiproject.tool.assessment.qti.util.XmlUtil;
import org.sakaiproject.tool.assessment.services.assessment.AssessmentService;
import org.sakaiproject.util.ResourceLoader;
import org.sakaiproject.tool.assessment.util.TextFormat;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <p>Has helper methods for data extraction (import) from QTI</p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2005 Sakai</p>
 * @author Ed Smiley esmiley@stanford.edu
 * @version $Id: ExtractionHelper.java 9274 2006-05-10 22:50:48Z daisyf@stanford.edu $
 */

public class ExtractionHelper
{
  private static final String QTI_VERSION_1_2_PATH = "v1p2";
  private static final String QTI_VERSION_2_0_PATH = "v2p0";
  private static final String TRANSFORM_PATH =
      "xml/xsl/dataTransform/import";
  private static final String TRANSFORM_PATH_RESPONDUS =
      "xml/xsl/dataTransform/import/respondus";

  private static final String ASSESSMENT_TRANSFORM =
      "extractAssessment.xsl";
  private static final String SECTION_TRANSFORM = "extractSection.xsl";
  private static final String ITEM_TRANSFORM = "extractItem.xsl";
  public static final String REMOVE_NAMESPACE_TRANSFORM = "removeDefaultNamespaceFromQTI.xsl";
  private static Log log = LogFactory.getLog(ExtractionHelper.class);

  private int qtiVersion = QTIVersion.VERSION_1_2;
  private String overridePath = null; // override defaults and settings
  private String FIB_BLANK_INDICATOR = " {} ";
  
  private String unzipLocation;

  // versioning title string that it will look for/use, followed by a number
  private static final String VERSION_START = "  - ";

  /**
   * @deprecated
   */
  public ExtractionHelper()
  {
    this.setQtiVersion(QTIVersion.VERSION_1_2);

  }

  /**
   * Get ExtractionHelper for QTIVersion.VERSION_1_2
   * or QTIVersion.VERSION_2_0
   * @param qtiVersion
   */
  public ExtractionHelper(int qtiVersion)
  {
    this.setQtiVersion(qtiVersion);
  }

  /**
   * Path to XSL transform code.
   * @return context-relative path to XSL transform code.
   */
  public String getTransformPath()
  {
	  return getTransformPath(false);
  }

  public String getTransformPath(boolean isRespondus)
  {
	  // first check to see if normal computed path has been overridden
	  if (overridePath != null)
	  {
		  return overridePath;
	  }
	  if (isRespondus) {
		  return TRANSFORM_PATH_RESPONDUS;
	  }
	  else {
		  return TRANSFORM_PATH + "/" + getQtiPath();
	  }
  }

  private String getQtiPath()
  {
    return qtiVersion == QTIVersion.VERSION_1_2 ? QTI_VERSION_1_2_PATH :
        QTI_VERSION_2_0_PATH;
  }

  /**
   * Get QTI version flag.
   * Either QTIVersion.VERSION_1_2 or QTIVersion.VERSION_2_0;
   * @return QTI version flag
   */
  public int getQtiVersion()
  {
    return qtiVersion;
  }

  /**
   * Set QTI version flag.
   * Either QTIVersion.VERSION_1_2 or QTIVersion.VERSION_2_0;
   * @param qtiVersion
   */
  public void setQtiVersion(int qtiVersion)
  {
    if (!QTIVersion.isValid(qtiVersion))
    {
      throw new IllegalArgumentException("NOT Legal Qti Version.");
    }
    this.qtiVersion = qtiVersion;
  }

  /**
   * Get an XML document for the transform
   * @param template
   * @return
   */
  public Document getTransformDocument(String template, boolean isRespondus)
  {
    //Document document = null;

    if (!isOKtransform(template))
    {
      throw new IllegalArgumentException("NOT valid template.");
    }
    String templateContextPath = this.getTransformPath(isRespondus) + "/" + template;
    return XmlUtil.readDocument(templateContextPath);
  }
  
  public Document getTransformDocument(String template)
  {
	  return getTransformDocument(template, false);
  }

  /**
   * Get map of data to set from assessment XML
   * @param assessmentXml
   * @return a Map
   */
  public Map mapAssessment(Assessment assessmentXml)
  {
	  return mapAssessment(assessmentXml, false);
  }
  
  public Map mapAssessment(Assessment assessmentXml, boolean isRespondus)
  {
    log.debug("inside: mapAssessment");
    return map(ASSESSMENT_TRANSFORM, assessmentXml, isRespondus);
  }

  /**
   * Get map of data to set from section XML
   * @param sectionXml
   * @return a Map
   */
  public Map mapSection(Section sectionXml, boolean isRespondus)
  {
    return map(SECTION_TRANSFORM, sectionXml, isRespondus);
  }
  
  public Map mapSection(Section sectionXml)
  {
    return mapSection(sectionXml, false);
  }

  /**
   * Get map of data to set from item XML
   * @param itemXml
   * @return a Map
   */
  public Map mapItem(Item itemXml)
  {
    return mapItem(itemXml, false);
  }
  
  public Map mapItem(Item itemXml, boolean isRespondus)
  {
    return map(ITEM_TRANSFORM, itemXml, isRespondus);
  }

  /**
   * Helper method
       * @param transformType ASSESSMENT_TRANSFORM, SECTION_TRANSFORM, ITEM_TRANSFORM
   * @param asi ASIBaseClass: Assessment, Section, or Item XML
   * @return
   */
  private Map map(String transformType, ASIBaseClass asi, boolean isRespondus)
  {
    if (!isOKasi(asi))
    {
      throw new IllegalArgumentException("Incorrect ASI subclass.");
    }
    if (!isOKtransform(transformType))
    {
      throw new IllegalArgumentException("Incorrect transform: " +
                                         transformType + ".");
    }
    Map map = null;
    try
    {
      Document transform = getTransformDocument(transformType, isRespondus);
      Document xml = asi.getDocument();
      Document model = XmlUtil.transformDocument(xml, transform);
      DOMBuilder in = new DOMBuilder(); 
      org.jdom.Document jdomDoc = in.build(model); 
      XMLOutputter  printer = new XMLOutputter();
      printer.output(jdomDoc, System.out);

      
      map = XmlMapper.map(model);
    }
    catch (IOException ex)
    {
      log.error(ex);
      ex.printStackTrace(System.out);
    }
    catch (SAXException ex)
    {
      log.error(ex);
      ex.printStackTrace(System.out);
    }
    catch (ParserConfigurationException ex)
    {
      log.error(ex);
      ex.printStackTrace(System.out);
    }
    return map;

  }
  
  private Map map(String transformType, ASIBaseClass asi)
  {
	  return map(transformType, asi, false);
  }

  /**
   * Look up a List of Section XML from Assessment Xml
   * @return a List of Section XML objects
   */
  public List getSectionXmlList(Assessment assessmentXml)
  {
    List nodeList = assessmentXml.selectNodes("//section");
    List sectionXmlList = new ArrayList();

    // now convert our list of Nodes to a list of section xml
    for (int i = 0; i < nodeList.size(); i++)
    {
      try
      {
        Node node = (Node) nodeList.get(i);
        // create a document for a section xml object
        Document sectionDoc = XmlUtil.createDocument();
        // Make a copy for inserting into the new document
        Node importNode = sectionDoc.importNode(node, true);
        // Insert the copy into sectionDoc
        sectionDoc.appendChild(importNode);
        Section sectionXml = new Section(sectionDoc,
               this.getQtiVersion());
        // add the new section xml object to the list
        sectionXmlList.add(sectionXml);
      }
      catch (DOMException ex)
      {
        log.error(ex);
        ex.printStackTrace(System.out);
      }
    }
    return sectionXmlList;
  }

  /**
   * Look up a List of Item XML from Section Xml
   * @param Section sectionXml
   * @return a List of Item XML objects
   */
  public List getItemXmlList(Section sectionXml)
  {
    String itemElementName =
        qtiVersion == QTIVersion.VERSION_1_2 ? "//item" : "//assessmentItem";

    // now convert our list of Nodes to a list of section xml
    List nodeList = sectionXml.selectNodes(itemElementName);
    List itemXmlList = new ArrayList();
    for (int i = 0; i < nodeList.size(); i++)
    {
      try
      {
        Node node = (Node) nodeList.get(i);
        // create a document for a item xml object
        Document itemDoc = XmlUtil.createDocument();
        // Make a copy for inserting into the new document
        Node importNode = itemDoc.importNode(node, true);
        // Insert the copy into itemDoc
        itemDoc.appendChild(importNode);
        Item itemXml = new Item(itemDoc,
               this.getQtiVersion());
        // add the new section xml object to the list
        itemXmlList.add(itemXml);
      }
      catch (DOMException ex)
      {
        log.error(ex);
        ex.printStackTrace(System.out);
      }
    }
    return itemXmlList;
  }

  /**
   * Used internally.
   * @param transform
   * @return true if OK
   */
  private boolean isOKtransform(String transform)
  {
    return (ASSESSMENT_TRANSFORM.equals(transform)) ||
            SECTION_TRANSFORM.equals(transform) ||
            ITEM_TRANSFORM.equals(transform) ||
            REMOVE_NAMESPACE_TRANSFORM.equals(transform) ? true : false;
  }

  /**
   * Used internally.
   * @param asi
   * @return true if OK
   */
  private boolean isOKasi(ASIBaseClass asi)
  {
    return (asi instanceof Assessment ||
            asi instanceof Section ||
            asi instanceof Item) ? true : false;
  }

//  /**
//   * Create assessment from the extracted properties.
//   * @param assessmentMap the extracted properties
//   * @return an assessment, which has been persisted
//   */
//  public AssessmentFacade createAssessment(Map assessmentMap)
//  {
//    String description = (String) assessmentMap.get("description");
//    String title = (String) assessmentMap.get("title");
//    AssessmentService assessmentService = new AssessmentService();
//    AssessmentFacade assessment = assessmentService.createAssessment(
//        title, description, null, null);
//    return assessment;
//  }

  /**
   * Update assessment from the extracted properties.
   * Note: you need to do a save when you are done.
   * @param assessment the assessment, which will  be persisted
   * @param assessmentMap the extracted properties
   */
  public void updateAssessment(AssessmentFacade assessment,
                               Map assessmentMap)
  {
    log.debug("ASSESSMENT updating metadata information");
    // set meta data
    List metalist = (List) assessmentMap.get("metadata");
    MetaDataList metadataList = new MetaDataList(metalist);
    metadataList.setDefaults(assessment);
    metadataList.addTo(assessment);

    // restricted IP address
    log.debug("ASSESSMENT updating access control, evaluation model, feedback");

    // access control
    String duration = (String) assessmentMap.get("duration");
    log.debug("duration: " + duration);

    makeAccessControl(assessment, duration);
    String submissionMsg = metadataList.getSubmissionMessage();
    updateSubmissionMessage(assessment,submissionMsg);

    // evaluation model control
    makeEvaluationModel(assessment);

    // assessment feedback control
    makeAssessmentFeedback(assessment);
    
  }

  /**
   * Put feedback settings into assessment (bi-directional)
   * @param assessment
   */
  private void makeAssessmentFeedback(AssessmentFacade assessment)
  {
    AssessmentFeedback feedback =
        (AssessmentFeedback) assessment.getAssessmentFeedback();
    if (feedback == null){
      feedback = new AssessmentFeedback();
      // Need to fix AssessmentFeedback so it can take AssessmentFacade later
      feedback.setAssessmentBase(assessment.getData());
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_QUESTION")))
    {
      feedback.setShowQuestionText(Boolean.TRUE);
    }
    else
    {
      feedback.setShowQuestionText(Boolean.FALSE);
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_RESPONSE")))
    {
      feedback.setShowStudentResponse(Boolean.TRUE);
    }
    else
    {
      feedback.setShowStudentResponse(Boolean.FALSE);
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_CORRECT_RESPONSE")))
    {
      feedback.setShowCorrectResponse(Boolean.TRUE);
    }
    else
    {
      feedback.setShowCorrectResponse(Boolean.FALSE);
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_STUDENT_SCORE")))
    {
      feedback.setShowStudentScore(Boolean.TRUE);
    }
    else
    {
      feedback.setShowStudentScore(Boolean.FALSE);
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_STUDENT_QUESTIONSCORE")))
    {
      feedback.setShowStudentQuestionScore(Boolean.TRUE);
    }
    else
    {
      feedback.setShowStudentQuestionScore(Boolean.FALSE);
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_ITEM_LEVEL")))
    {
      feedback.setShowQuestionLevelFeedback(Boolean.TRUE);
    }
    else
    {
      feedback.setShowQuestionLevelFeedback(Boolean.FALSE);
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_SELECTION_LEVEL")))
    {
      feedback.setShowSelectionLevelFeedback(Boolean.TRUE);
    }
    else
    {
      feedback.setShowSelectionLevelFeedback(Boolean.FALSE);
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_GRADER_COMMENT")))
    {
      feedback.setShowGraderComments(Boolean.TRUE);
    }
    else
    {
      feedback.setShowGraderComments(Boolean.FALSE);
    }

    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_SHOW_STATS")))
    {
      feedback.setShowStatistics(Boolean.TRUE);
    }
    else
    {
      feedback.setShowStatistics(Boolean.FALSE);
    }

    if (
        this.notNullOrEmpty(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_DELIVERY_DATE") )  ||
        "DATED".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_DELIVERY")))
    {
      feedback.setFeedbackDelivery(feedback.FEEDBACK_BY_DATE);
    }
    else if ("IMMEDIATE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_DELIVERY")))
    {
      feedback.setFeedbackDelivery(feedback.IMMEDIATE_FEEDBACK);
    }
    else if ("ON_SUBMISSION".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
    "FEEDBACK_DELIVERY")))
    {
      feedback.setFeedbackDelivery(feedback.FEEDBACK_ON_SUBMISSION);
    }
    else
    {
      feedback.setFeedbackDelivery(feedback.NO_FEEDBACK);
    }
    
    if ("SELECT_COMPONENTS".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel("FEEDBACK_COMPONENT_OPTION")))
    {
    	feedback.setFeedbackComponentOption(feedback.SELECT_COMPONENTS);
    }
    else {
    	feedback.setFeedbackComponentOption(feedback.SHOW_TOTALSCORE_ONLY);
    }

 if (
        "QUESTION".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_AUTHORING")))
    {
      feedback.setFeedbackAuthoring(feedback.QUESTIONLEVEL_FEEDBACK);
    }
    else if ("SECTION".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_AUTHORING")))
    {
      feedback.setFeedbackAuthoring(feedback.SECTIONLEVEL_FEEDBACK);
    }
    else
    {
      feedback.setFeedbackAuthoring(feedback.BOTH_FEEDBACK);
    }

    assessment.setAssessmentFeedback(feedback);
  }

  /**
   * Put evaluation settings into assessment (bi-directional)
   * @param assessment
   */
  private void makeEvaluationModel(AssessmentFacade assessment)
  {
    EvaluationModel evaluationModel =
        (EvaluationModel) assessment.getEvaluationModel();
    if (evaluationModel == null){
      evaluationModel = new EvaluationModel();
      // Need to fix EvaluationModel so it can take AssessmentFacade later
      evaluationModel.setAssessmentBase(assessment.getData());
    }

    // anonymous
    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "ANONYMOUS_GRADING")))
    {
      evaluationModel.setAnonymousGrading(EvaluationModel.ANONYMOUS_GRADING);
    }
    else
    {
      evaluationModel.setAnonymousGrading(EvaluationModel.NON_ANONYMOUS_GRADING);
    }

    // gradebook options, don't know how this is supposed to work, leave alone for now
    if ("DEFAULT".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "GRADEBOOK_OPTIONS")))
    {
      evaluationModel.setToGradeBook(EvaluationModel.TO_DEFAULT_GRADEBOOK.toString());
    }
    else if ("SELECTED".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "GRADEBOOK_OPTIONS")))
    {
      evaluationModel.setToGradeBook(EvaluationModel.NOT_TO_GRADEBOOK.toString());	// this is for backward compatibility. 
      // we've always used 2 for 'None' option. Old exported XML will have 'SELECTED' for assessemnts that are set not to send to gradebook, so we need to accomodate those.
      // TO_SELECTED_GRADEBOOK is not really used. 
    }
    //  SAK-7162
    else if ("NONE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
    "GRADEBOOK_OPTIONS")))
    {
    	evaluationModel.setToGradeBook(EvaluationModel.NOT_TO_GRADEBOOK.toString());
    }
   

    // highest or last
    if ("HIGHEST_SCORE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "GRADE_SCORE")))
    {
      evaluationModel.setScoringType(EvaluationModel.HIGHEST_SCORE);
    }
    // not implementing average for now
    else if ("AVERAGE_SCORE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "GRADE_SCORE")))
    {
      evaluationModel.setScoringType(EvaluationModel.AVERAGE_SCORE);
    }
    else if ("LAST_SCORE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "GRADE_SCORE")))
    {
      evaluationModel.setScoringType(EvaluationModel.LAST_SCORE);
    }
    assessment.setEvaluationModel(evaluationModel);
  }

  
  private void updateSubmissionMessage(AssessmentFacade assessment, String submissionMsg) 
  {
    AssessmentAccessControl control =
        (AssessmentAccessControl)assessment.getAssessmentAccessControl();
    if (control == null){
      control = new AssessmentAccessControl();
      // need to fix accessControl so it can take AssessmentFacade later
      control.setAssessmentBase(assessment.getData());
    }
      if (submissionMsg != null)
    {
      control.setSubmissionMessage(makeFCKAttachment(submissionMsg));
    }

  }
  /**
   * Put access control settings into assessment (bi-directional)
   * @param assessment
   * @param duration Time interval for timed assessment (Iso8601 format)
   */
  private void makeAccessControl(AssessmentFacade assessment, String duration)
  {
    AssessmentAccessControl control =
        (AssessmentAccessControl)assessment.getAssessmentAccessControl();
    if (control == null){
      control = new AssessmentAccessControl();
      // need to fix accessControl so it can take AssessmentFacade later
      control.setAssessmentBase(assessment.getData());
    }

    // Control dates
    Iso8601DateFormat iso = new Iso8601DateFormat();
    String startDate = assessment.getAssessmentMetaDataByLabel("START_DATE");
    String dueDate = assessment.getAssessmentMetaDataByLabel("END_DATE");
    String retractDate = assessment.getAssessmentMetaDataByLabel("RETRACT_DATE");
    String feedbackDate = assessment.getAssessmentMetaDataByLabel(
        "FEEDBACK_DELIVERY_DATE");

    try
    {
      control.setStartDate(iso.parse(startDate).getTime());
      assessment.getData().addAssessmentMetaData("hasAvailableDate", "true");

    }
    catch (Iso8601FormatException ex)
    {
      log.debug("Cannot set startDate.");
    }
    try
    {
      control.setDueDate(iso.parse(dueDate).getTime());
//      assessment.getData().addAssessmentMetaData("hasDueDate", "true");
      assessment.getData().addAssessmentMetaData("dueDate", "true");
    }
    catch (Iso8601FormatException ex)
    {
      log.debug("Cannot set dueDate.");
    }
    try
    {
      control.setRetractDate(iso.parse(retractDate).getTime());
      assessment.getData().addAssessmentMetaData("hasRetractDate", "true");
    }
    catch (Iso8601FormatException ex)
    {
      log.debug("Cannot set retractDate.");
    }
    try
    {
      control.setFeedbackDate(iso.parse(feedbackDate).getTime());
      assessment.getData().addAssessmentMetaData("FEEDBACK_DELIVERY","DATED");
    }
    catch (Iso8601FormatException ex)
    {
      log.debug("Cannot set feedbackDate.");
    }

    // don't know what site you will have in a new environment
    // but registered as a BUG in SAM-271 so turning it on.

    String releasedTo = assessment.getAssessmentMetaDataByLabel(
        "ASSESSMENT_RELEASED_TO");

    // for backwards compatibility with version 1.5 exports.
    if (releasedTo != null && releasedTo.indexOf("Authenticated Users") > -1)
    {
      log.debug(
          "Fixing obsolete reference to 'Authenticated Users', setting released to 'Anonymous Users'.");
      releasedTo = AuthoringConstantStrings.ANONYMOUS;
    }

    if (releasedTo != null) {
      log.debug("control.setReleaseTo(releasedTo)='"+releasedTo+"'.");
      control.setReleaseTo(releasedTo);
    }
    
    // Timed Assessment
    if (duration != null)
    {
      try
      {
        Iso8601TimeInterval tiso = new Iso8601TimeInterval(duration);
        log.debug("tiso.getDuration(): " + tiso.getDuration());

        if(tiso==null)
        {
          throw new Iso8601FormatException("Assessment duration could not be resolved.");
        }
        long millisecondsDuration = tiso.getDuration();
        int seconds = (int) millisecondsDuration /1000;
        control.setTimeLimit( Integer.valueOf(seconds));
        if (seconds !=0)
        {
          control.setTimedAssessment(AssessmentAccessControl.TIMED_ASSESSMENT);
          assessment.getData().addAssessmentMetaData("hasTimeAssessment", "true");
        }
        else
        {
          control.setTimeLimit(Integer.valueOf(0));
          control.setTimedAssessment(AssessmentAccessControl.
                                     DO_NOT_TIMED_ASSESSMENT);
        }
      }
      catch (Iso8601FormatException ex)
      {
        log.warn("Can't format assessment duration. " + ex);
        control.setTimeLimit(Integer.valueOf(0));
        control.setTimedAssessment(AssessmentAccessControl.
                                   DO_NOT_TIMED_ASSESSMENT);
      }
    }
    else
    {
      control.setTimeLimit(Integer.valueOf(0));
      control.setTimedAssessment(AssessmentAccessControl.
                                 DO_NOT_TIMED_ASSESSMENT);
    }

    log.debug("assessment.getAssessmentMetaDataByLabel(AUTO_SUBMIT): " +
             assessment.getAssessmentMetaDataByLabel("AUTO_SUBMIT"));


    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "AUTO_SUBMIT")))
    {
      log.debug("AUTO SUBMIT IS TRUE");
      control.setAutoSubmit(AssessmentAccessControl.AUTO_SUBMIT);
      assessment.getData().addAssessmentMetaData("hasAutoSubmit", "true");
    }
    else
    {
      control.setAutoSubmit(AssessmentAccessControl.DO_NOT_AUTO_SUBMIT);
    }

    // Assessment Organization
    // navigation
    if ("LINEAR".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "NAVIGATION")))
    {
      control.setItemNavigation(control.LINEAR_ACCESS);
    }
    else
    {
      control.setItemNavigation(control.RANDOM_ACCESS);
    }

    // numbering
    if ("CONTINUOUS".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "QUESTION_NUMBERING")))
    {
      control.setItemNumbering(control.CONTINUOUS_NUMBERING);
    }
    else if ("RESTART".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "QUESTION_NUMBERING")))
    {
      control.setItemNumbering(control.RESTART_NUMBERING_BY_PART);
    }

    //question layout
    if ("I".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "QUESTION_LAYOUT")))
    {
      control.setAssessmentFormat(control.BY_QUESTION);
    }
    else if ("S".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "QUESTION_LAYOUT")))
    {
      control.setAssessmentFormat(control.BY_PART);
    }
    else
    {
      control.setAssessmentFormat(control.BY_ASSESSMENT);
    }

    // Mark for Review
    if ("True".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "MARK_FOR_REVIEW")))
    {
      control.setMarkForReview(control.MARK_FOR_REVIEW);
    }
    else
    {
      control.setMarkForReview(control.NOT_MARK_FOR_REVIEW);
    }
    
    //Submissions
    // submissions allowed
    String maxAttempts =
        "" + assessment.getAssessmentMetaDataByLabel("MAX_ATTEMPTS");
    String unlimited = AuthoringConstantStrings.UNLIMITED_SUBMISSIONS;
    log.debug("maxAttempts: '" + maxAttempts + "'");
    log.debug("unlimited: '" + unlimited + "'");

    if (
        unlimited.equals(maxAttempts.trim()))
    {
      log.debug("unlimited.equals(maxAttempts.trim()");
      control.setUnlimitedSubmissions(Boolean.TRUE);
      control.setSubmissionsAllowed(AssessmentAccessControlIfc.
                                    UNLIMITED_SUBMISSIONS);
    }
    else
    {
      control.setUnlimitedSubmissions(Boolean.FALSE);
      try
      {
        control.setSubmissionsAllowed(new Integer(maxAttempts));
      }
      catch (NumberFormatException ex1)
      {
        control.setSubmissionsAllowed(new Integer("1"));
      }
    }
    log.debug("Set: control.getSubmissionsAllowed()="+control.getSubmissionsAllowed());
    log.debug("Set: control.getUnlimitedSubmissions()="+control.getUnlimitedSubmissions());

    // late submissions
    // I am puzzled as to why there is no ACCEPT_LATE_SUBMISSION, assuming it =T
    if ("FALSE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "LATE_HANDLING")))
    {
      control.setLateHandling(control.NOT_ACCEPT_LATE_SUBMISSION);
    }
    else
    {
      control.setLateHandling(Integer.valueOf(1));

    }

    // auto save
    if ("TRUE".equalsIgnoreCase(assessment.getAssessmentMetaDataByLabel(
        "AUTO_SAVE")))
    {
      control.setAutoSubmit(control.AUTO_SAVE);
    }

    // Submission Message
    /*
    String submissionMessage = assessment.getAssessmentMetaDataByLabel(
        "SUBMISSION_MESSAGE");
    if (submissionMessage != null)
    {
      control.setSubmissionMessage(submissionMessage);
    }
    */

    
    // Username, password, finalPageUrl
//    String considerUserId = assessment.getAssessmentMetaDataByLabel(
//        "CONSIDER_USERID"); //
    String userId = assessment.getAssessmentMetaDataByLabel("USERID");
    String password = assessment.getAssessmentMetaDataByLabel("PASSWORD");
    String finalPageUrl = TextFormat.convertPlaintextToFormattedTextNoHighUnicode(log, assessment.getAssessmentMetaDataByLabel("FINISH_URL"));

    if (//"TRUE".equalsIgnoreCase(considerUserId) &&
        notNullOrEmpty(userId) && notNullOrEmpty(password))
    {
      control.setUsername(userId);
      control.setPassword(password);
      assessment.getData().addAssessmentMetaData("hasUsernamePassword", "true");
    }
    control.setFinalPageUrl(finalPageUrl);

    assessment.setAssessmentAccessControl(control);
  }

  /**
   * the ip address is in a newline delimited string
   * @param assessment
   */
  public void makeSecuredIPAddressSet(AssessmentFacade assessment, String ipList)
  {
    Set securedIPAddressSet = (Set) assessment.getSecuredIPAddressSet();
    AssessmentBaseIfc data = assessment.getData();

    if (securedIPAddressSet == null)
    {
      securedIPAddressSet = new HashSet();
    }
    //log.info("Getting securedIPAddressSet=" + securedIPAddressSet);

    //log.info("ipList: " + ipList);

    if (ipList == null)
      ipList = "";
    String[] ip = ipList.split("\\n");

    for (int j = 0; j < ip.length; j++)
    {
      //log.info("ip # " + j + ": " + ip[j]);
      if (ip[j] != null)
      {
        SecuredIPAddress sip = new SecuredIPAddress(data, null, ip[j]);
        //sip.setAssessment(data);
        securedIPAddressSet.add(sip);
      }
    }

    //log.info("securedIPAddressSet.size()=" + securedIPAddressSet.size());
    if (securedIPAddressSet.size()>0)
    {
      //log.info("Setting securedIPAddressSet;addAssessmentMetaData(hasIpAddress, true)");
      //AssessmentService assessmentService = new AssessmentService();
//      assessment.getData().setSecuredIPAddressSet(securedIPAddressSet);
//      assessment.getData().addAssessmentMetaData("hasIpAddress", "true");
//      assessment.getData().addAssessmentMetaData("hasSpecificIP", "true");
//      data.setSecuredIPAddressSet(securedIPAddressSet);
      data.addAssessmentMetaData("hasIpAddress", "true");
      data.addAssessmentMetaData("hasSpecificIP", "true");
      assessment.updateData(data);
      assessment.setSecuredIPAddressSet(securedIPAddressSet);

    }
  }
  
  /**
   * the ip address is in a newline delimited string
   * @param assessment
   */
  public void makeAssessmentAttachmentSet(AssessmentFacade assessment)
  {
    // if unzipLocation is null, there is no assessment attachment - no action is needed
    if (unzipLocation == null) {
    	return;
    }
	// first check if there is any attachment
	// if no attachment - no action is needed
	String attachment = assessment.getAssessmentAttachmentMetaData();
	if (attachment == null || "".equals(attachment)) {
	  	return;
	}
	
    Set assessmentAttachmentSet = (Set) assessment.getAssessmentAttachmentSet();
    if (assessmentAttachmentSet == null) {
    	assessmentAttachmentSet = new HashSet();
    }
        
    AssessmentAttachmentIfc assessmentAttachment;
    String[] attachmentArray = attachment.split("\\n");
    HashSet set = new HashSet();
	AttachmentHelper attachmentHelper = new AttachmentHelper();
	AssessmentService assessmentService = new AssessmentService();
    for (int i = 0; i < attachmentArray.length; i++) {
    	String[] attachmentInfo = attachmentArray[i].split("\\|");
    	String fullFilePath = unzipLocation + "/" + attachmentInfo[0];
    	String filename = attachmentInfo[1];
    	ContentResource contentResource = attachmentHelper.createContentResource(fullFilePath, filename, attachmentInfo[2]);
    	assessmentAttachment = assessmentService.createAssessmentAttachment(assessment, contentResource.getId(), filename, ServerConfigurationService.getServerUrl());
    	assessmentAttachment.setAssessment((AssessmentIfc)assessment.getData());
    	set.add(assessmentAttachment);
    }
    assessment.setAssessmentAttachmentSet(set);
  }
  
  /**
   * the ip address is in a newline delimited string
   * @param assessment
   */
  public void makeSectionAttachmentSet(SectionFacade section, Map sectionMap)
  {
    // if unzipLocation is null, there is no assessment attachment - no action is needed
	if (unzipLocation == null) {
		return;
	}  
	// first check if there is any attachment
	// if no attachment - no action is needed
	String attachment = (String) sectionMap.get("attachment");
	if (attachment == null || "".equals(attachment)) {
	  	return;
	}
	
    Set sectionAttachmentSet = (Set) section.getSectionAttachmentSet();
    if (sectionAttachmentSet == null) {
    	sectionAttachmentSet = new HashSet();
    }
        
    SectionAttachmentIfc sectionAttachment;
    String[] attachmentArray = attachment.split("\\n");
    HashSet set = new HashSet();
	AttachmentHelper attachmentHelper = new AttachmentHelper();
	AssessmentService assessmentService = new AssessmentService();
    for (int i = 0; i < attachmentArray.length; i++) {
    	String[] attachmentInfo = attachmentArray[i].split("\\|");
    	String fullFilePath = unzipLocation + "/" + attachmentInfo[0];
    	String filename = attachmentInfo[1];
    	ContentResource contentResource = attachmentHelper.createContentResource(fullFilePath, filename, attachmentInfo[2]);
    	sectionAttachment = assessmentService.createSectionAttachment(section, contentResource.getId(), filename, ServerConfigurationService.getServerUrl());
    	sectionAttachment.setSection(section.getData());
    	set.add(sectionAttachment);
    }
    section.setSectionAttachmentSet(set);
  }
  
  /**
   * the ip address is in a newline delimited string
   * @param assessment
   */
  public void makeItemAttachmentSet(ItemFacade item)
  {
	  // if unzipLocation is null, there is no assessment attachment - no action is needed
	  if (unzipLocation == null) {
		  return;
	  }  
	  // first check if there is any attachment
	  // if no attachment - no action is needed
	  String attachment = item.getItemAttachmentMetaData();
	  if (attachment == null || "".equals(attachment)) {
		  return;
	  }

	  Set itemAttachmentSet = (Set) item.getItemAttachmentSet();
	  if (itemAttachmentSet == null) {
		  itemAttachmentSet = new HashSet();
	  }

	  ItemAttachmentIfc itemAttachment;
	  String[] attachmentArray = attachment.split("\\n");
	  HashSet set = new HashSet();
	  AttachmentHelper attachmentHelper = new AttachmentHelper();
	  AssessmentService assessmentService = new AssessmentService();
	  for (int i = 0; i < attachmentArray.length; i++) {
		  String[] attachmentInfo = attachmentArray[i].split("\\|");
		  String fullFilePath = unzipLocation + "/" + attachmentInfo[0];
		  String filename = attachmentInfo[1];
		  ContentResource contentResource = attachmentHelper.createContentResource(fullFilePath, filename, attachmentInfo[2]);
		  itemAttachment = assessmentService.createItemAttachment(item, contentResource.getId(), filename, ServerConfigurationService.getServerUrl());
		  itemAttachment.setItem(item.getData());
		  set.add(itemAttachment);
	  }
	  item.setItemAttachmentSet(set);
  }

  /**
   * the ip address is in a newline delimited string
   * @param assessment
   */
  public String makeFCKAttachment(String text) {
	  if (text == null) {
		  return text;
	  }
	  String processedText = XmlUtil.processFormattedText(log, text);
	  // if unzipLocation is null, there is no assessment attachment - no action is needed
	  if (unzipLocation == null || processedText.equals("")) {
		  return processedText;
	  }

	  String accessURL = ServerConfigurationService.getAccessUrl();
	  String referenceRoot = AssessmentService.getContentHostingService().REFERENCE_ROOT;
	  String prependString = accessURL + referenceRoot;
	  StringBuffer updatedText = null;
	  ContentResource contentResource = null;
	  AttachmentHelper attachmentHelper = new AttachmentHelper();
	  String resourceId = null;
		  String importedPrependString = getImportedPrependString(processedText);
		  if (importedPrependString == null) {
			  return processedText;
		  }
		  String[] splittedString = processedText.split(importedPrependString);
		  int endIndex = 0;
		  String filename = null;
		  String contentType = null;
		  String fullFilePath = null;
		  String oldResourceId = null;

		  updatedText = new StringBuffer(splittedString[0]);
		  for (int i = 1; i < splittedString.length; i++) {
			  log.debug("splittedString[" + i + "] = " + splittedString[i]);
			  // Here is an example, splittedString will be something like:
			  // /group/b917f0b9-e21d-4819-80ee-35feac91c9eb/Blue Hill.jpg" alt="...  or
			  // /user/ktsao/Blue Hill.jpg" alt="...
			  // oldResourceId = /group/b917f0b9-e21d-4819-80ee-35feac91c9eb/Blue Hill.jpg or /user/ktsao/Blue Hill.jpg
			  // oldSplittedResourceId[0] = ""
			  // oldSplittedResourceId[1] = group or user
			  // oldSplittedResourceId[2] = b917f0b9-e21d-4819-80ee-35feac91c9eb or ktsao
			  // oldSplittedResourceId[3] = Blue Hill.jpg
			  endIndex = splittedString[i].indexOf("\"");
			  oldResourceId = splittedString[i].substring(0, endIndex);
			  String[] oldSplittedResourceId = oldResourceId.split("/");
			  fullFilePath = unzipLocation + "/" + oldResourceId.replace(" ", "");
			  filename = oldSplittedResourceId[oldSplittedResourceId.length - 1];
			  MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
			  contentType = mimetypesFileTypeMap.getContentType(filename);
			  contentResource = attachmentHelper.createContentResource(fullFilePath, filename, contentType);

			  if (contentResource != null) {
				  resourceId = contentResource.getId();
				  updatedText.append(prependString);
				  updatedText.append(resourceId);
				  updatedText.append(splittedString[i].substring(endIndex));
			  }
			  else {
				  throw new RuntimeException("resourceId is null");
			  }

		  }
		  return updatedText.toString();	  
  }
  
  public String makeFCKAttachmentFromRespondus(String text) {  
	  if (text == null) {
		  return text;
	  }
	  String processedText = XmlUtil.processFormattedText(log, text);
	  // if unzipLocation is null, there is no assessment attachment - no action is needed
	  if (unzipLocation == null || text.equals("")) {
		  return processedText;
	  }
	  
	  String accessURL = ServerConfigurationService.getAccessUrl();
	  String referenceRoot = AssessmentService.getContentHostingService().REFERENCE_ROOT;
	  String url = accessURL + referenceRoot;
	  String finalString =  makeImageAttachment(text, url);
	  return finalString;
  }
  
  private String makeImageAttachment(String text, String url) {
	  String splitDelimiter = "<img alt=\"\" src=\"";
	  String[] splittedString = text.split(splitDelimiter);
	  if (splittedString.length == 1) {
		  // no image files. check if audio files
		  return makeAudioAttachment(text, url);
	  }
	  	  
	  String prependString = splitDelimiter + url;
	  ContentResource contentResource = null;
	  String resourceId = null;
	  String filename = "";

	  StringBuffer updatedText = new StringBuffer(splittedString[0]);
	  int endIndex = 0;
	  for (int i = 1; i < splittedString.length; i++) {
		  endIndex = splittedString[i].indexOf("\"");
		  filename = splittedString[i].substring(0, endIndex);
		  contentResource = makeContentResource(filename);
		  
		  if (contentResource != null) {
			  resourceId = contentResource.getId();
			  updatedText.append(prependString);
			  updatedText.append(resourceId);
			  updatedText.append(makeAudioAttachment(splittedString[i].substring(endIndex), url));
		  }
		  else {
			  throw new RuntimeException("resourceId is null");
		  }
	  }
	  return updatedText.toString();
  }
  
  private String makeAudioAttachment(String text, String url) {
	  String splitDelimiter = "<embed src=\"";
	  String[] splittedString = text.split(splitDelimiter);
	  if (splittedString.length == 1) {
		  return makeOtherAttachment(text, url);
	  }
	  
	  String prependString = splitDelimiter + url;
	  ContentResource contentResource = null;
	  String resourceId = null;
	  String filename = "";

	  StringBuffer updatedText = new StringBuffer(splittedString[0]);
	  int endIndex = 0;
	  for (int i = 1; i < splittedString.length; i++) {
		  String s = splittedString[i];
		  endIndex = splittedString[i].indexOf("\"");
		  filename = splittedString[i].substring(0, endIndex);
		  contentResource = makeContentResource(filename);
		  
		  if (contentResource != null) {
			  resourceId = contentResource.getId();
			  updatedText.append(prependString);
			  updatedText.append(resourceId);
			  updatedText.append("\" type=\"application/x-shockwave-flash");
			  updatedText.append(makeOtherAttachment(splittedString[i].substring(endIndex), url));
		  }
		  else {
			  throw new RuntimeException("resourceId is null");
		  }
	  }
	  return updatedText.toString();
  }
  
  private String makeOtherAttachment(String text, String url) {
	  String splitDelimiter = "<a href=\"";
	  String[] splittedString = text.split(splitDelimiter);
	  if (splittedString.length == 1) {
		  return text;
	  }
	  
	  String prependString = splitDelimiter + url;
	  ContentResource contentResource = null;
	  String resourceId = null;
	  String filename = "";

	  StringBuffer updatedText = new StringBuffer(splittedString[0]);
	  int endIndex = 0;
	  for (int i = 1; i < splittedString.length; i++) {
		  endIndex = splittedString[i].indexOf("\"");
		  filename = splittedString[i].substring(0, endIndex);
		  contentResource = makeContentResource(filename);
		  
		  if (contentResource != null) {
			  resourceId = contentResource.getId();
			  updatedText.append(prependString);
			  updatedText.append(resourceId);
			  updatedText.append("\" target=\"_blank\"");
			  updatedText.append(splittedString[i].substring(endIndex));
		  }
		  else {
			  throw new RuntimeException("resourceId is null");
		  }
	  }
	  return updatedText.toString();
  }
  
  private ContentResource makeContentResource(String filename) {
	  AttachmentHelper attachmentHelper = new AttachmentHelper();
	  StringBuffer fullFilePath = new StringBuffer(unzipLocation);
	  fullFilePath.append("/");
	  fullFilePath.append(filename);
	  MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
	  String contentType = mimetypesFileTypeMap.getContentType(filename);
	  ContentResource contentResource = attachmentHelper.createContentResource(fullFilePath.toString(), filename, contentType);
	  
	  return contentResource;
  }
  
  public String makeFCKAttachment(List respondueTextList) {
	  // if unzipLocation is null, there is no assessment attachment - no action is needed
	  if (respondueTextList == null || respondueTextList.size() == 0) {
		  return "";
	  }  
	  String accessURL = ServerConfigurationService.getAccessUrl();
	  String referenceRoot = AssessmentService.getContentHostingService().REFERENCE_ROOT;
	  String prependString = accessURL + referenceRoot;
	  ContentResource contentResource = null;
	  AttachmentHelper attachmentHelper = new AttachmentHelper();
	  String resourceId = null;

	  String contentType = "";
	  String filename = "";
	  StringBuffer fullFilePath = null;

	  Iterator iter = respondueTextList.iterator();
	  String itemText = "";
	  String formattedText = "";
	  StringBuffer updatedText = new StringBuffer();
	  while (iter.hasNext()) {
		  itemText = (String) iter.next();
		  formattedText = XmlUtil.processFormattedText(log, itemText);
		  formattedText = formattedText.replaceAll("\\?\\?"," ");
		  String [] splittedText = formattedText.split(":::");
		  if ("mattext".equals(splittedText[0])) {
			  // &lt;!-- RspA --&gt; is inserted by Respondus when using web link
			  String correctedText = splittedText[1].replaceAll("&lt;!-- RspA --&gt;", "");
			  updatedText.append(correctedText);
		  }
		  else if ("matimage".equals(splittedText[0])) {
			  contentType = splittedText[1];
			  filename = splittedText[2];
			  fullFilePath = new StringBuffer(unzipLocation);
			  fullFilePath.append("/");
			  fullFilePath.append(filename);
			  contentResource = attachmentHelper.createContentResource(fullFilePath.toString(), filename, contentType);
			  if (contentResource != null) {
				  resourceId = contentResource.getId();
				  updatedText.append("<img src=\"");
				  updatedText.append(prependString);
				  updatedText.append(resourceId);
				  updatedText.append("\"/>");
			  }
			  else {
				  throw new RuntimeException("resourceId is null");
			  }
		  }
	  }

	  return updatedText.toString();
  }
  
  private String getImportedPrependString(String text) {
		String accessPath = ServerConfigurationService.getAccessPath();
		String referenceRoot = AssessmentService.getContentHostingService().REFERENCE_ROOT;
		String importedPrependString = accessPath + referenceRoot;

		String[] splittedString = text.split("src=\"");
		for (int i = 0; i < splittedString.length; i ++) {
			if (splittedString[i].indexOf(importedPrependString) > -1) {
				int length = importedPrependString.length();
				return splittedString[i].substring(0, splittedString[i].indexOf(importedPrependString) + length);
			}
		}
		return null;
  }
  
  /**
   * Update questionpool from the extracted properties.
   * Note: you need to do a save when you are done.
   * @param questionpool, which will  be persisted
   * @param assessmentMap, the extracted properties
   */
  public void updateQuestionPool(QuestionPoolFacade questionpool,
          Map assessmentMap)
  {
	  
	  String title = ((String)assessmentMap.get("title"));
	  questionpool.setDescription((String)assessmentMap.get("description"));
	  //questionpool.setLastModifiedById("Sakai Import");
	  questionpool.setLastModified(new Date());
	  // note: currently dateCreated field not in use 	  
	  //questionpool.setDateCreated(new Date());
	  questionpool.setOrganizationName((String)assessmentMap.get("ASSESSMENT_ORGANIZATIONNAME"));
	  questionpool.setObjectives((String)assessmentMap.get("ASSESSMENT_OBJECTIVES"));
	  questionpool.setKeywords((String)assessmentMap.get("ASSESSMENT_KEYWORDS"));
	  questionpool.setRubric((String)assessmentMap.get("ASSESSMENT_RUBRICS"));
	  questionpool.setIntellectualPropertyId((Long)assessmentMap.get("INTELLECTUALPROPERTYID"));
	  
	  log.debug("QPOOL ASSESSMENT updating metadata information");

  }
  
  /**
   * Update section from the extracted properties.
   * Note: you need to do a save when you are done.
   * @param section the section, which will  be persisted
   * @param sectionMap the extracted properties
   */
  public void updateSection(SectionFacade section, Map sectionMap)
  {
    section.setTitle(TextFormat.convertPlaintextToFormattedTextNoHighUnicode(log, (String) sectionMap.get("title")));
    section.setDescription(makeFCKAttachment((String) sectionMap.get("description")));
  }

  /**
   * Update item from the extracted properties.
   * Note: you need to do a save when you are done.
   * @param item the item, which will  be persisted
   * @param itemMap the extracted properties
   */
  public void updateItem(ItemFacade item, Map itemMap)
  {
	  updateItem(item, itemMap, false);
  }
  
  public void updateItem(ItemFacade item, Map itemMap, boolean isRespondus)
  {
    // type and title
    String title = (String) itemMap.get("title");
    item.setDescription(title);

    // set meta data
    List metalist = (List) itemMap.get("metadata");
    MetaDataList metadataList = new MetaDataList(metalist);
    metadataList.addTo(item);

    // type
    Long typeId = null;
    if (isRespondus) {
    	typeId = ItemTypeExtractionStrategy.calculate(itemMap);
    }
    else {
    log.debug("itemMap="+itemMap);
    String qmd = item.getItemMetaDataByLabel("qmd_itemtype");
    String itemIntrospect = (String) itemMap.get("itemIntrospect");
    log.debug("Calling ItemTypeExtractionStrategy.calculate(");
    log.debug("    title="+title);
    log.debug("    , itemIntrospect="+itemIntrospect);
    log.debug("    ,  qmd="+qmd);
    log.debug(");");

    typeId = ItemTypeExtractionStrategy.calculate(title, itemIntrospect, qmd);
    }
    item.setTypeId(typeId);

    // basic properties
    addItemProperties(item, itemMap, isRespondus);

    // feedback
    // correct, incorrect, general
    HashMap<String, String> varequalLinkrefidMap = null;
    HashMap<String, String> allFeedbacksMap = new HashMap();
    
    if (isRespondus) {
      List varequalLinkrefidMappingList = (List) itemMap.get("varequalLinkrefidMapping");
      varequalLinkrefidMap = new HashMap();
      if (varequalLinkrefidMappingList != null) {
        Iterator iter = varequalLinkrefidMappingList.iterator();
        String [] vl = null;
        while (iter.hasNext()) {
    	  vl = ((String) iter.next()).split(":::");
    	  varequalLinkrefidMap.put(vl[0], vl[1]);
        }
      }
    
      List allFeedbackList = (List) itemMap.get("allFeedbacks");
      if (allFeedbackList != null) {
          String [] af = null;
          ArrayList feedbackList = null;
          String s = null;
          for(int i = 0; i < allFeedbackList.size(); i++) {   
        	  s = (String) allFeedbackList.get(i);
          	  af = s.split(":::");
          	  allFeedbacksMap.put(af[0], af[1]);
          }
      }

      addRespondusFeedbacks(item, allFeedbacksMap, typeId);
    }
    else {
      addFeedback(item, itemMap, typeId);
    }
    // item text and answers
    if (TypeIfc.FILL_IN_BLANK.longValue() == typeId.longValue())
    {
      if (isRespondus) {
    	  addRespondusFibTextAndAnswers(item, itemMap);
      }
      else {
        addFibTextAndAnswers(item, itemMap);
      }
    }

  else if (TypeIfc.FILL_IN_NUMERIC.longValue() == typeId.longValue())
	    {
	  		addFibTextAndAnswers(item, itemMap);
	      //addFinTextAndAnswers(item, itemMap);  // 10/3/2006: Diego's code, duplicate of addFibTextAndAnswers
	    }
  else if (TypeIfc.MATCHING.longValue() == typeId.longValue())
  {
	  if (isRespondus) {
		  addRespondusMatchTextAndAnswers(item, itemMap);
	  }
	  else {
		  addMatchTextAndAnswers(item, itemMap);
	  }
  }
    else
    {
    	if (isRespondus) {
    		addRespondusTextAndAnswers(item, itemMap, varequalLinkrefidMap, allFeedbacksMap);
    	}
    	else {
    		addTextAndAnswers(item, itemMap);
    	}
    }

  }


  /**
   *
   * @param item
   * @param itemMap
   */
  private void addItemProperties(ItemFacade item, Map itemMap, boolean isRespondus)
  {
    String score = "0";
    if (isRespondus) {
      if (item.getTypeId().equals(TypeIfc.ESSAY_QUESTION)) {
    	score = "1";
      }
      else if (item.getTypeId().equals(TypeIfc.MULTIPLE_CORRECT) || item.getTypeId().equals(TypeIfc.FILL_IN_BLANK) || item.getTypeId().equals(TypeIfc.MATCHING)) {
        score = (String) itemMap.get("score");
      }
      else {
        List<String> scoreList = (List<String>) itemMap.get("scoreList");
        Iterator<String> iter = scoreList.iterator();
        StringBuffer buf = new StringBuffer(); 
          while (iter.hasNext()) {
       	    buf.append(iter.next()); 
	      }
          score = buf.toString();
      }
	}
	else {
     score = (String) itemMap.get("score");
	}
    String discount = (String) itemMap.get("discount");
    String hasRationale = (String) item.getItemMetaDataByLabel("hasRationale");//rshastri :SAK-1824
    String status = (String) itemMap.get("status");
    String createdBy = (String) itemMap.get("createdBy");
    String partialCreditFlag= (String) item.getItemMetaDataByLabel("PARTIAL_CREDIT");
    
    // created by is not nullable
    if (createdBy == null)
    {
      createdBy = "Imported by Sakai";
    }

    String createdDate = (String) itemMap.get("createdDate");
    
    item.setInstruction( (String) itemMap.get("instruction"));
    if (notNullOrEmpty(score))
    {
      item.setScore( Float.valueOf(score));
    }
    else {
    	item.setScore(Float.valueOf(0));
    }
    
    if (notNullOrEmpty(discount))
    {
    	item.setDiscount(Float.valueOf(discount));
    }
    else {
    	item.setDiscount(Float.valueOf(0));
    }

    if (notNullOrEmpty( partialCreditFlag))
    {
    	item.setPartialCreditFlag((Boolean.parseBoolean( partialCreditFlag)));
    }
    else {
    	item.setPartialCreditFlag((Boolean.FALSE));
    }
    
    item.setHint( (String) itemMap.get("hint"));
    if (notNullOrEmpty(hasRationale))
    {
      item.setHasRationale( Boolean.valueOf(hasRationale));
    }
    if (notNullOrEmpty(status))
    {
      item.setStatus( Integer.valueOf(status));
    }
    item.setCreatedBy(createdBy);
    try
    {
      Iso8601DateFormat iso = new Iso8601DateFormat();
      Calendar cal = iso.parse(createdDate);
      item.setCreatedDate(cal.getTime());
    }
    catch (Exception ex)
    {
      item.setCreatedDate(new Date());
    }
    item.setLastModifiedBy("Sakai Import");
    item.setLastModifiedDate(new Date());
  }
  
  private void addRespondusFeedbacks(ItemFacade item, Map<String, String> map, Long typeId)
  {
	  String type = null;
	  String correctItemFeedback = "";
	  String incorrectItemFeedback = "";
	  String generalItemFeedback = "";

	  if (typeId.equals(TypeIfc.ESSAY_QUESTION)) {
		  generalItemFeedback = "";
	  }
	  else if (typeId.equals(TypeIfc.TRUE_FALSE)) {
		  for (Entry<String, String> af : map.entrySet()) {
			  type = af.getKey();
			  if (type.endsWith("_ALL")) {
				  generalItemFeedback = makeFCKAttachmentFromRespondus((String) af.getValue());
			  }
			  else if (type.endsWith("_C")) {
				  correctItemFeedback = makeFCKAttachmentFromRespondus((String) af.getValue());
			  }
			  else if (type.endsWith("_IC")) {
				  incorrectItemFeedback = makeFCKAttachmentFromRespondus((String) af.getValue());
			  }
		  }
		  correctItemFeedback = generalItemFeedback + correctItemFeedback;
		  incorrectItemFeedback = generalItemFeedback + incorrectItemFeedback;
	  }
	  else {
		  for (Entry<String, String> af : map.entrySet()) {
			  type = af.getKey();
			  if (type.endsWith("_ALL")) {
				  generalItemFeedback = makeFCKAttachmentFromRespondus((String) af.getValue());
			  }
		  }
		  correctItemFeedback = generalItemFeedback;
		  incorrectItemFeedback = generalItemFeedback;

	  }

	  item.setGeneralItemFeedback(generalItemFeedback);
	  item.setCorrectItemFeedback(correctItemFeedback);
	  item.setInCorrectItemFeedback(incorrectItemFeedback);
  }

  private void addFeedback(ItemFacade item, Map<String, String> map, Long typeId)
  {
	  String correctItemFeedback = (String) map.get("correctItemFeedback");
	  String incorrectItemFeedback = (String) map.get("incorrectItemFeedback");
	  String generalItemFeedback = (String) map.get("generalItemFeedback");

	  if (generalItemFeedback==null) generalItemFeedback = "";
	  if (TypeIfc.AUDIO_RECORDING.longValue() == typeId.longValue() ||
			  TypeIfc.FILE_UPLOAD.longValue() == typeId.longValue() ||
			  TypeIfc.ESSAY_QUESTION.longValue() == typeId.longValue())
	  {
		  if (notNullOrEmpty(incorrectItemFeedback))
		  {
			  generalItemFeedback += " " + incorrectItemFeedback;
		  }
		  if (notNullOrEmpty(correctItemFeedback))
		  {
			  generalItemFeedback += " " + correctItemFeedback;
		  }
	  }

	  if (notNullOrEmpty(correctItemFeedback))
	  {
		  item.setCorrectItemFeedback(makeFCKAttachment(correctItemFeedback));
	  }
	  if (notNullOrEmpty(incorrectItemFeedback))
	  {
		  item.setInCorrectItemFeedback(makeFCKAttachment(incorrectItemFeedback));
	  }
	  if (notNullOrEmpty(generalItemFeedback))
	  {
		  item.setGeneralItemFeedback(makeFCKAttachment(generalItemFeedback));
	  }
  }
  
  /**
   * @param item
   * @param itemMap
   */
  private void addTextAndAnswers(ItemFacade item, Map itemMap)
  {
	  List itemTextList = (List) itemMap.get("itemText");
	  HashSet itemTextSet = new HashSet();
	  List answerFeedbackList = (List) itemMap.get("itemAnswerFeedback");

	  ArrayList correctLabels = (ArrayList) itemMap.get("itemAnswerCorrectLabel");
	  if (correctLabels == null)
	  {
		  correctLabels = new ArrayList();
	  }
	  List answerList = new ArrayList();
	  List aList = (List) itemMap.get("itemAnswer");
	  answerList = aList == null ? answerList : aList;

	  for (int i = 0; i < itemTextList.size(); i++)
	  {
		  ItemText itemText = new ItemText();

		  String text = (String) itemTextList.get(i);
		  // should be allow this or, continue??
		  // for now, empty string OK, setting to empty string if null
		  if (text == null)
		  {
			  text = "";
		  }
		  text=text.replaceAll("\\?\\?"," ");//SAK-2298
		  log.debug("text: " + text);
		  itemText.setText(makeFCKAttachment(text));

		  itemText.setItem(item.getData());
		  itemText.setSequence( Long.valueOf(i + 1));

		  List answerScoreList = new ArrayList(); //--mustansar
		  List sList = (List) itemMap.get("answerScore"); //--mustansar
		  answerScoreList = sList == null ? answerScoreList : sList; //--mustansar
		  HashSet answerSet = new HashSet();
		  char answerLabel = 'A';
		  for (int a = 0; a < answerList.size(); a++)
		  {
			  Answer answer = new Answer();
			  String answerText = XmlUtil.processFormattedText(log, (String) answerList.get(a));
			  String ident = "";
			  // these are not supposed to be empty
			  if (notNullOrEmpty(answerText))
			  {
				  answerText=answerText.replaceAll("\\?\\?"," ");//SAK-2298
				  log.debug("answerText: " + answerText);
				  String label = "" + answerLabel++;
				  answer.setLabel(label); // up to 26, is this a problem?

				  // correct answer and score
				  float score = 0;
				  float discount = 0;

				  answer.setIsCorrect(Boolean.FALSE);
				  // if label matches correct answer it is correct
				  if (isCorrectLabel(label, correctLabels))
				  {
					  answer.setIsCorrect(Boolean.TRUE);
				  }

				  // normalize all true/false questions
				  if (item.getTypeId().equals(TypeIfc.TRUE_FALSE))
				  {
					  if (answerText.equalsIgnoreCase("true")) answerText = "true";
					  if (answerText.equalsIgnoreCase("false")) answerText = "false";
				  }

				  // manual authoring disregards correctness
				  // so we will do the same.
				  score = getCorrectScore(item, 1);
				  discount = getCorrectDiscount(item);
				  log.debug("setting answer" + label + " score to:" + score);
				  answer.setScore( Float.valueOf(score));
				  log.debug("setting answer " + label + " discount to:" + discount);
				  answer.setDiscount(Float.valueOf(discount));

				  answer.setText(makeFCKAttachment(answerText));
				  answer.setItemText(itemText);
				  answer.setItem(item.getData());
				  int sequence = a + 1;
				  answer.setSequence( Long.valueOf(sequence));
				  // prepare answer feedback - daisyf added this on 2/21/05
				  // need to check if this works for question type other than
				  // MC
				  if (item.getTypeId().equals(TypeIfc.MULTIPLE_CHOICE) || item.getTypeId().equals(TypeIfc.MULTIPLE_CORRECT)) {
					  HashSet set = new HashSet();
					  if (answerFeedbackList != null) {
						  AnswerFeedback answerFeedback = new AnswerFeedback();
						  answerFeedback.setAnswer(answer);
						  answerFeedback.setTypeId(AnswerFeedbackIfc.GENERAL_FEEDBACK);
						  if (answerFeedbackList.get(sequence - 1) != null)
						  {
							  answerFeedback.setText(makeFCKAttachment(XmlUtil.processFormattedText(log, (String) answerFeedbackList.get(sequence - 1))));
							  set.add(answerFeedback);
							  answer.setAnswerFeedbackSet(set);
						  }
					  }

					  boolean MCSC=itemMap.get("itemIntrospect").equals("Multiple Choice");
					  if(MCSC){
						  long index = answer.getSequence().longValue(); 
						  index = index - 1L;
						  int indexInteger = Long.valueOf(index).intValue();
						  String strPCredit = (String) answerScoreList.get(indexInteger);
						  float fltPCredit = Float.parseFloat(strPCredit);
						  Float pCredit = (fltPCredit/(item.getScore().floatValue()))*100;
						  if (pCredit.isNaN()){
							  answer.setPartialCredit(0F);
						  }
						  else{
							  answer.setPartialCredit(pCredit);
						  }
					  }

				  }

				  answerSet.add(answer);
			  }
		  }
		  itemText.setAnswerSet(answerSet);
		  itemTextSet.add(itemText);
	  }
	  item.setItemTextSet(itemTextSet);
  }

  private void addRespondusTextAndAnswers(ItemFacade item, Map itemMap, HashMap<String, String> varequalLinkrefidMap, 
		  HashMap<String, String> allFeedbacksMap)
  {
	  List itemTextList = (List) itemMap.get("itemText");
	  HashSet itemTextSet = new HashSet();
	  List answerFeedbackList = (List) itemMap.get("itemAnswerFeedback");

	  ArrayList correctLabels = (ArrayList) itemMap.get("itemAnswerCorrectLabel");
	  if (correctLabels == null)
	  {
		  correctLabels = new ArrayList();
	  }
	  List answerList = new ArrayList();
	  List aList = (List) itemMap.get("itemAnswer");
	  answerList = aList == null ? answerList : aList;

	  if (item.getTypeId().equals(TypeIfc.ESSAY_QUESTION) ) {
		  answerList.add(allFeedbacksMap);
	  }

	  for (int i = 0; i < itemTextList.size(); i++)
	  {
		  ItemText itemText = new ItemText();
		  itemText.setText(makeFCKAttachmentFromRespondus((String) itemTextList.get(i)));
		  itemText.setItem(item.getData());
		  itemText.setSequence( Long.valueOf(i + 1));

		  List answerScoreList = new ArrayList(); //--mustansar
		  List sList = (List) itemMap.get("answerScore"); //--mustansar
		  answerScoreList = sList == null ? answerScoreList : sList; //--mustansar
		  HashSet answerSet = new HashSet();
		  char answerLabel = 'A';
		  for (int a = 0; a < answerList.size(); a++)
		  {
			  Answer answer = new Answer();
			  if (item.getTypeId().equals(TypeIfc.ESSAY_QUESTION) ) {
				  for (Entry<String, String> entrySet : allFeedbacksMap.entrySet()) {
					  String allFeedback = (String) entrySet.getValue();
					  answer.setText(this.makeFCKAttachmentFromRespondus(allFeedback));
				  }
				  answer.setIsCorrect(Boolean.TRUE);
			  }
			  else {
				  String answerText = (String) answerList.get(a);
				  if (notNullOrEmpty(answerText)) {
					  String [] data = answerText.split(":::");
					  String ident = data[0];
					  answer.setIsCorrect(Boolean.FALSE);
					  if (isCorrectLabel(ident, correctLabels)) {
						  answer.setIsCorrect(Boolean.TRUE);
					  }

					  answerText = data[1];
					  // normalize all true/false questions
					  if (item.getTypeId().equals(TypeIfc.TRUE_FALSE))
					  {
						  if (answerText.equalsIgnoreCase("true")) answerText = "true";
						  if (answerText.equalsIgnoreCase("false")) answerText = "false";
						  answer.setText(answerText);
					  }
					  else {
						  answer.setText(makeFCKAttachmentFromRespondus(answerText));
					  }
					  // prepare answer feedback - daisyf added this on 2/21/05
					  // need to check if this works for question type other than
					  // MC
					  if (item.getTypeId().equals(TypeIfc.MULTIPLE_CHOICE) || item.getTypeId().equals(TypeIfc.MULTIPLE_CORRECT)) {
						  HashSet set = new HashSet();
						  AnswerFeedback answerFeedback = new AnswerFeedback();
						  answerFeedback.setAnswer(answer);
						  answerFeedback.setTypeId(AnswerFeedbackIfc.GENERAL_FEEDBACK);
						  if (varequalLinkrefidMap.get(ident) != null) {
							  String linkrefid = (String) varequalLinkrefidMap.get(ident);
							  if (linkrefid.endsWith("_C") || linkrefid.endsWith("_IC")) {
								  if (allFeedbacksMap.get(linkrefid) != null) {
									  answerFeedback.setText(this.makeFCKAttachmentFromRespondus((String) allFeedbacksMap.get(linkrefid)));
									  set.add(answerFeedback);
									  answer.setAnswerFeedbackSet(set);
								  }
							  }
						  }
					  }
				  }
			  }
			  String label = "" + answerLabel++;
			  answer.setLabel(label); // up to 26, is this a problem?

			  // correct answer and score
			  float score = 0;
			  float discount = 0;

			  // manual authoring disregards correctness
			  // so we will do the same.
			  score = getCorrectScore(item, 1);
			  discount = getCorrectDiscount(item);
			  log.debug("setting answer" + label + " score to:" + score);
			  answer.setScore( Float.valueOf(score));
			  log.debug("setting answer " + label + " discount to:" + discount);
			  answer.setDiscount(Float.valueOf(discount));

			  answer.setItemText(itemText);
			  answer.setItem(item.getData());
			  int sequence = a + 1;
			  answer.setSequence( Long.valueOf(sequence));

			  answerSet.add(answer);

		  }
		  itemText.setAnswerSet(answerSet);
		  itemTextSet.add(itemText);
	  }
	  item.setItemTextSet(itemTextSet);
  }

  private float getCorrectScore(ItemDataIfc item, int answerSize)
  {
    float score =0;
    if (answerSize>0 && item!=null && item.getScore()!=null)
    {
      score = item.getScore().floatValue()/answerSize;
    }
    return score;
  }

  private float getCorrectDiscount(ItemDataIfc item)
  {
	  float discount =0;
	  if (item!=null && item.getDiscount()!=null)
	  {
		  discount = item.getDiscount().floatValue();
	  }
	  return discount;
  }
  
  /**
   * Check to find out it response label is in the list of correct responses
   * @param testLabel response label
   * @param labels the list of correct responses
   * @return
   */  
  private boolean isCorrectLabel(String testLabel, ArrayList labels)
  {
    if (testLabel == null || labels == null
        || labels.indexOf(testLabel) == -1)
    {
      return false;
    }

    return true;
  }
  
  /**
   * Respondus questions
   */
  private boolean isCorrectAnswer(String answerText, ArrayList correctLabels)
  {
    if (answerText == null || correctLabels == null
        || correctLabels.indexOf(answerText) == -1)
    {
      return false;
    }

    return true;
  }

  /**
   * Respondus questions
   */
  private boolean isCorrectAnswer2(String answerText, ArrayList correctLabels)
  {
    if (answerText == null || correctLabels == null)
    {
      return false;
    }
    else {
    	String [] data = answerText.split(":::");
    	if (correctLabels.indexOf(data[0]) == -1) {
    		return false;
    	}
    }
    return true;
  }

  
  /**
   * FIB questions ONLY
   * @param item
   * @param itemMap
   */
  private void addFibTextAndAnswers(ItemFacade item, Map itemMap)
  {
    List itemTextList = new ArrayList();
    List iList = (List) itemMap.get("itemFibText");
    itemTextList = iList == null ? itemTextList : iList;

    List itemTList = new ArrayList();
    List tList = (List) itemMap.get("itemText");
    itemTList = iList == null ? itemTList : tList;

    HashSet itemTextSet = new HashSet();
    ItemText itemText = new ItemText();
    //String itemTextString = "";
    List answerFeedbackList = (List) itemMap.get("itemFeedback");

    List answerList = new ArrayList();
    List aList = (List) itemMap.get("itemFibAnswer");
    answerList = aList == null ? answerList : aList;

    // handle FIB with instructional text
    // sneak it into first text
    if (   !itemTList.isEmpty()
        && !itemTextList.isEmpty()
        && !(itemTextList.size()>1))
    {
      try
      {
        String firstFib = XmlUtil.processFormattedText(log, (String) itemTextList.get(0));
        String firstText = XmlUtil.processFormattedText(log, (String) itemTList.get(0));
        if (firstFib.equals(firstText))
        {
          log.debug("Setting FIB instructional text.");
//          itemTextList.remove(0);
          String newFirstFib
            = firstFib + "<br />" + itemTextList.get(0);
          itemTextList.set(0, newFirstFib);
        }
      }
      catch (Exception ex)
      {
        log.warn("Thought we found an instructional text but couldn't put it in."
                + " " + ex);
      }
    }
    // loop through all our extracted FIB texts interposing FIB_BLANK_INDICATOR
    
    StringBuilder itemTextStringbuf = new StringBuilder();
     
    
    for (int i = 0; i < itemTextList.size(); i++)
    {
      String text = XmlUtil.processFormattedText(log, (String) itemTextList.get(i));
      // we are assuming non-empty text/answer/non-empty text/answer etc.
      if (text == null)
      {
        continue;
      }
      //itemTextString += text;
      itemTextStringbuf.append(text);
      if (i < answerList.size())
      {
        itemTextStringbuf.append(FIB_BLANK_INDICATOR);
      }
    }
    String itemTextString = itemTextStringbuf.toString();
    
    itemTextString=itemTextString.replaceAll("\\?\\?"," ");//SAK-2298
    log.debug("itemTextString="+itemTextString);
    itemText.setText(makeFCKAttachment(itemTextString));
    itemText.setItem(item.getData());
    itemText.setSequence( Long.valueOf(0));
    HashSet answerSet = new HashSet();
    char answerLabel = 'A';
    for (int a = 0; a < answerList.size(); a++)
    {
      Answer answer = new Answer();
      String answerText = (String) answerList.get(a);
      // these are not supposed to be empty
      if (notNullOrEmpty(answerText))
      {
        answerText=answerText.replaceAll("\\?\\?"," ");//SAK-2298
        log.debug("answerText="+answerText);

        String label = "" + answerLabel++;
        answer.setLabel(label); // up to 26, is this a problem?
        answer.setText(makeFCKAttachment(answerText));
        answer.setItemText(itemText);

        // correct answer and score
        answer.setIsCorrect(Boolean.TRUE);
        // manual authoring disregards the number of partial answers
        // so we will do the same.
        float score = getCorrectScore(item, 1);
        float discount = getCorrectDiscount(item);
//        float score = getCorrectScore(item, answerList.size());

        log.debug("setting answer " + label + " score to:" + score);
        answer.setScore( Float.valueOf(score));
        
        log.debug("setting answer " + label + " discount to:" + discount);
        answer.setDiscount( Float.valueOf(discount));
        
        answer.setItem(item.getData());
        int sequence = a + 1;
        answer.setSequence( Long.valueOf(sequence));
        HashSet set = new HashSet();
        if (answerFeedbackList != null)
        {
          AnswerFeedback answerFeedback = new AnswerFeedback();
          answerFeedback.setAnswer(answer);
          answerFeedback.setTypeId(AnswerFeedbackIfc.GENERAL_FEEDBACK);
          if (answerFeedbackList.get(sequence - 1) != null)
          {
            answerFeedback.setText(makeFCKAttachment((String) answerFeedbackList.get(sequence - 1)));
            set.add(answerFeedback);
            answer.setAnswerFeedbackSet(set);
          }
        }
        answerSet.add(answer);
      }
    }

    itemText.setAnswerSet(answerSet);
    itemTextSet.add(itemText);
    item.setItemTextSet(itemTextSet);
  }

  private void addRespondusFibTextAndAnswers(ItemFacade item, Map itemMap)
  {
	  HashSet itemTextSet = new HashSet();
	  ItemText itemText = new ItemText();
	  List answerFeedbackList = (List) itemMap.get("itemFeedback");

	  List answerList = new ArrayList();
	  List aList = (List) itemMap.get("itemFibAnswer");
	  answerList = aList == null ? answerList : aList;

	  List itemTextList = (List) itemMap.get("itemText");
	  StringBuilder itemTextStringbuf = new StringBuilder();
	  if (itemTextList != null && itemTextList.size() > 0) {
		  itemTextStringbuf.append(makeFCKAttachmentFromRespondus((String) itemTextList.get(0)));
	  }

	  //itemTextStringbuf.append("<br/>");
	  itemTextStringbuf.append(FIB_BLANK_INDICATOR);
	  itemText.setText(itemTextStringbuf.toString());
	  itemText.setItem(item.getData());
	  itemText.setSequence(Long.valueOf(0));
	  HashSet answerSet = new HashSet();

	  Answer answer = new Answer();
	  StringBuilder answerTextStringbuf = new StringBuilder();
	  for (int a = 0; a < answerList.size(); a++)
	  {
		  String answerText = (String) answerList.get(a);
		  if (notNullOrEmpty(answerText))
		  {
			  answerText=answerText.replaceAll("\\?\\?"," ");
			  log.debug("answerText="+answerText);
			  answerTextStringbuf.append(answerText);
			  if (a < answerList.size() - 1) {
				  answerTextStringbuf.append("|");
			  }
		  }
	  }
	  answer.setLabel("A");
	  answer.setText(makeFCKAttachment(answerTextStringbuf.toString()));
	  answer.setItemText(itemText);
	  answer.setIsCorrect(Boolean.TRUE);
	  answer.setScore(Float.valueOf(item.getScore()));
	  answer.setItem(item.getData());
	  answer.setSequence(Long.valueOf(1l));
	  answerSet.add(answer);
	  itemText.setAnswerSet(answerSet);
	  itemTextSet.add(itemText);
	  item.setItemTextSet(itemTextSet);
  }

  /**
   * MATCHING questions ONLY
   * @param item
   * @param itemMap
   */
  private void addMatchTextAndAnswers(ItemFacade item, Map itemMap)
  {

    List sourceList = (List) itemMap.get("itemMatchSourceText");
    List targetList = (List) itemMap.get("itemMatchTargetText");
    List indexList = (List) itemMap.get("itemMatchIndex");
    List answerFeedbackList = (List) itemMap.get("itemFeedback");
    List correctMatchFeedbackList = (List) itemMap.get(
      "itemMatchCorrectFeedback");
    List incorrectMatchFeedbackList = (List) itemMap.get(
      "itemMatchIncorrectFeedback");
    List itemTextList = (List) itemMap.get("itemText");

    sourceList = sourceList == null ? new ArrayList() : sourceList;
    targetList = targetList == null ? new ArrayList() : targetList;
    indexList = indexList == null ? new ArrayList() : indexList;
    answerFeedbackList =
      answerFeedbackList == null ? new ArrayList() : answerFeedbackList;
    correctMatchFeedbackList =
      correctMatchFeedbackList ==
      null ? new ArrayList() : correctMatchFeedbackList;
    incorrectMatchFeedbackList =
      incorrectMatchFeedbackList ==
      null ? new ArrayList() : incorrectMatchFeedbackList;

    log.debug("*** original incorrect order");
    for (int i = 0; i < incorrectMatchFeedbackList.size(); i++) {
      log.debug("incorrectMatchFeedbackList.get(" + i + ")="+
                         incorrectMatchFeedbackList.get(i));
    }
    int maxNumCorrectFeedback = sourceList.size();
    int numIncorrectFeedback = incorrectMatchFeedbackList.size();

    if (maxNumCorrectFeedback>0 && numIncorrectFeedback>0)
    {
      incorrectMatchFeedbackList =
        reassembleIncorrectMatches(
          incorrectMatchFeedbackList, maxNumCorrectFeedback);
    }

    log.debug("*** NEW order");
    for (int i = 0; i < incorrectMatchFeedbackList.size(); i++) {
      log.debug("incorrectMatchFeedbackList.get(" + i + ")="+
                         incorrectMatchFeedbackList.get(i));

    }

    itemTextList =
      itemTextList == null ? new ArrayList() : itemTextList;

    if (targetList.size() <indexList.size())
    {
      log.debug("targetList.size(): " + targetList.size());
      log.debug("indexList.size(): " + indexList.size());
    }

    String itemTextString = "";
    if (itemTextList.size()>0)
    {
      itemTextString = XmlUtil.processFormattedText(log, (String) itemTextList.get(0));
    }

    HashSet itemTextSet = new HashSet();

    // first, add the question text
    if (itemTextString==null) itemTextString = "";
    itemTextString=itemTextString.replaceAll("\\?\\?"," ");//SAK-2298
    log.debug("item.setInstruction itemTextString: " + itemTextString);
    item.setInstruction(itemTextString);

    // loop through source texts indicating answers (targets)
    for (int i = 0; i < sourceList.size(); i++)
    {
      // create the entry for the matching item (source)
      String sourceText = (String) sourceList.get(i);
      if (sourceText == null) sourceText="";
      sourceText=sourceText.replaceAll("\\?\\?"," ");//SAK-2298
      log.debug("sourceText: " + sourceText);

      ItemText sourceItemText = new ItemText();
      sourceItemText.setText(makeFCKAttachment(sourceText));
      sourceItemText.setItem(item.getData());
      sourceItemText.setSequence( Long.valueOf(i + 1));

      // find the matching answer (target)
      HashSet targetSet = new HashSet();
      String targetString;
      int targetIndex = 999;// obviously not matching value
      try
      {
        targetIndex = Integer.parseInt( (String) indexList.get(i));
      }
      catch (NumberFormatException ex)
      {
        log.warn("No match for " + sourceText + "."); // default to no match
      }
      catch (IndexOutOfBoundsException ex)
      {
        log.error("Corrupt index list.  Cannot assign match for: " +sourceText + ".");
      }
      // loop through all possible targets (matching answers)
      char answerLabel = 'A';
      for (int a = 0; a < targetList.size(); a++)
      {
        targetString = XmlUtil.processFormattedText(log, (String) targetList.get(a));
        if (targetString == null)
        {
          targetString = "";
        }
        targetString=targetString.replaceAll("\\?\\?"," ");//SAK-2298
        log.debug("targetString: " + targetString);


        Answer target = new Answer();

        //feedback
        HashSet answerFeedbackSet = new HashSet();

        if (correctMatchFeedbackList.size() > i)
        {
          String fb = (String) correctMatchFeedbackList.get(i);
          answerFeedbackSet.add( new AnswerFeedback(
            target, AnswerFeedbackIfc.CORRECT_FEEDBACK, fb));
        }
        if (incorrectMatchFeedbackList.size() > i)
        {
          String fb = (String) incorrectMatchFeedbackList.get(i);
          log.debug("setting incorrect fb="+fb);
          answerFeedbackSet.add( new AnswerFeedback(
            target, AnswerFeedbackIfc.INCORRECT_FEEDBACK, fb));
        }

        target.setAnswerFeedbackSet(answerFeedbackSet);

        String label = "" + answerLabel++;
        target.setLabel(label); // up to 26, is this a problem?
        target.setText(makeFCKAttachment(targetString));
        target.setItemText(sourceItemText);
        target.setItem(item.getData());
        target.setSequence( Long.valueOf(a + 1));

        // correct answer and score
        // manual authoring disregards the number of partial answers
        // or whether the answer is correct so we will do the same.
//        float score = 0;
        float score = getCorrectScore(item, 1);
        float discount = getCorrectDiscount(item);
        
        // if this answer is the indexed one, flag as correct
        if (a + 1 == targetIndex)
        {
          target.setIsCorrect(Boolean.TRUE);
//          score = getCorrectScore(item, targetList.size());
          log.debug("source: " + sourceText + " matches target: " + targetString);
        }
        else
        {
          target.setIsCorrect(Boolean.FALSE);
        }
        log.debug("setting answer " + a + " score to:" + score);
        target.setScore( Float.valueOf(score));
        target.setDiscount(Float.valueOf(discount));

        if (answerFeedbackList != null)
        {
          Set targetFeedbackSet = new HashSet();
          AnswerFeedback tAnswerFeedback = new AnswerFeedback();
          tAnswerFeedback.setAnswer(target);
          tAnswerFeedback.setTypeId(AnswerFeedbackIfc.GENERAL_FEEDBACK);
          String targetFeedback = "";
          if (answerFeedbackList.size()>0)
          {
            targetFeedback = (String) answerFeedbackList.get(targetIndex);
          }
          if (targetFeedback.length()>0)
          {
            tAnswerFeedback.setText(makeFCKAttachment(targetFeedback));
            targetFeedbackSet.add(tAnswerFeedback);
            target.setAnswerFeedbackSet(targetFeedbackSet);
          }
        }
        targetSet.add(target);
      }

      sourceItemText.setAnswerSet(targetSet);
      itemTextSet.add(sourceItemText);
    }

    item.setItemTextSet(itemTextSet);
  }

  private void addRespondusMatchTextAndAnswers(ItemFacade item, Map itemMap) throws RespondusMatchingException
  {

	  List sourceList = (List) itemMap.get("itemMatchSourceText");
	  List targetList = (List) itemMap.get("itemAnswer");
	  List correctList = (List) itemMap.get("itemMatchingAnswerCorrect");

	  sourceList = sourceList == null ? new ArrayList() : sourceList;
	  targetList = targetList == null ? new ArrayList() : targetList;
	  correctList = correctList == null ? new ArrayList() : correctList;

	  List itemTextList = (List) itemMap.get("itemText");
	  if (itemTextList != null && itemTextList.size() > 0) {
		  item.setInstruction(makeFCKAttachmentFromRespondus((String) itemTextList.get(0)));
	  }
	  
	  if (Math.pow(sourceList.size(), 2) != targetList.size()) {
		  throw new RespondusMatchingException("Matching question error!");
	  }
	  
	  HashMap<String, String> sourceMap = new HashMap<String, String>();
	  if (sourceList != null) {
		  Iterator iter = sourceList.iterator();
		  String [] s = null;
		  while (iter.hasNext()) {
			  s = ((String) iter.next()).split(":::");
			  sourceMap.put(s[0], s[1]);
		  }
	  }
	  HashMap<String, String> targetMap = new HashMap<String, String>();
	  if (targetList != null) {
		  Iterator iter = targetList.iterator();
		  String [] s = null;
		  while (iter.hasNext()) {
			  s = ((String) iter.next()).split(":::");
			  targetMap.put(s[0], s[1]);
		  }
	  }
	  HashMap<String, String> correctMap = new HashMap<String, String>();
	  if (correctList != null) {
		  Iterator iter = correctList.iterator();
		  String [] s = null;
		  while (iter.hasNext()) {
			  s = ((String) iter.next()).split(":::");
			  correctMap.put(s[0], s[1]);
		  }
	  }

	  HashSet itemTextSet = new HashSet();
	  String ident = "";
	  String correctVar = "";
	  String sourceText = "";
	  String targetText = "";
	  int itemSequence = 1;
	  for (Entry<String, String> source : sourceMap.entrySet()) {
		  char answerLabel = 'A';
		  sourceText = source.getValue();
		  if (sourceText == null) sourceText = "";
		  sourceText = sourceText.replaceAll("\\?\\?"," ");//SAK-2298
		  log.debug("sourceText: " + sourceText);

		  ItemText sourceItemText = new ItemText();
		  sourceItemText.setText(makeFCKAttachmentFromRespondus(sourceText));
		  sourceItemText.setItem(item.getData());
		  sourceItemText.setSequence(Long.valueOf(itemSequence++)); 

		  int answerSequence = 1;
		  HashSet targetSet = new HashSet();
		  for (Entry<String, String> target : targetMap.entrySet()) {

			  targetText = target.getValue();
			  if (targetText == null) targetText = "";
			  targetText = targetText.replaceAll("\\?\\?"," ");
			  log.debug("targetText: " + targetText);

			  Answer answer = new Answer();

			  String label = "" + answerLabel++;
			  answer.setLabel(label); // up to 26, is this a problem?
			  answer.setText(makeFCKAttachmentFromRespondus(targetText));
			  answer.setItemText(sourceItemText);
			  answer.setItem(item.getData());
			  answer.setSequence( Long.valueOf(answerSequence++));
			  answer.setScore(Float.valueOf(getCorrectScore(item, 1)));

			  ident = source.getKey();
			  correctVar = correctMap.get(ident);
			  if (correctVar.equals(target.getKey())) {
				  answer.setIsCorrect(Boolean.TRUE);
			  }
			  else {
				  answer.setIsCorrect(Boolean.FALSE);
			  }
			  targetSet.add(answer);
		  }

		  sourceItemText.setAnswerSet(targetSet);
		  itemTextSet.add(sourceItemText);
	  }
	  
	  item.setItemTextSet(itemTextSet);
  }


  /**
   * Helper method rotates the first n.
   * This will work with Samigo matching where
   * incorrect matches (n) = the square of the correct matches (n**2)
   * and the 0th displayfeedback is correct and the next n are incorrect
   * feedback.  In export Samigo uses the incorrect feedback redundantly.
   *
   * For example, if there are 5 matches, there are 25 matches and mismatched,
   * 5 of which are correct and 20 of which are not, so there is redundancy in
   * Samigo.
   *
   * In non-Samigo matching, there may be more than one incorrect
   * feedback for a failed matching.
   *
   * @param list the list
   * @return a reassembled list of size n
   */
  private List reassembleIncorrectMatches(List list, int n)
  {
    // make sure we have a reasonable value
    if (n<0) n = -n;
    if (n==0) return list;

    // pad input list if too small or null
    if (list == null)
      list = new ArrayList();
    for (int i = 0; i < n && list.size()<n +1; i++)
    {
      list.add("");
    }

    // our output value
    List newList = new ArrayList();

    // move the last of the n entries (0-index) up to the front
    newList.add(list.get(n-1));

    // add the 2nd entry and so forth
    for (int i = 0; i < n-1 ; i++)
    {
      String s = (String) list.get(i);
      newList.add(s);
    }

    return newList;
  }

  /**
   * helper method
   * @param s
   * @return
   */
  private boolean notNullOrEmpty(String s)
  {
    return s != null && s.trim().length() > 0 ?
        true : false;
  }

  /**
   * Append "  - 2", "  - 3", etc. incrementing as you go.
   * @param title the original
   * @return the title with versioning appended
   */
  public String renameDuplicate(String title)
  {
    if (title==null) title = "";

    String rename = "";
    int index = title.lastIndexOf(VERSION_START);

    if (index>-1)//if is versioned
    {
      String mainPart = "";
      String versionPart = title.substring(index);
      if (index > 0)
      {
        mainPart = title.substring(0, index);
      }

      int nindex = index + VERSION_START.length();

      String version = title.substring(nindex);

      int versionNumber = 0;
      try
      {
        versionNumber = Integer.parseInt(version);
        if (versionNumber < 2) versionNumber = 2;
        versionPart = VERSION_START + (versionNumber + 1);

        rename = mainPart + versionPart;
      }
      catch (NumberFormatException ex)
      {
        rename = title + VERSION_START + "2";
      }
    }
    else
    {
      rename = title + VERSION_START + "2";
    }

    return rename;

  }

  /**
   * Primarily for testing purposes.
   * @return an overridden path if not null
   */
  public String getOverridePath()
  {
    return overridePath;
  }

  /**
   * Primarily for testing purposes.
   * @param overridePath an overriding path
   */
  public void setOverridePath(String overridePath)
  {
    this.overridePath = overridePath;
  }
  
  public void setUnzipLocation(String unzipLocation)
  {
    this.unzipLocation = unzipLocation;
  }
}