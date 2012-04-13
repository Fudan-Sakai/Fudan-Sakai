/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/metaobj/branches/sakai-2.8.1/metaobj-util/tool-lib/src/java/org/sakaiproject/spring/util/SakaiLocaleResolver.java $
 * $Id: SakaiLocaleResolver.java 98455 2011-09-20 22:36:35Z botimer@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2011 The Sakai Foundation
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

package org.sakaiproject.spring.util;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sakaiproject.util.ResourceLoader;
import org.springframework.web.servlet.LocaleResolver;

public class SakaiLocaleResolver implements LocaleResolver {
    //Shared ResourceLoader -- it is thread-safe with default constructor
    private static ResourceLoader loader = new ResourceLoader();

    public Locale resolveLocale(HttpServletRequest request) {
        Locale userOrSystem = loader.getLocale();
        return userOrSystem;
    }

    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException();
    }
}

