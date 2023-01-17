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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class OrderList extends AppCompatActivity {

    private ArrayList<OrderListData> arrayList;
    private RecyclerView recyclerView;
    private OrderListAdapter orderListAdapter;
    private LinearLayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private int count = 0;
    private int sum_price = 0;
    private String key;
    private int delivery = 2500;
    private AlertDialog dialog;
    private AlertDialog dialog_more;
    private int numberOfProduct = 0;
    private DatabaseReference databaseReference_admin;
    private long longTime;
    private long longTime_delivery;

    /* 배송중 페이지 변수들 */
    private ArrayList<DeliveryListData> arrayListDelivery;
    private RecyclerView recyclerViewDelivery;
    private DeliveryListAdapter deliveryListAdapter;
    private DatabaseReference databaseReference_delivery;
    private LinearLayoutManager layoutManagerDelivery;
    private DatabaseReference databaseReference_basic;
    private FrameLayout deliveryFrame;

    /* 환불 페이지 변수들 */
    private FrameLayout refundFrame;
    private ArrayList<RefundListData> arrayListRefund;
    private RecyclerView recyclerViewRefund;
    private RefundListAdapter refundListAdapter;
    private DatabaseReference databaseReference_refund;
    private LinearLayoutManager layoutManagerRefund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ImageView backBtn = findViewById(R.id.imageView47); // 뒤로가기 버튼
        ImageView homeBtn = findViewById(R.id.imageView46); // 홈페이지 버튼
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_orderList);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("OrderList_"+getIntent().getStringExtra("memberID"));
        databaseReference_admin = firebaseDatabase.getReference("OrderList_admin");
        OrderListData[] orderListData = new OrderListData[10];
        ArrayList<String> all_date = new ArrayList<>(); // 날짜를 전부 저장
        ArrayList<String> all_date_delivery = new ArrayList<>(); // 배송중인 품목 날짜 전부 저장
        ArrayList<String> all_date_refund = new ArrayList<>(); // 환불 품목 날짜 저장
        TabLayout tabLayout = findViewById(R.id.tabLayout3);

        recyclerViewDelivery = findViewById(R.id.recyclerView_deliveryList);
        arrayListDelivery = new ArrayList<>();
        deliveryListAdapter = new DeliveryListAdapter(arrayListDelivery, OrderList.this);
        layoutManagerDelivery = new LinearLayoutManager(OrderList.this);
        recyclerViewDelivery.setLayoutManager(layoutManagerDelivery);
        recyclerViewDelivery.setHasFixedSize(true);
        recyclerViewDelivery.setAdapter(deliveryListAdapter);
        databaseReference_delivery = firebaseDatabase.getReference("Delivery_"+getIntent().getStringExtra("memberID"));
        databaseReference_basic = firebaseDatabase.getReference();




        databaseReference.addValueEventListener(new ValueEventListener() { // 리사이클러뷰에 연동된 데이터베이스

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                arrayList.clear();
                all_date.clear(); // 주문 리스트에 있는 날짜 배열
                count = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    /* 예전 코드 */
//                    if(count >= 10){
//                        Toast.makeText(OrderList.this, "최대 10개까지 표시됩니다.", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    orderListData[count] = new OrderListData();
//                    key = snapshot.getKey();
//                    orderListData[count].setDate(key);
//                    Map<String, Object> map = (Map<String, Object>)snapshot.getValue();
//                    for(String Key : map.keySet()){
//                        Map<String, String> stringStringMap = (Map<String, String>) map.get(Key);
//                        orderListData[count].setItemImage(stringStringMap.get("productImage"));
//                        orderListData[count].setItemName(stringStringMap.get("productName"));
//                        orderListData[count].setReceiveDate(stringStringMap.get("receiveDate"));
//                        int price = Integer.parseInt(String.valueOf(stringStringMap.get("productPrice")));
//                        int quantity = Integer.parseInt(String.valueOf(stringStringMap.get("productQuantity")));
//                        sum_price += price * quantity;
//                    }
//                    orderListData[count].setItemQuantity(map.size());
//                    orderListData[count].setItemPrice(sum_price+delivery);
//                    arrayList.add(orderListData[count]);
//                    count += 1;
//                    sum_price = 0;

                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    OrderListData newOrderListData = new OrderListData();
                    all_date.add(snapshot.getKey());
                    for(String Key : map.keySet()){
                        Map<String, String> stringStringMap = (Map<String, String>) map.get(Key);
                        newOrderListData.setHowToPay(stringStringMap.get("howToPay"));
                        newOrderListData.setItemImage(stringStringMap.get("productImage"));
                        newOrderListData.setItemName(stringStringMap.get("productName"));
                        newOrderListData.setReceiveDate(stringStringMap.get("receiveDate"));
                        int price = Integer.parseInt(String.valueOf(stringStringMap.get("productPrice")));
                        int quantity = Integer.parseInt(String.valueOf(stringStringMap.get("productQuantity")));
                        sum_price += price * quantity;
                    }
                    newOrderListData.setItemQuantity(map.size());
                    newOrderListData.setItemPrice(sum_price+delivery);
                    arrayList.add(newOrderListData);
                    sum_price = 0;
                }
                orderListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("ProductDetailPage", "Failed to read value.", error.toException());
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(OrderList.this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        orderListAdapter = new OrderListAdapter(arrayList, OrderList.this);
        recyclerView.setAdapter(orderListAdapter);



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
                Intent homeBtnIntent = new Intent(OrderList.this, MainActivity.class);
                homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeBtnIntent);
                finish();
            }
        });

