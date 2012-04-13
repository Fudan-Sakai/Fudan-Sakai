/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/citations/branches/sakai-2.8.1/citations-impl/impl/src/java/org/sakaiproject/citation/impl/BasicCitationService.java $
 * $Id: BasicCitationService.java 59673 2009-04-03 23:02:03Z arwhyte@umich.edu $
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

package org.sakaiproject.citation.impl;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org.sakaiproject.citation.api.Citation;
import org.sakaiproject.citation.api.CitationCollection;
import org.sakaiproject.citation.api.Schema;
import org.sakaiproject.citation.impl.BaseCitationService.BasicCitationCollection;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.entity.api.HttpAccess;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This is a test class to implement a concrete CitationService using the Storage interface to define the 
 * interactions that will be needed when we do a DB implementation.  This version uses hashtables to 
 * store the java objects in memory, rather than serializing them and putting them in a persistent store.
 */
public class BasicCitationService extends BaseCitationService
{
	public class BasicCitationStorage implements Storage
	{
		protected Map m_collections;
		protected Map m_citations;
		protected Map m_schemas;

		/**
          */
        public BasicCitationStorage()
        {
	        super();
	       	m_collections = new Hashtable();
        	m_citations = new Hashtable();
 	        m_schemas = new Hashtable();
        }
		
		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#open()
         */
        public void open()
        {
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#close()
         */
        public void close()
        {
        	m_collections.clear();
        	m_collections = null;
        	
        	m_citations.clear();
        	m_citations = null;
        	
        	m_schemas.clear();
        	m_schemas = null;
        	
       }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#getCitation(java.lang.String)
         */
        public Citation getCitation(String citationId)
        {
	        return (Citation) m_citations.get(citationId);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#getCollection(java.lang.String)
         */
        public CitationCollection getCollection(String collectionId)
        {
	        return (CitationCollection) m_collections.get(collectionId);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#getSchema(java.lang.String)
         */
        public Schema getSchema(String schemaId)
        {
	        return (Schema) m_schemas.get(schemaId);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#getSchemaNames()
         */
        public Collection getSchemaNames()
        {
			return m_schemas.keySet();
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#getSchemas()
         */
        public List getSchemas()
        {
        	List list = new Vector();
    		Iterator it = m_schemas.keySet().iterator();
    		while(it.hasNext())
    		{
    			String key = (String) it.next();
    			Schema schema = (Schema) m_schemas.get(key);
    			{
    				list.add(new BasicSchema(schema));
    			}
    		}
    		return list;
       }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#putSchema(org.sakaiproject.citation.api.Schema)
         */
        public Schema addSchema(Schema schema)
        {
	        m_schemas.put(schema.getIdentifier(), schema);
	        return schema;
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#putSchemas(java.util.Collection)
         */
        public void putSchemas(Collection schemas)
        {
        	Iterator it = schemas.iterator();
        	while(it.hasNext())
        	{
        		Object obj = it.next();
        		if(obj instanceof Schema)
        		{
        			m_schemas.put(((Schema) obj).getIdentifier(), obj);
        		}
         	}
	        
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#checkCitation(java.lang.String)
         */
        public boolean checkCitation(String citationId)
        {
	        return this.m_citations.containsKey(citationId);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#checkCollection(java.lang.String)
         */
        public boolean checkCollection(String collectionId)
        {
	        return this.m_collections.containsKey(collectionId);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#checkSchema(java.lang.String)
         */
        public boolean checkSchema(String schemaId)
        {
	        return this.m_schemas.containsKey(schemaId);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#listSchemas()
         */
        public List listSchemas()
        {
        	Set names = this.m_schemas.keySet();
	        
	        return new Vector(names);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#putCollection(java.util.Map, java.util.List)
         */
        public CitationCollection addCollection(Map attributes, List citations)
        {
         	// need to create a collection (referred to below as "edit")
        	BasicCitationCollection edit = new BasicCitationCollection(attributes, citations);
        	
        	this.m_collections.put(edit.getId(), edit);
        	
        	return edit;
       }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#removeCitation(org.sakaiproject.citation.api.CitationEdit)
         */
        public void removeCitation(Citation edit)
        {
	        this.m_citations.remove(edit.getId());
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#removeCollection(org.sakaiproject.citation.api.CitationCollectionEdit)
         */
        public void removeCollection(CitationCollection edit)
        {
	        this.m_collections.remove(edit.getId());
       }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#updateSchema(org.sakaiproject.citation.api.Schema)
         */
        public void updateSchema(Schema schema)
        {
        	this.m_schemas.put(schema.getIdentifier(), schema);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#updateSchemas(java.util.Collection)
         */
        public void updateSchemas(Collection schemas)
        {
	        Iterator it = schemas.iterator();
	        while(it.hasNext())
	        {
	        	Schema schema = (Schema) it.next();
	        	this.m_schemas.put(schema.getIdentifier(), schema);
	        }
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#saveCitation(org.sakaiproject.citation.api.Citation)
         */
        public void saveCitation(Citation edit)
        {
	        this.m_citations.put(edit.getId(), edit);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#saveCollection(java.util.Collection)
         */
        public void saveCollection(CitationCollection collection)
        {
        	this.m_collections.put(collection.getId(), collection);
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#addCitation(java.lang.String)
         */
        public Citation addCitation(String mediatype)
        {
	        BasicCitation citation = new BasicCitation(mediatype);
	        this.m_citations.put(citation.getId(), citation);
	        return citation;
        }

		/* (non-Javadoc)
         * @see org.sakaiproject.citation.impl.BaseCitationService.Storage#removeSchema(org.sakaiproject.citation.api.Schema)
         */
        public void removeSchema(Schema schema)
        {
	        m_schemas.remove(schema.getIdentifier());
	        
        }

		public CitationCollection copyAll(String collectionId) 
		{
			BasicCitationCollection original = (BasicCitationCollection) this.m_collections.get(collectionId);
			CitationCollection copy = null;
			
			if(original != null)
			{
				copy = new BasicCitationCollection();
				Iterator it = original.iterator();
				while(it.hasNext())
				{
					BasicCitation citation = (BasicCitation) it.next();
					BasicCitation newCite = new BasicCitation();
					newCite.copy(citation);
					copy.add(newCite);
					m_citations.put(newCite.getId(), newCite);
				}
				m_collections.put(copy.getId(), copy);
			}

			return copy;
		}

		public long mostRecentUpdate(String collectionId) 
		{
			return 0;
		}

	}

	/* (non-Javadoc)
     * @see org.sakaiproject.citation.impl.BaseCitationService#newStorage()
     */
    public Storage newStorage()
    {
	    return new BasicCitationStorage();
    }    
}
