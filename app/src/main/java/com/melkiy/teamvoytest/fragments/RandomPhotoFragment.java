package com.melkiy.teamvoytest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melkiy.teamvoytest.R;
import com.melkiy.teamvoytest.models.LikeResponse;
import com.melkiy.teamvoytest.models.Photo;
import com.melkiy.teamvoytest.rest.API;
import com.melkiy.teamvoytest.rest.PhotoService;
import com.melkiy.teamvoytest.utils.Intents;
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

    private Photo photo;

    public RandomPhotoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_photo, container, false);

        userProfilePhoto = (ImageView) view.findViewById(R.id.user_profile_photo);
        username = (TextView) view.findViewById(R.id.username);
        datePublication = (TextView) view.findViewById(R.id.date_publication);
        photoImageView = (ImageView) view.findViewById(R.id.photo_image_view);
        like = (ImageView) view.findViewById(R.id.like_image_view);
        countLikes = (TextView) view.findViewById(R.id.like_counter_textview);
        likeLayout = (LinearLayout) view.findViewById(R.id.like_layout);

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

        loadRandomPhoto();

        photoImageView.setOnClickListener(v -> {
            Intents.startPhotoActivity(getContext(), photo.getUrls().getRegular());
        });

        likeLayout.setOnClickListener(v -> {
            if (photo.isLikedByUser()) {
                unlike(photo.getId());
            } else {
                like(photo.getId());
            }
        });
        return view;
    }

    private void loadRandomPhoto() {
        photoService.getRandomPhoto().enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                Photo photo = response.body();
                setPhoto(photo);
                initializeFields(photo);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void like(String photoId) {
        System.out.println("Like " + photoId);
        photoService.like(photoId).enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                updatePhoto(response.body());
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
                updatePhoto(response.body());
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
    }

    private void initializeFields(Photo photo) {
        System.out.println(photo.toString());
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
        loadRandomPhoto();
    }
}
