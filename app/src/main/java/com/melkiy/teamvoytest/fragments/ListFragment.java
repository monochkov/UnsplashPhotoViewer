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
import android.widget.TextView;

import com.melkiy.teamvoytest.R;
import com.melkiy.teamvoytest.adapters.PhotosRecyclerViewAdapter;
import com.melkiy.teamvoytest.models.LikeResponse;
import com.melkiy.teamvoytest.models.Photo;
import com.melkiy.teamvoytest.rest.API;
import com.melkiy.teamvoytest.rest.PhotoService;
import com.melkiy.teamvoytest.utils.Intents;
import com.melkiy.teamvoytest.utils.InternetUtils;

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
    private TextView noInternetTextView;
    private PhotosRecyclerViewAdapter adapter;

    private SortDialogFragment sortDialogFragment;

    private PhotoService photoService = API.getInstance().getPhotoService();

    private List<Photo> photos = new ArrayList<>();
    private String orderBy = SortDialogFragment.ORDER_BY_LATEST;

    //For pagination
    private int page = 1;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.photos_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PhotosRecyclerViewAdapter(getContext());
        adapter.setOnPhotoClickListener(this);
        recyclerView.setAdapter(adapter);

        //Pagination
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loadPhotosWithPagination(++page);
                    loading = true;
                }
            }
        });

        noInternetTextView = (TextView) view.findViewById(R.id.no_internet_textview);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        setVisibility(InternetUtils.isOnline(getContext()));
        if (InternetUtils.isOnline(getContext())) {
            loadPhotos(orderBy);
            swipeRefreshLayout.setRefreshing(false);
        }

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
        return super.onOptionsItemSelected(item);
    }

    private void loadPhotos(String orderBy) {
        photoService.getPhotos(orderBy).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        //For pagination
                        page = 1;
                        previousTotal = 0;
                        loading = true;
                        visibleThreshold = 5;

                        setPhotos(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadPhotosWithPagination(int page) {
        photoService.getPhotos(page, orderBy).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        photos.addAll(response.body());
                        setPhotos(photos);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setPhotos(List<Photo> photos) {
        this.photos = photos;
        adapter.setPhotos(photos);
    }

    @Override
    public void onRefresh() {
        setVisibility(InternetUtils.isOnline(getContext()));
        if (InternetUtils.isOnline(getContext())) {
            loadPhotos(orderBy);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPhotoClicked(String photoUrl) {
        Intents.startPhotoActivity(getContext(), photoUrl);
    }

    @Override
    public void onLikeClicked(Photo photo) {
        if (photo.isLikedByUser()) {
            unlike(photo.getId());
        } else {
            like(photo.getId());
        }
    }

    private void like(String photoId) {
        photoService.like(photoId).enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        updatePhoto(response.body());
                    }
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
                if (response != null) {
                    if (response.isSuccessful()) {
                        updatePhoto(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updatePhoto(LikeResponse likeResponse) {
        for (int i = 0; i < photos.size(); i++) {
            Photo photo = photos.get(i);
            if (photo.getId().equals(likeResponse.getPhoto().getId())) {
                Photo newPhoto = likeResponse.getPhoto();
                newPhoto.setUser(photo.getUser());
                photos.set(i, newPhoto);
            }
        }
        setPhotos(photos);
    }

    @Override
    public void onOrderValueClick(String orderBy) {
        this.orderBy = orderBy;
        setVisibility(InternetUtils.isOnline(getContext()));
        if (InternetUtils.isOnline(getContext())) {
            loadPhotos(orderBy);
        }
    }

    private void setVisibility(boolean visibility) {
        if (visibility) {
            recyclerView.setVisibility(View.VISIBLE);
            noInternetTextView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noInternetTextView.setVisibility(View.VISIBLE);
        }
    }
}
