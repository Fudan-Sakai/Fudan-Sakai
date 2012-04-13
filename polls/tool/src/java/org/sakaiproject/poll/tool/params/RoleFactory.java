/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/polls/tags/polls-1.4.3/tool/src/java/org/sakaiproject/poll/tool/params/RoleFactory.java $
 * $Id: RoleFactory.java 66331 2009-09-08 11:31:32Z david.horwitz@uct.ac.za $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007 Sakai Foundation
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

package org.sakaiproject.poll.tool.params;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.cover.AuthzGroupService;
import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.model.PollRolePerms;



public class RoleFactory {

	private static final Log LOG = LogFactory.getLog(RoleFactory.class);
	public Map<String, PollRolePerms> getRoles()
	{
		LOG.debug("Getting permRoles");
		Map<String, PollRolePerms> perms = new HashMap<String, PollRolePerms>();
		try {
			AuthzGroup group = AuthzGroupService.getAuthzGroup("/site/" + ToolManager.getCurrentPlacement().getContext());
			Set<Role> roles = group.getRoles();
			Iterator<Role> i = roles.iterator();
			
			while (i.hasNext())
			{
				Role role = (Role)i.next();
				String name = role.getId();
				LOG.debug("Adding element for " + name); 
				perms.put(name, new PollRolePerms(name, 
						role.isAllowed(PollListManager.PERMISSION_VOTE),
						role.isAllowed(PollListManager.PERMISSION_ADD),
						role.isAllowed(PollListManager.PERMISSION_DELETE_OWN),
						role.isAllowed(PollListManager.PERMISSION_DELETE_ANY),
						role.isAllowed(PollListManager.PERMISSION_EDIT_OWN),
						role.isAllowed(PollListManager.PERMISSION_EDIT_ANY)
						));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return perms;
	}
}
