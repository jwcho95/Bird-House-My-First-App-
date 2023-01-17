package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {
    private EditText et_Name;
    private EditText et_ID;
    private EditText et_Pw;
    private EditText check_et_Pw;
    private AlertDialog dialog; // 확인 메세지를 띄워주는 변수
    private boolean checkValidateOk = false; // 중복 확인
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ImageButton backBtn = (ImageButton) findViewById(R.id.imageButton); // 뒤로가기 버튼
        Button submit = (Button) findViewById(R.id.button); // 가입하기 버튼
        Button checkValidate = (Button) findViewById(R.id.button21); // 중복확인 버튼
        et_Name = findViewById(R.id.editTextTextPersonName3); // 유저의 이름
        et_ID = findViewById(R.id.editTextTextPersonName); // 유저의 아이디
        et_Pw = findViewById(R.id.editTextTextPassword2); // 유저의 패스워드
        check_et_Pw = findViewById(R.id.editTextTextPassword3); // 패스워드 확인
        EditText et_email = findViewById(R.id.editTextTextEmailAddress2); // 이메일
        
        backBtn.setOnClickListener(new View.OnClickListener() { // 뒤로가기 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        checkValidate.setOnClickListener(new View.OnClickListener() { // 중복확인 버튼
            @Override
            public void onClick(View v) {
                String checkUserId = et_ID.getText().toString();

                if(checkValidateOk){
                    return;
                }
                if(et_ID.getText().toString().equals("")){
                    Toast.makeText(SignUp.this,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUp.this);
                                if(success) {
                                    dialog = alertDialog.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                                    dialog.show();
                                    et_ID.setEnabled(false); // 아이디 입력란을 더 이상 누르지 못하게 바뀜
                                    checkValidateOk = true; // 중복 확인 되었음
                                    checkValidate.setBackgroundColor(Color.parseColor("#8BC34A"));
                                    checkValidate.setText("확인");
                                }else{
                                    dialog = alertDialog.setMessage("사용할 수 없는 아이디입니다.").setPositiveButton("확인", null).create();
                                    dialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Validate validate = new Validate(checkUserId, responseListener);
                    RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                    requestQueue.add(validate);
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() { // 회원가입 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                String userName = et_Name.getText().toString();
                String userId = et_ID.getText().toString();
                String userPw = et_Pw.getText().toString();
                String userEmail = et_email.getText().toString();
                if(userName.equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(userId.equals("")){
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(checkValidateOk == false) {
                    Toast.makeText(getApplicationContext(), "아이디 중복을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if(userPw.equals("")){
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else if(userEmail.equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(userPw.equals(check_et_Pw.getText().toString())){
                    Response.Listener<String> response_listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                boolean success_email = jsonObject.getBoolean("success_email");
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUp.this);
                                if(success && success_email){ // 회원등록 성공한 경우
                                    Toast.makeText(getApplicationContext(),"회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUp.this, LoginImage.class);
                                    startActivity(intent);
                                    finish();
                                }else if(success) {
                                    dialog = alertDialog.setMessage("이미 사용하고 있는 이메일입니다.").setPositiveButton("확인",null).create();
                                    dialog.show();
                                    return;
                                }else{
                                    Toast.makeText(getApplicationContext(),"회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    // 실제 서버로 Volley를 이용하여 요청한다.
                    RegisterRequest registerRequest = new RegisterRequest(userName, userId, userPw, userEmail,response_listener);
                    RequestQueue queue = Volley.newRequestQueue(SignUp.this);
                    queue.add(registerRequest);
                }else {
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




}