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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.melkiy.unsplashphotoviewer.R;
import com.melkiy.unsplashphotoviewer.fragments.PhotosFragment;
import com.melkiy.unsplashphotoviewer.fragments.RandomPhotoFragment;
import com.melkiy.unsplashphotoviewer.models.User;
import com.melkiy.unsplashphotoviewer.rest.API;
import com.melkiy.unsplashphotoviewer.utils.ImageLoaderDisplayOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String EXTRA_CURRENT_USER = "EXTRA_CURRENT_USER";

    private final ImageLoader imageLoader = ImageLoader.getInstance();
    private User currentUser;

    private DrawerLayout drawer;
    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView usernameTextView;

    private Toolbar toolbar;

    public static void show(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void show(Context context, User currentUser) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER, currentUser);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.photos);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        profileImageView = (ImageView) headerLayout.findViewById(R.id.profile_photo_image_view);
        nameTextView = (TextView) headerLayout.findViewById(R.id.name_text_view);
        usernameTextView = (TextView) headerLayout.findViewById(R.id.username_text_view);

        if (getIntent() != null) {
            currentUser = (User) getIntent().getSerializableExtra(EXTRA_CURRENT_USER);
        } else {
            currentUser = API.getInstance().getCurrentUser();
        }
        if (currentUser != null) {
            imageLoader.displayImage(
                    currentUser.getProfileImage().getMedium(),
                    profileImageView,
                    ImageLoaderDisplayOptions.DEFAULT_ROUNDED);

            nameTextView.setText(currentUser.getName());
            usernameTextView.setText(currentUser.getUsername());
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, PhotosFragment.newInstance())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_list: {
                toolbar.setTitle(R.string.photos);
                openFragment(PhotosFragment.newInstance());
                break;
            }

            case R.id.nav_random_photo: {
                toolbar.setTitle(R.string.random_photo);
                openFragment(RandomPhotoFragment.newInstance());
                break;
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
