<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:osp="http://www.osportfolio.org/OspML"
	xmlns:xs="http://www.w3.org/2001/XMLSchema">

   <xsl:output method="html" version="4.01"  
      encoding="utf-8" indent="yes" doctype-public="http://www.w3.org/TR/html4/loose.dtd"/>

   <xsl:variable name="config" select="/portal/config" />
   <xsl:variable name="externalized" select="/portal/externalized" />

   <xsl:variable name="roles" select="/portal/roles"/>

   <!--
   ============match /portal===============
   main template processing
   ========================================
   -->
	<xsl:template match="portal">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

   <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

      <xsl:for-each select="skins/skin">
         <link type="text/css" rel="stylesheet" media="all">
            <xsl:attribute name="href">
               <xsl:value-of select="."/>
            </xsl:attribute>
         </link>
      </xsl:for-each>

    <meta http-equiv="Content-Style-Type" content="text/css" />
      <title><xsl:value-of select="pageTitle" /></title>
      <script type="text/javascript" language="JavaScript" src="/library/js/headscripts.js">
         <xsl:value-of select="' '"/>
      </script>
   </head>
<body class="portalBody">
	<xsl:if test="loginInfo/topLogin = 'true' and not(currentUser)">
		<xsl:attribute name="onload">document.forms[0].eid.focus();</xsl:attribute>
	</xsl:if>

   <a href="#tocontent"  class="skip" accesskey="c">
   <xsl:attribute name="title">
      <xsl:value-of select="$externalized/entry[@key='sit.jumpcontent']"/>
   </xsl:attribute>
   <xsl:value-of select="$externalized/entry[@key='sit.jumpcontent']"/>
</a>
<a href="#toolmenu"  class="skip" accesskey="l">
   <xsl:attribute name="title">
      <xsl:value-of select="$externalized/entry[@key='sit.jumptools']"/>
   </xsl:attribute>
   <xsl:value-of select="$externalized/entry[@key='sit.jumptools']"/>
</a>
<a href="#sitetabs" class="skip" title="jump to worksite list" accesskey="w">
   <xsl:attribute name="title">
      <xsl:value-of select="$externalized/entry[@key='sit.jumpworksite']"/>
   </xsl:attribute>
   <xsl:value-of select="$externalized/entry[@key='sit.jumpworksite']"/>
</a>

<table border="0"  width="100%">
   <tr>
      <td>
         <xsl:call-template name="site_tabs" />
      </td>
   </tr>
   <tr>
      <td>
         <xsl:if test="currentUser">
            <xsl:call-template name="breadcrumbs" />
         </xsl:if>
      </td>
   </tr>
   <tr>
      <td>
         <div id="portalOuterContainer">
            <div id="portalContainer">
<div id="container">
   <xsl:attribute name="class">
      <xsl:value-of select="siteTypes/siteType[@selected='true']/name"/>
   </xsl:attribute>

   <xsl:choose>
      <xsl:when test="siteTypes/siteType/sites/site[@selected='true']">
         <xsl:call-template name="site_tools" />
         <xsl:for-each select="categories/category" >
            <xsl:sort select="@order" data-type="number" />
            <xsl:apply-templates select=".">
               <xsl:with-param name="content" select="'true'"/>
            </xsl:apply-templates>
         </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
         <xsl:call-template name="portal_tool">
            <xsl:with-param name="base" select="siteTypes/siteType[@selected='true']"/>
         </xsl:call-template>
      </xsl:otherwise>
   </xsl:choose>

<div>
<xsl:call-template name="footer"/>
</div>
</div>
</div>
</div>
      </td>
   </tr>
</table>
</body></html>
	</xsl:template>

   <!--
   ===============name portal_tool========================
   setup an iframe with the currently selected helper tool
   param: base - the node to get key and helperUrl from
   =======================================================
   -->
   <xsl:template name="portal_tool">
      <xsl:param name="base"/>
      <xsl:variable name="key" select="$base/key"/>
      <h1 class="skip">
         <xsl:value-of select="$externalized/entry[@key='sit.contentshead']"/>
      </h1>
      <a id="tocontent" class="skip" name="tocontent"></a>
      <div id="content">
         <div id="col1">
            <div class="portlet">
<div class="portletMainWrap">
<iframe
class ="portletMainIframe"
height="50"
width="100%"
frameborder="0"
marginwidth="0"
marginheight="0"
scrolling="auto">
<xsl:attribute name="title">
   <xsl:value-of select="$externalized/entry[@key=$key]" />
