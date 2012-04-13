/**
 * $Id: CheckAccountsJob.java 81430 2010-08-18 14:12:46Z david.horwitz@uct.ac.za $
 * $URL: https://source.sakaiproject.org/svn/reset-pass/tags/reset-pass-2.8.3/account-validator-impl/src/java/org/sakaiproject/accountvalidator/impl/jobs/CheckAccountsJob.java $
 *
 **************************************************************************
 * Copyright (c) 2008, 2009 The Sakai Foundation
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
 */
package org.sakaiproject.accountvalidator.impl.jobs;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.sakaiproject.accountvalidator.logic.ValidationLogic;
import org.sakaiproject.accountvalidator.model.ValidationAccount;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;

public class CheckAccountsJob implements Job {

	private static Log log = LogFactory.getLog(CheckAccountsJob.class);
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		userDirectoryService = uds;
	}
	
	private ValidationLogic validationLogic;
	public void setValidationLogic(ValidationLogic vl) {
		validationLogic = vl;
	}
	

	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			//get all Guest Users
			List<User> users = userDirectoryService.getUsers();
		
			for (int i =0; i < users.size(); i++ ) {
				User u = (User)users.get(i);
				if ("guest".equals(u.getType())) {
					if (!validationLogic.isAccountValidated(u.getReference())){
						log.info("found unvalidated account: " + u.getEid() + "(" + u.getId() + ")");
						ValidationAccount va = validationLogic.createValidationAccount(u.getReference(), ValidationAccount.ACCOUNT_STATUS_LEGACY);
						log.info("sent validation token of " + va.getValidationToken());
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
