package com.melkiy.teamvoytest.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melkiy.teamvoytest.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class API {

    public static final String TAG = API.class.getName();

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final API api = new API();
    private static final String API_BASE_URL = "https://" + BuildConfig.API_HOST + "/";

    private final AuthorizationService authorizationService;
    private final PhotoService photoService;
    private final UserService userService;

    public static API getInstance() {
        return api;
    }

    private API() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        authorizationService = retrofit.create(AuthorizationService.class);
        photoService = retrofit.create(PhotoService.class);
        userService = retrofit.create(UserService.class);
    }
}
