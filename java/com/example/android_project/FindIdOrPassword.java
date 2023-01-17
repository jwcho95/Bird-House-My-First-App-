package com.example.android_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FindIdOrPassword extends AppCompatActivity {
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_or_password);
        ImageButton backBtn = (ImageButton) findViewById(R.id.imageButton22); // 뒤로가기 버튼
        EditText findID_name = (EditText) findViewById(R.id.editTextTextPersonName5); // 아이디 찾기에 필요한 이름
        EditText findID_email = (EditText) findViewById(R.id.editTextTextEmailAddress3); // 아이디 찾기에 필요한 이메일
        Button findID = (Button) findViewById(R.id.button25); // 아이디 찾기 버튼
        EditText findPw_name = (EditText) findViewById(R.id.editTextTextPersonName7); // 비밀번호 찾기에 필요한 이름
        EditText findPw_ID = (EditText) findViewById(R.id.editTextTextPersonName9); // 비밀번호 찾기에 필요한 아이디
        EditText findPw_email = (EditText) findViewById(R.id.editTextTextEmailAddress4); // 비밀번호 찾기에 필요한 이메일
        Button findPassword = (Button) findViewById(R.id.button28); // 비밀번호 찾기 버튼

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String require_name = findID_name.getText().toString();
                String require_email = findID_email.getText().toString();

                Response.Listener<String> response_listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FindIdOrPassword.this);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                String wanted_ID = jsonObject.getString("memberID");
                                dialog = alertDialog.setMessage("아이디는 "+wanted_ID+"입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                            } else {
                                dialog = alertDialog.setMessage("아이디가 존재하지 않습니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                return;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                FindIDRequest findIDRequest = new FindIDRequest(require_name, require_email, response_listener);
                RequestQueue requestQueue = Volley.newRequestQueue(FindIdOrPassword.this);
                requestQueue.add(findIDRequest);
            }
        });
        
        findPassword.setOnClickListener(new View.OnClickListener() { // 비밀번호 찾기
            @Override
            public void onClick(View v) {
                String requiredID = findPw_ID.getText().toString();
                String requiredName = findPw_name.getText().toString();
                String requiredEmail = findPw_email.getText().toString();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FindIdOrPassword.this);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                SendMail sendMail = new SendMail();
                                sendMail.sendSecurityCode(FindIdOrPassword.this, jsonObject.getString("email"), jsonObject.getString("userPw"));
                                dialog = alertDialog.setMessage("설정하신 이메일로 비밀번호를 확인하여 주십시요.").setPositiveButton("확인", null).create();
                                dialog.show();
                            }else{
                                dialog = alertDialog.setMessage("비밀번호를 찾을 수 없습니다.\n정보들을 다시 확인해주세요.").setPositiveButton("확인", null).create();
                                dialog.show();
                                return;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                FindPasswordRequest findPasswordRequest = new FindPasswordRequest(requiredName, requiredID, requiredEmail, responseListener);
                RequestQueue requestQueue = Volley.newRequestQueue(FindIdOrPassword.this);
                requestQueue.add(findPasswordRequest);
            }
        });
    }
}