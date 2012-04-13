/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-hibernate/src/java/org/sakaiproject/tool/assessment/data/dao/questionpool/QuestionPoolItemData.java $
 * $Id: QuestionPoolItemData.java 92455 2011-04-29 21:39:10Z ktsao@stanford.edu $
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

package org.sakaiproject.tool.assessment.data.dao.questionpool;

import java.io.Serializable;

import org.sakaiproject.tool.assessment.data.dao.assessment.ItemData;
import org.sakaiproject.tool.assessment.data.ifc.questionpool.QuestionPoolItemIfc;
/**
 *
 * @author $author$
 * @version $Id: QuestionPoolItemData.java 92455 2011-04-29 21:39:10Z ktsao@stanford.edu $
 */
public class QuestionPoolItemData
    implements Serializable, QuestionPoolItemIfc 
{
  /** Use serialVersionUID for interoperability. */
  private final static long serialVersionUID = 9180085666292824370L;

  private Long questionPoolId;
  private String itemId;
  private ItemData itemData; //<-- is the item
    //private QuestionPool questionPool;

  public QuestionPoolItemData(){
  }

  public QuestionPoolItemData(Long questionPoolId, String itemId){
    this.questionPoolId = questionPoolId;
    this.itemId = itemId;
  }

  public QuestionPoolItemData(Long questionPoolId, String itemId, ItemData itemData){
    this.questionPoolId = questionPoolId;
    this.itemId = itemId;
    this.itemData = itemData;
  }

  public QuestionPoolItemData(ItemData itemData, QuestionPoolData questionPoolData){
    this.itemData = itemData;
    //this.questionPool = questionPool;
    //setQuestionPoolId(questionPoolProperties.getId());
    setItemId(itemData.getItemIdString());
    setQuestionPoolId(questionPoolData.getQuestionPoolId());
  }

  public Long getQuestionPoolId()
  {
    return questionPoolId;
  }

  public void setQuestionPoolId(Long questionPoolId)
  {
    this.questionPoolId = questionPoolId;
  }

  public String getItemId()
  {
    return itemId;
  }

  public void setItemId(String itemId)
  {
    this.itemId = itemId;
  }

  public boolean equals(Object questionPoolItem){
    boolean returnValue = false;
    if (this == questionPoolItem)
      returnValue = true;
    if (questionPoolItem != null && questionPoolItem.getClass()==this.getClass()){
      QuestionPoolItemData qpi = (QuestionPoolItemData)questionPoolItem;
      if ((this.getItemId()).equals(qpi.getItemId())
          && (this.getQuestionPoolId()).equals(qpi.getQuestionPoolId()))
        returnValue = true;
    }
    return returnValue;
  }

  public int hashCode(){
    String s = this.itemId+":"+(this.questionPoolId).toString();
    return (s.hashCode());
  }
}
