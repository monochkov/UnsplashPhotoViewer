package com.melkiy.teamvoytest.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.melkiy.teamvoytest.R;
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
