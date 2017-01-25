package com.melkiy.unsplashphotoviewer.rest;

import com.melkiy.unsplashphotoviewer.models.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("me")
    Call<User> getCurrentUser();
}
