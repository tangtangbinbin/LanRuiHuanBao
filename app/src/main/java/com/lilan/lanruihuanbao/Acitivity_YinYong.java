package com.lilan.lanruihuanbao;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class Acitivity_YinYong extends Activity {
    SharedPreferences sp;//取loginname
    ImageView back;
    String name;
    String username;
    LinearLayout lay_yyyy1;
    LinearLayout lay_yyyy3;
    LinearLayout main_gdyy;
    LinearLayout lay11;
    LinearLayout lay12;
    LinearLayout lay13;
    LinearLayout lay14;
    LinearLayout lay21;
    LinearLayout lay22;
    LinearLayout lay23;
    LinearLayout lay24;
    String acctName = "";
    ArrayList<LinearLayout> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_yinyong);
        back = (ImageView) findViewById(R.id.yy_back);
        lay_yyyy3 = (LinearLayout) findViewById(R.id.lay_yyyy3);
        lay_yyyy1 = (LinearLayout) findViewById(R.id.lay_yyyy1);
        main_gdyy = (LinearLayout) findViewById(R.id.main_gdyy);
        lay11 = (LinearLayout) findViewById(R.id.lay11);
        lay12 = (LinearLayout) findViewById(R.id.lay12);
        lay13 = (LinearLayout) findViewById(R.id.lay13);
        lay14 = (LinearLayout) findViewById(R.id.lay14);
        lay21 = (LinearLayout) findViewById(R.id.lay21);
        lay22 = (LinearLayout) findViewById(R.id.lay22);
        lay23 = (LinearLayout) findViewById(R.id.lay23);
        lay24 = (LinearLayout) findViewById(R.id.lay24);

        arrayList.add(lay11);
        arrayList.add(lay12);
        arrayList.add(lay13);
        arrayList.add(lay14);
        arrayList.add(lay21);
        arrayList.add(lay22);
        arrayList.add(lay23);
        arrayList.add(lay24);

        main_gdyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Acitivity_YinYong.this,Activity_appmore.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取登录成功时的loginname
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        name = sp.getString("loginname","");
        username = sp.getString("userName","");
        getconn();

    }
    public void getconn(){
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                try {
                    JSONArray jsonObjs = new JSONObject(result).getJSONArray("children");
                    for(int i = 0; i < jsonObjs.length() ; i++){
                        JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));
                        String text = jsonObj.getString("text");
                        String state = jsonObj.getString("state");
                        String iconurl = jsonObj.getString("iconurl");
                        String code = jsonObj.getString("code");
                        Log.w("解析的数据：",text+state+iconurl+code);
                        View v = createView(text,state,iconurl,code);
                            arrayList.get(i).addView(v);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                String result = GetApps.sendPostRequest(name);
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }

    public View createView(String text,String state,String iconurl,String code){
        LinearLayout view = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams vlp2 = new ViewGroup.LayoutParams(200,200);
        view.setLayoutParams(lp);

        view.setOrientation(LinearLayout.VERTICAL);
        ImageView img = new ImageView(this);
        img.setLayoutParams(vlp2);
        img.setPadding(10,10,10,10);
        /*final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String)msg.obj;
                //Toast.makeText(getApplicationContext(),"getappsacc:"+result,Toast.LENGTH_SHORT).show();
            }
        };*/
        switch (code){
            case "oa":
                //img.setBackgroundResource(R.mipmap.oa);
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getApplicationContext()).getOptions(),new SimpleImageLoadingListener());

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                //Message msg = new Message();
                                geturl("oa");
                               // msg.obj = result;
                               // handler.sendMessage(msg);
                            }
                        };
                        new Thread(runnable).start();
                        /*Runnable runnable1 = new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                    String appurl = GetAppsurl.sendPostRequest(name,"oa",acctName);

                                    Log.w("appurl:",appurl);
                                //handler.sendMessage(msg);
                            }
                        };
                        new Thread(runnable1).start();*/

                    }
                });
                break;
            case "email":
                //img.setBackgroundResource(R.mipmap.email);
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                geturl("email");
                            }
                        };
                        new Thread(runnable).start();
                    }
                });
                break;
            case "PASGPM":
                //img.setBackgroundResource(R.mipmap.email);
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                geturl("PASGPM");
                            }
                        };
                        new Thread(runnable).start();
                    }
                });
                break;
            case "pasg":
                //img.setBackgroundResource(R.mipmap.pasg);
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getApplicationContext()).getOptions(),new SimpleImageLoadingListener());

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                geturl("pasg");
                            }
                        };
                        new Thread(runnable).start();
                    }
                });
                break;
            case "zlgl":
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getApplicationContext()).getOptions(),new SimpleImageLoadingListener());

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler() {
                            public void handleMessage(Message msg) {
                                String accname = (String) msg.obj;
                                Intent intent = new Intent();
                                ComponentName cn = new ComponentName("com.zcy.qualitysupervisionandmanagement",
                                        "com.zcy.qualitysupervisionandmanagement.activity.viewImpl.LoginActivity");
                                intent.setComponent(cn);
                                intent.setAction("android.intent.action.MAIN");
                                intent.putExtra("userName", accname);
                                intent.putExtra("unifyUserName", name);
                                try {
                                    startActivityForResult(intent, RESULT_OK);
                                } catch (Exception e) {
                                    //Toast.makeText(getActivity(), "还未安装质量监督系统，立即下载吗？", Toast.LENGTH_SHORT).show();
                                    //写下载质量监督的代码，下载URL部署好以后给你
                                    new UpdateVersionUtil(Acitivity_YinYong.this,"http://211.149.223.11:8080/process/app/qc.apk").showDownloadDialog();
                                }
                            }};
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                String result = GetAppsAcc.sendPostRequest(name,"zlgl");
                                JSONArray jsonObjs = null;
                                try {
                                    jsonObjs = new JSONObject(result).getJSONArray("children");
                                    for(int i = 0; i < jsonObjs.length() ; i++){
                                        JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));
                                        acctName = jsonObj.getString("acctName");
                                    }
                                    Message msg = new Message();
                                    msg.obj = acctName;
                                    handler.sendMessage(msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        new Thread(runnable).start();
                    }
                });
                break;
            case "monitor":
                // img.setBackgroundResource(R.mipmap.monitor);
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getApplicationContext()).getOptions(),new SimpleImageLoadingListener());

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                geturl("monitor");
                            }
                        };
                        new Thread(runnable).start();
                    }
                });
                break;
            case "pytp":
                //img.setBackgroundResource(R.mipmap.pytp);
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getApplicationContext()).getOptions(),new SimpleImageLoadingListener());

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                geturl("pytp");
                            }
                        };
                        new Thread(runnable).start();
                    }
                });
                break;
        }
        TextView tx = new TextView(this);
        tx.setLayoutParams(vlp);
        tx.setText(text);
        view.addView(img);
        view.addView(tx);

        return view;
    }
    public void geturl(String code){
        String result = GetAppsAcc.sendPostRequest(name,code);

        Log.w("getappsacc:",result);
        try {
            JSONArray jsonObjs = new JSONObject(result).getJSONArray("children");
            List acclist = new ArrayList();
            HashMap<String,String> urlmap = new HashMap<String,String>();
            for(int i = 0; i < jsonObjs.length() ; i++){
                JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));
                acctName = jsonObj.getString("acctName");
                acclist.add(acctName);
                String appurl = GetAppsurl.sendPostRequest(name,code,acctName);
                urlmap.put(acctName,appurl);
            }
            for (int i=0;i<acclist.size();i++){
                Log.w("发送前list数据：",acclist.get(i).toString());
                Log.w("发送前map数据：",urlmap.get(acclist.get(i).toString()));
            }

            // String appurl = GetAppsurl.sendPostRequest(name,code,acctName);
            Intent intent = new Intent();
            if (code.equals("pasg")){
                intent.setClass(this,Activity_apps_pasg.class);
            }else{
                intent.setClass(this,Activity_apps.class);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("urlmap",urlmap);
            intent.putExtras(bundle);
            intent.putStringArrayListExtra("accname", (ArrayList<String>) acclist);
            startActivity(intent);

            //Log.w("appurl:",appurl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
