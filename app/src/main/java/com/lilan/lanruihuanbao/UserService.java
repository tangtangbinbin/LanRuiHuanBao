package com.lilan.lanruihuanbao;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/16.
 */

public class UserService {
    /**
     * 验证用户登录是否合法
     * 返回值：请求是否成功
     */
    public static String check(String name, String pass) {
        //http://sso.liblue.com/mini4Aweb/servlet/loginAction?op=login
        String path="http://sso.liblue.com/mini4Aweb/servlet/loginAction?op=login";
        //将用户名和密码放入HashMap中
        Map<String,String> params=new HashMap<String,String>();
        params.put("loginname", name);
        params.put("loginpwd", pass);
        params.put("idfycode", "app");

        try {
            return sendGETRequest(path,params,"UTF-8");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "-1";
    }
    private static String sendGETRequest(String path,
                                          Map<String, String> params,String encode) throws MalformedURLException, IOException {
        StringBuilder url=new StringBuilder(path);
        url.append("&");
        for(Map.Entry<String, String> entry:params.entrySet())
        {
            url.append(entry.getKey()).append("=");
            url.append(URLEncoder.encode(entry.getValue(),encode));
            url.append("&");
        }
        //删掉最后一个&
        url.deleteCharAt(url.length()-1);
        Log.w( "sendGETRequest: ",url.toString());
        HttpURLConnection conn=(HttpURLConnection)new URL(url.toString()).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.connect();
        if(conn.getResponseCode()==200){
        InputStream is = conn.getInputStream();   //获取输入流，此时才真正建立链接
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferReader = new BufferedReader(isr);
        String inputLine  = "";
        String resultData = "";
        while((inputLine = bufferReader.readLine()) != null){
            resultData += inputLine + "\n";
        }

        /*JSONObject jsonObject = null;
        try {
            Log.w("denglu:",resultData);
            jsonObject = new JSONObject(resultData);
            jsonObject.get("token");
            Log.w("服务器信息：", (String) jsonObject.get("code"));
            return Integer.parseInt( jsonObject.get("code").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
            return resultData;
        }


        return "-1";
    }
}
