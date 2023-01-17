package com.example.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PaymentFinishedPage_PhonePay extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<PaymentFinishedData> arrayList;
    private PaymentFinishedAdapter paymentFinishedAdapter;
    private LinearLayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private int sum_price = 0;
    private int delivery = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_finished_phone_pay);

        TextView price = findViewById(R.id.textView109);
        TextView date = findViewById(R.id.textView111);
        Button okBtn = findViewById(R.id.button4);


        date.setText(getIntent().getStringExtra("Date"));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_payment_finished);
        arrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("OrderList_"+getIntent().getStringExtra("memberID")).child(getIntent().getStringExtra("Date"));

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(PaymentFinishedPage_PhonePay.this);
        recyclerView.setLayoutManager(layoutManager);
        paymentFinishedAdapter = new PaymentFinishedAdapter(arrayList, PaymentFinishedPage_PhonePay.this);
        recyclerView.setAdapter(paymentFinishedAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PaymentFinishedData paymentFinishedData = dataSnapshot.getValue(PaymentFinishedData.class);
                    sum_price += paymentFinishedData.getProductPrice() * paymentFinishedData.getProductQuantity();
                    arrayList.add(paymentFinishedData);
                    paymentFinishedAdapter.notifyDataSetChanged();
                }
                price.setText(sum_price+delivery+"원");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeBtnIntent = new Intent(PaymentFinishedPage_PhonePay.this, MainActivity.class);
                homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeBtnIntent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeBtnIntent = new Intent(PaymentFinishedPage_PhonePay.this, MainActivity.class);
        homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
        homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
        startActivity(homeBtnIntent);
        finish();
    }
}