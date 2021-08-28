package com.cleaner.cybercleanerapp.ui.optimization_complete;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.MainActivity;
import com.cleaner.cybercleanerapp.ui.junk_cleaner.JunkCleanerFragment;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.SharedData;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.Date;

public class CompleteFragment extends Fragment {

    private int countGoodOptimized = 0;
    private ConstraintLayout boosterItem;
    private ConstraintLayout batteryItem;
    private ConstraintLayout cpuItem;
    private ConstraintLayout junkItem;
    private TextView countOptimizationText;
    private TextView optimizationSecondText;
    private ProgressBar main_progress;
    private ImageView back_btn;
    private MainActivity mainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.complete_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity.isHideToolbar(true);
        initViews(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.isHideToolbar(false);
    }

    private void checkCountOptimized() {
        if (countGoodOptimized < 4) {
            countOptimizationText.setText(countGoodOptimized + "/4 optimization completed!");
        } else {
            countOptimizationText.setVisibility(View.GONE);
            optimizationSecondText.setVisibility(View.GONE);
        }
        main_progress.setMax(4);
        main_progress.setProgress(countGoodOptimized);
    }

    private void initViews(View view) {
        boosterItem = view.findViewById(R.id.phoneBoosterItem);
        batteryItem = view.findViewById(R.id.batteryItem);
        cpuItem = view.findViewById(R.id.cpuItem);
        junkItem = view.findViewById(R.id.junkItem);
        main_progress = view.findViewById(R.id.main_progress);
        back_btn = view.findViewById(R.id.back_btn);

        back_btn.setOnClickListener(v -> {
            NavController controller = NavHostFragment.findNavController(CompleteFragment.this);
            controller.popBackStack();
        });

        countOptimizationText = view.findViewById(R.id.count_optimization_text);
        optimizationSecondText = view.findViewById(R.id.optimization_second_text);
        checkData();
        checkCountOptimized();
    }

    public void checkData() {
        checkElement(boosterItem, Util.SHARED_STORAGE);
        checkElement(batteryItem, Util.SHARED_BATTERY);
        checkElement(cpuItem, Util.SHARED_CPU);
        checkElement(junkItem, Util.SHARED_JUNK);
    }

    private void checkElement(ConstraintLayout container, String sharedKey) {
        ImageView image = (ImageView) container.getChildAt(0);
        TextView label = (TextView) container.getChildAt(1);
        ProgressBar progress = (ProgressBar) (((LinearLayout) container.getChildAt(2)).getChildAt(1));
        TextView value = (TextView) (((LinearLayout) container.getChildAt(2)).getChildAt(2));
        SharedData data = LocalSharedUtil.getParameter(sharedKey, getContext());


        boolean isOptimized = (Long.parseLong(data.getDate()) + 7_200_000) > new Date().getTime();
        if (isOptimized) {
            countGoodOptimized++;

            label.setBackgroundResource(R.drawable.ic_cleaning_required_btn_optimized);
            label.setText("optimized");

            image.setBackgroundResource(R.drawable.ic_circle_bg_green);
        } else {
            if (data.getPercent() > 33 && data.getPercent() <= 66) {
                image.setBackgroundResource(R.drawable.ic_circle_bg_fiol);
            }
            if (data.getPercent() > 66) {
                image.setBackgroundResource(R.drawable.ic_circle_bg_red);
            }
        }
        progress.setProgress(data.getPercent());
        value.setText(data.getValue());
    }
}