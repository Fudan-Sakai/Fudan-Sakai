/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/help/branches/sakai-2.8.1/help-component-shared/src/java/org/sakaiproject/component/app/help/model/CategoryBean.java $
 * $Id: CategoryBean.java 59674 2009-04-03 23:05:58Z arwhyte@umich.edu $
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

package org.sakaiproject.component.app.help.model;

import java.util.HashSet;
import java.util.Set;

import org.sakaiproject.api.app.help.Category;
import org.sakaiproject.component.cover.ServerConfigurationService;

/**
 * category bean
 * @version $Id: CategoryBean.java 59674 2009-04-03 23:05:58Z arwhyte@umich.edu $
 */
public class CategoryBean implements Category, Comparable
{
  private Long id;
  private String name;
  private Set resources = new HashSet();
  private Set categories = new HashSet();
  private Category parent;

  /**
   * get id
   * @return Returns the id.
   */
  public Long getId()
  {
    return id;
  }

  /**
   * set id
   * @param id The id to set.
   */
  public void setId(Long id)
  {
    this.id = id;
  }

  /**
   * @see org.sakaiproject.api.app.help.Category#getCategories()
   */
  public Set getCategories()
  {
    return categories;
  }

  /**
   * @see org.sakaiproject.api.app.help.Category#getName()
   */
  public String getName()
  {
    return name;
  }

  /**
   * @see org.sakaiproject.api.app.help.Category#getResources()
   */
  public Set getResources()
  {
    return resources;
  }

  /**
   * @see org.sakaiproject.api.app.help.Category#setCategories(java.util.Set)
   */
  public void setCategories(Set categories)
  {
    this.categories = categories;
  }

  /**
   * @see org.sakaiproject.api.help.Category#setName(java.lang.String)
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * @see org.sakaiproject.api.app.help.Category#setResources(java.util.Set)
   */
  public void setResources(Set resources)
  {
    this.resources = resources;
  }
  
  /**
   * @see org.sakaiproject.api.app.help.Category#getParent()
   */
  public Category getParent()
  {
    return parent;
  }
  public void setParent(Category parent)
  {   
    this.parent = parent;
  }
  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (!(obj instanceof CategoryBean)) return false;
    CategoryBean other = (CategoryBean) obj;
    return this.name.equals(other.name);
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    return name.hashCode();
  }

  public int compareTo(Object o)
  {
    CategoryBean cb = (CategoryBean) o;
    
    if (!"".equals(ServerConfigurationService.getString("help.location"))){
      return id.compareTo(cb.id);	
    }
    else{
      return name.compareTo(cb.name);
    }   
  }
}


