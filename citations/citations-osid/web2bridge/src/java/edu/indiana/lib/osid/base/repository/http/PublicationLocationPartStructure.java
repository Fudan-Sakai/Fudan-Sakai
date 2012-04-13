package edu.indiana.lib.osid.base.repository.http;

public class PublicationLocationPartStructure
implements org.osid.repository.PartStructure
{
		private static org.apache.commons.logging.Log	_log = edu.indiana.lib.twinpeaks.util.LogUtils.getLog(PublicationLocationPartStructure.class);
		
    private org.osid.shared.Id PUBLICATION_LOCATION_PART_STRUCTURE_ID = null;
    private org.osid.shared.Type type = new Type( "sakaibrary", "partStructure",
    		"publicationLocation", "Publication Location" );
    private String displayName = "Publication Location";
    private String description = "Publication Location is usually a city name " +
    		"or a geographical region indicating the place where a resource " +
    		"has been published.";
    private boolean mandatory = false;
    private boolean populatedByRepository = false;
    private boolean repeatable = true;

		private static PublicationLocationPartStructure publicationLocationPartStructure =
			new PublicationLocationPartStructure();

		protected static PublicationLocationPartStructure getInstance()
		{
			return publicationLocationPartStructure;
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

    protected PublicationLocationPartStructure()
    {
        try
        {
            this.PUBLICATION_LOCATION_PART_STRUCTURE_ID = Managers.getIdManager().getId(
            		"9jk234ff201080005465hg2f168342540");
        }
        catch (Throwable t)
        {
        }
    }

    public void updateDisplayName(String displayName)
    throws org.osid.repository.RepositoryException
    {
        throw new org.osid.repository.RepositoryException(org.osid.OsidException.UNIMPLEMENTED);
    }

    public org.osid.shared.Id getId()
    throws org.osid.repository.RepositoryException
    {
        return this.PUBLICATION_LOCATION_PART_STRUCTURE_ID;
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
