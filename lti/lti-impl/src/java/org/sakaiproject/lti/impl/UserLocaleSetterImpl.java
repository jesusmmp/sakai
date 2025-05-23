/**
 * Copyright (c) 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sakaiproject.lti.impl;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.tsugi.lti.LTIConstants;

import org.sakaiproject.entity.api.ResourcePropertiesEdit;
import org.sakaiproject.lti.api.UserLocaleSetter;
import org.sakaiproject.user.api.Preferences;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.PreferencesService;

/**
 *  @author Adrian Fish <a.fish@lancaster.ac.uk>
 */
@Slf4j
public class UserLocaleSetterImpl implements UserLocaleSetter {

    private PreferencesService preferencesService = null;
    public void setPreferencesService(PreferencesService  preferencesService) {
        this.preferencesService = preferencesService;
    }
     /* Email Trusted consumer case we are not creating the user, we look up the user based on the email address. 
      snd user Locale must already be set and simply return*/
    public void setupUserLocale(Map payload, User user, boolean isTrustedConsumer, boolean isEmailTrustedConsumer) {
        if (isTrustedConsumer) return;
        if (isEmailTrustedConsumer) return;

        String locale = (String) payload.get(LTIConstants.LAUNCH_PRESENTATION_LOCALE);
        if (user != null && locale != null && !locale.isEmpty()) {
            String userId = user.getId();
            preferencesService.applyEditWithAutoCommit(userId, edit -> {
                ResourcePropertiesEdit propsEdit = edit.getPropertiesEdit("sakai:resourceloader");
                propsEdit.removeProperty(Preferences.FIELD_LOCALE);
                propsEdit.addProperty(Preferences.FIELD_LOCALE, locale);
            });
        }
    }
}
