/* Script for pop-up dhtml more tabs implementation 
 * uses jQuery library
 */

// SAK-20576 - the following wae pulled from includeStandardHead.vm so the declaration of 'portal' is available to OSP Portal
// SAK-16484 Allow Javascript to easily get at user details.
// SAK-13987, SAK-16162, SAK-19132 - Portal Logout Timer
var portal = {
    "loggedIn": false,
    "portalPath": "/portal",
    "loggedOutUrl": "/portal/logout",
    "user": {
        "id": "",
        "eid": ""
    },
    "timeoutDialog" : {
        "enabled": true,
        "seconds": 60 * 60
    },
    "toggle" : {
        "allowauto": true,
        "tools": true,
        "sitenav": true
    }
};

/*
   SAK-20576
   This method can be called by any portal implementation to set the login status information for
   the current user
 */
function setLoginStatus (loggedIn, portalPath, loggedOutUrl, userId, userEid)
{
    portal["loggedIn"] = loggedIn;
    portal["portalPath"] = portalPath;
    portal["loggedOutUrl"] = loggedOutUrl;
    portal["user"]["id"] = userId;
    portal["user"]["eid"] = userEid;
}

/*
   SAK-20576
   This method can be called by any portal implementation to set the timeout configuration
 */
function setTimeoutInfo (timeoutDialogEnabled, timeoutDialogWarningSeconds)
{
    portal["timeoutDialog"]["enabled"] = timeoutDialogEnabled;
    portal["timeoutDialog"]["seconds"] = timeoutDialogWarningSeconds;
}

/*
   SAK-20576
   This method can be called by any portal implementation to set various UI state properties
 */
function setUIToggleState (portal_allow_auto_minimize, portal_allow_minimize_tools, portal_allow_minimize_navigation)
{
    portal["toggle"]["allowauto"] = portal_allow_auto_minimize;
    portal["toggle"]["tools"] = portal_allow_minimize_tools;
    portal["toggle"]["sitenav"] = portal_allow_minimize_navigation;
}

/* dhtml_more_tabs
 * displays the More Sites div 
 * note the technique of recasting the function after initalization
 */

var dhtml_more_tabs = function() {
	// first time through set up the DOM
	jQuery('#selectNav').appendTo('#linkNav').addClass('dhtml_more_tabs'); // move the selectNav in the DOM
	jQuery('#selectNav').css('top',jQuery('#linkNav').height() - 3);       // set its top position
	jQuery('#selectNav').width(jQuery('#linkNav').width()*0.75);           // set its width to fix an IE6 bug
	jQuery('#selectNav').css('z-index',9900);                                 // explicitely set the z-index
	jQuery('.more-tab').css('z-index',9800);                                  //  " for the More Tabs div element
	
	// then recast the function to the post initialized state which will run from then on
	dhtml_more_tabs = function() {
		if (jQuery('#selectNav').css('display') == 'none' ) {
			jQuery('div#selectNav').show();
			// highlight the more tab
			jQuery('.more-tab').addClass('more-active');
			// dim the current tab
			jQuery('.selectedTab').addClass('tab-dim');
			// mask the rest of the page
			createDHTMLMask(dhtml_more_tabs) ;
			// bind this function to the More Tabs tab to close More Tabs on click
			jQuery('.selectedTab').bind('click',function(){dhtml_more_tabs();return false;});
		} else {
			// unhighlight the more tab
			jQuery('.more-tab').removeClass('more-active');
			// hide the dropdown
			jQuery('div#selectNav').hide(); // hide the box
			//undim the currently selected tab
			jQuery('.selectedTab').removeClass('tab-dim');
			removeDHTMLMask()
			jQuery('.selectedTab').unbind('click');
		}
	}
	// finally run the inner function, first time through
	dhtml_more_tabs();
}

function createDHTMLMask(callback) {
	jQuery('body').append('<div id="portalMask">&nbsp;</div>');
	jQuery('#portalMask').css('height',browserSafeDocHeight()).css('width','100%').css('z-index',1000).bind("click",function(event){
		callback();
		return false;
	});
	jQuery('#portalMask').bgiframe();
}

