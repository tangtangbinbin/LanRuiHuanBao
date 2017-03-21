package com.lilan.lanruihuanbao;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/23.
 */

public class GetAppsurl {

    static String  url1 = "http://sso.liblue.com/mini4Aweb/servlet/appSsoAction";
    public static String sendPostRequest(String loginname,String appcode,String appAcctName) {

        String url = url1+"?loginname="+loginname+"&appCode="+appcode+"&loginflag=app&appAcctName="+appAcctName;
        Log.w("getappurl:",url);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url.toString()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            /*// 取得sessionid.
            String cookieval = conn.getHeaderField("set-cookie");
            String sessionid = null;
            if(cookieval != null) {
                sessionid = cookieval.substring(0, cookieval.indexOf(";"));
            }
            if(sessionid != null) {
                conn.setRequestProperty("cookie", sessionid);
            }*/
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bufferReader = new BufferedReader(isr);
                String inputLine  = "";
                String resultData = "";
                while((inputLine = bufferReader.readLine()) != null){
                    resultData += inputLine + "\n";
                }
                return resultData;
            }else{
                InputStream is = conn.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bufferReader = new BufferedReader(isr);
                String inputLine  = "";
                String resultData = "";
                while((inputLine = bufferReader.readLine()) != null){
                    resultData += inputLine + "\n";
                }
                return conn.getResponseCode()+":"+resultData;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "发生异常";
    }
}
