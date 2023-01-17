package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderCanceled extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_canceled);
        Button okBtn = (Button) findViewById(R.id.button13); // 확인 버튼

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderCanceled.this, MainActivity.class);
                intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                intent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(intent);
                finish();
            }
        });
    }
}