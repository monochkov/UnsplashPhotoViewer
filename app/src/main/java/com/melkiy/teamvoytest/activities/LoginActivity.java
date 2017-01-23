package com.melkiy.teamvoytest.activities;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.melkiy.teamvoytest.BuildConfig;
import com.melkiy.teamvoytest.R;
import com.melkiy.teamvoytest.models.AccessToken;
import com.melkiy.teamvoytest.models.User;
import com.melkiy.teamvoytest.rest.API;
import com.melkiy.teamvoytest.rest.AuthorizationService;
import com.melkiy.teamvoytest.utils.Intents;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String AUTHORIZE_URL = "https://unsplash.com/oauth/authorize";
    public static final String TOKEN_URL = "https://unsplash.com/oauth/token";
    public static final String RESPONSE_TYPE = "code";
    public static final String SCOPE = "public+read_user+write_likes";
    public static final String SITE = "teamvoy-test.com";
    public static final String GRANT_TYPE = "authorization_code";

    private API api = API.getInstance();
    private WebView webView;
    private AuthorizationService authorizationService;

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
                if (url.startsWith(BuildConfig.REDIRECT_URI)) {
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
                "&redirect_uri=" + BuildConfig.REDIRECT_URI +
                "&response_type=" + RESPONSE_TYPE +
                "&scope=" + SCOPE);
    }

    private void authorize(String code) {
        authorizationService.authorize(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, BuildConfig.REDIRECT_URI, code, GRANT_TYPE)
                .enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                AccessToken accessToken = response.body();
                                api.setAccessToken(accessToken);
                                loadCurrentUser();
                            }
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
                if (response != null) {
                    if (response.isSuccessful()) {
                        User currentUser = response.body();
                        api.setCurrentUser(currentUser);
                        Intents.startMainActivity(LoginActivity.this, currentUser);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
