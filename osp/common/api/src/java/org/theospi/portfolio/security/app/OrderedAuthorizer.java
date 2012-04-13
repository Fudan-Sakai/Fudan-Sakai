/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/osp/branches/sakai-2.8.1/common/api/src/java/org/theospi/portfolio/security/app/OrderedAuthorizer.java $
* $Id: OrderedAuthorizer.java 59678 2009-04-03 23:20:50Z arwhyte@umich.edu $
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
package org.theospi.portfolio.security.app;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Jul 21, 2005
 * Time: 11:02:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrderedAuthorizer implements Comparable {

   private ApplicationAuthorizer authorizer;
   private int order = Integer.MAX_VALUE;

   public ApplicationAuthorizer getAuthorizer() {
      return authorizer;
   }

   public void setAuthorizer(ApplicationAuthorizer authorizer) {
      this.authorizer = authorizer;
   }

   public int getOrder() {
      return order;
   }

   public void setOrder(int order) {
      this.order = order;
   }

   public int compareTo(Object o) {
      OrderedAuthorizer other = (OrderedAuthorizer) o;

      return getOrder() - other.getOrder();
   }
}