</xsl:attribute>
<xsl:attribute name="name">Main<xsl:value-of select="$base/escapedKey" /></xsl:attribute>
<xsl:attribute name="id">Main<xsl:value-of select="$base/escapedKey" /></xsl:attribute>
<xsl:attribute name="src"><xsl:value-of select="$base/helperUrl" /></xsl:attribute>
your browser doesn't support iframes
</iframe>
</div>
            </div>
      </div>
   </div>
   </xsl:template>


   <!--
   =========match category that isn't categorized=====================
   process a tool category
   param:content - "true" or "false" if rendering tool content or tool list
   =================================
   -->
   <xsl:template match="category[key='org.theospi.portfolio.portal.model.ToolCategory.uncategorized']">
      <xsl:param name="content"/>
      <xsl:for-each select="pages/page" >
         <xsl:sort select="@order" data-type="number"/>
         <xsl:apply-templates select=".">
            <xsl:with-param name="content" select="$content"/>
         </xsl:apply-templates>
      </xsl:for-each>
   </xsl:template>

   <!--
   =========match category selected================
   process a tool category
   param:content - "true" or "false" if rendering tool content or tool list
   ================================================
   -->
   <xsl:template match="category[@selected='true']">
      <xsl:param name="content"/>
      <xsl:if test="$content != 'true'">
         <xsl:variable name="key" select="key"/>
         <li>
            <a accesskey="1" class="selected">
               <xsl:attribute name="href">
                  <xsl:value-of select="url"/>
               </xsl:attribute>            
               <xsl:attribute name="accesskey">
                  <xsl:value-of select="@order"/>
               </xsl:attribute>
               <xsl:attribute name="title">
                  <xsl:value-of select="$externalized/entry[@key=$key]"/>
               </xsl:attribute>
               <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </a>
         </li>
      </xsl:if>
      <xsl:if test="$content='true'">
         <xsl:choose>
         <xsl:when test="pages/page[@selected='true']">
            <xsl:for-each select="pages/page" >
               <xsl:sort select="@order" data-type="number"/>
               <xsl:apply-templates select=".">
                  <xsl:with-param name="content" select="$content"/>
               </xsl:apply-templates>
            </xsl:for-each>
         </xsl:when>
            <xsl:otherwise>
               <!-- make the tool category here -->
               <!--xsl:call-template name="portal_tool">
                  <xsl:with-param name="base" select="."/>
               </xsl:call-template-->
               <xsl:call-template name="tool_category">
                  <xsl:with-param name="category" select="."/>
               </xsl:call-template>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:if>
   </xsl:template>

   <!--
   =========match category================
   process a tool category
   param:content - "true" or "false" if rendering tool content or tool list
   ================================================
   -->
   <xsl:template match="category">
      <xsl:param name="content"/>
      <xsl:if test="$content != 'true'">
         <xsl:variable name="key" select="key"/>
         <li>
            <a>
               <xsl:attribute name="href">
                  <xsl:value-of select="url"/>
               </xsl:attribute>
               <xsl:attribute name="accesskey">
                  <xsl:value-of select="@order" />
               </xsl:attribute>
               <xsl:attribute name="title">
                  <xsl:value-of select="$externalized/entry[@key=$key]"/>
               </xsl:attribute>
               <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </a>
         </li>
      </xsl:if>
   </xsl:template>

   <!--
   ========name site_tabs============
   Handle putting up the site tabs
   ===============================
   -->
   <xsl:template name="site_tabs">
      <!-- site tabs here -->
