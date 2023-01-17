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

public class OrderDetailListAdapter extends RecyclerView.Adapter<OrderDetailListAdapter.CustomViewHolder> {

    ArrayList<OrderDetailListData> arrayList;
    Context context;

    public OrderDetailListAdapter(ArrayList<OrderDetailListData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_list,parent,false);
        OrderDetailListAdapter.CustomViewHolder customViewHolder = new OrderDetailListAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        Glide.with(holder.itemView)
                .load(arrayList.get(position).productImage)
                .into(holder.productImage);
        holder.productName.setText(arrayList.get(position).productName);
        holder.productPrice.setText(arrayList.get(position).productPrice+"원");
        holder.productQuantity.setText(arrayList.get(position).productQuantity+"개");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private TextView productQuantity;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.productImage = (ImageView) itemView.findViewById(R.id.productImageOrderDetail);
            this.productName = (TextView) itemView.findViewById(R.id.productNameOrderDetail);
            this.productPrice = (TextView) itemView.findViewById(R.id.productPriceOrderDetail);
            this.productQuantity = (TextView) itemView.findViewById(R.id.productQuantityOrderDetail);
        }
    }
}
