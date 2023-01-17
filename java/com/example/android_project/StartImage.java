package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;

public class StartImage extends AppCompatActivity{

    private String[] permissionsList = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    }; // 권한이 필요한 모든 권한들
    private final int MULTI_PERMISSIONS = 1000;
    ArrayList allPermissionList;

    public boolean checkPermissions(){ // 권한을 확인 // true이면 승인받아야할 권한이 있음을 의미
        int results;
        allPermissionList = new ArrayList<>();

        for(String pm : permissionsList){
            results = ContextCompat.checkSelfPermission(this, pm);
            if(results != PackageManager.PERMISSION_GRANTED){
                allPermissionList.add(pm);
            }
        }

        if(!allPermissionList.isEmpty())
            return true;
        else return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_image);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(checkPermissions() == true){
            Intent firstCome = new Intent(StartImage.this, CheckPermissions.class);
            startActivity(firstCome);
            finish();
        }else{
            Intent notFirstCome = new Intent(StartImage.this, LoginImage.class);
            startActivity(notFirstCome);
            finish();
        }
    }
}