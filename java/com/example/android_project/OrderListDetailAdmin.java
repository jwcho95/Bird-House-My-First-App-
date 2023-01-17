package com.example.android_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderListDetailAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<OrderDetailListData> arrayList_order;
    private ArrayList<DeliveryListDetailData> arrayList_delivery;
    private ArrayList<DeliveryListDetailData> arrayList_refund;
    private LinearLayoutManager layoutManager;
    private OrderDetailListAdapter orderDetailListAdapter;
    private DeliveryListDetailAdapter deliveryListDetailAdapter;
    private DeliveryListDetailAdapter refundListAdminAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String reference;
    private int sum_price = 0;
    private int delivery = 2500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_detail_admin);

        if(getIntent().getStringExtra("diff").equals("orderListAdmin")){ // 주문 신청 페이지
            reference = "OrderList_admin";
        }else if(getIntent().getStringExtra("diff").equals("deliveryListAdmin")){ // 배송중인 품목
            reference = "DeliveryList_admin";
        }else if(getIntent().getStringExtra("diff").equals("refundListAdmin")){ // 환불 페이지
            reference = "RefundList_admin";
        }


        ImageView backBtn = findViewById(R.id.imageView5);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(reference);

        TextView orderDate = findViewById(R.id.orderDate_orderDetailList); // 주문 날짜
        TextView orderName = findViewById(R.id.orderName_orderDetailList); // 주문자 이름
        TextView orderPhoneNum = findViewById(R.id.orderPhoneNum_orderDetailList); // 주문자 연락처
        TextView destination = findViewById(R.id.destination_orderDetailList); // 배송지
        TextView productPrice = findViewById(R.id.productPrice_OrderDetailList); // 총 내역
        TextView email = findViewById(R.id.email_orderDetailList); // 이메일
        TextView howToPay = findViewById(R.id.howToPay_admin); // 결제 방법
        TextView bankName = findViewById(R.id.bankName_admin); // 은행 이름
        TextView bankNum = findViewById(R.id.bankNum_admin); // 계좌 번호
        TextView bankName1 = findViewById(R.id.textView247);
        TextView bankNum1 = findViewById(R.id.textView248);
        TextView refundDate = findViewById(R.id.textView164);
        TextView reasonOfRefundSub = findViewById(R.id.textView252);
        TextView reasonOfRefundMain = findViewById(R.id.reasonOfRefund_admin); // 환불 사유

        orderDate.setText(getIntent().getStringExtra("all_date_detail"));

        recyclerView = findViewById(R.id.recyclerView_order_detail_list);
        arrayList_order = new ArrayList<>();
        arrayList_delivery = new ArrayList<>();
        arrayList_refund = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(OrderListDetailAdmin.this);
        recyclerView.setLayoutManager(layoutManager);
        if(getIntent().getStringExtra("diff").equals("orderListAdmin")){ // 주문 신청
            orderDetailListAdapter = new OrderDetailListAdapter(arrayList_order, OrderListDetailAdmin.this);
            recyclerView.setAdapter(orderDetailListAdapter);
        }else if(getIntent().getStringExtra("diff").equals("deliveryListAdmin")){ // 배송중
            deliveryListDetailAdapter = new DeliveryListDetailAdapter(arrayList_delivery, OrderListDetailAdmin.this);
            recyclerView.setAdapter(deliveryListDetailAdapter);
        }else if(getIntent().getStringExtra("diff").equals("refundListAdmin")){ // 환불
            refundListAdminAdapter = new DeliveryListDetailAdapter(arrayList_refund, OrderListDetailAdmin.this);
            recyclerView.setAdapter(refundListAdminAdapter);
            refundDate.setText("환불 신청 날짜: ");
        }

        if(!getIntent().getStringExtra("diff").equals("refundListAdmin")){
            reasonOfRefundMain.setVisibility(View.GONE);
            reasonOfRefundSub.setVisibility(View.GONE);
        }

        databaseReference.child(getIntent().getStringExtra("all_date")).child(getIntent().getStringExtra("all_date_detail")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_order.clear();
                arrayList_delivery.clear();
                arrayList_refund.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(getIntent().getStringExtra("diff").equals("orderListAdmin")){
                        OrderDetailListData orderDetailListData = dataSnapshot.getValue(OrderDetailListData.class);
                        orderName.setText(orderDetailListData.getOrderName());
                        orderPhoneNum.setText(orderDetailListData.getOrderPhoneNum());
                        destination.setText(orderDetailListData.getDestination());
                        email.setText(orderDetailListData.getEmail());
                        howToPay.setText(orderDetailListData.getHowToPay());
                        if(orderDetailListData.getHowToPay().equals("휴대폰소액결제")){
                            bankName.setVisibility(View.GONE);
                            bankNum.setVisibility(View.GONE);
                            bankName1.setVisibility(View.GONE);
                            bankNum1.setVisibility(View.GONE);
                        }
                        bankName.setText(orderDetailListData.getBank());
                        bankNum.setText(orderDetailListData.getBankNum());
                        reasonOfRefundMain.setText(orderDetailListData.getReasonOfRefund());
                        sum_price += orderDetailListData.getProductPrice() * orderDetailListData.getProductQuantity();
                        arrayList_order.add(orderDetailListData);
                    }else if(getIntent().getStringExtra("diff").equals("deliveryListAdmin")){
                        DeliveryListDetailData deliveryListDetailData = dataSnapshot.getValue(DeliveryListDetailData.class);
                        orderName.setText(deliveryListDetailData.getOrderName());
                        orderPhoneNum.setText(deliveryListDetailData.getOrderPhoneNum());
                        destination.setText(deliveryListDetailData.getDestination());
                        email.setText(deliveryListDetailData.getEmail());
                        howToPay.setText(deliveryListDetailData.getHowToPay());
                        if(deliveryListDetailData.getHowToPay().equals("휴대폰소액결제")){
                            bankName.setVisibility(View.GONE);
                            bankNum.setVisibility(View.GONE);
                            bankName1.setVisibility(View.GONE);
                            bankNum1.setVisibility(View.GONE);
                        }
                        bankName.setText(deliveryListDetailData.getBank());
                        bankNum.setText(deliveryListDetailData.getBankNum());
                        reasonOfRefundMain.setText(deliveryListDetailData.getReasonOfRefund());
                        sum_price += deliveryListDetailData.getProductPrice() * deliveryListDetailData.getProductQuantity();
                        arrayList_delivery.add(deliveryListDetailData);
                    }else if(getIntent().getStringExtra("diff").equals("refundListAdmin")){
                        DeliveryListDetailData refundListData = dataSnapshot.getValue(DeliveryListDetailData.class);
                        orderName.setText(refundListData.getOrderName());
                        orderPhoneNum.setText(refundListData.getOrderPhoneNum());
                        destination.setText(refundListData.getDestination());
                        email.setText(refundListData.getEmail());
                        howToPay.setText(refundListData.getHowToPay());
                        if(refundListData.getHowToPay().equals("휴대폰소액결제")){
                            bankName.setVisibility(View.GONE);
                            bankNum.setVisibility(View.GONE);
                            bankName1.setVisibility(View.GONE);
                            bankNum1.setVisibility(View.GONE);
                        }
                        bankName.setText(refundListData.getBank());
                        bankNum.setText(refundListData.getBankNum());
                        reasonOfRefundMain.setText(refundListData.getReasonOfRefund());
                        sum_price += refundListData.getProductPrice() * refundListData.getProductQuantity();
                        arrayList_refund.add(refundListData);
                    }
                }
                productPrice.setText(sum_price+"원 + 배송비 "+delivery+"원");
                if(getIntent().getStringExtra("diff").equals("orderListAdmin")) {
                    orderDetailListAdapter.notifyDataSetChanged();
                }else if(getIntent().getStringExtra("diff").equals("deliveryListAdmin")){
                    deliveryListDetailAdapter.notifyDataSetChanged();
                }else if(getIntent().getStringExtra("diff").equals("refundListAdmin")){
                    refundListAdminAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
}
