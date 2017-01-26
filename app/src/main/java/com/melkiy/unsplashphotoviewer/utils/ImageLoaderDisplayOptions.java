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

package com.melkiy.unsplashphotoviewer.utils;

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
