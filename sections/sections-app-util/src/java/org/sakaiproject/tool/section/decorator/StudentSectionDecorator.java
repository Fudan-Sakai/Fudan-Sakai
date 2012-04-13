/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sections/branches/sakai-2.8.1/sections-app-util/src/java/org/sakaiproject/tool/section/decorator/StudentSectionDecorator.java $
 * $Id: StudentSectionDecorator.java 59686 2009-04-03 23:37:55Z arwhyte@umich.edu $
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
package org.sakaiproject.tool.section.decorator;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.section.api.coursemanagement.CourseSection;
import org.sakaiproject.tool.section.jsf.JsfUtil;

/**
 * Decorates a CourseSection for use in the students' UI.
 * 
 * @author <a href="mailto:jholtzman@berkeley.edu">Josh Holtzman</a>
 *
 */
public class StudentSectionDecorator extends SectionDecorator
	implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(StudentSectionDecorator.class);

	protected boolean full;
	protected boolean joinable;
	protected boolean switchable;
	protected boolean member;
		
	public StudentSectionDecorator(CourseSection courseSection, String categoryForDisplay,
			List<String> instructorNames, int totalEnrollments, boolean member,
			boolean memberOtherSection, boolean showNegativeSpots) {
		super(courseSection, categoryForDisplay, instructorNames, totalEnrollments, showNegativeSpots);
		this.member = member;
		if( ! this.member && "0".equals(this.spotsAvailable)) {
			this.full = true;
		}
		if( ! this.member && ! this.full) {
			this.switchable = memberOtherSection;
			this.joinable = ! memberOtherSection;
		}
	}

	public StudentSectionDecorator() {
		// Needed for serialization
	}
	
	public List getInstructorNames() {
		return instructorNames;
	}
	
	public boolean isFull() {
		return full;
	}

	public boolean isJoinable() {
		return joinable;
	}

	public boolean isMember() {
		return member;
	}

	public boolean isSwitchable() {
		return switchable;
	}
}
