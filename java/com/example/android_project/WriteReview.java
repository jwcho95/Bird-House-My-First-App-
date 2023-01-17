package com.example.android_project;

import androidx.annotation.NonNull;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WriteReview extends AppCompatActivity {
    private final int GET_PICTURE = 1000;
    ImageView printImage;
    private String userID;
    private String userContent;
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private boolean checkUploadImage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ImageView backBtn = findViewById(R.id.imageView58); // 뒤로가기 버튼
        ImageView homeBtn = findViewById(R.id.imageView59); // 홈페이지 버튼
        TextView userId = (TextView) findViewById(R.id.textView107); // 아이디가 보여지는 공간
        EditText contentOfReview = (EditText) findViewById(R.id.editTextTextMultiLine); // 리뷰 내용이 있는 공간
        Button finished = (Button) findViewById(R.id.button18); // 작성 완료 버튼
        Button addPicture = (Button) findViewById(R.id.button22); // 사진을 추가하는 버튼
        printImage = (ImageView) findViewById(R.id.imageView18); // 이미지가 보여지는 공간
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar2); // 별점

        userID = getIntent().getStringExtra("memberID");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(getIntent().getStringExtra("Review_item"));

        ratingBar.setRating((float) 5.0);
        userId.setText("아이디: "+getIntent().getStringExtra("memberID")); // 아이디 표시
        contentOfReview.setText(getIntent().getStringExtra("reviewContent"));
        
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
                Intent homeIntent = new Intent(WriteReview.this, MainActivity.class);
                homeIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeIntent);
                finish();
            }
        });

        finished.setOnClickListener(new View.OnClickListener() { // 작성하기 버튼
            @Override
            public void onClick(View v) {
                if(contentOfReview.getText().toString().equals("")){
                    Toast.makeText(WriteReview.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    userContent = contentOfReview.getText().toString();
                    Map<String, Object>map = new HashMap<>();
                    if(imageUri != null) {
                        map.put("review_rating", ratingBar.getRating());
                        map.put("review_image",imageUri.toString());
                        map.put("review_id", userID);
                        map.put("review_content", userContent);
                    }else{
                        map.put("review_rating", ratingBar.getRating());
                        map.put("review_id", userID);
                        map.put("review_content", userContent);
                    }
                    databaseReference.child("Review_"+userID).updateChildren(map);
                    Toast.makeText(WriteReview.this, "작성 되었습니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }
            }
        });

        addPicture.setOnClickListener(new View.OnClickListener() { // 이미지 추가하는 버튼
            @Override
            public void onClick(View v) {
                Intent changeImageIntent = new Intent(Intent.ACTION_PICK);
                changeImageIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(changeImageIntent, GET_PICTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            printImage.setImageURI(imageUri);
            uploadPicture();
            checkUploadImage = true;
        }
    }

    private void uploadPicture() {
//        final String randomKey = UUID.randomUUID().toString();
//        StorageReference riversRef = storageReference.child("images/"+randomKey);


        StorageReference riversRef = storageReference.child("images/review_"+getIntent().getStringExtra("memberID")+".jpeg");

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}