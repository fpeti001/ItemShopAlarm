package com.ige.itemshop4;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.provider.Telephony.Carriers.PASSWORD;
import static com.ige.itemshop4.App.CHANNEL_ID;


public class ExampleService extends Service {
    List<String> maiItemekArray = new ArrayList<String>();
    ArrayList imageViewTartoArray = new ArrayList();
    ArrayList nagyImageViewTartoArray = new ArrayList();
    List<String> osszesItemArray = new ArrayList<String>();
    List<String> kedvencekarray = new ArrayList<String>();
    List<String> itemPriceArray = new ArrayList<String>();
    String napiurl = "https://fnapi.me/api/shop/?lang={{lang}}";
    String osszesitemurl = "https://fnapi.me/api/items/all?lang={{lang}}";
    String valtozooo = "nyemjoo";
    String itemNevek = "";
    String itemNevekEgysorba = "";
    Boolean kiskepBitmapKesz = false;
    Boolean nagykepBitmapKesz = false;
    TextView textView;
    Notification notification;
    int hanyadikattolti = 0;
    List<String> nagyKepUrlStringArray = new ArrayList<String>();
    List<String> kepUrlStringArray = new ArrayList<String>();
    int ucsonagykepaminemnull;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setContentTitle("Example Service")
                .setContentText("start foregrund")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread

