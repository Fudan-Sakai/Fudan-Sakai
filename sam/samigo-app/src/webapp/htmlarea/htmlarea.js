//
// htmlArea v3.0 - Copyright (c) 2002 interactivetools.com, inc.
// This copyright notice MUST stay intact for use (see license.txt).
//
// A free WYSIWYG editor replacement for <textarea> fields.
// For full source code and docs, visit http://www.interactivetools.com/
//
// Version 3.0 developed by Mihai Bazon for InteractiveTools.
//	     http://students.infoiasi.ro/~mishoo
//
///////////////////////////////////////////////////////////////////////////////
// Sakai Project:
// $Id: htmlarea.js,v 1.1.1.1 2004/07/28 21:32:08 rgollub.stanford.edu Exp $
///////////////////////////////////////////////////////////////////////////////
// Creates a new HTMLArea object.  Tries to replace the textarea with the given
// ID with it.
///////////////////////////////////////////////////////////////////////////////
function HTMLArea(textarea, config) {// Sakai project modification, removed "root"
///////////////////////////////////////////////////////////////////////////////
  if (HTMLArea.checkSupportedBrowser()) {
    if (typeof config == "undefined") {
///////////////////////////////////////////////////////////////////////////////
      this.config = new HTMLArea.Config();// Sakai project modification, removed "root"
///////////////////////////////////////////////////////////////////////////////
    } else {
      this.config = config;
    }
    this._htmlArea = null;
    this._textArea = textarea;
    this._editMode = "wysiwyg";
    this.plugins = {};
    this._timerToolbar = null;
    this._mdoc = document; // cache the document, we need it in plugins
  }
};
///////////////////////////////////////////////////////////////////////////////
HTMLArea.Config = function () { // Sakai project modification, removed "root"
///////////////////////////////////////////////////////////////////////////////
  this.version = "3.0";

  this.width = "auto";
  this.height = "auto";

  // enable creation of a status bar?
  this.statusBar = true;

  // the next parameter specifies whether the toolbar should be included
  // in the size or not.
  this.sizeIncludesToolbar = true;

  // style included in the iframe document
  this.pageStyle = "body { background-color: #fff; font-family: verdana,sans-serif; }";
  if (typeof _editor_url != "undefined") {
    this.editorURL = _editor_url;
  } else {
    this.editorURL = "";
  }
///////////////////////////////////////////////////////////////////////////////
  // URL-s
  this.imgURL = "images/";// Sakai project modification, removed "root"
  this.popupURL = "popups/";// Sakai project modification, removed "root"
///////////////////////////////////////////////////////////////////////////////

  // configuration for plugins
  this.plugins = {};

  /** CUSTOMIZING THE TOOLBAR
   * -------------------------
   *
   * It is recommended that you customize the toolbar contents in an
   * external file (i.e. the one calling HTMLArea) and leave this one
   * unchanged.  That's because when we (InteractiveTools.com) release a
   * new official version, it's less likely that you will have problems
   * upgrading HTMLArea.
   */
  this.toolbar = [
    [ "fontname", "space",
      "fontsize", "space",
      "formatblock", "space",
      "bold", "italic", "underline", "separator",
      "strikethrough", "subscript", "superscript", "separator",
      "copy", "cut", "paste", "space", "undo", "redo" ],

    [ "justifyleft", "justifycenter", "justifyright", "justifyfull", "separator",
      "insertorderedlist", "insertunorderedlist", "outdent", "indent", "separator",
      "forecolor", "hilitecolor", "textindicator", "separator",
      "inserthorizontalrule", "createlink", "insertimage", "inserttable", "htmlmode", "separator",
      "popupeditor", "separator", "showhelp", "about" ]
    ];

  this.fontname = {
    "Arial":	   'arial,helvetica,sans-serif',
    "Courier New":	   'courier new,courier,monospace',
    "Georgia":	   'georgia,times new roman,times,serif',
    "Tahoma":	   'tahoma,arial,helvetica,sans-serif',
    "Times New Roman": 'times new roman,times,serif',
    "Verdana":	   'verdana,arial,helvetica,sans-serif',
    "impact":	   'impact',
    "WingDings":	   'wingdings'
  };

  this.fontsize = {
    "1 (8 pt)":  "1",
    "2 (10 pt)": "2",
    "3 (12 pt)": "3",
    "4 (14 pt)": "4",
    "5 (18 pt)": "5",
    "6 (24 pt)": "6",
    "7 (36 pt)": "7"
  };

  this.formatblock = {
    "Heading 1": "h1",
    "Heading 2": "h2",
    "Heading 3": "h3",
    "Heading 4": "h4",
    "Heading 5": "h5",
    "Heading 6": "h6",
    "Normal": "p",
    "Address": "address",
    "Formatted": "pre"
  };

  this.customSelects = {};

  function cut_copy_paste(e, cmd, obj) {
    try {
      e.execCommand(cmd);
    } catch (e) {
      if (HTMLArea.is_gecko) {
        alert("Some revisions of Mozilla/Gecko do not support programatic " +
              "access to cut/copy/paste functions, for security reasons.  " +
              "Your browser is one of them.  Please use the standard key combinations:\n" +
              "CTRL-X for cut, CTRL-C for copy, CTRL-V for paste.");
        obj.element.style.display = "none";
      }
    }
  };

  // ADDING CUSTOM BUTTONS: please read below!
  // format of the btnList elements is "ID: [ ToolTip, Icon, Enabled in text mode?, ACTION ]"
  //    - ID: unique ID for the button.  If the button calls document.execCommand
  //	    it's wise to give it the same name as the called command.
  //    - ACTION: function that gets called when the button is clicked.
  //              it has the following prototype:
  //                 function(editor, buttonName)
  //              - editor is the HTMLArea object that triggered the call
  //              - buttonName is the ID of the clicked button
  //              These 2 parameters makes it possible for you to use the same
  //              handler for more HTMLArea objects or for more different buttons.
  //    - ToolTip: default tooltip, for cases when it is not defined in the -lang- file (HTMLArea.I18N)
  //    - Icon: path to an icon image file for the button (TODO: use one image for all buttons!)
  //    - Enabled in text mode: if false the button gets disabled for text-only mode; otherwise enabled all the time.
  this.btnList = {
    bold: [ "Bold", "images/ed_format_bold.gif", false, function(e) {e.execCommand("bold");} ],
    italic: [ "Italic", "images/ed_format_italic.gif", false, function(e) {e.execCommand("italic");} ],
    underline: [ "Underline", "images/ed_format_underline.gif", false, function(e) {e.execCommand("underline");} ],
    strikethrough: [ "Strikethrough", "images/ed_format_strike.gif", false, function(e) {e.execCommand("strikethrough");} ],
    subscript: [ "Subscript", "images/ed_format_sub.gif", false, function(e) {e.execCommand("subscript");} ],
    superscript: [ "Superscript", "images/ed_format_sup.gif", false, function(e) {e.execCommand("superscript");} ],
    justifyleft: [ "Justify Left", "images/ed_align_left.gif", false, function(e) {e.execCommand("justifyleft");} ],
    justifycenter: [ "Justify Center", "images/ed_align_center.gif", false, function(e) {e.execCommand("justifycenter");} ],
    justifyright: [ "Justify Right", "images/ed_align_right.gif", false, function(e) {e.execCommand("justifyright");} ],
    justifyfull: [ "Justify Full", "images/ed_align_justify.gif", false, function(e) {e.execCommand("justifyfull");} ],
    insertorderedlist: [ "Ordered List", "images/ed_list_num.gif", false, function(e) {e.execCommand("insertorderedlist");} ],
    insertunorderedlist: [ "Bulleted List", "images/ed_list_bullet.gif", false, function(e) {e.execCommand("insertunorderedlist");} ],
    outdent: [ "Decrease Indent", "images/ed_indent_less.gif", false, function(e) {e.execCommand("outdent");} ],
    indent: [ "Increase Indent", "images/ed_indent_more.gif", false, function(e) {e.execCommand("indent");} ],
    forecolor: [ "Font Color", "images/ed_color_fg.gif", false, function(e) {e.execCommand("forecolor");} ],
    hilitecolor: [ "Background Color", "images/ed_color_bg.gif", false, function(e) {e.execCommand("hilitecolor");} ],
    inserthorizontalrule: [ "Horizontal Rule", "images/ed_hr.gif", false, function(e) {e.execCommand("inserthorizontalrule");} ],
    createlink: [ "Insert Web Link", "images/ed_link.gif", false, function(e) {e.execCommand("createlink", true);} ],
    insertimage: [ "Insert Image", "images/ed_image.gif", false, function(e) {e.execCommand("insertimage");} ],
    inserttable: [ "Insert Table", "images/insert_table.gif", false, function(e) {e.execCommand("inserttable");} ],
    htmlmode: [ "Toggle HTML Source", "images/ed_html.gif", true, function(e) {e.execCommand("htmlmode");} ],
    popupeditor: [ "Enlarge Editor", "images/fullscreen_maximize.gif", true, function(e) {e.execCommand("popupeditor");} ],
    about: [ "About this editor", "images/ed_about.gif", true, function(e) {e.execCommand("about");} ],
    showhelp: [ "Help using editor", "images/ed_help.gif", true, function(e) {e.execCommand("showhelp");} ],
    undo: [ "Undoes your last action", "images/ed_undo.gif", false, function(e) {e.execCommand("undo");} ],
    redo: [ "Redoes your last action", "images/ed_redo.gif", false, function(e) {e.execCommand("redo");} ],
    cut: [ "Cut selection", "images/ed_cut.gif", false, cut_copy_paste ],
    copy: [ "Copy selection", "images/ed_copy.gif", false, cut_copy_paste ],
    paste: [ "Paste from clipboard", "images/ed_paste.gif", false, cut_copy_paste ]
  };
  /* ADDING CUSTOM BUTTONS
   * ---------------------
   *
   * It is recommended that you add the custom buttons in an external
   * file and leave this one unchanged.  That's because when we
   * (InteractiveTools.com) release a new official version, it's less
   * likely that you will have problems upgrading HTMLArea.
   *
   * Example on how to add a custom button when you construct the HTMLArea:
   *
   *   var editor = new HTMLArea("your_text_area_id");
   *   var cfg = editor.config; // this is the default configuration
   *   cfg.btnList["my-hilite"] =
   *   [ function(editor) { editor.surroundHTML('<span style="background:yellow">', '</span>'); }, // action
   *    "Highlight selection", // tooltip
   *    "my_hilite.gif", // image
   *    false // disabled in text mode
	 *                             	];
   *   cfg.toolbar.push(["linebreak", "my-hilite"]); // add the new button to the toolbar
   *
   * An alternate (also more convenient and recommended) way to
   * accomplish this is to use the registerButton function below.
   */
  // initialize tooltips from the I18N module
  for (var i in this.btnList) {
    var btn = this.btnList[i];
    if (typeof HTMLArea.I18N.tooltips[i] != "undefined") {
      btn[0] = HTMLArea.I18N.tooltips[i];
    }
  }
};

