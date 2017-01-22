package com.melkiy.teamvoytest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melkiy.teamvoytest.R;
import com.melkiy.teamvoytest.models.Photo;
import com.melkiy.teamvoytest.viewholders.PhotosListViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosListViewHolder> {

    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<Photo> photos = new ArrayList<>();

    public PhotosRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PhotosListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotosListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosListViewHolder holder, int position) {
        Photo photo = photos.get(position);
        imageLoader.displayImage(photo.getUser().getProfileImage().getMedium(), holder.userProfilePhoto);
        holder.username.setText(photo.getUser().getName());
        holder.datePublication.setText(photo.getCreatedAt().toString());
        imageLoader.displayImage(photo.getUrls().getRegular(), holder.photo);
        if (photo.isLikedByUser()) {
            holder.like.setImageResource(R.drawable.ic_favorite_red_28dp);
            holder.countLikes.setTextColor(context.getResources().getColor(R.color.like_red));
        } else {
            holder.like.setImageResource(R.drawable.ic_favorite_grey_28dp);
            holder.countLikes.setTextColor(context.getResources().getColor(R.color.like_grey));
        }
        holder.countLikes.setText(String.valueOf(photo.getLikes()));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }
}
