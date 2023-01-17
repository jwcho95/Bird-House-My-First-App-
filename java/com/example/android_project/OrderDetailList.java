package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class OrderDetailList extends AppCompatActivity {

    private ArrayList<OrderDetailListData> arrayList;
    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private LinearLayoutManager layoutManager;
    private OrderDetailListAdapter orderDetailListAdapter;
    private DatabaseReference databaseReference;
    private int sum_price = 0;
    private int delivery = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_detail);

        TextView orderDate = findViewById(R.id.orderDate_orderDetailList); // 주문 날짜
        TextView orderName = findViewById(R.id.orderName_orderDetailList); // 주문자 이름
        TextView orderPhoneNum = findViewById(R.id.orderPhoneNum_orderDetailList); // 주문자 연락처
        TextView destination = findViewById(R.id.destination_orderDetailList); // 배송지
        TextView productPrice = findViewById(R.id.productPrice_OrderDetailList); // 총 내역
        TextView howToPay = findViewById(R.id.howToPay_orderList_detail); // 결제 방법
        TextView bankNumAdmin = findViewById(R.id.textView169);
        TextView bankNumAdmin1 = findViewById(R.id.textView160);


        ImageView backBtn = findViewById(R.id.imageView49);
        ImageView homeBtn = findViewById(R.id.imageView48);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("OrderList_"+getIntent().getStringExtra("memberID")).child(getIntent().getStringExtra("orderDate"));

        orderDate.setText(getIntent().getStringExtra("orderDate"));

        /* 주문하신 물건 리사이클러뷰 */
        recyclerView = findViewById(R.id.recyclerView_order_detail_list);
        arrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(OrderDetailList.this);
        recyclerView.setLayoutManager(layoutManager);
        orderDetailListAdapter = new OrderDetailListAdapter(arrayList, OrderDetailList.this);
        recyclerView.setAdapter(orderDetailListAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    OrderDetailListData orderDetailListData = dataSnapshot.getValue(OrderDetailListData.class);
                    orderName.setText(orderDetailListData.getOrderName());
                    orderPhoneNum.setText(orderDetailListData.getOrderPhoneNum());
                    destination.setText(orderDetailListData.getDestination());
                    howToPay.setText(orderDetailListData.getHowToPay());
                    if(orderDetailListData.getHowToPay().equals("휴대폰소액결제")){
                        bankNumAdmin.setVisibility(View.GONE);
                        bankNumAdmin1.setVisibility(View.GONE);
                    }
                    sum_price += orderDetailListData.getProductPrice() * orderDetailListData.getProductQuantity();
                    arrayList.add(orderDetailListData);
                }
                productPrice.setText(sum_price+"원 + 배송비 "+delivery+"원");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() { // 홈페이지 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent homeBtnIntent = new Intent(OrderDetailList.this, MainActivity.class);
                homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeBtnIntent);
                finish();
            }
        });

    }
}
