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

package com.melkiy.unsplashphotoviewer.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.melkiy.unsplashphotoviewer.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoActivity extends Activity {

    private static final String EXTRA_PHOTO_URL = "EXTRA_PHOTO_URL";

    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView imageView;

    public static void show(Context context, String photoUrl) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(EXTRA_PHOTO_URL, photoUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = (ImageView) findViewById(R.id.photo_image_view);
        String photoUrl = getIntent().getStringExtra(EXTRA_PHOTO_URL);
        if (photoUrl != null) {
            imageLoader.displayImage(photoUrl, imageView);
        }

    }
}
