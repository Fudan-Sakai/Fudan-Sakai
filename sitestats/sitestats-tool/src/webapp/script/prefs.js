/*
 * $URL: https://source.sakaiproject.org/svn/sitestats/tags/sitestats-2.2.2/sitestats-tool/src/webapp/script/prefs.js $
 * $Id: prefs.js 72181 2009-09-28 11:28:47Z nuno@ufp.edu.pt $
 *
 * Copyright (c) 2006-2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
function updateToolSelection(selector) {
	jQuery(selector).each(function(i){
		jQuery(this).children('span').removeClass();
		
		// tool class
		if(jQuery(this).find('ul li :checkbox').length == jQuery(this).find('ul li :checked').length) {
			jQuery(this).children('span').addClass('nodeToolSelected');
			jQuery(this).children(':checkbox').attr('checked','checked');
			  
		}else if(jQuery(this).find('ul li :checked').length === 0) {
			jQuery(this).children('span').addClass('nodeToolUnselected');
			jQuery(this).children(':checkbox').removeAttr('checked');
			
		}else{
			jQuery(this).children('span').addClass('nodeToolPartialSelected');
			jQuery(this).children(':checkbox').attr('checked','checked');
		}
		
		// event class
		jQuery(this).find('ul li').each(function(i){
			jQuery(this).find('span').removeClass();
			if(jQuery(this).find(':checkbox').attr('checked')) {
				jQuery(this).find('span').addClass('nodeEventSelected');
			}else{
				jQuery(this).find('span').addClass('nodeEventUnselected');
			}
		});
	});
}

function toggleCheckboxAll() {
	if(jQuery('#useAllTools').attr('checked')) {
		jQuery('.eventTree').hide();
	}else{
		jQuery('.eventTree').show();
		setMainFrameHeightNoScroll( window.name );
	}
}

function selectUnselectEvents(obj) {
	if(obj.checked) {
		jQuery(obj).parent().find('ul li :checkbox').attr('checked','checked');
	}else{
		jQuery(obj).parent().find('ul li :checkbox').removeAttr('checked');
	}
}

function updateAllToolsSelection() {
	updateToolSelection('.tool');
}