package com.cleaner.cybercleanerapp.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cleaner.cybercleanerapp.R;

import cn.septenary.ui.widget.GradientProgressBar;


public class CircleView extends ConstraintLayout {

    View rootView;
    private GradientProgressBar bar2;
    private GradientProgressBar bar3;
    private GradientProgressBar bar4;
    TextView valueTextView;
    ImageView delFilter;

    public CircleView(Context context) {
        super(context);
        init(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setProgresscolor(int progress,boolean anim) {
        if (progress<=33){
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
            bar2.setVisibility(View.VISIBLE);
            bar2.setProgress(progress,anim);}
        if (progress>33 && progress<=66){
            bar2.setVisibility(View.GONE);
            bar4.setVisibility(View.GONE);
            bar3.setVisibility(View.VISIBLE);
            bar3.setProgress(progress,anim);}
        if (progress>66){
            bar2.setVisibility(View.GONE);
            bar3.setVisibility(View.GONE);
            bar4.setVisibility(View.VISIBLE);
            bar4.setProgress(progress,anim);}
    }


    private void init(Context context) {
        //do setup work here
        rootView = inflate(context, R.layout.circle_view, this);
        bar2 = rootView.findViewById(R.id.bar2);
        bar3 = rootView.findViewById(R.id.bar3);
        bar4 = rootView.findViewById(R.id.bar4);

    }
}