function removeDHTMLMask() {
	jQuery('#portalMask').remove();
}

/* Copyright (c) 2006 Brandon Aaron (http://brandonaaron.net)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php) 
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * $LastChangedDate: 2011-06-28 15:16:13 -0400 (Tue, 28 Jun 2011) $
 * $Rev: 94174 $
 *
 * Version 2.1.1
 */
(function($){$.fn.bgIframe=$.fn.bgiframe=function(s){if($.browser.msie&&/6.0/.test(navigator.userAgent)){s=$.extend({top:'auto',left:'auto',width:'auto',height:'auto',opacity:true,src:'javascript:false;'},s||{});var prop=function(n){return n&&n.constructor==Number?n+'px':n;},html='<iframe class="bgiframe"frameborder="0"tabindex="-1"src="'+s.src+'"'+'style="display:block;position:absolute;z-index:-1;'+(s.opacity!==false?'filter:Alpha(Opacity=\'0\');':'')+'top:'+(s.top=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderTopWidth)||0)*-1)+\'px\')':prop(s.top))+';'+'left:'+(s.left=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderLeftWidth)||0)*-1)+\'px\')':prop(s.left))+';'+'width:'+(s.width=='auto'?'expression(this.parentNode.offsetWidth+\'px\')':prop(s.width))+';'+'height:'+(s.height=='auto'?'expression(this.parentNode.offsetHeight+\'px\')':prop(s.height))+';'+'"/>';return this.each(function(){if($('> iframe.bgiframe',this).length==0)this.insertBefore(document.createElement(html),this.firstChild);});}return this;};})(jQuery);

//For SAK-13987
//For SAK-16162
//Just use the EB current.json as the session id rather than trying to do a search/replace
var sessionId = "current";
var sessionTimeOut;
var timeoutDialogEnabled = false;
var timeoutDialogWarningTime;
var timeoutLoggedoutUrl;
var timeoutPortalPath;
jQuery(document).ready(function(){
	// note a session exists whether the user is logged in or no
	if (portal.loggedIn && portal.timeoutDialog ) {
		setTimeout('setup_timeout_config();', 60000);
	}
});

var setup_timeout_config = function() {
	timeoutDialogEnabled = portal.timeoutDialog.enabled;
	timeoutDialogWarningTime = portal.timeoutDialog.seconds;
	timeoutLoggedoutUrl = portal.loggedOutUrl;
	timeoutPortalPath = portal.portalPath;
	if (timeoutDialogEnabled == true) {
		poll_session_data();
		fetch_timeout_dialog();
	}
}

var poll_session_data = function() {
    //Need to append Date.getTime as sakai still uses jquery pre 1.2.1 which doesn't support the cache: false parameter.
	jQuery.ajax({
		url: "/direct/session/" + sessionId + ".json?auto=true&_=" + (new Date()).getTime(),    //auto=true makes it not refresh the session lastaccessedtime
		dataType: "json",
		success: function(data){
		//get the maxInactiveInterval in the same ms
		data.maxInactiveInterval = data.maxInactiveInterval * 1000;
		if(data.active && data.userId != null && data.lastAccessedTime + data.maxInactiveInterval
				> data.currentTime) {
			//User is logged in, so now determine how much time is left
			var remaining = data.lastAccessedTime + data.maxInactiveInterval - data.currentTime;
			//If time remaining is less than timeoutDialogWarningTime minutes, show/update dialog box
			if (remaining < timeoutDialogWarningTime * 1000){
				//we are within 5 min now - show popup
				min = Math.round(remaining / (1000 * 60));
				show_timeout_alert(min);
				clearTimeout(sessionTimeOut);
				sessionTimeOut = setTimeout("poll_session_data()", 1000 * 60);
			} else {
				//more than timeoutDialogWarningTime min away
				clearTimeout(sessionTimeOut);
				sessionTimeOut = setTimeout("poll_session_data()", (remaining - timeoutDialogWarningTime*1000));
			}
		} else if (data.userId == null) {
			// if data.userId is null, the session is done; redirect the user to logoutUrl
			location.href=timeoutLoggedoutUrl;
		
		} else {
			//the timeout length has occurred, but there is a slight delay, do this until there isn't a user.
			sessionTimeOut = setTimeout("poll_session_data()", 1000 * 10);
		}
	},
	error: function(XMLHttpRequest, status, error){
		// We used to to 404 handling here but now we should always get good session data.
	}
	});
}

