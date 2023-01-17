package com.example.android_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DeliveryListAdapter extends RecyclerView.Adapter<DeliveryListAdapter.CustomViewHolder> {

    ArrayList<DeliveryListData> arrayList;
    Context context;

    final int REFUND_CODE = 1000;
    final int DETAIL_PAGE = 2000;

    public DeliveryListAdapter(ArrayList<DeliveryListData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff);
    }

    // 리스너 객체 참조를 저장하는 변수
    private DeliveryListAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(DeliveryListAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_list,parent,false);
        DeliveryListAdapter.CustomViewHolder customViewHolder = new DeliveryListAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.date.setText(arrayList.get(position).getDate());
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProductImage())
                .into(holder.productImage);
        holder.productName.setText(arrayList.get(position).getProductName());
        holder.productPrice.setText(arrayList.get(position).getProductPrice()+"원");
        if(arrayList.get(position).getItemCount() > 1) {
            holder.itemCount.setText("외 "+(arrayList.get(position).getItemCount() - 1) + "개");
        }else{
            holder.itemCount.setVisibility(View.INVISIBLE);
        }
        holder.deliveryNum.setText(arrayList.get(position).getDeliveryNum());
        holder.deliveryCompany.setText(arrayList.get(position).getDeliveryCompany());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView date;
        TextView productName;
        TextView itemCount;
        TextView productPrice;
        Button cancelDeliveryBtn;
        TextView deliveryNum;
        TextView deliveryCompany;
        TextView goToDetail;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.orderDate);
            this.productImage = itemView.findViewById(R.id.itemImage);
            this.productName = itemView.findViewById(R.id.itemName);
            this.itemCount = itemView.findViewById(R.id.itemCount);
            this.productPrice = itemView.findViewById(R.id.itemPrice);
            this.cancelDeliveryBtn = itemView.findViewById(R.id.cancel_delivery_btn);
            this.deliveryNum = itemView.findViewById(R.id.delivery_number);
            this.deliveryCompany = itemView.findViewById(R.id.delivery_company);
            this.goToDetail = itemView.findViewById(R.id.goToDetail);

            cancelDeliveryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, REFUND_CODE);
                        }
                    }
                }
            });

            goToDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, DETAIL_PAGE);
                        }
                    }
                }
            });
        }
    }
}
