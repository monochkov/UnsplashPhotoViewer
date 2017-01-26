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

package com.melkiy.unsplashphotoviewer.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.melkiy.unsplashphotoviewer.R;
import com.melkiy.unsplashphotoviewer.models.Order;

public class SortDialogFragment extends DialogFragment {

    public interface OnOrderListItemClickListener {
        void onOrderValueClick(Order order);
    }

    public static final String TAG = SortDialogFragment.class.getName();

    private static final int POSITION_ORDER_BY_LATEST = 0;
    private static final int POSITION_ORDER_BY_OLDEST = 1;
    private static final int POSITION_ORDER_BY_POPULAR = 2;

    private OnOrderListItemClickListener clickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.order_by)
                .setItems(R.array.order_by, (dialog, which) -> {
                    switch (which) {
                        case POSITION_ORDER_BY_LATEST:
                            notifyCallClicked(Order.LATEST);
                            break;
                        case POSITION_ORDER_BY_OLDEST:
                            notifyCallClicked(Order.OLDEST);
                            break;
                        case POSITION_ORDER_BY_POPULAR:
                            notifyCallClicked(Order.POPULAR);
                            break;
                    }
                });
        return builder.create();
    }

    public void setOnOrderListItemClickListener(OnOrderListItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void notifyCallClicked(Order order) {
        if (clickListener != null) {
            clickListener.onOrderValueClick(order);
        }
    }
}
