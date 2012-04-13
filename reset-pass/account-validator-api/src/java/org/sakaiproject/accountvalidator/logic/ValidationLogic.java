/**
 * $Id: ValidationLogic.java 81430 2010-08-18 14:12:46Z david.horwitz@uct.ac.za $
 * $URL: https://source.sakaiproject.org/svn/reset-pass/tags/reset-pass-2.8.3/account-validator-api/src/java/org/sakaiproject/accountvalidator/logic/ValidationLogic.java $
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
package org.sakaiproject.accountvalidator.logic;


import java.util.List;

import org.sakaiproject.accountvalidator.model.ValidationAccount;
/**
 * The basic logic for handling Validation Accounts
 * @author dhorwitz
 *
 */
public interface ValidationLogic {

	/**
	 * Get an account by its ID
	 * @param id the account ID
	 * @return the account or null if none found
	 */
	public ValidationAccount getVaLidationAcountById(Long id);
	
	/**
	 *  Get an validation account by the token 
	 * @param the token (String)
	 * @return the account or null if none found
	 */
	public ValidationAccount getVaLidationAcountBytoken(String token);
	
	/**
	 * get a Validation account for a specific user
	 * @param userId
	 * @return the account or null if none found
	 */
	public ValidationAccount getVaLidationAcountByUserId(String userId);
	
	/**
	 * Find if an account has been validated
	 * @param userId
	 * @return true if the account is currently validated
	 */
	public boolean isAccountValidated(String userId);
	
	/**
	 * Create a new validation request for a user
	 * @param UserId
	 * @return
	 */
	public ValidationAccount createValidationAccount(String UserId);
	
	/**
	 *  Create a new Validation account
	 * @param UserId
	 * @param newAccount is this a new user
	 * @return
	 */
	public ValidationAccount createValidationAccount(String UserId, boolean newAccount);
	
	/**
	 * Create a validation token for an account of a given status
	 * @param UserId
	 * @param accountStatus
	 * @return
	 */
	public ValidationAccount createValidationAccount(String UserId, Integer accountStatus);
	
	/**
	 * Merge 2 accounts the memberships of the first will be moved to the second
	 * @param oldUserReference the old account refence
	 * @param NewUserReference the new account reference
	 * @throws ValidationException 
	 */
	public void mergeAccounts(String oldUserReference, String newUserReference) throws ValidationException;
	
	/**
	 * Delete Validation account 
	 * @param toDelete
	 */
	public void deleteValidationAccount(ValidationAccount toDelete);
	
	/**
	 * Save a validation account
	 * @param toSave
	 */
	public void save(ValidationAccount toSave);
	
	
	/**
	 * Retrieve a list of accounts by a given status
	 * @param status
	 * @return a List of ValidationAccounts or an empty List if none found
	 */
	public List<ValidationAccount> getValidationAccountsByStatus(Integer status);
	
	/**
	 * Resend the validation to the given user
	 * @param token
	 */
	public void resendValidation(String token);
}
