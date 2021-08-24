package com.cleaner.cybercleanerapp.ui.phone_booster;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.cpu_cooler.CPUCoolerViewModel;

import cn.septenary.ui.widget.GradientProgressBar;

public class PhoneBoosterFragment extends Fragment {

    private PhoneBoosterViewModel mViewModel;

    private ImageView img_crcle;
    private View view_root;
    private GradientProgressBar bar;
    private LinearLayout button;
    private ConstraintLayout btn_form;
    private ImageView image_blick;
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
        bar.setProgress(90,true);
        starAnimBtn();


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
}