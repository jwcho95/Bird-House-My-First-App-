package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class FindAddressActivity extends AppCompatActivity {

    private WebView daum_webView;

    private Handler handler;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_address);

        sharedPreferences = getSharedPreferences("Payment", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // WebView 초기화

        init_webView();


        // 핸들러를 통한 JavaScript 이벤트 반응

        handler = new Handler();


    }

    @Override
    protected void onResume() {
        super.onResume();
        WebSettings settings = daum_webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FindAddressActivity.this, Payment.class);
        intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
        intent.putExtra("page", "addressPage");
        startActivity(intent);
        finish();
    }

    public void init_webView() {

        // WebView 설정

        daum_webView = (WebView) findViewById(R.id.daum_webview);


        // JavaScript 허용

        daum_webView.getSettings().setJavaScriptEnabled(true);


        // JavaScript의 window.open 허용

        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌

        daum_webView.addJavascriptInterface(new AndroidBridge(), "TestApp");


        // web client 를 chrome 으로 설정

        daum_webView.setWebChromeClient(new HelloWebChromeClient());


        // webview url load. php 파일 주소

        daum_webView.loadUrl("http://192.168.0.46/daum_address.php");

    }

    private class AndroidBridge {

        @JavascriptInterface

        public void setAddress(final String arg1, final String arg2, final String arg3) {

            handler.post(new Runnable() {

                @Override

                public void run() {

//                    daum_result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));

                    Intent intent = new Intent(FindAddressActivity.this, Payment.class);
//                    intent.putExtra("address", String.format("(%s) %s %s", arg1, arg2, arg3));
                    editor.putString("destination", String.format("(%s) %s %s", arg1, arg2, arg3));
                    editor.commit();
                    intent.putExtra("memberID", getIntent().getStringExtra("memberID"));
                    intent.putExtra("page", "addressPage");
                    startActivity(intent);

                    // WebView를 초기화 하지않으면 재사용할 수 없음

                    init_webView();

                    finish();

                }

            });

        }

    }

    public class HelloWebChromeClient extends WebChromeClient{
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView newWebView = new WebView(FindAddressActivity.this);
            WebSettings webSettings = newWebView.getSettings();

            webSettings.setJavaScriptEnabled(true);

            final Dialog dialog = new Dialog(FindAddressActivity.this);
            dialog.setContentView(newWebView);

            ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
            dialog.show();

            newWebView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onCloseWindow(WebView window) {
                    dialog.dismiss();
                }
            });

            ((WebView.WebViewTransport)resultMsg.obj).setWebView(newWebView);
            resultMsg.sendToTarget();
            return true;
        }
    }
}