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

package com.melkiy.unsplashphotoviewer.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melkiy.unsplashphotoviewer.R;

public class PhotosListViewHolder extends RecyclerView.ViewHolder {

    public final ImageView userProfilePhoto;
    public final TextView username;
    public final TextView datePublication;
    public final ImageView photo;
    public final ImageView like;
    public final TextView countLikes;
    public final LinearLayout likeLayout;

    public PhotosListViewHolder(View itemView) {
        super(itemView);

        userProfilePhoto = (ImageView) itemView.findViewById(R.id.user_profile_photo);
        username = (TextView) itemView.findViewById(R.id.username);
        datePublication = (TextView) itemView.findViewById(R.id.date_publication);
        photo = (ImageView) itemView.findViewById(R.id.photo_image_view);
        like = (ImageView) itemView.findViewById(R.id.like_image_view);
        countLikes = (TextView) itemView.findViewById(R.id.like_counter_textview);
        likeLayout = (LinearLayout) itemView.findViewById(R.id.like_layout);
    }
}
