/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * Portions of this software are based upon public domain software
 * originally written at the National Center for Supercomputing Applications,
 * University of Illinois, Urbana-Champaign.
 *
 */

 
var checkflag = "false";

function checkAll(field) {
if (field != null) {
  if (field.length >0){
// for more than one checkbox
    if (checkflag == "false") {
       for (i = 0; i < field.length; i++) {
           field[i].checked = true;}
       checkflag = "true";
       return "Uncheck all"; }
    else {
       for (i = 0; i < field.length; i++) {
           field[i].checked = false; }
       checkflag = "false";
       return "Check all"; }
  }
  else {
// for only one checkbox
    if (checkflag == "false") {
  field.checked = true;
  checkflag = "true";
  return "Uncheck all"; }
else {
  field.checked = false; 
  checkflag = "false";
  return "Check all"; }

   }
}
}


function uncheck(field){
      field.checked = false; 
  checkflag = "false";
    return "uncheck"; 
}



// this is for movePool. uncheck all other radio buttons when one is checked, to make it  behave like they are in an array.

function uncheckOthers(field){
 var fieldname = field.getAttribute("name");
 var tables= document.getElementsByTagName("TABLE");

  for (var i = 0; i < tables.length; i++) {
        if ( tables[i].id.indexOf("radiobtn") >=0){
 		var radiobtn = tables[i].getElementsByTagName("INPUT")[0];
// go through the radio buttons, if it's the one clicked on, uncheck all others
	if (fieldname!=radiobtn.getAttribute("name")){
		radiobtn.checked = false;
}
        } 
  }
 
var selectId =  field.getAttribute("value");
var inputhidden = document.getElementById("movePool:selectedRadioBtn");
inputhidden.setAttribute("value", selectId);
 
}



// The following have been modified based on tigris tree javascript 

function flagRows(){
//return;
  var divs = document.getElementsByTagName("A");
  for (var i = 0; i < divs.length; i++) {
     var d = divs[i];
     if (d.className == "treefolder"){
        d.style.backgroundImage = "url(../../images/folder-closed.gif)";
     }
  }
}

function toggleRows(elm) {
 var tables = document.getElementsByTagName("TABLE");
 var t=0;
 for (t= 0; t< tables.length; t++) {
        if ( tables[t].id.indexOf("TreeTable") >=0){
                break;
        }
 }

 var rows = tables[t].getElementsByTagName("TR");

 //var rows = document.getElementsByTagName("TR");
 elm.style.backgroundImage = "url(../../images/folder-closed.gif)";
 var newDisplay = "none";
 var spannode = elm.parentNode;
 var inputnode = spannode.getElementsByTagName("INPUT")[0];
 var thisID = inputnode.value+ "-";
 // Are we expanding or contracting? If the first child is hidden, we expand
  for (var i = 1; i < rows.length; i++) {
   var r = rows[i];

// get row id 
     var cell= r.getElementsByTagName("TD")[0];
     var sf= cell.getElementsByTagName("SPAN")[0];
     var sfs= cell.getElementsByTagName("SPAN");
	if (sfs.length >0) {   // skip the table for remove checkboxes	
     var ht= sf.getElementsByTagName("INPUT")[0];
   var rowid = ht.value;

   if (matchStart(rowid, thisID, true)) {
    if (r.style.display == "none") {
     if (document.all) newDisplay = "block"; //IE4+ specific code
     else newDisplay = "table-row"; //Netscape and Mozilla
     elm.style.backgroundImage = "url(../../images/folder-open.gif)";
    }
    break;
   }
   }

 }

 // When expanding, only expand one level.  Collapse all desendants.
 var matchDirectChildrenOnly = (newDisplay != "none");

 for (var j = 1; j < rows.length; j++) {
   var s = rows[j];
// get row id 
     var cell= s.getElementsByTagName("TD")[0];
     var sf= cell.getElementsByTagName("SPAN")[0];
     var sfs= cell.getElementsByTagName("SPAN");
	if (sfs.length >0) {   // skip the table for remove checkboxes	
     var ht= sf.getElementsByTagName("INPUT")[0];
   var rowid = ht.value;

   if (matchStart(rowid, thisID, matchDirectChildrenOnly)) {
     s.style.display = newDisplay;
     var cell = s.getElementsByTagName("TD")[0];
     var tier = cell.getElementsByTagName("SPAN")[0];
     var folder = tier.getElementsByTagName("A")[0];
     if (folder.getAttribute("onclick") != null) {
      folder.style.backgroundImage = "url(../../images/folder-closed.gif)";
     }
   }
  }

 }
}

function matchStart(target, pattern, matchDirectChildrenOnly) {
 var pos = target.indexOf(pattern);
 if (pos != 0) return false;
 if (!matchDirectChildrenOnly) return true;
 if (target.slice(pos + pattern.length, target.length).indexOf("-") >= 0) return
 false;
 return true;
}

