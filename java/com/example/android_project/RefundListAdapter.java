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

public class RefundListAdapter extends RecyclerView.Adapter<RefundListAdapter.CustomViewHolder> {

    ArrayList<RefundListData> arrayList;
    Context context;
    private final int GO_TO_DETAIL = 100;

    public RefundListAdapter(ArrayList<RefundListData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff);
    }

    // 리스너 객체 참조를 저장하는 변수
    private RefundListAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(RefundListAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refund_list,parent,false);
        RefundListAdapter.CustomViewHolder customViewHolder = new RefundListAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.refundDate.setText(arrayList.get(position).getRefundDate());
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
        holder.state.setText("< "+arrayList.get(position).getState()+" >");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView refundDate;
        TextView productName;
        TextView itemCount;
        TextView productPrice;
        TextView deliveryNum;
        TextView deliveryCompany;
        TextView goToDetail;
        TextView state;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.refundDate = itemView.findViewById(R.id.orderDate);
            this.productImage = itemView.findViewById(R.id.itemImage);
            this.productName = itemView.findViewById(R.id.itemName);
            this.itemCount = itemView.findViewById(R.id.itemCount);
            this.productPrice = itemView.findViewById(R.id.itemPrice);
            this.deliveryNum = itemView.findViewById(R.id.delivery_number);
            this.deliveryCompany = itemView.findViewById(R.id.delivery_company);
            this.goToDetail = itemView.findViewById(R.id.goToDetail);
            this.state = itemView.findViewById(R.id.state_refund);

            goToDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, GO_TO_DETAIL);
                        }
                    }
                }
            });
        }
    }
}
