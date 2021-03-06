/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/polls/tags/polls-1.4.3/tool/src/java/org/sakaiproject/poll/tool/params/OptionViewParameters.java $
 * $Id: OptionViewParameters.java 59679 2009-04-03 23:27:51Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008 The Sakai Foundation
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
package org.sakaiproject.poll.tool.params;

import uk.org.ponder.rsf.viewstate.SimpleViewParameters;

public class OptionViewParameters extends SimpleViewParameters {

		public String id;
		public String pollId;
		
		public OptionViewParameters() {
			
		}
		
		public OptionViewParameters(String viewId) {
			
		}
		
		public OptionViewParameters(String viewId, String id) {
			this.id=id;
			this.viewID = viewId;
		}

		public OptionViewParameters(String viewId, String id, String pollId) {
			this.id=id;
			this.viewID = viewId;
			this.pollId = pollId;
		}
		
}
