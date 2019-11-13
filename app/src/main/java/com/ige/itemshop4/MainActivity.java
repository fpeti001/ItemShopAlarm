package com.ige.itemshop4;

import android.annotation.SuppressLint;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.app.PendingIntent.FLAG_NO_CREATE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.ige.itemshop4.App.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {
    Button proba;
    String asaad="";
    private Button home;
    private Button kedvencek;
    private Button tartalekbutt;
    List<String> kedvencekarray = new ArrayList<String>();
    List<String> maiItemekArray = new ArrayList<String>();
    List<String> osszesItemArray = new ArrayList<String>();
    ArrayList mindennelKeszKepesArray=new ArrayList();
    List<String> itemPriceArray = new ArrayList<String>();
    String osszesItemNevek="";
    String itemNevek = "";
    String kedvencekarrayString = "";
    String napiurl = "https://fortnite-public-api.theapinetwork.com/prod09/store/get";
    String osszesitemurl = "https://fortnite-public-api.theapinetwork.com/prod09/items/list";
    ProgressDialog dialog;
    ProgressDialog dialogosszesitem;
    Boolean dialogFalse=false;
    ArrayList<String> listdata = new ArrayList<String>();
    TextView pprobaTextView;
    private PendingIntent pIntent;
    Button startServiceButton;
    CountDownTimer visszaszamolo;
    Button refresh;

    ArrayList arrayListKeszKep=new ArrayList();
    CustomAdapter adapter2;
    GridView gridView;
    List<String> kepUrlStringArray = new ArrayList<String>();
    List<String> nagystringBitmapArray = new ArrayList<String>();
    TextView popupTextView;
    String fejlectitle;
    ///azert kell hogy frissitsen ha visszalepek a kedvencek tabrol


 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//OCLICKLISTENER START HIER----------------------------->


        getSupportActionBar().setTitle((String)getText(R.string.daly_title));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

if (loadBoolean("sokadikinditas")==false)elsoInditas();

                IntentFilter filter = new IntentFilter("com.toxy.LOAD_URL");
        this.registerReceiver(new Receiver(), filter);

        kedvencekarray = loadData("kedvencekarray");
       if (vanMarAlarm()!=true)mindenapIsmetles();


        if (mavoltletoltes()) {
            maiItemekArray=loadData("maiItemekArray");
            itemPriceArray=loadData("itemPriceArray");
            nagystringBitmapArray=loadData("nagystringBitmapArray");
            setUpGridView();
            gridonclickcsinalo();

        }else {
           saveData(maiItemekArray,"maiItemekArray");
           saveData(itemPriceArray,"itemPriceArray");
            saveData(nagystringBitmapArray,"nagystringBitmapArray");
            dialog = ProgressDialog.show(this, "",
                    (String)getText(R.string.loading_please_wait), true);
            dialogFalse=true;
        }

        frissitesigVisszaszamolas();

        startService();




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                       // openActivity1();

                        break;
                    case R.id.kedvenc:
                        openActivity2();

                        break;
                    case R.id.tartalek:
                        openActivity3();

                        break;
                }
                return true;
            }
        });









    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.felso_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.vissza_szamolo_text).setTitle(fejlectitle);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popup_fejlecbe:
                onButtonShowPopupWindowClick();
            case R.id.refresh_felul_button:
                refresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, KedvencekHatterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
    public void openActivity3() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity (intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void saveData( List<String> arrayList,String milyenneven) {
        SharedPreferences sharedPreferences = getSharedPreferences(milyenneven, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("task list", json);
        editor.apply();
    }
    public void saveInt (String milyenneven,int mit){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(milyenneven+"int", mit).apply();

    }
    public  int loadInt (String milyenNeven){
        int mit= PreferenceManager.getDefaultSharedPreferences(this).getInt(milyenNeven+"int", 0);
        return mit;
    }
    public void saveBoolean (String milyenneven,Boolean mit){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(milyenneven+"boolean", mit).apply();

    }
    public  Boolean loadBoolean (String milyenNeven){
        Boolean mit= PreferenceManager.getDefaultSharedPreferences(this).getBoolean(milyenNeven+"boolean", false);
        return mit;
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

    public void saveDate (String milyenneven,Date mit){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putLong(milyenneven+"date", mit.getTime()).apply();

    }
    public  Date loadDate (String milyenNeven){
        Long bejovolong= PreferenceManager.getDefaultSharedPreferences(this).getLong(milyenNeven+"date", 0);
        Date mit = new Date(bejovolong);
        return mit ;
    }




    public void startService() {

      /*  String input = editTextInput.getText().toString();

        Intent serviceIntent = new Intent(this, ExampleService.class);
        //serviceIntent.putExtra("inputExtra", input);

   //   ContextCompat.startForegroundService(this, serviceIntent);
        this.startService(serviceIntent);*/


        Intent serviceIntent = new Intent(this, ExampleService.class);
    serviceIntent.putExtra("extra","mainactivity");

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);

        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 1001, intent, FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager!= null&&sender!=null) {
            alarmManager.cancel(sender);
        }
        if (sender!=null) {
            sender.cancel();
        }
        disableBootReciver();

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
    public Date idoDate() {

        Date currentTime = Calendar.getInstance().getTime();
        return  currentTime;
    }
    public String ido() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }
    public  void norifikacion(){




        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, FLAG_UPDATE_CURRENT);

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

        saveDate(melyiknek, eddigFriss);
//asdd
    }
    public void mindenapIsmetles(){



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
        popupTextAdd("Service Calendar: " + calendarNemzetkozi.getTime());

        long startUpTime = calendarNemzetkozi.getTimeInMillis();
        if (System.currentTimeMillis() > startUpTime) {
            startUpTime = startUpTime + 24 * 60 * 60 * 1000;

        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, AlarmManager.INTERVAL_DAY, pendingIntent);

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
            imageView.setImageBitmap(result);
         //   adapter2.notifyDataSetChanged();
            // textView.setBackground(new BitmapDrawable(getResources(), result));
        }

    }
  public void makeImageArrayList(){


        kepUrlStringArray=loadData("kepUrlStringArray");

        for (int i=0;i<kepUrlStringArray.size();i++) {
            ImageView imageView = new ImageView(this);
            String url=(String)kepUrlStringArray.get(i);
            new DownloadImageFromInternet(imageView)
                    .execute(url);
            arrayListKeszKep.add(imageView);

        }
      maiItemekArray=loadData("maiItemekArray");
      gridView = (GridView) findViewById(R.id.grid_view);
      gridView.setAdapter(adapter2=new CustomAdapter(this, maiItemekArray, arrayListKeszKep,layoutParams(),itemPriceArray));

    }

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
        gridView.setAdapter(adapter2=new CustomAdapter(this, maiItemekArray, mindennelKeszKepesArray,layoutParams(),itemPriceArray));

    }
    public void maiListaRefresh(){
        final String itemnevekmasolat=loadString("itemNevek");
        Intent serviceIntent = new Intent(this, ExampleService.class);
        serviceIntent.putExtra("extra","refresh");


        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent arg1) {
            String extra = arg1.getStringExtra("extra");
        switch (extra) {
    case "nagykep":
        nagystringBitmapArray = loadData("nagystringBitmapArray");
        gridonclickcsinalo();

        if (dialogFalse) {
            dialog.dismiss();
        }
        break;
    case "kiskep":
        //makeImageArrayList();
        //mitortenjen a receievkor:


        kepUrlStringArray = loadData("kepUrlStringArray");
        maiItemekArray = loadData("maiItemekArray");
        itemPriceArray = loadData("itemPriceArray");
        if (kepUrlStringArray != null) {
            setUpGridView();
        }
        if (dialogFalse) {
            dialog.dismiss();
        }
        break;
    case "error":
        if (dialogFalse) {
            dialog.dismiss();
        }
        if (BasicMethods.checkWifiOnAndConnected(context)) {
            infoDialog((String) getText(R.string.download_error));
        } else {
            infoDialog((String) getText(R.string.no_internet_connection));
            break;
        }
    }

        }
    }

    public void sharednullazo (View v){

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.commit();


    }
    public void onButtonShowPopupWindowClick() {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        String inudlasiidolista;
        inudlasiidolista= loadString("inudlasiidolista");
        popupTextView=popupView.findViewById(R.id.hatter_indulas_text_view);
        popupTextView.setText(inudlasiidolista);




        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);




        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        View anchor = this.getWindow().getDecorView().getRootView();
        popupWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });


    }

    public void refreshbutton(View view){
        maiListaRefresh();
    }


    public void startserviceonclick(View view){
        mindenapIsmetles();
        vanMarAlarm();
      //  idozito(5000,ExampleService.class);
    }

    public void indulasiidolistabeallito(){
        String inudlasiidolista;
        inudlasiidolista= loadString("inudlasiidolista");
        TextView textView=findViewById(R.id.hatter_indulas_text_view);
        textView.setText(inudlasiidolista);
    }
    public void popupTextAdd(String string){

        string=string+"\n";
        String inudlasiidolista;
        inudlasiidolista=loadString("inudlasiidolista");
        inudlasiidolista=string+inudlasiidolista;
        saveString("inudlasiidolista",inudlasiidolista);
    }
    public void gridonclickcsinalo(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (maiItemekArray.size()!=0) {
                    if (maiItemekArray.get(position)!=null) {

                        nagyPopupIndito(position);

                    }
                }



            }
        });

    }
