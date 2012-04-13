/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-hibernate/src/java/org/sakaiproject/tool/assessment/data/dao/grading/MediaData.java $
 * $Id: MediaData.java 11438 2006-06-30 20:06:03Z daisyf@stanford.edu $
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

package org.sakaiproject.tool.assessment.data.dao.assessment;

import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentAttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.AssessmentIfc;
import java.io.Serializable;
import java.util.Date;


public class AssessmentAttachment
    extends AttachmentData
    implements Serializable, AssessmentAttachmentIfc
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 5236325714234687092L;
private AssessmentIfc assessment;

  public AssessmentAttachment()  { }

  public AssessmentAttachment(Long attachmentId, AssessmentIfc assessment, String resourceId,
                   String filename, String mimeType,
                   Long fileSize, String description, String location,
                   Boolean isLink, Integer status, String createdBy, Date createdDate,
                   String lastModifiedBy, Date lastModifiedDate){
    super(attachmentId, resourceId, AssessmentAttachmentIfc.ASSESSMENT_ATTACHMENT,
          filename, mimeType,
          fileSize, description, location, isLink, status,
          createdBy, createdDate, lastModifiedBy, lastModifiedDate);
    this.assessment = assessment;
  }

  public AssessmentAttachment(Long attachmentId, String resourceId,
                   String filename, String mimeType,
                   Long fileSize, String description, String location,
                   Boolean isLink, Integer status, String createdBy, Date createdDate,
                   String lastModifiedBy, Date lastModifiedDate){
    super(attachmentId, resourceId, AssessmentAttachmentIfc.ASSESSMENT_ATTACHMENT,
          filename, mimeType,
          fileSize, description, location, isLink, status,
          createdBy, createdDate, lastModifiedBy, lastModifiedDate);
  }

  public AssessmentIfc getAssessment() {
    return assessment;
  }

  public void setAssessment(AssessmentIfc assessment) {
    this.assessment = assessment;
  }

  public Long getAttachmentType() {
    return AssessmentAttachmentIfc.ASSESSMENT_ATTACHMENT;
  }

  public void setAttachmentType(Long attachmentType) {
  }


}
