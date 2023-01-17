package com.example.android_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class WriteComplaint extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 300;
    ImageView printImage;
    private String complaint_content;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_complaint);
        ImageView backBtn = findViewById(R.id.imageView60); // 뒤로가기 버튼
        ImageView homeBtn = findViewById(R.id.imageView61); // 홈페이지 버튼
        Button finished = (Button) findViewById(R.id.button19); // 작성하기 버튼
        EditText title = (EditText) findViewById(R.id.editTextTextPersonName4); // 문의사항 제목
        EditText content = (EditText) findViewById(R.id.editTextTextMultiLine2); // 문의사항 내용
        Button addImage = (Button) findViewById(R.id.button24); // 이미지 첨부버튼
        printImage = (ImageView) findViewById(R.id.imageView7); // 이미지를 보여주는 뷰
        FirebaseDatabase firebaseDatabase_complaint = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference_complaint = firebaseDatabase_complaint.getReference(getIntent().getStringExtra("Complaint_item"));

        title.setText(getIntent().getStringExtra("complaintTitle"));
        content.setText(getIntent().getStringExtra("complaintContent"));

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
                Intent homeIntent = new Intent(WriteComplaint.this, MainActivity.class);
                homeIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                startActivity(homeIntent);
            }
        });

        finished.setOnClickListener(new View.OnClickListener() { // 작성하기 버튼
            @Override
            public void onClick(View v) {
                if(title.getText().toString().equals("")){
                    Toast.makeText(WriteComplaint.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(content.getText().toString().equals("")){
                    Toast.makeText(WriteComplaint.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    complaint_content = content.getText().toString();
                    Map<String, Object> map = new HashMap<>();
                    if(imageUri != null) {
                        map.put("complaint_image", imageUri.toString());
                        map.put("complaint_id", getIntent().getStringExtra("memberID"));
                        map.put("complaint_title", content.getText().toString());
                        map.put("complaint_content", complaint_content);
                    }else{
                        map.put("complaint_id", getIntent().getStringExtra("memberID"));
                        map.put("complaint_title", content.getText().toString());
                        map.put("complaint_content", complaint_content);
                    }
                    databaseReference_complaint.child("Complaint_"+getIntent().getStringExtra("memberID")).updateChildren(map);
                    Toast.makeText(WriteComplaint.this, "작성 되었습니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeImageIntent = new Intent(Intent.ACTION_PICK);
                changeImageIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); // 이미지 타입과 이미지를 가진 uri만 불러옴
                startActivityForResult(changeImageIntent, GET_GALLERY_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 사진을 선택하고 그에 따른 답변을 기다리는 메소드
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            printImage.setImageURI(imageUri);
        }
    }
}