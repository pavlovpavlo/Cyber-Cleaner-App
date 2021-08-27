package com.cleaner.cybercleanerapp.ui.cpu_cooler;

import android.annotation.SuppressLint;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;

import static com.cleaner.cybercleanerapp.util.Util.MAX_CPU_TEMP;
import static com.cleaner.cybercleanerapp.util.Util.cpuTemperature;

public class CPUCoolerFragment extends BaseFragment implements BaseFragmentInterface {
    private View view_root;
    private TextView cpuTempText;
    private int basicProcent;
    private float cpuTemp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root = inflater.inflate(R.layout.cpu_cooler_fragment, container, false);
        onAttachFragment(view_root, this);
        return view_root;
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        setMainText();
    }

    private void setMainText() {
        cpuTempText.setText(cpuTemp + "°C");

        bar_circle.setProgressСolor(basicProcent, true,getContext());
    }


    @Override
    public void basicInit() {
        cpuTempText = view_root.findViewById(R.id.cpuTempText);
        cpuTemp = cpuTemperature();
        basicProcent = (int)((cpuTemp/MAX_CPU_TEMP) * 100);
        starAnimBtn();
        setData();
    }

    @Override
    public void optimization() {
        PhoneTaskCleanerUtil.clearBackgroundTasks(getContext());
    }

    @Override
    public void onOptimizationComplete() {
        cpuTemp -= new Random().nextInt(3);
        basicProcent = (int)((cpuTemp/MAX_CPU_TEMP) * 100);
        setData();
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(basicProcent, true);

        LocalSharedUtil.setParameter(
                new SharedData(basicProcent,
                        cpuTemp + "°C", ""+new Date().getTime()),
                Util.SHARED_CPU, getContext());
    }
}