package com.example.android_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.CustomViewHolder> {

    ArrayList<ShoppingBasketData> arrayList; // 데이터 형태가 같아서 ShoppingBasketData를 사용한다.
    Context context;
    private final int CHECK_CODE = 102;

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff);
    }

    // 리스너 객체 참조를 저장하는 변수
    private PaymentAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(PaymentAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public PaymentAdapter(ArrayList<ShoppingBasketData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_basket,parent,false);
        PaymentAdapter.CustomViewHolder customViewHolder = new PaymentAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProductImage())
                .into(holder.paymentImage);
        holder.paymentName.setText(arrayList.get(position).getProductName());
        holder.paymentPrice.setText(arrayList.get(position).getProductPrice()+"원");
        holder.paymentQuantity.setText(arrayList.get(position).getProductQuantity()+"개");
        holder.paymentCheck.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView paymentImage;
        protected TextView paymentName;
        protected TextView paymentPrice;
        protected TextView paymentQuantity;
        protected CheckBox paymentCheck;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.paymentImage = (ImageView) itemView.findViewById(R.id.shoppingBasketImage);
            this.paymentName = (TextView) itemView.findViewById(R.id.shoppingBasketName);
            this.paymentPrice = (TextView) itemView.findViewById(R.id.shoppingBasketPrice);
            this.paymentQuantity = (TextView) itemView.findViewById(R.id.shoppingBasketQuantity);
            this.paymentCheck = (CheckBox) itemView.findViewById(R.id.shoppingBasketCheck);

            paymentCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, CHECK_CODE);
                        }
                    }
                }
            });
        }
    }
}