<div id="siteNavWrapper">
   <xsl:attribute name="class">
      <xsl:value-of select="siteTypes/siteType[@selected='true']/name"/>
   </xsl:attribute>

   <div id="mastHead">
      <div id="mastLogo">
         <img title="Logo" alt="Logo">
            <xsl:attribute name="src">
               <xsl:value-of select="config/logo"/>
            </xsl:attribute>
         </img>
      </div>
      <div id="mastBanner">
         <img title="Banner" alt="Banner">
            <xsl:attribute name="src">
               <xsl:value-of select="config/banner"/>
            </xsl:attribute>
         </img>
      </div>
      <div id="mastLogin">
        <xsl:choose>
        <xsl:when test="currentUser">
           <div id="loginLinks">
              <a target="_parent">
                 <xsl:attribute name="href">
                    <xsl:value-of select="config/logout"/>
                 </xsl:attribute>
                 <xsl:attribute name="title">
                    <xsl:value-of select="loginInfo/logoutText"/>
                 </xsl:attribute>
                 <xsl:value-of select="loginInfo/logoutText"/>
              </a>
           </div>
        </xsl:when>
        <xsl:when test="loginInfo/topLogin != 'true'">
        		<!-- use links instead of form fields -->
        		<div id="loginLinks">
			<a target="_parent">
				<xsl:attribute name="href">
                    <xsl:value-of select="loginInfo/logInOutUrl"/>
                 </xsl:attribute>
                 <xsl:attribute name="title">
                    <xsl:value-of select="loginInfo/loginText"/>
                 </xsl:attribute>
				<xsl:choose>
					<xsl:when test="loginInfo/image1">
						<img>
							<xsl:attribute name="src">
                    <xsl:value-of select="loginInfo/image1"/>
                 </xsl:attribute>
                 <xsl:attribute name="alt">
                    <xsl:value-of select="loginInfo/loginText"/>
                 </xsl:attribute>
						</img>
					</xsl:when>
					<xsl:otherwise><xsl:value-of select="loginInfo/loginText"/></xsl:otherwise>			
			</xsl:choose>
			</a>
			<xsl:if test="loginInfo/logInOutUrl2">
				<a target="_parent">
				<xsl:attribute name="href">
                    <xsl:value-of select="loginInfo/logInOutUrl2"/>
                 </xsl:attribute>
                 <xsl:attribute name="title">
                    <xsl:value-of select="loginInfo/loginText2"/>
                 </xsl:attribute>
				<xsl:choose>
					<xsl:when test="loginInfo/image2">
						<img>
							<xsl:attribute name="src">
                    <xsl:value-of select="loginInfo/image2"/>
                 </xsl:attribute>
                  <xsl:attribute name="alt">
                    <xsl:value-of select="loginInfo/loginText2"/>
                 </xsl:attribute>
						</img>
					</xsl:when>
					<xsl:otherwise><xsl:value-of select="loginInfo/loginText2"/></xsl:otherwise>		
					</xsl:choose>
					</a>
			</xsl:if>
			
			
			</div>
        		
        		
        		
        </xsl:when>
        <xsl:otherwise>
<form method="post" action="/osp-portal/xlogin" enctype="application/x-www-form-urlencoded" target="_parent">
<xsl:value-of select="$externalized/entry[@key='log.userid']"/>
<input name="eid" id="eid" type="text" style ="width: 10em" />

<xsl:value-of select="$externalized/entry[@key='log.pass']"/>
<input name="pw" id="pw" type="password" style ="width: 10em" />
<input name="submit" type="submit" id="submit">
   <xsl:attribute name="value">
      <xsl:value-of select="$externalized/entry[@key='log.login']"/>
   </xsl:attribute>
</input>
<br/>
</form>
         </xsl:otherwise>
         </xsl:choose>
      </div>
   </div>
   <div>
      <xsl:attribute name="class">siteNavWrap
         <xsl:value-of select="siteTypes/siteType[@selected='true']/name"/>
      </xsl:attribute>

         <xsl:choose>
            <xsl:when test="currentUser and siteTabs/div[@id='blank']/div/div[@id='siteNav']">
               <xsl:comment>reverting to charon portal site navigation</xsl:comment>
               <xsl:copy-of select="siteTabs/div[@id='blank']/div/div[@id='siteNav']"/>
            </xsl:when>
            <xsl:when test="currentUser">
               <div id="siteNav">
                  <div id="linkNav">
                     <a id="sitetabs" class="skip" name="sitetabs"></a>
                     <h1 class="skip">Worksites begin here</h1>
                     <ul id="siteLinkList">
                        <xsl:for-each select="siteTypes/siteType">
                           <xsl:sort select="@order" data-type="number"/>
                           <xsl:apply-templates select="." >
                              <xsl:with-param name="extra" select="'false'" />
                           </xsl:apply-templates>
                        </xsl:for-each>
                        <li style="display:none;border-width:0" class="fixTabsIE"><a href="javascript:void(0);">#x20;</a></li>
                     </ul>
                  </div>
               </div>
            </xsl:when>
            <xsl:otherwise>
               <div id="siteNav">
               </div>
            </xsl:otherwise>
         </xsl:choose>
      <div class="divColor" id="tabBottom">
      <br/>
      </div>
   </div>
