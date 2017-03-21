package com.lilan.lanruihuanbao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/10.
 */

public class Activity_appmore extends Activity {
    ImageView img_back;
    String name;
    SharedPreferences sp;//取loginname
    ListView listview;
    ArrayList<HashMap<String, Object>> listItem =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_appmore);
        listview = (ListView) findViewById(R.id.appmore_list);
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        img_back = (ImageView) findViewById(R.id.gdyy_back);
        listItem = new ArrayList<HashMap<String, Object>>();
        name = sp.getString("loginname","");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getinfo();
    }
    public void getinfo(){
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                String result = (String) msg.obj;
                try {
                    JSONArray jsonObjs = new JSONObject(result).getJSONArray("children");
                    for(int i = 0; i < jsonObjs.length() ; i++){
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));
                        String appName = jsonObj.getString("appName");
                        map.put("appName",appName);
                        String appDescribe = jsonObj.getString("appDescribe");
                        map.put("appDescribe",appDescribe);
                        String appCode = jsonObj.getString("appCode");
                        map.put("appCode",appCode);
                        String iconurl = jsonObj.getString("iconurl");
                        map.put("iconurl",iconurl);
                        listItem.add(map);
                        Log.w("解析的数据：",appName+appDescribe+appCode);
                    }
                    /*SimpleAdapter listitemadapter = new SimpleAdapter(getApplicationContext(),listItem,R.layout.appmore_item,
                            new String[]{"appName","appDescribe","img"},new int[]{R.id.appmore_title,R.id.appmore_desc,R.id.appmore_img});
                    */
                    MysAdapter adapter = new MysAdapter(getApplicationContext());
                    listview.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                String result = GetAccAppInfo.sendPostRequest(name);
                msg.obj = result;
                Log.w("app more:",result);
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }
    public class MysAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

        public MysAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listItem.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            MyListener myListener=new MyListener(position);
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.appmore_item, null);
                holder.title = (TextView)convertView.findViewById(R.id.appmore_title);
                holder.info = (TextView)convertView.findViewById(R.id.appmore_desc);
                holder.viewBtn = (Button)convertView.findViewById(R.id.appmore_btn);
                holder.img = (ImageView) convertView.findViewById(R.id.appmore_img);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.title.setText((String)listItem.get(position).get("appName"));
            holder.info.setText((String)listItem.get(position).get("appDescribe"));
            ImageLoader.getInstance().displayImage(listItem.get(position).get("iconurl").toString(), holder.img,new ImageLoaderPicture(getApplicationContext()).getOptions(),new SimpleImageLoadingListener());

            holder.viewBtn.setTag(position);
            //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
            holder.viewBtn.setOnClickListener( myListener);

            //holder.viewBtn.setOnClickListener(MyListener(position));

            return convertView;
        }
    }
    public int getdrawid(String code){
        switch (code){
            case "oa":
                return R.mipmap.oa;
            case "email":
                return R.mipmap.email;
            case "pasg":
                return R.mipmap.pasg;
            case "monitor":
                return R.mipmap.monitor;
            case "pytp":
                return R.mipmap.pytp;
        }
        return 0;
    }
    private class MyListener implements View.OnClickListener {
        int mPosition;
        public MyListener(int inPosition){
            mPosition= inPosition;
        }
        @Override
        public void onClick(View v) {
            //sendsms();
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(), listItem.get(mPosition).get("appName").toString()+"申请已发送", Toast.LENGTH_SHORT).show();
            /*Runnable runnable2 = new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), listItem.get(mPosition).get("appName").toString(), Toast.LENGTH_SHORT).show();

                    //Message msg = new Message();
                    String  result = AskForApp.sendPostRequest(name,getSharedPreferences("userinfo", MODE_PRIVATE).getString("userName",""),
                            listItem.get(mPosition).get("appCode").toString(),listItem.get(mPosition).get("appName").toString());
                    Log.w("askforapp:",result);
                    try {
                        JSONObject result2 =new JSONObject(result);
                        if (result2.getString("code").equals("1200")){
                            //Toast.makeText(getApplicationContext(),"申请发送成功",Toast.LENGTH_SHORT).show();
                            Log.w("askforappcode:",result);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            new Thread(runnable2).start();*/
        }

    }

    public void sendsms(){
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(Activity_appmore.this, "sendsms info", Toast.LENGTH_SHORT).show();
                //Message msg = new Message();
                    /*String  result = AskForApp.sendPostRequest(name,getSharedPreferences("userinfo", MODE_PRIVATE).getString("userName",""),
                            listItem.get(mPosition).get("appCode").toString(),listItem.get(mPosition).get("appName").toString());
                    Log.w("askforapp:",result);
                    try {
                        JSONObject result2 =new JSONObject(result);
                        if (result2.getString("code").equals("1200")){
                            Toast.makeText(getApplicationContext(),"申请发送成功",Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                }
            };
            new Thread(runnable2).start();
    }
    public final class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView info;
        public Button viewBtn;
    }

}
