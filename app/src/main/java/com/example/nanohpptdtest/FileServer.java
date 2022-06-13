package com.example.nanohpptdtest;

import android.util.Log;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class FileServer extends NanoHTTPD {
    //    public static final int DEFAULT_SERVER_PORT= com.example.zjt.nanohttpexample.Status.MY_PORT;//为8080
    public static final int DEFAULT_SERVER_PORT= 8080;//为8080
    public static final String TAG = FileServer.class.getSimpleName();
    //根目录
    private static  final String REQUEST_ROOT = "/";

    private int count = 0; //用于记录请求为第几次
    static final String HTTP_URI_HELLO = "/api/holleMan";
    static final String HTTP_URI_SUM = "/api/sumHundredTime";
    static final String HTTP_URI_SAYSOMETHING = "/api/saySomething";
    static final String HTTP_IP = "172.16.29.182";//这是我当前手机流量下的IP地址
    static final String HTTP_URL = "http://" + HTTP_IP + ":" + DEFAULT_SERVER_PORT;

    private Gson mGson = new Gson();

    public FileServer(String hostName){
        super(hostName, DEFAULT_SERVER_PORT);
    }
    //当接受到连接时会调用此方法
    public Response serve(IHTTPSession session){
        return dealWith(session);
    }

    private Response dealWith(IHTTPSession session) {
        Log.i(TAG, "dealWith:session.uri=" + session.getUri() + ",method=" + session.getMethod() +
                ",header=" + session.getHeaders() + ",parameters=" + session.getParameters());
        //响应get请求
        if (session.getMethod().equals(Method.GET)) {

            if (session.getUri().equals(HTTP_URI_SAYSOMETHING)) {
                count++;
                Map<String, List<String>> param = session.getParameters();
                return responseJsonString(
                        200,
                        param.get("name").get(0) + ", say somthing to me" + count + ", ok?",
                        "请求成功！"
                );
            } else {
                return responseJsonString(404, "It's nothing!", "Success！");
            }

        } else if (Method.POST == session.getMethod()) {//响应post请求
            //获取请求头数据
            Map<String, String> header = session.getHeaders();
            //获取传参参数
            Map<String, List<String>> param = session.getParameters();
            if (session.getUri().equals(HTTP_URI_SUM)) {
                int b = Integer.parseInt(param.get("number").get(0));
                for (int i = 0; i < 100; i++) {
                    b += 1;
                }
                return responseJsonString(200, b, "Success！");
            } else if (session.getUri().equals(HTTP_URI_HELLO)) {
                return responseJsonString(
                        200,
                        "Hello" + param.get("name").get(0) + "!",
                        "Success！"
                );
            } else {
                return responseJsonString(404, "It's nothing!", "Success！");
            }
        }
        return responseJsonString(404, "", "Request not support!");
    }

    private <T> Response responseJsonString(int code, T data, String msg) {
        Responser<T> response = new Responser<T>(code, data, msg);
        Log.i(TAG, "responseJsonString:" + response);
        return newFixedLengthResponse(mGson.toJson(response));
    }

    //页面不存在，或者文件不存在时
    public Response response404(IHTTPSession session,String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html>body>");
        builder.append("Sorry,Can't Found" + url + " !");
        builder.append("</body></html>\n");
        return newFixedLengthResponse(builder.toString());
    }
}

