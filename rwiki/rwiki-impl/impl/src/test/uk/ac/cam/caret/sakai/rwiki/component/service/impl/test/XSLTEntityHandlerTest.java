/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/rwiki/branches/sakai-2.8.1/rwiki-impl/impl/src/test/uk/ac/cam/caret/sakai/rwiki/component/service/impl/test/XSLTEntityHandlerTest.java $
 * $Id: XSLTEntityHandlerTest.java 84671 2010-11-12 18:27:24Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package uk.ac.cam.caret.sakai.rwiki.component.service.impl.test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.MimeConstants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import uk.ac.cam.caret.sakai.rwiki.component.model.impl.RWikiEntityImpl;
import uk.ac.cam.caret.sakai.rwiki.component.service.impl.Decoded;
import uk.ac.cam.caret.sakai.rwiki.component.service.impl.XSLTEntityHandler;
import uk.ac.cam.caret.sakai.rwiki.model.RWikiCurrentObjectImpl;
import uk.ac.cam.caret.sakai.rwiki.service.api.model.RWikiEntity;

/**
 * @author ieb
 */
public class XSLTEntityHandlerTest extends TestCase
{
	private static Log logger = LogFactory.getLog(XSLTEntityHandlerTest.class);

	private XSLTEntityHandler eh = null;

	private static final String testinputhtml = "/uk/ac/cam/caret/sakai/rwiki/component/service/impl/test/testpattern.xhtml";

	private static final String testinputfop = "/uk/ac/cam/caret/sakai/rwiki/component/service/impl/test/testinput.xml";

	public void setUp()
	{

		eh = new XSLTEntityHandler();

	}

