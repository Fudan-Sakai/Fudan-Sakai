/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/citations/branches/sakai-2.8.1/citations-osid/web2bridge/src/java/edu/indiana/lib/osid/component/id/Id.java $
 * $Id: Id.java 59673 2009-04-03 23:02:03Z arwhyte@umich.edu $
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

package edu.indiana.lib.osid.component.id;

import java.util.Random;

/**
 * @inheritDoc
 */
public class Id implements org.osid.shared.Id
{
	private static org.apache.commons.logging.Log	_log = edu.indiana.lib.twinpeaks.util.LogUtils.getLog(Id.class);

	private static long	idBase		= System.currentTimeMillis();
	private String 			idString 	= null;


	private synchronized long getIdBase()
	{
		return idBase++;
	}

	private void log(String entry) throws org.osid.shared.SharedException
	{
		_log.debug("Id: " + entry);
	}

	protected Id() throws org.osid.shared.SharedException
	{
		long 			base;
		Random		random;

		base 			= getIdBase();
		random 		= new Random(base);

		idString 	= String.valueOf(base) + "-" + String.valueOf(random.nextLong());
		random		= null;
	}

	protected Id(String idString) throws org.osid.shared.SharedException
	{
		if (idString == null)
		{
			throw new org.osid.shared.SharedException(org.osid.id.IdException.NULL_ARGUMENT);
		}
		this.idString = idString;
	}

	public String getIdString() throws org.osid.shared.SharedException
	{
		return this.idString;
	}

	public boolean isEqual(org.osid.shared.Id id) throws org.osid.shared.SharedException
	{
		return id.getIdString().equals(this.idString);
	}
}
