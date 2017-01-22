package com.melkiy.teamvoytest.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.melkiy.teamvoytest.BuildConfig;
import com.melkiy.teamvoytest.R;
import com.melkiy.teamvoytest.models.AccessToken;
import com.melkiy.teamvoytest.models.User;
import com.melkiy.teamvoytest.rest.API;
import com.melkiy.teamvoytest.rest.AuthorizationService;
import com.melkiy.teamvoytest.utils.Intents;

import org.greenrobot.eventbus.EventBus;

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

    private EventBus eventBus = EventBus.getDefault();
    private API api = API.getInstance();
    private WebView webView;
    private AuthorizationService authorizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authorizationService = API.getInstance().getAuthorizationService();

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {
            //API lower than 21
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(BuildConfig.REDIRECT_URI)) {
                    String code = url.substring(url.indexOf('=') + 1);
                    authorize(code);
                }
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl().getHost().equals(SITE)) {
                    String code = request.getUrl().getQueryParameter(RESPONSE_TYPE);
                    authorize(code);
                }
                return true;
            }
        });
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
                        AccessToken accessToken = response.body();
                        api.setAccessToken(accessToken);
                        loadCurrentUser();
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
                User currentUser = response.body();
                api.setCurrentUser(currentUser);
                eventBus.post(currentUser);
                Intents.startMainActivity(LoginActivity.this);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
