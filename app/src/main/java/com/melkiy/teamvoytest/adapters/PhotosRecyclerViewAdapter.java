package com.melkiy.teamvoytest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melkiy.teamvoytest.R;
import com.melkiy.teamvoytest.models.Photo;
import com.melkiy.teamvoytest.viewholders.PhotosListViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosListViewHolder> {

    private Context context;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<Photo> photos = new ArrayList<>();

    private DisplayImageOptions options;

    private OnPhotoClickListener onPhotoClickListener;

    public PhotosRecyclerViewAdapter(Context context) {
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(10000))
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.drawable.sym_def_app_icon)
                .showImageOnFail(android.R.drawable.sym_def_app_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public PhotosListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotosListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosListViewHolder holder, int position) {
        Photo photo = photos.get(position);
        imageLoader.displayImage(photo.getUser().getProfileImage().getMedium(), holder.userProfilePhoto, options);
        holder.username.setText(photo.getUser().getName());

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");
        holder.datePublication.setText(formatter.print(photo.getCreatedAt()));

        imageLoader.displayImage(photo.getUrls().getRegular(), holder.photo);
        if (photo.isLikedByUser()) {
            holder.like.setImageResource(R.drawable.ic_favorite_red_28dp);
            holder.countLikes.setTextColor(context.getResources().getColor(R.color.like_red));
        } else {
            holder.like.setImageResource(R.drawable.ic_favorite_grey_28dp);
            holder.countLikes.setTextColor(context.getResources().getColor(R.color.like_grey));
        }
        holder.countLikes.setText(String.valueOf(photo.getLikes()));

        holder.photo.setOnClickListener(v -> {
            notifyPhotoClicked(photo.getUrls().getRegular());
        });

        holder.likeLayout.setOnClickListener(v -> {
            notifyLikeClicked(photo);
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public interface OnPhotoClickListener {

        void onPhotoClicked(String imageUrl);
        void onLikeClicked(Photo photo);
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        onPhotoClickListener = listener;
    }

    private void notifyPhotoClicked(String photoUrl) {
        if (onPhotoClickListener != null) {
            onPhotoClickListener.onPhotoClicked(photoUrl);
        }
    }

    private void notifyLikeClicked(Photo photo) {
        if (onPhotoClickListener != null) {
            onPhotoClickListener.onLikeClicked(photo);
        }
    }
}