</div>
   </xsl:template>

   <!--
   =========match selected 1 column layous============
   process a selected page with one column layouts
   param:content - "true" or "false" if rendering tool content or tool list
   ===================================================
   -->
   <xsl:template match="page[@layout='0' and @selected='true']">
      <xsl:param name="content"/>
      <xsl:if test="$content='true'">
         <xsl:call-template name="page-content">
            <xsl:with-param name="page" select="."/>
         </xsl:call-template>
      </xsl:if>
      <xsl:if test="$content='false'">
         <li>
            <a accesskey="1" class="selected" href="#">
               <xsl:attribute name="accesskey">
                  <xsl:value-of select="../../@order"/>
               </xsl:attribute>
               <xsl:value-of select="title"/>
            </a>
         </li>
      </xsl:if>
   </xsl:template>

   <!--
   ===============match selected 2 column layous============
   process a selected page with two column layouts
   param:content - "true" or "false" if rendering tool content or tool list
   =========================================================
   -->
   <xsl:template match="page[@layout='1' and @selected='true']">
      <xsl:param name="content"/>
      <xsl:if test="$content='true'">
         <xsl:call-template name="page-content-columns">
            <xsl:with-param name="page" select="."/>
         </xsl:call-template>
      </xsl:if>
      <xsl:if test="$content='false'">
         <li>
            <a accesskey="1" class="selected" href="#">
               <xsl:attribute name="accesskey">
                  <xsl:value-of select="../../@order"/>
               </xsl:attribute>
               <xsl:value-of select="title"/>
            </a>
         </li>
      </xsl:if>
   </xsl:template>

   <!--
   ===============match page (default case)=================
   process a page
   param:content - "true" or "false" if rendering tool content or tool list
   =========================================================
   -->
   <xsl:template match="page">
      <xsl:param name="content"/>
      <xsl:if test="$content='true'">
         <!-- do nothing -->
      </xsl:if>
      <xsl:if test="$content='false'">
         <li>
            <a>
               <xsl:if test="@popUp='false'">
                  <xsl:attribute name="href">
                     <xsl:value-of select="url"/>
                  </xsl:attribute>
               </xsl:if>
               <xsl:if test="@popUp='true'">
                  <xsl:attribute name="href">#</xsl:attribute>
                  <xsl:attribute name="onclick">
                     window.open('<xsl:value-of select="columns/column/tools/tool/url"/>',
                        '<xsl:value-of select="title"/>',
                        'resizable=yes,toolbar=no,scrollbars=yes, width=800,height=600')
                  </xsl:attribute>
               </xsl:if>
               <xsl:attribute name="accesskey">
                  <xsl:value-of select="../../@order" />
               </xsl:attribute>
               <xsl:value-of select="title"/>
            </a>
         </li>
      </xsl:if>
   </xsl:template>

   <!--
   ======================name page-content============
   process a page's content
   param:page - node for the current page
   ===================================================
   -->
   <xsl:template name="page-content">
      <xsl:param name="page"/>
      <h1 class="skip">
         <xsl:value-of select="$externalized/entry[@key='sit.contentshead']"/>
      </h1>
      <a id="tocontent" class="skip" name="tocontent"></a>
<div id="content">
   <div id="col1">
      <div class="portlet">

   <xsl:for-each select="$page/columns/column[@index='0']/tools/tool">
      <xsl:call-template name="tool">
         <xsl:with-param name="tool" select="."/>
      </xsl:call-template>
   </xsl:for-each>

      </div>
   </div>
      </div>
   </xsl:template>

   <!--
   ================name page-content-columns================
   process a page's content
   param:page - node for the current page
   =========================================================
   -->
   <xsl:template name="page-content-columns">
      <xsl:param name="page"/>
      <h1 class="skip">
         <xsl:value-of select="$externalized/entry[@key='sit.contentshead']"/>
      </h1>
      <a id="tocontent" class="skip" name="tocontent"></a>
      <div id="content">
         <div id="col1of2">
            <div class="portlet">
         <xsl:for-each select="$page/columns/column[@index='0']/tools/tool">
            <xsl:call-template name="tool">
               <xsl:with-param name="tool" select="."/>
            </xsl:call-template>
         </xsl:for-each>
            </div>
         </div>
         <div id="col2of2">
         <div class="portlet">
         <xsl:for-each select="$page/columns/column[@index='1']/tools/tool">
            <xsl:call-template name="tool">
               <xsl:with-param name="tool" select="."/>
            </xsl:call-template>
         </xsl:for-each>
         </div>
      </div>
      </div>
   </xsl:template>

   <!--
   ================name tool===============================
   process a tool for displaying content
   param:tool - node for the current tool
   ========================================================
   -->
   <xsl:template name="tool">
      <xsl:param name="tool"/>