/** Helper function: register a new button with the configuration.  It can be
 * called with all 5 arguments, or with only one (first one).  When called with
 * only one argument it must be an object with the following properties: id,
 * tooltip, image, textMode, action.  Examples:
 *
 * 1. config.registerButton("my-hilite", "Hilite text", "my-hilite.gif", false, function(editor) {...});
 * 2. config.registerButton({
 *      id       : "my-hilite",      // the ID of your button
 *      tooltip  : "Hilite text",    // the tooltip
 *      image    : "my-hilite.gif",  // image to be displayed in the toolbar
 *      textMode : false,            // disabled in text mode
 *      action   : function(editor) { // called when the button is clicked
 *                   editor.surroundHTML('<span class="hilite">', '</span>');
 *                 },
 *      context  : "p"               // will be disabled if outside a <p> element
 *    });
 */
HTMLArea.Config.prototype.registerButton = function(id, tooltip, image, textMode, action, context) {
  var the_id;
  if (typeof id == "string") {
    the_id = id;
  } else if (typeof id == "object") {
    the_id = id.id;
  } else {
    alert("ERROR [HTMLArea.Config::registerButton]:\ninvalid arguments");
    return false;
  }
  // check for existing id
  if (typeof this.customSelects[the_id] != "undefined") {
    alert("WARNING [HTMLArea.Config::registerDropdown]:\nA dropdown with the same ID already exists.");
  }
  if (typeof this.btnList[the_id] != "undefined") {
    alert("WARNING [HTMLArea.Config::registerDropdown]:\nA button with the same ID already exists.");
  }
  switch (typeof id) {
      case "string": this.btnList[id] = [ tooltip, image, textMode, action, context ]; break;
      case "object": this.btnList[id.id] = [ id.tooltip, id.image, id.textMode, id.action, id.context ]; break;
  }
};

/** The following helper function registers a dropdown box with the editor
 * configuration.  You still have to add it to the toolbar, same as with the
 * buttons.  Call it like this:
 *
 * FIXME: add example
 */
HTMLArea.Config.prototype.registerDropdown = function(object) {
  // check for existing id
  if (typeof this.customSelects[object.id] != "undefined") {
    alert("WARNING [HTMLArea.Config::registerDropdown]:\nA dropdown with the same ID already exists.");
  }
  if (typeof this.btnList[object.id] != "undefined") {
    alert("WARNING [HTMLArea.Config::registerDropdown]:\nA button with the same ID already exists.");
  }
  this.customSelects[object.id] = object;
};

/** Helper function: replace all TEXTAREA-s in the document with HTMLArea-s. */
HTMLArea.replaceAll = function(config) {
  var tas = document.getElementsByTagName("textarea");
  for (var i = tas.length; i > 0; (new HTMLArea(tas[--i], config)).generate());
};

/** Helper function: replaces the TEXTAREA with the given ID with HTMLArea. */
HTMLArea.replace = function(id, config) {
  var ta = document.getElementById(id);
  return ta ? (new HTMLArea(ta, config)).generate() : null;
};