function keep_session_alive(){
	dismiss_session_alert();
	jQuery.get(timeoutPortalPath);
}

var dismiss_session_alert = function(){
	removeDHTMLMask();
	jQuery("#timeout_alert_body").remove();
}

var timeoutDialogFragment;
function fetch_timeout_dialog() {
	jQuery.ajax({
		url: "/portal/timeout?auto=true",
		cache: true,
		dataType: "text",
		success: function(data) {
		timeoutDialogFragment = data; 
	},
	error: function(XMLHttpRequest, status, error){
		timeoutDialogEnabled = false;
	}
	});
}

function show_timeout_alert(min) {
	if (!timeoutDialogEnabled) {
		return;
	}

	if (!jQuery("#portalMask").get(0)){
		createDHTMLMask(dismiss_session_alert);
		jQuery("#portalMask").css("z-index", 10000);
	}
	if (jQuery("#timeout_alert_body").get(0)) {
		//its there, just update the min
		jQuery("#timeout_alert_body span").html(min);
		jQuery("#timeout_alert_body span").css('top', (f_scrollTop() + 100) + "px");
	} else {
		var dialog = timeoutDialogFragment.replace("{0}", min);
		jQuery("body").append(dialog);
		jQuery('#timeout_alert_body').css('top', (f_scrollTop() + 100) + "px");
	}
}

// The official way for a tool to request minimized navigation
function portalMaximizeTool() {
        if ( ! ( portal.toggle.sitenav || portal.toggle.tools ) ) return;
        if ( ! portal.toggle.allowauto ) return;
        sakaiMinimizeNavigation();
}

function sakaiMinimizeNavigation() {
        if ( ! ( portal.toggle.sitenav || portal.toggle.tools ) ) return;
	if (portal.toggle.sitenav) {
		$('#portalContainer').addClass('sakaiMinimizeSiteNavigation')
	}
	if (portal.toggle.tools){
		$('#container').addClass('sakaiMinimizePageNavigation');	
	}
	$('#toggleToolMax').hide();
	$('#toggleNormal').css({'display':'block'});
}

function sakaiRestoreNavigation() {
        if ( ! ( portal.toggle.sitenav || portal.toggle.tools ) ) return;
	if (portal.toggle.sitenav) {
		$('#portalContainer').removeClass('sakaiMinimizeSiteNavigation')
	}
	if (portal.toggle.tools){
		$('#container').removeClass('sakaiMinimizePageNavigation');	
	}
	$('#toggleToolMax').show();
	$('#toggleNormal').css({'display':'none'});
}

function updatePresence() {
	jQuery.ajax( {
		url: sakaiPresenceFragment,
		cache: false,
		success: function(frag){
			var whereHead = frag.indexOf('<head>');
			if ( whereHead > 0 ) {
			 	location.reload();
			} else {
				$("#presenceIframe").html(frag);
			}
		},
		// If we get an error, wait 60 seconds before retry
		error: function(request, strError){
			sakaiLastPresenceTimeOut = setTimeout('updatePresence()', 60000);
		}
	});
}

function f_scrollTop() {
	return f_filterResults (
		window.pageYOffset ? window.pageYOffset : 0,
		document.documentElement ? document.documentElement.scrollTop : 0,
		document.body ? document.body.scrollTop : 0
	);
}

function f_filterResults(n_win, n_docel, n_body) {
	var n_result = n_win ? n_win : 0;
	if (n_docel && (!n_result || (n_result > n_docel)))
		n_result = n_docel;
	return n_body && (!n_result || (n_result > n_body)) ? n_body : n_result;
}
