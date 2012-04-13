/**********************************************************************************
*
 * Copyright (c) 2003, 2004, 2007, 2008 The Sakai Foundation
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
package edu.indiana.lib.twinpeaks.util;

import java.lang.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.w3c.dom.html.*;
import org.xml.sax.*;



public class LogUtils {
  /**
   * Get a Log instance
   * @param logClass Java Class being logged
   */
	public static Log getLog(Class logClass) {
  	return LogFactory.getLog(logClass);
	}

  /**
   * Get a Log instance
   * @param logName Name being logged
   */
	public static Log getLog(String logName) {
  	return LogFactory.getLog(logName);
	}

	/**
	 * Serialize an XML object (Document or Element) to the log
	 * @param log Apache Log object
	 * @param recordElement The XML object to disolay (Document, Element)
	 */
	public static void displayXml(org.apache.commons.logging.Log log,
																Object xmlObject) {
		displayXml(log, null, xmlObject);
	}

	/**
	 * Serialize an XML object (Document or Element) to the log (with an
	 * optional warning header)
	 * @param log Apache Log object
	 * @param errorText Error message (null for none)
	 * @param recordElement The XML object to disolay (Document, Element)
	 */
	public static void displayXml(org.apache.commons.logging.Log log,
																String errorText,
																Object xmlObject) {

  	if (!(xmlObject instanceof Document) && !(xmlObject instanceof Element)) {
     	throw new IllegalArgumentException("Unexpected object for serialzation: "
     																	+ 	xmlObject.toString());
		}

		if (!StringUtils.isNull(errorText)) {
			log.error(errorText);
		}

		log.info("Record Start ----------------------------------------");

		try {
			log.info(DomUtils.serialize(xmlObject));
		} catch (DomException exception) {
			log.error("Failed to serialize element " +	xmlObject.toString(),
									exception);
		}
		log.info("Record End ------------------------------------------");
	}

	/*
	 * Test
	 */
  public static void main(String[] args) {

		Log log = LogUtils.getLog(LogUtils.class);

		log.debug("Debug");
		log.error("Error");
		log.warn("Warn");
		log.info("Info");
	}
}