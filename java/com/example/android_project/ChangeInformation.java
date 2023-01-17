package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeInformation extends AppCompatActivity {
    // private final int MY_PERMISSIONS_REQUEST_CODE = 100; // 여러 개의 권한을 요청할 때 구분하기 위해 임의로 지정해놓은 코드 <- 권한을 연습할 때 사용한 코드
    private final int GET_GALLERY_IMAGE = 200;
    ImageView changeImageView;
    private AlertDialog dialog; // 메세지 창을 띄우는 변수
    private boolean okPresentPw = false;
    private boolean okNewPw = false;
    private String realPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        TextView Name = (TextView) findViewById(R.id.textView96); // 이름이 보여지는 공간
        TextView Id = (TextView) findViewById(R.id.textView97); // 아이디가 보여지는 공간
        TextView Email = (TextView) findViewById(R.id.textView95); // 이메일이 보여지는 공간
        ImageView backBtn = findViewById(R.id.imageView57); // 뒤로가기 버튼
        ImageView homeBtn = findViewById(R.id.imageView56); // 홈페이지 버튼
        Button changeImage = (Button) findViewById(R.id.button15); // 사진 선택하는 버튼
        changeImageView = (ImageView) findViewById(R.id.imageView15); // 바뀌는 사진
        Button changeInformationBtn = (Button) findViewById(R.id.button30); // 개인정보 수정 버튼
        EditText presentPw = findViewById(R.id.editTextTextPassword); // 현재 비밀번호
        EditText checkPresentPw = findViewById(R.id.editTextTextPassword4); // 현재 비밀번호 확인
        EditText newPw = findViewById(R.id.editTextTextPassword5); // 새 비밀번호
        EditText checkNewPw = findViewById(R.id.editTextTextPassword6); // 새 비밀번호 확인
        TextView dangerPresentPw = findViewById(R.id.textView260); // 현재 비밀번호 일치 경고
        TextView dangerNewPw = findViewById(R.id.textView261); // 새 비밀번호 일치 경고

        dangerPresentPw.setVisibility(View.GONE);
        dangerNewPw.setVisibility(View.GONE);

        backBtn.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() { // 홈페이지 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent homeBtnIntent = new Intent(ChangeInformation.this, MainActivity.class);
                homeBtnIntent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                homeBtnIntent.putExtra("memberName", getIntent().getStringExtra("memberName"));
                startActivity(homeBtnIntent);
                finish();
            }
        });

        Name.setText(getIntent().getStringExtra("memberName"));
        Id.setText(getIntent().getStringExtra("memberID"));


        changeImage.setOnClickListener(new View.OnClickListener() { // 사진 선택하기를 눌렀을 때
            @Override
            public void onClick(View v) {
                Intent changeImageIntent = new Intent(Intent.ACTION_PICK);
                changeImageIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); // 이미지 타입과 이미지를 가진 uri만 불러옴
                startActivityForResult(changeImageIntent, GET_GALLERY_IMAGE);
            }
        });

        presentPw.addTextChangedListener(new TextWatcher() { // 현재 비밀번호
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = presentPw.getText().toString();

                if(pw.equals("")){
                    dangerPresentPw.setVisibility(View.VISIBLE);
                    dangerPresentPw.setText("비밀번호를 입력해주세요");
                    dangerPresentPw.setTextColor(Color.parseColor("#cc0000"));
                    okPresentPw = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pw = presentPw.getText().toString();
                String checkPw = checkPresentPw.getText().toString();

                Response.Listener<String> response_listener = new Response.Listener<String>() { // 아이디를 통해서 서버에서 기타 정보를 가져오는 것
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String userName = jsonObject.getString("memberName");
                                String userEmail = jsonObject.getString("email");
                                Name.setText(userName);
                                Email.setText(userEmail);
                                realPw = jsonObject.getString("password");
                            }else{
                                Toast.makeText(ChangeInformation.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                String userID = getIntent().getStringExtra("memberID");
                InformationRequest informationRequest = new InformationRequest(userID, response_listener);
                RequestQueue requestQueue = Volley.newRequestQueue(ChangeInformation.this);
                requestQueue.add(informationRequest);

                if(pw.equals("")){
                    dangerPresentPw.setVisibility(View.VISIBLE);
                    dangerPresentPw.setText("비밀번호를 입력해주세요");
                    dangerPresentPw.setTextColor(Color.parseColor("#cc0000"));
                    okPresentPw = false;
                }else if (pw.equals(checkPw) && pw.equals(realPw)) { // 일치 했을 경우
                    dangerPresentPw.setVisibility(View.VISIBLE);
                    dangerPresentPw.setText("일치");
                    dangerPresentPw.setTextColor(Color.parseColor("#00C853"));
                    okPresentPw = true;
                } else {
                    dangerPresentPw.setVisibility(View.VISIBLE);
                    dangerPresentPw.setText("비밀번호가 맞지 않습니다.");
                    dangerPresentPw.setTextColor(Color.parseColor("#cc0000"));
                    okPresentPw = false;
                }
            }
        });

        checkPresentPw.addTextChangedListener(new TextWatcher() { // 현재 비밀번호 확인
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pw = presentPw.getText().toString();
                String checkPw = checkPresentPw.getText().toString();

                Response.Listener<String> response_listener = new Response.Listener<String>() { // 아이디를 통해서 서버에서 기타 정보를 가져오는 것
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                String userName = jsonObject.getString("memberName");
                                String userEmail = jsonObject.getString("email");
                                Name.setText(userName);
                                Email.setText(userEmail);
                                realPw = jsonObject.getString("password");
                            }else{
                                Toast.makeText(ChangeInformation.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                String userID = getIntent().getStringExtra("memberID");
                InformationRequest informationRequest = new InformationRequest(userID, response_listener);
                RequestQueue requestQueue = Volley.newRequestQueue(ChangeInformation.this);
                requestQueue.add(informationRequest);

                if(pw.equals("")){
                    dangerPresentPw.setVisibility(View.VISIBLE);
                    dangerPresentPw.setText("비밀번호를 입력해주세요");
                    dangerPresentPw.setTextColor(Color.parseColor("#cc0000"));
                    okPresentPw = false;
                }else if (pw.equals(checkPw) && pw.equals(realPw)) { // 일치 했을 경우
                    dangerPresentPw.setVisibility(View.VISIBLE);
                    dangerPresentPw.setText("일치");
                    dangerPresentPw.setTextColor(Color.parseColor("#00C853"));
                    okPresentPw = true;
                } else {
                    dangerPresentPw.setVisibility(View.VISIBLE);
                    dangerPresentPw.setText("비밀번호가 맞지 않습니다.");
                    dangerPresentPw.setTextColor(Color.parseColor("#cc0000"));
                    okPresentPw = false;
                }
            }
        });


        newPw.addTextChangedListener(new TextWatcher() { // 새 비밀번호
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = newPw.getText().toString();

                if(pw.equals("")){
                    dangerNewPw.setVisibility(View.VISIBLE);
                    dangerNewPw.setText("비밀번호를 입력해주세요");
                    dangerNewPw.setTextColor(Color.parseColor("#cc0000"));
                    okNewPw = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pw = newPw.getText().toString();
                String checkPw = checkNewPw.getText().toString();

                if(pw.equals("")){
                    dangerNewPw.setVisibility(View.VISIBLE);
                    dangerNewPw.setText("비밀번호를 입력해주세요");
                    dangerNewPw.setTextColor(Color.parseColor("#cc0000"));
                    okNewPw = false;
                }else if (pw.equals(checkPw)) { // 일치 했을 경우
                    dangerNewPw.setVisibility(View.VISIBLE);
                    dangerNewPw.setText("일치");
                    dangerNewPw.setTextColor(Color.parseColor("#00C853"));
                    okNewPw = true;
                } else {
                    dangerNewPw.setVisibility(View.VISIBLE);
                    dangerNewPw.setText("비밀번호가 맞지 않습니다.");
                    dangerNewPw.setTextColor(Color.parseColor("#cc0000"));
                    okNewPw = false;
                }
            }
        });

        checkNewPw.addTextChangedListener(new TextWatcher() { // 새 비밀번호 확인
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pw = newPw.getText().toString();
                String checkPw = checkNewPw.getText().toString();

                if(pw.equals("")){
                    dangerNewPw.setVisibility(View.VISIBLE);
                    dangerNewPw.setText("비밀번호를 입력해주세요");
                    dangerNewPw.setTextColor(Color.parseColor("#cc0000"));
                    okNewPw = false;
                }else if (pw.equals(checkPw)) { // 일치 했을 경우
                    dangerNewPw.setVisibility(View.VISIBLE);
                    dangerNewPw.setText("일치");
                    dangerNewPw.setTextColor(Color.parseColor("#00C853"));
                    okNewPw = true;
                } else {
                    dangerNewPw.setVisibility(View.VISIBLE);
                    dangerNewPw.setText("비밀번호가 맞지 않습니다.");
                    dangerNewPw.setTextColor(Color.parseColor("#cc0000"));
                    okNewPw = false;
                }
            }
        });
        
        changeInformationBtn.setOnClickListener(new View.OnClickListener() { // 수정하기 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangeInformation.this);
                if(okPresentPw && okNewPw) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() { // 서버에서 수정을 요청한 후 답변
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    dialog = alertDialog.setMessage("비밀번호가 변경되었습니다.").setPositiveButton("확인",null).create();
                                    dialog.show();
                                    presentPw.setText("");
                                    checkPresentPw.setText("");
                                    newPw.setText("");
                                    checkNewPw.setText("");
                                    dangerPresentPw.setVisibility(View.GONE);
                                    dangerNewPw.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(ChangeInformation.this, "다시 시도해주십시오.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(getIntent().getStringExtra("memberID"), checkNewPw.getText().toString(), responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(ChangeInformation.this);
                    requestQueue.add(changePasswordRequest);
                }else{
                    dialog = alertDialog.setMessage("비밀번호를 변경할 수 없습니다.").setPositiveButton("확인",null).create();
                    dialog.show();
                }
            }
        });
//
//        //권한 연습 코드
//        /*if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){ // 승인을 받지 못했을 때
//            Toast.makeText(this,"사진을 선택하기 위해서 사진첩에 대한 권한이 필요합니다.",Toast.LENGTH_LONG).show();
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){ // 전에 권한 요청을 하였는지 확인하고 하였으면 true, 아니면 false
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_CODE);
//            }else{
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_CODE); // 사용자에게 권한 요청
//            }
//        }*/
//
        Response.Listener<String> response_listener = new Response.Listener<String>() { // 아이디를 통해서 서버에서 기타 정보를 가져오는 것
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        String userName = jsonObject.getString("memberName");
                        String userEmail = jsonObject.getString("email");
                        Name.setText(userName);
                        Email.setText(userEmail);
                        realPw = jsonObject.getString("password");
                    }else{
                        Toast.makeText(ChangeInformation.this, "정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        String userID = getIntent().getStringExtra("memberID");
        InformationRequest informationRequest = new InformationRequest(userID, response_listener);
        RequestQueue requestQueue = Volley.newRequestQueue(ChangeInformation.this);
        requestQueue.add(informationRequest);
    }

    // 권한 연습 코드
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){ // 권한에 대한 응답이 있을 때 작동하는 메소드
        if(requestCode == 1){ // 권한을 허용하였을 때
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "승인 되었습니다.",Toast.LENGTH_LONG).show();
            }
        }else{
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 사진을 선택하고 그에 따른 답변을 기다리는 메소드
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri selectedImageUri = data.getData();
            changeImageView.setImageURI(selectedImageUri);
        }
    }
}