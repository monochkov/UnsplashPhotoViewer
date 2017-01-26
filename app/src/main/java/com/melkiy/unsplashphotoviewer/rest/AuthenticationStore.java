/*
    Copyright (C) 2015 Ihor Monochkov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.melkiy.unsplashphotoviewer.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melkiy.unsplashphotoviewer.models.AccessToken;
import com.melkiy.unsplashphotoviewer.models.User;

import java.io.IOException;

public class AuthenticationStore {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String CURRENT_USER = "current_user";

    private static final ObjectMapper objectMapper = API.getObjectMapper();
    private final SharedPreferences preferences;

    public AuthenticationStore(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveAccessToken(AccessToken accessToken) {
        try {
            String token = objectMapper.writeValueAsString(accessToken);
            preferences.edit()
                    .putString(ACCESS_TOKEN, token)
                    .apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public AccessToken readAccessToken() {
        String rawToken = preferences.getString(ACCESS_TOKEN, "");
        if (!rawToken.isEmpty()) {
            try {
                return objectMapper.readValue(rawToken, AccessToken.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void saveCurrentUser(User user) {
        try {
            String rawUser = objectMapper.writeValueAsString(user);
            preferences.edit()
                    .putString(CURRENT_USER, rawUser)
                    .apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public User readCurrentUser() {
        String rawUser = preferences.getString(CURRENT_USER, "");
        if (!rawUser.isEmpty()) {
            try {
                return objectMapper.readValue(rawUser, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
