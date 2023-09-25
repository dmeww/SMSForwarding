package com.dmew.smslistener.tool;


import android.util.Log;

import com.dmew.smslistener.SMSListenApp;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Caller {

    static RequestBody getRequestBody(String title,
                                      String message,
                                      String priority
    ) {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", title)
                .addFormDataPart("message", message)
                .addFormDataPart("priority", priority)
                .build();
    }

    static Request getRequest(String url,
                              RequestBody body) {
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }


    public static void makeRequest(String url, String token,
                                   String title, String message, String priority
    ) {
        OkHttpClient client = SMSListenApp.getClient();


        Request req = getRequest(String.format("%s/message?token=%s", url, token),
                getRequestBody(title, message, priority)
        );


        // 发送请求并获取响应
        try (Response response = client.newCall(req).execute()) {
            if (response.isSuccessful()) {
                // 处理响应
                Log.e("CALLER","转发成功");
//                String responseBody = response.body().string();
//                System.out.println("Response: " + responseBody);
            } else {
//                System.out.println("Request failed. Error code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // "测试标题","测试信息","8"
    public static boolean makeRequestTest(String url, String token,String priority) {
        String title = "测试标题", message = "测试信息" ;
        // 获取OkhttpClient
        OkHttpClient client = SMSListenApp.getClient();

        Request request = getRequest(
                String.format("%s/message?token=%s", url, token)
                , getRequestBody(title, message, priority)
        );

        // 发送请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}
