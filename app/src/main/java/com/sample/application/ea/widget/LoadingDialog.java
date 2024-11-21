package com.sample.application.ea.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sample.application.ea.R;

import java.util.Objects;

/**
 * Dialog fragment showing a loading indicator.
 */
public final class LoadingDialog extends DialogFragment {

    // Inflates the loading dialog view.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_loading, container, false);
    }

    // Creates the dialog with specific properties.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        Window win = Objects.requireNonNull(dialog.getWindow());
        win.setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

}

