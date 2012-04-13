/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/ui/bean/questionpool/QuestionPoolDataModel.java $
 * $Id: QuestionPoolDataModel.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
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



package org.sakaiproject.tool.assessment.ui.bean.questionpool;

import javax.faces.model.DataModel;
import javax.faces.model.DataModelListener;

import org.sakaiproject.tool.assessment.data.model.Tree;
import org.sakaiproject.tool.assessment.facade.QuestionPoolFacade;



/**
 * This Data Model contains the tree.
 *
 * $Id: QuestionPoolDataModel.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 */
public class QuestionPoolDataModel extends DataModel
{
          // for JSF
  private Tree tree;
  private DataModel model;



  /**
   * Creates a new QuestionPoolDatModel object.
   */
  public QuestionPoolDataModel(Tree tree, DataModel model)
  {
     this.model = model;
     this.tree=tree;
    //buildTree();
  }

  public Object getRowData(){
    try{
        tree.setCurrentId(  ((QuestionPoolFacade) (model.getRowData())).getQuestionPoolId());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
	return model.getRowData();
  }


  public boolean isRowAvailable() {
	return model.isRowAvailable();
  }

  public int getRowCount() {
	return model.getRowCount();
  }

  public int getRowIndex() {
	return model.getRowIndex();
  }

  public void setRowIndex(int rowIndex) {
	model.setRowIndex(rowIndex);
  }

  public Object getWrappedData() {
	return model.getWrappedData();
  }

  public void setWrappedData(Object data) {
	model.setWrappedData(data);
  }

  public void addDataModelListener(DataModelListener listener) {
	model.addDataModelListener(listener);
  }

  public DataModelListener[] getDataModelListeners(){
	return model.getDataModelListeners();
  }

  public void removeDataModelListener(DataModelListener listener) {
	model.removeDataModelListener(listener);

  }

}