//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent cancelIntent = new Intent(OrderList.this, OrderCanceled.class);
//                cancelIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
//                cancelIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
//                startActivity(cancelIntent);
//                finish();
//            }
//        });

        orderListAdapter.setOnItemClickListener(new OrderListAdapter.OnItemClickListener() { // 리사이클러뷰 속 내용
            @Override
            public void onItemClick(View v, int pos, int diff) {
                if(diff == 110){ // 주문 취소 버튼
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderList.this);
                    AlertDialog.Builder alertDialog_more = new AlertDialog.Builder(OrderList.this);
                    dialog = alertDialog.setMessage("주문을 취소하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String date = all_date.get(pos);

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 시간을 없애고 날짜만 추출
                            try {
                                longTime = simpleDateFormat.parse(date).getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                            String newDate = simpleDateFormat1.format(longTime);


                            databaseReference.child(all_date.get(pos)).removeValue();
                            databaseReference_admin.child(newDate).child(all_date.get(pos)).setValue(null);
                            dialog_more = alertDialog_more.setMessage("주문이 취소되었습니다.").setPositiveButton("확인", null).create();
                            dialog_more.show();
                            orderListAdapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("아니오", null).create();
                    dialog.show();
                }else if(diff == 210){ // 주문 상세 버튼
                    Intent intent = new Intent(OrderList.this, OrderDetailList.class);
                    intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                    intent.putExtra("orderDate", all_date.get(pos));
                    startActivity(intent);
                }
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

        databaseReference_delivery.addValueEventListener(new ValueEventListener() { // 배송중 페이지 리사이클러뷰
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListDelivery.clear();
                all_date_delivery.clear();
                int sum_price = 0;
                String date = "";

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Map<String, Object> map = new HashMap<>();
                    map = (Map<String, Object>) dataSnapshot.getValue();
                    DeliveryListData deliveryListData = new DeliveryListData();

                    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(map); // 키 값을 정렬 시키기 위해 추가
                    Set<String> map_key = treeMap.keySet();

                    for(String key : map_key){
                        Map<String, String> map_more = (Map<String, String>) map.get(key);
                        date = map_more.get("Date");
                        deliveryListData.setProductImage(map_more.get("productImage"));
                        deliveryListData.setProductName(map_more.get("productName"));
                        deliveryListData.setDate(map_more.get("timeOfArrival"));
                        deliveryListData.setDeliveryNum(map_more.get("deliveryNum"));
                        deliveryListData.setDeliveryCompany(map_more.get("deliveryCompany"));
                        sum_price += Integer.parseInt(String.valueOf(map_more.get("productPrice"))) * Integer.parseInt(String.valueOf(map_more.get("productQuantity")));
                    }
                    all_date_delivery.add(date);
                    deliveryListData.setProductPrice(sum_price+delivery);
                    deliveryListData.setItemCount(map.size());
                    arrayListDelivery.add(deliveryListData);
                    sum_price = 0;
                }
                deliveryListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        deliveryListAdapter.setOnItemClickListener(new DeliveryListAdapter.OnItemClickListener() { // 배송중 페이지 리사이클러뷰를 클릭했을 때
            @Override
            public void onItemClick(View v, int pos, int diff) {
                ArrayList<String> date = new ArrayList<>();
                if(diff == 1000){ // 환불 신청 버튼을 눌렀을 때
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderList.this);
//                    dialog = alertDialog.setMessage("환불 신청을 하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ArrayList<String> date = new ArrayList<>();
//                            databaseReference_basic.child("Delivery_"+getIntent().getStringExtra("memberID"))
//                                    .addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    date.clear();
//                                    Map<String, Object> map = new HashMap<>();
//                                    map = (Map<String, Object>) snapshot.getValue();
//
//                                    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(map); // 키 값을 정렬 시키기 위해 추가
//                                    Set<String> map_key = treeMap.keySet();
//
//                                    Map<String, Object> map_more = new HashMap<>();
//                                    for(String key : map_key){
//                                        date.add(key);
//                                        map_more = (Map<String, Object>) map.get(key);
//                                    }
//                                    String Date = date.get(pos);
//                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 시간을 없애고 날짜만 추출
//                                    try {
//                                        longTime_delivery = simpleDateFormat.parse(Date).getTime();
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//                                    String newDate = simpleDateFormat1.format(longTime_delivery);
//
//                                    databaseReference_basic.child("RefundList_admin").child(newDate).child(date.get(pos)).updateChildren(map_more);
//                                    AlertDialog.Builder alertDialog_more = new AlertDialog.Builder(OrderList.this);
//                                    dialog_more = alertDialog_more.setMessage("환불 승인이 되면 주문하신 계좌로 2~3일 후에 입금됩니다. 은행의 사정에 따라 더 늦어질 수도 있습니다.")
//                                            .setPositiveButton("확인", null)
//                                            .create();
//                                    dialog_more.show();


//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//
//                        }
//                    }).setNegativeButton("아니오", null).create();
//                    dialog.show();


                    Intent intent = new Intent(OrderList.this, ReasonOfRefund.class);
                    intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                    intent.putExtra("orderDate", all_date_delivery.get(pos));
                    startActivity(intent);

                }else if(diff == 2000){ // 주문 상세 페이지
                    Intent intent = new Intent(OrderList.this, DeliveryListDetail.class);
                    intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                    intent.putExtra("orderDate", all_date_delivery.get(pos));
                    intent.putExtra("page", "deliveryPage");
                    startActivity(intent);
                }
            }
        });

        // 환불 리사이클러뷰
        arrayListRefund = new ArrayList<>();
        recyclerViewRefund = findViewById(R.id.recyclerView_refundList);
        layoutManagerRefund = new LinearLayoutManager(OrderList.this);
        refundListAdapter = new RefundListAdapter(arrayListRefund, OrderList.this);
        recyclerViewRefund.setHasFixedSize(true);
        recyclerViewRefund.setLayoutManager(layoutManagerRefund);
        recyclerViewRefund.setAdapter(refundListAdapter);
        databaseReference_refund = firebaseDatabase.getReference("RefundList_"+getIntent().getStringExtra("memberID"));

        databaseReference_refund.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListRefund.clear();
                all_date_refund.clear();
                int sum_price = 0;
                String date = "";

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Map<String, Object> map = new HashMap<>();
                    map = (Map<String, Object>) dataSnapshot.getValue();
                    RefundListData refundListData = new RefundListData();

                    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(map); // 키 값을 정렬 시키기 위해 추가
                    Set<String> map_key = treeMap.keySet();

                    for(String key : map_key){
                        Map<String, String> map_more = (Map<String, String>) map.get(key);
                        date = map_more.get("refundDateDetail");
                        refundListData.setState(map_more.get("state"));
                        refundListData.setProductImage(map_more.get("productImage"));
                        refundListData.setProductName(map_more.get("productName"));
                        refundListData.setRefundDate(map_more.get("refundDate"));
                        refundListData.setDeliveryNum(map_more.get("deliveryNum"));
                        refundListData.setDeliveryCompany(map_more.get("deliveryCompany"));
                        sum_price += Integer.parseInt(String.valueOf(map_more.get("productPrice"))) * Integer.parseInt(String.valueOf(map_more.get("productQuantity")));
                    }
                    all_date_refund.add(date);
                    refundListData.setProductPrice(sum_price+delivery);
                    refundListData.setItemCount(map.size());
                    arrayListRefund.add(refundListData);
                    sum_price = 0;
                }
                refundListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refundListAdapter.setOnItemClickListener(new RefundListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos, int diff) {
                if(diff == 100){ // 주문 상세 페이지
                    Intent intent = new Intent(OrderList.this, DeliveryListDetail.class);
                    intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                    intent.putExtra("orderDate", all_date_refund.get(pos));
                    intent.putExtra("page", "refundPage");
                    startActivity(intent);
                }
            }
        });

    }

    public void changeTab(int tabNum){
        FrameLayout orderListFrame = (FrameLayout) findViewById(R.id.orderList_frame); // 입금 확인 페이지
        deliveryFrame = (FrameLayout) findViewById(R.id.delivery_frame); // 배송중 페이지
        refundFrame = (FrameLayout) findViewById(R.id.refundList_frame); // 환불 페이지

        switch (tabNum){
            case 0:
                orderListFrame.setVisibility(View.VISIBLE);
                deliveryFrame.setVisibility(View.GONE);
                refundFrame.setVisibility(View.GONE);
                break;
            case 1:
                orderListFrame.setVisibility(View.GONE);
                deliveryFrame.setVisibility(View.VISIBLE);
                refundFrame.setVisibility(View.GONE);
                break;
            case 2:
                orderListFrame.setVisibility(View.GONE);
                deliveryFrame.setVisibility(View.GONE);
                refundFrame.setVisibility(View.VISIBLE);
                break;
        }
    }
}