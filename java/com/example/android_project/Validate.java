package com.example.android_project;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Validate extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "http://172.30.1.18/Validate.php";
    private Map<String, String> map;

    public Validate(String userId, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("memberID", userId);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError{
        return map;
    }
}
