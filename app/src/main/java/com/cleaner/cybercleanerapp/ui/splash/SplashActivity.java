package com.cleaner.cybercleanerapp.ui.splash;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.MainActivity;
import com.cleaner.cybercleanerapp.ui.base.ProgressBarAnimation;
import com.cleaner.cybercleanerapp.util.DiskStat;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.MemStat;
import com.cleaner.cybercleanerapp.util.SharedData;
import com.cleaner.cybercleanerapp.util.SingletonClassApp;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.Date;

import static com.cleaner.cybercleanerapp.util.Util.MAX_CPU_TEMP;
import static com.cleaner.cybercleanerapp.util.Util.cpuTemperature;
import static com.cleaner.cybercleanerapp.util.Util.getBatteryPercentage;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
    }

    private void initViews() {
        getMemory();
        ProgressBar progressView = findViewById(R.id.progress);
        ProgressBarAnimation anim = new ProgressBarAnimation(progressView, 0, 100);
        anim.setDuration(7000);
        progressView.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void getMemory() {
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final DiskStat diskStat = new DiskStat();
                final MemStat memStat = new MemStat(getApplicationContext());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("mamory", memStat.getUsedMemory() + "/" + memStat.getTotalMemory());
                        SingletonClassApp.getInstance().UsedMemory = String.valueOf(memStat.getUsedMemory());
                        SingletonClassApp.getInstance().TotalMemory = String.valueOf(memStat.getTotalMemory());
                        SingletonClassApp.getInstance().procentMemory= 100 - memStat.getProcentMemory();
                        SingletonClassApp.getInstance().UsedMemoryInt=memStat.getUsedMemoryLong();
                        SingletonClassApp.getInstance().TotalMemoryInt=memStat.getTotalMemoryLong();

                        baseInitOptimizationData();
                    }
                });
            }
        });

        thread.start();
    }

    private void baseInitOptimizationData(){
        LocalSharedUtil.setParameter(
                new SharedData(SingletonClassApp.getInstance().procentMemory,
                        SingletonClassApp.getInstance().UsedMemory + " MB", ""+new Date().getTime()),
                Util.SHARED_STORAGE, getApplicationContext());

        LocalSharedUtil.setParameter(
                new SharedData(getBatteryPercentage(getApplicationContext()),
                        getBatteryPercentage(getApplicationContext()) + " %", ""+new Date().getTime()),
                Util.SHARED_BATTERY, getApplicationContext());

        LocalSharedUtil.setParameter(
                new SharedData(SingletonClassApp.getInstance().procentMemory,
                        SingletonClassApp.getInstance().UsedMemory + " MB" + "/" + SingletonClassApp.getInstance().TotalMemory + " GB", ""+new Date().getTime()),
                Util.SHARED_CPU, getApplicationContext());

        float cpuTemp = cpuTemperature();
        int basicProcent = (int)((cpuTemp/MAX_CPU_TEMP) * 100);
        LocalSharedUtil.setParameter(
                new SharedData(basicProcent,
                        cpuTemp + "°C", ""+new Date().getTime()),
                Util.SHARED_CPU, getApplicationContext());
    }

}