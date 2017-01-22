package com.melkiy.teamvoytest.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.melkiy.teamvoytest.R;

public class PhotosListViewHolder extends RecyclerView.ViewHolder {

    public final ImageView userProfilePhoto;
    public final TextView username;
    public final TextView datePublication;
    public final ImageView photo;
    public final ImageView like;
    public final TextView countLikes;

    public PhotosListViewHolder(View itemView) {
        super(itemView);

        userProfilePhoto = (ImageView) itemView.findViewById(R.id.user_profile_photo);
        username = (TextView) itemView.findViewById(R.id.username);
        datePublication = (TextView) itemView.findViewById(R.id.date_publication);
        photo = (ImageView) itemView.findViewById(R.id.photo_image_view);
        like = (ImageView) itemView.findViewById(R.id.like_image_view);
        countLikes = (TextView) itemView.findViewById(R.id.like_counter_textview);
    }
}
