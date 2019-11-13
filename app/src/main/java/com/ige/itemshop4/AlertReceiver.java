package com.ige.itemshop4;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;


public class AlertReceiver extends BroadcastReceiver {
Context context1;
String getExtra;
String sendExtra;
    @Override
    public void onReceive(Context context, Intent intent) {
        getExtra=intent.getStringExtra("extra");
    context1=context;
    if(getExtra==null)getExtra="ures";

    switch (getExtra){
        case "mindennap":
            Intent serviceIntent = new Intent(context, ExampleService.class);
            serviceIntent.putExtra("extra", "mindennap");
            ContextCompat.startForegroundService(context, serviceIntent);
            break;
        case "napkozbeni" :
            Intent serviceIntent2 = new Intent(context, ExampleService.class);
            serviceIntent2.putExtra("extra", "napkozbeni");
            ContextCompat.startForegroundService(context, serviceIntent2);
             break;
        case "refresh" :
            Intent serviceIntent3 = new Intent(context, ExampleService.class);
            serviceIntent3.putExtra("extra", "refresh");
            ContextCompat.startForegroundService(context, serviceIntent3);
            break;

        default:
            Intent serviceIntent4 = new Intent(context, ExampleService.class);
            serviceIntent4.putExtra("extra", "default");
            ContextCompat.startForegroundService(context, serviceIntent4);




    }

    }


}