        idoTextKeszito();
        popupTextAdd("wifi be: "+BasicMethods.checkWifiOnAndConnected(getApplicationContext()));
        String extra = intent.getStringExtra("extra");
        switch (extra){
            case "mindennap":
                napilistaletolto();
                osszesitemletolto();

                break;
            case "refresh":
                napilistaletolto();
                osszesitemletolto();

                break;

            case "mainactivity":
                if (mavoltletoltes() == false) {
                    napilistaletolto();
                    osszesitemletolto();
                break;
        }

            case "bootreceiver":
                mindenapIsmetles();
                if (mavoltletoltes() == false) {
                    napilistaletolto();
                    osszesitemletolto();
                }
                break;

            case "napkozbeni":
                napkozbeniNotification();
                break;

            case "default":
                napilistaletolto();
                osszesitemletolto();
                break;
            case "settings":
                napkozbeniServiceIndito();
                break;



        }
        if (!vanMarAlarm())mindenapIsmetles();
        stopSelf();


        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String ido() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }

    public Date idoDate() {

        Date currentTime = Calendar.getInstance().getTime();
        return currentTime;
    }

    public String datumido() {
        String date = new SimpleDateFormat("yyyy-MM-dd' Time:'HH:mm", Locale.getDefault()).format(new Date());
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog(String title, String text, int id) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
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
                .setSmallIcon(R.drawable.ic_kedvenc)
                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(text)
                .setContentInfo("Information")
                .setContentIntent(pendingIntent);


        notificationManager.notify(id, notificationBuilder.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog2(String title, String text, int id) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_02";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setSound(null, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)

                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_kedvenc)
                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setContentText(text)
                .setContentInfo("Information")
                .setContentIntent(pendingIntent);


        notificationManager.notify(id, notificationBuilder.build());
    }


    public void osszesitemletolto() {
        Boolean wifiswitch= BasicMethods.loadBoolean("wifiswitch",this);
        if (wifiswitch!=true || BasicMethods.checkWifiOnAndConnected(this)) {

            StringRequest request = new StringRequest(osszesitemurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {


                    try {
                        JSONObject reader = new JSONObject(string);
                        JSONArray items = reader.getJSONArray("data");



                        for (int i = 0; i < items.length(); i++) {

                            JSONObject elso = items.getJSONObject(i);
                            JSONObject item = elso.getJSONObject("item");
                            String name = item.getString("name");
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
                        saveData(osszesItemArray, "osszesItemArray");
                     //   letolteskesz("osszesitemletolto");
                        //   napiItemText.setText("KÉÉÉÉÉÉÉÉÉÉÉÉSZ");


                    } catch (JSONException e) {
                        //    napiItemText.setText("hibaa");
                        letoltesKeszReciverKuldes("error");
                        popupTextAdd("Try error");
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    letoltesKeszReciverKuldes("error");
                    popupTextAdd("letöltés error");


                }
            })
            {


                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "8204318380acf3fe58c7d8d5670d0fd68e1dade3a47bcc8d21f11f1efbb3ae74");
                    return headers;
                }
            };




            RequestQueue rQueue = Volley.newRequestQueue(ExampleService.this);
            rQueue.add(request);

            // return listdata;





        }else {
            letoltesKeszReciverKuldes("error");
        }
    }

    public void napilistaletolto() {
       Boolean wifiswitch= BasicMethods.loadBoolean("wifiswitch",this);
       if (wifiswitch!=true || BasicMethods.checkWifiOnAndConnected(this)) {
            StringRequest request = new StringRequest(napiurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String string) {


                    try {
                        JSONObject reader = new JSONObject(string);
                        JSONArray items = reader.getJSONArray("data");




                 /*   if (items != null) {
                        for (int i = 0; i < items.length(); i++) {
                            maiItemekArray.add(items.getString(i));
                            saveData(maiItemekArray,"maiItemekArray");
                        }
                    }*/
                        maiItemekArray.clear();
                        kepUrlStringArray.clear();


                        for (int i = 0; i < items.length(); i++) {
                            JSONObject elso = items.getJSONObject(i);
                            JSONObject store = elso.getJSONObject("store");
                            JSONObject itemm = elso.getJSONObject("item");

                            JSONObject imagess = itemm.getJSONObject("images");
                          //  JSONObject featured = imagess.getJSONObject("featured");
                            String imageUrl = imagess.getString("background");
                            String bigImagesUrl = imagess.getString("featured");
                            String name = itemm.getString("name");
                            String cost = store.getString("cost");


                            maiItemekArray.add(name);
                            itemPriceArray.add(cost);
                            kepUrlStringArray.add(imageUrl);
                            String type = itemm.getString("type");

                            if (bigImagesUrl.equals("null")) {

                                if (type.equals("emote")){
                                    bigImagesUrl = imagess.getString("background");
                                } else {
                                    bigImagesUrl = imagess.getString("icon");
                                }


                                nagyKepUrlStringArray.add(bigImagesUrl);
                              // nagyKepUrlStringArray.add("nincs");
                            } else {
                                if (type.equals("emote")){
                                    bigImagesUrl = imagess.getString("background");
                                }
                                nagyKepUrlStringArray.add(bigImagesUrl);
                            }
                        }
                        for (int i = 0; i < maiItemekArray.size(); i++) {
                            String name = maiItemekArray.get(i);
                            itemNevek = itemNevek + name + "\n";
                            itemNevekEgysorba = itemNevekEgysorba + name + " ";


                        }

                        saveData(maiItemekArray, "maiItemekArray");
                        saveData(itemPriceArray, "itemPriceArray");
                        saveString("itemNevek", itemNevek);
                        saveData(kepUrlStringArray, "kepUrlStringArray");
                        saveData(nagyKepUrlStringArray, "nagyKepUrlStringArray");
                     //   letolteskesz("napilistaletolto");
                        napiItemEgysorbakeszito();
                        kedvencKereses();
                        makeImageArrayList(kepUrlStringArray, imageViewTartoArray, "kiskep");
                        makeImageArrayList(nagyKepUrlStringArray, nagyImageViewTartoArray, "nagykep");

                        //  napiItemText.setText(itemNevek);
                    } catch (JSONException e) {
                        //   napiItemText.setText("hibaa");
                        letoltesKeszReciverKuldes("error");
                        popupTextAdd("Try error");
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    popupTextAdd("letolt error");
                    letoltesKeszReciverKuldes("error");
                }
            })
            {


                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "8204318380acf3fe58c7d8d5670d0fd68e1dade3a47bcc8d21f11f1efbb3ae74");
                    return headers;
                }
            };

            RequestQueue rQueue = Volley.newRequestQueue(ExampleService.this);
            rQueue.add(request);

            //   return listdata;
        }else {
           letoltesKeszReciverKuldes("error");
       }
    }

    private void saveData(List<String> arrayList, String milyenneven) {
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

    public void saveString(String milyenneven, String mit) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(milyenneven + "string", mit).apply();

    }

    public String loadString(String milyenNeven) {
        String mit = PreferenceManager.getDefaultSharedPreferences(this).getString(milyenNeven + "string", "error");
        return mit;
    }

    public void saveDate(String milyenneven, Date mit) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putLong(milyenneven + "date", mit.getTime()).apply();

    }

    public Date loadDate(String milyenNeven) {
        Long bejovolong = PreferenceManager.getDefaultSharedPreferences(this).getLong(milyenNeven + "date", 0);
        Date mit = new Date(bejovolong);
        return mit;
    }

    public void saveInt(String milyenneven, int mit) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(milyenneven + "int", mit).apply();

    }

    public int loadInt(String milyenNeven) {
        int mit = PreferenceManager.getDefaultSharedPreferences(this).getInt(milyenNeven + "int", 0);
        return mit;
    }

    public void saveBoolean(String milyenneven, Boolean mit) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(milyenneven + "boolean", mit).apply();

    }

    public Boolean loadBoolean(String milyenNeven) {
        Boolean mit = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(milyenNeven + "boolean", false);
        return mit;
    }

    public boolean mavoltletoltes() {
        Date h = loadDate("napilistaletolto");
        Date h2 = loadDate("osszesitemletolto");
        Date mostIdo = idoDate();
        boolean ih;
        if (mostIdo.before(h) && mostIdo.before(h2)) {
            ih = true;
        } else {
            ih = false;
        }

        return ih;
    }

    public void letolteskesz(String melyiknek) {
        Date mostido = idoDate();
        Date eddigFriss;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + BasicMethods.hatralevoIdoFrissitesig());
         calendar.add(Calendar.MINUTE,-2);

        eddigFriss = calendar.getTime();
        popupTextAdd(melyiknek+"friss: "+eddigFriss);
        saveDate(melyiknek, eddigFriss);
