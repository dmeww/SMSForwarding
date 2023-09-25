package com.dmew.smslistener;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class SMSListenApp extends Application {

    private static OkHttpClient client;

    @Override
    public void onCreate() {
        client = new OkHttpClient.Builder()
                .callTimeout(3, TimeUnit.SECONDS)
                .build();

        super.onCreate();
    }

    public static OkHttpClient getClient() {
        return client;
    }
}