function collapseAllRows() {
 var tables = document.getElementsByTagName("TABLE");
 var t= 0;
 var hastreetable= "false";

 if (!tables.length > 0){

	return; 
 }
 for (t= 0; t< tables.length; t++) {
	if ( tables[t].id.indexOf("TreeTable") >=0){
	hastreetable= "true";
		break;
        }	
 }

if (hastreetable== "false") {
        return;
}

 var rows = tables[t].getElementsByTagName("TR");
 //var rows = document.getElementsByTagName("TR");
 for (var j = 1; j < rows.length; j++) {
   var r = rows[j];
     var cells= r.getElementsByTagName("TD");
     var cell= r.getElementsByTagName("TD")[0];
     var sfs= cell.getElementsByTagName("SPAN");
	if (sfs.length >0) {   // skip the table for remove checkboxes	
     var sf= cell.getElementsByTagName("SPAN")[0];
     var ht= sf.getElementsByTagName("INPUT")[0];
   if (ht.value.indexOf("-") >= 0) {
     r.style.display = "none";
   }
  }

 }
}



/****************************************************************/
// this is for movePool and copyPool, where there are nested tr, td tags 

function toggleRowsForSelectList(elm) {
 var tables = document.getElementsByTagName("TABLE");
 var t=0;
 for (t= 0; t< tables.length; t++) {
        if ( tables[t].id.indexOf("TreeTable") >=0){
                break;
        }
 }

 var rows = tables[t].getElementsByTagName("TR");

 //var rows = document.getElementsByTagName("TR");
 elm.style.backgroundImage = "url(../../images/folder-closed.gif)";
 var newDisplay = "none";
 var spannode = elm.parentNode;
 var inputnode = spannode.getElementsByTagName("INPUT")[0];
 var thisID = inputnode.value+ "-";
 // Are we expanding or contracting? If the first child is hidden, we expand
  for (var i = 1; i < rows.length; i++) {
   var r = rows[i];

// to skip the cell for radio button or checkbox
      var cells= r.getElementsByTagName("TD");
if (cells.length !=7) {
 continue;
}

     var cell= r.getElementsByTagName("TD")[2];
     var sf= cell.getElementsByTagName("SPAN")[0];
     var ht= sf.getElementsByTagName("INPUT")[0];
   var rowid = ht.value;

   if (matchStart(rowid, thisID, true)) {
    if (r.style.display == "none") {
     if (document.all) newDisplay = "block"; //IE4+ specific code
     else newDisplay = "table-row"; //Netscape and Mozilla
     elm.style.backgroundImage = "url(../../images/folder-open.gif)";
    }
    break;
   }
 }

 // When expanding, only expand one level.  Collapse all desendants.
 var matchDirectChildrenOnly = (newDisplay != "none");

 for (var j = 1; j < rows.length; j++) {
   var s = rows[j];
// get row id
// to skip the cell for radio button or checkbox
      var cells= s.getElementsByTagName("TD");
if (cells.length !=7) {
 continue;
}

     var cell= s.getElementsByTagName("TD")[2];

     var sf= cell.getElementsByTagName("SPAN")[0];
     var ht= sf.getElementsByTagName("INPUT")[0];
   var rowid = ht.value;

   if (matchStart(rowid, thisID, matchDirectChildrenOnly)) {
     s.style.display = newDisplay;
     var cell = s.getElementsByTagName("TD")[2];
     var tier = cell.getElementsByTagName("SPAN")[0];
     var folder = tier.getElementsByTagName("A")[0];
     if (folder.getAttribute("onclick") != null) {
      folder.style.backgroundImage = "url(../../images/folder-closed.gif)";
     }
   }
 }
}




function collapseAllRowsForSelectList() {
 var tables = document.getElementsByTagName("TABLE");
 var t=0;
 for (t= 0; t< tables.length; t++) {
        if ( tables[t].id.indexOf("TreeTable") >=0){
                break;
        }
 }
 var rows = tables[t].getElementsByTagName("TR");
 //var rows = document.getElementsByTagName("TR");
 for (var j = 1; j < rows.length; j++) {
   var r = rows[j];
// to skip the cell for radio button or checkbox
      var cells= r.getElementsByTagName("TD");
if (cells.length !=7) {
 continue;

}
      var cell= r.getElementsByTagName("TD")[2];


     var sf= cell.getElementsByTagName("SPAN")[0];
     var ht= sf.getElementsByTagName("INPUT")[0];
   if (ht.value.indexOf("-") >= 0) {
     r.style.display = "none";
   }
 }
}