// Creates the toolbar and appends it to the _htmlarea
HTMLArea.prototype._createToolbar = function () {
  var editor = this;	// to access this in nested functions

  var toolbar = document.createElement("div");
  this._toolbar = toolbar;
  toolbar.className = "toolbar";
  toolbar.unselectable = "1";
  var tb_row = null;
  var tb_objects = new Object();
  this._toolbarObjects = tb_objects;

  // creates a new line in the toolbar
  function newLine() {
    var table = document.createElement("table");
    table.border = "0px";
    table.cellSpacing = "0px";
    table.cellPadding = "0px";
    toolbar.appendChild(table);
    // TBODY is required for IE, otherwise you don't see anything
    // in the TABLE.
    var tb_body = document.createElement("tbody");
    table.appendChild(tb_body);
    tb_row = document.createElement("tr");
    tb_body.appendChild(tb_row);
  }; // END of function: newLine
  // init first line
  newLine();

  // updates the state of a toolbar element.  This function is member of
  // a toolbar element object (unnamed objects created by createButton or
  // createSelect functions below).
  function setButtonStatus(id, newval) {
    var oldval = this[id];
    var el = this.element;
    if (oldval != newval) {
      switch (id) {
          case "enabled":
        if (newval) {
          HTMLArea._removeClass(el, "buttonDisabled");
          el.disabled = false;
        } else {
          HTMLArea._addClass(el, "buttonDisabled");
          el.disabled = true;
        }
        break;
          case "active":
        if (newval) {
          HTMLArea._addClass(el, "buttonPressed");
        } else {
          HTMLArea._removeClass(el, "buttonPressed");
        }
        break;
      }
      this[id] = newval;
    }
  }; // END of function: setButtonStatus

  // this function will handle creation of combo boxes.  Receives as
  // parameter the name of a button as defined in the toolBar config.
  // This function is called from createButton, above, if the given "txt"
  // doesn't match a button.
  function createSelect(txt) {
    var options = null;
    var el = null;
    var cmd = null;
    var customSelects = editor.config.customSelects;
    var context = null;
    switch (txt) {
        case "fontsize":
        case "fontname":
        case "formatblock":
      // the following line retrieves the correct
      // configuration option because the variable name
      // inside the Config object is named the same as the
      // button/select in the toolbar.  For instance, if txt
      // == "formatblock" we retrieve config.formatblock (or
      // a different way to write it in JS is
      // config["formatblock"].
      options = editor.config[txt];
      cmd = txt;
      break;
        default:
      // try to fetch it from the list of registered selects
      cmd = txt;
      var dropdown = customSelects[cmd];
      if (typeof dropdown != "undefined") {
        options = dropdown.options;
        context = dropdown.context;
      } else {
        alert("ERROR [createSelect]:\nCan't find the requested dropdown definition");
      }
      break;
    }
    if (options) {
      el = document.createElement("select");
      var obj = {
        name	: txt, // field name
        element : el,	// the UI element (SELECT)
        enabled : true, // is it enabled?
        text	: false, // enabled in text mode?
        cmd	: cmd, // command ID
        state	: setButtonStatus, // for changing state
        context : context
      };
      tb_objects[txt] = obj;
      for (var i in options) {
        var op = document.createElement("option");
        op.appendChild(document.createTextNode(i));
        op.value = options[i];
        el.appendChild(op);
      }
      HTMLArea._addEvent(el, "change", function () {
        editor._comboSelected(el, txt);
      });
    }
    return el;
  }; // END of function: createSelect

  // appends a new button to toolbar
  function createButton(txt) {
    // the element that will be created
    var el = null;
    var btn = null;
    switch (txt) {
        case "separator":
      el = document.createElement("div");
      el.className = "separator";
      break;
        case "space":
      el = document.createElement("div");
      el.className = "space";
      break;
        case "linebreak":
      newLine();
      return false;
        case "textindicator":
      el = document.createElement("div");
      el.appendChild(document.createTextNode("A"));
      el.className = "indicator";
      el.title = HTMLArea.I18N.tooltips.textindicator;
      var obj = {
        name	: txt, // the button name (i.e. 'bold')
        element : el, // the UI element (DIV)
        enabled : true, // is it enabled?
        active	: false, // is it pressed?
        text	: false, // enabled in text mode?
        cmd	: "textindicator", // the command ID
        state	: setButtonStatus // for changing state
      };
      tb_objects[txt] = obj;
      break;
        default:
      btn = editor.config.btnList[txt];
    }
    if (!el && btn) {
      el = document.createElement("div");
      el.title = btn[0];
      el.className = "button";
      // let's just pretend we have a button object, and
      // assign all the needed information to it.
      var obj = {
        name	: txt, // the button name (i.e. 'bold')
        element : el, // the UI element (DIV)
        enabled : true, // is it enabled?
        active	: false, // is it pressed?
        text	: btn[2], // enabled in text mode?
        cmd	: btn[3], // the command ID
        state	: setButtonStatus, // for changing state
        context : btn[4] || null // enabled in a certain context?
      };
      tb_objects[txt] = obj;
      // handlers to emulate nice flat toolbar buttons
      HTMLArea._addEvent(el, "mouseover", function () {
        if (obj.enabled) {
          HTMLArea._addClass(el, "buttonHover");
        }
      });
      HTMLArea._addEvent(el, "mouseout", function () {
        if (obj.enabled) with (HTMLArea) {
          _removeClass(el, "buttonHover");
          _removeClass(el, "buttonActive");
          (obj.active) && _addClass(el, "buttonPressed");
        }
      });
      HTMLArea._addEvent(el, "mousedown", function (ev) {
        if (obj.enabled) with (HTMLArea) {
          _addClass(el, "buttonActive");
          _removeClass(el, "buttonPressed");
          _stopEvent(is_ie ? window.event : ev);
        }
      });
      // when clicked, do the following:
      HTMLArea._addEvent(el, "click", function (ev) {
        if (obj.enabled) with (HTMLArea) {
          _removeClass(el, "buttonActive");
          _removeClass(el, "buttonHover");
          obj.cmd(editor, obj.name, obj);
          _stopEvent(is_ie ? window.event : ev);
        }
      });
      var img = document.createElement("img");
      img.src = editor.imgURL(btn[1]);
      img.style.width = "18px";
      img.style.height = "18px";
      el.appendChild(img);
    } else if (!el) {
      el = createSelect(txt);
    }
    if (el) {
      var tb_cell = document.createElement("td");
      tb_row.appendChild(tb_cell);
      tb_cell.appendChild(el);
    } else {
      alert("FIXME: Unknown toolbar item: " + txt);
    }
    return el;
  };

  var first = true;
  for (var i in this.config.toolbar) {
    if (!first) {
      createButton("linebreak");
    } else {
      first = false;
    }
    var group = this.config.toolbar[i];
    for (var j in group) {
      var code = group[j];
      if (/^([IT])\[(.*?)\]/.test(code)) {
        // special case, create text label
        var l7ed = RegExp.$1 == "I"; // localized?
        var label = RegExp.$2;
        if (l7ed) {
          label = HTMLArea.I18N.custom[label];
        }
        var tb_cell = document.createElement("td");
        tb_row.appendChild(tb_cell);
        tb_cell.className = "label";
        tb_cell.innerHTML = label;
      } else {
        createButton(code);
      }
    }
  }

  this._htmlArea.appendChild(toolbar);
};

HTMLArea.prototype._createStatusBar = function() {
  var div = document.createElement("div");
  div.className = "statusBar";
  this._htmlArea.appendChild(div);
  this._statusBar = div;
  div.appendChild(document.createTextNode(HTMLArea.I18N.msg["Path"] + ": "));
  // creates a holder for the path view
  div = document.createElement("span");
  div.className = "statusBarTree";
  this._statusBarTree = div;
  this._statusBar.appendChild(div);
  if (!this.config.statusBar) {
    // disable it...
    div.style.display = "none";
  }
};

