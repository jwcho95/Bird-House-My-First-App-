package com.example.android_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment_bigBird extends Fragment {

    private View view;

    public static MainFragment_bigBird newInstance(){
        MainFragment_bigBird mainFragmentBigBird = new MainFragment_bigBird();
        return mainFragmentBigBird;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_big_bird, container, false);

        return view;
    }
}
