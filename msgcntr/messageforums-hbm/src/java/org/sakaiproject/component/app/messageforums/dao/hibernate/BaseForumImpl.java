/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msgcntr/trunk/messageforums-hbm/src/java/org/sakaiproject/component/app/messageforums/dao/hibernate/BaseForumImpl.java $
 * $Id: BaseForumImpl.java 9227 2006-05-15 15:02:42Z cwen@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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
package org.sakaiproject.component.app.messageforums.dao.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.app.messageforums.Area;
import org.sakaiproject.api.app.messageforums.Attachment;
import org.sakaiproject.api.app.messageforums.BaseForum;
import org.sakaiproject.api.app.messageforums.DBMembershipItem;
import org.sakaiproject.api.app.messageforums.Topic;
import org.sakaiproject.component.app.messageforums.dao.hibernate.util.comparator.AttachmentByCreatedDateDesc;
import org.sakaiproject.component.app.messageforums.dao.hibernate.util.comparator.TopicBySortIndexAscAndCreatedDateDesc;
 
public class BaseForumImpl extends MutableEntityImpl implements BaseForum {

    private static final Log LOG = LogFactory.getLog(BaseForumImpl.class);

    private String title;
    private String shortDescription;
    private String extendedDescription;
    private String typeUuid;
    private SortedSet attachmentsSet;// = new HashSet();
    private SortedSet topicsSet;// = new HashSet();
    private Set membershipItemSet;
    private Area area;
    private Integer sortIndex; 
    private Boolean moderated;
    
    public Set getAttachmentsSet() {
        return attachmentsSet;
    }

    public void setAttachmentsSet(SortedSet attachmentsSet) {
        this.attachmentsSet = attachmentsSet;
    }
    
    public List getAttachments()
    {
      return Util.setToList(attachmentsSet);
    }

    public void setAttachments(List attachments)
    {
      this.attachmentsSet = new TreeSet(new AttachmentByCreatedDateDesc());
      this.attachmentsSet.addAll(attachments);
    }

    public String getExtendedDescription() {
        return extendedDescription;
    }

    public void setExtendedDescription(String extendedDescription) {
        this.extendedDescription = extendedDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List getTopics() {
        List topics =  Util.setToList(topicsSet);
        boolean isUnsorted = true;
        for(Iterator i = topics.iterator(); i.hasNext(); ) {
           Topic topic = (Topic)i.next();
           if(topic.getSortIndex().intValue() != 0)
              isUnsorted = false;
        }
        if(isUnsorted) {
           int x = 1;
           for(Iterator i = topics.iterator(); i.hasNext(); x++) {
              Topic topic = (Topic)i.next();
              topic.setSortIndex(Integer.valueOf(x));
           }
        }
        return topics;
    }

    public void setTopics(List topics) {
        this.topicsSet = new TreeSet(new TopicBySortIndexAscAndCreatedDateDesc());
        this.topicsSet.addAll(topics);
    }

    public Set getTopicsSet() {
        return topicsSet;
    }

    public void setTopicsSet(SortedSet topicsSet) {
        this.topicsSet = topicsSet;
    }
    
    public Set getMembershipItemSet() {
  		return membershipItemSet;
  	}

  	public void setMembershipItemSet(Set membershipItemSet) {
  		this.membershipItemSet = membershipItemSet;
  	}

    public String getTypeUuid() {
        return typeUuid;
    }

    public void setTypeUuid(String typeUuid) {
        this.typeUuid = typeUuid;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    
    public Boolean getModerated() {
        return moderated;
    }

    public void setModerated(Boolean moderated) {
        this.moderated = moderated;
    }

    public String toString() {
    	return "Forum/" + id;
        //return "Forum.id:" + id;
    }
    
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof BaseForum) {
            return getId().equals(((BaseForum)obj).getId());
        }
        return false;
    }

    // needs a better impl
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    ////////////////////////////////////////////////////////////////////////
    // helper methods for collections
    ////////////////////////////////////////////////////////////////////////
    
    public void addTopic(Topic topic) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("addTopic(topic " + topic + ")");
        }
        
        if (topic == null) {
            throw new IllegalArgumentException("topic == null");
        }
        
        if (topicsSet == null) {
            topicsSet = new TreeSet(new TopicBySortIndexAscAndCreatedDateDesc());
        }
        topic.setBaseForum(this);
        topicsSet.add(topic);
    }

    public void removeTopic(Topic topic) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeTopic(topic " + topic + ")");
        }
        
        if (topic == null) {
            throw new IllegalArgumentException("Illegal topic argument passed!");
        }
        
        topic.setOpenForum(null);
        topic.setPrivateForum(null);
        topic.setBaseForum(null);
        topicsSet.remove(topic);
    }
       
    public void addAttachment(Attachment attachment) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("addAttachment(Attachment " + attachment + ")");
        }
        
        if (attachment == null) {
            throw new IllegalArgumentException("attachment == null");
        }
        
        if (attachmentsSet == null) {
            attachmentsSet = new TreeSet(new AttachmentByCreatedDateDesc());
        }
        attachment.setForum(this);
        attachmentsSet.add(attachment);
    }

    public void removeAttachment(Attachment attachment) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeAttachment(Attachment " + attachment + ")");
        }
        
        if (attachment == null) {
            throw new IllegalArgumentException("Illegal attachment argument passed!");
        }
        
        attachment.setForum(null);
        attachmentsSet.remove(attachment);
    }
    
    public void addMembershipItem(DBMembershipItem item) {
      if (LOG.isDebugEnabled()) {
          LOG.debug("addMembershipItem(item " + item + ")");
      }
      
      if (item == null) {
          throw new IllegalArgumentException("item == null");
      }
      
      if (membershipItemSet == null) {
      	membershipItemSet = new HashSet();
      }          
      membershipItemSet.add(item);
  }

    public void removeMembershipItem(DBMembershipItem item) {
      if (LOG.isDebugEnabled()) {
          LOG.debug("removeMembershipItem(item " + item + ")");
      }
      
      if (item == null) {
          throw new IllegalArgumentException("Illegal level argument passed!");
      }
          
      membershipItemSet.remove(item);
    }
    
}
