package com.cleaner.cybercleanerapp.ui.battery_saver;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleaner.cybercleanerapp.R;

public class BatterySaverFragment extends Fragment {

    private BatterySaverViewModel mViewModel;

    public static BatterySaverFragment newInstance() {
        return new BatterySaverFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.battery_saver_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BatterySaverViewModel.class);
        // TODO: Use the ViewModel
    }

}