package com.example.android_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment_birdcage extends Fragment {

    private View view;

    public static MainFragment_birdcage newInstance(){
        MainFragment_birdcage mainFragmentBirdcage = new MainFragment_birdcage();
        return mainFragmentBirdcage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_birdcage, container, false);

        return view;
    }
}