<div class="portletTitleWrap">
<iframe
	class ="portletTitleIframe"
	height="22"
	width="99%"
	frameborder="0"
	marginwidth="0"
	marginheight="0"
	scrolling="no">
   <xsl:attribute name="title">
      <xsl:value-of select="$tool/title" />
   </xsl:attribute>
   <xsl:attribute name="name">Title<xsl:value-of select="$tool/escapedId" /></xsl:attribute>
   <xsl:attribute name="id">Title<xsl:value-of select="$tool/escapedId" /></xsl:attribute>
   <xsl:attribute name="src">
      <xsl:value-of select="$tool/titleUrl" />
   </xsl:attribute>
   your browser doesn't support iframes
</iframe>
</div>
<div class="portletMainWrap">
<iframe
	class ="portletMainIframe"
	height="50"
	width="100%"
	frameborder="0"
	marginwidth="0"
	marginheight="0"
	scrolling="auto">
   <xsl:attribute name="title">
      <xsl:value-of select="$tool/title" />
   </xsl:attribute>
   <xsl:attribute name="name">Main<xsl:value-of select="$tool/escapedId" /></xsl:attribute>
   <xsl:attribute name="id">Main<xsl:value-of select="$tool/escapedId" /></xsl:attribute>
   <xsl:attribute name="src">
      <xsl:value-of select="$tool/url" />
   </xsl:attribute>
   your browser doesn't support iframes
</iframe>
</div>
   </xsl:template>

   <!--
   ======================name site_tools====================
   process the site tools list
   =============================================================
   -->
   <xsl:template name="site_tools">
<div class="divColor" id="toolMenuWrap">
	<div id="worksiteLogo">
      <xsl:if test="siteTypes/siteType[@selected='true']/sites/site[@selected='true' and @published='false']">
         <p id="siteStatus">unpublished site</p>
      </xsl:if>
	</div>
	<a id="toolmenu" class="skip" name="toolmenu"></a>
	<h1 class="skip">
      <xsl:value-of select="$externalized/entry[@key='sit.toolshead']"/>
	</h1>

	<div id="toolMenu">
		<ul>

<xsl:for-each select="categories/category" >
   <xsl:sort select="@order" data-type="number"/>
   <xsl:apply-templates select=".">
      <xsl:with-param name="content" select="'false'"/>
   </xsl:apply-templates>
</xsl:for-each>

         <li>
				<a  accesskey="h" href="javascript:;">
               <xsl:attribute name="onclick">
                  window.open('<xsl:value-of select="config/helpUrl"/>','Help','resizable=yes,toolbar=no,scrollbars=yes, width=800,height=600')
               </xsl:attribute>
               <xsl:attribute name="onkeypress">
                  window.open('<xsl:value-of select="config/helpUrl"/>','Help','resizable=yes,toolbar=no,scrollbars=yes, width=800,height=600')
               </xsl:attribute>
               Help</a>
			</li>

      </ul>
	</div>

   <xsl:if test="$config/presence[@include='true']">
      <xsl:call-template name="presence" />
   </xsl:if>

</div>
   </xsl:template>

   <!--
   ===============name footer==========================
   process the main portal footer
   ========================================================
   -->
   <xsl:template name="footer">
<div align="center" id="footer">
	<div class="footerExtNav" align="center">
   <xsl:for-each select="config/bottomNavs/bottomNav">
      <xsl:value-of select="." disable-output-escaping="yes"/>
      <xsl:if test="last() != position()">
         <xsl:value-of select="' | '" />
      </xsl:if>
   </xsl:for-each>
	</div>

	<div id="footerInfo">
      <span class="skip">
         <xsl:value-of select="$externalized/entry[@key='site.newwindow']"/>
      </span>
      <xsl:for-each select="config/poweredBy">
         <a href="http://sakaiproject.org" target="_blank">
            <img border="0" src="/library/image/sakai_powered.gif" alt="Powered by Sakai" />
         </a>
      </xsl:for-each>

      <br />
      <span class="sakaiCopyrightInfo"><xsl:value-of select="config/copyright"/><br />
         <xsl:value-of select="config/service"/> - <xsl:value-of select="config/serviceVersion"/> - Sakai <xsl:value-of
            select="config/sakaiVersion"/> - Server "<xsl:value-of select="config/server"/>"
      </span>
	</div>
