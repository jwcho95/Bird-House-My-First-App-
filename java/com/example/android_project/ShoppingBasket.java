package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingBasket extends AppCompatActivity {

    private ArrayList<ShoppingBasketData> arrayList;
    private ShoppingBasketAdapter shoppingBasketAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_order;
    private Dialog dialog;
    int sum_price; // 합계 금액
    int count = 0;
    
    /*정확한 계산 값을 내기 위해 필요한 변수들*/
    private int MaxPrice;
    private boolean checkMaxPrice = false;
    private boolean deleteAllProduct = false;

    private ArrayList<String> deleteArrayList; // 삭제에 필요한 리스트
    private Map<String, ShoppingBasketData> orderList; // 구매에 필요한 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basket);
        Button paymentBtn = (Button) findViewById(R.id.button9); // 구매하기 버튼
        Button deleteBtn = (Button) findViewById(R.id.button8); // 삭제하기 버튼
        TextView printSumPrice = (TextView) findViewById(R.id.textView76); // 합계 금액이 보여지는 곳
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView); // 하단의 탭
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.shopping_car_tab).setChecked(true); // 탭에서 장바구니 화면을 강조 표시를 해줌
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewShoppingBasket);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ShoppingBasket_"+getIntent().getStringExtra("memberID"));
        databaseReference_order = firebaseDatabase.getReference("PaymentList_"+getIntent().getStringExtra("memberID"));

        deleteArrayList = new ArrayList<>();
        orderList = new HashMap<>();

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

                    deleteArrayList.add(arrayList.get(count).getProductName());
                    if(deleteAllProduct == false) {
                        sum_price += arrayList.get(count).getProductPrice() * arrayList.get(count).getProductQuantity();
                    }else{
                        sum_price = 0;
                    }
                    count += 1;
                }

                if(checkMaxPrice == false){
                    MaxPrice = sum_price;
                    checkMaxPrice = true;
                }

                printSumPrice.setText(sum_price+"원");
                shoppingBasketAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("ProductDetailPage", "Failed to read value.", error.toException());
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ShoppingBasket.this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        shoppingBasketAdapter = new ShoppingBasketAdapter(arrayList, ShoppingBasket.this);
        recyclerView.setAdapter(shoppingBasketAdapter);

        shoppingBasketAdapter.setOnItemClickListener(new ShoppingBasketAdapter.OnItemClickListener() { // 리사이클러뷰가 눌렸을 때
            @Override
            public void onItemClick(View v, int pos, int diff) {
                if(diff == 101){ // 체크박스
                    if(!arrayList.get(pos).isCheck()) { // 체크박스를 눌렀을 때

                        ShoppingBasketData orderData = new ShoppingBasketData();
                        orderData.setProductImage(arrayList.get(pos).getProductImage());
                        orderData.setProductName(arrayList.get(pos).getProductName());
                        orderData.setProductPrice(arrayList.get(pos).getProductPrice());
                        orderData.setProductQuantity(arrayList.get(pos).getProductQuantity());
                        orderList.put(arrayList.get(pos).getProductName(), orderData);

                        sum_price += arrayList.get(pos).getProductPrice() * arrayList.get(pos).getProductQuantity();
                        deleteArrayList.add(arrayList.get(pos).getProductName());
                        printSumPrice.setText(sum_price+"원");
                        arrayList.get(pos).setCheck(true);
                    }else{ // 체크박스를 취소했을 때

                        orderList.remove(arrayList.get(pos).getProductName());

                        sum_price -= arrayList.get(pos).getProductPrice() * arrayList.get(pos).getProductQuantity();
                        deleteArrayList.remove(arrayList.get(pos).getProductName());
                        printSumPrice.setText(sum_price+"원");
                        arrayList.get(pos).setCheck(false);
                    }
                }else{
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category_tab:
                        Intent categoryIntent = new Intent(ShoppingBasket.this, Category.class);
                        categoryIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(categoryIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        break;
                    case R.id.search_tab:
                        Intent SearchIntent = new Intent(ShoppingBasket.this, SearchPage.class);
                        SearchIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(SearchIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        break;
                    case R.id.home_tab:
                        Intent homeIntent = new Intent(ShoppingBasket.this, MainActivity.class);
                        homeIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(homeIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        break;
                    case R.id.person_tab:
                        Intent myInformationIntent = new Intent(ShoppingBasket.this, MyInformation.class);
                        myInformationIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(myInformationIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        break;
                    case R.id.shopping_car_tab:
                        break;
                }
                return false;
            }
        });

//        backBtn.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                finish();
//            }
//        });
//
//        homeBtn.setOnClickListener(new View.OnClickListener() { // 홈페이지 버튼
//            @Override
//            public void onClick(View v) {
//                Intent homeBtnIntent = new Intent(ShoppingBasket.this, MainActivity.class);
//                homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
//                homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
//                startActivity(homeBtnIntent);
//                finish();
//            }
//        });

        paymentBtn.setOnClickListener(new View.OnClickListener() { // 구매하기 버튼
            @Override
            public void onClick(View v) {
                if(arrayList.size() == 0){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShoppingBasket.this);
                    dialog = alertDialog.setMessage("구매할 상품이 존재하지 않습니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                }else {
                    Intent paymentIntent = new Intent(ShoppingBasket.this, Payment.class);
                    paymentIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                    paymentIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                    paymentIntent.putExtra("page", "shoppingBasketPage");

                    databaseReference_order.removeValue();

                    Map<String, Object> map = new HashMap<>();
                    for(String key : orderList.keySet()) {
                        map.put("productImage", orderList.get(key).getProductImage());
                        map.put("productName", orderList.get(key).getProductName());
                        map.put("productPrice", orderList.get(key).getProductPrice());
                        map.put("productQuantity", orderList.get(key).getProductQuantity());

                        databaseReference_order.child(orderList.get(key).getProductName()).updateChildren(map);
                    }
                    startActivity(paymentIntent);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() { // 삭제하기 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                for(int i = 0; i < deleteArrayList.size(); i++){
                    firebaseDatabase.getReference("ShoppingBasket_"+getIntent().getStringExtra("memberID")).child(deleteArrayList.get(i)).removeValue();
                }
                if(sum_price == MaxPrice){
                    deleteAllProduct = true;
                }
                deleteArrayList.clear();
                shoppingBasketAdapter.notifyDataSetChanged();
            }
        });
    }
}