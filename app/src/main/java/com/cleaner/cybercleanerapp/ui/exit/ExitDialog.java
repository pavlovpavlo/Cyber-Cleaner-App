package com.cleaner.cybercleanerapp.ui.exit;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.cleaner.cybercleanerapp.R;

import java.util.Date;

public class ExitDialog extends DialogFragment {

    public static final String TAG = "info_dialog";
    private static AppCompatActivity activity;
    private View stillOptimized;


    public static ExitDialog display(FragmentManager fragmentManager, AppCompatActivity appCompatActivity) {
        ExitDialog infoDialog = new ExitDialog();
        infoDialog.show(fragmentManager, TAG);
        activity = appCompatActivity;
        return infoDialog;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            dialog.getWindow().setLayout(width, height);
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.6f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialogLight);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_quit, container, false);


        view.findViewById(R.id.dialog_container).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.quite).setOnClickListener(v -> {
            dismiss();
            activity.finish();
        });
        view.findViewById(R.id.close).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.optimize).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.content).setOnClickListener(v -> {
        });


        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
