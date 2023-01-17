package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ProductList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ImageButton backBtn = (ImageButton) findViewById(R.id.imageButton6); // 뒤로가기 버튼
        ImageButton homeBtn = (ImageButton) findViewById(R.id.imageButton7); // 홈페이지 버튼
        TabLayout productTab = (TabLayout) findViewById(R.id.tabLayout2); // 상품을 바꾸는 탭
        ImageView firstProduct = (ImageView) findViewById(R.id.imageView11); // 탭의 첫번째 상품

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
                Intent homeBtnIntent = new Intent(ProductList.this, MainActivity.class);
                homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeBtnIntent);
                finish();
            }
        });

        productTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { // 탭이 선택되지 않음에서 선택되었을 때
                int pos = tab.getPosition(); // 탭의 순서대로 값을 리턴(첫번째 탭은 0, 두번째 탭은 1 등)
                changeTab(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        firstProduct.setOnClickListener(new View.OnClickListener() { // 첫 번째로 나열된 상품
            @Override
            public void onClick(View v) {
                Intent firstProductIntent = new Intent(ProductList.this, ProductDetailPage.class);
                firstProductIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                firstProductIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(firstProductIntent);
            }
        });
    }

    public void changeTab(int tabNum){ // 탭에 따른 화면을 전환시켜주는 메소드
        FrameLayout smallBirdTab = (FrameLayout) findViewById(R.id.small_bird_product);
        FrameLayout middleBirdTab = (FrameLayout) findViewById(R.id.middle_bird_product);

        switch (tabNum){
            case 0:
                smallBirdTab.setVisibility(View.VISIBLE);
                middleBirdTab.setVisibility(View.INVISIBLE);
                break;
            case 1:
                smallBirdTab.setVisibility(View.INVISIBLE);
                middleBirdTab.setVisibility(View.VISIBLE);
                break;
        }
    }
}