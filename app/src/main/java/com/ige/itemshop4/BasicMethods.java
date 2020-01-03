package com.ige.itemshop4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public  class BasicMethods {


        public static boolean checkWifiOnAndConnected(Context context) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            boolean wifi=false;
            if (mWifi.isConnected()) {
                // Do whatever
                wifi=true;
            }
            return wifi;
        }
    public static void saveBoolean (String milyenneven,Boolean mit,Context context){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(milyenneven+"boolean", mit).apply();

    }
    public static Boolean loadBoolean (String milyenNeven,Context context){
        Boolean mit= PreferenceManager.getDefaultSharedPreferences(context).getBoolean(milyenNeven+"boolean", false);
        return mit;
    }
    public  static long hatralevoIdoFrissitesig(){

        Calendar localTime=Calendar.getInstance();
        long ennyiIdoMulva;
        Calendar mostUTC = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        mostUTC.setTimeInMillis(localTime.getTimeInMillis());

        Calendar frissulUTC= new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        frissulUTC.setTimeInMillis(localTime.getTimeInMillis());

        frissulUTC.set(Calendar.HOUR_OF_DAY, 23);
        frissulUTC.set(Calendar.MINUTE, 59);
        frissulUTC.set(Calendar.SECOND, 00);
        frissulUTC.add(Calendar.MINUTE,1);
        long utcmost=mostUTC.getTimeInMillis();
        long utcfrisites=frissulUTC.getTimeInMillis();
        ennyiIdoMulva=frissulUTC.getTimeInMillis()-mostUTC.getTimeInMillis();


        return ennyiIdoMulva;
    }


}
