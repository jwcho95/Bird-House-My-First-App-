package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.internal.DiskLruCache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.lang.String.valueOf;

public class AdminPage extends AppCompatActivity {

    private AlertDialog dialog;
    private RecyclerView recyclerView_orderList;
    private LinearLayoutManager layoutManager;
    private OrderListAdminAdapter orderListAdminAdapter;
    private ArrayList<OrderListAdminData> arrayList_orderList;
    private FirebaseDatabase firebaseDatabase_orderList;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference_orderList;
    private OrderListAdminData orderListAdminData;
    private int all_price = 0;
    private int delivery = 2500;
    private final int GO_TO_DETAIL = 100;
    private final int AGREE_DELIVERY = 200;
    private ArrayList<String> all_date; // 날짜 저장
    private ArrayList<String> all_date_detail; // 시간 저장
    private ArrayList<String> userIDs; // 주문한 사람들 아이디
    private ArrayList<String> userIDs_delivery;
    private ArrayList<String> all_date_delivery;
    private ArrayList<String> all_date_detail_delivery;
    private ArrayList<String> all_date_refund;
    private ArrayList<String> all_date_detail_refund;
    private ArrayList<String> userIDs_refund;

    private RecyclerView recyclerView_deliveryList;
    private RecyclerView recyclerView_refundList;
    private RefundListAdminAdapter refundListAdminAdapter;
    private DatabaseReference databaseReference_refundList;
    private ArrayList<DeliveryListAdminData> arrayList_refundList;
    private LinearLayoutManager layoutManager_refundList;
    private DeliveryListAdminAdapter deliveryListAdminAdapter;
    private LinearLayoutManager layoutManager_deliveryList;
    private ArrayList<DeliveryListAdminData> arrayList_deliveryList;
    private DatabaseReference databaseReference_delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        SharedPreferences sharedPreferences = getSharedPreferences("MyKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ImageView backBtn = findViewById(R.id.imageView3);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        all_date = new ArrayList<>();
        all_date_detail = new ArrayList<>();
        userIDs = new ArrayList<>();
        userIDs_delivery = new ArrayList<>();
        all_date_delivery = new ArrayList<>();
        all_date_detail_delivery = new ArrayList<>();
        all_date_refund = new ArrayList<>();
        all_date_detail_refund = new ArrayList<>();
        userIDs_refund = new ArrayList<>();
        firebaseDatabase_orderList = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase_orderList.getReference();

        /* 주문 신청 리사이클러뷰 */
        databaseReference_orderList = firebaseDatabase_orderList.getReference("OrderList_admin");
        recyclerView_orderList = findViewById(R.id.recyclerView_orderList_admin);
        arrayList_orderList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(AdminPage.this);
        orderListAdminAdapter = new OrderListAdminAdapter(arrayList_orderList, AdminPage.this);

        recyclerView_orderList.setHasFixedSize(true);
        recyclerView_orderList.setLayoutManager(layoutManager);
        recyclerView_orderList.setAdapter(orderListAdminAdapter);



        /* 배송중인 상품 리사이클러뷰 */
        recyclerView_deliveryList = findViewById(R.id.recyclerView_deliveryList_admin);
        arrayList_deliveryList = new ArrayList<>();
        deliveryListAdminAdapter = new DeliveryListAdminAdapter(arrayList_deliveryList, AdminPage.this);
        layoutManager_deliveryList = new LinearLayoutManager(AdminPage.this);
        recyclerView_deliveryList.setHasFixedSize(true);
        recyclerView_deliveryList.setLayoutManager(layoutManager_deliveryList);
        recyclerView_deliveryList.setAdapter(deliveryListAdminAdapter);
        databaseReference_delivery = firebaseDatabase_orderList.getReference("DeliveryList_admin");




        /* 환불 리스트 리사이클러뷰 */
        arrayList_refundList = new ArrayList<>();
        recyclerView_refundList = findViewById(R.id.recyclerView_refundList_admin);
        layoutManager_refundList = new LinearLayoutManager(AdminPage.this);
        refundListAdminAdapter = new RefundListAdminAdapter(arrayList_refundList, AdminPage.this);
        recyclerView_refundList.setHasFixedSize(true);
        recyclerView_refundList.setAdapter(refundListAdminAdapter);
        recyclerView_refundList.setLayoutManager(layoutManager_refundList);
        databaseReference_refundList = firebaseDatabase_orderList.getReference("RefundList_admin");





        backBtn.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPage.this);
                dialog = alertDialog.setMessage("나가시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putBoolean("returnToLogin", true);
                                editor.commit();
                                Intent logoutIntent = new Intent(AdminPage.this, LoginImage.class);
                                startActivity(logoutIntent);
                                finish();
                            }
                        }).setNegativeButton("아니오", null).create();
                dialog.show();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { // 탭을 눌렀을 때
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


        /* 주문 신청 리스트 */
        databaseReference_orderList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderListAdminAdapter.notifyDataSetChanged();
                arrayList_orderList.clear();
                all_date.clear();
                all_date_detail.clear();
                userIDs.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String Key = dataSnapshot.getKey(); // 주문한 날짜
                    Map<String, Object> map = new HashMap<>();
                    map = (Map<String, Object>) dataSnapshot.getValue();

                    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(map); // 키 값을 정렬 시키기 위해 추가
                    Set<String> map_key = treeMap.keySet();

                    for(String key : map_key){ // 주문한 시간
                        all_date.add(Key);
                        all_date_detail.add(key);
                        orderListAdminData = new OrderListAdminData();
                        orderListAdminData.setDate(key);
                        Map<String, Object> map_more = (Map<String, Object>) map.get(key);
                        for(String key_more : map_more.keySet()){ // 주문한 품목
                            Map<String, String> stringStringMap = new HashMap<>();
                            stringStringMap = (Map<String, String>) map_more.get(key_more);
                            orderListAdminData.setMemberID(((Map<String, String>) map_more.get(key_more)).get("memberID"));
                            orderListAdminData.setProductImage(((Map<String, String>) map_more.get(key_more)).get("productImage"));
                            orderListAdminData.setBankName(((Map<String, String>) map_more.get(key_more)).get("bankName")); // 입금자
                            orderListAdminData.setBank(((Map<String, String>) map_more.get(key_more)).get("bank")); // 은행
                            orderListAdminData.setBankNum(((Map<String, String>) map_more.get(key_more)).get("bankNum"));
                            int price = Integer.parseInt(valueOf(((Map<String, String>) map_more.get(key_more)).get("productPrice")));
                            int quantity = Integer.parseInt(valueOf(((Map<String, String>) map_more.get(key_more)).get("productQuantity")));
                            all_price += price * quantity;
                        }
                        userIDs.add(orderListAdminData.memberID);
                        orderListAdminData.setProductPrice(all_price+delivery);
                        arrayList_orderList.add(orderListAdminData);
                        orderListAdminAdapter.notifyDataSetChanged();
                        all_price = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        orderListAdminAdapter.setOnItemClickListener(new OrderListAdminAdapter.OnItemClickListener() { // 주문 신청 리스트 클릭 이벤트
            @Override
            public void onItemClick(View v, int pos, int diff) {
                if(diff == GO_TO_DETAIL){ // 주문 상세 페이지 버튼
                    Intent goToDetail = new Intent(AdminPage.this, OrderListDetailAdmin.class);
                    goToDetail.putExtra("memberID", userIDs.get(pos));
                    goToDetail.putExtra("all_date", all_date.get(pos));
                    goToDetail.putExtra("all_date_detail", all_date_detail.get(pos));
                    goToDetail.putExtra("diff", "orderListAdmin"); // 주문 상세 페이지 이동 시 어떤 페이지에서 이동했는지 구분하기 위해 추가
                    startActivity(goToDetail);
                }else if(diff == AGREE_DELIVERY){ // 입금확인 버튼
                    Intent agreeIntent = new Intent(AdminPage.this, AgreeDelivery.class);
                    agreeIntent.putExtra("memberID", userIDs.get(pos));
                    agreeIntent.putExtra("all_date", all_date.get(pos));
                    agreeIntent.putExtra("all_date_detail", all_date_detail.get(pos));
                    startActivity(agreeIntent);
                }
            }
        });


        databaseReference_delivery.addValueEventListener(new ValueEventListener() { // 배송중인 상품 리스트 리사이클러뷰
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_deliveryList.clear();
                all_date_delivery.clear();
                all_date_detail_delivery.clear();
                userIDs_delivery.clear();
                int sum_price = 0;
                String Id = "";

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Map<String, Object> map = new HashMap<>();
                    map = (Map<String, Object>) dataSnapshot.getValue();

                    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(map); // 키 값을 정렬 시키기 위해 추가
                    Set<String> map_key = treeMap.keySet();

                    all_date_delivery.add(dataSnapshot.getKey());
                    for(String key : map_key){
                        Map<String, Object> map_more = new HashMap<>();
                        map_more = (Map<String, Object>) map.get(key);

                        all_date_detail_delivery.add(key);
                        DeliveryListAdminData deliveryListAdminData = new DeliveryListAdminData();

                        for(String key_more : map_more.keySet()){
                            Map<String, String> stringStringMap = new HashMap<>();
                            stringStringMap = (Map<String, String>) map_more.get(key_more);
                            Id = stringStringMap.get("memberID");
                            deliveryListAdminData.setDeliveryCompany(stringStringMap.get("deliveryCompany"));
                            deliveryListAdminData.setDeliveryNum(stringStringMap.get("deliveryNum"));
                            deliveryListAdminData.setOrderName(stringStringMap.get("orderName"));
                            deliveryListAdminData.setOrderPhoneNum(stringStringMap.get("orderPhoneNum"));
                            deliveryListAdminData.setDestination(stringStringMap.get("destination"));
                            deliveryListAdminData.setProductImage(stringStringMap.get("productImage"));
                            deliveryListAdminData.setProductName(stringStringMap.get("productName"));
                            sum_price += Integer.parseInt(String.valueOf(stringStringMap.get("productPrice"))) * Integer.parseInt(String.valueOf(stringStringMap.get("productQuantity")));
                        }
                        userIDs_delivery.add(Id);
                        deliveryListAdminData.setItemCount(map_more.size());
                        deliveryListAdminData.setProductPrice(sum_price+delivery);
                        arrayList_deliveryList.add(deliveryListAdminData);
                        sum_price = 0;
                    }
                }
                deliveryListAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        deliveryListAdminAdapter.setOnItemClickListener(new DeliveryListAdminAdapter.OnItemClickListener() { // 배송중인 상품 리사이클러뷰를 클릭했을 때
            @Override
            public void onItemClick(View v, int pos, int diff) {
                if(diff == GO_TO_DETAIL){ // 주문 상세 내역을 눌렀을 때
                    Intent goToDetail = new Intent(AdminPage.this, OrderListDetailAdmin.class);
                    goToDetail.putExtra("memberID", userIDs_delivery.get(pos));
                    String newDate = "";
                    try {
                        String oldDate = all_date_detail_delivery.get(pos);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date date = simpleDateFormat.parse(oldDate);
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                        newDate = simpleDateFormat1.format(date);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    goToDetail.putExtra("all_date", newDate);
                    goToDetail.putExtra("all_date_detail", all_date_detail_delivery.get(pos));
                    goToDetail.putExtra("diff", "deliveryListAdmin"); // 주문 상세 페이지 이동 시 어떤 페이지에서 이동했는지 구분하기 위해 추가
                    startActivity(goToDetail);
                }
            }
        });


        databaseReference_refundList.addValueEventListener(new ValueEventListener() { // 환불중인 상품 리스트 리사이클러뷰
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_refundList.clear();
                all_date_refund.clear();
                all_date_detail_refund.clear();
                userIDs_refund.clear();
                int sum_price = 0;
                String Id = "";

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Map<String, Object> map = new HashMap<>();
                    map = (Map<String, Object>) dataSnapshot.getValue();

                    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(map); // 키 값을 정렬 시키기 위해 추가
                    Set<String> map_key = treeMap.keySet();

                    all_date_refund.add(dataSnapshot.getKey());

                    for(String key : map_key){
                        Map<String, Object> map_more = new HashMap<>();
                        map_more = (Map<String, Object>) map.get(key);
                        all_date_detail_refund.add(key);
                        DeliveryListAdminData refundListAdminData = new DeliveryListAdminData();

                        for(String key_more : map_more.keySet()){
                            Map<String, String> stringStringMap = new HashMap<>();
                            stringStringMap = (Map<String, String>) map_more.get(key_more);
                            Id = stringStringMap.get("memberID");
                            refundListAdminData.setDeliveryCompany(stringStringMap.get("deliveryCompany"));
                            refundListAdminData.setDeliveryNum(stringStringMap.get("deliveryNum"));
                            refundListAdminData.setOrderName(stringStringMap.get("orderName"));
                            refundListAdminData.setOrderPhoneNum(stringStringMap.get("orderPhoneNum"));
                            refundListAdminData.setDestination(stringStringMap.get("destination"));
                            refundListAdminData.setProductImage(stringStringMap.get("productImage"));
                            refundListAdminData.setProductName(stringStringMap.get("productName"));
                            sum_price += Integer.parseInt(String.valueOf(stringStringMap.get("productPrice"))) * Integer.parseInt(String.valueOf(stringStringMap.get("productQuantity")));
                        }
                        userIDs_refund.add(Id);
                        refundListAdminData.setItemCount(map_more.size());
                        refundListAdminData.setProductPrice(sum_price+delivery);
                        arrayList_refundList.add(refundListAdminData);
                        sum_price = 0;
                    }
                }
                refundListAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refundListAdminAdapter.setOnItemClickListener(new RefundListAdminAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, int diff) {
                if(diff == GO_TO_DETAIL){ // 주문 상세 정보
                    String newDate = "";
                    try {
                        String oldDate = all_date_detail_refund.get(pos);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date date = simpleDateFormat.parse(oldDate);
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                        newDate = simpleDateFormat1.format(date);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent goToDetail = new Intent(AdminPage.this, OrderListDetailAdmin.class);
                    goToDetail.putExtra("memberID", userIDs_refund.get(pos));
                    goToDetail.putExtra("all_date", newDate);
                    goToDetail.putExtra("all_date_detail", all_date_detail_refund.get(pos));
                    goToDetail.putExtra("diff", "refundListAdmin"); // 주문 상세 페이지 이동 시 어떤 페이지에서 이동했는지 구분하기 위해 추가
                    startActivity(goToDetail);
                }else if(diff == 200){ // 환불 승인
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPage.this);
                    dialog = alertDialog.setMessage("환불을 승인하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newDate = "";
                            try {
                                String oldDate = all_date_detail_refund.get(pos);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Date date = simpleDateFormat.parse(oldDate);
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                                newDate = simpleDateFormat1.format(date);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            databaseReference.child("RefundList_admin").child(newDate).child(all_date_detail_refund.get(pos)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String date = "";
                                    Map<String, Object> map = new HashMap<>();
                                    map = (Map<String, Object>) snapshot.getValue();
                                    for(String key : map.keySet()){
                                        Map<String, Object> map_more = new HashMap<>();
                                        map_more = (Map<String, Object>) map.get(key);
                                        for(String key_more : map_more.keySet()){
                                            map_more.put("state", "진행중");
                                            date = map_more.get("Date").toString();
                                        }
                                        map.put(key, map_more);
                                    }
                                    databaseReference.child("RefundList_"+userIDs_refund.get(pos)).child(all_date_detail_refund.get(pos)).updateChildren(map);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(AdminPage.this, "환불 처리하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("아니오", null).create();
                    dialog.show();
                }
            }
        });
    }

    public void changeTab(int tabNum){
        FrameLayout orderListAdminFrame = (FrameLayout) findViewById(R.id.orderList_admin_frame); // 주문 신청
        FrameLayout deliveryListAdminFrame = (FrameLayout) findViewById(R.id.deliveryList_admin_frame); // 배송중인 물품
        FrameLayout refundListAdminFrame = (FrameLayout) findViewById(R.id.refundList_admin_frame); // 환불 신청

        switch (tabNum){
            case 0:
                orderListAdminFrame.setVisibility(View.VISIBLE);
                deliveryListAdminFrame.setVisibility(View.GONE);
                refundListAdminFrame.setVisibility(View.GONE);
                break;
            case 1:
                orderListAdminFrame.setVisibility(View.GONE);
                deliveryListAdminFrame.setVisibility(View.VISIBLE);
                refundListAdminFrame.setVisibility(View.GONE);
                break;
            case 2:
                orderListAdminFrame.setVisibility(View.GONE);
                deliveryListAdminFrame.setVisibility(View.GONE);
                refundListAdminFrame.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyKey", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPage.this);
        dialog = alertDialog.setMessage("나가시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("returnToLogin", true);
                        editor.commit();
                        Intent logoutIntent = new Intent(AdminPage.this, LoginImage.class);
                        startActivity(logoutIntent);
                        finish();
                    }
                }).setNegativeButton("아니오", null).create();
        dialog.show();
    }
}