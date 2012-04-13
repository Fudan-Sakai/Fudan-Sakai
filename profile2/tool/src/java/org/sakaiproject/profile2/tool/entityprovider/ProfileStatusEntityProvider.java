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

package org.sakaiproject.profile2.tool.entityprovider;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sakaiproject.entitybroker.EntityReference;
import org.sakaiproject.entitybroker.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.AutoRegisterEntityProvider;
import org.sakaiproject.entitybroker.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybroker.entityprovider.extension.Formats;
import org.sakaiproject.entitybroker.entityprovider.search.Search;
import org.sakaiproject.entitybroker.exception.EntityException;
import org.sakaiproject.entitybroker.exception.EntityNotFoundException;
import org.sakaiproject.entitybroker.util.AbstractEntityProvider;
import org.sakaiproject.profile2.logic.ProfileLogic;
import org.sakaiproject.profile2.logic.ProfileStatusLogic;
import org.sakaiproject.profile2.logic.SakaiProxy;
import org.sakaiproject.profile2.model.ProfileStatus;

/**
 * This is the entity provider for a user's profile status.
 * 
 * @author Steve Swinsburg (s.swinsburg@lancaster.ac.uk)
 *
 */
public class ProfileStatusEntityProvider extends AbstractEntityProvider implements CoreEntityProvider, AutoRegisterEntityProvider, RESTful {
	
	public final static String ENTITY_PREFIX = "profile-status";
	
	public String getEntityPrefix() {
		return ENTITY_PREFIX;
	}
		
	public boolean entityExists(String eid) {
		return true;
	}

	public Object getSampleEntity() {
		return new ProfileStatus();
	}
	
	public Object getEntity(EntityReference ref) {
	
		if(!sakaiProxy.isLoggedIn()) {
			throw new SecurityException("You must be logged in to get a user's status.");
		}
		
		//note, returning null = 404 thrown by EB.
		
		//convert input to uuid
		String uuid = sakaiProxy.ensureUuid(ref.getId());
		if(StringUtils.isBlank(uuid)) {
			return null;
		}
		
		return statusLogic.getUserStatus(uuid);
	}
	
	
	public void updateEntity(EntityReference ref, Object entity, Map<String, Object> params) {
	
		if(!sakaiProxy.isLoggedIn()) {
			throw new SecurityException("You must be logged in to update your status.");
		}
		
		String userId = ref.getId();
		if (StringUtils.isBlank(userId)) {
			throw new IllegalArgumentException("Cannot update, No userId in provided reference: " + ref);
		}
		
		if (entity.getClass().isAssignableFrom(ProfileStatus.class)) {
			ProfileStatus status = (ProfileStatus) entity;
			statusLogic.setUserStatus(status);
		} else {
			 throw new IllegalArgumentException("Invalid entity for update, must be ProfileStatus object");
		}
	
	}
	
	
	public String createEntity(EntityReference ref, Object entity, Map<String, Object> params) {
				
		//reference will be the userUuid, which comes from the ProfileStatus obj passed in
		String userUuid = null;

		if (entity.getClass().isAssignableFrom(ProfileStatus.class)) {
			ProfileStatus status = (ProfileStatus) entity;
			
			if(statusLogic.setUserStatus(status)) {
				userUuid = status.getUserUuid();
			}
			if(userUuid == null) {
				throw new EntityException("Could not create entity", ref.getReference());
			}
		} else {
			 throw new IllegalArgumentException("Invalid entity for create, must be ProfileStatus object");
		}
		return userUuid;
	}
	
	
	public void deleteEntity(EntityReference ref, Map<String, Object> params) {
		// TODO Auto-generated method stub
	}

	public List<?> getEntities(EntityReference ref, Search search) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getHandledOutputFormats() {
		return new String[] {Formats.XML, Formats.JSON};
	}

	public String[] getHandledInputFormats() {
		return new String[] {Formats.XML, Formats.JSON, Formats.HTML};
	}
	
	private SakaiProxy sakaiProxy;
	public void setSakaiProxy(SakaiProxy sakaiProxy) {
		this.sakaiProxy = sakaiProxy;
	}
	
	private ProfileStatusLogic statusLogic;
	public void setStatusLogic(ProfileStatusLogic statusLogic) {
		this.statusLogic = statusLogic;
	}
	
}
