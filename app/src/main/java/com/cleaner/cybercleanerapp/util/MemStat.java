package com.cleaner.cybercleanerapp.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

/**
 * Created by mazhuang on 2016/12/8.
 */

public class MemStat {
    private long mTotalMemory;
    private long mUsedMemory;
   // private long mUsedMemory;
    private static final double B_TO_GB = 1024d * 1024d * 1024d;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public MemStat(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memInfo);

        mTotalMemory = memInfo.totalMem;
        mUsedMemory = (memInfo.totalMem - memInfo.availMem);
    }

    public static float getAvailableMemory(ActivityManager.MemoryInfo memoryInfo) {
        return ((int) (memoryInfo.availMem / B_TO_GB * 100)) / 100.0f;
    }

    public static float getTotalMemory(ActivityManager.MemoryInfo memoryInfo) {
        return ((int) (memoryInfo.totalMem / B_TO_GB * 100)) / 100.0f;
    }

    public float getTotalMemory() {
        return ((int) (mTotalMemory / B_TO_GB * 100)) / 100.0f;
    }

    public int getProcentMemory() {
        return ((int) (int)(mUsedMemory * 100 / mTotalMemory));
    }


    public float getUsedMemory() {
        return ((int) (mUsedMemory / B_TO_GB * 100)) / 100.0f;
    }
}
