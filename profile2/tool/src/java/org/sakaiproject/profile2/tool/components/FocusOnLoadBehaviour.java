/**
 * Copyright (c) 2008-2010 The Sakai Foundation
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

package org.sakaiproject.profile2.tool.components;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

/*
 * FocusOnLoadBehaviour. 
 * A component you add to another component to make it focused when it is rendered.
 * 
 * use:
 * 
 * component.add(new FocusOnLoadBehaviour);
 * 
 * Steve Swinsburg
 * steve.swinsburg@gmail.com
 * 
 * ref: http://cwiki.apache.org/confluence/display/WICKET/Request+Focus+on+a+Specific+Form+Component
 * 
 */

public class FocusOnLoadBehaviour extends AbstractBehavior {
    
	private static final long serialVersionUID = 1L;
	
	private Component component;

    public void bind( Component component )
    {
        this.component = component;
        component.setOutputMarkupId(true);
    }

    public void renderHead( IHeaderResponse iHeaderResponse )
    {
        super.renderHead(iHeaderResponse);
        iHeaderResponse.renderOnLoadJavascript("document.getElementById('" + component.getMarkupId() + "').focus()");
    }

    public boolean isTemporary()
    {
        // remove the behavior after component has been rendered       
        return true;
    }
}

