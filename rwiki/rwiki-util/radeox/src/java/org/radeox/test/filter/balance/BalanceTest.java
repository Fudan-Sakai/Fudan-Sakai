/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/rwiki/branches/sakai-2.8.1/rwiki-util/radeox/src/java/org/radeox/test/filter/balance/BalanceTest.java $
 * $Id: BalanceTest.java 74433 2010-03-08 17:21:23Z david.horwitz@uct.ac.za $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 Sakai Foundation
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

package org.radeox.test.filter.balance;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.filter.balance.Balancer;

/**
 * @author ieb
 */
public class BalanceTest extends TestCase
{

	public static final String matcherString = "(<([^ />]+)(?: [^>]*?[^/])?>)|(</([^ />]+)>)";

	private static final Log log = LogFactory.getLog(BalanceTest.class);

	/**
	 * @param arg0
	 */
	public BalanceTest(String arg0)
	{
		super(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testSAK12828() throws Exception
	{
		Pattern p = Pattern.compile(matcherString);
		Matcher m = p.matcher(getSAK12828Pattern());
		if (m.find())
		{
			Balancer b = new Balancer();
			b.setMatcher(m);
			String result = b.filter();
		}
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private String getSAK12828Pattern() throws IOException
	{
		InputStreamReader in = null;
		StringBuilder sb = new StringBuilder();
		try {
			in = new InputStreamReader(this.getClass().getResourceAsStream(
			"SAK12828.html"));
			char[] b = new char[4096];
			
			int i = 0;
			while ((i = in.read(b)) > 0)
			{
				sb.append(b, 0, i);
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			in.close();
		}
		return sb.toString();
	}

}
