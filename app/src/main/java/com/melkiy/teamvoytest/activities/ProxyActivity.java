package com.melkiy.teamvoytest.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.melkiy.teamvoytest.rest.API;
import com.melkiy.teamvoytest.utils.Intents;

public class ProxyActivity extends AppCompatActivity {

    private API api = API.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api.initialize(this);
        if (api.isAuthenticated()) {
            Intents.startMainActivity(this);
        } else {
            Intents.startLoginActivity(this);
        }
        finish();
    }
}
