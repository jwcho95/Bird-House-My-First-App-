package com.example.android_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PaymentFinishedAdapter extends RecyclerView.Adapter<PaymentFinishedAdapter.CustomViewHolder> {

    ArrayList<PaymentFinishedData> arrayList;
    Context context;

    public PaymentFinishedAdapter(ArrayList<PaymentFinishedData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_finished,parent,false);
        PaymentFinishedAdapter.CustomViewHolder customViewHolder = new PaymentFinishedAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProductImage())
                .into(holder.productImage);
        holder.productName.setText(arrayList.get(position).getProductName());
        holder.productPrice.setText(arrayList.get(position).getProductPrice()+"원");
        holder.productQuantity.setText(arrayList.get(position).getProductQuantity()+"개");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productQuantity;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.productImage = itemView.findViewById(R.id.productImagePaymentFinished);
            this.productName = itemView.findViewById(R.id.productNamePaymentFinished);
            this.productPrice = itemView.findViewById(R.id.productPricePaymentFinished);
            this.productQuantity = itemView.findViewById(R.id.productQuantityPaymentFinished);
        }
    }
}
