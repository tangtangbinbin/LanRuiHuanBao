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
import android.view.Window;
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
 * Created by Administrator on 2016/12/20.
 */

public class Activity_apps_pasg extends Activity {
    private WebView webview;
    ImageView web_back;
    ImageView web_acc;
    String[] item ;
    Map<String,String> accmap = new HashMap<>();
    //https://exmail.qq.com/login http://m.exmail.qq.com/cgi-bin/loginpage
    //private static  final String URL="www.baidu.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_apps_pasg);
        web_acc = (ImageView) findViewById(R.id.webpasg_acc);
        web_back = (ImageView) findViewById(R.id.webpasg_back);
        webview = (WebView) findViewById(R.id.web_pasg);
        web_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        web_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_apps_pasg.this);
                builder.setTitle("请选择账号：");

                builder.setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(Activity_apps_pasg.this,"点击了:"+item[which],Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        initView(accmap.get(item[which]));
                    }
                });
                builder.show();
            }
        });
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
        String url = url1;
        webview.loadUrl(url);
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
