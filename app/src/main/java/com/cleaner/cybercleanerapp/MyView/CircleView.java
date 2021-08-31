package com.cleaner.cybercleanerapp.MyView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cleaner.cybercleanerapp.R;

import cn.septenary.ui.widget.GradientProgressBar;


public class CircleView extends ConstraintLayout {

    View rootView;
    private LinearLayout linearLayoutColor;
    private GradientProgressBar bar2;
    private GradientProgressBar bar3;
    private GradientProgressBar bar4;
    private ConstraintLayout global_color;
    TextView valueTextView;
    ImageView delFilter;
    Context context;

    public CircleView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setProgressСolor(int progress, boolean anim, Context context) {
        if (progress <= 33) {
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
            try{
            linearLayoutColor.setBackground(context.getDrawable(R.drawable.ic_green_light));
            global_color.setBackground(context.getDrawable(R.drawable.ic_green_light));
            bar2.setVisibility(View.VISIBLE);
            bar2.setProgress(progress, anim);}catch (NullPointerException e){}
        }
        if (progress > 33 && progress <= 66) {
            bar2.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
           try{
            linearLayoutColor.setBackground(context.getDrawable(R.drawable.ic_purple_light));
            global_color.setBackground(context.getDrawable(R.drawable.ic_purple_light));
            bar3.setVisibility(View.VISIBLE);
            bar3.setProgress(progress, anim);}catch (NullPointerException e){}
        }
        if (progress > 66) {

            bar2.setVisibility(View.GONE);
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.VISIBLE);
            try{
            linearLayoutColor.setBackground(context.getDrawable(R.drawable.ic_red_light));
            global_color.setBackground(context.getDrawable(R.drawable.ic_red_light));
            bar4.setProgress(progress, anim);}catch (NullPointerException e){}
        }
    }

    public void optimizationComplete(int progress, boolean anim) {
        bar3.setVisibility(View.GONE);
        bar4.setVisibility(View.GONE);
        bar2.setVisibility(View.VISIBLE);
        bar2.setProgress(progress, anim);
        linearLayoutColor.setBackground(context.getDrawable(R.drawable.ic_green_light));
        global_color.setBackground(context.getDrawable(R.drawable.ic_green_light));

    }

    public void startAnim(int time) {
        RotateAnimation r = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration(500);
        r.setInterpolator(new LinearInterpolator());
        r.setRepeatCount(time);
        rootView.startAnimation(r);
    }

    private void init(Context context) {
        //do setup work here
        rootView = inflate(context, R.layout.circle_view, this);
        bar2 = rootView.findViewById(R.id.bar2);
        bar3 = rootView.findViewById(R.id.bar3);
        bar4 = rootView.findViewById(R.id.bar4);
        global_color = rootView.findViewById(R.id.global_color);
        linearLayoutColor = rootView.findViewById(R.id.linearLayoutColor);
    }
}

