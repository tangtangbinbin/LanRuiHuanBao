package com.lilan.lanruihuanbao;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.RadioGroup;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends Activity{

    Fragment shouye;
    Fragment yingyong;
    Fragment wode;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JPushInterface.init(this);
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
