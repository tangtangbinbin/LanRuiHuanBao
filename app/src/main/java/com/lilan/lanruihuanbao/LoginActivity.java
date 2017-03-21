package com.lilan.lanruihuanbao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/12/15.
 */

public class LoginActivity extends Activity {
    //必须使用，Activity启动页
    //private final static String lancherActivityClassName = Welcome.class.getName();
    Button btn;
    CheckBox cb;
    EditText acc;
    EditText pass;
    SharedPreferences sp;
    SharedPreferences userinfo;
    SharedPreferences usertoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btn = (Button) findViewById(R.id.login_btn);
        cb = (CheckBox) findViewById(R.id.login_cb);
        acc = (EditText) findViewById(R.id.edit_acc);
        pass = (EditText) findViewById(R.id.edit_pass);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        userinfo = getSharedPreferences("userinfo", MODE_PRIVATE);
        usertoken = getSharedPreferences("usertoken", MODE_PRIVATE);

        //设置icon消息显示数
        //BadgeUtil.setBadgeCount(getApplicationContext(), 35);

        //获取sp存储的数据
        String spacc = sp.getString("account", "");
        String sppass = sp.getString("password", "");
        //显示到输入框
        acc.setText(spacc);
        pass.setText(sppass);
        if (!(spacc.equals("")) && !(sppass.equals(""))){
            login(spacc,sppass);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = acc.getText().toString();
                String password = pass.getText().toString();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (cb.isChecked()) {
                        //保存账号密码
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("account", account);
                        editor.putString("password", password);
                        editor.commit();
                    }
                    login(account,password);
                }

            }
        });

    }
        private void login(final String name, final String pwd) {
            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == 1000) {
                        SharedPreferences.Editor editor = userinfo.edit();
                        editor.putString("loginname",name);
                        editor.commit();
                        //提示登陆成功
                        finish();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_LONG).show();
                    } else if (msg.what == 1001) {
                        Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG).show();

                    } else if (msg.what == 1002) {
                        Toast.makeText(getApplicationContext(), "用户已被加锁" , Toast.LENGTH_LONG).show();
                    }else if (msg.what == 1003) {
                        Toast.makeText(getApplicationContext(), "用户名或密码错误" , Toast.LENGTH_LONG).show();
                    }else if (msg.what == -1) {
                        Toast.makeText(getApplicationContext(), "连接服务器出错，请检查网络" , Toast.LENGTH_LONG).show();
                    }
                }
            };
            /*new Thread() {
                public void run() {
                    Message msg = new Message();
                    try {
                        boolean result = UserService.check(name, pwd);

                        if (result) {
                            msg.what = 1;//成功
                        } else {
                            msg.what = 0;//失败
                            msg.obj = "2";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.what = -1;
                        msg.obj = e;
                    }
                    handler.sendMessage(msg);
                }
            }.start();*/

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Message msg = new Message();
                        String  result = UserService.check(name, pwd);
                    JSONObject jsonObject = null;
                    try {
                        Log.w("denglu:",result);
                        jsonObject = new JSONObject(result);
                        SharedPreferences.Editor editor = usertoken.edit();
                        editor.putString("token", jsonObject.get("token").toString());
                        editor.commit();
                        Log.w("token value:",jsonObject.get("token").toString());
                        msg.what =Integer.parseInt(jsonObject.get("code").toString());
                        Log.w("服务器信息：", jsonObject.get("token").toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.sendMessage(msg);
                }
            };
            new Thread(runnable).start();

        }
}
