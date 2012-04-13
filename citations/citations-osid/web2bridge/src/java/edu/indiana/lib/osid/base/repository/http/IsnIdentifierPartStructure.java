package edu.indiana.lib.osid.base.repository.http;

/*******************************************************************************
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
*******************************************************************************/

public class IsnIdentifierPartStructure
implements org.osid.repository.PartStructure
{
		private static org.apache.commons.logging.Log	_log = edu.indiana.lib.twinpeaks.util.LogUtils.getLog(IsnIdentifierPartStructure.class);
		
    private org.osid.shared.Id ISN_IDENTIFIER_PART_STRUCTURE_ID = null;
    private org.osid.shared.Type type = new Type( "sakaibrary", "partStructure",
    		"isnIdentifier", "International Standard Number" );
    private String displayName = "International Standard Number";
    private String description = "International Standard Number, usually ISSN" +
    		" or ISBN";
    private boolean mandatory = false;
    private boolean populatedByRepository = false;
    private boolean repeatable = false;

	private static IsnIdentifierPartStructure isnIdentifierPartStructure;

    private IsnIdentifierPartStructure()
    {
        try
       {
            this.ISN_IDENTIFIER_PART_STRUCTURE_ID =
            	Managers.getIdManager().getId("d7345qe3245a1006esr98n1234bvh8730");
        }
        catch (Throwable ignore) { }
    }

		/**
		 * Public method to fetch the PartStructure ID
		 */
		public static org.osid.shared.Id getPartStructureId()
		{
			org.osid.shared.Id id = null;

			try
			{
				id = getInstance().getId();
			}
			catch (org.osid.repository.RepositoryException ignore) { }

			return id;
		}

		protected static synchronized IsnIdentifierPartStructure getInstance()
		{
			if( isnIdentifierPartStructure == null ) {
				isnIdentifierPartStructure = new IsnIdentifierPartStructure();
			}
			return isnIdentifierPartStructure;
		}

    public String getDisplayName()
    throws org.osid.repository.RepositoryException
    {
        return this.displayName;
    }

    public String getDescription()
    throws org.osid.repository.RepositoryException
    {
        return this.description;
    }

    public boolean isMandatory()
    throws org.osid.repository.RepositoryException
    {
        return this.mandatory;
    }

    public boolean isPopulatedByRepository()
    throws org.osid.repository.RepositoryException
    {
        return this.populatedByRepository;
    }

    public boolean isRepeatable()
    throws org.osid.repository.RepositoryException
    {
        return this.repeatable;
    }

    public void updateDisplayName(String displayName)
    throws org.osid.repository.RepositoryException
    {
        throw new org.osid.repository.RepositoryException(org.osid.OsidException.UNIMPLEMENTED);
    }

    public org.osid.shared.Id getId()
    throws org.osid.repository.RepositoryException
    {
        return this.ISN_IDENTIFIER_PART_STRUCTURE_ID;
    }

    public org.osid.shared.Type getType()
    throws org.osid.repository.RepositoryException
    {
        return this.type;
    }

    public org.osid.repository.RecordStructure getRecordStructure()
    throws org.osid.repository.RepositoryException
    {
        return RecordStructure.getInstance();
    }

    public boolean validatePart(org.osid.repository.Part part)
    throws org.osid.repository.RepositoryException
    {
        return true;
    }

    public org.osid.repository.PartStructureIterator getPartStructures()
    throws org.osid.repository.RepositoryException
    {
        return new PartStructureIterator(new java.util.Vector());
    }
}
