package com.melkiy.teamvoytest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.melkiy.teamvoytest.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoActivity extends Activity {

    public static final String PHOTO_URL = "PHOTO_URL";

    private ImageView imageView;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = (ImageView) findViewById(R.id.photo_image_view);
        String photoUrl = getIntent().getStringExtra(PHOTO_URL);
        if (photoUrl != null) {
            imageLoader.displayImage(photoUrl, imageView);
        }

    }
}
