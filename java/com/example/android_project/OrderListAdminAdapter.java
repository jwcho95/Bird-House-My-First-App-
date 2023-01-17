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

public class OrderListAdminAdapter extends RecyclerView.Adapter<OrderListAdminAdapter.CustomViewHolder> {

    private ArrayList<OrderListAdminData> arrayList;
    private Context context;

    private final int GO_TO_DETAIL_PAGE = 100;
    private final int AGREE = 200;

    public OrderListAdminAdapter(ArrayList<OrderListAdminData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos, int diff);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OrderListAdminAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OrderListAdminAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list_admin,parent,false);
        OrderListAdminAdapter.CustomViewHolder customViewHolder = new OrderListAdminAdapter.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.Date.setText("주문 날짜: "+arrayList.get(position).getDate());
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getProductImage())
                .into(holder.productImage);
        holder.userID.setText(arrayList.get(position).getMemberID());
        holder.bankName.setText(arrayList.get(position).getBankName());
        holder.bank.setText(arrayList.get(position).getBank());
        holder.bankNum.setText(String.valueOf(arrayList.get(position).getBankNum()));
        holder.productPrice.setText(arrayList.get(position).getProductPrice()+"원");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView Date;
        ImageView productImage;
        TextView userID; // 유저 아이디
        TextView bankName; // 입금자 명
        TextView bank; // 은행 이름
        TextView bankNum; // 계좌 번호
        TextView productPrice; // 가격
        TextView goToDetail; // 주문 상세 정보
        Button agreeDelivery; // 입금확인 버튼

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.Date = itemView.findViewById(R.id.orderDate);
            this.productImage = itemView.findViewById(R.id.productImageAdmin);
            this.userID = itemView.findViewById(R.id.user_ID_admin);
            this.bankName = itemView.findViewById(R.id.buyerName);
            this.bankNum = itemView.findViewById(R.id.bankNum);
            this.bank = itemView.findViewById(R.id.whichBank);
            this.productPrice = itemView.findViewById(R.id.itemPrice);
            this.goToDetail = itemView.findViewById(R.id.goToDetail);
            this.agreeDelivery = itemView.findViewById(R.id.agree_delivery_btn);

            goToDetail.setOnClickListener(new View.OnClickListener() { // 주문 상세 정보 클릭
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

            agreeDelivery.setOnClickListener(new View.OnClickListener() { // 입금확인 버튼
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(v,position, AGREE);
                        }
                    }
                }
            });
        }
    }
}
