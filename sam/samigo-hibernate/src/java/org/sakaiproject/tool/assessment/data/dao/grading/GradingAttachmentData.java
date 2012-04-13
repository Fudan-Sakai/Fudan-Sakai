/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/samigo-hibernate/src/java/org/sakaiproject/tool/assessment/data/dao/grading/MediaData.java $
 * $Id: GradingAttachmentData.java 11438 2006-06-30 20:06:03Z daisyf@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.tool.assessment.data.dao.grading;

import org.sakaiproject.tool.assessment.data.ifc.assessment.AttachmentIfc;
import org.sakaiproject.tool.assessment.data.ifc.assessment.ItemDataIfc;
import java.io.Serializable;
import java.util.Date;


public class GradingAttachmentData	implements Serializable, AttachmentIfc
{
	private Long attachmentId;
	private String resourceId;
	private String filename;
	private String mimeType;
	private Long fileSize; // in kilobyte
	private String description;
	private String location;
	private Boolean isLink;
	private Integer status;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long attachmentType;

	public GradingAttachmentData()
	{
	}

	public GradingAttachmentData(String resourceId, 
			Long attachmentType, String filename, String mimeType,
			Long fileSize, String description, String location,
			Boolean isLink, Integer status, String createdBy, Date createdDate,
			String lastModifiedBy, Date lastModifiedDate){
		this.resourceId = resourceId;
		this.attachmentType = attachmentType;
		this.filename = filename;
		this.mimeType = mimeType;
		this.fileSize = fileSize;
		this.description = description;
		this.location = location;
		this.isLink = isLink;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	public GradingAttachmentData(Long attachmentId, String resourceId,
			Long attachmentType, String filename, String mimeType,
			Long fileSize, String description, String location,
			Boolean isLink, Integer status, String createdBy, Date createdDate,
			String lastModifiedBy, Date lastModifiedDate){
		this.attachmentId = attachmentId;
		this.resourceId = resourceId;
		this.attachmentType = attachmentType;
		this.filename = filename;
		this.mimeType = mimeType;
		this.fileSize = fileSize;
		this.description = description;
		this.location = location;
		this.isLink = isLink;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getAttachmentId()
	{
		return attachmentId;
	}

	public void setAttachmentId(Long attachmentId)
	{
		this.attachmentId = attachmentId;
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public String getMimeType()
	{
		return mimeType;
	}


	public Long getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(Long fileSize)
	{
		this.fileSize = fileSize;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String pdescription)
	{
		description = pdescription;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Boolean getIsLink()
	{
		return isLink;
	}

	public void setIsLink(Boolean isLink)
	{
		this.isLink = isLink;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Long attachmentType) {
	}
}
