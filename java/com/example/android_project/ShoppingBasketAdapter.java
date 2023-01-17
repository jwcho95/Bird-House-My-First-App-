package com.example.android_project;

import android.annotation.SuppressLint;
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

public class ShoppingBasketAdapter extends RecyclerView.Adapter<ShoppingBasketAdapter.CustomViewHolder> {

    private ArrayList<ShoppingBasketData> arrayList;
    private Context context;
    private final int CHECK_CODE = 101;
    boolean check = true;

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff);
    }

    // 리스너 객체 참조를 저장하는 변수
    private ShoppingBasketAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(ShoppingBasketAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public ShoppingBasketAdapter(ArrayList<ShoppingBasketData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_basket,parent,false);
        ShoppingBasketAdapter.CustomViewHolder customViewHolder = new ShoppingBasketAdapter.CustomViewHolder(view);

        return customViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProductImage())
                .into(holder.shoppingBasketImage);
        holder.shoppingBasketName.setText(arrayList.get(position).getProductName());
        holder.shoppingBasketPrice.setText(arrayList.get(position).getProductPrice()+"원");
        holder.shoppingBasketQuantity.setText(arrayList.get(position).getProductQuantity()+"개");
        holder.shoppingBasketCheck.setChecked(true);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView shoppingBasketImage;
        protected TextView shoppingBasketName;
        protected TextView shoppingBasketPrice;
        protected TextView shoppingBasketQuantity;
        protected CheckBox shoppingBasketCheck;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.shoppingBasketImage = (ImageView) itemView.findViewById(R.id.shoppingBasketImage);
            this.shoppingBasketName = (TextView) itemView.findViewById(R.id.shoppingBasketName);
            this.shoppingBasketPrice = (TextView) itemView.findViewById(R.id.shoppingBasketPrice);
            this.shoppingBasketQuantity = (TextView) itemView.findViewById(R.id.shoppingBasketQuantity);
            this.shoppingBasketCheck = (CheckBox) itemView.findViewById(R.id.shoppingBasketCheck);

            shoppingBasketCheck.setOnClickListener(new View.OnClickListener() {
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
