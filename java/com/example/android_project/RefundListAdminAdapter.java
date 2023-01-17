package com.example.android_project;

import android.content.Context;
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

public class RefundListAdminAdapter extends RecyclerView.Adapter<RefundListAdminAdapter.CustomViewHolder> {

    ArrayList<DeliveryListAdminData> arrayList; // 배달 목록의 데이터와 형태가 같음
    Context context;
    final int GO_TO_DETAIL = 100;
    final int AGREE_REFUND = 200;

    public RefundListAdminAdapter(ArrayList<DeliveryListAdminData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff);
    }

    // 리스너 객체 참조를 저장하는 변수
    private RefundListAdminAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(RefundListAdminAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refund_list_admin,parent,false);
        RefundListAdminAdapter.CustomViewHolder customViewHolder = new RefundListAdminAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.deliveryCompany.setText(arrayList.get(position).getDeliveryCompany());
        holder.deliveryNum.setText(arrayList.get(position).getDeliveryNum());
        holder.orderName.setText(arrayList.get(position).getOrderName());
        holder.orderPhoneNum.setText(arrayList.get(position).getOrderPhoneNum());
        holder.destination.setText(arrayList.get(position).getDestination());
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
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView deliveryCompany;
        TextView deliveryNum;
        TextView orderName;
        TextView orderPhoneNum;
        TextView destination;
        ImageView productImage;
        TextView productName;
        TextView itemCount;
        TextView productPrice;
        TextView goToDetail;
        Button agreeRefund;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.deliveryCompany = itemView.findViewById(R.id.delivery_company);
            this.deliveryNum = itemView.findViewById(R.id.delivery_number);
            this.orderName = itemView.findViewById(R.id.orderName_deliveryList_admin);
            this.orderPhoneNum = itemView.findViewById(R.id.orderPhoneNum_deliveryList_admin);
            this.destination = itemView.findViewById(R.id.destination_deliveryList_admin);
            this.productImage = itemView.findViewById(R.id.itemImage);
            this.productName = itemView.findViewById(R.id.itemName);
            this.itemCount = itemView.findViewById(R.id.itemCount);
            this.productPrice = itemView.findViewById(R.id.itemPrice);
            this.goToDetail = itemView.findViewById(R.id.goToDetail);
            this.agreeRefund = itemView.findViewById(R.id.button31);

            goToDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, GO_TO_DETAIL);
                        }
                    }
                }
            });

            agreeRefund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, AGREE_REFUND);
                        }
                    }
                }
            });
        }
    }
}
