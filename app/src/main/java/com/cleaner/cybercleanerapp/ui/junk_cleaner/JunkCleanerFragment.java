package com.cleaner.cybercleanerapp.ui.junk_cleaner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.callback.IScanCallback;
import com.cleaner.cybercleanerapp.model.JunkGroup;
import com.cleaner.cybercleanerapp.model.JunkInfo;
import com.cleaner.cybercleanerapp.task.OverallScanTask;
import com.cleaner.cybercleanerapp.task.ProcessScanTask;
import com.cleaner.cybercleanerapp.task.SysCacheScanTask;
import com.cleaner.cybercleanerapp.ui.base.BaseFragment;
import com.cleaner.cybercleanerapp.ui.base.BaseFragmentInterface;
import com.cleaner.cybercleanerapp.util.CleanUtil;
import com.cleaner.cybercleanerapp.util.SingletonClassApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cn.septenary.ui.widget.GradientProgressBar;

public class JunkCleanerFragment extends BaseFragment implements BaseFragmentInterface {
    private static final double B_TO_GB = 1024d * 1024d * 1024d;
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

    private boolean mIsSysCacheScanFinish = false;
    private boolean mIsSysCacheCleanFinish = false;

    private boolean mIsProcessScanFinish = false;
    private boolean mIsProcessCleanFinish = false;

    private boolean mIsOverallScanFinish = false;
    private boolean mIsOverallCleanFinish = false;

    private boolean mIsScanning = false;

    private BaseExpandableListAdapter mAdapter;
    private HashMap<Integer, JunkGroup> mJunkGroups = null;

    private Button mCleanButton;


    private JunkCleanerViewModel mViewModel;
    private ImageView img_crcle;
    private View view_root;
    private GradientProgressBar bar;
    private LinearLayout button;
    private ConstraintLayout btn_form;
    private ImageView image_blick;
    private TextView text_total_size;
    public static JunkCleanerFragment newInstance() {
        return new JunkCleanerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       view_root=inflater.inflate(R.layout.junk_cleaner_fragment, container, false);




        handler = new Handler() {
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
                        mIsSysCacheScanFinish = true;
                        checkScanFinish();
                        break;

                    case MSG_SYS_CACHE_CLEAN_FINISH:
                        mIsSysCacheCleanFinish = true;
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
                        bar_circle.setProgressСolor(50,true,getContext());
                        bar_circle.startAnim(400);
                        break;

                    case MSG_PROCESS_POS:
                      //  mHeaderView.mProgress.setText("正在扫描:" + ((JunkInfo) msg.obj).mPackageName);
                      //  mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(JunkCleanActivity.this, getTotalSize()));
                        break;

                    case MSG_PROCESS_FINISH:
                        mIsProcessScanFinish = true;
                        checkScanFinish();
                        break;

                    case MSG_PROCESS_CLEAN_FINISH:
                        mIsProcessCleanFinish = true;
                        checkCleanFinish();
                        break;

                    case MSG_OVERALL_BEGIN:
                        break;

                    case MSG_OVERALL_POS:
                     //   mHeaderView.mProgress.setText("正在扫描:" + ((JunkInfo) msg.obj).mPath);
                     //   mHeaderView.mSize.setText(CleanUtil.formatShortFileSize(JunkCleanActivity.this, getTotalSize()));
                        break;

                    case MSG_OVERALL_FINISH:
                        mIsOverallScanFinish = true;
                        checkScanFinish();
                        break;

                    case MSG_OVERALL_CLEAN_FINISH:
                        mIsOverallCleanFinish = true;
                        checkCleanFinish();
                        break;
                }
            }
        };


        onAttachFragment(view_root, this);
        initView();

        return view_root;
    }

    private void checkCleanFinish() {
    }

    private void checkScanFinish(){
        long size = getTotalSize();
        Log.d("memo_clear",""+size);
        String totalSize = CleanUtil.formatShortFileSize(getContext(), size);
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete( (int)(size * 100 / 100000000), true);
        text_total_size.setText(totalSize);
    }

    private void startScan() {
        bar_circle.setProgressСolor(50,true,getContext());
//        ProcessScanTask processScanTask = new ProcessScanTask(new IScanCallback() {
//            @Override
//            public void onBegin() {
//                Message msg = handler.obtainMessage(MSG_PROCESS_BEGIN);
//                msg.sendToTarget();
//            }
//
//            @Override
//            public void onProgress(JunkInfo info) {
//                Message msg = handler.obtainMessage(MSG_PROCESS_POS);
//                msg.obj = info;
//                msg.sendToTarget();
//            }
//
//            @Override
//            public void onFinish(ArrayList<JunkInfo> children) {
//                JunkGroup cacheGroup = mJunkGroups.get(JunkGroup.GROUP_PROCESS);
//                cacheGroup.mChildren.addAll(children);
//                for (JunkInfo info : children) {
//                    cacheGroup.mSize += info.mSize;
//                }
//                Message msg = handler.obtainMessage(MSG_PROCESS_FINISH);
//                msg.sendToTarget();
//            }
//        });
//        processScanTask.execute();

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
        mIsScanning = false;
        mIsSysCacheScanFinish = false;
        mIsSysCacheCleanFinish = false;
        mIsProcessScanFinish = false;
        mIsProcessCleanFinish = false;
        mJunkGroups = new HashMap<>();
       // mCleanButton.setEnabled(true);

        JunkGroup cacheGroup = new JunkGroup();
        cacheGroup.mName ="cache_clean";
        cacheGroup.mChildren = new ArrayList<>();
        mJunkGroups.put(JunkGroup.GROUP_CACHE, cacheGroup);

        JunkGroup processGroup = new JunkGroup();
        processGroup.mName = "process_clean";
        processGroup.mChildren = new ArrayList<>();
        mJunkGroups.put(JunkGroup.GROUP_PROCESS, processGroup);

        JunkGroup apkGroup = new JunkGroup();
        apkGroup.mName ="apk_clean";
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
        Thread clearThread = new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(JunkCleanerViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initView(){
        text_total_size=view_root.findViewById(R.id.text_total_size);
    }



    @Override
    public void basicInit() {
        starAnimBtn();
        bar_circle.setProgressСolor(50,true,getContext());
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