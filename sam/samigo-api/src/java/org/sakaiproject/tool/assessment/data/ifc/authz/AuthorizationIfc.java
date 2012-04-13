/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-api/src/java/org/sakaiproject/tool/assessment/data/ifc/authz/AuthorizationIfc.java $
 * $Id: AuthorizationIfc.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
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



package org.sakaiproject.tool.assessment.data.ifc.authz;

import java.util.Date;

public  interface AuthorizationIfc
    extends java.io.Serializable
{

  String getAgentIdString();

  void setAgentIdString(String id);

  String getFunctionId();

  void setFunctionId(String id);

  String getQualifierId();

  void setQualifierId(String id);

  Date getAuthorizationEffectiveDate();

  void setAuthorizationEffectiveDate(Date cal);

  Date getAuthorizationExpirationDate();

  void setAuthorizationExpirationDate(Date cal);

  String getLastModifiedBy();

  void setLastModifiedBy(String id);

  Date getLastModifiedDate();

  void setLastModifiedDate(Date cal);

  Boolean getIsExplicitBoolean();

  void setIsExplicitBoolean(Boolean type);

  Boolean getIsActiveNowBoolean();

}
