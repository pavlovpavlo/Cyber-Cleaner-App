package com.cleaner.cybercleanerapp.ui.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.cleaner.cybercleanerapp.MyView.CircleView;
import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.MainActivity;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.SharedData;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BaseFragment extends Fragment {
    private View view;
    private MainActivity mainActivity;
    private BaseFragmentInterface baseFragmentInterface;
    private boolean isPermissionFragment;
    public CircleView bar_circle;
    public LinearLayout button;
    public ImageView image_blick;
    public ImageView iv;
    public ImageView imageLoadIcon;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    public void onAttachFragment(View view, BaseFragmentInterface baseFragmentInterface, boolean isPermissionFragment){
        this.view = view;
        this.baseFragmentInterface = baseFragmentInterface;
        this.isPermissionFragment = isPermissionFragment;
        initViews();
        baseFragmentInterface.basicInit();
    }

    private void initViews(){
        bar_circle = view.findViewById(R.id.bar_new);
        button = view.findViewById(R.id.btn_start);
        image_blick = view.findViewById(R.id.image_blick);
        iv = view.findViewById(R.id.image_circle_1);
        imageLoadIcon = view.findViewById(R.id.loadIcon);

        button.setOnClickListener(v -> {
            if(isPermissionFragment) {
                if (ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            110011);
                } else {
                    startOptimization();
                }
            }else
                startOptimization();


        });
    }

    private void startOptimization(){
        baseFragmentInterface.optimizationClick();
        bar_circle.startAnim(4000);
        getApp();
        starAnimBtn();
    }

    public void checkElement(String sharedKey) {
        SharedData data = LocalSharedUtil.getParameter(sharedKey, getContext());

        boolean isOptimized = (Long.parseLong(data.getDate()) + 7_200_000) > new Date().getTime();
        if (isOptimized) {
            setButtonOptimized();
            baseFragmentInterface.fragmentIsOptimized(data);
        }
    }

    public void starAnimBtn() {
        image_blick.setVisibility(View.VISIBLE);
        final ValueAnimator translateAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        ObjectAnimator flashAnimator = ObjectAnimator.ofFloat(image_blick, "alpha", 0.0f, 1.0f);

        translateAnimator.setRepeatCount(5);  // animation repeat count
        translateAnimator.setRepeatMode(ValueAnimator.REVERSE);
        translateAnimator.setRepeatCount(Animation.INFINITE);

        translateAnimator.setDuration(200);
        flashAnimator.setRepeatMode(ValueAnimator.REVERSE);
        flashAnimator.setRepeatCount(Animation.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(flashAnimator, translateAnimator);
        animatorSet.setDuration(2000);

        final float x = image_blick.getX();
        final float y = image_blick.getY();
        translateAnimator.addUpdateListener(valueAnimator -> {
            float t = (Float) translateAnimator.getAnimatedValue();
            image_blick.setTranslationX(x + t * 255);
        });
        animatorSet.start();
    }

    public void getApp() {
        setAnimeCircle(4, 0.9f);
        MyTask mt = new MyTask();
        mt.execute();
    }

    public void setButtonOptimized(){
        TextView buttonText = (TextView)button.getChildAt(0);
        ImageView buttonImage = (ImageView)button.getChildAt(1);

        buttonText.setText("Optimized");
        button.setBackgroundResource(R.drawable.main_btn_bg_optimize);
        buttonImage.setImageResource(R.drawable.ic_main_complete);
        image_blick.setVisibility(View.INVISIBLE);
    }

    public void setAnimeCircle(int interval, float scale) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                iv,
                PropertyValuesHolder.ofFloat("scaleX", scale),
                PropertyValuesHolder.ofFloat("scaleY", scale));
        scaleDown.setDuration(500);
        scaleDown.setRepeatCount(interval);
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
            if(mainActivity != null)
                mainActivity.isOptimizationActive = true;
            baseFragmentInterface.optimization();
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
            baseFragmentInterface.onOptimizationComplete();
            mainActivity.checkData();
            setAnimeCircle(0, 1.0f);
            setButtonOptimized();
            if(mainActivity != null)
                mainActivity.isOptimizationActive = false;
        }
    }
}
