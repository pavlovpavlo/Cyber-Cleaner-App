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

    private PhoneBoosterViewModel mViewModel;
private  RotateAnimation r = new RotateAnimation(0, 360,
    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
    private ProgressView progressView;
    private Integer speed=0;
    Integer pos=0;
    public static PhoneBoosterFragment newInstance() {
        return new PhoneBoosterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root=inflater.inflate(R.layout.phone_booster_fragment, container, false);
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
        progressView=view_root.findViewById(R.id.progressView);
        bar_circle=view_root.findViewById(R.id.bar_new);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("APPS", "click");
             //   killProcess();
                speed=0;
               // goCircle(10);





                getApp();

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
        bar_circle.setProgresscolor(90,true);
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

        ImageView iv = view_root.findViewById(R.id.image_circle_1);
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                PropertyValuesHolder.ofFloat("scaleY", 0.9f));
        scaleDown.setDuration(500);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();


        final int[] pos = {0};
        for (int i=0;i<repit;i++){

            MyTask mt = new MyTask();
            mt.execute();
        }
    }

@SuppressLint("SetTextI18n")
private void setMemory(){
    bar.setProgress(SingletonClassApp.getInstance().procentMemory,true);
    bar_circle.setProgresscolor(SingletonClassApp.getInstance().procentMemory,true);
    text_memory_p.setText(SingletonClassApp.getInstance().procentMemory+" %");
    memory_p.setText(SingletonClassApp.getInstance().procentMemory+" %");
    memory_use.setText(SingletonClassApp.getInstance().UsedMemory+" GB"+"/"+SingletonClassApp.getInstance().TotalMemory+" GB");

    ActivityManager localActivityManager = (ActivityManager)getContext().getSystemService(ACTIVITY_SERVICE);

    List RunningServiceInfoservices = localActivityManager.getRunningServices(1000);


    Log.e("TAG", " memoryInfo.availMem " + RunningServiceInfoservices.size() + "\n" );

    ActivityManager activityManager = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
    ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
    activityManager.getMemoryInfo(memoryInfo);

    Log.i("TAG", " memoryInfo.availMem " + memoryInfo.availMem + "\n" );
    Log.i("TAG", " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n" );
    Log.i("TAG", " memoryInfo.threshold " + memoryInfo.threshold + "\n" );

    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

    Map<Integer, String> pidMap = new TreeMap<Integer, String>();
    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses)
    {
        pidMap.put(runningAppProcessInfo.pid, runningAppProcessInfo.processName);
    }

    Collection<Integer> keys = pidMap.keySet();

    for(int key : keys)
    {
        int pids[] = new int[1];
        pids[0] = key;
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
        for(android.os.Debug.MemoryInfo pidMemoryInfo: memoryInfoArray)
        {
            Log.i("TAG", String.format("** MEMINFO in pid %d [%s] **\n",pids[0],pidMap.get(pids[0])));
            Log.i("TAG", " pidMemoryInfo.getTotalPrivateDirty(): " + pidMemoryInfo.getTotalPrivateDirty() + "\n");
            Log.i("TAG", " pidMemoryInfo.getTotalPss(): " + pidMemoryInfo.getTotalPss() + "\n");
            Log.i("TAG", " pidMemoryInfo.getTotalSharedDirty(): " + pidMemoryInfo.getTotalSharedDirty() + "\n");
        }
    }




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
    ActivityManager manager = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
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
        r.setDuration(500);
        r.setInterpolator(new LinearInterpolator());
        r.setRepeatMode(Animation.RESTART);
        r.setRepeatCount(Animation.INFINITE);
        bar_circle.startAnimation(r);
        MyTask mt = new MyTask();
        mt.execute();

    }

    public void KillApplication(List<String> KillPackage)
    {

    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // tvInfo.setText("Begin");
        }

        @Override
        protected Void doInBackground(Void... params) {

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


    for (Iterator<ApplicationInfo> it = apps.iterator(); it.hasNext(); ) {
        if (!packages.contains(it.next().packageName)) {
            it.remove();
        }
    }
            List<String> packages_all = new ArrayList<>();
            Log.i("APPS", apps.size()+""+apps.get(0).packageName+" "+apps.get(0).processName);

            for (ApplicationInfo app : apps) {
                String appName = app.loadLabel(pm).toString();
                packages_all.add(app.processName);



                int uid = app.uid;
                long ulBytes = TrafficStats.getUidTxBytes(uid);
                long dlBytes = TrafficStats.getUidRxBytes(uid);
                /* do your stuff */
            }
            ActivityManager am = (ActivityManager)getContext().getSystemService(ACTIVITY_SERVICE);

            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);


            for (String name:packages_all
            ) {
                try {
                    Log.d("myapp",name);
                    if (!name.equals("com.cleaner.cybercleanerapp")){
                        am.killBackgroundProcesses(name);}
                    //Toast.makeText(getContext(), "Process Killed : " + name, Toast.LENGTH_LONG).show();
                }catch (Exception e){}
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
//            r.setDuration(0);
//            bar_circle.startAnimation(null);
//            bar_circle.setProgresscolor(SingletonClassApp.getInstance().procentMemory,true);

         }



    }

    public void killProcess() {
        // TODO Auto-generated method stub
        ActivityManager am = (ActivityManager) getContext()
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        // 获得正在运行的所有进程
        List<ActivityManager.RunningAppProcessInfo> processes = am
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info != null && info.processName != null
                    && info.processName.length() > 0) {
                String pkgName = info.processName;
                if (!("system".equals(pkgName) || "launcher".equals(pkgName)
                        || "android.process.media".equals(pkgName)
                        || "android.process.acore".equals(pkgName)
                        || "com.android.phone".equals(pkgName)
                        || "com.fb.FileBrower".equals(pkgName)// 浏览器
                        || "com.ott_pro.launcher".equals(pkgName)// 桌面
                        || "com.ott_pro.upgrade".equals(pkgName)// 升级
                        || "com.example.airplay".equals(pkgName)// 媒体分享
                        || "com.amlogic.mediacenter".equals(pkgName)// 媒体分享
                        || "com.android.dreams.phototable".equals(pkgName)// 屏保
                        || "com.amlogic.inputmethod.remote".equals(pkgName)// 输入法
                        || pkgName.startsWith("com.lefter"))) {
                    am.killBackgroundProcesses(pkgName);// 杀进程
                }
            }
        }

    }}