//asdd
    }

    public void kedvencKereses() {
        Boolean vantalalat = false;
        String talalatok = "";
        kedvencekarray = loadData("kedvencekarray");
        maiItemekArray = loadData("maiItemekArray");
        for (int i = 0; i < maiItemekArray.size(); i++) {
            if (kedvencekarray.contains(maiItemekArray.get(i))) {
                talalatok = talalatok + ">" + maiItemekArray.get(i) + "<";
                vantalalat = true;
            }
        }
        if (vantalalat) {
            notificationDialog2((String)getText(R.string.today_availbiable), talalatok, 6);
            napkozbeniServiceIndito();
        }

    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        TextView textView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            // this.textView=textView;

        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;

        }

        protected void onPostExecute(Bitmap result) {

            hanyadikattolti++;
            imageView.setImageBitmap(result);
            ImageView kisKeputsoimageview = (ImageView) imageViewTartoArray.get(imageViewTartoArray.size() - 1);
            ImageView nagykepKeputsoimageview = (ImageView) nagyImageViewTartoArray.get(ucsonagykepaminemnull);
            if (nagykepBitmapKesz == false && nagykepKeputsoimageview != null && nagykepKeputsoimageview.getDrawable() != null) {
                bitmapizalokepbol(nagyImageViewTartoArray, "nagykep");
                nagykepBitmapKesz = true;
            }
            if (kiskepBitmapKesz == false && kisKeputsoimageview.getDrawable() != null) {
                bitmapizalokepbol(imageViewTartoArray, "kiskep");
                kiskepBitmapKesz = true;

            }

//            adapter.notifyDataSetChanged();
            // textView.setBackground(new BitmapDrawable(getResources(), result));

        }

    }

    public void makeImageArrayList(List<String> melyikUrleketToltsele, ArrayList melyikArraybaToltseAKepeket, String melyikbe) {

        for (int i = 0; i < melyikUrleketToltsele.size(); i++) {
            ImageView imageView = new ImageView(this);
            String url = melyikUrleketToltsele.get(i);
            if (!url.equals("nincs")) {
                new DownloadImageFromInternet(imageView).execute(url);
            } else {
                imageView = null;
            }

            if (melyikbe.equals("kiskep")) imageViewTartoArray.add(imageView);
            if (melyikbe.equals("nagykep")) nagyImageViewTartoArray.add(imageView);


        }
        if (melyikbe.equals("nagykep")) {
            for (int i = 0; i < nagyImageViewTartoArray.size(); i++) {
                if (nagyImageViewTartoArray.get(i) != null) ucsonagykepaminemnull = i;
            }
        }
    }

    public void bitmapizalokepbol(ArrayList melyiketBitmapizalja, String melyiklistaString) {
        List<String> stringBitmapArray = new ArrayList<String>();

        for (int i = 0; melyiketBitmapizalja.size() > i; i++) {
            if (melyiketBitmapizalja.get(i) != null) {
                ImageView imageView = new ImageView(this);

                imageView = (ImageView) melyiketBitmapizalja.get(i);

                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                String stringBitmap = encodeTobase64(bitmap);
                stringBitmapArray.add(stringBitmap);
            } else {
                stringBitmapArray.add(null);
            }
        }
        if (melyiklistaString.equals("kiskep")) {
            saveData(stringBitmapArray, "stringBitmapArray");
            letoltesKeszReciverKuldes("kiskep");
        }
        if (melyiklistaString.equals("nagykep")) {
            saveData(stringBitmapArray, "nagystringBitmapArray");
            letolteskesz("napilistaletolto");
            letolteskesz("osszesitemletolto");
            letoltesKeszReciverKuldes("nagykep");

        }

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public void letoltesKeszReciverKuldes(String extra) {
        Intent intent = new Intent();
        intent.setAction("com.toxy.LOAD_URL");
        intent.putExtra("extra", extra);
        sendBroadcast(intent);
    }
    public void letoltesKeszReciverKuldes(String extra,String milyenError) {
        saveString("error",milyenError);
        Intent intent = new Intent();
        intent.setAction("com.toxy.LOAD_URL");
        intent.putExtra("extra", extra);
        sendBroadcast(intent);

    }

    public void idoTextKeszito() {
        String inudlasiido ="SERVICE: "+ datumido() + "^^^^^^\n";
        String inudlasiidolista;
        inudlasiidolista = loadString("inudlasiidolista");
        inudlasiidolista = inudlasiido + inudlasiidolista;
        saveString("inudlasiidolista", inudlasiidolista);

    }

    public void napiItemEgysorbakeszito() {
        String string;
        string = itemNevekEgysorba + "\n";
        String inudlasiidolista;
        inudlasiidolista = loadString("inudlasiidolista");
        inudlasiidolista = string + inudlasiidolista;
        saveString("inudlasiidolista", inudlasiidolista);


    }

    public void mindenapIsmetles2() {


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("extra", "mindennap");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1001, intent, FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 5);
        popupTextAdd("Service Cal Time: " + cal.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 00);
        popupTextAdd("Service Calendar: " + calendar.getTime());

        long startUpTime = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > startUpTime) {
            startUpTime = startUpTime + 24 * 60 * 60 * 1000;

        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    public void mindenapIsmetles() {


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("extra", "mindennap");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1001, intent, FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 5);
        popupTextAdd("Service Cal Time: " + cal.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 00);

       Calendar calendarNemzetkozi=Calendar.getInstance();
        calendarNemzetkozi.setTimeInMillis(System.currentTimeMillis() + BasicMethods.hatralevoIdoFrissitesig());
        calendarNemzetkozi.add(Calendar.MINUTE,3);
        popupTextAdd("Service Calendar: " + calendarNemzetkozi.getTime());

        long startUpTime = calendarNemzetkozi.getTimeInMillis();

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void popupTextAdd(String string) {

        string = string + "\n";
        String inudlasiidolista;
        inudlasiidolista = loadString("inudlasiidolista");
        inudlasiidolista = string + inudlasiidolista;
        saveString("inudlasiidolista", inudlasiidolista);
    }

    public void napkozbeniServiceIndito() {
        Boolean vantalalat = false;
        if (loadBoolean("napkozbeniswitch")) {

            kedvencekarray = loadData("kedvencekarray");
            maiItemekArray = loadData("maiItemekArray");
            for (int i = 0; i < maiItemekArray.size(); i++) {
                if (kedvencekarray.contains(maiItemekArray.get(i))) {
                    vantalalat = true;
                }
            }
        }
        if (vantalalat) {
            Calendar calendarMost = Calendar.getInstance();
            int ora = loadInt("hour");
            int perc = loadInt("minute");

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            intent.putExtra("extra", "napkozbeni"); //data to pass

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1002, intent, FLAG_CANCEL_CURRENT);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 5);
            popupTextAdd("Cal Time: " + cal.getTime());

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, ora);
            calendar.set(Calendar.MINUTE, perc);
            calendar.set(Calendar.SECOND, 00);
            if (ora<2)calendar.add(Calendar.HOUR_OF_DAY,24);
            if (ora==2&&perc<34)calendar.add(Calendar.HOUR_OF_DAY,24);
            popupTextAdd("napkozbeni: " + calendar.getTime());

            long startUpTime = calendar.getTimeInMillis();
            long mostaniTime = calendarMost.getTimeInMillis();
            if (startUpTime > mostaniTime)
                alarmManager.set(AlarmManager.RTC_WAKEUP, startUpTime, pendingIntent);


            //   Toast.makeText(ExampleService.this, "Alarm beallitva napkozben", Toast.LENGTH_SHORT).show();
        }

    }

    public void napkozbeniNotification() {
        Boolean vantalalat = false;
        String talalatok = "";
        kedvencekarray = loadData("kedvencekarray");
        maiItemekArray = loadData("maiItemekArray");
        for (int i = 0; i < maiItemekArray.size(); i++) {
            if (kedvencekarray.contains(maiItemekArray.get(i))) {
                talalatok = talalatok + ">" + maiItemekArray.get(i) + "<";
                vantalalat = true;
            }
        }
        if (vantalalat) {
            notificationDialog((String)getText(R.string.today_availbiable), talalatok, 7);
        }
    }
    public  Boolean vanMarAlarm(){
        Boolean visszaad=true;
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1001, intent, PendingIntent.FLAG_NO_CREATE);
        PendingIntent sender2 = PendingIntent.getBroadcast(this, 1002, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        boolean isWorking =(alarmManager!= null);
        popupTextAdd("alarmmanager: "+isWorking);
        boolean isWorking2 =(sender!= null);
        boolean isWorking3 =(sender2!= null);//just changed the flag
        popupTextAdd("Pendingintent bevankapcsolva: "+isWorking2);
        popupTextAdd("Pendingintentnappal bevankapcsolva: "+isWorking3);
        if (isWorking&&isWorking2){}else {visszaad=false;}
        return visszaad;
    }
}