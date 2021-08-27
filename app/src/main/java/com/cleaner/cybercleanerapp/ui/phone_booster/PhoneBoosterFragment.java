package com.cleaner.cybercleanerapp.ui.phone_booster;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.base.BaseFragment;
import com.cleaner.cybercleanerapp.ui.base.BaseFragmentInterface;
import com.cleaner.cybercleanerapp.util.MemStat;
import com.cleaner.cybercleanerapp.util.SingletonClassApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

import static android.content.Context.ACTIVITY_SERVICE;

public class PhoneBoosterFragment extends BaseFragment implements BaseFragmentInterface {
    private ProgressBar progressView;
    private ProgressBar progressView2;
    private View view_root;
    private TextView memory_p;
    private TextView memory_use;
    private TextView text_memory_p;
    private TextView memory_text_p;
    private TextView text_r_process;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root = inflater.inflate(R.layout.phone_booster_fragment, container, false);
        onAttachFragment(view_root, this);
        return view_root;
    }

    @SuppressLint("SetTextI18n")
    private void setMemory() {
        setProgressBarData();
        setMainText();
        getProcessCount();
    }

    private void setProgressBarData(){
        progressView.setMax(100);
        progressView.setProgress(SingletonClassApp.getInstance().procentMemory);
        progressView2.setMax(100);
        progressView2.setProgress(SingletonClassApp.getInstance().procentMemory);
        bar_circle.setProgressСolor(SingletonClassApp.getInstance().procentMemory, true,getContext());
    }

    private void getProcessCount(){
        PackageManager pm = getContext().getPackageManager();
        List<String> stdout = Shell.SH.run("ps");
        List<String> packages = new ArrayList<>();

        for (String line : stdout) {
            // Get the process-name. It is the last column.
            String[] arr = line.split("\\s+");
            String processName = arr[arr.length - 1].split(":")[0];
            packages.add(processName);
        }
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);
        Log.i("APPS", apps.size() + "");
        text_r_process.setText("" + apps.size());
    }

    private void setMainText(){
        text_memory_p.setText(SingletonClassApp.getInstance().UsedMemory + " GB");
        memory_p.setText(SingletonClassApp.getInstance().procentMemory + " %");
        memory_text_p.setText(SingletonClassApp.getInstance().UsedMemory + " GB" + "/" + SingletonClassApp.getInstance().TotalMemory + " GB");
        memory_use.setText(SingletonClassApp.getInstance().UsedMemory + " GB" + "/" + SingletonClassApp.getInstance().TotalMemory + " GB");
    }

    @Override
    public void basicInit() {
        progressView = view_root.findViewById(R.id.progress);
        progressView2 = view_root.findViewById(R.id.progress2);
        memory_p = view_root.findViewById(R.id.memory_p);
        memory_use = view_root.findViewById(R.id.memory_use);
        text_memory_p = view_root.findViewById(R.id.text_memory_p);
        text_r_process = view_root.findViewById(R.id.text_r_process);
        memory_text_p = view_root.findViewById(R.id.memory_text_p);
        starAnimBtn();
        setMemory();
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
            final MemStat memStat = new MemStat(getContext());
            SingletonClassApp.getInstance().UsedMemory = String.valueOf(memStat.getUsedMemory());
            SingletonClassApp.getInstance().TotalMemory = String.valueOf(memStat.getTotalMemory());
            SingletonClassApp.getInstance().procentMemory = 100 - memStat.getProcentMemory();
        }
    }

    @Override
    public void onOptimizationComplete() {
        setMemory();
        //bar_circle.setProgressСolor(SingletonClassApp.getInstance().procentMemory, true);
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(SingletonClassApp.getInstance().procentMemory, true);
    }
}

