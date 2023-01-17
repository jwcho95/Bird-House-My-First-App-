package com.example.android_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class Payment extends AppCompatActivity {
    private ArrayList<ShoppingBasketData> arrayList;
    private RecyclerView recyclerView;
    private PaymentAdapter paymentAdapter;
    private LinearLayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_order;
    private int count = 0;
    private int sum_price = 0;
    private int delivery = 2500;
    private boolean plusDelivery = false;
    private int orderNum = 0;
    private DatabaseReference databaseReference_order_admin; // 관리자가 받는 데이터베이스
    private TextView Destination;
    private TextView Destination_detail;
    private int stuck = 100; // 물건 재고

    /*정확한 계산 값을 내기 위해 필요한 변수들*/
    private int MaxPrice;
    private boolean checkMaxPrice = false;
    private boolean deleteAllProduct = false;

    private ArrayList<String> deleteArrayList; // 삭제에 필요한 리스트
    private Map<String, ShoppingBasketData> orderList; // 구매에 필요한 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

//        BootpayAnalytics.init(this, "60509952d8c1bd002bf4c713");

        ImageView backBtn = (ImageView) findViewById(R.id.imageView44); // 뒤로가기 버튼
        ImageView homeBtn = (ImageView) findViewById(R.id.imageView45); // 홈페이지 버튼
        Button nextPayment = (Button) findViewById(R.id.button3); // 결제하기 버튼
        EditText Name = (EditText) findViewById(R.id.editTextTextPersonName2); // 이름
        EditText PhoneNum = (EditText) findViewById(R.id.editTextPhone); // 전화번호
        Destination = findViewById(R.id.textView265); // 배송지
        Button setDestination = findViewById(R.id.button29); // 주소 검색
        Destination_detail = findViewById(R.id.editTextTextPostalAddress); // 상세 주소
        EditText bankName = (EditText) findViewById(R.id.editTextTextPersonName8); // 입금자명
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress); // 이메일
        CheckBox howToPay = (CheckBox) findViewById(R.id.checkBox6); // 무통장 입금 버튼
        CheckBox bankTransfer = (CheckBox) findViewById(R.id.checkBox3); // 계좌이체 버튼
        TextView printSumPrice = (TextView) findViewById(R.id.textView30); // 합계 금액이 보여지는 곳
        Spinner bank = (Spinner) findViewById(R.id.spinner); // 은행
        EditText bankNum = (EditText) findViewById(R.id.editTextTextPersonName10); // 계좌 번호
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_payment);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("PaymentList_"+getIntent().getStringExtra("memberID"));
        orderList = new HashMap<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Payment", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(getIntent().getStringExtra("page").equals("addressPage")){ // 주소 선택창에서 주소를 선택한 이후
            Name.setText(sharedPreferences.getString("memberName", "")); // 이름
            PhoneNum.setText(sharedPreferences.getString("memberPhoneNum", "")); // 전화번호
            Destination.setText(sharedPreferences.getString("destination", "")); // 배송지
            Destination_detail.setText(sharedPreferences.getString("destination_detail", "")); // 상세 주소
            email.setText(sharedPreferences.getString("email", "")); // 이메일
            bankName.setText(sharedPreferences.getString("orderName", "")); // 입금자 명
            bankNum.setText(sharedPreferences.getString("bankNum", "")); // 계좌번호
            if(Destination.getText().toString().equals("")) {
            }else{
                Destination_detail.requestFocus();
            }
        }


        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                arrayList.clear();
                count = 0;
                sum_price = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ShoppingBasketData shoppingBasketData = snapshot.getValue(ShoppingBasketData.class);
                    arrayList.add(shoppingBasketData);

                    ShoppingBasketData orderData = new ShoppingBasketData();
                    orderData.setProductImage(arrayList.get(count).getProductImage());
                    orderData.setProductName(arrayList.get(count).getProductName());
                    orderData.setProductPrice(arrayList.get(count).getProductPrice());
                    orderData.setProductQuantity(arrayList.get(count).getProductQuantity());
                    orderList.put(arrayList.get(count).getProductName(), orderData);

                    if(deleteAllProduct == false) {
                        sum_price += arrayList.get(count).getProductPrice() * arrayList.get(count).getProductQuantity();
                    }else{
                        sum_price = 0;
                    }
                    count += 1;
                }

                sum_price += delivery; // 배달비 추가
                if(checkMaxPrice == false){
                    MaxPrice = sum_price;
                    checkMaxPrice = true;
                }

                printSumPrice.setText(sum_price+"원");
                paymentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("ProductDetailPage", "Failed to read value.", error.toException());
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Payment.this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        paymentAdapter = new PaymentAdapter(arrayList, Payment.this);
        recyclerView.setAdapter(paymentAdapter);


        paymentAdapter.setOnItemClickListener(new PaymentAdapter.OnItemClickListener() { // 리사이클러뷰를 누를 때
            @Override
            public void onItemClick(View v, int pos, int diff) {
                if(diff == 102){
                    if(!arrayList.get(pos).isCheck()) { // 체크박스를 눌렀을 때

                        ShoppingBasketData orderData = new ShoppingBasketData();
                        orderData.setProductImage(arrayList.get(pos).getProductImage());
                        orderData.setProductName(arrayList.get(pos).getProductName());
                        orderData.setProductPrice(arrayList.get(pos).getProductPrice());
                        orderData.setProductQuantity(arrayList.get(pos).getProductQuantity());
                        orderList.put(arrayList.get(pos).getProductName(), orderData);

                        sum_price += arrayList.get(pos).getProductPrice() * arrayList.get(pos).getProductQuantity();
                        if(plusDelivery == false){
                            sum_price += delivery;
                            plusDelivery = true;
                        }
                        printSumPrice.setText(sum_price+"원");
                        arrayList.get(pos).setCheck(true);
                    }else{ // 체크박스를 취소했을 때

                        orderList.remove(arrayList.get(pos).getProductName());

                        sum_price -= arrayList.get(pos).getProductPrice() * arrayList.get(pos).getProductQuantity();
                        if(sum_price <= delivery){
                            sum_price = 0;
                            plusDelivery = false;
                        }
                        printSumPrice.setText(sum_price+"원");
                        arrayList.get(pos).setCheck(false);
                    }
                }else{

                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼
            @Override
            public void onClick(View v) {
                databaseReference.removeValue();
                onBackPressed();
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() { // 홈페이지 버튼
            @Override
            public void onClick(View v) {
                databaseReference.removeValue();
                Intent homeIntent = new Intent(Payment.this, MainActivity.class);
                homeIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeIntent);
                finish();
            }
        });

        nextPayment.setOnClickListener(new View.OnClickListener() { // 결제하기 버튼
            @Override
            public void onClick(View v) {
                if(Name.getText().toString().equals("") || PhoneNum.getText().toString().equals("") || Destination.getText().toString().equals("")
                        || Destination_detail.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "필수 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(howToPay.isChecked() == false && bankTransfer.isChecked() == false){
                    Toast.makeText(getApplicationContext(), "결제 방식을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    if(howToPay.isChecked()) { // 무통장 입금
                        Intent finishPaymentIntent = new Intent(Payment.this, PaymentFinishedPage.class);
                        finishPaymentIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        finishPaymentIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                        finishPaymentIntent.putExtra("payment", "bank");

                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat simpleDateFormat_admin = new SimpleDateFormat("yyyy-MM-dd");
                        String getTime = simpleDateFormat.format(date);
                        String getTime_admin = simpleDateFormat_admin.format(date);

                        long deliveryDate = now + 604800000; // 입금 만료 날짜
                        Date ReceiveDate = new Date(deliveryDate);
                        SimpleDateFormat simpleDateFormat_receive = new SimpleDateFormat("yyyy/MM/dd");
                        String getReceiveTime = simpleDateFormat_receive.format(ReceiveDate);

                        databaseReference_order = firebaseDatabase.getReference("OrderList_" + getIntent().getStringExtra("memberID")).child(getTime);
                        databaseReference_order_admin = firebaseDatabase.getReference("OrderList_admin").child(getTime_admin).child(getTime);
                        Map<String, Object> map = new HashMap<>();
                        for (String key : orderList.keySet()) {
                            map.put("productImage", orderList.get(key).getProductImage());
                            map.put("productName", orderList.get(key).getProductName());
                            map.put("productPrice", orderList.get(key).getProductPrice());
                            map.put("productQuantity", orderList.get(key).getProductQuantity());
                            map.put("orderName", Name.getText().toString()); // 주문자 이름
                            map.put("orderPhoneNum", PhoneNum.getText().toString()); // 주문자 전화번호
                            map.put("destination", Destination.getText().toString() + " " + Destination_detail.getText().toString()); // 배송지
                            map.put("bankName", bankName.getText().toString()); // 입금자 명
                            map.put("bank", bank.getSelectedItem().toString()); // 은행 이름
                            map.put("email", email.getText().toString()); // 이메일
                            map.put("Date", getTime); // 주문 시간
                            map.put("receiveDate", getReceiveTime); // 입금 만료 날짜
                            map.put("howToPay", "무통장입금");
                            map.put("bankNum", bankNum.getText().toString()); // 계좌 번호
                            map.put("memberID", getIntent().getStringExtra("memberID"));

                            databaseReference_order.child(orderList.get(key).getProductName()).updateChildren(map);
                            databaseReference_order_admin.child(orderList.get(key).getProductName()).updateChildren(map);
                        }

                        finishPaymentIntent.putExtra("Date", getTime);
                        finishPaymentIntent.putExtra("receiveDate", getReceiveTime);
                        startActivity(finishPaymentIntent);
                        finish();
                    }else if(bankTransfer.isChecked()){ // 계좌 이체
                        BootUser bootUser = new BootUser().setPhone(PhoneNum.getText().toString());
                        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

                        Bootpay.init(getFragmentManager())
                                .setApplicationId("60509952d8c1bd002bf4c713") // 해당 프로젝트(안드로이드)의 application id 값
                                .setPG(PG.KCP) // 결제할 PG 사
                                .setMethod(Method.PHONE) // 결제수단
                                .setContext(Payment.this)
                                .setBootUser(bootUser)
                                .setBootExtra(bootExtra)
                                .setUX(UX.PG_DIALOG)
//                                .setUserPhone("010-1234-5678") // 구매자 전화번호
                                .setName("버드하우스 상품") // 결제할 상품명
                                .setOrderId("1234") // 결제 고유번호
                                .setPrice(sum_price) // 결제할 금액
//                                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
//                                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                                    @Override
                                    public void onConfirm(@Nullable String message) {

                                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                                        Log.d("confirm", message);
                                    }
                                })
                                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                                    @Override
                                    public void onDone(@Nullable String message) {
                                        Intent finishPaymentIntent = new Intent(Payment.this, PaymentFinishedPage_PhonePay.class);
                                        finishPaymentIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                        finishPaymentIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                                        finishPaymentIntent.putExtra("payment", "kakao");

                                        long now = System.currentTimeMillis();
                                        Date date = new Date(now);
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        SimpleDateFormat simpleDateFormat_admin = new SimpleDateFormat("yyyy-MM-dd");
                                        String getTime = simpleDateFormat.format(date);
                                        String getTime_admin = simpleDateFormat_admin.format(date);

                                        long deliveryDate = now + 604800000; // 입금 만료 날짜
                                        Date ReceiveDate = new Date(deliveryDate);
                                        SimpleDateFormat simpleDateFormat_receive = new SimpleDateFormat("yyyy/MM/dd");
                                        String getReceiveTime = simpleDateFormat_receive.format(ReceiveDate);

                                        databaseReference_order = firebaseDatabase.getReference("OrderList_" + getIntent().getStringExtra("memberID")).child(getTime);
                                        databaseReference_order_admin = firebaseDatabase.getReference("OrderList_admin").child(getTime_admin).child(getTime);
                                        Map<String, Object> map = new HashMap<>();
                                        for (String key : orderList.keySet()) {
                                            map.put("productImage", orderList.get(key).getProductImage());
                                            map.put("productName", orderList.get(key).getProductName());
                                            map.put("productPrice", orderList.get(key).getProductPrice());
                                            map.put("productQuantity", orderList.get(key).getProductQuantity());
                                            map.put("orderName", Name.getText().toString()); // 주문자 이름
                                            map.put("orderPhoneNum", PhoneNum.getText().toString()); // 주문자 전화번호
                                            map.put("destination", Destination.getText().toString() + " " + Destination_detail.getText().toString()); // 배송지
                                            map.put("bankName", ""); // 입금자 명
                                            map.put("bank", ""); // 은행 이름
                                            map.put("email", email.getText().toString()); // 이메일
                                            map.put("Date", getTime); // 주문 시간
                                            map.put("receiveDate", ""); // 입금 만료 날짜
                                            map.put("howToPay", "휴대폰소액결제");
                                            map.put("bankNum", ""); // 계좌 번호
                                            map.put("memberID", getIntent().getStringExtra("memberID"));

                                            databaseReference_order.child(orderList.get(key).getProductName()).updateChildren(map);
                                            databaseReference_order_admin.child(orderList.get(key).getProductName()).updateChildren(map);
                                        }

                                        finishPaymentIntent.putExtra("Date", getTime);
                                        finishPaymentIntent.putExtra("receiveDate", getReceiveTime);
                                        startActivity(finishPaymentIntent);
                                        finish();
                                    }
                                })
                                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                                    @Override
                                    public void onReady(@Nullable String message) {
                                        Log.d("ready", message);
                                    }
                                })
                                .onCancel(new CancelListener() { // 결제 취소시 호출
                                    @Override
                                    public void onCancel(@Nullable String message) {

                                        Log.d("cancel", message);
                                    }
                                })
                                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                                    @Override
                                    public void onError(@Nullable String message) {
                                        Log.d("error", message);
                                    }
                                })
                                .onClose(
                                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                                            @Override
                                            public void onClose(String message) {

                                            }
                                        })
                                .request();
                    }
                }
            }
        });

        setDestination.setOnClickListener(new View.OnClickListener() { // 주소 검색을 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, FindAddressActivity.class);
                intent.putExtra("memberID", getIntent().getStringExtra("memberID"));

                editor.putString("memberName", Name.getText().toString()); // 이름
                editor.putString("memberPhoneNum", PhoneNum.getText().toString()); // 전화번호
                editor.putString("destination", Destination.getText().toString()); // 배송지
                editor.putString("destination_detail", Destination_detail.getText().toString()); // 상세 주소
                editor.putString("email", email.getText().toString()); // 이메일
                editor.putString("orderName", bankName.getText().toString()); // 입금자 명
                editor.putString("bankNum", bankNum.getText().toString()); // 계좌번호
                editor.commit();

                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        CheckBox howToPay = (CheckBox) findViewById(R.id.checkBox6); // 무통장 입금 버튼
        CheckBox bankTransfer = (CheckBox) findViewById(R.id.checkBox3); // 계좌이체 버튼
        EditText bankName = (EditText) findViewById(R.id.editTextTextPersonName8); // 입금자명
        EditText bankNum = (EditText) findViewById(R.id.editTextTextPersonName10); // 계좌 번호
        TextView bankNameText = (TextView) findViewById(R.id.textView38);
        TextView bankNumText = (TextView) findViewById(R.id.textView84);
        TextView bankText = (TextView) findViewById(R.id.textView39);
        Spinner bank = (Spinner) findViewById(R.id.spinner);


        bankNameText.setVisibility(View.GONE);
        bankNumText.setVisibility(View.GONE);
        bankText.setVisibility(View.GONE);
        bankName.setVisibility(View.GONE);
        bankNum.setVisibility(View.GONE);
        bank.setVisibility(View.GONE);

        howToPay.setOnClickListener(new View.OnClickListener() { // 무통장 입금 체크에 따른 효과
            @Override
            public void onClick(View v) {
                if(howToPay.isChecked()){
                    bankTransfer.setChecked(false);
                    howToPay.setChecked(true);
                    bankName.setVisibility(View.VISIBLE);
                    bankNum.setVisibility(View.VISIBLE);
                    bank.setVisibility(View.VISIBLE);
                    bankText.setVisibility(View.VISIBLE);
                    bankNameText.setVisibility(View.VISIBLE);
                    bankNumText.setVisibility(View.VISIBLE);
                }else{
                    howToPay.setChecked(false);
                    bankName.setVisibility(View.GONE);
                    bankNum.setVisibility(View.GONE);
                    bank.setVisibility(View.GONE);
                    bankNameText.setVisibility(View.GONE);
                    bankNumText.setVisibility(View.GONE);
                    bankText.setVisibility(View.GONE);
                }
            }
        });

        bankTransfer.setOnClickListener(new View.OnClickListener() { // 계좌이체 버튼 체크에 따른 효과
            @Override
            public void onClick(View v) {
                if(bankTransfer.isChecked()){
                    bankTransfer.setChecked(true);
                    howToPay.setChecked(false);
                    bankName.setVisibility(View.GONE);
                    bankNum.setVisibility(View.GONE);
                    bank.setVisibility(View.GONE);
                    bankNameText.setVisibility(View.GONE);
                    bankNumText.setVisibility(View.GONE);
                    bankText.setVisibility(View.GONE);
                }else{
                    bankTransfer.setChecked(false);
                }
            }
        });
    }
}