/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/test/org/sakaiproject/tool/assessment/jsf/SubBackingBean.java $
* $Id: SubBackingBean.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
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
package test.org.sakaiproject.tool.assessment.jsf;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
/**
 * <p> </p>
 * <p>Description: A Test Baking Bean</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organization: Sakai Project</p>
 * @author Ed Smiley
 * @version $id: $
 */

public class SubBackingBean implements Serializable
{
  private String title;
  private String name;
  private String address;
  private Date date;
  private String id;
  private HashMap map;

  public SubBackingBean()
  {
    title = "General Discord";
    name = "Name";
    address = "address";
    date = new Date();
    id = "666";
    map = new HashMap();
    map.put("author", "author");
    map.put("template", "template");
    map.put("author2", "author");
    map.put("template2", "template");
    map.put("author3", "author");
    map.put("template3", "template");
 }


 public String getTitle()
 {
   return title;
 }

 public void setTitle(String t)
 {
   title = t;
 }
 public Date getDate()
 {
   return date;
 }

 public void setDate(Date d)
 {
   date = d;
 }
 public String getName()
 {
   return name;
 }

 public void setName(String p)
 {
   name = p;
 }
 public String getId()
 {
   return id;
 }

 public void setId(String p)
 {
   id = p;
 }
  public String getAddress()
  {
    return address;
  }

  public void setAddress(String p)
  {
    address = p;
  }
  public java.util.HashMap getMap()
  {
    return map;
  }
  public void setMap(java.util.HashMap map)
  {
    this.map = map;
  }

}