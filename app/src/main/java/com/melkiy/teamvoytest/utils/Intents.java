package com.melkiy.teamvoytest.utils;

import android.content.Context;
import android.content.Intent;

import com.melkiy.teamvoytest.activities.LoginActivity;
import com.melkiy.teamvoytest.activities.MainActivity;

public class Intents {

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}