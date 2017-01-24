package com.melkiy.teamvoytest.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public final class ImageLoaderDisplayOptions {

    public static final DisplayImageOptions DEFAULT;
    public static final DisplayImageOptions DEFAULT_ROUNDED;

    static {
        DEFAULT = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.drawable.sym_def_app_icon)
                .showImageOnFail(android.R.drawable.sym_def_app_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        DEFAULT_ROUNDED = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(1000))
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(android.R.drawable.sym_def_app_icon)
                .showImageOnFail(android.R.drawable.sym_def_app_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
}
