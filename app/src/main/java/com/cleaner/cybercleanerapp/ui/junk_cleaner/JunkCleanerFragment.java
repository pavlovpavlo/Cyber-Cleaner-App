package com.cleaner.cybercleanerapp.ui.junk_cleaner;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleaner.cybercleanerapp.R;

public class JunkCleanerFragment extends Fragment {

    private JunkCleanerViewModel mViewModel;

    public static JunkCleanerFragment newInstance() {
        return new JunkCleanerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.junk_cleaner_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(JunkCleanerViewModel.class);
        // TODO: Use the ViewModel
    }

}