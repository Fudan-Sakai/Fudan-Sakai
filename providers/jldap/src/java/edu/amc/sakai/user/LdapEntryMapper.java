/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/providers/branches/sakai-2.8.1/jldap/src/java/edu/amc/sakai/user/LdapEntryMapper.java $
 * $Id: LdapEntryMapper.java 61856 2009-05-05 17:53:41Z dmccallum@unicon.net $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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

package edu.amc.sakai.user;

import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPSearchResults;

/**
 * Similar to {@link org.springframework.jdbc.core.RowMapper}, but
 * for {@link LDAPSearchResults} rather than {@link java.sql.ResultSet}s.
 * The callback method signature is slightly different than
 * {@link org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)}
 * due to API differences between Novell and JDBC libraries.
 * Clients interact with {@link LDAPSearchResults} as if it were
 * a collection of {@link LDAPEntry}, so passing the entire
 * {@link LDAPSearchResults} object to
 * {@Link {@link #mapLdapEntry(LDAPEntry, int)} places undue burden on
 * implementations.
 * 
 * @author Dan McCallum (dmccallum@unicon.net)
 */
public interface LdapEntryMapper {

    /**
     * Map the given {@link LDAPEntry} onto some {@link Object}.
     * 
     * @param searchResult the {@link LDAPEntry} to map
     * @param resultNum the number of times this mapper has been invoked
     *   for the current LDAP search result set. One-based.
     * @return the mapped {@link LDAPEntry}
     */
    public Object mapLdapEntry(LDAPEntry searchResult, int resultNum);
    
}
