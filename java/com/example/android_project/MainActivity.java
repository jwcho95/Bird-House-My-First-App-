package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amar.library.ui.StickyScrollView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private long btnTime = 0; // 뒤로가기를 위해 만든 변수
    private MainSliderAdapter mainSliderAdapter;
    private ArrayList<MainSliderData> imageList;
    private FirebaseFirestore db;
    private Handler sliderHandler = new Handler();
    private ViewPager2 mainSlider;
    private FragmentStateAdapter fragmentPagerAdapter;
    private final List<String> tabElement = Arrays.asList("소형\n앵무새", "중형\n앵무새", "대형\n앵무새", "장난감", "새장");

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 액티비티가 생성되는 순간 버튼 초기화
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton myInformation = (ImageButton) findViewById(R.id.imageButton31); // 내 정보 버튼
        ImageButton shoppingListBtn = (ImageButton) findViewById(R.id.imageButton32); // 쇼핑하기 버튼
        ImageButton shoppingBasketBtn = (ImageButton) findViewById(R.id.imageButton33); // 장바구니 버튼
        TextView callCustomerCenter = findViewById(R.id.textView251); // 고객센터 연결 버튼
        TextView goToMap = findViewById(R.id.textView253); // 찾아오기 길 버튼
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView); // 하단의 탭
        Menu menu = bottomNavigationView.getMenu();
        imageList = new ArrayList<>();
        mainSlider = findViewById(R.id.mainSlider);
        mainSliderAdapter = new MainSliderAdapter(MainActivity.this, imageList, mainSlider);
        mainSlider.setAdapter(mainSliderAdapter);
        db = FirebaseFirestore.getInstance();

        // 아래의 뷰페이저 세팅
        ViewPager2 itemViewPager = findViewById(R.id.itemViewPager);
        fragmentPagerAdapter = new MainViewPagerAdapter(this);
        TabLayout mainItemTab = findViewById(R.id.mainItemTab);
        itemViewPager.setAdapter(fragmentPagerAdapter);



        new TabLayoutMediator(mainItemTab, itemViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(MainActivity.this);
                textView.setText(tabElement.get(position));
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                tab.setCustomView(textView);
            }
        }).attach();

        mainSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000);
            }
        });

        db.collection("advertiseProduct")
                .orderBy("Order")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot snapshot : task.getResult()){
                            Map<String, Object> map = snapshot.getData();
                            MainSliderData mainSliderData = new MainSliderData();
                            mainSliderData.setProductImage(String.valueOf(map.get("productImage")));
                            mainSliderData.setProductName(String.valueOf(map.get("productName")));
                            imageList.add(mainSliderData);
                        }
                        mainSliderAdapter.notifyDataSetChanged();
                    }
                });

        menu.findItem(R.id.home_tab).setChecked(true); // 탭에서 홈 화면에서 강조 표시를 해줌

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.category_tab:
                        Intent categoryIntent = new Intent(MainActivity.this, Category.class);
                        categoryIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(categoryIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        break;
                    case R.id.search_tab:
                        Intent searchIntent = new Intent(MainActivity.this, SearchPage.class);
                        searchIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(searchIntent);
                        overridePendingTransition(R.anim.from_left_in_animation, R.anim.none_animation);
                        break;
                    case R.id.home_tab:
                        break;
                    case R.id.person_tab:
                        Intent myInformationIntent = new Intent(MainActivity.this, MyInformation.class);
                        myInformationIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(myInformationIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                    case R.id.shopping_car_tab:
                        Intent shoppingBasketIntent = new Intent(MainActivity.this, ShoppingBasket.class);
                        shoppingBasketIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                        startActivity(shoppingBasketIntent);
                        overridePendingTransition(R.anim.from_right_in_animation, R.anim.none_animation);
                        break;
                }
                return false;
            }
        });

        myInformation.setOnClickListener(new View.OnClickListener() { // 내 정보 버튼
            @Override
            public void onClick(View v) {
                Intent myInformationIntent = new Intent(MainActivity.this, MyInformation.class);
                myInformationIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                myInformationIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(myInformationIntent);
            }
        });

        shoppingListBtn.setOnClickListener(new View.OnClickListener() { // 쇼핑하기 버튼
            @Override
            public void onClick(View v) {
                Intent shoppingListIntent = new Intent(MainActivity.this, ProductList.class);
                shoppingListIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                shoppingListIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(shoppingListIntent);
            }
        });

        shoppingBasketBtn.setOnClickListener(new View.OnClickListener() { // 장바구니 버튼
            @Override
            public void onClick(View v) {
                Intent shoppingBasketIntent = new Intent(MainActivity.this, ShoppingBasket.class);
                shoppingBasketIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                shoppingBasketIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(shoppingBasketIntent);
            }
        });

        callCustomerCenter.setOnClickListener(new View.OnClickListener() { // 고객센터 연결 버튼
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01011111111"));
                startActivity(callIntent);
            }
        });

        goToMap.setOnClickListener(new View.OnClickListener() { // 찾아오시는 길 버튼
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(mapIntent);
            }
        });

    }

    @Override
    public void onBackPressed() {  // 뒤로가기 버튼을 눌렀을 때의 종료
        long currentTime = System.currentTimeMillis();
        long gapTime = currentTime - btnTime;

        if(gapTime >= 0 && gapTime <= 2000){
            finishAffinity(); // 재실행해도 처음 화면으로 갈 수 있게 해준다. (어플의 루트 액티비티 제거)
            System.runFinalization(); // 현재 작업중인 쓰레드가 다 종료되면 종료시키라는 명령어
            System.exit(0);
        }
        else{
            btnTime = currentTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            mainSlider.setCurrentItem(mainSlider.getCurrentItem() + 1);
        }
    };
}