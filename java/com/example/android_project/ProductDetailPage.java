package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailPage extends AppCompatActivity {
    private TextView priceText;
    private EditText priceNum;
    int price = 10000; // 상품 가격
    int priceOfProduct = 0; // 상품 합계
    private AlertDialog dialog; // 확인 메세지를 띄워주는 변수
    private AlertDialog dialog_more;

    /*리뷰 리사이클러뷰에 필요한 변수들*/
    private ArrayList<ReviewData> arrayList;
    private ReviewAdapter reviewAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    /*문의사항 리사이클러뷰에 필요한 변수들*/
    private ArrayList<ComplaintData> arrayList_complaint;
    private ComplaintAdapter complaintAdapter;
    private RecyclerView recyclerView_complaint;
    private LinearLayoutManager layoutManager_complaint;
    private FirebaseDatabase firebaseDatabase_complaint;
    private DatabaseReference databaseReference_complaint;

    private DatabaseReference databaseReference_shoppingBasket;
    private DatabaseReference databaseReference_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_page);
        ImageView backBtn = findViewById(R.id.imageView55); // 뒤로가기 버튼
        ImageView homeBtn = findViewById(R.id.imageView54); // 홈페이지 버튼
        TabLayout introduceProductTab = (TabLayout) findViewById(R.id.introduce_product_tab); // 상품 소개 및 리뷰 탭
        Button shoppingBasketBtn = (Button) findViewById(R.id.button2); // 장바구니 버튼
        Button paymentBtn = (Button) findViewById(R.id.button7); // 구매하기 버튼
        priceText = (TextView) findViewById(R.id.textView15); // 상품 합계를 보여주는 뷰
        priceNum = (EditText) findViewById(R.id.number_of_price);
        priceText.setText("0원"); // 가격은 처음에 0원으로 설정
        Button reviewBtn = (Button) findViewById(R.id.button17); // 리뷰쓰기 버튼
        Button complaintBtn = (Button) findViewById(R.id.button20); // 문의하기 버튼
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Review_item_01");
        firebaseDatabase_complaint = FirebaseDatabase.getInstance();
        databaseReference_complaint = firebaseDatabase_complaint.getReference("Complaint_item_01");
        databaseReference_shoppingBasket = firebaseDatabase.getReference("ShoppingBasket_"+getIntent().getStringExtra("memberID"));
        databaseReference_payment = firebaseDatabase.getReference("PaymentList_"+getIntent().getStringExtra("memberID"));


        /* 리뷰에 필요한 리사이클러뷰*/
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ReviewData reviewData = snapshot.getValue(ReviewData.class);
                    arrayList.add(reviewData);
                }
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("ProductDetailPage", "Failed to read value.", error.toException());
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        reviewAdapter = new ReviewAdapter(arrayList, this);
        recyclerView.setAdapter(reviewAdapter);




        /*문의사항 리사이클러뷰*/
        // Read from the database
        databaseReference_complaint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                arrayList_complaint.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ComplaintData complaintData = snapshot.getValue(ComplaintData.class);
                    arrayList_complaint.add(complaintData);
                }
               complaintAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("ProductDetailPage", "Failed to read value.", error.toException());
            }
        });
        recyclerView_complaint = (RecyclerView) findViewById(R.id.recyclerViewComplaint);
        recyclerView_complaint.setHasFixedSize(true);
        layoutManager_complaint = new LinearLayoutManager(ProductDetailPage.this);
        recyclerView_complaint.setLayoutManager(layoutManager_complaint);

        arrayList_complaint = new ArrayList<>();

        complaintAdapter = new ComplaintAdapter(arrayList_complaint, ProductDetailPage.this);
        recyclerView_complaint.setAdapter(complaintAdapter);




        
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
                Intent homeBtnIntent = new Intent(ProductDetailPage.this, MainActivity.class);
                homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeBtnIntent);
                finish();
            }
        });

        shoppingBasketBtn.setOnClickListener(new View.OnClickListener() { // 장바구니 버튼
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);

                if(priceNum.getText().toString().equals("") || priceNum.getText().toString().equals("0")){
                    dialog = alertDialog.setMessage("수량을 적어주세요.").setPositiveButton("확인",null).create();
                    dialog.show();
                }else {
                    dialog = alertDialog.setMessage("장바구니에 추가되었습니다. 확인하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent shoppingBasketIntent = new Intent(ProductDetailPage.this, ShoppingBasket.class);
                            shoppingBasketIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                            shoppingBasketIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));

                            Map<String, Object> map = new HashMap<>();
                            map.put("productImage", "https://firebasestorage.googleapis.com/v0/b/myapplication-76961.appspot.com/o/productImage%2Fseed.jpg?alt=media&token=a122883c-3aea-4419-b1a0-98aebbd4df76");
                            map.put("productName", "해피 좁쌀");
                            map.put("productPrice", 10000);
                            map.put("productQuantity", Integer.parseInt(priceNum.getText().toString()));

                            databaseReference_shoppingBasket.child("해피 좁쌀").updateChildren(map);
                            startActivity(shoppingBasketIntent);
                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("productImage", "https://firebasestorage.googleapis.com/v0/b/myapplication-76961.appspot.com/o/productImage%2Fseed.jpg?alt=media&token=a122883c-3aea-4419-b1a0-98aebbd4df76");
                            map.put("productName", "해피 좁쌀");
                            map.put("productPrice", 10000);
                            map.put("productQuantity", Integer.parseInt(priceNum.getText().toString()));

                            databaseReference_shoppingBasket.child("해피 좁쌀").updateChildren(map);
                        }
                    }).create();
                    dialog.show();
                }
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() { // 구매하기 버튼
            @Override
            public void onClick(View v) {
                if(priceNum.getText().toString().equals("") || priceNum.getText().toString().equals("0")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                    dialog = alertDialog.setMessage("수량을 적어주세요.").setPositiveButton("확인",null).create();
                    dialog.show();
                }else {
                    Intent paymentIntent = new Intent(ProductDetailPage.this, Payment.class);
                    paymentIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                    paymentIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                    paymentIntent.putExtra("page", "productPage");

                    databaseReference_payment.removeValue();
                    Map<String, Object> map = new HashMap<>();
                    map.put("productImage", "https://firebasestorage.googleapis.com/v0/b/myapplication-76961.appspot.com/o/productImage%2Fseed.jpg?alt=media&token=a122883c-3aea-4419-b1a0-98aebbd4df76");
                    map.put("productName", "해피 좁쌀");
                    map.put("productPrice", 10000);
                    map.put("productQuantity", Integer.parseInt(priceNum.getText().toString()));

                    databaseReference_payment.child("해피 좁쌀").updateChildren(map);

                    startActivity(paymentIntent);
                }
            }
        });

        introduceProductTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { // 탭을 눌렀을 때
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeTab(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        priceNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { // 입력하기 전 호출
                priceText.setText("0원");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                
            }

            @Override
            public void afterTextChanged(Editable s) { // 입력한 후 호출
                if(priceNum.getText().toString().length() == 0){
                    priceText.setText("0원");
                }else {
                    priceOfProduct = price * Integer.parseInt(priceNum.getText().toString());
                    priceText.setText(priceOfProduct + "원");
                }
            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener() { // 리뷰쓰기 버튼
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(ProductDetailPage.this, WriteReview.class);
                reviewIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                reviewIntent.putExtra("Review_item", "Review_item_01");
                startActivity(reviewIntent);

//                ReviewData reviewData = new ReviewData(R.drawable.basic_image,getIntent().getStringExtra("memberID"),"Hello world!");
//                arrayList.add(reviewData);
//                reviewAdapter.notifyDataSetChanged();
            }
        });

        complaintBtn.setOnClickListener(new View.OnClickListener() { // 문의하기 버튼
            @Override
            public void onClick(View v) {
                Intent complaintIntent = new Intent(ProductDetailPage.this, WriteComplaint.class);
                complaintIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                complaintIntent.putExtra("Complaint_item", "Complaint_item_01");
                complaintIntent.putExtra("complaintTitle", "");
                complaintIntent.putExtra("complaintContent", "");
                startActivity(complaintIntent);
            }
        });

        reviewAdapter.setOnItemClickListener(new ReviewAdapter.OnItemClickListener() { // 리뷰 알고리즘
            @Override
            public void onItemClick(View v, int pos, int diff, String ID, String Content) {
                Map<String, Object> childUpdate = new HashMap<>();
                if(diff == 1000) { // 리뷰 수정
                    if(getIntent().getStringExtra("memberID").equals(ID)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                        dialog = alertDialog.setMessage("수정하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProductDetailPage.this, WriteReview.class);
                                intent.putExtra("Review_item", "Review_item_01");
                                intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                intent.putExtra("reviewContent", Content);
                                startActivity(intent);
                            }
                        }).setNegativeButton("취소",null).create();
                        dialog.show();
                    }else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                        dialog = alertDialog.setMessage("수정 권한이 없습니다.").setPositiveButton("확인", null).create();
                        dialog.show();
                    }
                }else if(diff == 2000){ // 리뷰 삭제
                    if(getIntent().getStringExtra("memberID").equals(ID)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                        dialog = alertDialog.setMessage("삭제하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseDatabase.getReference("Review_item_01").child("Review_" + getIntent().getStringExtra("memberID")).setValue(null);
                            }
                        }).setNegativeButton("취소",null).create();
                        dialog.show();
                    }else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                        dialog = alertDialog.setMessage("삭제 권한이 없습니다.").setPositiveButton("확인", null).create();
                        dialog.show();
                    }

                    reviewAdapter.notifyDataSetChanged();
                }
            }
        });

        complaintAdapter.setOnItemClickListener(new ComplaintAdapter.OnItemClickListener() { // 문의사항 알고리즘
            @Override
            public void onItemClick(View v, int pos, int diff, String ID, String Content, String Title) {
                Map<String, Object> childUpdate = new HashMap<>();
                if(diff == 1001) { // 문의사항 수정
                    if(getIntent().getStringExtra("memberID").equals(ID)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                        dialog = alertDialog.setMessage("수정하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ProductDetailPage.this, WriteComplaint.class);
                                intent.putExtra("Complaint_item", "Complaint_item_01");
                                intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                intent.putExtra("complaintTitle", Title);
                                intent.putExtra("complaintContent", Content);
                                startActivity(intent);
                            }
                        }).setNegativeButton("취소",null).create();
                        dialog.show();
                    }else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                        dialog = alertDialog.setMessage("수정 권한이 없습니다.").setPositiveButton("확인", null).create();
                        dialog.show();
                    }
                }else if(diff == 2001){ // 문의사항 삭제
                    if(getIntent().getStringExtra("memberID").equals(ID)) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                        dialog = alertDialog.setMessage("삭제하시겠습니까?").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseDatabase_complaint.getReference("Complaint_item_01").child("Complaint_" + getIntent().getStringExtra("memberID")).setValue(null);
                            }
                        }).setNegativeButton("취소",null).create();
                        dialog.show();
                    }else{
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductDetailPage.this);
                        dialog = alertDialog.setMessage("삭제 권한이 없습니다.").setPositiveButton("확인", null).create();
                        dialog.show();
                    }

                    complaintAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void changeTab(int tabNum){
        FrameLayout introduceTab = (FrameLayout) findViewById(R.id.introduce_frame); // 상품 소개
        FrameLayout reviewTab = (FrameLayout) findViewById(R.id.review_frame); // 리뷰
        FrameLayout qnaTab = (FrameLayout) findViewById(R.id.qna_frame); // 상품 문의

        switch (tabNum){
            case 0:
                introduceTab.setVisibility(View.VISIBLE);
                reviewTab.setVisibility(View.INVISIBLE);
                qnaTab.setVisibility(View.INVISIBLE);
                break;
            case 1:
                introduceTab.setVisibility(View.INVISIBLE);
                reviewTab.setVisibility(View.VISIBLE);
                qnaTab.setVisibility(View.INVISIBLE);
                break;
            case 2:
                introduceTab.setVisibility(View.INVISIBLE);
                reviewTab.setVisibility(View.INVISIBLE);
                qnaTab.setVisibility(View.VISIBLE);
                break;
        }
    }
}