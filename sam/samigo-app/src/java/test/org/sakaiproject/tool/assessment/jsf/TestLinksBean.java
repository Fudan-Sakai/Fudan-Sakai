/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/test/org/sakaiproject/tool/assessment/jsf/TestLinksBean.java $
* $Id: TestLinksBean.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
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
import java.util.ArrayList;

/**
 * <p> </p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2004 Sakai</p>
 * <p> </p>
 * @author Ed Smiley esmiley@stanford.edu
 * @version $Id: TestLinksBean.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 */

public class TestLinksBean implements Serializable
{
  public TestLinksBean()
  {
    String[] actions = { "select", "author", "template" };
    links = new ArrayList();
    for (int i = 0; i < actions.length; i++)
    {
      TestLink link = new TestLink();
      link.setAction(actions[i]);
      link.setText("Link to " + actions[i]);
      links.add(link);
    }

  }
  private ArrayList links;
  public ArrayList getLinks()
  {
    return links;
  }
  public void setLinks(ArrayList links)
  {
    this.links = links;
  }
}