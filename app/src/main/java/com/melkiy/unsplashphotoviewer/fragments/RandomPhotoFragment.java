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

package com.melkiy.unsplashphotoviewer.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melkiy.unsplashphotoviewer.R;
import com.melkiy.unsplashphotoviewer.activities.PhotoActivity;
import com.melkiy.unsplashphotoviewer.models.LikeResponse;
import com.melkiy.unsplashphotoviewer.models.Photo;
import com.melkiy.unsplashphotoviewer.rest.API;
import com.melkiy.unsplashphotoviewer.rest.PhotoService;
import com.melkiy.unsplashphotoviewer.utils.Constants;
import com.melkiy.unsplashphotoviewer.utils.InternetUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomPhotoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private PhotoService photoService = API.getInstance().getPhotoService();

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView userProfilePhoto;
    private TextView username;
    private TextView datePublication;
    private ImageView photoImageView;
    private ImageView like;
    private TextView countLikes;
    private LinearLayout likeLayout;
    private TextView errorTextView;
    private CardView cardView;

    private Photo photo;

    public static RandomPhotoFragment newInstance() {
        return new RandomPhotoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_random_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        userProfilePhoto = (ImageView) view.findViewById(R.id.user_profile_photo);
        username = (TextView) view.findViewById(R.id.username);
        datePublication = (TextView) view.findViewById(R.id.date_publication);
        photoImageView = (ImageView) view.findViewById(R.id.photo_image_view);
        like = (ImageView) view.findViewById(R.id.like_image_view);
        countLikes = (TextView) view.findViewById(R.id.like_counter_textview);
        likeLayout = (LinearLayout) view.findViewById(R.id.like_layout);
        errorTextView = (TextView) view.findViewById(R.id.no_internet_textview);
        cardView = (CardView) view.findViewById(R.id.card_view);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(10000))
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.drawable.sym_def_app_icon)
                .showImageOnFail(android.R.drawable.sym_def_app_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();


        setVisibility(InternetUtils.hasConnection(getContext()));
        if (InternetUtils.hasConnection(getContext())) {
            loadRandomPhoto();
            swipeRefreshLayout.setRefreshing(false);
        }

        photoImageView.setOnClickListener(v -> {
            PhotoActivity.show(getContext(), photo.getUrls().getRegular());
        });

        likeLayout.setOnClickListener(v -> {
            if (photo.isLikedByUser()) {
                unlike(photo.getId());
            } else {
                like(photo.getId());
            }
        });
    }

    private void loadRandomPhoto() {
        photoService.getRandomPhoto().enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if (response.isSuccessful()) {
                    setPhoto(response.body());
                    initializeFields(response.body());
                }

                if (response.code() == Constants.ERROR_STATUS_FORBIDDEN) {
                    setVisibility(false);
                    errorTextView.setText(Constants.FORBIDDEN_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void like(String photoId) {
        photoService.like(photoId).enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                if (response.isSuccessful()) {
                    updatePhoto(response.body());
                }

                if (response.code() == Constants.ERROR_STATUS_FORBIDDEN) {
                    setVisibility(false);
                    errorTextView.setText(Constants.FORBIDDEN_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void unlike(String photoId) {
        photoService.unlike(photoId).enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                if (response.isSuccessful()) {
                    updatePhoto(response.body());
                }

                if (response.code() == Constants.ERROR_STATUS_FORBIDDEN) {
                    setVisibility(false);
                    errorTextView.setText(Constants.FORBIDDEN_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updatePhoto(LikeResponse likeResponse) {
        Photo photo = likeResponse.getPhoto();
        photo.setUser(this.photo.getUser());
        setPhoto(photo);
        initializeFields(photo);
    }

    private void setPhoto(Photo photo) {
        this.photo = photo;
        initializeFields(photo);
    }

    private void initializeFields(Photo photo) {
        imageLoader.displayImage(photo.getUser().getProfileImage().getMedium(), userProfilePhoto, options);
        username.setText(photo.getUser().getName());

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        datePublication.setText(formatter.print(photo.getCreatedAt()));

        imageLoader.displayImage(photo.getUrls().getRegular(), photoImageView);
        if (photo.isLikedByUser()) {
            like.setImageResource(R.drawable.ic_favorite_red_28dp);
            countLikes.setTextColor(getResources().getColor(R.color.like_red));
        } else {
            like.setImageResource(R.drawable.ic_favorite_grey_28dp);
            countLikes.setTextColor(getResources().getColor(R.color.like_grey));
        }
        countLikes.setText(String.valueOf(photo.getLikes()));
    }

    @Override
    public void onRefresh() {
        setVisibility(InternetUtils.hasConnection(getContext()));
        if (InternetUtils.hasConnection(getContext())) {
            loadRandomPhoto();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void setVisibility(boolean visibility) {
        if (visibility) {
            cardView.setVisibility(View.VISIBLE);
            errorTextView.setVisibility(View.GONE);
        } else {
            cardView.setVisibility(View.GONE);
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}
