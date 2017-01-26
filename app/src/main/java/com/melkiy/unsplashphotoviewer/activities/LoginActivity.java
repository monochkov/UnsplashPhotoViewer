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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.melkiy.unsplashphotoviewer.BuildConfig;
import com.melkiy.unsplashphotoviewer.Configuration;
import com.melkiy.unsplashphotoviewer.R;
import com.melkiy.unsplashphotoviewer.models.AccessToken;
import com.melkiy.unsplashphotoviewer.models.User;
import com.melkiy.unsplashphotoviewer.rest.API;
import com.melkiy.unsplashphotoviewer.rest.AuthorizationService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String AUTHORIZE_URL = "https://unsplash.com/oauth/authorize";
    public static final String TOKEN_URL = "https://unsplash.com/oauth/token";
    public static final String RESPONSE_TYPE = "code";
    public static final String SCOPE = "public+read_user+write_likes";
    //public static final String SITE = "teamvoy-test.com";
    public static final String GRANT_TYPE = "authorization_code";

    private API api = API.getInstance();
    private WebView webView;
    private AuthorizationService authorizationService;

    public static void show(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authorizationService = API.getInstance().getAuthorizationService();

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(Configuration.REDIRECT_URI)) {
                    String code = url.substring(url.indexOf('=') + 1);
                    authorize(code);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        loadAuthorizeUrl();
    }

    private void loadAuthorizeUrl() {
        webView.loadUrl(AUTHORIZE_URL +
                "?client_id=" + BuildConfig.CLIENT_ID +
                "&redirect_uri=" + Configuration.REDIRECT_URI +
                "&response_type=" + RESPONSE_TYPE +
                "&scope=" + SCOPE);
    }

    private void authorize(String code) {
        authorizationService.authorize(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, Configuration.REDIRECT_URI, code, GRANT_TYPE)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if (response.isSuccessful()) {
                            AccessToken accessToken = response.body();
                            api.setAccessToken(accessToken);
                            loadCurrentUser();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void loadCurrentUser() {
        api.getUserService().getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User currentUser = response.body();
                    api.setCurrentUser(currentUser);
                    MainActivity.show(LoginActivity.this, currentUser);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