// Creates the HTMLArea object and replaces the textarea with it.
HTMLArea.prototype.generate = function () {
  var editor = this;	// we'll need "this" in some nested functions
  // get the textarea
  var textarea = this._textArea;
  if (typeof textarea == "string") {
    // it's not element but ID
    this._textArea = textarea = document.getElementById(textarea);
  }
  this._ta_size = {
    w: textarea.offsetWidth,
    h: textarea.offsetHeight
  };
  textarea.style.display = "none";

  // create the editor framework
  var htmlarea = document.createElement("div");
  htmlarea.className = "htmlarea";
  this._htmlArea = htmlarea;

  // insert the editor before the textarea.
  textarea.parentNode.insertBefore(htmlarea, textarea);

  if (textarea.form) {
    // we have a form, on submit get the HTMLArea content and
    // update original textarea.
    textarea.form.onsubmit = function() {
      editor._textArea.value = editor.getHTML();
    };
  }

  // add a handler for the "back/forward" case -- on body.unload we save
  // the HTML content into the original textarea.
  window.onunload = function() {
    editor._textArea.value = editor.getHTML();
  };

  // creates & appends the toolbar
  this._createToolbar();

  // create the IFRAME
  var iframe = document.createElement("iframe");
  ////////////////////////////////////////////////////
  // Sakai project modification, changed for SSL issue
  iframe.src = this.popupURL("blank.html");
  ////////////////////////////////////////////////////

  htmlarea.appendChild(iframe);

  this._iframe = iframe;

  // creates & appends the status bar, if the case
  this._createStatusBar();

  // remove the default border as it keeps us from computing correctly
  // the sizes.  (somebody tell me why doesn't this work in IE)

  if (!HTMLArea.is_ie) {
    iframe.style.borderWidth = "1px";
  // iframe.frameBorder = "1";
  // iframe.marginHeight = "0";
  // iframe.marginWidth = "0";
  }

  // size the IFRAME according to user's prefs or initial textarea
  var height = (this.config.height == "auto" ? (this._ta_size.h + "px") : this.config.height);
  height = parseInt(height);
  var width = (this.config.width == "auto" ? (this._ta_size.w + "px") : this.config.width);
  width = parseInt(width);

  if (!HTMLArea.is_ie) {
    height -= 2;
    width -= 2;
  }

  iframe.style.width = width + "px";
  if (this.config.sizeIncludesToolbar) {
    // substract toolbar height
    height -= this._toolbar.offsetHeight;
    height -= this._statusBar.offsetHeight;
  }
  if (height < 0) {
    height = 0;
  }
  iframe.style.height = height + "px";

  // the editor including the toolbar now have the same size as the
  // original textarea.. which means that we need to reduce that a bit.
  textarea.style.width = iframe.style.width;
  textarea.style.height = iframe.style.height;

  // IMPORTANT: we have to allow Mozilla a short time to recognize the
  // new frame.  Otherwise we get a stupid exception.
  function initIframe() {
    var doc = editor._iframe.contentWindow.document;
    if (!doc) {
      // Try again..
      // FIXME: don't know what else to do here.  Normally
      // we'll never reach this point.
      if (HTMLArea.is_gecko) {
        setTimeout(initIframe, 10);
        return false;
      } else {
        alert("ERROR: IFRAME can't be initialized.");
      }
    }
    if (HTMLArea.is_gecko) {
      // enable editable mode for Mozilla
      doc.designMode = "on";
    }
    editor._doc = doc;
    doc.open();
    var html = "<html>\n";
    html += "<head>\n";
    html += "<style>" + editor.config.pageStyle + "</style>\n";
    html += "</head>\n";
    html += "<body>\n";
    html += editor._textArea.value;
    html += "</body>\n";
    html += "</html>";
    doc.write(html);
    doc.close();

    if (HTMLArea.is_ie) {
      // enable editable mode for IE.	 For some reason this
      // doesn't work if done in the same place as for Gecko
      // (above).
      doc.body.contentEditable = true;
    }

    editor.focusEditor();
    // intercept some events; for updating the toolbar & keyboard handlers
    HTMLArea._addEvents
      (doc, ["keydown", "keypress", "mousedown", "mouseup", "drag"],
       function (event) {
         return editor._editorEvent(HTMLArea.is_ie ? editor._iframe.contentWindow.event : event);
       });
    editor.updateToolbar();
  };
  setTimeout(initIframe, HTMLArea.is_gecko ? 10 : 0);
};

// Switches editor mode; parameter can be "textmode" or "wysiwyg".  If no
// parameter was passed this function toggles between modes.
HTMLArea.prototype.setMode = function(mode) {
  if (typeof mode == "undefined") {
    mode = ((this._editMode == "textmode") ? "wysiwyg" : "textmode");
  }
  switch (mode) {
      case "textmode":
    this._textArea.value = this.getHTML();
    this._iframe.style.display = "none";
    this._textArea.style.display = "block";
    if (this.config.statusBar) {
      this._statusBar.innerHTML = HTMLArea.I18N.msg["TEXT_MODE"];
    }
    break;
      case "wysiwyg":
    if (HTMLArea.is_gecko) {
      // disable design mode before changing innerHTML
      this._doc.designMode = "off";
    }
    this._doc.body.innerHTML = this.getHTML();
    this._iframe.style.display = "block";
    this._textArea.style.display = "none";
    if (HTMLArea.is_gecko) {
      // we need to refresh that info for Moz-1.3a
      this._doc.designMode = "on";
    }
    if (this.config.statusBar) {
      this._statusBar.innerHTML = '';
      this._statusBar.appendChild(document.createTextNode(HTMLArea.I18N.msg["Path"] + ": "));
      this._statusBar.appendChild(this._statusBarTree);
    }
    break;
      default:
    alert("Mode <" + mode + "> not defined!");
    return false;
  }
  this._editMode = mode;
  this.focusEditor();
};

/***************************************************
 *  Category: PLUGINS
 ***************************************************/

// Create the specified plugin and register it with this HTMLArea
///////////////////////////////////////////////////////////////////////////////
// Sakai project modification, removed "root"
HTMLArea.prototype.registerPlugin = function(pluginName) {
///////////////////////////////////////////////////////////////////////////////
  this.plugins[pluginName] = eval("new " + pluginName + "(this);");
};

// static function that loads the required plugin and lang file, based on the
// language loaded already for HTMLArea.  You better make sure that the plugin
// _has_ that language, otherwise shit might happen ;-)
///////////////////////////////////////////////////////////////////////////////
// Sakai project modification, removed "root"
HTMLArea.loadPlugin = function(pluginName) {
///////////////////////////////////////////////////////////////////////////////
  var editorurl = '';
  if (typeof _editor_url != "undefined") {
    editorurl = _editor_url + "/";
  }
  var dir = editorurl + "plugins/" + pluginName;
  var plugin = pluginName.replace(/([a-z])([A-Z])([a-z])/g,
          function (str, l1, l2, l3) {
            return l1 + "-" + l2.toLowerCase() + l3;
          }).toLowerCase() + ".js";
  document.write("<script type='text/javascript' src='" + dir + "/" + plugin + "'></script>");
  document.write("<script type='text/javascript' src='" + dir + "/lang/" + HTMLArea.I18N.lang + ".js'></script>");
};

/***************************************************
 *  Category: EDITOR UTILITIES
 ***************************************************/

HTMLArea.prototype.forceRedraw = function() {
  this._doc.body.style.visibility = "hidden";
  this._doc.body.style.visibility = "visible";
  // this._doc.body.innerHTML = this.getInnerHTML();
};

// focuses the iframe window.  returns a reference to the editor document.
HTMLArea.prototype.focusEditor = function() {
  switch (this._editMode) {
      case "wysiwyg" : this._iframe.contentWindow.focus(); break;
      case "textmode": this._textArea.focus(); break;
      default	   : alert("ERROR: mode " + this._editMode + " is not defined");
  }
  return this._doc;
};

