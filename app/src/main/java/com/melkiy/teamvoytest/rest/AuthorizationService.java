package com.melkiy.teamvoytest.rest;

import com.melkiy.teamvoytest.activities.LoginActivity;
import com.melkiy.teamvoytest.models.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthorizationService {

    @FormUrlEncoded
    @POST(LoginActivity.TOKEN_URL)
    Call<AccessToken> authorize(@Field("client_id") String clientId,
                                @Field("client_secret") String clientSecret,
                                @Field("redirect_uri") String redirectUri,
                                @Field("code") String code,
                                @Field("grant_type") String grantType);
}
