package com.lilan.lanruihuanbao;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioGroup;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Activity{

    Fragment shouye;
    Fragment yingyong;
    Fragment wode;
    RadioGroup rg;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        //获取sp存储的数据
        String spacc = sp.getString("loginname", "");
        if ((spacc.equals(""))){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }
       /*
          动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);

                }
            }
        }
        initview();
    }

    public void initview(){
        getFragmentManager().beginTransaction().replace(R.id.main_content,new ShouYeFragment()).commit();
        rg = (RadioGroup) findViewById(R.id.tab_menu);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btn_shouye:
                        if (shouye==null)
                            shouye = new ShouYeFragment();
                        getFragmentManager().beginTransaction().replace(R.id.main_content,shouye).commit();
                        break;
                    case R.id.btn_yingyong:
                        if (yingyong==null)
                            yingyong = new YingYongFragment();
                        getFragmentManager().beginTransaction().replace(R.id.main_content,yingyong).commit();
                        break;
                    case R.id.btn_wode:
                        if (wode==null)
                            wode = new WoDeFragment();
                        getFragmentManager().beginTransaction().replace(R.id.main_content,wode).commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
