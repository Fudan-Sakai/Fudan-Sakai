package edu.indiana.lib.osid.base.repository.http;

/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/citations/branches/sakai-2.8.1/citations-osid/web2bridge/src/java/edu/indiana/lib/osid/base/repository/http/Record.java $
 * $Id: Record.java 59673 2009-04-03 23:02:03Z arwhyte@umich.edu $
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
public class Record extends edu.indiana.lib.osid.base.repository.Record
{
		private static org.apache.commons.logging.Log	_log = edu.indiana.lib.twinpeaks.util.LogUtils.getLog(Record.class);

    private java.util.Vector partVector = new java.util.Vector();
    private org.osid.id.IdManager idManager = null;
    private org.osid.shared.Id recordStructureId = null;
    private String displayName = null;
    private org.osid.shared.Id id = null;


    public String getDisplayName()
    throws org.osid.repository.RepositoryException
    {
        return this.displayName;
    }

    public org.osid.shared.Id getId()
    throws org.osid.repository.RepositoryException
    {
        return this.id;
    }

    protected Record(org.osid.shared.Id recordStructureId
                   , org.osid.id.IdManager idManager)
    throws org.osid.repository.RepositoryException
    {
        try
        {
            this.idManager = idManager;
            this.recordStructureId = recordStructureId;
            this.id = idManager.createId();
        }
        catch (Throwable t)
        {
            _log.error(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.OsidException.OPERATION_FAILED);
        }
    }

    public org.osid.repository.Part createPart(org.osid.shared.Id partStructureId
                                             , java.io.Serializable value)
    throws org.osid.repository.RepositoryException
    {
        try
        {
            org.osid.repository.Part part = new Part(partStructureId,value,this.idManager);
            this.partVector.addElement(part);
            return part;
        }
        catch (Throwable t)
        {
            _log.error(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.OsidException.OPERATION_FAILED);
        }
    }

    public void deletePart(org.osid.shared.Id partId)
    throws org.osid.repository.RepositoryException
    {
        try
        {
            for (int i=0, size = this.partVector.size(); i < size; i++)
            {
                org.osid.repository.Part part = (org.osid.repository.Part)this.partVector.elementAt(i);
                if (part.getId().isEqual(partId))
                {
                    this.partVector.removeElementAt(i);
                    return;
                }
            }
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.UNKNOWN_ID);
        }
        catch (Throwable t)
        {
            _log.error(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.OsidException.OPERATION_FAILED);
        }
    }

    public org.osid.repository.PartIterator getParts()
    throws org.osid.repository.RepositoryException
    {
        return new PartIterator(this.partVector);
    }

    public org.osid.repository.RecordStructure getRecordStructure()
    throws org.osid.repository.RepositoryException
    {
        try
        {
            if (this.recordStructureId.isEqual(RecordStructure.getInstance().getId()))
            {
                return new RecordStructure();
            }
            else
            {
                return null;
            }
        }
        catch (Throwable t)
        {
            _log.error(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.OsidException.OPERATION_FAILED);
        }
    }

    public boolean isMultivalued()
    throws org.osid.repository.RepositoryException
    {
        return false;
    }
}
