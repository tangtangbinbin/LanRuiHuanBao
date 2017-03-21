package com.lilan.lanruihuanbao;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.content.Context.MODE_PRIVATE;


public class ShouYeFragment extends Fragment {
    SharedPreferences sp;
    String name;
    String userName;
    String Alias;
    ArrayList<String> list;
    String acctName;
    private LinearLayout lay_syyy;
    private LinearLayout lay_email;
    private LinearLayout lay_xmgl;
    private LinearLayout lay_bgxt;
    private LinearLayout lay_zdgl;
    private LinearLayout lay_xmtb;
    Fragment yingyong;
    TextView tz_sy;
    LinearLayout lin_tz;
    TextView dbsx_sy;
    LinearLayout lin_dbsx;
    private static final String TAG = "JPush";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shouye,container,false);
        list = new ArrayList<String>();
        getcode();
        sp = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        name = sp.getString("loginname","");
        userName = sp.getString("userName","");
        Alias = sp.getString("Alias","");
        tz_sy = (TextView) view.findViewById(R.id.tz_sy);
        lin_tz = (LinearLayout) view.findViewById(R.id.lin_tz);
        dbsx_sy = (TextView) view.findViewById(R.id.dbsx_sy);
        lin_dbsx = (LinearLayout) view.findViewById(R.id.lin_dbsx);
        lay_email = (LinearLayout) view.findViewById(R.id.lay_yjxt);
        lay_xmgl = (LinearLayout) view.findViewById(R.id.lay_xmgl);
        lay_bgxt = (LinearLayout) view.findViewById(R.id.lay_bgxt);
        lay_zdgl = (LinearLayout) view.findViewById(R.id.lay_zdgl);
        lay_xmtb = (LinearLayout) view.findViewById(R.id.lay_xntb);
        lay_syyy = (LinearLayout) view.findViewById(R.id.lay_syyy);
        lay_syyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),Acitivity_YinYong.class);
                startActivity(intent);
            }
        });
        lay_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("email")){
                     getaccount("email");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lay_xmgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("pasg")){
                     getaccount("pasg");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tz_sy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("oa")){
                    getaccount("oa");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lin_dbsx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("oa")){
                    getaccount("oa");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lin_tz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("oa")){
                    getaccount("oa");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dbsx_sy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("oa")){
                    getaccount("oa");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lay_bgxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("oa")){
                     getaccount("oa");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lay_zdgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("monitor")){
                     getaccount("monitor");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
        lay_xmtb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpremission("xmtb")){
                     getaccount("xmtb");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"没有权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
//Toast.makeText(getActivity().getApplicationContext(),"当前别名为："+Alias,Toast.LENGTH_SHORT).show();
        if (Alias==null ||Alias==""){
           // Toast.makeText(getActivity().getApplicationContext(),"别名为空，设置为："+userName,Toast.LENGTH_SHORT).show();
            setAlias(userName);
        }
        if (!Alias.equals(userName)){
            //Toast.makeText(getActivity().getApplicationContext(),"别名："+Alias+"与用户名:"+userName+"不同，设置别名为："+userName,Toast.LENGTH_SHORT).show();
            setAlias(userName);
        }

        getOATongZhi();
        getOADBSX();
        getUserInfo();
        checkAPPUpdate();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        return view;
    }
    public void checkAPPUpdate(){
        final Handler mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        UpdateManager manager = new UpdateManager(getActivity());
                        // 检查软件更新
                        manager.checkUpdate();
                        break;
                }
            }
        };
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 0;
                mhandler.sendMessage(msg);
            }
        };
        new Thread(r).start();
    }
    private static final int MSG_SET_ALIAS = 1001;
    private void setAlias(String userName) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, userName));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                   // Toast.makeText(getActivity().getApplicationContext(),"别名设置成功！"+userName,Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("Alias", userName);
                    editor.commit();
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                        Log.i(TAG, "No network");
                    Toast.makeText(getActivity().getApplicationContext(),logs,Toast.LENGTH_SHORT).show();
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
                    Toast.makeText(getActivity().getApplicationContext(),logs,Toast.LENGTH_SHORT).show();
            }
        }

    };
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getActivity().getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    public void getOADBSX(){
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                try {
                    JSONArray jsonObjs = new JSONArray(result);
                    for(int i = 0; i < jsonObjs.length() ; i++){
                        String info = jsonObjs.get(i).toString();
                        //Toast.makeText(getActivity().getApplicationContext(),info,Toast.LENGTH_SHORT).show();
                        //Log.w("解析的OA数据1：",info);
                        View v = createView(info);
                        lin_dbsx.addView(v);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                String result = GetOADBSX.sendPostRequest(userName);
                msg.obj = result;
                //Log.w("解析的OA数据11：",result);
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }
    public void getOATongZhi(){
        final Handler handler1 = new Handler() {
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                try {
                    JSONArray jsonObjs = new JSONArray(result);
                    for(int i = 0; i < jsonObjs.length() ; i++){
                        String info = jsonObjs.get(i).toString();
                        //Toast.makeText(getActivity().getApplicationContext(),info,Toast.LENGTH_SHORT).show();
                        //Log.w("解析的OA数据21：",info);
                        View v = createView(info);
                        lin_tz.addView(v);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                String result = GetOATongZhi.sendPostRequest();
                msg.obj = result;
                //Log.w("解析的OA数据2：",result);
                handler1.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }
    public View createView(String text){
        LinearLayout view = new LinearLayout(getActivity().getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        view.setOrientation(LinearLayout.VERTICAL);

        TextView tx = new TextView(getActivity());
        tx.setLayoutParams(vlp);
        tx.setText(text);
        view.addView(tx);

        return view;
    }
    public void getUserInfo(){
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                //Message msg = new Message();
                String  result = GetUserInfo.sendPostRequest(getActivity().getSharedPreferences("userinfo", MODE_PRIVATE).getString("loginname",""),
                        getActivity().getSharedPreferences("usertoken", MODE_PRIVATE).getString("token",""));
                Log.w("getUserInfo:",result);
                try {
                    JSONObject userinfo =new JSONObject( new JSONObject(result).getString("userinfo"));
                    Log.w("userinfo_username:",userinfo.getString("userName"));
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userName", userinfo.getString("userName"));
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable2).start();
    }

    public void getaccount(final String code){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String result = GetAppsAcc.sendPostRequest(name,code);
                Log.w("checkacct:",result);
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
                       // Log.w("发送前list数据：",acclist.get(i).toString());
                       // Log.w("发送前map数据：",urlmap.get(acclist.get(i).toString()));
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
        };
        new Thread(runnable).start();
    }
    public void getcode(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String result = GetApps.sendPostRequest(name);
                try {
                    JSONArray jsonObjs = new JSONObject(result).getJSONArray("children");
                    for(int i = 0; i < jsonObjs.length() ; i++){
                        JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));
                        String code = jsonObj.getString("code");
                        list.add(code);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
    public boolean checkpremission(String code){
        for(String  a : list){
            if (a.equals(code)){
                return  true;
            }
        }
        return false;
    }



}