package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Category extends AppCompatActivity {

    private FirebaseFirestore db;

    private LinearLayoutManager layoutManager;
    private CategoryLeftAdapter categoryLeftAdapter;
    private ArrayList<CategoryLeftData> arrayList_left;
    private CategoryLeftData categoryLeftData;

    private CategoryRightAdapter categoryRightAdapter;
    private ArrayList<CategoryRightData> arrayList_right;
    private CategoryRightData categoryRightData;

    private String Tag = "MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView); // 하단 탭
        Menu menu = bottomNavigationView.getMenu();
        db = FirebaseFirestore.getInstance();
        arrayList_left = new ArrayList<>();
        arrayList_right = new ArrayList<>();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { // 하단 탭
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category_tab:
                        break;
                    case R.id.search_tab:
                        Intent searchIntent = new Intent(Category.this, SearchPage.class);
                        searchIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(searchIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                    case R.id.home_tab:
                        Intent homeIntent = new Intent(Category.this, MainActivity.class);
                        homeIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(homeIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                    case R.id.person_tab:
                        Intent myInformationIntent = new Intent(Category.this, MyInformation.class);
                        myInformationIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(myInformationIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                    case R.id.shopping_car_tab:
                        Intent shoppingBasketIntent = new Intent(Category.this, ShoppingBasket.class);
                        shoppingBasketIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(shoppingBasketIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                }
                return false;
            }
        });

        db.collection("Product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // 카테고리가 처음 시작될 때 모든 제품을 나열함
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        arrayList_right.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categoryRightData = document.toObject(CategoryRightData.class);
                                if(categoryRightData.productName != null) {
                                    arrayList_right.add(categoryRightData);
                                }
                            }
                            categoryRightAdapter.notifyDataSetChanged();

                            categoryRightAdapter.setOnItemClickListener(new CategoryRightAdapter.OnItemClickListener() { // 카테고리 오른쪽 내용들을 클릭했을 때 이동
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(Category.this, ProductDetailPage.class);
                                    intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d(Tag, "Error getting documents: ", task.getException());
                        }
                    }
                });


        /* 카테고리 좌측 리사이클러뷰 */
        RecyclerView recyclerView_left = findViewById(R.id.recyclerView_category_left);
        recyclerView_left.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Category.this);
        recyclerView_left.setLayoutManager(layoutManager);

        categoryLeftAdapter = new CategoryLeftAdapter(arrayList_left, Category.this);
        recyclerView_left.setAdapter(categoryLeftAdapter);

        /* 카테고리 좌측 리사이클러뷰 */
        RecyclerView recyclerView_right = findViewById(R.id.recyclerView_category_right);
        recyclerView_right.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(Category.this);
        recyclerView_right.setLayoutManager(layoutManager);

        categoryRightAdapter = new CategoryRightAdapter(arrayList_right, Category.this);
        recyclerView_right.setAdapter(categoryRightAdapter);

        db.collection("Product")
                .orderBy("Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { // 카테고리 좌측 쿼리문
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        arrayList_left.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categoryLeftData = document.toObject(CategoryLeftData.class);
                                arrayList_left.add(categoryLeftData);
                            }
                            categoryLeftAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(Tag, "Error getting documents: ", task.getException());
                        }
                    }
                });

        categoryLeftAdapter.setOnItemClickListener(new CategoryLeftAdapter.OnItemClickListener() { // 카테고리를 누르면 해당 카테고리에 속한 제품들을 보여줌
            @Override
            public void onItemClick(View v, int pos) {
                String name = choiceCategory(pos);
                db.collection("Product")
                        .document(name)
                        .collection(name+"Product")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                arrayList_right.clear();
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        categoryRightData = document.toObject(CategoryRightData.class);
                                        arrayList_right.add(categoryRightData);
                                    }
                                    categoryRightAdapter.notifyDataSetChanged();

                                    categoryRightAdapter.setOnItemClickListener(new CategoryRightAdapter.OnItemClickListener() { // 카테고리 오른쪽 내용들을 클릭했을 때 이동
                                        @Override
                                        public void onItemClick(View v, int pos) {
                                            Intent intent = new Intent(Category.this, ProductDetailPage.class);
                                            intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    Log.d(Tag, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }

    public String choiceCategory(int pos){ // 카테고리를 골라주는 함수
        String documentName = "";

        switch (pos){
            case 0:
                documentName = "all";
                break;
            case 1:
                documentName = "smallBird";
                break;
            case 2:
                documentName = "middleBird";
                break;
            case 3:
                documentName = "bigBird";
                break;
            case 4:
                documentName = "birdToy";
                break;
            case 5:
                documentName = "birdcage";
                break;
        }

        return documentName;
    }
}