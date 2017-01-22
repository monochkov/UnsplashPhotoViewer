package com.melkiy.teamvoytest.rest;

import com.melkiy.teamvoytest.models.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("me")
    Call<User> getCurrentUser();
}
