package com.melkiy.unsplashphotoviewer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melkiy.unsplashphotoviewer.R;
import com.melkiy.unsplashphotoviewer.models.Photo;
import com.melkiy.unsplashphotoviewer.utils.ImageLoaderDisplayOptions;
import com.melkiy.unsplashphotoviewer.viewholders.PhotosListViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class PhotosRecyclerAdapter extends RecyclerView.Adapter<PhotosListViewHolder> {

    public interface OnPhotoClickListener {

        void onPhotoClicked(String imageUrl);
        void onLikeClicked(Photo photo);
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");

    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private final Context context;
    private List<Photo> photos = new ArrayList<>();

    private OnPhotoClickListener onPhotoClickListener;

    public PhotosRecyclerAdapter(Context context) {
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
        imageLoader.displayImage(
                photo.getUser().getProfileImage().getMedium(),
                holder.userProfilePhoto,
                ImageLoaderDisplayOptions.DEFAULT_ROUNDED);
        holder.username.setText(photo.getUser().getName());

        holder.datePublication.setText(DATE_FORMATTER.print(photo.getCreatedAt()));

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
