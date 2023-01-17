package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class MainFragment_smallBird extends Fragment {

    private View view;

    public static MainFragment_smallBird newInstance(){
        MainFragment_smallBird mainFragmentSmallBird = new MainFragment_smallBird();
        return mainFragmentSmallBird;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_small_bird, container, false);
        ImageView itemImage_01 = view.findViewById(R.id.imageView16);

        itemImage_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductDetailPage.class);
                intent.putExtra("memberID", getActivity().getIntent().getStringExtra("memberID"));
                startActivity(intent);
            }
        });

        return view;
    }
}
