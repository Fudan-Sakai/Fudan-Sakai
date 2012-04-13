package edu.indiana.lib.osid.base.repository.http;

/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/citations/branches/sakai-2.8.1/citations-osid/web2bridge/src/java/edu/indiana/lib/osid/base/repository/http/TypeIterator.java $
 * $Id: TypeIterator.java 59673 2009-04-03 23:02:03Z arwhyte@umich.edu $
 **********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2007, 2008 The Sakai Foundation
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
/**
 * @author Massachusetts Institute of Techbology, Sakai Software Development Team
 * @version
 */
public class TypeIterator extends edu.indiana.lib.osid.base.repository.TypeIterator
{
		private static org.apache.commons.logging.Log	_log = edu.indiana.lib.twinpeaks.util.LogUtils.getLog(TypeIterator.class);
		
    private java.util.Vector vector = new java.util.Vector();
    private int i = 0;

    public TypeIterator(java.util.Vector vector)
    throws org.osid.shared.SharedException
    {
        this.vector = vector;
    }

    public boolean hasNextType()
    throws org.osid.shared.SharedException
    {
        return i < vector.size();
    }

    public org.osid.shared.Type nextType()
    throws org.osid.shared.SharedException
    {
        if (i < vector.size())
        {
            return (org.osid.shared.Type)vector.elementAt(i++);
        }
        else
        {
            throw new org.osid.shared.SharedException(org.osid.shared.SharedException.NO_MORE_ITERATOR_ELEMENTS);
        }
    }
}
