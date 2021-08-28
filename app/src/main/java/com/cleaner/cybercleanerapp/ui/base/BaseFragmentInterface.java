package com.cleaner.cybercleanerapp.ui.base;

import com.cleaner.cybercleanerapp.util.SharedData;

public interface BaseFragmentInterface {
    void basicInit();
    void optimization();
    void optimizationClick();
    void fragmentIsOptimized(SharedData data);
    void onOptimizationComplete();
}
