package com.cleaner.cybercleanerapp.ui.cpu_cooler;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.base.BaseFragment;
import com.cleaner.cybercleanerapp.ui.base.BaseFragmentInterface;
import com.cleaner.cybercleanerapp.util.MemStat;
import com.cleaner.cybercleanerapp.util.SingletonClassApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cn.septenary.ui.widget.GradientProgressBar;
import eu.chainfire.libsuperuser.Shell;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.BATTERY_SERVICE;

public class CPUCoolerFragment extends BaseFragment implements BaseFragmentInterface {
    private View view_root;
    private TextView cpuTempText;
    private int basicProcent;
    private final float MAX_CPU_TEMP = 42.0f;
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

    public float cpuTemperature() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if(line!=null) {
                float temp = Float.parseFloat(line);
                return temp / 1000.0f;
            }else{
                return 51.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
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
        PackageManager pm = getContext().getPackageManager();
        List<String> stdout = Shell.SH.run("ps");
        List<String> packages = new ArrayList<>();
        for (String line : stdout) {
            String[] arr = line.split("\\s+");
            String processName = arr[arr.length - 1].split(":")[0];
            packages.add(processName);
        }
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);

        for (Iterator<ApplicationInfo> it = apps.iterator(); it.hasNext(); ) {
            if (!packages.contains(it.next().packageName)) {
                it.remove();
            }
        }
        List<String> packages_all = new ArrayList<>();
        for (ApplicationInfo app : apps) {
            packages_all.add(app.processName);
        }
        ActivityManager am = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
        for (String name : packages_all
        ) {
            try {
                if (!name.equals("com.cleaner.cybercleanerapp")) {
                    am.killBackgroundProcesses(name);
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onOptimizationComplete() {
        cpuTemp -= new Random().nextInt(3);
        basicProcent = (int)((cpuTemp/MAX_CPU_TEMP) * 100);
        setData();
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(basicProcent, true);
    }
}