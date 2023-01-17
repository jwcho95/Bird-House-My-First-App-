package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckPermissions extends AppCompatActivity {

    boolean goNext = true;

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

    public void requestPermissions(){ // 권한들을 하나의 배열로 묶어 하나씩 승인 받게 함
        String[] rePermissionsArray = new String[allPermissionList.size()];
        rePermissionsArray = (String[]) allPermissionList.toArray(rePermissionsArray);
        ActivityCompat.requestPermissions(this, rePermissionsArray, MULTI_PERMISSIONS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permissions);
        Button nextBtn = (Button) findViewById(R.id.button23);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermissions() == true){
                    requestPermissions();
                    for(int i = 0; i < permissionsList.length; i++) {
                        if(checkSelfPermission(permissionsList[i]) == PackageManager.PERMISSION_DENIED){
                            Toast.makeText(getApplicationContext(),"권한을 승인해주셔야 어플 사용이 가능합니다. 다음 화면으로 넘어가지 않으시면 설정->앱에서 권한을 확인해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    for(int i = 0; i < permissionsList.length; i++) {
                        if(checkSelfPermission(permissionsList[i]) != PackageManager.PERMISSION_GRANTED){
                            goNext = false;
                        }
                    }
                    if(goNext){
                        Intent goNextIntent = new Intent(CheckPermissions.this, LoginImage.class);
                        startActivity(goNextIntent);
                        finish();
                    }
                }
            }
        });
    }
}