	public void xtestDecode()
	{

		XSLTEntityHandler eh = new XSLTEntityHandler();
		eh.setAccessURLStart("/wiki/");
		eh.setMinorType("html");
		String[] test = {
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465/home/sdfsdf/sdfsdf/sdfsdfsd/sdfsdfsdf/sdfsdfdsf/sdfsdf,123123.html",
				"/wikisite/c8e34826-dab9-466c-80a9-e8e9bed50465/home/sdfsdf/sdfsdf/sdfsdfsd/sdfsdfsdf/sdfsdfdsf/sdfsdf,123123.html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465/home,sdfsdf,223,234.html",
				"/wikin/site/c8e34826-dab9-466c-80a9-e8e9bed50465/home.html",
				"/wikin/site/c8e34826-dab9-466c-80a9-e8e9bed50465/home.html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465/sdfsdfs/home,123123.html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465/home,123123.html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465/home.html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465/sdfsdfs/home,123123.html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465/sdfsdfs/home.html",
				"wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465/sdfsdfs/home.html",
				"home.html",
				"/wiki/sitec8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/home.html",
				"/wiki/sitec8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs///home.html",
				"/wiki///sitec8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/home.html",
				"/wiki/global/HelpPage.html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/HelpPage.",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/HelpPage..",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/HelpPage..html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/HelpPage",
				"/wiki/site/site-uk.ac.cam.caret.sakai.rwiki.component.test.componentintegrationtest-71220.0/hometestpage.html",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/HelpPage.09.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/HelpPage.10.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/HelpPage.20.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/HelpPage.atom",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/ .09.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/ .10.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/ .20.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/ .atom",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/subsite/ .09.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/subsite/ .10.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/subsite/ .20.rss",
				"/wiki/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs/subsite/ .atom"

		};
		Decoded[] results_html = {
				new Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465",
						"/home/sdfsdf/sdfsdf/sdfsdfsd/sdfsdfsdf/sdfsdfdsf",
						"sdfsdf", "123123"),
				null,
				new Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465", "/",
						"home", "sdfsdf,223,234"),
				null,
				null,
				new Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465",
						"/sdfsdfs", "home", "123123"),
				new Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465", "/",
						"home", "123123"),
				new Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465", "/",
						"home", "-1"),
				new Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465",
						"/sdfsdfs", "home", "123123"),
				new Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465",
						"/sdfsdfs", "home", "-1"),
				null,
				null,
				new Decoded("/sitec8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", "home", "-1"),
				null,
				null,
				new Decoded("/global", "/", "HelpPage", "-1"),
				null,
				null,
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", "HelpPage.", "-1"),
				null,
				new Decoded(
						"/site/site-uk.ac.cam.caret.sakai.rwiki.component.test.componentintegrationtest-71220.0",
						"/", "hometestpage", "-1"), null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //.09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //20.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //20.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","","-1"),
				// //.atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //20.rss",
				null // new
		// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
		// ","-1") //atom"
		};
		Decoded[] results_09rss = {
				null, // 0
				null, // 1
				null, // 2
				null, // 3
				null, // 4
				null, // 5
				null, // 6
				null, // 7
				null, // 8
				null, // 9
				null, // 10
				null, // 11
				null, // 12
				null, // 13
				null, // 14
				null, // 15
				null, // 16
				null, // 17
				null, // 18
				null, // 19
				null, // 20
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", "HelpPage", "-1"), // .09.rss", //21
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //10.rss", //22
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //20.rss", //23
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //atom", //24
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", " ", "-1"), // 09.rss", //25
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //10.rss", //26
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //20.rss", //27
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //.atom", //28
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/subsite", " ", "-1"), // 09.rss", //29
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //10.rss", //30
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //20.rss", //31
				null // new
		// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
		// ","-1") //atom" //32
		};
		Decoded[] results_ = {
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", "HelpPage", "-1"),
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", "HelpPage.", "-1"), null, null, null, null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //.09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //20.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //20.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //.atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //20.rss",
				null // new
		// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
		// ","-1") //atom"
		};
		Decoded[] results_10rss = {
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //.09.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", "HelpPage", "-1"), // 10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //20.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //09.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", " ", "-1"), // 10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //20.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //.atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //09.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/subsite", " ", "-1"), // 10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //20.rss",
				null // new
		// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
		// ","-1") //atom"
		};
		Decoded[] results_20rss = {
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //.09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //10.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", "HelpPage", "-1"), // 20.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //10.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", " ", "-1"), // 20.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //.atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //10.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/subsite", " ", "-1"), // 20.rss",
				null // new
		// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
		// ","-1") //atom"
		};
		Decoded[] results_atom = {
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //.09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","HelpPage","-1"),
				// //20.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", "HelpPage", "-1"), // atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/","
				// ","-1"), //20.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/", " ", "-1"), // .atom",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //09.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //10.rss",
				null, // new
				// Decoded("/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs","/subsite","
				// ","-1"), //20.rss",
				new Decoded(
						"/site/c8e34826-dab9-466c-80a9-e8e9bed50465sdfsdfs",
						"/subsite", " ", "-1") // atom"
		};

		Object[] testSet = { results_html, results_, results_09rss,
				results_10rss, results_20rss, results_atom };
		String[] postfix = { "html", "", "09.rss", "10.rss", "20.rss", "atom" };
		assertEquals("Test sequence are not setup correctly ", testSet.length,
				postfix.length);
		for (int ts = 0; ts < testSet.length; ts++)
		{
			Decoded[] o = (Decoded[]) testSet[ts];
			assertEquals("test and result are different lengths for entry "
					+ ts, o.length, test.length);
		}
		assertEquals("Test sequence are not setup correctly ", testSet.length,
				postfix.length);
		for (int ts = 0; ts < testSet.length; ts++)
		{
			Decoded[] results = (Decoded[]) testSet[ts];
			eh.setMinorType(postfix[ts]);
			for (int j = 0; j < test.length; j++)
			{
				Decoded decoded = eh.decode(test[j]);

				Decoded result = results[j];
				if (decoded != null)
				{
					logger.info("--Context = " + decoded.getContext());
					logger.info("--getContainer = " + decoded.getContainer());
					logger.info("--getPage = " + decoded.getPage());
					logger.info("--getVersion = " + decoded.getVersion());
					logger.info("--getId = " + decoded.getId());

				}
				else
				{
					logger.info("--null");
				}
				if (result != null)
				{
					logger.info("++Context = " + result.getContext());
					logger.info("++getContainer = " + result.getContainer());
					logger.info("++getPage = " + result.getPage());
					logger.info("++getVersion = " + result.getVersion());
					logger.info("++getId = " + result.getId());
				}
				else
				{
					logger.info("++null");
				}

				if (result != null && decoded == null)
					fail("Test Set " + ts + " minor " + postfix[ts] + " item "
							+ j + " Should have matched  " + test[j]);
				if (result == null && decoded != null)
					fail(" Should not have matched  " + test[j]);
				if (result != null && decoded != null)
				{
					assertEquals("Test " + ts + ":" + j
							+ " Failed Contexts not the same " + test[j],
							result.getContext(), decoded.getContext());
					assertEquals("Test " + ts + ":" + j
							+ " Failed Container not the same " + test[j],
							result.getContainer(), decoded.getContainer());
					assertEquals("Test " + ts + ":" + j
							+ " Failed Page not the same " + test[j], result
							.getPage(), decoded.getPage());
					assertEquals("Test " + ts + ":" + j
							+ " Failed Version not the same " + test[j], result
							.getVersion(), decoded.getVersion());
					assertEquals("Test " + ts + ":" + j
							+ " Failed Id not the same " + test[j], result
							.getId(), decoded.getId());
				}
			}
		}

		long start = System.currentTimeMillis();
		int iters = 10000;
		for (int i = 0; i < iters; i++)
		{
			eh.decode(test[i % test.length]);

		}
		float timet = (float) 1.0 * (System.currentTimeMillis() - start);
		float tper = (float) (timet / (1.0 * iters));
		logger.info("Decode call cost = " + tper + " ms");

	}

	public void xtestXSLT() throws Exception
	{
		String[] test = {
				"/uk/ac/cam/caret/sakai/rwiki/component/service/impl/null.xslt",
				"/uk/ac/cam/caret/sakai/rwiki/component/service/impl/toatom03.xslt",
				"/uk/ac/cam/caret/sakai/rwiki/component/service/impl/tohtml.xslt",
				"/uk/ac/cam/caret/sakai/rwiki/component/service/impl/torss091.xslt",
				"/uk/ac/cam/caret/sakai/rwiki/component/service/impl/torss10.xslt",
				"/uk/ac/cam/caret/sakai/rwiki/component/service/impl/torss20.xslt",
				"/uk/ac/cam/caret/sakai/rwiki/component/service/impl/xhtml2fo.xslt"

		};

		RWikiCurrentObjectImpl rwco = new RWikiCurrentObjectImpl();
		RWikiEntity rwe = new RWikiEntityImpl(rwco);
		rwco.setContent("Some Content");
		rwco.setGroupAdmin(false);
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setOwner("The Owner");
		rwco.setUser("The User");
		rwco.setVersion(new Date());
		rwco.setRevision(Integer.valueOf(5));

		MockHttpServletRequest request = new MockHttpServletRequest();

		for (int i = 0; i < test.length; i++)
		{
			MockHttpServletResponse response = new MockHttpServletResponse();
			eh.setXslt(test[i]);
			eh.init();
			eh.outputContent(rwe, rwe, request, response);
			logger.info(response.getContentAsString());
		}
		long start = System.currentTimeMillis();
		int iters = 10;
		for (int j = 0; j < iters; j++)
		{
			for (int i = 0; i < test.length; i++)
			{
				MockHttpServletResponse response = new MockHttpServletResponse();
				eh.outputContent(rwe, rwe, request, response);
			}
		}
		float timet = (float) 1.0 * (System.currentTimeMillis() - start);
		float tper = (float) (timet / (1.0 * iters));
		logger.info("Transform and Serialize Call Cost = " + tper + " ms");

	}

	public void xtestRTF() throws Exception
	{
		String transform = "/uk/ac/cam/caret/sakai/rwiki/component/service/impl/xhtml2fo.xslt";
		StringBuffer sb = new StringBuffer();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(testinputhtml)));
		String line = reader.readLine();
		while (line != null)
		{
			sb.append(line).append("\n");
			line = reader.readLine();
		}

		RWikiCurrentObjectImpl rwco = new RWikiCurrentObjectImpl();
		RWikiEntity rwe = new RWikiEntityImpl(rwco);
		rwco.setContent(sb.toString());
		rwco.setGroupAdmin(false);
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setOwner("The Owner");
		rwco.setUser("The User");
		rwco.setVersion(new Date());
		rwco.setRevision(Integer.valueOf(5));

		MockHttpServletRequest request = new MockHttpServletRequest();

		XSLTEntityHandler xeh = new XSLTEntityHandler();
		xeh.setAccessURLStart("/wiki/");
		xeh.setAnchorLinkFormat("/wiki{0}.html#{1}");
		xeh.setXslt(transform);
		xeh.setMinorType("rtf");
		xeh.setDefaultStackTrace("Failed To generate Stack Trace : {0}");
		xeh
				.setErrorFormat("Error encounvered performing transform : {0} \n {1}");
		xeh.setAuthZPrefix("/wiki");
		xeh.setAnchorLinkFormat("/wiki{0}.html#{1}");
		xeh.setStandardLinkFormat("/wiki{0}.html");
		xeh.setHrefTagFormat("<a href=\"{0}\" >{1}</a>");
		xeh.setAccessURLStart("/wiki/");
		xeh
				.setFeedFormat("<a href=\"{0}rtf\" target=\"feeds\"><img src=\"/library/image/sakai/rtf.gif\" border=\"0\"  alt=\"RTF\" /></a>");
		HashMap responseHeaders = new HashMap();
		responseHeaders.put("content-type", "text/rtf");
		xeh.setResponseHeaders(responseHeaders);
		HashMap outputProperties = new HashMap();
		outputProperties
				.put("{http://xml.apache.org/xalan}content-handler",
						"uk.ac.cam.caret.sakai.rwiki.component.service.impl.FOP2RTFSerializer");
		xeh.setOutputProperties(outputProperties);

		MockHttpServletResponse response = new MockHttpServletResponse();
		xeh.init();
		xeh.outputContent(rwe, rwe, request, response);
		File f = new File("testoutput.rtf");
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(response.getContentAsByteArray());
		fo.close();

		long start = System.currentTimeMillis();
		int iters = 10;
		for (int j = 0; j < iters; j++)
		{
			response = new MockHttpServletResponse();
			xeh.outputContent(rwe, rwe, request, response);
		}
		float timet = (float) 1.0 * (System.currentTimeMillis() - start);
		float tper = (float) (timet / (1.0 * iters));
		logger.info("Transform and Serialize Call Cost = " + tper + " ms");

	}

	public void xtestPDF() throws Exception
	{
		String transform = "/uk/ac/cam/caret/sakai/rwiki/component/service/impl/xhtml2fo.xslt";
		StringBuffer sb = new StringBuffer();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(testinputhtml)));
		String line = reader.readLine();
		while (line != null)
		{
			sb.append(line).append("\n");
			line = reader.readLine();
		}

		RWikiCurrentObjectImpl rwco = new RWikiCurrentObjectImpl();
		RWikiEntity rwe = new RWikiEntityImpl(rwco);
		rwco.setContent(sb.toString());
		rwco.setGroupAdmin(false);
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setOwner("The Owner");
		rwco.setUser("The User");
		rwco.setVersion(new Date());
		rwco.setRevision(Integer.valueOf(5));

		MockHttpServletRequest request = new MockHttpServletRequest();

		XSLTEntityHandler xeh = new XSLTEntityHandler();
		xeh.setAccessURLStart("/wiki/");
		xeh.setAnchorLinkFormat("/wiki{0}.html#{1}");
		xeh.setXslt(transform);
		xeh.setMinorType("pdf");
		xeh.setDefaultStackTrace("Failed To generate Stack Trace : {0}");
		xeh
				.setErrorFormat("Error encounvered performing transform : {0} \n {1}");
		xeh.setAuthZPrefix("/wiki");
		xeh.setAnchorLinkFormat("/wiki{0}.html#{1}");
		xeh.setStandardLinkFormat("/wiki{0}.html");
		xeh.setHrefTagFormat("<a href=\"{0}\" >{1}</a>");
		xeh.setAccessURLStart("/wiki/");
		xeh
				.setFeedFormat("<a href=\"{0}pdf\" target=\"feeds\"><img src=\"/library/image/sakai/pdf.gif\" border=\"0\"  alt=\"PDF\" /></a>");
		HashMap responseHeaders = new HashMap();
		responseHeaders.put("content-type", "application/pdf");
		xeh.setResponseHeaders(responseHeaders);
		HashMap outputProperties = new HashMap();
		outputProperties
				.put("{http://xml.apache.org/xalan}content-handler",
						"uk.ac.cam.caret.sakai.rwiki.component.service.impl.FOP2PDFSerializer");
		xeh.setOutputProperties(outputProperties);

		MockHttpServletResponse response = new MockHttpServletResponse();
		xeh.init();
		xeh.outputContent(rwe, rwe,  request, response);
		File f = new File("testoutput.pdf");
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(response.getContentAsByteArray());
		fo.close();

		long start = System.currentTimeMillis();
		int iters = 10;
		for (int j = 0; j < iters; j++)
		{
			response = new MockHttpServletResponse();
			xeh.outputContent(rwe, rwe, request, response);
		}
		float timet = (float) 1.0 * (System.currentTimeMillis() - start);
		float tper = (float) (timet / (1.0 * iters));
		logger.info("Transform and Serialize Call Cost = " + tper + " ms");

	}

	public void xtestFOP() throws Exception
	{
		String transform = "/uk/ac/cam/caret/sakai/rwiki/component/service/impl/xhtml2fo.xslt";
		StringBuffer sb = new StringBuffer();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(testinputhtml)));
		String line = reader.readLine();
		while (line != null)
		{
			sb.append(line).append("\n");
			line = reader.readLine();
		}

		RWikiCurrentObjectImpl rwco = new RWikiCurrentObjectImpl();
		RWikiEntity rwe = new RWikiEntityImpl(rwco);
		rwco.setContent(sb.toString());
		rwco.setGroupAdmin(false);
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setOwner("The Owner");
		rwco.setUser("The User");
		rwco.setVersion(new Date());
		rwco.setRevision(Integer.valueOf(5));

		MockHttpServletRequest request = new MockHttpServletRequest();

		XSLTEntityHandler xeh = new XSLTEntityHandler();
		xeh.setAccessURLStart("/wiki/");
		xeh.setAnchorLinkFormat("/wiki{0}.html#{1}");
		xeh.setXslt(transform);
		xeh.setMinorType("pdf");
		xeh.setDefaultStackTrace("Failed To generate Stack Trace : {0}");
		xeh
				.setErrorFormat("Error encounvered performing transform : {0} \n {1}");
		xeh.setAuthZPrefix("/wiki");
		xeh.setAnchorLinkFormat("/wiki{0}.html#{1}");
		xeh.setStandardLinkFormat("/wiki{0}.html");
		xeh.setHrefTagFormat("<a href=\"{0}\" >{1}</a>");
		xeh.setAccessURLStart("/wiki/");
		xeh
				.setFeedFormat("<a href=\"{0}pdf\" target=\"feeds\"><img src=\"/library/image/sakai/pdf.gif\" border=\"0\"  alt=\"PDF\" /></a>");
		HashMap responseHeaders = new HashMap();
		responseHeaders.put("content-type", "application/pdf");
		xeh.setResponseHeaders(responseHeaders);
		HashMap outputProperties = new HashMap();
		// outputProperties.put("{http://xml.apache.org/xalan}content-handler","uk.ac.cam.caret.sakai.rwiki.component.service.impl.FOP2PDFSerializer");
		xeh.setOutputProperties(outputProperties);

		MockHttpServletResponse response = new MockHttpServletResponse();
		xeh.init();
		xeh.outputContent(rwe, rwe, request, response);
		File f = new File("testoutputfop.xml");
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(response.getContentAsByteArray());
		fo.close();

		long start = System.currentTimeMillis();
		int iters = 10;
		for (int j = 0; j < iters; j++)
		{
			response = new MockHttpServletResponse();
			xeh.outputContent(rwe, rwe, request, response);
		}
		float timet = (float) 1.0 * (System.currentTimeMillis() - start);
		float tper = (float) (timet / (1.0 * iters));
		logger.info("Transform and Serialize Call Cost = " + tper + " ms");

	}

	public void xtestNULL() throws Exception
	{
		String transform = "/uk/ac/cam/caret/sakai/rwiki/component/service/impl/null.xslt";
		StringBuffer sb = new StringBuffer();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				getClass().getResourceAsStream(testinputhtml)));
		String line = reader.readLine();
		while (line != null)
		{
			sb.append(line).append("\n");
			line = reader.readLine();
		}

		RWikiCurrentObjectImpl rwco = new RWikiCurrentObjectImpl();
		RWikiEntity rwe = new RWikiEntityImpl(rwco);
		rwco.setContent(sb.toString());
		rwco.setGroupAdmin(false);
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setId("/site/sdf-sdf-sdf-sdf-sdf-sfd/SomePage/sdfgsfd/Home");
		rwco.setOwner("The Owner");
		rwco.setUser("The User");
		rwco.setVersion(new Date());
		rwco.setRevision(Integer.valueOf(5));

		MockHttpServletRequest request = new MockHttpServletRequest();

		XSLTEntityHandler xeh = new XSLTEntityHandler();
		xeh.setAccessURLStart("/wiki/");
		xeh.setAnchorLinkFormat("/wiki{0}.html#{1}");
		xeh.setXslt(transform);
		xeh.setMinorType("pdf");
		xeh.setDefaultStackTrace("Failed To generate Stack Trace : {0}");
		xeh
				.setErrorFormat("Error encounvered performing transform : {0} \n {1}");
		xeh.setAuthZPrefix("/wiki");
		xeh.setAnchorLinkFormat("/wiki{0}.html#{1}");
		xeh.setStandardLinkFormat("/wiki{0}.html");
		xeh.setHrefTagFormat("<a href=\"{0}\" >{1}</a>");
		xeh.setAccessURLStart("/wiki/");
		xeh
				.setFeedFormat("<a href=\"{0}pdf\" target=\"feeds\"><img src=\"/library/image/sakai/pdf.gif\" border=\"0\"  alt=\"PDF\" /></a>");
		HashMap responseHeaders = new HashMap();
		responseHeaders.put("content-type", "application/pdf");
		xeh.setResponseHeaders(responseHeaders);
		HashMap outputProperties = new HashMap();
		// outputProperties.put("{http://xml.apache.org/xalan}content-handler","uk.ac.cam.caret.sakai.rwiki.component.service.impl.FOP2PDFSerializer");
		xeh.setOutputProperties(outputProperties);

		MockHttpServletResponse response = new MockHttpServletResponse();
		xeh.init();
		xeh.outputContent(rwe, rwe, request, response);
		File f = new File("testoutput.xml");
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(response.getContentAsByteArray());
		fo.close();

		long start = System.currentTimeMillis();
		int iters = 10;
		for (int j = 0; j < iters; j++)
		{
			response = new MockHttpServletResponse();
			xeh.outputContent(rwe, rwe, request, response);
		}
		float timet = (float) 1.0 * (System.currentTimeMillis() - start);
		float tper = (float) (timet / (1.0 * iters));
		logger.info("Transform and Serialize Call Cost = " + tper + " ms");

	}
	
	public void testDummy() {
		
	}

}
