package com.melkiy.teamvoytest.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.melkiy.teamvoytest.R;
import com.melkiy.teamvoytest.fragments.ListFragment;
import com.melkiy.teamvoytest.fragments.RandomPhotoFragment;
import com.melkiy.teamvoytest.models.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EventBus eventBus = EventBus.getDefault();
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Menu menu;

    private ImageView profileImageView;
    private TextView nameTextView;
    private TextView usernameTextView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Photos");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        profileImageView = (ImageView) headerLayout.findViewById(R.id.profile_photo_image_view);
        nameTextView = (TextView) headerLayout.findViewById(R.id.name_text_view);
        usernameTextView = (TextView) headerLayout.findViewById(R.id.username_text_view);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ListFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                return false;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_list:
                toolbar.setTitle("Photos");
                menu.findItem(R.id.action_sort).setVisible(true);
                openFragment(new ListFragment());
                break;
            case R.id.nav_random_photo:
                toolbar.setTitle("Random photo");
                menu.findItem(R.id.action_sort).setVisible(false);
                openFragment(new RandomPhotoFragment());
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        eventBus.register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateNavigationDrawer(User currentUser) {
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000))
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.drawable.sym_def_app_icon)
                .showImageOnFail(android.R.drawable.sym_def_app_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader.displayImage(currentUser.getProfileImage().getMedium(), profileImageView, options);
        nameTextView.setText(currentUser.getName());
        usernameTextView.setText(currentUser.getUsername());
    }
}
