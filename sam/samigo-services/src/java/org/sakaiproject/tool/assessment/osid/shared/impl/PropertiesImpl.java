/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/component/src/java/org/sakaiproject/tool/assessment/osid/shared/impl/PropertiesImpl.java $
 * $Id: PropertiesImpl.java 9276 2006-05-10 23:04:20Z daisyf@stanford.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2008 The Sakai Foundation
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

package org.sakaiproject.tool.assessment.osid.shared.impl;

import org.osid.shared.Properties;
import org.osid.shared.ObjectIterator;
import org.osid.shared.Type;

import java.io.Serializable;
import java.util.HashMap;

public class PropertiesImpl implements Properties
{
  HashMap hashmap = null;

  public PropertiesImpl(HashMap map)
  {
    hashmap = map;
  }

  public Type getType() throws org.osid.shared.SharedException
  {
    return null;
  }

  public Serializable getProperty(java.io.Serializable key)
        throws org.osid.shared.SharedException
  {
    return (Serializable) hashmap.get(key);
  }

  public ObjectIterator getKeys() throws org.osid.shared.SharedException
  {
    return new ObjectIteratorImpl(hashmap.keySet().iterator());
  }
}
