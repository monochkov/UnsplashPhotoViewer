package com.melkiy.unsplashphotoviewer.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.melkiy.unsplashphotoviewer.R;
import com.melkiy.unsplashphotoviewer.activities.PhotoActivity;
import com.melkiy.unsplashphotoviewer.adapters.PhotosRecyclerAdapter;
import com.melkiy.unsplashphotoviewer.models.LikeResponse;
import com.melkiy.unsplashphotoviewer.models.Order;
import com.melkiy.unsplashphotoviewer.models.Photo;
import com.melkiy.unsplashphotoviewer.rest.API;
import com.melkiy.unsplashphotoviewer.rest.PhotoService;
import com.melkiy.unsplashphotoviewer.utils.Constants;
import com.melkiy.unsplashphotoviewer.utils.InternetUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        PhotosRecyclerAdapter.OnPhotoClickListener,
        SortDialogFragment.OnOrderListItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView errorTextView;
    private PhotosRecyclerAdapter adapter;

    private SortDialogFragment sortDialogFragment;

    private PhotoService photoService = API.getInstance().getPhotoService();

    private List<Photo> photos = new ArrayList<>();
    private Order order = Order.LATEST;

    //For pagination
    private int page = 1;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;

    public static PhotosFragment newInstance() {
        return new PhotosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.photos_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PhotosRecyclerAdapter(getContext());
        adapter.setOnPhotoClickListener(this);
        recyclerView.setAdapter(adapter);

        //Pagination
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading && totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    loadPhotosWithPagination(++page);
                    loading = true;
                }
            }
        });

        errorTextView = (TextView) view.findViewById(R.id.no_internet_textview);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        showRecyclerView(InternetUtils.hasConnection(getContext()));
        if (InternetUtils.hasConnection(getContext())) {
            loadPhotos(order);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_photos, menu);
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

    private void loadPhotos(Order order) {
        photoService.getPhotos(order.getValue()).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    //For pagination
                    page = 1;
                    previousTotal = 0;
                    loading = true;
                    visibleThreshold = 5;

                    setPhotos(response.body());
                }

                if (response.code() == Constants.ERROR_STATUS_FORBIDDEN) {
                    showRecyclerView(false);
                    errorTextView.setText(Constants.FORBIDDEN_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadPhotosWithPagination(int page) {
        photoService.getPhotos(page, order.getValue()).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    photos.addAll(response.body());
                    setPhotos(photos);
                }

                if (response.code() == Constants.ERROR_STATUS_FORBIDDEN) {
                    showRecyclerView(false);
                    errorTextView.setText(Constants.FORBIDDEN_MESSAGE);
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
        showRecyclerView(InternetUtils.hasConnection(getContext()));
        if (InternetUtils.hasConnection(getContext())) {
            loadPhotos(order);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPhotoClicked(String photoUrl) {
        PhotoActivity.show(getContext(), photoUrl);
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
                if (response.isSuccessful()) {
                    updatePhoto(response.body());
                }

                if (response.code() == Constants.ERROR_STATUS_FORBIDDEN) {
                    showRecyclerView(false);
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
                    showRecyclerView(false);
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
    public void onOrderValueClick(Order order) {
        this.order = order;
        showRecyclerView(InternetUtils.hasConnection(getContext()));
        if (InternetUtils.hasConnection(getContext())) {
            loadPhotos(order);
        }
    }

    private void showRecyclerView(boolean visibility) {
        if (visibility) {
            recyclerView.setVisibility(View.VISIBLE);
            errorTextView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            errorTextView.setVisibility(View.VISIBLE);
        }
    }
}
