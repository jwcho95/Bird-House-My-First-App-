package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

public class DeliveryListDetail extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ArrayList<DeliveryListDetailData> arrayList;
    private LinearLayoutManager layoutManager;
    private DeliveryListDetailAdapter deliveryListDetailAdapter;
    private int sum_price = 0;
    private int delivery = 2500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list_detail);

        ImageView backBtn = findViewById(R.id.imageView50);
        ImageView homeBtn = findViewById(R.id.imageView51);

        TextView orderDate = findViewById(R.id.orderDate_orderDetailList); // 주문 날짜
        TextView orderName = findViewById(R.id.orderName_orderDetailList); // 주문자 이름
        TextView orderPhoneNum = findViewById(R.id.orderPhoneNum_orderDetailList); // 주문자 연락처
        TextView destination = findViewById(R.id.destination_orderDetailList); // 배송지
        TextView productPrice = findViewById(R.id.productPrice_OrderDetailList); // 총 내역
        TextView deliveryCompany = findViewById(R.id.deliveryCompany_deliveryListDetail); // 택배사
        TextView deliveryNum = findViewById(R.id.deliveryNum_deliveryListDetail); // 운송장 번호
        TextView refundInformation = findViewById(R.id.textView13);
        TextView refundText = findViewById(R.id.textView164);
        TextView howToPay = findViewById(R.id.textView163);

        firebaseDatabase = FirebaseDatabase.getInstance();
        if(getIntent().getStringExtra("page").equals("refundPage")){
            databaseReference = firebaseDatabase.getReference("RefundList_"+getIntent().getStringExtra("memberID")).child(getIntent().getStringExtra("orderDate"));
        }else {
            databaseReference = firebaseDatabase.getReference("Delivery_" + getIntent().getStringExtra("memberID")).child(getIntent().getStringExtra("orderDate"));
        }
        orderDate.setText(getIntent().getStringExtra("orderDate"));

        if(getIntent().getStringExtra("page").equals("refundPage")){
            refundInformation.setText("환불 및 반품 정보");
            refundText.setText("환불 신청 날짜: ");
        }

        /* 주문하신 상품 리스트 */
        recyclerView = findViewById(R.id.recyclerView_delivery_detail_list);
        arrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(DeliveryListDetail.this);
        recyclerView.setLayoutManager(layoutManager);
        deliveryListDetailAdapter = new DeliveryListDetailAdapter(arrayList, DeliveryListDetail.this);
        recyclerView.setAdapter(deliveryListDetailAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DeliveryListDetailData orderDetailListData = dataSnapshot.getValue(DeliveryListDetailData.class);
                    orderName.setText(orderDetailListData.getOrderName());
                    orderPhoneNum.setText(orderDetailListData.getOrderPhoneNum());
                    destination.setText(orderDetailListData.getDestination());
                    deliveryCompany.setText(orderDetailListData.getDeliveryCompany());
                    deliveryNum.setText(orderDetailListData.getDeliveryNum());
                    howToPay.setText(orderDetailListData.getHowToPay());
                    sum_price += orderDetailListData.getProductPrice() * orderDetailListData.getProductQuantity();
                    arrayList.add(orderDetailListData);
                }
                productPrice.setText(sum_price+"원 + 배송비 "+delivery+"원");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() { // 홈페이지 버튼
            @Override
            public void onClick(View v) {
                Intent homeBtnIntent = new Intent(DeliveryListDetail.this, MainActivity.class);
                homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeBtnIntent);
                finish();
            }
        });
    }
}
