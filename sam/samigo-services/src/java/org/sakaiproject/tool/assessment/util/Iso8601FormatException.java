/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/trunk/component/src/java/org/sakaiproject/tool/assessment/util/Iso8601FormatException.java $
 * $Id: Iso8601FormatException.java 9273 2006-05-10 22:34:28Z daisyf@stanford.edu $
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


package org.sakaiproject.tool.assessment.util;


/**
 * <p>Title: NavigoProject.org</p>
 * <p>Description: OKI based implementation</p>
 * <p>Copyright: Copyright 2003 Trustees of Indiana University</p>
 * <p>Company: </p>
 * @author <a href="mailto:lance@indiana.edu">Lance Speelmon</a>
 * @version $Id: Iso8601FormatException.java 9273 2006-05-10 22:34:28Z daisyf@stanford.edu $
 */
public class Iso8601FormatException
  extends FormatException
{
  /**
   * Creates a new Iso8601FormatException object.
   *
   * @param message DOCUMENTATION PENDING
   */
  public Iso8601FormatException(String message)
  {
    super(message);
  }

  /**
   * Creates a new Iso8601FormatException object.
   *
   * @param message DOCUMENTATION PENDING
   * @param cause DOCUMENTATION PENDING
   */
  public Iso8601FormatException(String message, Throwable cause)
  {
    super(message, cause);
  }

  /**
   * Creates a new Iso8601FormatException object.
   *
   * @param cause DOCUMENTATION PENDING
   */
  public Iso8601FormatException(Throwable cause)
  {
    super(cause);
  }
}
