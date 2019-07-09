package com.example.itemshop4;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
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

public class MainActivity extends AppCompatActivity {
    String asaad="";
    private Button home;
    private Button kedvencek;
    private Button tartalekbutt;
    List<String> kedvencekarray = new ArrayList<String>();
    List<String> maiItemekArray = new ArrayList<String>();
    List<String> osszesItemArray = new ArrayList<String>();
    ArrayList mindennelKeszKepesArray=new ArrayList();
    TextView napiItemText;
    String osszesItemNevek="";
    String itemNevek = "";
    String kedvencekarrayString = "";
    String napiurl = "https://fortnite-public-api.theapinetwork.com/prod09/store/get";
    String osszesitemurl = "https://fortnite-public-api.theapinetwork.com/prod09/items/list";
    ProgressDialog dialog;
    ProgressDialog dialogosszesitem;
    ArrayList<String> listdata = new ArrayList<String>();
    TextView pprobaTextView;
    private PendingIntent pIntent;
    Button startServiceButton;

    ArrayList arrayListKeszKep=new ArrayList();
    CustomAdapter adapter2;
    GridView gridView;
    List<String> kepUrlStringArray = new ArrayList<String>();
    ///azert kell hogy frissitsen ha visszalepek a kedvencek tabrol
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//OCLICKLISTENER START HIER----------------------------->

        mindenapIsmetles();

        napiItemText = (TextView) findViewById(R.id.myAwesomeTextView);
        kedvencekarray = loadData("kedvencekarray");

      //  startService();
       if (mavoltletoltes()) {napiItemText.setText(loadString("itemNevek"));}
        if (mavoltletoltes()) {napiItemText.setText(loadString(ido()));}
        osszesItemArray=loadData("osszesItemArray");
        kedvenclistabealito();


        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
            if (mavoltletoltes()){onFinish();}
            }

            public void onFinish() {
                kepUrlStringArray=loadData("kepUrlStringArray");
               if (kepUrlStringArray!=null)  {setUpGridView();}

            if (mavoltletoltes()){
                itemNevek=loadString("itemNevek");
                napiItemText.setText(itemNevek);

            }else {napiItemText.setText("download error");}


            }
        }.start();



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        openActivity1();
                        Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.kedvenc:
                        openActivity2();
                        Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tartalek:
                        Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        Button button=findViewById(R.id.clear);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kedvencclear();

            }
        });

        Button buttonRefresh=findViewById(R.id.refresh_button);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            maiListaRefresh();
            }
        });


        startServiceButton=findViewById(R.id.startservice_button);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idozito(5000,ExampleService.class);

            }
        });

        kepletolto();
    }


    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, KedvencekActivity.class);
        startActivityForResult(intent, 2);
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



    public void mailistacsinalo() {
        itemNevek = "";
      //  maiItemekArray = napilistaletolto();
        for (int i = 0; i < maiItemekArray.size(); i++) {
            String name = maiItemekArray.get(i);
            itemNevek = itemNevek + name + "\n";

        }

        napiItemText.setText(itemNevek);

    }
    public List<String> napilistaletolto() {


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.show();

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
                    saveString(ido(),itemNevek);
                    napiItemText.setText(itemNevek);
                } catch (JSONException e) {
                    napiItemText.setText("hibaa");
                    e.printStackTrace();
                }


                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "Download error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

        return listdata;

    }





    public List<String> osszesitemletolto() {


        dialogosszesitem = new ProgressDialog(this);
        dialogosszesitem.setMessage("Loading....");
        dialogosszesitem.show();

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
                    napiItemText.setText("hibaa");
                    e.printStackTrace();
                }


                dialogosszesitem.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "All item download error", Toast.LENGTH_SHORT).show();
                dialogosszesitem.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

        return listdata;

    }
    public void kedvenclistabealito(){
        if (kedvencekarray.size()!=0) {
            for (int l = 0; l < kedvencekarray.size(); l++) {
                String soronlevo = kedvencekarray.get(l);
                kedvencekarrayString = kedvencekarrayString + soronlevo + "\n";
            }
        }else {kedvencekarrayString=" ";}
        TextView textView = findViewById(R.id.kedvenceklista);
        textView.setText(kedvencekarrayString);

    }
