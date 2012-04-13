/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/help/branches/sakai-2.8.1/help-component/src/java/org/sakaiproject/component/app/help/SizedList.java $
 * $Id: SizedList.java 59674 2009-04-03 23:05:58Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 The Sakai Foundation
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

package org.sakaiproject.component.app.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * size list
 * @version $Id: SizedList.java 59674 2009-04-03 23:05:58Z arwhyte@umich.edu $
 */
public class SizedList extends ArrayList
{
  private int size = -1;

  /**
   * constructor
   */
  public SizedList()
  {
    super();
  }

  /**
   * overloaded constructor
   * @param size
   */
  public SizedList(int size)
  {
    super();
    this.size = size;
  }

  /** 
   * @see java.util.Collection#add(java.lang.Object)
   */
  public boolean add(Object item)
  {
    if (this.contains(item))
    {
      this.remove(item);
    }
    super.add(0, item);
    if (this.size() > size)
    {
      this.remove(this.size() - 1);
    }
    return true;
  }

  /** 
   * @see java.util.Collection#addAll(java.util.Collection)
   */
  public boolean addAll(Collection c)
  {
    for (Iterator i = c.iterator(); i.hasNext();)
    {
      add(i.next());
    }
    return true;
  }

}