// updates enabled/disable/active state of the toolbar elements
HTMLArea.prototype.updateToolbar = function(noStatus) {
  var doc = this._doc;
  var text = (this._editMode == "textmode");
  var ancestors = null;
  if (!text) {
    ancestors = this.getAllAncestors();
    if (this.config.statusBar && !noStatus) {
      this._statusBarTree.innerHTML = ''; // clear
      for (var i = ancestors.length; --i >= 0;) {
        var el = ancestors[i];
        if (!el) {
          // hell knows why we get here; this
          // could be a classic example of why
          // it's good to check for conditions
          // that are impossible to happen ;-)
          continue;
        }
        var a = document.createElement("a");
        a.href = "#";
        a.el = el;
        a.editor = this;
        a.onclick = function() {
          this.blur();
          this.editor.selectNodeContents(this.el);
          this.editor.updateToolbar(true);
          return false;
        };
        a.oncontextmenu = function() {
          // TODO: add context menu here
          this.blur();
          var info = "Inline style:\n\n";
          info += this.el.style.cssText.split(/;\s*/).join(";\n");
          alert(info);
          return false;
        };
        var txt = el.tagName.toLowerCase();
        a.title = el.style.cssText;
        if (el.id) {
          txt += "#" + el.id;
        }
        if (el.className) {
          txt += "." + el.className;
        }
        a.appendChild(document.createTextNode(txt));
        this._statusBarTree.appendChild(a);
        if (i != 0) {
          this._statusBarTree.appendChild(document.createTextNode(String.fromCharCode(0xbb)));
        }
      }
    }
  }
  for (var i in this._toolbarObjects) {
    var btn = this._toolbarObjects[i];
    var cmd = i;
    var inContext = true;
    if (btn.context && !text) {
      inContext = false;
      var context = btn.context;
      var attrs = [];
      if (/(.*)\[(.*?)\]/.test(context)) {
        context = RegExp.$1;
        attrs = RegExp.$2.split(",");
      }
      context = context.toLowerCase();
      var match = (context == "*");
      for (var k in ancestors) {
        if (!ancestors[k]) {
          // the impossible really happens.
          continue;
        }
        if (match || (ancestors[k].tagName.toLowerCase() == context)) {
          inContext = true;
          for (var ka in attrs) {
            if (!eval("ancestors[k]." + attrs[ka])) {
              inContext = false;
              break;
            }
          }
          if (inContext) {
            break;
          }
        }
      }
    }
    btn.state("enabled", (!text || btn.text) && inContext);
    if (typeof cmd == "function") {
      continue;
    }
    // look-it-up in the custom dropdown boxes
    var dropdown = this.config.customSelects[cmd];
    if ((!text || btn.text) && (typeof dropdown != "undefined")) {
      dropdown.refresh(this);
      continue;
    }
    switch (cmd) {
        case "fontname":
        case "fontsize":
        case "formatblock":
      if (!text) {
        var value = ("" + doc.queryCommandValue(cmd)).toLowerCase();
        if (!value) {
          // FIXME: what do we do here?
          break;
        }
        // HACK -- retrieve the config option for this
        // combo box.  We rely on the fact that the
        // variable in config has the same name as
        // button name in the toolbar.
        var options = this.config[cmd];
        var k = 0;
        // btn.element.selectedIndex = 0;
        for (var j in options) {
          // FIXME: the following line is scary.
          if ((j.toLowerCase() == value) ||
              (options[j].substr(0, value.length).toLowerCase() == value)) {
            btn.element.selectedIndex = k;
            break;
          }
          ++k;
        }
      }
      break;
        case "textindicator":
      if (!text) {
        try {with (btn.element.style) {
          backgroundColor = HTMLArea._makeColor(
            doc.queryCommandValue(HTMLArea.is_ie ? "backcolor" : "hilitecolor"));
          if (/transparent/i.test(backgroundColor)) {
            // Mozilla
            backgroundColor = HTMLArea._makeColor(doc.queryCommandValue("backcolor"));
          }
          color = HTMLArea._makeColor(doc.queryCommandValue("forecolor"));
          fontFamily = doc.queryCommandValue("fontname");
          fontWeight = doc.queryCommandState("bold") ? "bold" : "normal";
          fontStyle = doc.queryCommandState("italic") ? "italic" : "normal";
        }} catch (e) {
          // alert(e + "\n\n" + cmd);
        }
      }
      break;
        case "htmlmode": btn.state("active", text); break;
        default:
      try {
        btn.state("active", (!text && doc.queryCommandState(cmd)));
      } catch (e) {}
    }
  }
};

/** Returns a node after which we can insert other nodes, in the current
 * selection.  The selection is removed.  It splits a text node, if needed.
 */
HTMLArea.prototype.insertNodeAtSelection = function(toBeInserted) {
  if (!HTMLArea.is_ie) {
    var sel = this._getSelection();
    var range = this._createRange(sel);
    // remove the current selection
    sel.removeAllRanges();
    range.deleteContents();
    var node = range.startContainer;
    var pos = range.startOffset;
    switch (node.nodeType) {
        case 3: // Node.TEXT_NODE
      // we have to split it at the caret position.
      if (toBeInserted.nodeType == 3) {
        // do optimized insertion
        node.insertData(pos, toBeInserted.data);
        range = this._createRange();
        range.setEnd(node, pos + toBeInserted.length);
        range.setStart(node, pos + toBeInserted.length);
        sel.addRange(range);
      } else {
        node = node.splitText(pos);
        var selnode = toBeInserted;
        if (toBeInserted.nodeType == 11 /* Node.DOCUMENT_FRAGMENT_NODE */) {
          selnode = selnode.firstChild;
        }
        node.parentNode.insertBefore(toBeInserted, node);
        this.selectNodeContents(selnode);
        this.updateToolbar();
      }
      break;
        case 1: // Node.ELEMENT_NODE
      var selnode = toBeInserted;
      if (toBeInserted.nodeType == 11 /* Node.DOCUMENT_FRAGMENT_NODE */) {
        selnode = selnode.firstChild;
      }
      node.insertBefore(toBeInserted, node.childNodes[pos]);
      this.selectNodeContents(selnode);
      this.updateToolbar();
      break;
    }
  } else {
    return null;	// this function not yet used for IE <FIXME>
  }
};

// Returns the deepest node that contains both endpoints of the selection.
HTMLArea.prototype.getParentElement = function() {
  var sel = this._getSelection();
  var range = this._createRange(sel);
  if (HTMLArea.is_ie) {
    return range.parentElement ? range.parentElement() : this._doc.body;
  } else {
    var p = range.commonAncestorContainer;
    while (p.nodeType == 3) {
      p = p.parentNode;
    }
    return p;
  }
};

// Returns an array with all the ancestor nodes of the selection.
HTMLArea.prototype.getAllAncestors = function() {
  var p = this.getParentElement();
  var a = [];
  while (p && (p.nodeType == 1) && (p.tagName.toLowerCase() != 'body')) {
    a.push(p);
    p = p.parentNode;
  }
  a.push(this._doc.body);
  return a;
};

// Selects the contents inside the given node
HTMLArea.prototype.selectNodeContents = function(node, pos) {
  this.focusEditor();
  this.forceRedraw();
  var range;
  var collapsed = (typeof pos != "undefined");
  if (HTMLArea.is_ie) {
    range = this._doc.body.createTextRange();
    range.moveToElementText(node);
    (collapsed) && range.collapse(pos);
    range.select();
  } else {
    var sel = this._getSelection();
    range = this._doc.createRange();
    range.selectNodeContents(node);
    (collapsed) && range.collapse(pos);
    sel.removeAllRanges();
    sel.addRange(range);
  }
};

/** Call this function to insert HTML code at the current position.  It deletes
 * the selection, if any.
 */