public void nagyPopupIndito(int hanyadikkep){


    Intent intent =new Intent(this,PopupNagyKep.class);
    intent.putExtra("hanyadikkep",hanyadikkep);
    startActivity(intent);
}
public  Boolean vanMarAlarm(){
        Boolean visszaad=true;
   Intent intent = new Intent(this, AlertReceiver.class);
    PendingIntent sender = PendingIntent.getBroadcast(this, 1001, intent, PendingIntent.FLAG_NO_CREATE);
    PendingIntent sender2 = PendingIntent.getBroadcast(this, 1002, intent, PendingIntent.FLAG_NO_CREATE);
    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    boolean isWorking =(alarmManager!= null);
    boolean isWorking2 =(sender!= null);
    boolean isWorking3 =(sender2!= null);//just changed the flag
    popupTextAdd("Pendingintent bevankapcsolva: "+isWorking2);
    popupTextAdd("Pendingintentnappal bevankapcsolva: "+isWorking3);
    if (isWorking&&isWorking2){}else {visszaad=false;}
    return visszaad;
}
public void enableBootReciver(){
    ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
    PackageManager pm = this.getPackageManager();

    pm.setComponentEnabledSetting(receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP);
}
public void disableBootReciver(){
    ComponentName receiver = new ComponentName(this, SampleBootReceiver.class);
    PackageManager pm = this.getPackageManager();

    pm.setComponentEnabledSetting(receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP);
}
public void elsoInditas(){
       saveInt("hour",17);
       saveInt("minute",0);
       saveBoolean("napkozbeniswitch",false);
    saveBoolean("wifiswitch",false);
       saveBoolean("sokadikinditas",true);
}
public void paddingintentTeszt(View v){
        vanMarAlarm();
    popupTextView.setText(loadString("inudlasiidolista"));
    fejlectitle="markolo";


}
public void frissitesigVisszaszamolas(){

    Calendar calendar = Calendar.getInstance();

    calendar.setTimeInMillis(System.currentTimeMillis() + BasicMethods.hatralevoIdoFrissitesig());



    long startUpTime = calendar.getTimeInMillis();

    final long beirandoKezdeshez=startUpTime-System.currentTimeMillis();

    visszaszamolo=new CountDownTimer(beirandoKezdeshez, 1000) { // adjust the milli seconds here


        public void onTick(long millisUntilFinished) {
        fejlectitle="";
        //    long Days = beirando / (24 * 60 * 60 * 1000);
            long Hours = millisUntilFinished / (60 * 60 * 1000) % 24;
            long Minutes = millisUntilFinished / (60 * 1000) % 60;
            long Seconds = millisUntilFinished / 1000 % 60;
            //
            fejlectitle=(String.format("%02d", Hours)+":");
            fejlectitle+=(String.format("%02d", Minutes)+":");
            fejlectitle+=(String.format("%02d", Seconds));
        //   fejlectitle=Hours+":"+Minutes+":"+Seconds;

            invalidateOptionsMenu();
        }

        public void onFinish() {
            fejlectitle="done!";
        }
    }.start();

}
public void lealllito_culdown(View v){
        visszaszamolo.cancel();
}
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void infoDialog(String szoveg){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.info_popup);
        TextView txt = (TextView)dialog.findViewById(R.id.info_text);
        txt.setText(szoveg);
        dialog.show();

    }
    public void refresh(){
maiItemekArray.clear();
itemPriceArray.clear();
nagystringBitmapArray.clear();
        gridView.setOnItemClickListener(null);
        saveData(maiItemekArray,"maiItemekArray");
        saveData(itemPriceArray,"itemPriceArray");
        saveData(nagystringBitmapArray,"nagystringBitmapArray");

        Date h3;
        Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -10);
            h3 = calendar.getTime();
        saveDate("napilistaletolto",h3);
        saveDate("osszesitemletolto",h3);

        dialog = ProgressDialog.show(this, "",
                (String)getText(R.string.loading_please_wait), true);
        dialogFalse=true;
        startService();
    }
}
