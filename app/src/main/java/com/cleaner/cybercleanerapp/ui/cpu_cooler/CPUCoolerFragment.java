package com.cleaner.cybercleanerapp.ui.cpu_cooler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.cleaner.cybercleanerapp.R;
import com.cleaner.cybercleanerapp.ui.MainActivity;
import com.cleaner.cybercleanerapp.ui.base.BaseFragment;
import com.cleaner.cybercleanerapp.ui.base.BaseFragmentInterface;
import com.cleaner.cybercleanerapp.ui.phone_booster.PhoneBoosterFragment;
import com.cleaner.cybercleanerapp.util.LocalSharedUtil;
import com.cleaner.cybercleanerapp.util.PhoneTaskCleanerUtil;
import com.cleaner.cybercleanerapp.util.SharedData;
import com.cleaner.cybercleanerapp.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.cleaner.cybercleanerapp.util.Util.MAX_CPU_TEMP;
import static com.cleaner.cybercleanerapp.util.Util.cpuTemperature;

public class CPUCoolerFragment extends BaseFragment implements BaseFragmentInterface {
    private View view_root;
    private TextView cpuTempText;
    private int basicProcent;
    private float cpuTemp;
    public List<InstalledAppsInfoModel> installedAppsInfo = new ArrayList<>();
    private LinearLayout containerApp;
    private LayoutInflater lLayoutInflater;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view_root = inflater.inflate(R.layout.cpu_cooler_fragment, container, false);
        onAttachFragment(view_root, this, false);
        return view_root;
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        setMainText();
    }

    private void setMainText() {
        cpuTempText.setText(cpuTemp + "°C");

        bar_circle.setProgressСolor(basicProcent, true, getContext());
    }

    @Override
    public void basicInit() {
        containerApp = view_root.findViewById(R.id.containerApp);
        cpuTempText = view_root.findViewById(R.id.cpuTempText);
        lLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cpuTemp = cpuTemperature();
        basicProcent = (int) ((cpuTemp / MAX_CPU_TEMP) * 100);
        starAnimBtn();
        setData();
        new AsyncTaskList((MainActivity) getActivity(), this).execute();
    }

    public void setAppsList() {
        int countInRow = 4;
        int countAll = installedAppsInfo.size();

        for (int i = 0; i < installedAppsInfo.size() / countInRow; i++) {
            LinearLayout programContainer = (LinearLayout)lLayoutInflater.inflate(R.layout.item_program_container, null);

            int countCell = countAll < countInRow ? countAll : countInRow;
            for (int j = 0; j < countCell; j++) {
                View program = lLayoutInflater.inflate(R.layout.item_process, null);
                TextView text = program.findViewById(R.id.process_size);
                ImageView image = program.findViewById(R.id.process_image);
                View line = program.findViewById(R.id.line);
                InstalledAppsInfoModel model = installedAppsInfo.get(i*countInRow + j);

                image.setImageDrawable(model.getInstalledAppsIcons());
                text.setText(model.getInstalledAppsSize());
                if(j == countCell-1)
                    line.setVisibility(View.GONE);

                programContainer.addView(program);
            }
            countAll -= countCell;

            containerApp.addView(programContainer);
        }

    }

    @Override
    public void optimization() {
        PhoneTaskCleanerUtil.clearBackgroundTasks(getContext());
    }

    @Override
    public void optimizationClick() {
        cpuTempText.setVisibility(View.GONE);
        imageLoadIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void onOptimizationComplete() {
        cpuTemp -= new Random().nextInt(3);
        basicProcent = (int) ((cpuTemp / MAX_CPU_TEMP) * 100);
        setData();
        bar_circle.startAnim(0);
        bar_circle.optimizationComplete(basicProcent, true);

        LocalSharedUtil.setParameter(
                new SharedData(basicProcent,
                        cpuTemp + "°C", "" + new Date().getTime()),
                Util.SHARED_CPU, getContext());

        cpuTempText.setVisibility(View.VISIBLE);
        imageLoadIcon.setVisibility(View.GONE);
        NavController controller = NavHostFragment.findNavController(CPUCoolerFragment.this);
        controller.navigate(R.id.complete_fragment, null);
    }
}