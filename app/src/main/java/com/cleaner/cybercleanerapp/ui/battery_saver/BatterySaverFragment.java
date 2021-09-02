package com.cleaner.cybercleanerapp.ui.battery_saver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.base.BaseFragment;
import com.cleaner.cybercleanerapp.ui.base.BaseFragmentInterface;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.PhoneTaskCleanerUtil;
import com.cleaner.cybercleanerapp.util.SharedData;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.Date;

import static com.cleaner.cybercleanerapp.util.Util.getBatteryPercentage;

public class BatterySaverFragment extends BaseFragment implements BaseFragmentInterface {
    private View view_root;
    private TextView timeBattery;
    private TextView batteryText;
    private int basicProcent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root = inflater.inflate(R.layout.battery_saver_fragment, container, false);
        onAttachFragment(view_root, this, false);
        return view_root;
    }

    private void setMainText() {
        batteryText.setText(basicProcent + " %");

        bar_circle.setProgressСolor(basicProcent, true, getContext());
        basicProcent += 2;
        if (basicProcent > 100)
            basicProcent = 99;

        int minuteWork = (int) (360 * ((float) basicProcent / 100.0));
        int h = minuteWork / 60;
        int m = minuteWork - h * 60;
        timeBattery.setText(h + " h " + m + " m");

    }

    @Override
    public void basicInit() {
        batteryText = view_root.findViewById(R.id.batteryText);
        timeBattery = view_root.findViewById(R.id.timeBattery);
        basicProcent = getBatteryPercentage(getActivity());
        starAnimBtn();
        setMainText();
        checkElement(Util.SHARED_BATTERY);
    }

    @Override
    public void optimization() {
        PhoneTaskCleanerUtil.clearBackgroundTasks(getContext());
        basicProcent -= 2;
    }

    @Override
    public void optimizationClick() {
        batteryText.setVisibility(View.GONE);
        imageLoadIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void fragmentIsOptimized(SharedData data) {
        basicProcent = getBatteryPercentage(getActivity());
        setMainText();
        //bar_circle.optimizationComplete(basicProcent, true);
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(basicProcent, true);
    }

    @Override
    public void onOptimizationComplete() {
        bar_circle.startAnim(0);
        LocalSharedUtil.setParameter(
                new SharedData(basicProcent,
                        basicProcent + " %", "" + new Date().getTime()),
                Util.SHARED_BATTERY, getContext());

        setMainText();
        bar_circle.optimizationComplete(basicProcent-2, true);

        batteryText.setVisibility(View.VISIBLE);
        imageLoadIcon.setVisibility(View.GONE);
        NavController controller = NavHostFragment.findNavController(BatterySaverFragment.this);
        controller.navigate(R.id.complete_fragment, null);
    }
}