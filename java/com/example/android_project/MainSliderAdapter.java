package com.example.android_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainSliderAdapter extends RecyclerView.Adapter<MainSliderAdapter.CustomViewHolder> {

    private static final String TAG = "SliderAdapter";

    private Context mContext;
    private ArrayList<MainSliderData> sliderItems;
    private ViewPager2 viewPager2;

    public MainSliderAdapter(Context mContext, ArrayList<MainSliderData> sliderItems, ViewPager2 viewPager2) {
        this.mContext = mContext;
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_slider,parent,false);
        MainSliderAdapter.CustomViewHolder customViewHolder = new MainSliderAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(sliderItems.get(position).productImage)
                .into(holder.imageView);
        holder.textView.setText(sliderItems.get(position).productName);
        if(position == sliderItems.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView8);
            this.textView = itemView.findViewById(R.id.textView117);
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };
}
