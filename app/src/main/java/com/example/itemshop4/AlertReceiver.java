package com.example.itemshop4;

import android.content.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AlertReceiver extends BroadcastReceiver {
Context context1;
    @Override
    public void onReceive(Context context, Intent intent) {
context1=context;
    //   if (mavoltletoltes()==false) {
           Intent serviceIntent = new Intent(context, ExampleService.class);
           serviceIntent.putExtra("reciver", true);
           ContextCompat.startForegroundService(context, serviceIntent);
   //    }
    }
    public boolean mavoltletoltes(){

        String h=loadString("napilistaletolto");
        String h2=loadString("osszesitemletolto");
        boolean ih;
        String ido=ido();
        if (ido().equals(h)&&ido().equals(h2)){
            ih=true;
        }else{
            ih=false;}

        return ih;
    }
    public String ido() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }
    public  String loadString (String milyenNeven){
        String mit= PreferenceManager.getDefaultSharedPreferences(context1).getString(milyenNeven+"string", "error");
        return mit;
    }
}