/**
 * $Id: ValidationClaim.java 81430 2010-08-18 14:12:46Z david.horwitz@uct.ac.za $
 * $URL: https://source.sakaiproject.org/svn/reset-pass/tags/reset-pass-2.8.3/account-validator-api/src/java/org/sakaiproject/accountvalidator/model/ValidationClaim.java $
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
package org.sakaiproject.accountvalidator.model;

public class ValidationClaim {

	private String userId;
	private String valiDationToken;
	private String password1;
	private String password2;
	private String userEid;
	
	public String getUserEid() {
		return userEid;
	}
	public void setUserEid(String userEid) {
		this.userEid = userEid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getValidationToken() {
		return valiDationToken;
	}
	public void setValidationToken(String valiDationToken) {
		this.valiDationToken = valiDationToken;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	
}
