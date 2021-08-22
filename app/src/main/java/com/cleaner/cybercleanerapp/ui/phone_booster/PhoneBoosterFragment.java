package com.cleaner.cybercleanerapp.ui.phone_booster;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleaner.cybercleanerapp.R;

public class PhoneBoosterFragment extends Fragment {

    private PhoneBoosterViewModel mViewModel;

    public static PhoneBoosterFragment newInstance() {
        return new PhoneBoosterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.phone_booster_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PhoneBoosterViewModel.class);
        // TODO: Use the ViewModel
    }

}