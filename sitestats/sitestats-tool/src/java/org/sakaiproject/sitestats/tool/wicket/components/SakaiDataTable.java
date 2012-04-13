/**
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-tool/src/java/org/sakaiproject/sitestats/tool/wicket/components/SakaiDataTable.java $
 * $Id: SakaiDataTable.java 72172 2009-09-23 00:48:53Z arwhyte@umich.edu $
 *
 * Copyright (c) 2006-2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.sitestats.tool.wicket.components;

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.ResourceModel;

public class SakaiDataTable extends AjaxFallbackDefaultDataTable {
	private static final long	serialVersionUID	= 1L;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param columns
	 *            list of columns
	 * @param dataProvider
	 *            data provider
	 * @param rowsPerPage
	 *            number of rows per page
	 */
	public SakaiDataTable(String id, final List/* <IColumn> */columns,
			ISortableDataProvider dataProvider,
			boolean pageable)
	{
		this(id, (IColumn[])columns.toArray(new IColumn[columns.size()]), dataProvider, 20, pageable);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param columns
	 *            array of columns
	 * @param dataProvider
	 *            data provider
	 * @param rowsPerPage
	 *            number of rows per page
	 */
	public SakaiDataTable(String id, final IColumn[] columns, ISortableDataProvider dataProvider,
			int rowsPerPage, boolean pageable)
	{
		super(id, columns, dataProvider, rowsPerPage);
		((RepeatingView) get("topToolbars")).removeAll();
		((RepeatingView) get("bottomToolbars")).removeAll();
		
		if(pageable) {
			addTopToolbar(new SakaiNavigationToolBar(this));
		}
		addTopToolbar(new HeadersToolbar(this, dataProvider));
		addBottomToolbar(new NoRecordsToolbar(this, new ResourceModel("no_data")));
	}
}
