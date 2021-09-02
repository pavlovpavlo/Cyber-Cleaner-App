package com.cleaner.cybercleanerapp.ui.phone_booster;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.cleaner.cybercleanerapp.util.SingletonClassApp;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import eu.chainfire.libsuperuser.Shell;

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
        onAttachFragment(view_root, this, false);
        return view_root;
    }

    @SuppressLint("SetTextI18n")
    private void setMemory() {
        setProgressBarData();
        setMainText();
        getProcessCount();
    }

    private void setProgressBarData() {
        progressView.setMax(100);
        progressView.setProgress(SingletonClassApp.getInstance().procentMemory);
        progressView2.setMax(100);
        progressView2.setProgress(SingletonClassApp.getInstance().procentMemory);
        bar_circle.setProgressСolor(SingletonClassApp.getInstance().procentMemory, true, getContext());
    }

    private void getProcessCount() {
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
        if(Integer.parseInt(text_r_process.getText().toString()) < apps.size())
        text_r_process.setText("" + apps.size());
        else{
            final int min = 1;
            final int max = 4;
            final int random = new Random().nextInt((max - min) + 1) + min;
            text_r_process.setText("" + (apps.size() - random));
        }
    }

    private void setMainText() {
        text_memory_p.setText(SingletonClassApp.getInstance().UsedMemory + " MB");
        memory_p.setText(SingletonClassApp.getInstance().procentMemory + " %");
        memory_text_p.setText(SingletonClassApp.getInstance().UsedMemory + " MB" + "/" + SingletonClassApp.getInstance().TotalMemory + " GB");
        memory_use.setText(SingletonClassApp.getInstance().UsedMemory + " MB" + "/" + SingletonClassApp.getInstance().TotalMemory + " GB");
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
        checkElement(Util.SHARED_STORAGE);
    }

    @Override
    public void optimization() {

        PhoneTaskCleanerUtil.clearBackgroundTasks(getContext());
    }

    @Override
    public void optimizationClick() {
        ((LinearLayout) text_memory_p.getParent()).setVisibility(View.GONE);
        imageLoadIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void fragmentIsOptimized(SharedData data) {
        SingletonClassApp.getInstance().procentMemory = data.getPercent();
        SingletonClassApp.getInstance().UsedMemory = data.getValue().substring(0, data.getValue().indexOf(" "));
        setMemory();
        //bar_circle.setProgressСolor(SingletonClassApp.getInstance().procentMemory, true);
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(SingletonClassApp.getInstance().procentMemory, true);
    }

    @Override
    public void onOptimizationComplete() {
        setMemory();
        //bar_circle.setProgressСolor(SingletonClassApp.getInstance().procentMemory, true);
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(SingletonClassApp.getInstance().procentMemory, true);

        LocalSharedUtil.setParameter(
                new SharedData(SingletonClassApp.getInstance().procentMemory,
                        SingletonClassApp.getInstance().UsedMemory + " MB", "" + new Date().getTime()),
                Util.SHARED_STORAGE, getContext());
        ((LinearLayout) text_memory_p.getParent()).setVisibility(View.VISIBLE);
        imageLoadIcon.setVisibility(View.GONE);
        NavController controller = NavHostFragment.findNavController(PhoneBoosterFragment.this);
        controller.navigate(R.id.complete_fragment, null);
    }
}

