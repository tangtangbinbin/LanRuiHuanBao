package com.lilan.lanruihuanbao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/23.
 */

public class UpdatePWD {

    static String  url1 = "http://sso.liblue.com/mini4Aweb/servlet/userAction?op=updatePwd&loginName=";
    public static String sendPostRequest(String name,String old,String news) {

        String url = url1+name+"&oldPassword="+old+"&newPassword="+news;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url.toString()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
           /* // 取得sessionid.
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
                InputStream is = conn.getInputStream();   //获取输入流，此时才真正建立链接
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader bufferReader = new BufferedReader(isr);
                String inputLine  = "";
                String resultData = "";
                while((inputLine = bufferReader.readLine()) != null){
                    resultData += inputLine + "\n";
                }
                return resultData;
            }else{
                return "连接服务器出错";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "发生异常";
    }
}
