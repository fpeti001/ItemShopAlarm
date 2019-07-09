package com.example.itemshop4;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JobIntentNotificationService extends JobIntentService {
    List<String> maiItemekArray = new ArrayList<String>();
    List<String> osszesItemArray = new ArrayList<String>();
    String napiurl = "https://fortnite-public-api.theapinetwork.com/prod09/store/get";
    String osszesitemurl = "https://fortnite-public-api.theapinetwork.com/prod09/items/list";
    String valtozooo="nyemjoo";
    String itemNevek = "";
    public static void start(Context context) {
        Intent starter = new Intent(context, JobIntentNotificationService.class);
        JobIntentNotificationService.enqueueWork(context, starter);
    }

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    private static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, JobIntentNotificationService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        notificationDialog("elsooooooooooo");

napilistaletolto();
    //    szamolo();

    }

    private void notificationDialog(String irass) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("letoltes")
                .setContentText(irass)
                .setContentInfo("Information");
        notificationManager.notify(3, notificationBuilder.build());
    }
    public void osszesitemletolto() {




        StringRequest request = new StringRequest(osszesitemurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {


                try {

                    JSONArray items = new JSONArray(string);



                    for (int i = 0; i < items.length(); i++) {

                        JSONObject elso = items.getJSONObject(i);
                        String name = elso.getString("name");
                        osszesItemArray.add(name);
                        //     napiItemText.setText(osszesItemArray.get(0));
                        //   asaad = asaad  + name+ "\n";
                        //
                    }
                    ////abc sorreenndd
                    Collections.sort(osszesItemArray, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return s1.compareToIgnoreCase(s2);
                        }
                    });
                    saveData(osszesItemArray,"osszesItemArray");
                    //   napiItemText.setText("KÉÉÉÉÉÉÉÉÉÉÉÉSZ");

                } catch (JSONException e) {
                    //    napiItemText.setText("hibaa");
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "All item download error", Toast.LENGTH_SHORT).show();

            }
        });



        // return listdata;

    }
    public void napilistaletolto() {




        StringRequest request = new StringRequest(napiurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String string) {


                try {
                    JSONObject reader = new JSONObject(string);
                    JSONArray items = reader.getJSONArray("items");


                    if (items != null) {
                        for (int i = 0; i < items.length(); i++) {
                            maiItemekArray.add(items.getString(i));
                        }
                    }

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject elso = items.getJSONObject(i);
                        String name = elso.getString("name");
                        itemNevek = itemNevek  + name+ "\n";
                    }
                    // saveString(ido(),itemNevek);
                 //   notificationDialog("letolteskozbeni");
                    //  napiItemText.setText(itemNevek);
                    saveString("itemNevek",itemNevek);
                } catch (JSONException e) {
                    //   napiItemText.setText("hibaa");
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Download error", Toast.LENGTH_SHORT).show();

            }
        });



        //   return listdata;

    }
    private void saveData( List<String> arrayList,String milyenneven) {
        SharedPreferences sharedPreferences = getSharedPreferences(milyenneven, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("task list", json);
        editor.apply();
    }

    private List<String> loadData(String melyiket) {
        List<String> arrayList = new ArrayList<String>();
        SharedPreferences sharedPreferences = getSharedPreferences(melyiket, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        arrayList = gson.fromJson(json, type);

        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        return arrayList;
    }
    public void saveString (String milyenneven,String mit){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(milyenneven+"string", mit).apply();

    }
    public  String loadString (String milyenNeven){
        String mit= PreferenceManager.getDefaultSharedPreferences(this).getString(milyenNeven+"string", "error");
        return mit;
    }
    public void szamolo() {
        for (int i = 0; i < 10; i++) {

            notificationDialog(i + "");

            try {
                Thread.sleep(1000);
                //   napilistaletolto();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}