public void kedvencclear (){
        kedvencekarray.clear();
        kedvenclistabealito();
        saveData(kedvencekarray,"kedvencekarray");

}
    public void startService() {

      /*  String input = editTextInput.getText().toString();

        Intent serviceIntent = new Intent(this, ExampleService.class);
        //serviceIntent.putExtra("inputExtra", input);

   //   ContextCompat.startForegroundService(this, serviceIntent);
        this.startService(serviceIntent);*/
        Intent serviceIntent = new Intent(this, ExampleService.class);
    serviceIntent.putExtra("reciver",false);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);

        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(sender);

        Toast.makeText(getApplicationContext(), "Service stopped",
                Toast.LENGTH_LONG).show();
    }
    public void premission (){

    }
    public void irjvalamit(){


    }
    public Date maiItemMentes(){
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime;
    }

    public void saveString (String milyenneven,String mit){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(milyenneven+"string", mit).apply();

    }

    public  String loadString (String milyenNeven){
        String mit= PreferenceManager.getDefaultSharedPreferences(this).getString(milyenNeven+"string", "error");
        return mit;
    }
    public String ido() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }
    public  void norifikacion(){




        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this,CHANNEL_ID);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_android)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog() {
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
                .setContentTitle("sample notification")
                .setContentText("This is sample notification")
                .setContentInfo("Information");
        notificationManager.notify(2, notificationBuilder.build());
    }
    public void idozito(int hanymasodpercmulva,Class mitnyissonmeg){
  /*      Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 5, 18, 13, 11);
        Intent intent = new Intent(MainActivity.this, mitnyissonmeg);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        ((AlarmManager) getSystemService(ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() +
                hanymasodpercmulva*1000, pendingIntent);
/*
/*
        Intent ishintent = new Intent(this, ExampleService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, ishintent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),5000, pintent);

        */

          AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() +
                    hanymasodpercmulva*1000, pendingIntent);

      /*  Calendar c = Calendar.getInstance();
        c.set(2019, 5, 19, 21, 8);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

            if (c.before(Calendar.getInstance())) {
                c.add(Calendar.DATE, 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
*/
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
    public void letolteskesz(String melyiknek){
        saveString(melyiknek,ido());

    }
    public void mindenapIsmetles(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,3);
        calendar.set(Calendar.MINUTE, 4);
        calendar.set(Calendar.SECOND,00);
        long startUpTime = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > startUpTime) {
            startUpTime = startUpTime + 24*60*60*1000;
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

public void kepletolto(){

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
            imageView.setImageBitmap(result);
         //   adapter2.notifyDataSetChanged();
            // textView.setBackground(new BitmapDrawable(getResources(), result));
        }

    }
  /*  public void makeImageArrayList(){


        kepUrlStringArray=loadData("kepUrlStringArray");

        for (int i=0;i<kepUrlStringArray.size();i++) {
            ImageView imageView = new ImageView(this);
            String url=(String)kepUrlStringArray.get(i);
            new DownloadImageFromInternet(imageView)
                    .execute(url);
            arrayListKeszKep.add(imageView);

        }

    }*/

    public LinearLayout.LayoutParams layoutParams(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
        layoutParams.height = width/3;
        layoutParams.width = width/3;

        return layoutParams;
    }
    public void setUpGridView(){

        //    imageView=findViewById(R.id.item_image_view);
        List<String> kepUrlStringArray2 = new ArrayList<String>();
        kepUrlStringArray2 =loadData("stringBitmapArray");
        for (int i=0;kepUrlStringArray2.size()>i;i++){
            ImageView imageView20 = new ImageView(this);
            String string=kepUrlStringArray2.get(i);
            Bitmap bitmap=decodeBase64(string);
            imageView20.setImageBitmap(bitmap);
            mindennelKeszKepesArray.add(imageView20);
        }


        gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(adapter2=new CustomAdapter(this, maiItemekArray, mindennelKeszKepesArray,layoutParams()));
    }
    public void maiListaRefresh(){
        final String itemnevekmasolat=loadString("itemNevek");
        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("reciver",true);


        ContextCompat.startForegroundService(this, serviceIntent);
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (itemnevekmasolat.equals(loadString("itemNevek"))){onFinish();}
            }

            public void onFinish() {

                    itemNevek=loadString("itemNevek");
                    napiItemText.setText(itemNevek);



            }
        }.start();



    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
