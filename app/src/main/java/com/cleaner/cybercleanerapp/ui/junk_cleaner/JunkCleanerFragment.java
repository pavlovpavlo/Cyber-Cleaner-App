package com.cleaner.cybercleanerapp.ui.junk_cleaner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    private HashMap<Integer, JunkGroup> mJunkGroups = null;

    private View view_root;
    private TextView text_total_size;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root = inflater.inflate(R.layout.junk_cleaner_fragment, container, false);

        initHandler();
        onAttachFragment(view_root, this);
        initView();

        return view_root;
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
                        //  mHeaderView.mProgress.setText("正在扫描:" + ((JunkInfo) msg.obj).mPackageName);
                        //    mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(JunkCleanActivity.this, getTotalSize()));
                        break;

                    case MSG_SYS_CACHE_FINISH:
                        checkScanFinish();
                        break;

                    case MSG_SYS_CACHE_CLEAN_FINISH:
                        checkCleanFinish();
                        Bundle bundle = msg.getData();
                        if (bundle != null) {
                            boolean hanged = bundle.getBoolean(HANG_FLAG, false);
                            if (hanged) {
                                // Toast.makeText(getContext(), "清理系统缓存出现异常！", Toast.LENGTH_SHORT)
//                                        .show();
                            }
                        }
                        break;

                    case MSG_PROCESS_BEGIN:
                        bar_circle.setProgressСolor(50, true, getContext());
                        bar_circle.startAnim(400);
                        break;

                    case MSG_PROCESS_POS:
                        //  mHeaderView.mProgress.setText("正在扫描:" + ((JunkInfo) msg.obj).mPackageName);
                        //  mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(JunkCleanActivity.this, getTotalSize()));
                        break;

                    case MSG_PROCESS_FINISH:
                        checkScanFinish();
                        break;

                    case MSG_PROCESS_CLEAN_FINISH:
                        checkCleanFinish();
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
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(SingletonClassApp.getInstance().procentMemory, true);
        startScan();
    }

    private void checkScanFinish() {
        long size = getTotalSize();
        Log.d("memo_clear", "" + size);
        String totalSize = CleanUtil.formatShortFileSize(getContext(), size);
        bar_circle.startAnim(0);
        bar_circle.setProgressСolor((int) (size * 100 / 100000000), true, getContext());
        text_total_size.setText(totalSize);

        LocalSharedUtil.setParameter(
                new SharedData(SingletonClassApp.getInstance().procentMemory,
                        totalSize + " MB" + "/" + SingletonClassApp.getInstance().TotalMemory + " GB", ""+new Date().getTime()),
                Util.SHARED_JUNK, getContext());
    }

    private void startScan() {
        bar_circle.setProgressСolor(50, true, getContext());

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
        startScan();
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
        starAnimBtn();
        bar_circle.setProgressСolor(50, true, getContext());
        bar_circle.startAnim(4000);
        setAnimeCircle(4, 0.9f);
        resetState();
    }

    @Override
    public void optimization() {
        clearAll();
    }

    @Override
    public void onOptimizationComplete() {
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(SingletonClassApp.getInstance().procentMemory, true);
    }
}