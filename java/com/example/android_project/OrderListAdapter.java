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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.CustomViewHolder> {

    ArrayList<OrderListData> arrayList;
    Context context;

    private final int CANCEL_ORDER_CODE = 110; // 주문 취소 버튼
    private final int GO_TO_DETAIL_PAGE = 210; // 주문 상세 보기

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OrderListAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OrderListAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public OrderListAdapter(ArrayList<OrderListData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list,parent,false);
        OrderListAdapter.CustomViewHolder customViewHolder = new OrderListAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if(arrayList.get(position).getHowToPay().equals("휴대폰소액결제")){
            holder.date.setText("배송 준비중입니다.");
        }else {
            holder.date.setText(arrayList.get(position).getReceiveDate() + "까지 입금하셔야 합니다.");
        }
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getItemImage())
                .into(holder.itemImage);
        holder.itemName.setText(arrayList.get(position).getItemName());
        holder.itemPrice.setText(arrayList.get(position).getItemPrice()+"원");
        if(arrayList.get(position).getItemQuantity() > 1) {
            holder.itemQuantity.setText("외 "+(arrayList.get(position).getItemQuantity() - 1) + "개");
        }else{
            holder.itemQuantity.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView date;
        protected TextView goToDetailPage;
        protected ImageView itemImage;
        protected TextView itemName;
        protected TextView itemPrice;
        protected TextView itemQuantity;
        protected Button cancelBtn;
        protected CardView cardView;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.date = (TextView) itemView.findViewById(R.id.orderDate);
            this.goToDetailPage = (TextView) itemView.findViewById(R.id.goToDetail);
            this.itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
            this.itemName = (TextView) itemView.findViewById(R.id.itemName);
            this.itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            this.itemQuantity = (TextView) itemView.findViewById(R.id.itemQuantity);
            this.cancelBtn = (Button) itemView.findViewById(R.id.cancel_order_btn);

            goToDetailPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, GO_TO_DETAIL_PAGE);
                        }
                    }
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, CANCEL_ORDER_CODE);
                        }
                    }
                }
            });
        }
    }
}
