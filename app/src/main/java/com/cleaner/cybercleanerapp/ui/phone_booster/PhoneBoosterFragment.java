package com.cleaner.cybercleanerapp.ui.phone_booster;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cleaner.cybercleanerapp.MyView.CircleView;
import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.MainActivity;
import com.cleaner.cybercleanerapp.ui.cpu_cooler.CPUCoolerViewModel;
import com.cleaner.cybercleanerapp.util.DiskStat;
import com.cleaner.cybercleanerapp.util.MemStat;
import com.cleaner.cybercleanerapp.util.SingletonClassApp;
import com.progress.progressview.ProgressView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import cn.septenary.ui.widget.GradientProgressBar;
import eu.chainfire.libsuperuser.Shell;

import static android.content.Context.ACTIVITY_SERVICE;

public class PhoneBoosterFragment extends Fragment {
    private ObjectAnimator scaleDown;
    private PhoneBoosterViewModel mViewModel;
    private RotateAnimation r = new RotateAnimation(0, 360,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private ProgressBar progressView;
    private ProgressBar progressView2;
    private ImageView img_crcle;
    private View view_root;
    private GradientProgressBar bar;
    private CircleView bar_circle;
    private LinearLayout button;
    private ConstraintLayout btn_form;
    private ImageView image_blick;
    private TextView memory_p;
    private TextView memory_use;
    private TextView text_memory_p;
    private ConstraintLayout btn_boost;
    private TextView memory_text_p;
    private Integer speed = 0;
    private TextView text_r_process;
    private  ImageView iv ;
    Integer pos = 0;

    public static PhoneBoosterFragment newInstance() {
        return new PhoneBoosterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root = inflater.inflate(R.layout.phone_booster_fragment, container, false);
        initView();
        return view_root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PhoneBoosterViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initView() {
        iv= view_root.findViewById(R.id.image_circle_1);
        progressView = view_root.findViewById(R.id.progress);
        progressView2 = view_root.findViewById(R.id.progress2);
        bar = view_root.findViewById(R.id.bar);
        button = view_root.findViewById(R.id.btn_start);
        image_blick = view_root.findViewById(R.id.image_blick);
        btn_form = view_root.findViewById(R.id.btn_form);
        memory_p = view_root.findViewById(R.id.memory_p);
        memory_use = view_root.findViewById(R.id.memory_use);
        text_memory_p = view_root.findViewById(R.id.text_memory_p);

        text_r_process = view_root.findViewById(R.id.text_r_process);
        bar_circle = view_root.findViewById(R.id.bar_new);
        memory_text_p = view_root.findViewById(R.id.memory_text_p);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("APPS", "click");
                speed = 0;
                bar_circle.startAnim(4000);
                getApp();
            }
        });

        starAnimBtn();
        setMemory();

    }

    private void starAnimBtn() {

        final ValueAnimator translateAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        ObjectAnimator flashAnimator = ObjectAnimator.ofFloat(image_blick, "alpha", 0.0f, 1.0f);

        //  translateAnimator.setDuration(2000);  // animation duration
        translateAnimator.setRepeatCount(5);  // animation repeat count
        translateAnimator.setRepeatMode(ValueAnimator.REVERSE);
        translateAnimator.setRepeatCount(Animation.INFINITE);

        translateAnimator.setDuration(200);
        //translateAnimator.setRepeatCount(5);
        flashAnimator.setRepeatMode(ValueAnimator.REVERSE);
        flashAnimator.setRepeatCount(Animation.INFINITE);


        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(flashAnimator, translateAnimator);
        animatorSet.setDuration(2000);

        final float x = image_blick.getX();
        final float y = image_blick.getY();
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float t = (Float) translateAnimator.getAnimatedValue();
                image_blick.setTranslationX(x + t * 255);    // do your own
                //  image_blick.setTranslationY(y + t*100);    // thing here

            }
        });
        animatorSet.start();
    }



    @SuppressLint("SetTextI18n")
    private void setMemory() {

        Log.d("settttt", "" + SingletonClassApp.getInstance().TotalMemoryInt);
        progressView.setMax(100);
        progressView.setProgress(SingletonClassApp.getInstance().procentMemory);
        progressView2.setMax(100);
        progressView2.setProgress(SingletonClassApp.getInstance().procentMemory);
        bar.setProgress(SingletonClassApp.getInstance().procentMemory, true);
        bar_circle.setProgressСolor(SingletonClassApp.getInstance().procentMemory, true);
        text_memory_p.setText(SingletonClassApp.getInstance().procentMemory + " %");
        memory_p.setText(SingletonClassApp.getInstance().procentMemory + " %");
        memory_text_p.setText(SingletonClassApp.getInstance().UsedMemory + " GB" + "/" + SingletonClassApp.getInstance().TotalMemory + " GB");
        memory_use.setText(SingletonClassApp.getInstance().UsedMemory + " GB" + "/" + SingletonClassApp.getInstance().TotalMemory + " GB");

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

    private void getApp() {



        setAnimeCircle(4,0.9f);

        MyTask mt = new MyTask();
        mt.execute();

    }

    private void setAnimeCircle(int interval,float scale){

        scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", scale),
                PropertyValuesHolder.ofFloat("scaleY", scale));
        scaleDown.setDuration(500);
        scaleDown.setRepeatCount(4);
        //scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            PackageManager pm = getContext().getPackageManager();
// Get the output of running "ps" in a shell.
// This uses libsuperuser: https://github.com/Chainfire/libsuperuser
// To add this to your project: compile 'eu.chainfire:libsuperuser:1.0.0.+'
            List<String> stdout = Shell.SH.run("ps");
            List<String> packages = new ArrayList<>();
            for (String line : stdout) {
                // Get the process-name. It is the last column.
                String[] arr = line.split("\\s+");
                String processName = arr[arr.length - 1].split(":")[0];
                packages.add(processName);
            }

// Get a list of all installed apps on the device.
            List<ApplicationInfo> apps = pm.getInstalledApplications(0);


            for (Iterator<ApplicationInfo> it = apps.iterator(); it.hasNext(); ) {
                if (!packages.contains(it.next().packageName)) {
                    it.remove();
                }
            }
            List<String> packages_all = new ArrayList<>();
            Log.i("APPS", apps.size() + "" + apps.get(0).packageName + " " + apps.get(0).processName);

            for (ApplicationInfo app : apps) {
                String appName = app.loadLabel(pm).toString();
                packages_all.add(app.processName);
                int uid = app.uid;
                long ulBytes = TrafficStats.getUidTxBytes(uid);
                long dlBytes = TrafficStats.getUidRxBytes(uid);
                /* do your stuff */
            }
            ActivityManager am = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
            for (String name : packages_all
            ) {
                try {
                    if (!name.equals("com.cleaner.cybercleanerapp")) {
                        am.killBackgroundProcesses(name);
                    }
                    //Toast.makeText(getContext(), "Process Killed : " + name, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                }
                final DiskStat diskStat = new DiskStat();
                final MemStat memStat = new MemStat(getContext());
                SingletonClassApp.getInstance().UsedMemory = String.valueOf(memStat.getUsedMemory());
                SingletonClassApp.getInstance().TotalMemory = String.valueOf(memStat.getTotalMemory());
                SingletonClassApp.getInstance().procentMemory = memStat.getProcentMemory();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setMemory();
            bar_circle.setProgressСolor(SingletonClassApp.getInstance().procentMemory, true);
            bar_circle.startAnim(0);
            bar_circle.setProgressСolor(SingletonClassApp.getInstance().procentMemory, true);
            setAnimeCircle(0,1.0f);

        }

    }


}

