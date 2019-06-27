package com.hbrc.srcapp.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hbrc.srcapp.SplashAty;
import com.hbrc.srcapp.utils.ShareSetGetUtils;

/**
 * Created by  王学波 on 2018/11/5.
 */
//开机自启动广播接受
public class AutoStartBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot ="android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)){
            ShareSetGetUtils.saveLogin("");
           Intent sayHelloIntent=new Intent(context,SplashAty.class);
//            sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sayHelloIntent);
        }
    }

}