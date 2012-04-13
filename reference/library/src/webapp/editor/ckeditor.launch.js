/*******************************************************************************
 * $URL:  $
 * $Id:  $
 * **********************************************************************************
 *
 * Copyright (c) 2010 The Sakai Foundation
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
 ******************************************************************************/

var sakai = sakai || {};
sakai.editor = sakai.editor || {};
sakai.editor.editors = sakai.editor.editors || {};

sakai.editor.editors.ckeditor = {};
sakai.editor.editors.ckeditor.launch = function(targetId, config) {
    var folder = "";
    if (sakai.editor.collectionId) {
        folder = "&CurrentFolder=" + sakai.editor.collectionId;
    }
    CKEDITOR.replace(targetId, {
        skin: 'v2',
        height: 460,
        filebrowserBrowseUrl :'/library/editor/FCKeditor/editor/filemanager/browser/default/browser.html?Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector' + folder,
        filebrowserImageBrowseUrl : '/library/editor/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Image&Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector' + folder,
        filebrowserFlashBrowseUrl :'/library/editor/FCKeditor/editor/filemanager/browser/default/browser.html?Type=Flash&Connector=/sakai-fck-connector/web/editor/filemanager/browser/default/connectors/jsp/connector' + folder,
        extraPlugins: (sakai.editor.enableResourceSearch ? 'resourcesearch' : ''),

        // These two settings enable the browser's native spell checking and context menus.
        // Control-Right-Click (Windows/Linux) or Command-Right-Click (Mac) on highlighted words
        // will cause the CKEditor menu to be suppressed and display the browser's standard context
        // menu. In some cases (Firefox and Safari, at least), this supplies corrections, suggestions, etc.
        disableNativeSpellChecker: false,
        browserContextMenuOnCtrl: true,

        toolbar_Full:
        [
            ['Source','-','Templates'],
            // Uncomment the next line and comment the following to enable the default spell checker.
            // Note that it uses spellchecker.net, displays ads and sends content to remote servers without additional setup.
            //['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
            ['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print'],
            ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
            ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote','CreateDiv'],
            '/',
            ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
            ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
            ['BidiLtr', 'BidiRtl' ],
            ['Link','Unlink','Anchor'],
            (sakai.editor.enableResourceSearch
                ? ['ResourceSearch', 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak']
                : ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak']),
            '/',
            ['Styles','Format','Font','FontSize'],
            ['TextColor','BGColor'],
            ['Maximize', 'ShowBlocks','-','About']
        ],
        resize_dir: 'vertical'
    });
}

sakai.editor.launch = sakai.editor.editors.ckeditor.launch;

