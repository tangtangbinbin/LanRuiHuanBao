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

public class AskForApp {

    static String  url1 = "http://sso.liblue.com/mini4Aweb/servlet/smsSendAction?op=askForApp&loginName=";
    public static String sendPostRequest(String loginname,String name,String appcode,String appname) {

        String url = url1+name+"&name="+name+"&appCode="+appcode+"&appName="+appname;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url.toString()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
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