</div>      
   </xsl:template>

   <!--
   ===============match siteType selected===============
   process the siteType element
   param:extra - if this is running during the "more" list
   ============================================================
   -->
   <xsl:template match="siteType[@userSite='true' and @selected='true']">
      <xsl:param name="extra"/>
      <xsl:variable name="key" select="key"/>
      <li class="selectedTab">
         <a>
            <xsl:attribute name="href">
               <xsl:value-of select="sites/site/url"/>
            </xsl:attribute>
            <xsl:attribute name="title">
               <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </xsl:attribute>
            <span>
            <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </span>
         </a>
      </li>
   </xsl:template>

   <!--
   ===============match siteType selected===============
   process the siteType element
   param:extra - if this is running during the "more" list
   ============================================================
   -->
   <xsl:template match="siteType[@userSite='true' and @selected='false']">
      <xsl:param name="extra"/>
      <xsl:variable name="key" select="key"/>
      <li>
         <a target="_parent">
            <xsl:attribute name="href">
               <xsl:value-of select="sites/site/url"/>
            </xsl:attribute>
            <xsl:attribute name="title">
               <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </xsl:attribute>
            <span>
            <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </span>
         </a>
      </li>
   </xsl:template>
   
   <!--
   ===============match siteType selected===============
   process the siteType element
   param:extra - if this is running during the "more" list
   ============================================================
   -->
   <xsl:template match="siteType[@userSite='false' and @selected='true']">
      <xsl:param name="extra"/>
      <xsl:variable name="key" select="key"/>
      <li class="selectedTab">
         <a>
            <xsl:attribute name="href">
               <xsl:value-of select="url"/>
            </xsl:attribute>
            <xsl:attribute name="title">
               <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </xsl:attribute>
            <span>
            <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </span>
         </a>
      </li>
   </xsl:template>

   <!--
   ===============match siteType===============
   process the siteType element
   param:extra - if this is running during the "more" list
   ============================================================
   -->
   <xsl:template match="siteType">
      <xsl:param name="extra"/>
      <xsl:variable name="key" select="key"/>
      <li>
         <a target="_parent">
            <xsl:attribute name="href">
               <xsl:value-of select="url"/>
            </xsl:attribute>
            <xsl:attribute name="title">
               <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </xsl:attribute>
            <span>
            <xsl:value-of select="$externalized/entry[@key=$key]"/>
            </span>
         </a>
      </li>
   </xsl:template>

   <!--
   ===============match site marked as an extra site===============
   process an extra site for navigation
   param:extra - if this is running during the "more" list
   =====================================================================
   -->
   <!--xsl:template match="site[@extra='true']">
      <xsl:param name="extra"/>
      <xsl:if test="$extra='true'">
         <option>
            <xsl:attribute name="title">
               <xsl:value-of select="title"/>
            </xsl:attribute>
            <xsl:attribute name="value">
               <xsl:value-of select="url"/>
            </xsl:attribute>
            <xsl:value-of select="title"/>
         </option>
      </xsl:if>
   </xsl:template-->

   <!--
   ===============match site that has been selected===============
   process a selected site for navigation
   param:extra - if this is running during the "more" list
   ===================================================================
   -->
   <!--xsl:template match="site[@selected='true']">
      <xsl:param name="extra"/>
      <xsl:if test="$extra='false'">
         <td>
            <a href="#">
               <xsl:value-of select="title"/>
            </a>
         </td>
      </xsl:if>
   </xsl:template-->

   <!--
   ===============match site (default case)===============
   process a selected site for navigation
   param:extra - if this is running during the "more" list
   ============================================================
   -->
   <!--xsl:template match="site">
      <xsl:param name="extra"/>
      <xsl:if test="$extra='false'">
         <td>
            <a target="_parent">
               <xsl:attribute name="href">
                  <xsl:value-of select="url"/>
               </xsl:attribute>
               <xsl:attribute name="title">
                  <xsl:value-of select="title"/>
               </xsl:attribute>
               <xsl:value-of select="title"/>
            </a>
         </td>
      </xsl:if>
   </xsl:template-->

   <!--
   ======-name presence===========================
   process the presence area
   ====================================================
   -->
   <xsl:template name="presence">
      <div class="presenceWrapper">
         <div id="presenceTitle">
         <xsl:value-of select="$externalized/entry[@key='sit.presencetitle']"/>
         </div>
         <iframe
            name="presence"
            id="presenceIframe"
            title="Users Present in Site"
            frameborder="0"
            marginwidth="0"
            marginheight="0"
            scrolling="auto">
            <xsl:attribute name="src">
               <xsl:value-of select="$config/presence"/>
            </xsl:attribute>
            Your browser doesn't support frames
         </iframe>
      </div>
   </xsl:template>

   <!--
   =================name breadcrumbs==============
   breadcumb processing
   ===============================================
   -->
   <xsl:template name="breadcrumbs">
      <xsl:variable name="siteTypeKey" select="siteTypes/siteType[@selected='true']/key"/>
      <xsl:variable name="toolCategoryKey" select="categories/category[@selected='true']/key"/>

