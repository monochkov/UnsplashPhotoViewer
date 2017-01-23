package com.melkiy.teamvoytest.utils;

import android.content.Context;
import android.content.Intent;

import com.melkiy.teamvoytest.activities.LoginActivity;
import com.melkiy.teamvoytest.activities.MainActivity;
import com.melkiy.teamvoytest.activities.PhotoActivity;
import com.melkiy.teamvoytest.models.User;

public class Intents {

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startMainActivity(Context context, User currentUser) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(User.CURRENT_USER, currentUser);
        context.startActivity(intent);
    }

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startPhotoActivity(Context context, String photoUrl) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra(PhotoActivity.PHOTO_URL, photoUrl);
        context.startActivity(intent);
    }
}
