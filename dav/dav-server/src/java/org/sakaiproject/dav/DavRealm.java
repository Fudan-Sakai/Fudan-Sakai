/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/dav/branches/sakai-2.8.1/dav-server/src/java/org/sakaiproject/dav/DavRealm.java $
 * $Id: DavRealm.java 64253 2009-06-27 13:34:09Z stephen.marquard@uct.ac.za $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 The Sakai Foundation
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

package org.sakaiproject.dav;

import java.security.Principal;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.realm.RealmBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple implementation of <b>Realm</b> that consults the Sakai user directory service to provide container security equivalent to then application security in CHEF.
 * <p>
 * <strong>IMPLEMENTATION NOTE</strong>: The user is assumed to have all "roles" because servlets and teamlets will enforce roles within Sakai - so in this realm, we simply indicate "true".
 */
public final class DavRealm extends RealmBase
{
	
	private static Log M_log = LogFactory.getLog(DavRealm.class);

	/** Descriptive information about this Realm implementation. */
	protected static final String info = "org.sakaiproject.realm.DavRealm/1.0";

	/** Descriptive information about this Realm implementation. */
	protected static final String name = "DavRealm";

	/**
	 * Return descriptive information about this Realm implementation and the corresponding version number, in the format <code>&lt;description&gt;/&lt;version&gt;</code>.
	 */
	public String getInfo()
	{
		return info;
	}

	/**
	 * Return the Principal associated with the specified username and credentials, if there is one; otherwise return <code>null</code>.
	 * 
	 * @param username
	 *        Username of the Principal to look up
	 * @param credentials
	 *        Password or other credentials to use in authenticating this username
	 */
	public Principal authenticate(String username, String credentials)
	{
		if (username == null || credentials == null) return null;
		if (username.length() <= 0 || credentials.length() <= 0) return null;

		return new DavPrincipal(username, credentials);
	}

	/**
	 * Return a short name for this Realm implementation.
	 */
	protected String getName()
	{
		return name;
	}

	protected Principal getPrincipal(String username)
	{
		M_log.debug("DavRealm.getPrincipal(" + username + ") -- why is this being called?");

		if (username == null) return null;

		return new DavPrincipal(username, " ");
	}

	/**
	 * Return the password associated with the given principal's user name.
	 */
	protected String getPassword(String username)
	{
		M_log.debug("DavRealm.getPassword(" + username + ")");
		return null;
	}

	/**
	 * Prepare for active use of the public methods of this Component.
	 * 
	 * @exception IllegalStateException
	 *            if this component has already been started
	 * @exception LifecycleException
	 *            if this component detects a fatal error that prevents it from being started
	 */
	public synchronized void start() throws LifecycleException
	{
		M_log.info("start()");

		// Perform normal superclass initialization
		super.start();
	}

	/**
	 * Gracefully shut down active use of the public methods of this Component.
	 * 
	 * @exception IllegalStateException
	 *            if this component has not been started
	 * @exception LifecycleException
	 *            if this component detects a fatal error that needs to be reported
	 */
	public synchronized void stop() throws LifecycleException
	{
		// Perform normal superclass finalization
		super.stop();

		// No shutdown activities required
	}

	public boolean hasRole(Principal principal, String role)
	{
		return true;
	}
}
