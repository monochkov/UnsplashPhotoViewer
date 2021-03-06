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
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.melkiy.unsplashphotoviewer.Configuration;
import com.melkiy.unsplashphotoviewer.activities.LoginActivity;
import com.melkiy.unsplashphotoviewer.models.AccessToken;
import com.melkiy.unsplashphotoviewer.models.User;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class API {

    public static final String TAG = API.class.getName();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final API api = new API();
    private static final String API_BASE_URL = "https://" + Configuration.API_HOST + "/";

    private final AuthorizationService authorizationService;
    private final PhotoService photoService;
    private final UserService userService;

    private AccessToken accessToken;
    private User currentUser;

    private AuthenticationStore authenticationStore;

    public static API getInstance() {
        return api;
    }

    private API() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .addInterceptor(new TokenInterceptor())
                .build();

        objectMapper.registerModule(new JodaModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();

        authorizationService = retrofit.create(AuthorizationService.class);
        photoService = retrofit.create(PhotoService.class);
        userService = retrofit.create(UserService.class);
    }

    public void initialize(Context context) {
        authenticationStore = new AuthenticationStore(context);

        this.accessToken = authenticationStore.readAccessToken();
        this.currentUser = authenticationStore.readCurrentUser();

        if (isAuthenticated()) {
            updateCurrentUser();
        }
    }

    private void updateCurrentUser() {
        userService.getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    setCurrentUser(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public boolean isAuthenticated() {
        return accessToken != null;
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public PhotoService getPhotoService() {
        return photoService;
    }

    public UserService getUserService() {
        return userService;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        authenticationStore.saveAccessToken(accessToken);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        authenticationStore.saveCurrentUser(currentUser);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public class TokenInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            if (chain.request().url().toString().contains(LoginActivity.TOKEN_URL)) {
                return chain.proceed(original);
            }

            if (accessToken != null) {
                original = original.newBuilder()
                        .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                        .method(original.method(), original.body())
                        .build();
            } else {
                Log.i(TAG, "Access token is null");
            }

            return chain.proceed(original);
        }
    }
}
