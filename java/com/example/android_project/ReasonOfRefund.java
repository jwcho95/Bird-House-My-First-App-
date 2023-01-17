package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReasonOfRefund extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private OrderDetailListAdapter orderDetailListAdapter;
    private ArrayList<OrderDetailListData> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private AlertDialog dialog;
    private AlertDialog dialog_more;
    private long longTime_delivery;
    private int delivery = 2500;
    private int sum_price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reason_of_refund);

        ImageView backBtn = findViewById(R.id.imageView52);
        ImageView homeBtn = findViewById(R.id.imageView53);
        TextView date = findViewById(R.id.date_refund); // 주문일
        TextView price = findViewById(R.id.sumPrice_reason); // 합계 금액
        Button refundBtn = findViewById(R.id.button32);
        Spinner reasonSub = findViewById(R.id.spinner4); // 환불 사유
        EditText reasonMain = findViewById(R.id.editTextTextMultiLine3); // 상세 사유


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 시간을 없애고 날짜만 추출
        try {
            longTime_delivery = simpleDateFormat.parse(getIntent().getStringExtra("orderDate")).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            String newDate = simpleDateFormat1.format(longTime_delivery);
        date.setText(newDate); // 주문일 설정

        recyclerView = findViewById(R.id.recyclerView2);
        arrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(ReasonOfRefund.this);
        orderDetailListAdapter = new OrderDetailListAdapter(arrayList, ReasonOfRefund.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderDetailListAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Delivery_"+getIntent().getStringExtra("memberID"))
                .child(getIntent().getStringExtra("orderDate"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            OrderDetailListData refundList = dataSnapshot.getValue(OrderDetailListData.class);
                            arrayList.add(refundList);
                            sum_price += refundList.getProductPrice() * refundList.getProductQuantity();
                        }
                        price.setText((sum_price + delivery) + "원");
                        orderDetailListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        refundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReasonOfRefund.this);
                dialog = alertDialog.setMessage("신청하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("Delivery_"+getIntent().getStringExtra("memberID"))
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String getTime_more = "";
                                        String simpleDate = "";
                                        Map<String, Object> map = new HashMap<>();
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            map = (Map<String, Object>) dataSnapshot.getValue();
                                            for(String key : map.keySet()){
                                                Map<String, Object> map_more = new HashMap<>();
                                                map_more = (Map<String, Object>) map.get(key);

                                                long time = System.currentTimeMillis();
                                                Date mDate = new Date(time);
                                                SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                                                SimpleDateFormat dateFormat_simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                                                getTime_more = mSimpleDateFormat.format(mDate);
                                                String getTime = dateFormat.format(mDate);
                                                simpleDate = dateFormat_simpleDate.format(mDate);

                                                map_more.put("reasonOfRefund", reasonSub.getSelectedItem().toString()+"\n▶ "+reasonMain.getText().toString());
                                                map_more.put("refundDate", getTime);
                                                map_more.put("refundDateDetail", getTime_more);
                                                map_more.put("state", "환불 처리 신청");
                                                map.put(key, map_more);
                                            }
                                        }
                                        databaseReference.child("RefundList_admin").child(simpleDate).child(getTime_more).updateChildren(map);
                                        databaseReference.child("RefundList_"+getIntent().getStringExtra("memberID")).child(getTime_more).updateChildren(map);
                                        databaseReference.child("Delivery_"+getIntent().getStringExtra("memberID")).child(getIntent().getStringExtra("orderDate")).setValue(null);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                        AlertDialog.Builder alertDialog_more = new AlertDialog.Builder(ReasonOfRefund.this);
                        dialog_more = alertDialog_more.setMessage("환불 확인 및 처리가 되면 주문하신 계좌로 2~3일 후에 입금됩니다. 은행의 사정에 따라 더 늦어질 수도 있습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create();
                        dialog_more.show();
                    }
                }).setNegativeButton("아니오", null).create();
                dialog.show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() { // 홈 버튼
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(ReasonOfRefund.this, MainActivity.class);
                homeIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeIntent);
                finish();
            }
        });

    }
}