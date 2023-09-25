package com.dmew.smslistener.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


import com.dmew.smslistener.tool.Caller;

import java.util.concurrent.Executors;

public class SMSReceiver extends BroadcastReceiver {

    String url, token, priority;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("RECEIVER", "SMS Received");

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String senderNumber = smsMessage.getOriginatingAddress();
                        String messageBody = smsMessage.getMessageBody();
                        String messageTitle = smsMessage.getDisplayMessageBody();

                        // 输出打印短信信息
//                        System.out.println("发送者号码：" + senderNumber);
//                        System.out.println("短信标题：" + messageTitle);
//                        System.out.println("短信内容：" + messageBody);

                        getData(context);

                        boolean valid = checkData(url, token, priority);
                        if (valid)
                            Executors.newSingleThreadExecutor().execute(() -> {
                                Caller.makeRequest(url, token, senderNumber + ":" + messageTitle, messageBody, priority);
                                Log.e("SMSRECEIVER","执行完成！");
                            });
                        else
                            Log.e("SMSRECEIVER","读取到错误的配置信息，请重新配置");
                    }
                }
            }
        }


    }

    void getData(Context context) {
        SharedPreferences sp = context.getSharedPreferences("app", Context.MODE_PRIVATE);

        url = sp.getString("url", "");
        token = sp.getString("token", "");
        priority = sp.getString("priority", "");
    }


    boolean checkData(String _url, String _token, String _priority) {

        if (_url == null || _url.isEmpty()) {
            return false;
        }
        if (_token == null || _token.isEmpty()) {
            return false;
        }
        if (_priority == null || _priority.isEmpty()) {
            return false;
        }
        return true;
    }


}
