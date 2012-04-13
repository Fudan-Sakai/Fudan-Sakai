/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-api/api/src/java/org/sakaiproject/metaobj/shared/mgt/ReferenceHolder.java $
 * $Id: ReferenceHolder.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

package org.sakaiproject.metaobj.shared.mgt;

import java.io.IOException;
import java.io.Serializable;

import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.cover.EntityManager;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Nov 15, 2005
 * Time: 3:13:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceHolder implements Serializable {

   private transient Reference base;

   public ReferenceHolder() {
   }

   public ReferenceHolder(Reference base) {
      this.base = base;
   }

   public Reference getBase() {
      return base;
   }

   public void setBase(Reference base) {
      this.base = base;
   }

   private void writeObject(java.io.ObjectOutputStream out)
         throws IOException {
      out.writeObject(base.getReference());
   }

   private void readObject(java.io.ObjectInputStream in)
         throws IOException, ClassNotFoundException {
      String ref = (String) in.readObject();
      setBase(EntityManager.newReference(ref));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof ReferenceHolder)) {
         return false;
      }

      final ReferenceHolder referenceHolder = (ReferenceHolder) o;

      if (base != null ? !base.getReference().equals(referenceHolder.base.getReference()) : referenceHolder.base != null) {
         return false;
      }

      return true;
   }

   public int hashCode() {
      return (base != null ? base.hashCode() : 0);
   }

}
