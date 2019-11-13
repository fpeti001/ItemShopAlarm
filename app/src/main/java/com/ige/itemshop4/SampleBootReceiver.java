package com.ige.itemshop4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;

public class SampleBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            Intent serviceIntent = new Intent(context, ExampleService.class);
            serviceIntent.putExtra("extra", "bootreceiver");
            ContextCompat.startForegroundService(context, serviceIntent);
        }
    }
}
