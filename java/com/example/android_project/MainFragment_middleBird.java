package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment_middleBird extends Fragment {

    private View view;

    public static MainFragment_middleBird newInstance(){
        MainFragment_middleBird mainFragment_middleBird = new MainFragment_middleBird();
        return mainFragment_middleBird;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_middle_bird, container, false);
        ImageView itemImage_01 = view.findViewById(R.id.imageView19);

        itemImage_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductDetailPage.class);
                intent.putExtra("memberID", getActivity().getIntent().getStringExtra("memberID"));
                startActivity(intent);
            }
        });


        return view;
    }
}
