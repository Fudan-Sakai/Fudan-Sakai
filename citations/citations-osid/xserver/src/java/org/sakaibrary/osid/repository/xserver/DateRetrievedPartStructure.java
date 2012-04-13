/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/citations/branches/sakai-2.8.1/citations-osid/xserver/src/java/org/sakaibrary/osid/repository/xserver/DateRetrievedPartStructure.java $
 * $Id: DateRetrievedPartStructure.java 59673 2009-04-03 23:02:03Z arwhyte@umich.edu $
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

package org.sakaibrary.osid.repository.xserver;

public class DateRetrievedPartStructure
implements org.osid.repository.PartStructure
{
	private static final org.apache.commons.logging.Log LOG =
		org.apache.commons.logging.LogFactory.getLog(
				"org.sakaibrary.osid.repository.xserver.DateRetrievedPartStructure" );

	private org.osid.shared.Id DATE_RETRIEVED_PART_STRUCTURE_ID = null;
	private org.osid.shared.Type type = new Type( "sakaibrary", "partStructure",
			"dateRetrieved", "Date Retrieved" );
	private String displayName = "Date Retrieved";
	private String description = "Date Retrieved indicates the date this " +
			"resource was retrieved.";
	private boolean mandatory = false;
	private boolean populatedByRepository = false;
	private boolean repeatable = false;
	
	private static DateRetrievedPartStructure dateRetrievedPartStructure;

	private DateRetrievedPartStructure()
	{
		try {
			this.DATE_RETRIEVED_PART_STRUCTURE_ID = Managers.getIdManager().getId(
					"b1asbd8f09s3450909751910168500100");
		} catch (Throwable t) {
			LOG.warn( "DateRetrievedPartStructure() failed to get partStructure id: "
					+ t.getMessage() );
		}
	}

	protected static synchronized DateRetrievedPartStructure getInstance()
	{
		if( dateRetrievedPartStructure == null ) {
			dateRetrievedPartStructure = new DateRetrievedPartStructure();
		}
		return dateRetrievedPartStructure;
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
		throw new org.osid.repository.RepositoryException(
				org.osid.OsidException.UNIMPLEMENTED);
	}

	public org.osid.shared.Id getId()
	throws org.osid.repository.RepositoryException
	{
		return this.DATE_RETRIEVED_PART_STRUCTURE_ID;
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
