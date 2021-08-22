package com.cleaner.cybercleanerapp.ui.cpu_cooler;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleaner.cybercleanerapp.R;

public class CPUCoolerFragment extends Fragment {

    private CPUCoolerViewModel mViewModel;

    public static CPUCoolerFragment newInstance() {
        return new CPUCoolerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cpu_cooler_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CPUCoolerViewModel.class);
        // TODO: Use the ViewModel
    }

}