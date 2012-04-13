/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-impl/api-impl/src/java/org/sakaiproject/metaobj/shared/model/DateBean.java $
 * $Id: DateBean.java 59676 2009-04-03 23:18:23Z arwhyte@umich.edu $
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

package org.sakaiproject.metaobj.shared.model;

import java.text.MessageFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.metaobj.utils.mvc.intf.FieldValueWrapper;
import org.sakaiproject.metaobj.utils.xml.NormalizationException;
import org.sakaiproject.util.DateWidgetFormat;

public class DateBean implements FieldValueWrapper {
   protected final Log logger = LogFactory.getLog(getClass());
   private String month = "";
   private String year = "";
   private String day = "";
   private String hour = "";
   private String minute = "";
   private String second = "";
   private String fullDate = null;
   boolean nullFlag = true;

   private Format dateFormat;
   private DateWidgetFormat dateFormatUtil = new DateWidgetFormat();

   public DateBean() {
      dateFormat = dateFormatUtil.getLocaleDateFormat();
   }

   public DateBean(Date date) {
      dateFormat = dateFormatUtil.getLocaleDateFormat();
      if (date != null) {
         setBackingDate(date);
      }
   }

   public void setBackingDate(Date date) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      setYear("" + cal.get(Calendar.YEAR));
      setMonth("" + (cal.get(Calendar.MONTH) + 1));
      setDay("" + cal.get(Calendar.DATE));
      setHour("" + cal.get(Calendar.HOUR));
      setMinute("" + cal.get(Calendar.MINUTE));
      setSecond("" + cal.get(Calendar.SECOND));
      nullFlag = false;
   }

   public String getMonth() {
      return month;
   }

   public void setMonth(String month) {
      this.month = month;
      checkFlag(month);
   }

   public String getYear() {
      return year;
   }

   public void setYear(String year) {
      this.year = year;
      checkFlag(year);
   }

   public String getDay() {
      return day;
   }

   public void setDay(String day) {
      this.day = day;
      checkFlag(day);
   }

   public String getHour() {
      return hour;
   }

   public void setHour(String hour) {
      this.hour = hour;
      checkFlag(hour);
   }

   public String getMinute() {
      return minute;
   }

   public void setMinute(String minute) {
      this.minute = minute;
      checkFlag(minute);
   }

   public String getSecond() {
      return second;
   }

   public void setSecond(String second) {
      this.second = second;
      checkFlag(second);
   }

   public void setValue(Object value) {
      setBackingDate((Date) value);
      nullFlag = (value == null);
   }

   public Object getValue() {
      return getDate();
   }

   public void validate(String fieldName, List errors, String label) {
      if (nullFlag) {
         return;
      }

      if (fullDate != null) {
         try {
            setValue(dateFormat.parseObject(getFullDate()));
         } catch (ParseException e) {
            errors.add(new ValidationError(label, fieldName, NormalizationException.DATE_INVALID_ERROR_CODE,
               new Object[]{getFullDate()}, MessageFormat.format("invalid date {0}", new Object[]{getFullDate()})));
            nullFlag = true;
         }

         return;
      }

      try {
         Integer.parseInt(getYear());
      }
      catch (NumberFormatException e) {
         errors.add(new ValidationError(label, fieldName + ".year", "invalid year {0}", new Object[]{getYear()},
               MessageFormat.format("invalid year {0}", new Object[]{getYear()})));
      }
      try {
         Integer.parseInt(getMonth());
      }
      catch (NumberFormatException e) {
         errors.add(new ValidationError(label, fieldName + ".month", "invalid month {0}", new Object[]{getYear()},
               MessageFormat.format("invalid month {0}", new Object[]{getYear()})));
      }
      try {
         Integer.parseInt(getDay());
      }
      catch (NumberFormatException e) {
         errors.add(new ValidationError(label, fieldName + ".day", "invalid day {0}", new Object[]{getYear()},
               MessageFormat.format("invalid day {0}", new Object[]{getYear()})));
      }

      /*
      try {
         Integer.parseInt(getHour());
      } catch (NumberFormatException e) {
         ValidationError error = new ValidationError("invalid hour: {0}",
               new Object[]{getHour()});
         errors.add(error);
      }
      try {
         Integer.parseInt(getMinute());
      } catch (NumberFormatException e) {
         ValidationError error = new ValidationError("invalid minute: {0}",
               new Object[]{getMinute()});
         errors.add(error);
      }
      try {
         Integer.parseInt(getSecond());
      } catch (NumberFormatException e) {
         ValidationError error = new ValidationError("invalid second: {0}",
               new Object[]{getSecond()});
         errors.add(error);
      }
      */
   }

   public Date getDate() {
      if (nullFlag) {
         return null;
      }

      return new GregorianCalendar(getValue(getYear()),
            getValue(getMonth()) - 1, // month is zero indexed
            getValue(getDay()),
            getValue(getHour()),
            getValue(getMinute()),
            getValue(getSecond())).getTime();
   }

   protected int getValue(String value) {
      try {
         return Integer.parseInt(value);
      }
      catch (NumberFormatException e) {
         return 0;
      }
   }

   protected void checkFlag(String value) {
      nullFlag = (value == null || value.length() == 0);
   }

   public String getFullDate() {
      return fullDate;
   }

   public void setFullDate(String fullDate) {
      this.fullDate = fullDate;
      checkFlag(fullDate);
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException e) {
         throw new RuntimeException(e);
      }
   }
}
