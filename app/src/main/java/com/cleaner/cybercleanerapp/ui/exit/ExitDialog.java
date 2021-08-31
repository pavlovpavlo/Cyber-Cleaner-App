package com.cleaner.cybercleanerapp.ui.exit;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.SharedData;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.Date;

public class ExitDialog extends DialogFragment {

    public static final String TAG = "info_dialog";
    private static AppCompatActivity activity;
    private static OnExitDialogListener listener;
    private int optimizationTab;
    private String optimizationText;
    private TextView textOptim;


    public static ExitDialog display(FragmentManager fragmentManager, AppCompatActivity appCompatActivity, OnExitDialogListener onExitDialogListener) {
        ExitDialog infoDialog = new ExitDialog();
        infoDialog.show(fragmentManager, TAG);
        activity = appCompatActivity;
        listener = onExitDialogListener;
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

        checkData();
        textOptim = view.findViewById(R.id.textOptim);
        textOptim.setText(optimizationText);
        view.findViewById(R.id.dialog_container).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.quite).setOnClickListener(v -> {
            dismiss();
            listener.onQuitClick();
        });
        view.findViewById(R.id.close).setOnClickListener(v -> {
            dismiss();
        });
        view.findViewById(R.id.optimize).setOnClickListener(v -> {
            listener.onOptimizeClick(optimizationTab);
            dismiss();
        });
        view.findViewById(R.id.phone_booster).setOnClickListener(v -> {
            listener.onOptimizeClick(optimizationTab);
            dismiss();
        });
        view.findViewById(R.id.content).setOnClickListener(v -> {
        });

        return view;
    }

    public void checkData() {
        if(!checkElement(Util.SHARED_STORAGE)){
            optimizationTab = R.id.tab_storage;
            optimizationText = "Phone booster";
        }else{
            if(!checkElement(Util.SHARED_BATTERY)){
                optimizationTab = R.id.tab_battery;
                optimizationText = "Battery Saver";
            }else{
                if(!checkElement(Util.SHARED_CPU)){
                    optimizationTab = R.id.tab_cpu;
                    optimizationText = "CPU Cooler";
                }else{
                    if(!checkElement(Util.SHARED_JUNK)){
                        optimizationTab = R.id.tab_junk;
                        optimizationText = "Junk Cleaner";
                    }
                }
            }
        }
    }

    private boolean checkElement(String sharedKey) {
        SharedData data = LocalSharedUtil.getParameter(sharedKey, activity);

        return (Long.parseLong(data.getDate()) + 7_200_000) > new Date().getTime();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