HTMLArea.prototype.insertHTML = function(html) {
  var sel = this._getSelection();
  var range = this._createRange(sel);
  if (HTMLArea.is_ie) {
    range.pasteHTML(html);
  } else {
    // construct a new document fragment with the given HTML
    var fragment = this._doc.createDocumentFragment();
    var div = this._doc.createElement("div");
    div.innerHTML = html;
    while (div.firstChild) {
      // the following call also removes the node from div
      fragment.appendChild(div.firstChild);
    }
    // this also removes the selection
    var node = this.insertNodeAtSelection(fragment);
  }
};

/**
 *  Call this function to surround the existing HTML code in the selection with
 *  your tags.  FIXME: buggy!  This function will be deprecated "soon".
 */
HTMLArea.prototype.surroundHTML = function(startTag, endTag) {
  var html = this.getSelectedHTML();
  // the following also deletes the selection
  this.insertHTML(startTag + html + endTag);
};

/// Retrieve the selected block
HTMLArea.prototype.getSelectedHTML = function() {
  var sel = this._getSelection();
  var range = this._createRange(sel);
  var existing = null;
  if (HTMLArea.is_ie) {
    existing = range.htmlText;
  } else {
    existing = HTMLArea.getHTML(range.cloneContents(), false);
  }
  return existing;
};

// Called when the user clicks on "InsertImage" button
HTMLArea.prototype._insertImage = function() {
  var editor = this;	// for nested functions
  this._popupDialog("insert_image.html", function(param) {
    if (!param) {	// user must have pressed Cancel
      return false;
    }
    var sel = editor._getSelection();
    var range = editor._createRange(sel);
    editor._doc.execCommand("insertimage", false, param["f_url"]);
    var img = null;
    if (HTMLArea.is_ie) {
      img = range.parentElement();
      // wonder if this works...
      if (img.tagName.toLowerCase() != "img") {
        img = img.previousSibling;
      }
    } else {
      img = range.startContainer.previousSibling;
    }
    for (field in param) {
      var value = param[field];
      if (!value) {
        continue;
      }
      switch (field) {
          case "f_alt"    : img.alt	 = value; break;
          case "f_border" : img.border = parseInt(value); break;
          case "f_align"  : img.align	 = value; break;
          case "f_vert"   : img.vspace = parseInt(value); break;
          case "f_horiz"  : img.hspace = parseInt(value); break;
      }
    }
  }, null);
};

// Called when the user clicks the Insert Table button
HTMLArea.prototype._insertTable = function() {
  var sel = this._getSelection();
  var range = this._createRange(sel);
  var editor = this;	// for nested functions
  this._popupDialog("insert_table.html", function(param) {
    if (!param) {	// user must have pressed Cancel
      return false;
    }
    var doc = editor._doc;
    // create the table element
    var table = doc.createElement("table");
    // assign the given arguments
    for (var field in param) {
      var value = param[field];
      if (!value) {
        continue;
      }
      switch (field) {
          case "f_width"   : table.style.width = value + param["f_unit"]; break;
          case "f_align"   : table.align	 = value; break;
          case "f_border"  : table.border	 = parseInt(value); break;
          case "f_spacing" : table.cellspacing = parseInt(value); break;
          case "f_padding" : table.cellpadding = parseInt(value); break;
      }
    }
    var tbody = doc.createElement("tbody");
    table.appendChild(tbody);
    for (var i = 0; i < param["f_rows"]; ++i) {
      var tr = doc.createElement("tr");
      tbody.appendChild(tr);
      for (var j = 0; j < param["f_cols"]; ++j) {
        var td = doc.createElement("td");
        tr.appendChild(td);
        // Mozilla likes to see something inside the cell.
        (HTMLArea.is_gecko) && td.appendChild(doc.createElement("br"));
      }
    }
    if (HTMLArea.is_ie) {
      range.pasteHTML(table.outerHTML);
    } else {
      // insert the table
      editor.insertNodeAtSelection(table);
    }
    return true;
  }, null);
};

/***************************************************
 *  Category: EVENT HANDLERS
 ***************************************************/

// el is reference to the SELECT object
// txt is the name of the select field, as in config.toolbar
HTMLArea.prototype._comboSelected = function(el, txt) {
  this.focusEditor();
  var value = el.options[el.selectedIndex].value;
  switch (txt) {
      case "fontname":
      case "fontsize": this.execCommand(txt, false, value); break;
      case "formatblock":
    (HTMLArea.is_ie) && (value = "<" + value + ">");
    this.execCommand(txt, false, value);
    break;
      default:
    // try to look it up in the registered dropdowns
    var dropdown = this.config.customSelects[txt];
    if (typeof dropdown != "undefined") {
      dropdown.action(this);
    } else {
      alert("FIXME: combo box " + txt + " not implemented");
    }
  }
};

// the execCommand function (intercepts some commands and replaces them with
// our own implementation)
HTMLArea.prototype.execCommand = function(cmdID, UI, param) {
  var editor = this;	// for nested functions
  this.focusEditor();
  switch (cmdID.toLowerCase()) {
      case "htmlmode" : this.setMode(); break;
      case "hilitecolor":
    (HTMLArea.is_ie) && (cmdID = "backcolor");
      case "forecolor":
    this._popupDialog("select_color.html", function(color) {
      if (color) { // selection not canceled
        editor._doc.execCommand(cmdID, false, "#" + color);
      }
    }, HTMLArea._colorToRgb(this._doc.queryCommandValue(cmdID)));
    break;
      case "createlink":
    if (HTMLArea.is_ie || !UI) {
      this._doc.execCommand(cmdID, UI, param);
    } else {
      // browser is Mozilla & wants UI
      var param;
      if ((param = prompt("Enter URL"))) {
        this._doc.execCommand(cmdID, false, param);
      }
    }
    break;
      case "popupeditor":
    if (HTMLArea.is_ie) {
      window.open(this.popupURL("fullscreen.html"), "ha_fullscreen",
            "toolbar=no,location=no,directories=no,status=no,menubar=no," +
            "scrollbars=no,resizable=yes,width=640,height=480");
    } else {
      window.open(this.popupURL("fullscreen.html"), "ha_fullscreen",
            "toolbar=no,menubar=no,personalbar=no,width=640,height=480," +
            "scrollbars=no,resizable=yes");
    }
    // pass this object to the newly opened window
    HTMLArea._object = this;
    break;
      case "inserttable": this._insertTable(); break;
      case "insertimage": this._insertImage(); break;
      case "about"    : this._popupDialog("about.html", null, null); break;
///////////////////////////////////////////////////////////////////////////////
      // Sakai project modification, removed "root"
      case "showhelp" : window.open("reference.html", "ha_help"); break;
///////////////////////////////////////////////////////////////////////////////
      default: this._doc.execCommand(cmdID, UI, param);
  }
  this.updateToolbar();
  return false;
};

/** A generic event handler for things that happen in the IFRAME's document.
 * This function also handles key bindings. */
