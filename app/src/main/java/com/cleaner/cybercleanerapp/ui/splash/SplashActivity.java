package com.cleaner.cybercleanerapp.ui.splash;



import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initViews();
    }

    private void initViews() {
        ProgressBar progressView = findViewById(R.id.progress);
        ProgressBarAnimation anim = new ProgressBarAnimation(progressView, 0, 100);
        anim.setDuration(7000);
        progressView.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}