/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/providers/branches/sakai-2.8.1/jldap-integration-test/src/test/java/edu/amc/sakai/user/JLDAPDirectoryProviderIntegrationTestSuite.java $
 * $Id: JLDAPDirectoryProviderIntegrationTestSuite.java 61858 2009-05-05 17:57:52Z dmccallum@unicon.net $
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

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.sakaiproject.test.SakaiTestBase;

/**
 * 
 * @author John Lewis (jlewis@unicon.net)
 *
 */
public class JLDAPDirectoryProviderIntegrationTestSuite extends SakaiTestBase {
    
    public static Test suite() {
        
        TestSuite suite = new TestSuite("Integration tests for edu.amc.sakai.user.JLDAPDirectoryProvider");
        suite.addTestSuite(JLDAPDirectoryProviderIntegrationTest.class);
        
        TestSetup decoratedSuite = new TestSetup(suite) {
            protected void setUp() throws Exception {
                oneTimeSetup();
            }
            protected void tearDown() throws Exception {
                oneTimeTearDown();
            }
        };
        
        return decoratedSuite;
    }

}
