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

import com.melkiy.unsplashphotoviewer.models.LikeResponse;
import com.melkiy.unsplashphotoviewer.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PhotoService {

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("order_by") String order);

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("page") int page,
                                @Query("order_by") String order);


    @GET("photos/random")
    Call<Photo> getRandomPhoto();

    @POST("photos/{id}/like")
    Call<LikeResponse> like(@Path("id") String id);

    @DELETE("photos/{id}/like")
    Call<LikeResponse> unlike(@Path("id") String id);
}
