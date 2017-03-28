package com.lilan.lanruihuanbao;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2016/12/14.
 */

public class YingYongFragment extends Fragment {
    SharedPreferences sp;//取loginname
    String name;
    String username;
    LinearLayout lay_yyyy1;
    LinearLayout lay_yyyy3;
    LinearLayout lay_gdyy;
    String acctName = "";
    LinearLayout lay11;
    LinearLayout lay12;
    LinearLayout lay13;
    LinearLayout lay14;
    LinearLayout lay21;
    LinearLayout lay22;
    LinearLayout lay23;
    LinearLayout lay24;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yingyong,container,false);
        lay_yyyy1 = (LinearLayout) view.findViewById(R.id.lay_yyyy1_fra);
        lay_yyyy3 = (LinearLayout) view.findViewById(R.id.lay_yyyy3_fra);
        lay_gdyy = (LinearLayout) view.findViewById(R.id.main_gdyy_fra);
        lay11 = (LinearLayout) view.findViewById(R.id.lay11_fra);
        lay12 = (LinearLayout) view.findViewById(R.id.lay12_fra);
        lay13 = (LinearLayout) view.findViewById(R.id.lay13_fra);
        lay14 = (LinearLayout) view.findViewById(R.id.lay14_fra);
        lay21 = (LinearLayout) view.findViewById(R.id.lay21_fra);
        lay22 = (LinearLayout) view.findViewById(R.id.lay22_fra);
        lay23 = (LinearLayout) view.findViewById(R.id.lay23_fra);
        lay24 = (LinearLayout) view.findViewById(R.id.lay24_fra);
        ArrayList<LinearLayout> arrayList = new ArrayList<>();
        arrayList.add(lay11);
        arrayList.add(lay12);
        arrayList.add(lay13);
        arrayList.add(lay14);
        arrayList.add(lay21);
        arrayList.add(lay22);
        arrayList.add(lay23);
        arrayList.add(lay24);
        lay_gdyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplication(),Activity_appmore.class);
                startActivity(intent);
            }
        });
        sp = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        name = sp.getString("loginname","");
        username = sp.getString("userName","");
        getconn(arrayList);
        return view;
    }
    public void getconn(final ArrayList<LinearLayout> arrayList){
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
        LinearLayout view = new LinearLayout(getActivity().getApplicationContext());
        view.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams vlp2 = new ViewGroup.LayoutParams(getPixelsFromDp(60),getPixelsFromDp(60));
        view.setLayoutParams(lp);
        view.setOrientation(LinearLayout.VERTICAL);
        ImageView img = new ImageView(getActivity().getApplicationContext());
        img.setLayoutParams(vlp2);
        img.setPadding(10,10,10,10);
        //AsyncImageLoader.setImageViewFromUrl(iconurl, img);
        //用ImageLoader加载图片
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

                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getActivity().getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(getActivity(), "点击了OA", Toast.LENGTH_SHORT).show();
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
            case "zlgl":
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getActivity().getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
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
                                Log.w("传过去的数据：",accname+"---"+name);
                                try {
                                    startActivityForResult(intent, getActivity().RESULT_OK);
                                } catch (Exception e) {
                                    //Toast.makeText(getActivity(), "还未安装质量监督系统，立即下载吗？", Toast.LENGTH_SHORT).show();
                                    //写下载质量监督的代码，下载URL部署好以后给你
                                    new UpdateVersionUtil(getActivity(),"http://211.149.223.11:8080/process/app/qc.apk").showDownloadDialog();
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

                        };

                });
                break;
            case "PASGPM":
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getActivity().getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
                //img.setBackgroundResource(R.mipmap.email);
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
            case "email":
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getActivity().getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
                //img.setBackgroundResource(R.mipmap.email);
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
            case "pasg":
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getActivity().getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
                //img.setBackgroundResource(R.mipmap.pasg);
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
            case "monitor":
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getActivity().getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
                // img.setBackgroundResource(R.mipmap.monitor);
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
                ImageLoader.getInstance().displayImage(iconurl, img,new ImageLoaderPicture(getActivity().getApplicationContext()).getOptions(),new SimpleImageLoadingListener());
                //img.setBackgroundResource(R.mipmap.pytp);
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
        TextView tx = new TextView(getActivity());
        tx.setLayoutParams(vlp);
        tx.setText(text);
        view.addView(img);
        view.addView(tx);

        return view;
    }

    private int getPixelsFromDp(int size){

        DisplayMetrics metrics =new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

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
                intent.setClass(getActivity(),Activity_apps_pasg.class);
            }else{
                intent.setClass(getActivity(),Activity_apps.class);
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
