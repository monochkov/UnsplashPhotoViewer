package com.melkiy.teamvoytest.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melkiy.teamvoytest.models.AccessToken;
import com.melkiy.teamvoytest.models.User;

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
