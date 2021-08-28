package com.cleaner.cybercleanerapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.exit.ExitDialog;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.SharedData;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private View lastActiveView;
    public boolean isOptimizationActive = false;
    private NavController navController;
    private TextView toolbarText;
    private ImageView boosterItem;
    private ImageView batteryItem;
    private ImageView cpuItem;
    private ImageView junkItem;
    private LinearLayout toolbar;
    private ConstraintLayout bottomNav;

    @Override
    public void onBackPressed() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        int prevFragId = navController.getCurrentBackStackEntry().getDestination().getId();
        if (prevFragId == R.id.complete_fragment)
            super.onBackPressed();
        else
            ExitDialog.display(getSupportFragmentManager(), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    public void isHideToolbar(boolean isHide){
        if(isHide){
            toolbar.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.VISIBLE);
        }else{
            toolbar.setVisibility(View.GONE);
            bottomNav.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        bottomNav = findViewById(R.id.bottomNav);
        toolbarText = findViewById(R.id.toolbar_text);
        boosterItem = findViewById(R.id.storage_icon);
        batteryItem = findViewById(R.id.battery_icon);
        cpuItem = findViewById(R.id.cpu_icon);
        junkItem = findViewById(R.id.junk_icon);
        lastActiveView = findViewById(R.id.tab_storage);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        setActiveTab(lastActiveView);
        checkData();
    }

    public void onTabClick(View view) {
        if (!isOptimizationActive) {
            disableLastTab();
            lastActiveView = view;
            setActiveTab(view);

            NavOptions.Builder navBuilder = new NavOptions.Builder();

            switch (view.getId()) {
                case R.id.tab_storage:

                    NavOptions navOptions = navBuilder.setPopUpTo(R.id.phone_booster_fragment, true).build();
                    navController.navigate(R.id.phone_booster_fragment, null, navOptions);
                    break;
                case R.id.tab_battery:
                    NavOptions navOptions1 = navBuilder.setPopUpTo(R.id.battery_saver_fragment, true).build();
                    navController.navigate(R.id.battery_saver_fragment, null, navOptions1);
                    break;
                case R.id.tab_cpu:
                    NavOptions navOptions2 = navBuilder.setPopUpTo(R.id.cpu_cooler_fragment, true).build();
                    navController.navigate(R.id.cpu_cooler_fragment, null, navOptions2);
                    break;
                case R.id.tab_junk:
                    NavOptions navOptions3 = navBuilder.setPopUpTo(R.id.junk_cleaner_fragment, true).build();
                    navController.navigate(R.id.junk_cleaner_fragment, null, navOptions3);
                    break;
            }
        }
    }

    public void checkData() {
        checkElement(boosterItem, Util.SHARED_STORAGE);
        checkElement(batteryItem, Util.SHARED_BATTERY);
        checkElement(cpuItem, Util.SHARED_CPU);
        checkElement(junkItem, Util.SHARED_JUNK);
    }

    private void checkElement(ImageView image, String sharedKey) {
        SharedData data = LocalSharedUtil.getParameter(sharedKey, this);

        boolean isOptimized = (Long.parseLong(data.getDate()) + 7_200_000) > new Date().getTime();
        if (isOptimized) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
        }
    }

    private void setActiveTab(View activeTab) {
        ImageView imageView = (ImageView) (
                (ViewGroup)
                        ((ViewGroup) activeTab).getChildAt(0))
                .getChildAt(0);

        switch (activeTab.getId()) {
            case R.id.tab_storage:
                toolbarText.setText("Phone Booster");
                imageView.setImageResource(R.drawable.ic_tab_storage_active);
                break;
            case R.id.tab_battery:
                toolbarText.setText("Battery Saver");
                imageView.setImageResource(R.drawable.ic_tab_battery_active);
                break;
            case R.id.tab_cpu:
                toolbarText.setText("CPU Cooler");
                imageView.setImageResource(R.drawable.ic_tab_cpu_active);
                break;
            case R.id.tab_junk:
                toolbarText.setText("Junk Cleaner");
                imageView.setImageResource(R.drawable.ic_tab_junk_active);
                break;
        }
    }

    private void disableLastTab() {
        ImageView imageView = (ImageView) (
                (ViewGroup)
                        ((ViewGroup) lastActiveView).getChildAt(0))
                .getChildAt(0);

        switch (lastActiveView.getId()) {
            case R.id.tab_storage:
                imageView.setImageResource(R.drawable.ic_tab_storage);
                break;
            case R.id.tab_battery:
                imageView.setImageResource(R.drawable.ic_tab_battery);
                break;
            case R.id.tab_cpu:
                imageView.setImageResource(R.drawable.ic_tab_cpu);
                break;
            case R.id.tab_junk:
                imageView.setImageResource(R.drawable.ic_tab_junk);
                break;
        }
    }
}