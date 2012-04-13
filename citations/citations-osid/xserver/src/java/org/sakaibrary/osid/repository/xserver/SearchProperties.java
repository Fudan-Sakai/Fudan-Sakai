/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/citations/branches/sakai-2.8.1/citations-osid/xserver/src/java/org/sakaibrary/osid/repository/xserver/SearchProperties.java $
 * $Id: SearchProperties.java 59673 2009-04-03 23:02:03Z arwhyte@umich.edu $
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

import org.osid.shared.SharedException;

public class SearchProperties implements org.osid.shared.Properties {
	private Type type = new Type( "sakaibrary", "properties", "asynchMetasearch" );
	private java.util.Properties properties;
	private java.util.Vector keys;

	public SearchProperties( java.util.Properties properties ) {
		this.keys = new java.util.Vector();
		this.properties = properties;
		
		java.util.Enumeration keyNames = properties.keys();
		while( keyNames.hasMoreElements() ) {
			this.keys.add( (java.io.Serializable)keyNames.nextElement() );
		}
	}
	
	public org.osid.shared.ObjectIterator getKeys()
	throws SharedException {
		return new ObjectIterator( keys );
	}

	public java.io.Serializable getProperty( java.io.Serializable key )
	throws SharedException {
		return (java.io.Serializable)properties.get( key );
	}

	public org.osid.shared.Type getType() 
	throws SharedException {
		return type;
	}
}
