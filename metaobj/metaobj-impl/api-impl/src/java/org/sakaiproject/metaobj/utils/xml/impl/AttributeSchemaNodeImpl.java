/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-impl/api-impl/src/java/org/sakaiproject/metaobj/utils/xml/impl/AttributeSchemaNodeImpl.java $
 * $Id: AttributeSchemaNodeImpl.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

package org.sakaiproject.metaobj.utils.xml.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.sakaiproject.metaobj.utils.xml.SchemaInvalidException;

public class AttributeSchemaNodeImpl extends SimpleSchemaNodeImpl {
   protected final Log logger = LogFactory.getLog(getClass());
   private boolean required = false;

   public AttributeSchemaNodeImpl(Element schemaElement, SchemaNodeImpl.GlobalMaps globalMaps,
                                  boolean isAttribute) throws SchemaInvalidException {
      super(schemaElement, globalMaps, isAttribute);
      required = "required".equals(getSchemaElement().getAttributeValue("use"));
   }

   protected void initSchemaElement() {
      super.initSchemaElement();
   }

   /**
    * Retuns the max number of times the element
    * defined by this node can occur in its parent.
    * The root schema will always return 1 here.
    *
    * @return
    */
   public int getMaxOccurs() {
      return 1;
   }

   /**
    * Returns the min number of times the element
    * defined by this node can occur in its parent.
    * The root schema will always return 1 here.
    *
    * @return
    */
   public int getMinOccurs() {
      return required ? 1 : 0;
   }
}
