/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/mailarchive/branches/sakai-2.8.1/mailarchive-impl/impl/src/java/org/sakaiproject/mailarchive/impl/conversion/ExtractXMLToColumns.java $
 * $Id: ExtractXMLToColumns.java 73049 2010-02-01 12:33:49Z david.horwitz@uct.ac.za $
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

package org.sakaiproject.mailarchive.impl.conversion;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.util.conversion.SchemaConversionHandler;

import org.sakaiproject.util.commonscodec.CommonsCodecBase64;

/**
 * @author ieb
 */
public class ExtractXMLToColumns implements SchemaConversionHandler
{

	private static final Log log = LogFactory
			.getLog(ExtractXMLToColumns.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.sakaiproject.content.impl.serialize.impl.SchemaConversionHandler#getSource(java.lang.String,
	 *      java.sql.ResultSet)
	 */
	public Object getSource(String id, ResultSet rs) throws SQLException
	{
		ResultSetMetaData metadata = rs.getMetaData();
		String rv = null;
		switch(metadata.getColumnType(1))
		{
		case Types.BLOB:
			Blob blob = rs.getBlob(1);
			if(blob != null)
			{
				rv = new String(blob.getBytes(1L, (int) blob.length()));
			}
			break;
		case Types.CLOB:
			Clob clob = rs.getClob(1);
			if(clob != null)
			{
				rv = clob.getSubString(1L, (int) clob.length());
			}
			break;
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			byte[] bytes = rs.getBytes(1);
			if(bytes != null)
			{
				rv = new String(bytes);
			}
			break;
		}
		// System.out.println("getSource(" + id + ") \n" + rv + "\n");
		return rv;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.sakaiproject.content.impl.serialize.impl.SchemaConversionHandler#convertSource(java.lang.String,
	 *      java.lang.Object, java.sql.PreparedStatement)
	 */
	public boolean convertSource(String id, Object source, PreparedStatement updateRecord)
			throws SQLException
	{
		// System.out.println("convertSource id="+id+" prep="+updateRecord+" source="+source);
		String xml = (String) source;

		if (!xml.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"))
		{
			log.warn("Improperly formatted XML");
			return false;
		}

		/*
		 * <?xml version="1.0" encoding="UTF-8"?> <message
		 * body="Qm9keSAyMDA4MDEyNzIwMTM0MTkzMw=="
		 * body-html="Qm9keSAyMDA4MDEyNzIwMTM0MTkzMw=="> <header
		 * access="channel" date="20080127201341934" from="admin"
		 * id="d978685c-8730-4975-b3ea-55fdf03e0e5a"
		 * mail-date="20080127201341933" mail-from="from 20080127201341933"
		 * subject="Subject 20080127201341933"/><properties/></message>
		 */
		String body = getXmlAttr(xml, "body");
		String subject = getXmlAttr(xml, "subject");

		byte[] decoded = null;
		try 
		{
			if ( body != null ) 
			{
				decoded = CommonsCodecBase64.decodeBase64(body.getBytes("UTF-8"));
				body = new String(decoded, "UTF-8");
			}
		} 
		catch (Exception e) 
		{
			log.warn("Error Base64 Decoding Body and HTML Body");
			return false;
		}

		updateRecord.setString(1, subject);
		updateRecord.setString(2, body);
		updateRecord.setString(3, id);
		return true;
	}

	String getXmlAttr(String xml, String tagName)
	{
		String lookfor = tagName+"=\""; 
		int ipos = xml.indexOf(lookfor);
		if ( ipos < 1 ) return null;
		ipos = ipos + lookfor.length();
		int jpos = xml.indexOf("\"",ipos);
		if ( jpos < 1 || ipos > jpos ) return null;
		return xml.substring(ipos,jpos);
	}

	/**
	 * @see org.sakaiproject.util.conversion.SchemaConversionHandler#validate(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void validate(String id, Object source, Object result) throws Exception
	{
		// System.out.println("validate id="+id+" source="+source+" result="+result);
	}

	/* (non-Javadoc)
	 * @see org.sakaiproject.content.impl.serialize.impl.conversion.SchemaConversionHandler#getValidateSource(java.lang.String, java.sql.ResultSet)
	 */
	public Object getValidateSource(String id, ResultSet rs) throws SQLException
	{
		ResultSetMetaData metadata = rs.getMetaData();
		byte[] rv = null;
		switch(metadata.getColumnType(1))
		{
		case Types.BLOB:
			Blob blob = rs.getBlob(1);
			if(blob != null)
			{
				rv = blob.getBytes(1L, (int) blob.length());
			}
			else
			{
				System.out.println("getValidateSource(" + id + ") blob ==  null" );
			}
			break;
		case Types.CLOB:
			Clob clob = rs.getClob(1);
			if(clob != null)
			{
				rv = clob.getSubString(1L, (int) clob.length()).getBytes();
			}
			break;
		case Types.CHAR:
		case Types.LONGVARCHAR:
		case Types.VARCHAR:
			rv = rs.getString(1).getBytes();
			break;
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			rv = rs.getBytes(1);
			break;
		}
		// System.out.println("getValidateSource(" + id + ") \n" + rv + "\n");
		return rv;
	}



}
