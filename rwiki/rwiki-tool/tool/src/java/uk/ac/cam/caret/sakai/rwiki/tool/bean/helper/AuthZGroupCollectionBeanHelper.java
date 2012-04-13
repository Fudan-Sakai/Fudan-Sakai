/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/rwiki/branches/sakai-2.8.1/rwiki-tool/tool/src/java/uk/ac/cam/caret/sakai/rwiki/tool/bean/helper/AuthZGroupCollectionBeanHelper.java $
 * $Id: AuthZGroupCollectionBeanHelper.java 20354 2007-01-17 10:30:57Z ian@caret.cam.ac.uk $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
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

package uk.ac.cam.caret.sakai.rwiki.tool.bean.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.cover.EntityManager;

import uk.ac.cam.caret.sakai.rwiki.service.api.RWikiObjectService;
import uk.ac.cam.caret.sakai.rwiki.service.api.model.RWikiObject;
import uk.ac.cam.caret.sakai.rwiki.tool.bean.AuthZGroupBean;
import uk.ac.cam.caret.sakai.rwiki.tool.bean.AuthZGroupCollectionBean;
import uk.ac.cam.caret.sakai.rwiki.tool.bean.ViewBean;

public class AuthZGroupCollectionBeanHelper
{
	private static Log log = LogFactory
			.getLog(AuthZGroupCollectionBeanHelper.class);

	public static AuthZGroupCollectionBean createAuthZCollectionBean(
			AuthzGroupService realmService, RWikiObject currentRWikiObject,
			ViewBean viewBean, RWikiObjectService objectService)
	{
		Entity entity = objectService.getEntity(currentRWikiObject);

		Collection groupRefs = objectService.getEntityAuthzGroups(EntityManager
				.newReference(entity.getReference()), null);
		// TODO - we might want to deal with this null userId parameter -ggolden
		// TODO: ieb comment, it is trying to get a generic set, the API indicates 
		// a null user id will deliver a generic set.

		List groups = new ArrayList(groupRefs.size());

		for (Iterator it = groupRefs.iterator(); it.hasNext();)
		{
			String groupRef = (String) it.next();
			AuthZGroupBean ab = new AuthZGroupBean(viewBean.getPageName(),
					viewBean.getLocalSpace());
			ab.setRealmId(groupRef);
			try
			{

				AuthzGroup azg = realmService.getAuthzGroup(groupRef);
				ab.setCurrentRealm(azg);
				ab.setRealmId(azg.getId());
				log.info("Got Id " + groupRef);
			}
			catch (GroupNotDefinedException e)
			{
				log.info("Id Unused: " + groupRef
						+ " doesnt exist for this user . ");
			}

			groups.add(ab);
		}

		AuthZGroupCollectionBean collectionBean = new AuthZGroupCollectionBean();
		collectionBean.setCurrentRealms(groups);
		collectionBean.setVb(viewBean);
		return collectionBean;
	}

}
