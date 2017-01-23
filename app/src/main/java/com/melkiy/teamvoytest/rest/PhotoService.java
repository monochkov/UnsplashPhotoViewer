package com.melkiy.teamvoytest.rest;

import com.melkiy.teamvoytest.models.LikeResponse;
import com.melkiy.teamvoytest.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PhotoService {

    @GET("photos/{id}")
    Call<Photo> getPhoto(@Path("id") String id);

    @GET("photos")
    Call<List<Photo>> getPhotos();

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("order_by") String order);

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("page") int page,
                                @Query("order_by") String order);

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("page") int page,
                                @Query("per_page") int perPage,
                                @Query("order_by") String order);

    @GET("photos/random")
    Call<Photo> getRandomPhoto();

    @POST("photos/{id}/like")
    Call<LikeResponse> like(@Path("id") String id);

    @DELETE("photos/{id}/like")
    Call<LikeResponse> unlike(@Path("id") String id);
}
