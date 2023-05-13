package com.example.android_project;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://IP주소/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userName, String userId, String userPw, String userEmail, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("memberName", userName);
        map.put("memberID", userId);
        map.put("password", userPw);
        map.put("email", userEmail);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}
