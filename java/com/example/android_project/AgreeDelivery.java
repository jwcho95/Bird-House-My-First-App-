package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AgreeDelivery extends AppCompatActivity {
    private AlertDialog dialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_orderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Myapp", getIntent().getStringExtra("memberID"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_delivery);

        ImageView backBtn = findViewById(R.id.imageView4);
        Spinner deliveryCompany = findViewById(R.id.spinner2);
        EditText deliveryNum = findViewById(R.id.editTextNumber);
        Button agreeBtn = findViewById(R.id.button16);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference_orderList = firebaseDatabase.getReference("OrderList_admin");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deliveryNum.getText().toString().equals("")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AgreeDelivery.this);
                    dialog = alertDialog.setMessage("운송 번호를 입력해주세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AgreeDelivery.this);
                    dialog = alertDialog.setMessage("계속 진행하시려면 \"확인\"을 눌러주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference_orderList.child(getIntent().getStringExtra("all_date"))
                                    .child(getIntent().getStringExtra("all_date_detail"))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            Map<String, Object> map_build = new HashMap<>();
                                            map_build.put(task.getResult().getKey(), task.getResult().getValue());
                                            long timeNow = System.currentTimeMillis() + 172800000; // 현재 시간에 2일의 시간을 추가 => 배송 예정 시간
                                            Date date = new Date(timeNow);
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            String timeOfArrival = simpleDateFormat.format(date);
                                            for(String key : map_build.keySet()){
                                                Map<String, Object> map = new HashMap<>();
                                                map = (Map<String, Object>) map_build.get(key);
                                                for(String key_more : map.keySet()){
                                                    Map<String, String> map_more = new HashMap<>();
                                                    map_more = (Map<String, String>) map.get(key_more); // 데이터베이스에 항목을 추가할 수 있는 부분
                                                    map_more.put("timeOfArrival", timeOfArrival);
                                                    map_more.put("deliveryCompany", deliveryCompany.getSelectedItem().toString());
                                                    map_more.put("deliveryNum", deliveryNum.getText().toString());
                                                    map.put(key_more, map_more);
                                                }
                                                map_build.put(key, map);
                                            }
                                            databaseReference.child("Delivery_"+getIntent().getStringExtra("memberID")).updateChildren(map_build); // 유저에게 제공되는 데이터
                                            databaseReference.child("DeliveryList_admin").child(getIntent().getStringExtra("all_date")).updateChildren(map_build); // 관리자에게 제공되는 데이터
                                            databaseReference_orderList.child(getIntent().getStringExtra("all_date")).child(getIntent().getStringExtra("all_date_detail")).setValue(null);
                                            databaseReference.child("OrderList_"+getIntent().getStringExtra("memberID")).child(getIntent().getStringExtra("all_date_detail")).setValue(null);
                                            Toast.makeText(AgreeDelivery.this, "입금 확인 처리되었습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                        }
                    }).setNegativeButton("취소", null).create();
                    dialog.show();
                }
            }
        });


    }
}