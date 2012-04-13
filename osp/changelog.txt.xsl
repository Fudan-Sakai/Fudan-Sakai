<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
    xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
    version='1.0'>

<!--
    Copyright  2002,2004 The Apache Software Foundation
   
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
   
         http://www.apache.org/licenses/LICENSE-2.0
   
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
   
-->
  <xsl:param name="title"/>
  <xsl:param name="module"/>
  <xsl:param name="cvsweb"/>

  <!-- Copy standard document elements.  Elements that
       should be ignored must be filtered by apply-templates
       tags. -->
  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="attribute::*[. != '']"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="changelog">
<!--
    <html>
      <head>
        <title><xsl:value-of select="$title"/></title>
        <style type="text/css">
          body, p {
            font-family: Verdana, Arial, Helvetica, sans-serif;
            font-size: 80%;
            color: #000000;
            background-color: #ffffff;
          }
          tr, td {
            font-family: Verdana, Arial, Helvetica, sans-serif;
            background: #eeeee0;
          }
          td {
            padding-left: 20px;
          }
      .dateAndAuthor {
            font-family: Verdana, Arial, Helvetica, sans-serif;
            font-weight: bold;
            text-align: left;
            background: #a6caf0;
            padding-left: 3px;
      }
          a {
            color: #000000;
          }
          pre {
            font-weight: bold;
          }
        </style>
      </head>
      -->
<xsl:value-of select="$title"/>
---------------------------------

 <xsl:apply-templates select=".//entry">
   <xsl:sort select="date" data-type="text" order="descending"/>
   <xsl:sort select="time" data-type="text" order="descending"/>
 </xsl:apply-templates>

  </xsl:template>

  <xsl:template match="entry">
 <!--
    <tr>
      <td class="dateAndAuthor">
        <xsl:value-of select="date"/><xsl:text> </xsl:text><xsl:value-of select="time"/><xsl:text> </xsl:text><xsl:value-of select="author"/>
      </td>
    </tr>
-->
<xsl:apply-templates select="msg"/>
<!--
        <ul>
          <xsl:apply-templates select="file"/>
        </ul>
-->
----------------------------------
  </xsl:template>
<!--
  <xsl:template match="date">
    <i><xsl:value-of select="."/></i>
  </xsl:template>

  <xsl:template match="time">
    <i><xsl:value-of select="."/></i>
  </xsl:template>
-->

  <xsl:template match="author">
    <i>
      <a>
        <xsl:attribute name="href">mailto:<xsl:value-of select="."/></xsl:attribute>
        <xsl:value-of select="."/></a>
    </i>
  </xsl:template>

  <xsl:template match="file">
    <li>
      <a>
        <xsl:choose>
          <xsl:when test="string-length(prevrevision) = 0 ">
            <xsl:attribute name="href"><xsl:value-of select="$cvsweb"/><xsl:value-of select="$module" />/<xsl:value-of select="name" />?rev=<xsl:value-of select="revision" />&amp;content-type=text/x-cvsweb-markup</xsl:attribute>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="href"><xsl:value-of select="$cvsweb"/><xsl:value-of select="$module" />/<xsl:value-of select="name" />?r1=<xsl:value-of select="revision" />&amp;r2=<xsl:value-of select="prevrevision"/></xsl:attribute>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:value-of select="name" /> (<xsl:value-of select="revision"/>)</a>
    </li>
  </xsl:template>


  <!-- Any elements within a msg are processed,
       so that we can preserve HTML tags. -->
<xsl:template match="msg">
<xsl:apply-templates/>
</xsl:template>
  
</xsl:stylesheet>
