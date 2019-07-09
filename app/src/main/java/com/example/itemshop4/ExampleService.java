package com.example.itemshop4;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

import static com.example.itemshop4.App.CHANNEL_ID;


public class ExampleService extends Service {
    List<String> maiItemekArray = new ArrayList<String>();
    ArrayList imageViewTartoArray=new ArrayList();
    List<String> osszesItemArray = new ArrayList<String>();
    List<String> kedvencekarray = new ArrayList<String>();
    String napiurl = "https://fortnite-public-api.theapinetwork.com/prod09/store/get";
    String osszesitemurl = "https://fortnite-public-api.theapinetwork.com/prod09/items/list";
    String valtozooo="nyemjoo";
    String itemNevek = "";
TextView textView;
    Notification notification;
    int hanyadikattolti=0;

    List<String> kepUrlStringArray = new ArrayList<String>();
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
                .setContentText("legyszi induljeel foregrund")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
boolean reciver=intent.getBooleanExtra("reciver",true);
if (reciver){napilistaletolto();osszesitemletolto();}
   //   saveString("proba", DateFormat.getDateTimeInstance().format(new Date()));

    if (mavoltletoltes()==false){napilistaletolto();osszesitemletolto();}
        Toast.makeText(getApplicationContext(), "Hatter befejezodott",
                Toast.LENGTH_LONG).show();


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
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog(String title,String text,int id) {
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
                .setContentInfo("Information");
        notificationManager.notify(id, notificationBuilder.build());
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
                    letolteskesz("osszesitemletolto");
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

        RequestQueue rQueue = Volley.newRequestQueue(ExampleService.this);
        rQueue.add(request);

       // return listdata;

    }
    public void napilistaletolto() {




        StringRequest request = new StringRequest(napiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {


                try {
                    JSONObject reader = new JSONObject(string);
                    JSONArray items = reader.getJSONArray("items");


                 /*   if (items != null) {
                        for (int i = 0; i < items.length(); i++) {
                            maiItemekArray.add(items.getString(i));
                            saveData(maiItemekArray,"maiItemekArray");
                        }
                    }*/
                maiItemekArray.clear();

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject elso = items.getJSONObject(i);
                        JSONObject itemm=elso.getJSONObject("item");
                        JSONObject imagess=itemm.getJSONObject("images");
                        String imageUrl=imagess.getString("background");
                        String name = elso.getString("name");
                        maiItemekArray.add(name);
                        kepUrlStringArray.add(imageUrl);
                    }
                    for (int i = 0; i < maiItemekArray.size(); i++) {
                        String name = maiItemekArray.get(i);
                        itemNevek = itemNevek  + name+ "\n";


                    }
                    saveData(maiItemekArray,"maiItemekArray");
                    saveString("itemNevek",itemNevek);
                    saveData(kepUrlStringArray,"kepUrlStringArray");
                    letolteskesz("napilistaletolto");

                    kedvencKereses();
                    makeImageArrayList();
                  //  napiItemText.setText(itemNevek);
                } catch (JSONException e) {
                 //   napiItemText.setText("hibaa");
                    ;
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Download error", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(ExampleService.this);
        rQueue.add(request);

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
    public boolean mavoltletoltes(){
        String h=loadString("napilistaletolto");
        String h2=loadString("osszesitemletolto");
        boolean ih;
        String ido=ido();
        if (ido().equals(h)&&ido().equals(h2)){ ih=true; }else{ ih=false;}
        return ih;
    }
    public void letolteskesz(String melyiknek){
        saveString(melyiknek,ido());

    }
public void kedvencKereses(){
    kedvencekarray = loadData("kedvencekarray");
        maiItemekArray=loadData("maiItemekArray");
        for (int i=0;i<maiItemekArray.size();i++){
           if(kedvencekarray.contains(maiItemekArray.get(i))){
               notificationDialog("Ma ezek a kedvenced elérhető:",maiItemekArray.get(i),6);}
        }
}
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        TextView textView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            // this.textView=textView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
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
            if (hanyadikattolti==kepUrlStringArray.size()){
                bitmapizalokepbol();
            }
//            adapter.notifyDataSetChanged();
            // textView.setBackground(new BitmapDrawable(getResources(), result));
        }

    }
    public void makeImageArrayList() {

        for (int i = 0; i < kepUrlStringArray.size(); i++) {
            ImageView imageView = new ImageView(this);
            String url = kepUrlStringArray.get(i);
            new DownloadImageFromInternet(imageView)
                    .execute(url);
            imageViewTartoArray.add(imageView);


        }
    }

    public void bitmapizalokepbol(){
        List<String> stringBitmapArray = new ArrayList<String>();

        for (int i=0;imageViewTartoArray.size()>i;i++){
            ImageView imageView = new ImageView(this);
            imageView=(ImageView) imageViewTartoArray.get(i);

            Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
            String stringBitmap= encodeTobase64(bitmap);
            stringBitmapArray.add(stringBitmap);
        }
        saveData(stringBitmapArray,"stringBitmapArray");
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
}
