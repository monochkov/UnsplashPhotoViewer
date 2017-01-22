package com.melkiy.teamvoytest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.melkiy.teamvoytest.R;
import com.melkiy.teamvoytest.adapters.PhotosRecyclerViewAdapter;
import com.melkiy.teamvoytest.models.Photo;
import com.melkiy.teamvoytest.rest.API;
import com.melkiy.teamvoytest.rest.PhotoService;
import com.melkiy.teamvoytest.utils.Intents;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        PhotosRecyclerViewAdapter.OnPhotoClickListener,
        SortDialogFragment.OnOrderListItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private PhotosRecyclerViewAdapter adapter;

    private SortDialogFragment sortDialogFragment;

    private PhotoService photoService = API.getInstance().getPhotoService();

    private List<Photo> photos = new ArrayList<>();
    private String orderBy = SortDialogFragment.ORDER_BY_LATEST;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.photos_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PhotosRecyclerViewAdapter(getContext());
        adapter.setOnPhotoClickListener(this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        //TODO: recommit
        //loadPhotos();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                sortDialogFragment = new SortDialogFragment();
                sortDialogFragment.setOnOrderListItemClickListener(this);
                sortDialogFragment.show(getActivity().getFragmentManager(), SortDialogFragment.TAG);
                return true;
        }
        return false;
    }

    private void loadPhotos(String orderBy) {
        photoService.getPhotos(orderBy).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                setPhotos(response.body());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void refreshPhotos() {
        loadPhotos(orderBy);
    }

    private void setPhotos(List<Photo> photos) {
        this.photos = photos;
        adapter.setPhotos(photos);
    }

    @Override
    public void onRefresh() {
        refreshPhotos();
    }

    @Override
    public void onPhotoClicked(String photoUrl) {
        Intents.startPhotoActivity(getContext(), photoUrl);
    }

    @Override
    public void onLikeClicked(Photo photo) {
        /*if (photo.isLikedByUser()) {
            unlike(photo.getId());
        } else {
            like(photo.getId());
        }*/
    }
/*
    private void like(String photoId) {
        photoService.like(photoId).enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                //updatePhoto(photoId);
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void unlike(String photoId) {
        photoService.unlike(photoId).enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                //updatePhoto(photoId);
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    private void updatePhoto(String photoId) {
        for (Photo photo : photos) {

        }
    }

    @Override
    public void onOrderValueClick(String orderBy) {
        this.orderBy = orderBy;
        loadPhotos(orderBy);
    }
}
