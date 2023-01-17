package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginImage extends AppCompatActivity {
    private TextInputEditText userId;
    private TextInputEditText userPw;
    private long btnTime = 0;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth = null;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_image);
        Button signUpBtn = (Button) findViewById(R.id.button6); // 뒤로가기 버튼
        userId = (TextInputEditText) findViewById(R.id.User_ID); // 유저 아이디
        userPw = (TextInputEditText) findViewById(R.id.User_Pw); // 유저 패스워드
        TextInputLayout password = (TextInputLayout) findViewById(R.id.textInputLayout3); // 비밀번호 입력란
        password.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE); // 비밀번호 입력란에 있는 눈 모양 토글
        Button loginBtn = (Button) findViewById(R.id.button5); // 로그인 버튼
        TextView findIdOrPassword = (TextView) findViewById(R.id.textView133); // 아이디 및 비밀번호 찾기
        CheckBox saveID = (CheckBox) findViewById(R.id.checkBox); // 아이디 저장
        CheckBox autoLogin = (CheckBox) findViewById(R.id.checkBox2); // 자동 로그인

        preferences = getSharedPreferences("MyKey", MODE_PRIVATE);
        editor = preferences.edit();

        userId.setText(preferences.getString("MyKey", ""));



        if(preferences.getBoolean("saved",false)){ // 아이디 저장 체크
            saveID.setChecked(true);
        }

        if(!preferences.getBoolean("returnToLogin", true)){
            autoLogin.setChecked(true);
        }



//        /*구글 로그인이 필요할 시 사용*/
//        signInButton = findViewById(R.id.signInButton); // 구글 로그인 버튼
//        mAuth = FirebaseAuth.getInstance(); // FirebaseAuth를 사용하기 위한 인스턴스를 받아오는 작업
//
//        /* 구글 로그인 */
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, RC_SIGN_IN);
//            }
//        });

        signUpBtn.setOnClickListener(new View.OnClickListener() { // 회원가입 버튼
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginImage.this, SignUp.class);
                startActivity(signUpIntent);
            }
        });

        userPw.setOnKeyListener(new View.OnKeyListener() { // 비밀번호 입력 후 엔터키 처리
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        loginBtn.requestFocus();
                        return true;
                }
                return false;
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() { // 로그인 버튼을 눌렀을 때
            @Override
            public void onClick(View v) {
                String userID = userId.getText().toString(); // 유저 아이디
                String userPassword = userPw.getText().toString(); // 유저 패스워드

                if(saveID.isChecked()){
                    editor.putString("MyKey", userID);
                    editor.putBoolean("saved", true);
                    editor.commit();
                }else {
                    editor.putString("MyKey", "");
                    editor.putBoolean("saved", false);
                    editor.commit();
                }

                if(autoLogin.isChecked()){
                    editor.putString("MyKey", userID);
                    editor.putString("MyPw", userPassword);
                    editor.putBoolean("returnToLogin", false);
                    editor.commit();
                }else {
                    editor.commit();
                }

                Response.Listener<String> response_listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){ // 로그인에 성공한 경우
                                String Name = jsonObject.getString("memberName");
                                String userID = jsonObject.getString("memberID");
                                String userName = jsonObject.getString("memberName");
                                if(userID.equals("admin")){ // 관리자 계정
                                    Intent adminIntent = new Intent(LoginImage.this, AdminPage.class);
                                    adminIntent.putExtra("memberID", userID);
                                    adminIntent.putExtra("memberName", userName);
                                    startActivity(adminIntent);
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(), "환영합니다! " + Name + "님", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginImage.this, MainActivity.class);
                                    intent.putExtra("memberID", userID);
                                    intent.putExtra("memberName", userName);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"아이디 및 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, response_listener);
                RequestQueue requestQueue = Volley.newRequestQueue(LoginImage.this);
                requestQueue.add(loginRequest);
            }
        });

        findIdOrPassword.setOnClickListener(new View.OnClickListener() { // 아이디 및 비밀번호 찾기
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginImage.this, FindIdOrPassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button loginBtn = (Button) findViewById(R.id.button5);

        if(!preferences.getBoolean("returnToLogin", true)){
            userId.setText(preferences.getString("MyKey",""));
            userPw.setText(preferences.getString("MyPw", ""));
            loginBtn.performClick();
        }
    }

    @Override
    public void onBackPressed() {  // 뒤로가기 버튼을 눌렀을 때의 종료
        finishAffinity(); // 재실행해도 처음 화면으로 갈 수 있게 해준다. (어플의 루트 액티비티 제거)
        System.runFinalization(); // 현재 작업중인 쓰레드가 다 종료되면 종료시키라는 명령어
        System.exit(0);
    }


//    /* 구글 로그인이 필요할 시 사용 */
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }
//
//    private void updateUI(FirebaseUser user) { //update ui code here
//        if (user != null) {
//            Intent intent = new Intent(LoginImage.this, MainActivity.class);
//            Toast.makeText(LoginImage.this, "환영합니다!",Toast.LENGTH_SHORT).show();
//            startActivity(intent);
//            finish();
//        }
//    }
}