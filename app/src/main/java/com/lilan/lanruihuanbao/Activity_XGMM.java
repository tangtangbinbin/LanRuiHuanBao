package com.lilan.lanruihuanbao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/22.
 */

public class Activity_XGMM extends Activity {
    SharedPreferences sp;
    String name;
    ImageView back;
    EditText oldpass;
    EditText newpass;
    EditText conpass;
    Button updatebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wo_xgmm);
        back = (ImageView) findViewById(R.id.xgmm_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        oldpass = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpass);
        conpass = (EditText) findViewById(R.id.conpass);
        updatebtn = (Button) findViewById(R.id.update_btn);
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        name = sp.getString("loginname","");

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String old = oldpass.getText().toString();
                final String news = newpass.getText().toString();
                String con = conpass.getText().toString();
                if(news.length()>7){
                    if (news.length()<17){
                        if(news.equals(con)){
                            final Handler handler = new Handler() {
                                public void handleMessage(Message msg) {
                                    String result = (String) msg.obj;
                                    Toast.makeText(getApplicationContext(),"修改结果："+result,Toast.LENGTH_SHORT).show();
                                }};
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    String result = UpdatePWD.sendPostRequest(name,old,news);
                                    Log.w("修改密码结果：",result);
                                    String code;
                                    String msg1 = "";

                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        code = jsonObject.getString("code");
                                        msg1 = jsonObject.getString("msg");
                                        if (code.equals("30000")){
                                            SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();
                                            editor.putString("password", "");
                                            editor.commit();
                                            Intent intent = new Intent();
                                            intent.setClass(Activity_XGMM.this,LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Message msg = new Message();
                                    msg.obj = msg1;
                                    handler.sendMessage(msg);
                                }
                            };
                            new Thread(runnable).start();
                        }else{
                            Toast.makeText(getApplicationContext(),"两次输入的新密码不一致",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"密码输入过长",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"密码输入过短",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
