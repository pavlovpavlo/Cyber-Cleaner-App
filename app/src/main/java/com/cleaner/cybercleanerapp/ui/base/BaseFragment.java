package com.cleaner.cybercleanerapp.ui.base;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cleaner.cybercleanerapp.MyView.CircleView;
import com.cleaner.cybercleanerapp.R;

import java.util.concurrent.TimeUnit;

public class BaseFragment extends Fragment {
    private View view;
    private BaseFragmentInterface baseFragmentInterface;
    public CircleView bar_circle;
    public LinearLayout button;
    public ImageView image_blick;
    public ImageView iv;

    public void onAttachFragment(View view, BaseFragmentInterface baseFragmentInterface){
        this.view = view;
        this.baseFragmentInterface = baseFragmentInterface;
        initViews();
        baseFragmentInterface.basicInit();
    }

    private void initViews(){
        bar_circle = view.findViewById(R.id.bar_new);
        button = view.findViewById(R.id.btn_start);
        image_blick = view.findViewById(R.id.image_blick);
        iv = view.findViewById(R.id.image_circle_1);
        button.setOnClickListener(v -> {
            bar_circle.startAnim(4000);
            getApp();
            starAnimBtn();
        });
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
            setAnimeCircle(0, 1.0f);
            setButtonOptimized();
        }
    }
}