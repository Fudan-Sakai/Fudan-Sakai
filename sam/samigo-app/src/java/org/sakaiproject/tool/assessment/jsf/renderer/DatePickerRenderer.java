/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/sam/tags/samigo-2.8.3/samigo-app/src/java/org/sakaiproject/tool/assessment/jsf/renderer/DatePickerRenderer.java $
 * $Id: DatePickerRenderer.java 59684 2009-04-03 23:33:27Z arwhyte@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
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




package org.sakaiproject.tool.assessment.jsf.renderer;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import org.sakaiproject.util.ResourceLoader;

import org.sakaiproject.tool.assessment.jsf.renderer.util.RendererUtil;
import org.sakaiproject.tool.assessment.ui.listener.util.ContextUtil;

/**
 * <p>Description: </p>
 * <p>Render the custom color picker control.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organization: Sakai Project</p>
 * @author Ed Smiley
 * @version $id: $
 */

public class DatePickerRenderer extends Renderer
{
  // icon height and width
  private static final String HEIGHT = "16";
  private static final String WIDTH = "16";
  private static final String CURSORSTYLE = "cursor:pointer;";
  
  //moved to properties
  //private static final String CLICKALT = "Click Here to Pick Date";

  public boolean supportsComponentType(UIComponent component)
  {
    return (component instanceof UIInput);
  }

  /**
   * decode the value
   * @param context
   * @param component
   */
  public void decode(FacesContext context, UIComponent component)
  {
    // we haven't added these attributes--yet--defensive programming...
    if(RendererUtil.isDisabledOrReadonly(component))
    {
      return;
    }

    String clientId = component.getClientId(context);
    Map requestParameterMap = context.getExternalContext()
                              .getRequestParameterMap();
    String newValue = (String) requestParameterMap.get(clientId );
    UIInput comp = (UIInput) component;
    comp.setSubmittedValue(newValue);
  }

  public void encodeBegin(FacesContext context,
    UIComponent component) throws IOException
  {
    ;
  }

  public void encodeChildren(FacesContext context,
    UIComponent component) throws IOException
  {
    ;
  }

  /**
   * <p>Faces render output method .</p>
   * <p>Method Generator: org.sakaiproject.tool.assessment.devtoolsRenderMaker</p>
   *
   *  @param context   <code>FacesContext</code> for the current request
   *  @param component <code>UIComponent</code> being rendered
   *
   * @throws IOException if an input/output error occurs
   */
  public void encodeEnd(FacesContext context,
    UIComponent component) throws IOException
  {
	ResourceLoader rb= new ResourceLoader("org.sakaiproject.tool.assessment.bundle.AssessmentSettingsMessages");
    ResponseWriter writer = context.getResponseWriter();
    String contextPath = context.getExternalContext()
                         .getRequestContextPath();

    String jsfId = (String) component.getAttributes().get("id");
    String id = jsfId;

    if (component.getId() != null &&
      !component.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
    {
      id = component.getClientId(context);
    }

    Object value = null;
    if (component instanceof UIInput)
    {
      value = ( (UIInput) component).getSubmittedValue();
    }
    if (value == null && component instanceof ValueHolder)
    {
      value = ( (ValueHolder) component).getValue();
    }
    String valString = "";
    if (value != null)
    {
      valString = value.toString();
    }

    String type = "text";
    String size = (String) component.getAttributes().get("size");
    if (size == null)
    {
      size = "20";

      // script creates unique calendar object with input object
    }
    
    String display_dateFormat= ContextUtil.getLocalizedString("org.sakaiproject.tool.assessment.bundle.GeneralMessages","output_data_picker_w_sec");
    String genDate = null;
    String prsDate = null;
    if (display_dateFormat.toLowerCase().startsWith("dd")) {
    	genDate = "cal_gen_date2_dm";
    	prsDate = "cal_prs_date2_dm";
    }
    else {
    	genDate = "cal_gen_date2_md";
    	prsDate = "cal_prs_date2_md";
    }
    String calRand = "cal" + ("" + Math.random()).substring(2);
    String calScript =
      "var " + calRand + " = new calendar2(" +
      "document.getElementById('" + id + "'), " + genDate + ", " + prsDate + ");" +
      "" + calRand + ".year_scroll = true;" +
      "" + calRand + ".time_comp = true;";

    writer.write("<input type=\"" + type + "\" name=\"" + id +
      "\" id=\"" + id + "\" size=\"" + size + "\" value=");
    writer.write("\"" + valString + "\">&#160;<img \n  onclick=");
    writer.write("\"javascript:" + calScript +
      calRand + ".popup('','" + contextPath +
      "/html/');\"\n");
//    "/jsf/widget/datepicker/');\"\n");
    writer.write("  width=\"" + WIDTH + "\"\n");
    writer.write("  height=\"" + HEIGHT + "\"\n");
    writer.write("  style=\"" + CURSORSTYLE + "\" ");
    writer.write("  src=\"" + contextPath + "/images/calendar/cal.gif\"\n");
    writer.write("  border=\"0\"\n");
    writer.write("  id=\"_datePickerPop_" + id + "\"");
    //writer.write("  alt=\"" + CLICKALT + "\"/>&#160;&#160;\n");
    writer.write("  alt=\"" + rb.getString("dp_CLICKALT") + "\"/>&#160;&#160;\n");
  }

}
