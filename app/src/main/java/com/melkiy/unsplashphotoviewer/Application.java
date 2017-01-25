package com.melkiy.unsplashphotoviewer;

import com.melkiy.unsplashphotoviewer.utils.ImageLoaderDisplayOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(ImageLoaderDisplayOptions.DEFAULT)
                .build();

        ImageLoader.getInstance().init(config);
    }
}
