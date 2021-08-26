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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.MainActivity;
import com.cleaner.cybercleanerapp.ui.cpu_cooler.CPUCoolerViewModel;
import com.cleaner.cybercleanerapp.util.DiskStat;
import com.cleaner.cybercleanerapp.util.MemStat;
import com.cleaner.cybercleanerapp.util.SingletonClassApp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.septenary.ui.widget.GradientProgressBar;
import eu.chainfire.libsuperuser.Shell;

public class PhoneBoosterFragment extends Fragment {

    private PhoneBoosterViewModel mViewModel;

    private ImageView img_crcle;
    private View view_root;
    private GradientProgressBar bar;
    private LinearLayout button;
    private ConstraintLayout btn_form;
    private ImageView image_blick;
    private TextView memory_p;
    private TextView memory_use;
    private TextView text_memory_p;
    private ConstraintLayout btn_boost;
    Integer pos=0;
    public static PhoneBoosterFragment newInstance() {
        return new PhoneBoosterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root=inflater.inflate(R.layout.phone_booster_fragment, container, false);
        ImageView iv = view_root.findViewById(R.id.image_circle_1);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                PropertyValuesHolder.ofFloat("scaleY", 0.9f));
        scaleDown.setDuration(500);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();

        initView();
        return view_root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PhoneBoosterViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initView(){
        bar=view_root.findViewById(R.id.bar);
        button=view_root.findViewById(R.id.btn_start);
        image_blick=view_root.findViewById(R.id.image_blick);
        btn_form=view_root.findViewById(R.id.btn_form);
        memory_p=view_root.findViewById(R.id.memory_p);
        memory_use=view_root.findViewById(R.id.memory_use);
        text_memory_p=view_root.findViewById(R.id.text_memory_p);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("APPS", "click");
                goCircle(20);
               // getApp();
            }
        });

//        btn_boost=view_root.findViewById(R.id.btn_form);
//       btn_boost.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               getApp();
//           }
//       });


        bar.setProgress(90,true);
        starAnimBtn();
        setMemory();

    }

    private void starAnimBtn(){

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
        animatorSet.playTogether(flashAnimator,translateAnimator);
        animatorSet.setDuration(2000);

        final float x = image_blick.getX();
        final float y = image_blick.getY();
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float t = (Float) translateAnimator.getAnimatedValue();
                image_blick.setTranslationX(x + t*255);    // do your own
                //  image_blick.setTranslationY(y + t*100);    // thing here

            }
        });

        animatorSet.start();

    }

    private void goCircle(int repit){

        final int[] pos = {0};
        for (int i=0;i<repit;i++){

            MyTask mt = new MyTask();
            mt.execute();
        }
    }

@SuppressLint("SetTextI18n")
private void setMemory(){
    bar.setProgress(SingletonClassApp.getInstance().procentMemory,true);
    text_memory_p.setText(SingletonClassApp.getInstance().procentMemory+" %");
    memory_p.setText(SingletonClassApp.getInstance().UsedMemory+" GB");
    memory_use.setText(SingletonClassApp.getInstance().UsedMemory+" GB"+"/"+SingletonClassApp.getInstance().TotalMemory+" GB");


    PackageManager pm =getContext().getPackageManager();
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

// Remove apps which are not running.
//    for (Iterator<ApplicationInfo> it = apps.iterator(); it.hasNext(); ) {
//        if (!packages.contains(it.next().packageName)) {
//            it.remove();
//        }
//    }
    Log.i("APPS", apps.size()+"");
    for (ApplicationInfo app : apps) {
        String appName = app.loadLabel(pm).toString();
        int uid = app.uid;
        long ulBytes = TrafficStats.getUidTxBytes(uid);
        long dlBytes = TrafficStats.getUidRxBytes(uid);
        /* do your stuff */
    }





    ActivityManager actvityManager = (ActivityManager)
            getContext().getSystemService(getContext().ACTIVITY_SERVICE );
    List<ActivityManager.RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();

    for(ActivityManager.RunningAppProcessInfo runningProInfo:procInfos){

        Log.d("Running Processes", "()()"+runningProInfo.processName);
    }

    String currentProcName = "";
    int pid = android.os.Process.myPid();
    ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
    Log.e("proc",manager.getRunningAppProcesses().size()+"");
    for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses())
    {
        if (processInfo.pid == pid)
        {
            currentProcName = processInfo.processName;
            Log.d("Running Processes", "()()"+currentProcName);
            return;
        }
    }


    }

    private void getApp(){

        PackageManager pm =getContext().getPackageManager();
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

// Remove apps which are not running.
//    for (Iterator<ApplicationInfo> it = apps.iterator(); it.hasNext(); ) {
//        if (!packages.contains(it.next().packageName)) {
//            it.remove();
//        }
//    }
        List<String> packages_all = new ArrayList<>();
        Log.i("APPS", apps.size()+"");
        for (ApplicationInfo app : apps) {
            String appName = app.loadLabel(pm).toString();
            packages_all.add(appName);
            int uid = app.uid;
            long ulBytes = TrafficStats.getUidTxBytes(uid);
            long dlBytes = TrafficStats.getUidRxBytes(uid);
            /* do your stuff */
        }
        KillApplication(packages_all);
    }

    public void KillApplication(List<String> KillPackage)
    {
        ActivityManager am = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startMain);

        for (String name:KillPackage
             ) {
            try {
                am.killBackgroundProcesses(name);
             //   Toast.makeText(getContext(), "Process Killed : " + name, Toast.LENGTH_LONG).show();
            }catch (Exception e){}
            }

    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // tvInfo.setText("Begin");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           if (pos==0){pos=100;}else {pos=0;};
            bar.setProgress(pos,true);

        }
    }


}

