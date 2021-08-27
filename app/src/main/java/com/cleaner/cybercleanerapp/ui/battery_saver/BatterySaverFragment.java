package com.cleaner.cybercleanerapp.ui.battery_saver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.base.BaseFragment;
import com.cleaner.cybercleanerapp.ui.base.BaseFragmentInterface;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.PhoneTaskCleanerUtil;
import com.cleaner.cybercleanerapp.util.SharedData;
import com.cleaner.cybercleanerapp.util.SingletonClassApp;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.Date;

import static android.content.Context.BATTERY_SERVICE;
import static com.cleaner.cybercleanerapp.util.Util.getBatteryPercentage;

public class BatterySaverFragment extends BaseFragment implements BaseFragmentInterface {
    private View view_root;
    private TextView timeBattery;
    private int basicProcent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root = inflater.inflate(R.layout.battery_saver_fragment, container, false);
        onAttachFragment(view_root, this);
        return view_root;
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        setMainText();
    }

    private void setMainText() {
        int minuteWork = (int) (360 * ((float) basicProcent / 100.0));
        int h = minuteWork / 60;
        int m = minuteWork - h * 60;
        timeBattery.setText(h + " h " + m + " m");

        bar_circle.setProgressСolor(basicProcent, true,getContext());
    }


    @Override
    public void basicInit() {
        timeBattery = view_root.findViewById(R.id.timeBattery);
        basicProcent = getBatteryPercentage(getActivity());
        starAnimBtn();
        setData();
    }

    @Override
    public void optimization() {
        PhoneTaskCleanerUtil.clearBackgroundTasks(getContext());
    }

    @Override
    public void onOptimizationComplete() {
        basicProcent += 2;
        setData();
        //bar_circle.optimizationComplete(basicProcent, true);
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(basicProcent, true);

        LocalSharedUtil.setParameter(
                new SharedData(basicProcent,
                        basicProcent + " %", ""+new Date().getTime()),
                Util.SHARED_BATTERY, getContext());
    }
}