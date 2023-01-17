package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SearchPage extends AppCompatActivity {

    private final String Tag = "MyActivity";
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    SearchAdapter searchAdapter;
    ArrayList<SearchData> arrayList;
    private Map<String, Object> map_search;
    SearchData newSearchData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        SearchView searchView = findViewById(R.id.search_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_search);
        HashSet<SearchData> hashSet = new HashSet<>();



        /* 리사이클러뷰에 필요 */
        arrayList = new ArrayList<SearchData>();
        SearchData[] searchData = new SearchData[10];

        menu.findItem(R.id.search_tab).setChecked(true);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(SearchPage.this);
        recyclerView.setLayoutManager(layoutManager);

        searchAdapter = new SearchAdapter(arrayList, SearchPage.this);
        recyclerView.setAdapter(searchAdapter);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { // 하단 탭
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category_tab:
                        Intent categoryIntent = new Intent(SearchPage.this, Category.class);
                        categoryIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(categoryIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        break;
                    case R.id.search_tab:
                        break;
                    case R.id.home_tab:
                        Intent homeIntent = new Intent(SearchPage.this, MainActivity.class);
                        homeIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(homeIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                    case R.id.person_tab:
                        Intent myInformationIntent = new Intent(SearchPage.this, MyInformation.class);
                        myInformationIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(myInformationIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                    case R.id.shopping_car_tab:
                        Intent shoppingBasketIntent = new Intent(SearchPage.this, ShoppingBasket.class);
                        shoppingBasketIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(shoppingBasketIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // 검색창
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // 텍스트가 계속 변할 때
                arrayList.clear();

                db.collection("Product")
                        .document("all")
                        .collection("allProduct")
                        .orderBy("productName")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(newText.equals("")){
                                    arrayList.clear();
                                    searchAdapter.notifyDataSetChanged();
                                }else if(newText.equals("대형앵무새") || newText.contains("대형앵")) { // 대형앵무새 카테고리에 접근했을 때
                                    Query query = db.collection("Product")
                                            .document("bigBird").collection("bigBirdProduct");
                                    query.orderBy("productName")
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            arrayList.clear();
                                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                                Map<String, Object> map = snapshot.getData();
                                                SearchData search_data = new SearchData();
                                                search_data.setProductImage(map.get("productImage").toString());
                                                search_data.setProductName(map.get("productName").toString());
                                                arrayList.add(search_data);
                                            }
                                            searchAdapter.notifyDataSetChanged();

                                            searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View v, int pos) {
                                                    Intent intent = new Intent(SearchPage.this, ProductDetailPage.class);
                                                    intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    });
                                }else if(newText.equals("중형앵무새") || newText.contains("중형앵")) { // 중형 앵무새 카테고리에 접근
                                    Query query = db.collection("Product")
                                            .document("middleBird").collection("middleBirdProduct");
                                    query.orderBy("productName")
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    arrayList.clear();
                                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                                        Map<String, Object> map = snapshot.getData();
                                                        SearchData search_data = new SearchData();
                                                        search_data.setProductImage(map.get("productImage").toString());
                                                        search_data.setProductName(map.get("productName").toString());
                                                        arrayList.add(search_data);
                                                    }
                                                    searchAdapter.notifyDataSetChanged();

                                                    searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(View v, int pos) {
                                                            Intent intent = new Intent(SearchPage.this, ProductDetailPage.class);
                                                            intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            });
                                }else if(newText.equals("소형앵무새") || newText.contains("소형앵")){ // 소형 앵무새 카테고리에 접근
                                    Query query = db.collection("Product")
                                            .document("smallBird").collection("smallBirdProduct");
                                    query.orderBy("productName")
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    arrayList.clear();
                                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                                        Map<String, Object> map = snapshot.getData();
                                                        SearchData search_data = new SearchData();
                                                        search_data.setProductImage(map.get("productImage").toString());
                                                        search_data.setProductName(map.get("productName").toString());
                                                        arrayList.add(search_data);
                                                    }
                                                    searchAdapter.notifyDataSetChanged();

                                                    searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(View v, int pos) {
                                                            Intent intent = new Intent(SearchPage.this, ProductDetailPage.class);
                                                            intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            });
                                }else if(newText.equals("장난감")){ // 장난감 카테고리에 접근
                                    Query query = db.collection("Product")
                                            .document("birdToy").collection("birdToyProduct");
                                    query.orderBy("productName")
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    arrayList.clear();
                                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                                        Map<String, Object> map = snapshot.getData();
                                                        SearchData search_data = new SearchData();
                                                        search_data.setProductImage(map.get("productImage").toString());
                                                        search_data.setProductName(map.get("productName").toString());
                                                        arrayList.add(search_data);
                                                    }
                                                    searchAdapter.notifyDataSetChanged();
                                                }
                                            });
                                }else if(newText.equals("새장")){ // 새장 카테고리에 접근
                                    Query query = db.collection("Product")
                                            .document("birdcage").collection("birdcageProduct");
                                    query.orderBy("productName")
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    arrayList.clear();
                                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                                        Map<String, Object> map = snapshot.getData();
                                                        SearchData search_data = new SearchData();
                                                        search_data.setProductImage(map.get("productImage").toString());
                                                        search_data.setProductName(map.get("productName").toString());
                                                        arrayList.add(search_data);
                                                    }
                                                    searchAdapter.notifyDataSetChanged();
                                                }
                                            });
                                }
                                else {
                                    arrayList.clear();
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            newSearchData = document.toObject(SearchData.class);
                                            if(newSearchData.getProductName().contains(newText)) {
                                                arrayList.add(newSearchData);
                                            }
                                        }
                                        searchAdapter.notifyDataSetChanged();

                                        searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View v, int pos) {
                                                Intent intent = new Intent(SearchPage.this, ProductDetailPage.class);
                                                intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        Log.d(Tag, "Error getting documents: ", task.getException());
                                    }
                                }
                            }
                        });
                return true;
            }
        });
    }
}