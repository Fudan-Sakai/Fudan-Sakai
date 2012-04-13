/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-api/api/src/java/org/sakaiproject/metaobj/shared/model/InvalidUploadException.java $
 * $Id: InvalidUploadException.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

package org.sakaiproject.metaobj.shared.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InvalidUploadException extends OspException {
   protected final transient Log logger = LogFactory.getLog(getClass());

   private String fieldName;

   /**
    *
    */
   public InvalidUploadException(String fieldName) {
      this.fieldName = fieldName;
   }

   /**
    * @param cause
    */
   public InvalidUploadException(Throwable cause, String fieldName) {
      super(cause);
      this.fieldName = fieldName;
   }

   /**
    * @param message
    */
   public InvalidUploadException(String message, String fieldName) {
      super(message);
      this.fieldName = fieldName;
   }

   /**
    * @param message
    * @param cause
    */
   public InvalidUploadException(String message, Throwable cause, String fieldName) {
      super(message, cause);
      this.fieldName = fieldName;
   }

   public String getFieldName() {
      return fieldName;
   }

   public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
   }
}
