package com.lilan.lanruihuanbao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/28.
 */

public class Activity_apps extends Activity {
    WebView webview ;
    ImageView web_back;
    ImageView web_acc;
    String[] item ;
    Map<String,String> accmap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_apps);
        web_back = (ImageView) findViewById(R.id.web_back);
        web_acc = (ImageView) findViewById(R.id.web_acc);
        web_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        web_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_apps.this);
                builder.setTitle("请选择账号：");

                builder.setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(Activity_apps.this,"点击了:"+item[which],Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        initView(accmap.get(item[which]));
                    }
                });
                builder.show();
            }
        });
        webview = (WebView) findViewById(R.id.web_apps);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        //String url = bundle.getString("url");
        List<String> acclist = new ArrayList();
        acclist = intent.getStringArrayListExtra("accname");

        accmap = (HashMap)bundle.get("urlmap");
        for (int i=0;i<acclist.size();i++){
            Log.w("接收到list数据：",acclist.get(i).toString());
            Log.w("接收到map数据：",accmap.get(acclist.get(i).toString()));
        }
       item = new String[acclist.size()];
        for (int i=0;i<acclist.size();i++){
            item[i]=acclist.get(i);
        }
        String url = accmap.get(item[0]);
        initView(url);
    }
    private void initView(String url1){
        Log.w("appsurl:",url1);
        String url = url1;

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        //适应屏幕
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setBackgroundColor(Color.argb(0,0,0,0));
        // 设置可以支持缩放
        webview.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webview.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true);
//自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.loadUrl(url);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {  // goBack()表示返回WebView的上一页面
            webview.goBack();
            return true;
        } else {
            //结束当前页
            return super.onKeyDown(keyCode, event);
        }
    }
}
