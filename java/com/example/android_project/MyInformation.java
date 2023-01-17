package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyInformation extends AppCompatActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        Button changeInformation = (Button) findViewById(R.id.button10); // 개인정보 수정 버튼
        Button shoppingBasketBtn = (Button) findViewById(R.id.button11); // 장바구니 버튼
        Button orderListBtn = (Button) findViewById(R.id.button12); // 주문 내역 버튼
        Button logout = (Button) findViewById(R.id.button14); // 로그아웃 버튼
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);  // 하단 탭
        Menu menu = bottomNavigationView.getMenu();
        SharedPreferences preferences = getSharedPreferences("MyKey", MODE_PRIVATE); // 로그아웃 시 사용
        SharedPreferences.Editor editor = preferences.edit();

        menu.findItem(R.id.person_tab).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category_tab:
                        Intent categoryIntent = new Intent(MyInformation.this, Category.class);
                        categoryIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(categoryIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        break;
                    case R.id.search_tab:
                        Intent SearchIntent = new Intent(MyInformation.this, SearchPage.class);
                        SearchIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(SearchIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        finish();
                        break;
                    case R.id.home_tab:
                        Intent homeBtnIntent = new Intent(MyInformation.this, MainActivity.class);
                        homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(homeBtnIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        finish();
                        break;
                    case R.id.person_tab:
                        break;
                    case R.id.shopping_car_tab:
                        Intent shoppingBasketIntent = new Intent(MyInformation.this, ShoppingBasket.class);
                        shoppingBasketIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(shoppingBasketIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                }
                return false;
            }
        });

        changeInformation.setOnClickListener(new View.OnClickListener() { // 개인정보 수정을 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent changeInformationIntent = new Intent(MyInformation.this, ChangeInformation.class);
                changeInformationIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                startActivity(changeInformationIntent);
            }
        });

        shoppingBasketBtn.setOnClickListener(new View.OnClickListener() { // 장바구니 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent shoppingBasketIntent = new Intent(MyInformation.this, ShoppingBasket.class);
                shoppingBasketIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                shoppingBasketIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(shoppingBasketIntent);
            }
        });

        orderListBtn.setOnClickListener(new View.OnClickListener() { // 주문 내역 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent orderListIntent = new Intent(MyInformation.this, OrderList.class);
                orderListIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                orderListIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(orderListIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyInformation.this);
                dialog = alertDialog.setMessage("로그아웃 하시겠습니까?").setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putBoolean("returnToLogin", true);
                        editor.commit();
                        Toast.makeText(MyInformation.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent logoutIntent = new Intent(MyInformation.this, LoginImage.class);
                        startActivity(logoutIntent);
                        finish();
                    }
                }).setNegativeButton("아니오", null).create();
                dialog.show();
            }
        });
    }
}