HTMLArea.prototype._editorEvent = function(ev) {
  var editor = this;
  var keyEvent = (HTMLArea.is_ie && ev.type == "keydown") || (ev.type == "keypress");
  if (keyEvent && ev.ctrlKey) {
    var sel = null;
    var range = null;
    var key = String.fromCharCode(HTMLArea.is_ie ? ev.keyCode : ev.charCode).toLowerCase();
    var cmd = null;
    var value = null;
    switch (key) {
        case 'a':
      if (!HTMLArea.is_ie) {
        // KEY select all
        sel = this._getSelection();
        sel.removeAllRanges();
        range = this._createRange();
        range.selectNodeContents(this._doc.body);
        sel.addRange(range);
        HTMLArea._stopEvent(ev);
      }
      break;

      // simple key commands follow

        case 'b': cmd = "bold"; break;
        case 'i': cmd = "italic"; break;
        case 'u': cmd = "underline"; break;
        case 's': cmd = "strikethrough"; break;
        case 'l': cmd = "justifyleft"; break;
        case 'e': cmd = "justifycenter"; break;
        case 'r': cmd = "justifyright"; break;
        case 'j': cmd = "justifyfull"; break;

      // headings
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
      cmd = "formatblock";
      value = "h" + key;
      if (HTMLArea.is_ie) {
        value = "<" + value + ">";
      }
      break;
    }
    if (cmd) {
      // execute simple command
      this.execCommand(cmd, false, value);
      HTMLArea._stopEvent(ev);
    }
  }
///////////////////////////////////////////////////////////////////////////////
// Sakai project modification.
// Internet Explorer only: make the enter key produce BR not P tags
///////////////////////////////////////////////////////////////////////////////
//	/*
  else if (keyEvent) {
    // other keys here
    switch (ev.keyCode) {
     case 13: // KEY enter
       if (HTMLArea.is_ie) {
         this.insertHTML("<br />");
//         HTMLArea._stopEvent(ev);
       }
      break;
    }
  }
//	*/
  // update the toolbar state after some time
  if (editor._timerToolbar) {
    clearTimeout(editor._timerToolbar);
  }
  editor._timerToolbar = setTimeout(function() {
    editor.updateToolbar();
    editor._timerToolbar = null;
  }, 50);
};

// retrieve the HTML
HTMLArea.prototype.getHTML = function() {
  switch (this._editMode) {
      case "wysiwyg"  : return HTMLArea.getHTML(this._doc.body, false);
      case "textmode" : return this._textArea.value;
      default	    : alert("Mode <" + mode + "> not defined!");
  }
  return false;
};

// retrieve the HTML (fastest version, but uses innerHTML)
HTMLArea.prototype.getInnerHTML = function() {
  switch (this._editMode) {
      case "wysiwyg"  : return this._doc.body.innerHTML;
      case "textmode" : return this._textArea.value;
      default	    : alert("Mode <" + mode + "> not defined!");
  }
  return false;
};

// completely change the HTML inside
HTMLArea.prototype.setHTML = function(html) {
  switch (this._editMode) {
      case "wysiwyg"  : this._doc.body.innerHTML = html; break;
      case "textmode" : this._textArea.value = html; break;
      default	    : alert("Mode <" + mode + "> not defined!");
  }
  return false;
};

/***************************************************
 *  Category: UTILITY FUNCTIONS
 ***************************************************/

// browser identification

HTMLArea.agt = navigator.userAgent.toLowerCase();
HTMLArea.is_ie	   = ((HTMLArea.agt.indexOf("msie") != -1) && (HTMLArea.agt.indexOf("opera") == -1));
HTMLArea.is_opera  = (HTMLArea.agt.indexOf("opera") != -1);
HTMLArea.is_mac	   = (HTMLArea.agt.indexOf("mac") != -1);
HTMLArea.is_mac_ie = (HTMLArea.is_ie && HTMLArea.is_mac);
HTMLArea.is_win_ie = (HTMLArea.is_ie && !HTMLArea.is_mac);
HTMLArea.is_gecko  = (navigator.product == "Gecko");

// variable used to pass the object to the popup editor window.
HTMLArea._object = null;

///////////////////////////////////////////////////////////////////////////////
// Sakai project modification.
// Mozilla: return false for versions < 20030210
// Internet Explorer:  return false for IE < 5.5
// if so suppress error messages, just stick with textarea control.
///////////////////////////////////////////////////////////////////////////////
HTMLArea.checkSupportedBrowser = function() {
  // test for supported gecko
  if (HTMLArea.is_gecko) {
    if (navigator.productSub < 20030210) {
//      alert("You need at least Mozilla 1.3 Beta Gecko Engine.");
      return false;
    }
  } // end gecko
  // test for supported ie
  if (HTMLArea.is_ie){
    msieagent = navigator.userAgent.toLowerCase();
    v = msieagent.substring(msieagent.indexOf("msie")+5);
    version = v.substring(0,v.indexOf(";"));
    if (version<"5.5"){
//      alert("You need at least Internet Explorer 5.5.");
     return false;
    }
  }// end ie
  return HTMLArea.is_gecko || HTMLArea.is_ie;
};

// selection & ranges

// returns the current selection object
HTMLArea.prototype._getSelection = function() {
  if (HTMLArea.is_ie) {
    return this._doc.selection;
  } else {
    return this._iframe.contentWindow.getSelection();
  }
};

// returns a range for the current selection
HTMLArea.prototype._createRange = function(sel) {
  if (HTMLArea.is_ie) {
    return sel.createRange();
  } else {
    this.focusEditor();
    if (typeof sel != "undefined") {
      return sel.getRangeAt(0);
    } else {
      return this._doc.createRange();
    }
  }
};

// event handling

HTMLArea._addEvent = function(el, evname, func) {
  if (HTMLArea.is_ie) {
    el.attachEvent("on" + evname, func);
  } else {
    el.addEventListener(evname, func, true);
  }
};

HTMLArea._addEvents = function(el, evs, func) {
  for (var i in evs) {
    HTMLArea._addEvent(el, evs[i], func);
  }
};

HTMLArea._removeEvent = function(el, evname, func) {
  if (HTMLArea.is_ie) {
    el.detachEvent("on" + evname, func);
  } else {
    el.removeEventListener(evname, func, true);
  }
};

HTMLArea._removeEvents = function(el, evs, func) {
  for (var i in evs) {
    HTMLArea._removeEvent(el, evs[i], func);
  }
};

HTMLArea._stopEvent = function(ev) {
  if (HTMLArea.is_ie) {
    ev.cancelBubble = true;
    ev.returnValue = false;
  } else {
    ev.preventDefault();
    ev.stopPropagation();
  }
};

HTMLArea._removeClass = function(el, className) {
  if (!(el && el.className)) {
    return;
  }
  var cls = el.className.split(" ");
  var ar = new Array();
  for (var i = cls.length; i > 0;) {
    if (cls[--i] != className) {
      ar[ar.length] = cls[i];
    }
  }
  el.className = ar.join(" ");
};

HTMLArea._addClass = function(el, className) {
  // remove the class first, if already there
  HTMLArea._removeClass(el, className);
  el.className += " " + className;
};

HTMLArea._hasClass = function(el, className) {
  if (!(el && el.className)) {
    return false;
  }
  var cls = el.className.split(" ");
  for (var i = cls.length; i > 0;) {
    if (cls[--i] == className) {
      return true;
    }
  }
  return false;
};

HTMLArea.isBlockElement = function(el) {
  var blockTags = " body form textarea fieldset ul ol dl li div " +
    "p h1 h2 h3 h4 h5 h6 quote pre table thead " +
    "tbody tfoot tr td iframe address ";
  return (blockTags.indexOf(" " + el.tagName.toLowerCase() + " ") != -1);
};

HTMLArea.needsClosingTag = function(el) {
  var closingTags = " script style div span tr td tbody table em strong font a ";
  return (closingTags.indexOf(" " + el.tagName.toLowerCase() + " ") != -1);
};

