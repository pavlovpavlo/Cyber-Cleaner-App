package com.cleaner.cybercleanerapp.ui.junk_cleaner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.callback.IScanCallback;
import com.cleaner.cybercleanerapp.model.JunkGroup;
import com.cleaner.cybercleanerapp.model.JunkInfo;
import com.cleaner.cybercleanerapp.task.OverallScanTask;
import com.cleaner.cybercleanerapp.task.SysCacheScanTask;
import com.cleaner.cybercleanerapp.ui.base.BaseFragment;
import com.cleaner.cybercleanerapp.ui.base.BaseFragmentInterface;
import com.cleaner.cybercleanerapp.util.CleanUtil;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.SharedData;
import com.cleaner.cybercleanerapp.util.SingletonClassApp;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class JunkCleanerFragment extends BaseFragment implements BaseFragmentInterface {
    public static final int MSG_SYS_CACHE_BEGIN = 0x1001;
    public static final int MSG_SYS_CACHE_POS = 0x1002;
    public static final int MSG_SYS_CACHE_FINISH = 0x1003;

    public static final int MSG_PROCESS_BEGIN = 0x1011;
    public static final int MSG_PROCESS_POS = 0x1012;
    public static final int MSG_PROCESS_FINISH = 0x1013;

    public static final int MSG_OVERALL_BEGIN = 0x1021;
    public static final int MSG_OVERALL_POS = 0x1022;
    public static final int MSG_OVERALL_FINISH = 0x1023;

    public static final int MSG_SYS_CACHE_CLEAN_FINISH = 0x1100;
    public static final int MSG_PROCESS_CLEAN_FINISH = 0x1101;
    public static final int MSG_OVERALL_CLEAN_FINISH = 0x1102;

    public static final String HANG_FLAG = "hanged";

    private Handler handler;
    private LinearLayout btn_start;
    private HashMap<Integer, JunkGroup> mJunkGroups = null;

    private View view_root;
    private TextView text_total_size;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root = inflater.inflate(R.layout.junk_cleaner_fragment, container, false);

        initView();
        initHandler();
        onAttachFragment(view_root, this, true);


        return view_root;
    }

    private void startScanJunk(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R){
            btn_start.setEnabled(true);
            final int min = 40;
            final int max = 70;
            final int random = new Random().nextInt((max - min) + 1) + min;

            text_total_size.setText((float)random + " MB");

            bar_circle.setProgress??olor(random, false, getContext());
            mainActivity.isOptimizationActive = false;
        }else {
            btn_start.setEnabled(false);
            bar_circle.setProgress??olor(50, true, getContext());
            bar_circle.startAnim(8000);
            setAnimeCircle(4, 0.9f);
            startScan();
        }
    }

    private void initHandler() {
        handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_SYS_CACHE_BEGIN:
                        break;

                    case MSG_SYS_CACHE_POS:
                        //  mHeaderView.mProgress.setText("????????????:" + ((JunkInfo) msg.obj).mPackageName);
                        //    mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(JunkCleanActivity.this, getTotalSize()));
                        break;

                    case MSG_SYS_CACHE_FINISH:
                        //  checkScanFinish();
                        break;

                    case MSG_SYS_CACHE_CLEAN_FINISH:
                        // checkCleanFinish();
                        Bundle bundle = msg.getData();
                        if (bundle != null) {
                            boolean hanged = bundle.getBoolean(HANG_FLAG, false);
                            if (hanged) {
                                // Toast.makeText(getContext(), "?????????????????????????????????", Toast.LENGTH_SHORT)
//                                        .show();
                            }
                        }
                        break;

                    case MSG_PROCESS_BEGIN:
                        bar_circle.setProgress??olor(50, true, getContext());
                        bar_circle.startAnim(400);
                        break;

                    case MSG_PROCESS_POS:
                        //  mHeaderView.mProgress.setText("????????????:" + ((JunkInfo) msg.obj).mPackageName);
                        //  mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(JunkCleanActivity.this, getTotalSize()));
                        break;

                    case MSG_PROCESS_FINISH:
                        //  checkScanFinish();
                        break;

                    case MSG_PROCESS_CLEAN_FINISH:
                        //  checkCleanFinish();
                        break;

                    case MSG_OVERALL_BEGIN:
                        break;

                    case MSG_OVERALL_POS:
                        break;

                    case MSG_OVERALL_FINISH:
                        checkScanFinish();
                        break;

                    case MSG_OVERALL_CLEAN_FINISH:

                        checkCleanFinish();
                        break;
                }
            }
        };
    }

    private void checkCleanFinish() {
        btn_start.setEnabled(true);
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(SingletonClassApp.getInstance().procentMemory, true);

        mainActivity.isOptimizationActive = true;
        startScan();

    }

    private void checkScanFinish() {
        btn_start.setEnabled(true);
        long size = getTotalSize();
        Log.d("memo_clear", "" + size);
        String totalSize = CleanUtil.formatShortFileSize(getContext(), size);
        bar_circle.startAnim(0);

        text_total_size.setText(totalSize);

        final int min = 40;
        final int max = 70;
        final int random = new Random().nextInt((max - min) + 1) + min;

        bar_circle.setProgress??olor(random, true, getContext());
        mainActivity.isOptimizationActive = false;
    }

    private void startScan() {
        btn_start.setEnabled(false);
        mainActivity.isOptimizationActive = true;
        bar_circle.setProgress??olor(50, true, getContext());
        bar_circle.startAnim(200000);


        SysCacheScanTask sysCacheScanTask = new SysCacheScanTask(new IScanCallback() {
            @Override
            public void onBegin() {
                Message msg = handler.obtainMessage(MSG_SYS_CACHE_BEGIN);
                msg.sendToTarget();
            }

            @Override
            public void onProgress(JunkInfo info) {
                Message msg = handler.obtainMessage(MSG_SYS_CACHE_POS);
                msg.obj = info;
                msg.sendToTarget();
            }

            @Override
            public void onFinish(ArrayList<JunkInfo> children) {
                JunkGroup cacheGroup = mJunkGroups.get(JunkGroup.GROUP_CACHE);
                cacheGroup.mChildren.addAll(children);
                Collections.sort(cacheGroup.mChildren);
                Collections.reverse(cacheGroup.mChildren);
                for (JunkInfo info : children) {
                    cacheGroup.mSize += info.mSize;
                }
                Message msg = handler.obtainMessage(MSG_SYS_CACHE_FINISH);
                msg.sendToTarget();

                mainActivity.isOptimizationActive = false;
            }
        });
        sysCacheScanTask.execute();

        OverallScanTask overallScanTask = new OverallScanTask(new IScanCallback() {
            @Override
            public void onBegin() {
                Message msg = handler.obtainMessage(MSG_OVERALL_BEGIN);
                msg.sendToTarget();
            }

            @Override
            public void onProgress(JunkInfo info) {
                Message msg = handler.obtainMessage(MSG_OVERALL_POS);
                msg.obj = info;
                msg.sendToTarget();
            }

            @Override
            public void onFinish(ArrayList<JunkInfo> children) {
                for (JunkInfo info : children) {
                    String path = info.mChildren.get(0).mPath;
                    int groupFlag = 0;
                    if (path.endsWith(".apk")) {
                        groupFlag = JunkGroup.GROUP_APK;
                    } else if (path.endsWith(".log")) {
                        groupFlag = JunkGroup.GROUP_LOG;
                    } else if (path.endsWith(".tmp") || path.endsWith(".temp")) {
                        groupFlag = JunkGroup.GROUP_TMP;
                    }

                    JunkGroup cacheGroup = mJunkGroups.get(groupFlag);
                    cacheGroup.mChildren.addAll(info.mChildren);
                    cacheGroup.mSize = info.mSize;
                }

                Message msg = handler.obtainMessage(MSG_OVERALL_FINISH);
                msg.sendToTarget();
            }
        });
        overallScanTask.execute();
    }

    private void resetState() {

        mJunkGroups = new HashMap<>();
        // mCleanButton.setEnabled(true);

        JunkGroup cacheGroup = new JunkGroup();
        cacheGroup.mName = "cache_clean";
        cacheGroup.mChildren = new ArrayList<>();
        mJunkGroups.put(JunkGroup.GROUP_CACHE, cacheGroup);

        JunkGroup processGroup = new JunkGroup();
        processGroup.mName = "process_clean";
        processGroup.mChildren = new ArrayList<>();
        mJunkGroups.put(JunkGroup.GROUP_PROCESS, processGroup);

        JunkGroup apkGroup = new JunkGroup();
        apkGroup.mName = "apk_clean";
        apkGroup.mChildren = new ArrayList<>();
        mJunkGroups.put(JunkGroup.GROUP_APK, apkGroup);

        JunkGroup tmpGroup = new JunkGroup();
        tmpGroup.mName = "tmp_clean";
        tmpGroup.mChildren = new ArrayList<>();
        mJunkGroups.put(JunkGroup.GROUP_TMP, tmpGroup);

        JunkGroup logGroup = new JunkGroup();
        logGroup.mName = "log_clean";
        logGroup.mChildren = new ArrayList<>();
        mJunkGroups.put(JunkGroup.GROUP_LOG, logGroup);
        startScanJunk();
    }

    private void clearAll() {
        Thread clearThread = new Thread(() -> {
            JunkGroup processGroup = mJunkGroups.get(JunkGroup.GROUP_PROCESS);
            for (JunkInfo info : processGroup.mChildren) {
                CleanUtil.killAppProcesses(info.mPackageName);
            }
            Message msg = handler.obtainMessage(JunkCleanerFragment.MSG_PROCESS_CLEAN_FINISH);
            msg.sendToTarget();

            CleanUtil.freeAllAppsCache(handler);

            ArrayList<JunkInfo> junks = new ArrayList<>();
            JunkGroup group = mJunkGroups.get(JunkGroup.GROUP_APK);
            junks.addAll(group.mChildren);

            group = mJunkGroups.get(JunkGroup.GROUP_LOG);
            junks.addAll(group.mChildren);

            group = mJunkGroups.get(JunkGroup.GROUP_TMP);
            junks.addAll(group.mChildren);

            CleanUtil.freeJunkInfos(junks, handler);
        });
        clearThread.start();
    }

    private long getTotalSize() {
        long size = 0L;
        for (JunkGroup group : mJunkGroups.values()) {
            size += group.mSize;
        }
        return size;
    }

    private void initView() {
        text_total_size = view_root.findViewById(R.id.text_total_size);
    }

    @Override
    public void basicInit() {
        btn_start = view_root.findViewById(R.id.btn_start);
        starAnimBtn();
        /*bar_circle.setProgress??olor(50, true, getContext());
        bar_circle.startAnim(8000);
        setAnimeCircle(4, 0.9f);*/
        resetState();
        checkElement(Util.SHARED_JUNK);
    }

    @Override
    public void optimization() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R){
            clearAll();
        }

    }

    @Override
    public void optimizationClick() {
        bar_circle.startAnim(0);
        ((LinearLayout) text_total_size.getParent()).setVisibility(View.GONE);
        imageLoadIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void fragmentIsOptimized(SharedData data) {
        //   bar_circle.startAnim(0);
        bar_circle.optimizationComplete(data.getPercent(), true);
    }

    @Override
    public void onOptimizationComplete() {
        //  bar_circle.startAnim(0);
        bar_circle.optimizationComplete(SingletonClassApp.getInstance().procentMemory, true);
        ((LinearLayout) text_total_size.getParent()).setVisibility(View.VISIBLE);
        imageLoadIcon.setVisibility(View.GONE);

        long size = getTotalSize();
        final int min = 10;
        final int max = 30;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String totalSize = CleanUtil.formatShortFileSize(getContext(), size);
        text_total_size.setText(totalSize);
        LocalSharedUtil.setParameter(
                new SharedData(random,
                        totalSize + "/" + SingletonClassApp.getInstance().TotalMemory + " GB", "" + new Date().getTime()),
                Util.SHARED_JUNK, getContext());

        NavController controller = NavHostFragment.findNavController(JunkCleanerFragment.this);
        controller.navigate(R.id.complete_fragment, null);
    }
}