/****************************************************************/
function collapseRowsByLevel(level) {

 var tables = document.getElementsByTagName("TABLE");
 var t= 0;
 var hastreetable= "false";
 if (!tables.length > 0){
        return;
 }
 for (t= 0; t< tables.length; t++) {
        if ( tables[t].id.indexOf("TreeTable") >=0){
 		hastreetable= "true";
                break;
        }
 }
if (hastreetable== "false") {
	return;
}

 var rows = tables[t].getElementsByTagName("TR");
 //var rows = document.getElementsByTagName("TR");
 for (var j = 1; j < rows.length; j++) {
   var r = rows[j];
     var cells= r.getElementsByTagName("TD");
     var cell= r.getElementsByTagName("TD")[0];
     var sfs= cell.getElementsByTagName("SPAN");
        if (sfs.length >0) {   // skip the table for remove checkboxes
     var sf= cell.getElementsByTagName("SPAN")[0];
     var ht= sf.getElementsByTagName("INPUT")[0];
   var rtokens =ht.value.split("-");
   if (ht.value.indexOf("-") >= 0) {
     if (rtokens.length > level) {
       r.style.display = "none";
     }
   }
  }

 }
}

/****************************************************************/


function collapseRowsByLevel1(i) {
// not used 
 var tables = document.getElementsByTagName("TABLE");
 var t=0;
 for (t= 0; t< tables.length; t++) {
        if ( tables[t].id.indexOf("TreeTable") >=0){
                break;
        }
 }

 var rows = tables[t].getElementsByTagName("TR");

 //var rows = document.getElementsByTagName("TR");
 for (var j = 0; j < rows.length; j++) {
   var r = rows[j];
   var rtokens =r.id.split("-");

   if (r.id.indexOf("-") >= 0) {
     if (rtokens.length > i) {
       r.style.display = "none";
     }
   }

 }
}


// below is for simple tree
function toggleBullet(elm) {
 var newDisplay = "none";
 var e = elm.nextSibling;
 while (e != null) {
  if (e.tagName == "OL" || e.tagName == "ol") {
   if (e.style.display == "none") newDisplay = "block";
   break;
  }
  e = e.nextSibling;
 }
 while (e != null) {
  if (e.tagName == "OL" || e.tagName == "ol") e.style.display = newDisplay;
  e = e.nextSibling;
 }
}

function collapseAll() {
  var lists = document.getElementsByTagName('OL');
  for (var j = 0; j < lists.length; j++)
   lists[j].style.display = "none";
  lists = document.getElementsByTagName('ol');
  for (var j = 0; j < lists.length; j++)
   lists[j].style.display = "none";
  var e = document.getElementById("root");
  e.style.display = "block";
}

function PopupWin(url)
{
   window.open(url,"ha_fullscreen","toolbar=no,location=no,directories=no,status=no,menubar=yes,"+"scrollbars=yes,resizable=yes,width=640,height=480");

}


function checkUpdate(){
 var tables= document.getElementsByTagName("INPUT");
 for (var i = 0; i < tables.length; i++) {
    if (tables[i].name.indexOf("removeCheckbox") >=0){
         if(tables[i].checked){   
            abledButton();
             break;
         }
         else disabledButton();
    }

 }
}

function inIt()
{
  var inputs= document.getElementsByTagName("INPUT");
  for (var i = 0; i < inputs.length; i++) {
    if (inputs[i].name.indexOf("Update") >=0) {
      inputs[i].disabled=false;
    }
  }
}

function disableIt()
{
  var inputs= document.getElementsByTagName("INPUT");
  for (var i = 0; i < inputs.length; i++) {
    if (inputs[i].name.indexOf("Update") >=0) {
      inputs[i].disabled=true;
    }
  }
}

function disabledButton(){
  var inputs= document.getElementsByTagName("INPUT");
  for (var i = 0; i < inputs.length; i++){
    if (inputs[i].name.indexOf("Submit") >=0) {
      inputs[i].disabled=true;
	  inputs[i].className='disabled';
	}
  }
}
function abledButton(){
  var inputs= document.getElementsByTagName("INPUT");
  for (var i = 0; i < inputs.length; i++) {
    if (inputs[i].name.indexOf("Submit") >=0) {
      inputs[i].disabled=false;
	  inputs[i].className='enabled';
	}
  }
}

function toggleRemove(){
  var inputs= document.getElementsByTagName("INPUT");
  var selectitem = null;
  for (var i = 0; i < inputs.length; i++) {
    if (inputs[i].name.indexOf("selectall") >=0) {
       selectitem = inputs[i];
       break;
    }
  }

  var checkit = selectitem.checked;  
  if (checkit)
    selectitem.title=textuncheckall;
  else
    selectitem.title=textcheckall;

  for (var i = 0; i < inputs.length; i++){
    if (inputs[i].name.indexOf("removeCheckbox") >=0)
      inputs[i].checked=checkit;
  }
}