// performs HTML encoding of some given string
HTMLArea.htmlEncode = function(str) {
  // we don't need regexp for that, but.. so be it for now.
  str = str.replace(/&/ig, "&amp;");
  str = str.replace(/</ig, "&lt;");
  str = str.replace(/>/ig, "&gt;");
  str = str.replace(/\x22/ig, "&quot;");
  // \x22 means '"' -- we use hex reprezentation so that we don't disturb
  // JS compressors (well, at least mine fails.. ;)
  return str;
};

// Retrieves the HTML code from the given node.	 This is a replacement for
// getting innerHTML, using standard DOM calls.
HTMLArea.getHTML = function(root, outputRoot) {
  var html = "";
  switch (root.nodeType) {
      case 1: // Node.ELEMENT_NODE
      case 11: // Node.DOCUMENT_FRAGMENT_NODE
    var closed;
    var i;
    if (outputRoot) {
      closed = (!(root.hasChildNodes() || HTMLArea.needsClosingTag(root)));
      html = "<" + root.tagName.toLowerCase();
      var attrs = root.attributes;
      for (i = 0; i < attrs.length; ++i) {
        var a = attrs.item(i);
        if (!a.specified) {
          continue;
        }
        var name = a.nodeName.toLowerCase();
        if (/_moz/.test(name)) {
          // Mozilla reports some special tags
          // here; we don't need them.
          continue;
        }
        var value;
        if (name != "style") {
          // IE5.5 reports 25 when cellSpacing is
          // 1; other values might be doomed too.
          // For this reason we extract the
          // values directly from the root node.
          // I'm starting to HATE JavaScript
          // development.  Browser differences
          // suck.
          if (typeof root[a.nodeName] != "undefined") {
            value = root[a.nodeName];
          } else {
            value = a.nodeValue;
          }
        } else { // IE fails to put style in attributes list
          // FIXME: cssText reported by IE is UPPERCASE
          value = root.style.cssText;
        }
        if (/_moz/.test(value)) {
          // Mozilla reports some special tags
          // here; we don't need them.
          continue;
        }
        html += " " + name + '="' + value + '"';
      }
      html += closed ? " />" : ">";
    }
    for (i = root.firstChild; i; i = i.nextSibling) {
      html += HTMLArea.getHTML(i, true);
    }
    if (outputRoot && !closed) {
      html += "</" + root.tagName.toLowerCase() + ">";
    }
    break;
      case 3: // Node.TEXT_NODE
    html = HTMLArea.htmlEncode(root.data);
    break;
      case 8: // Node.COMMENT_NODE
    html = "<!--" + root.data + "-->";
    break;		// skip comments, for now.
  }

///////////////////////////////////////////////////////////////////////////////
// Sakai project modification.
// Mozilla and IE: HTMLArea is turning relative URLs on uploaded links and images
// into absolute URLs--change them back. Suppress host.
///////////////////////////////////////////////////////////////////////////////
  if (html.indexOf(window.location.protocol + "//" + window.location.host)>-1){
    html = __cleanAbsoluteURLs(html);
  }
///////////////////////////////////////////////////////////////////////////////
// Sakai project modification.
// Internet Explorer only: prevent the HTML from having nonproductive P tags
///////////////////////////////////////////////////////////////////////////////
  if (HTMLArea.is_ie && html.indexOf("<p")>-1) {
         html = __cleanParagraphs(html);
  }
  return html;
};

///////////////////////////////////////////////////////////////////////////////
// Sakai project. Retest later versions of HTMLArea to see if still required.
// helper function to eliminate host dependencies on uploaded links
///////////////////////////////////////////////////////////////////////////////
function __cleanAbsoluteURLs(htmlStr){
  newHtmlString = "";
  separator = window.location.protocol + "//" + window.location.host;
  arrayForAbsUrls = htmlStr.split(separator);
  for (var i=0; i < arrayForAbsUrls.length; i++) {
    newHtmlString += arrayForAbsUrls[i];
  }
  return newHtmlString;
}
///////////////////////////////////////////////////////////////////////////////
// Sakai project. Retest later versions of HTMLArea to see if still required.
// helper function to eliminate non alignment P tags
///////////////////////////////////////////////////////////////////////////////
function __cleanParagraphs(htmlStr){
  newHtmlString = "";

  // first, ditch empty P tags ("<p />")
  htmlStr = htmlStr.replace("<p />","");
  htmlStr = htmlStr.replace("<p/>","");

  // now split into an array, "<p" starts all our paragraph tags
  separator = "<p";
  arrayForPTags = htmlStr.split(separator);

  // traversal on the p tags
  for (var i=0; i < arrayForPTags.length; i++) {
      html = arrayForPTags[i];

      // now, if we have "<p" + ">" we want to remove the <p> and </p> tags
      if (html.indexOf(">")==0){
        html = "<p" + html;
        html = html.replace("<p>"," ");
        html = html.replace("</p>"," ");

      } else if
      (html.indexOf(" align")==0 ||// retain p tags with align for justification
       html.indexOf("aram ")==0 || // retain param tags
       html.indexOf("re")==0       // retain pre tags
      )
      {
        html = "<p" + html;
      }
      newHtmlString +=  html;
  }
  return newHtmlString;
}
///////////////////////////////////////////////////////////////////////////////

// creates a rgb-style color from a number
HTMLArea._makeColor = function(v) {
  if (typeof v != "number") {
    // already in rgb (hopefully); IE doesn't get here.
    return v;
  }
  // IE sends number; convert to rgb.
  var r = v & 0xFF;
  var g = (v >> 8) & 0xFF;
  var b = (v >> 16) & 0xFF;
  return "rgb(" + r + "," + g + "," + b + ")";
};

// returns hexadecimal color representation from a number or a rgb-style color.
HTMLArea._colorToRgb = function(v) {
  // returns the hex representation of one byte (2 digits)
  function hex(d) {
    return (d < 16) ? ("0" + d.toString(16)) : d.toString(16);
  };

  if (typeof v == "number") {
    // we're talking to IE here
    var r = v & 0xFF;
    var g = (v >> 8) & 0xFF;
    var b = (v >> 16) & 0xFF;
    return "#" + hex(r) + hex(g) + hex(b);
  }

  if (v.substr(0, 3) == "rgb") {
    // in rgb(...) form -- Mozilla
    var re = /rgb\s*\(\s*([0-9]+)\s*,\s*([0-9]+)\s*,\s*([0-9]+)\s*\)/;
    if (v.match(re)) {
      var r = parseInt(RegExp.$1);
      var g = parseInt(RegExp.$2);
      var b = parseInt(RegExp.$3);
      return "#" + hex(r) + hex(g) + hex(b);
    }
    // doesn't match RE?!  maybe uses percentages or float numbers
    // -- FIXME: not yet implemented.
    return null;
  }

  if (v[0] == "#") {
    // already hex rgb (hopefully :D )
    return v;
  }

  // if everything else fails ;)
  return null;
};

// modal dialogs for Mozilla (for IE we're using the showModalDialog() call).

// receives an URL to the popup dialog and a function that receives one value;
// this function will get called after the dialog is closed, with the return
// value of the dialog.
HTMLArea.prototype._popupDialog = function(url, action, init) {
  Dialog(this.popupURL(url), action, init);
};

// paths

HTMLArea.prototype.imgURL = function(file, plugin) {
  if (typeof plugin == "undefined") {
    return this.config.editorURL + file;
  } else {
    return this.config.editorURL + "plugins/" + plugin + "/img/" + file;
  }
};

HTMLArea.prototype.popupURL = function(file) {
  return this.config.editorURL + this.config.popupURL + file;
};

// EOF
// Local variables: //
// c-basic-offset:8 //
// indent-tabs-mode:t //
// End: //
