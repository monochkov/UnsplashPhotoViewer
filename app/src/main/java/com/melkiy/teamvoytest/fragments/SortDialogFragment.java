package com.melkiy.teamvoytest.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.melkiy.teamvoytest.R;

public class SortDialogFragment extends DialogFragment {

    public static final String TAG = SortDialogFragment.class.getName();
    public static final String ORDER_BY_LATEST = "latest";
    public static final String ORDER_BY_OLDEST = "oldest";
    public static final String ORDER_BY_POPULAR = "popular";

    private OnOrderListItemClickListener clickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.order_by)
                .setItems(R.array.order_by, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            notifyCallClicked(ORDER_BY_LATEST);
                            break;
                        case 1:
                            notifyCallClicked(ORDER_BY_OLDEST);
                            break;
                        case 2:
                            notifyCallClicked(ORDER_BY_POPULAR);
                            break;
                    }
                });
        return builder.create();
    }

    public void setOnOrderListItemClickListener(OnOrderListItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnOrderListItemClickListener {
        void onOrderValueClick(String orderBy);
    }

    private void notifyCallClicked(String orderBy) {
        if (clickListener != null) {
            clickListener.onOrderValueClick(orderBy);
        }
    }
}
