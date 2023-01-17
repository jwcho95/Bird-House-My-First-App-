package com.example.android_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryRightAdapter extends RecyclerView.Adapter<CategoryRightAdapter.CustomViewHolder> {

    ArrayList<CategoryRightData> arrayList_right;
    Context context;

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    // 리스너 객체 참조를 저장하는 변수
    private CategoryRightAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(CategoryRightAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }


    public CategoryRightAdapter(ArrayList<CategoryRightData> arrayList_right, Context context) {
        this.arrayList_right = arrayList_right;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_right,parent,false);
        CategoryRightAdapter.CustomViewHolder customViewHolder = new CategoryRightAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList_right.get(position).getProductImage())
                .into(holder.productImage);
        holder.productName.setText(arrayList_right.get(position).getProductName());
        holder.productIntroduce.setText(arrayList_right.get(position).getProductIntroduce());
        holder.producePrice.setText(arrayList_right.get(position).getProductPrice() + "원");
    }

    @Override
    public int getItemCount() {
        return arrayList_right.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productIntroduce;
        TextView producePrice;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.productImage = itemView.findViewById(R.id.productImageCategory);
            this.productName = itemView.findViewById(R.id.productNameCategory);
            this.productIntroduce = itemView.findViewById(R.id.productIntroduceCategory);
            this.producePrice = itemView.findViewById(R.id.productPriceCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();

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