<div class="breadcrumb breadcrumbHolder workspace">
            <!--Active link/breadcrum li gets the class selectedCrumb-->
            <xsl:if test="siteTypes/siteType[@selected='true']">
               <xsl:if test="siteTypes/siteType[@selected='true' and key!='org.theospi.portfolio.portal.myWorkspace'
                             and not(//portal/siteTabs/div[@id='blank']/div/div[@id='siteNav'])]">
                  <xsl:call-template name="breadcrumb_entry">
                     <xsl:with-param name="node" select="siteTypes/siteType[@selected='true']"/>
                     <xsl:with-param name="title" select="$externalized/entry[@key=$siteTypeKey]"/>
                     <xsl:with-param name="last" select="count(siteTypes/siteType/sites/site[@selected='true']) = 0"/>
                  </xsl:call-template>
               </xsl:if>
               <xsl:if test="siteTypes/siteType/sites/site[@selected='true']">
                  <xsl:call-template name="breadcrumb_entry">
                     <xsl:with-param name="node" select="siteTypes/siteType/sites/site[@selected='true']"/>
                     <xsl:with-param name="title" select="siteTypes/siteType/sites/site[@selected='true']/title"/>
                  </xsl:call-template>
                  <xsl:if test="categories/category[@selected='true']">
                     <xsl:if test="categories/category[key!='org.theospi.portfolio.portal.model.ToolCategory.uncategorized']">
                        <xsl:call-template name="breadcrumb_entry">
                           <xsl:with-param name="node" select="categories/category[@selected='true']"/>
                           <xsl:with-param name="title" select="$externalized/entry[@key=$toolCategoryKey]"/>
                           <xsl:with-param name="last" select="count(categories/category/pages/page[@selected='true']) = 0"/>
                        </xsl:call-template>
                     </xsl:if>
                  </xsl:if>
                  <xsl:if test="categories/category/pages/page[@selected='true']">
                     <xsl:call-template name="breadcrumb_entry">
                        <xsl:with-param name="node" select="categories/category/pages/page[@selected='true']"/>
                        <xsl:with-param name="title" select="categories/category/pages/page[@selected='true']/title"/>
                        <xsl:with-param name="last" select="'true'"/>
                     </xsl:call-template>
                  </xsl:if>
               </xsl:if>
            </xsl:if>
</div>
   </xsl:template>

   <!--
   =================name breadcrumb_entry==============
   breadcumb processing
   ===============================================
   -->
   <xsl:template name="breadcrumb_entry">
      <xsl:param name="node"/>
      <xsl:param name="title"/>
      <xsl:param name="last"/>
            <a>
               <xsl:if test="$last = 'true'">
                  <xsl:attribute name="class">selectedCrumb</xsl:attribute>
               </xsl:if>
               <xsl:attribute name="href">
                  <xsl:value-of select="$node/url"/>
               </xsl:attribute>
               <xsl:attribute name="title">
                  <xsl:value-of select="$title"/>
               </xsl:attribute>
               <xsl:value-of select="$title"/>
            </a>
            <xsl:if test="$last != 'true'">
               &gt;
            </xsl:if>
   </xsl:template>

   <!--
   ====================================================
   -->
   <xsl:template name="tool_category">
      <xsl:param name="category"/>
      <xsl:variable name="layoutFile" select="$category/layoutFile"/>
      <xsl:variable name="layout" select="document($layoutFile)"/>

      <h1 class="skip">
         <xsl:value-of select="$externalized/entry[@key='sit.contentshead']"/>
      </h1>
      <a id="tocontent" class="skip" name="tocontent"></a>
      <div id="content">
         <div id="col1">
            <div class="portlet">
<div class="portletMainWrap">
<div class="portletBody">
      <xsl:apply-templates select="$layout/*">
         <xsl:with-param name="category" select="$category" />
      </xsl:apply-templates>
</div>
</div>
</div>
      </div>
      </div>
   </xsl:template>

   <xsl:template match="osp:tool">
      <xsl:param name="category" />
      <xsl:variable name="currentToolId" select="@id" />
      <xsl:if test="$category/pages/page[@toolId=$currentToolId]">
         <xsl:apply-templates select="@*|node()" >
            <xsl:with-param name="currentTool" select="$category/pages/page[@toolId=$currentToolId]" />
            <xsl:with-param name="category" select="$category" />
         </xsl:apply-templates>
      </xsl:if>
   </xsl:template>

   <xsl:template match="osp:toolIterator">
      <xsl:param name="category" />
      <xsl:param name="currentTool" />
      <xsl:variable name="currentToolId" select="$currentTool/@toolId"/>
      <xsl:variable name="iteratorNode" select="."/>
      <xsl:for-each select="$category/pages/page[@toolId=$currentToolId]">
         <xsl:apply-templates select="$iteratorNode/*">
            <xsl:with-param name="currentTool" select="." />
            <xsl:with-param name="category" select="$category" />
         </xsl:apply-templates>
      </xsl:for-each>
   </xsl:template>

   <xsl:template match="osp:toolTitle">
      <xsl:param name="category" />
      <xsl:param name="currentTool" />
      <xsl:value-of select="$currentTool/title" />
   </xsl:template>

   <xsl:template match="osp:toolLink">
      <xsl:param name="category" />
      <xsl:param name="currentTool" />
      <a>
         <xsl:if test="$currentTool/@popUp='false'">
            <xsl:attribute name="href">
               <xsl:value-of select="$currentTool/url"/>
            </xsl:attribute>
            <xsl:attribute name="target">_parent</xsl:attribute>
         </xsl:if>
         <xsl:if test="$currentTool/@popUp='true'">
            <xsl:attribute name="href">#</xsl:attribute>
            <xsl:attribute name="onclick">
               window.open('<xsl:value-of select="$currentTool/columns/column/tools/tool/url"/>',
                  '<xsl:value-of select="title"/>',
                  'resizable=yes,toolbar=no,scrollbars=yes, width=800,height=600')
            </xsl:attribute>
         </xsl:if>
         <xsl:apply-templates select="@*|node()" >
            <xsl:with-param name="currentTool" select="$currentTool" />
            <xsl:with-param name="category" select="$category" />
         </xsl:apply-templates>
      </a>
   </xsl:template>

   <xsl:template match="osp:site_role">
      <xsl:param name="category" />
      <xsl:param name="currentTool" />
      <xsl:variable name="roleId" select="@role"/>
      <xsl:comment>
         got a role section:
         <xsl:value-of select="$roleId"/>
      </xsl:comment>
      <xsl:if test="$roles/role[@id=$roleId]">
         <xsl:comment>
            matched a role:
            <xsl:value-of select="$roleId"/>
         </xsl:comment>
         <xsl:apply-templates select="@*|node()" >
            <xsl:with-param name="currentTool" select="$currentTool" />
            <xsl:with-param name="category" select="$category" />
         </xsl:apply-templates>
      </xsl:if>
   </xsl:template>

   <!-- Identity transformation -->
   <xsl:template match="@*|*">
      <xsl:param name="currentTool" />
      <xsl:param name="category" />
      <xsl:if test="count($category) > 0">
         <xsl:copy>
            <xsl:apply-templates select="@*|node()" >
               <xsl:with-param name="currentTool" select="$currentTool" />
               <xsl:with-param name="category" select="$category" />
            </xsl:apply-templates>
         </xsl:copy>
      </xsl:if>
   </xsl:template>

</xsl:stylesheet>
