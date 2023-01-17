package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return MainFragment_smallBird.newInstance();
            case 1:
                return MainFragment_middleBird.newInstance();
            case 2:
                return MainFragment_bigBird.newInstance();
            case 3:
                return MainFragment_birdToy.newInstance();
            case 4:
                return MainFragment_birdcage.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
