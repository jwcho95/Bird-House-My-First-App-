package com.example.android_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryLeftAdapter extends RecyclerView.Adapter<CategoryLeftAdapter.CustomViewHolder> {

    private ArrayList<CategoryLeftData> arrayList_left;
    private Context context;

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    // 리스너 객체 참조를 저장하는 변수
    private CategoryLeftAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(CategoryLeftAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public CategoryLeftAdapter(ArrayList<CategoryLeftData> arrayList, Context context) {
        this.arrayList_left = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_left,parent,false);
        CategoryLeftAdapter.CustomViewHolder customViewHolder = new CategoryLeftAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.Range.setText(arrayList_left.get(position).getRange());
    }

    @Override
    public int getItemCount() {
        return arrayList_left.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView Range;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            Range = itemView.findViewById(R.id.textView60);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position);
                        }
                    }
                }
            });
        }
    }
}
