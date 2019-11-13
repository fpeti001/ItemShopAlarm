package com.ige.itemshop4;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static android.app.PendingIntent.FLAG_NO_CREATE;

public class SettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{
int nappaliMinute;
int nappaliHour;
TextView napkozbeniIdo;
Switch napkozbeniSwitch;
Switch wifiSwitch;
ImageView napkozbeniInfoImgae;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        napkozbeniInfoImgae=findViewById(R.id.napkozbeni_info_image);
        napkozbeniIdo=findViewById(R.id.napkozbeni_ebresztes_ido_textview);
        napkozbeniSwitch=findViewById(R.id.napkozbeni_ebresztes_ido_switch);
        wifiSwitch=findViewById(R.id.wifi_switch);

       napkozbeniSwitch.setChecked(loadBoolean("napkozbeniswitch"));
        wifiSwitch.setChecked(loadBoolean("wifiswitch"));


       nappaliHour=loadInt("hour");
       nappaliMinute=loadInt("minute");
       String kiirando;
        if (nappaliMinute<10){kiirando=":0"+nappaliMinute;}else {kiirando=":"+nappaliMinute;}
        napkozbeniIdo.setText(nappaliHour+kiirando);





        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        openActivity1();

                        break;
                    case R.id.kedvenc:
                        openActivity2();

                        break;
                    case R.id.tartalek:
                     //   openActivity3();

                        break;
                }
                return true;
            }
        });

        napkozbeniSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveBoolean("napkozbeniswitch",isChecked);

                if (isChecked==false) {
                    Intent intent = new Intent(SettingsActivity.this, AlertReceiver.class);
                    PendingIntent sender = PendingIntent.getBroadcast(SettingsActivity.this, 1002, intent, FLAG_NO_CREATE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    if (alarmManager != null && sender != null) {
                        alarmManager.cancel(sender);
                    }
                    if (sender != null) {
                        sender.cancel();
                    }

                }else {
                    Intent serviceIntent = new Intent(SettingsActivity.this, ExampleService.class);
                    serviceIntent.putExtra("extra","settings");

                    ContextCompat.startForegroundService(SettingsActivity.this, serviceIntent);

                }


            }
        });

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveBoolean("wifiswitch",isChecked);


            }
        });
        napkozbeniIdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        napkozbeniInfoImgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kiirandoHour;
                String kiirandoMin;
                Calendar calendar= Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis() + BasicMethods.hatralevoIdoFrissitesig());
                int hour= calendar.get(Calendar.HOUR_OF_DAY);
                int min=calendar.get(Calendar.MINUTE);

                String delelottutan;
                if (hour>11){delelottutan=" ";}else {delelottutan=" AM ";}
                if (min<10){kiirandoMin=":0"+min;}else {kiirandoMin=":"+min;}
              //  if (hour<10){kiirandoHour="0"+min;}else {kiirandoHour=hour+"";}
                int kiirando=R.string.napkozbeni_ertesites_info;
                infoDialog((String)getText(R.string.napkozbeni_ertesites_info)+" "+hour+kiirandoMin+delelottutan+(String)getText(R.string.napkozbeni_ertesites_info2));
            }
        });
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        nappaliHour=hourOfDay;
        nappaliMinute=minute;
        saveInt("hour",hourOfDay);
        saveInt("minute",minute);
        String kiirando;

       if (nappaliMinute<10){kiirando=":0"+nappaliMinute;}else {kiirando=":"+nappaliMinute;}
        napkozbeniIdo.setText(nappaliHour+kiirando);
    if (napkozbeniSwitch.isChecked()) {
        Intent serviceIntent = new Intent(SettingsActivity.this, ExampleService.class);
        serviceIntent.putExtra("extra", "settings");

        ContextCompat.startForegroundService(SettingsActivity.this, serviceIntent);
    }


    }

    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void openActivity2() {
        Intent intent = new Intent(this, KedvencekHatterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    public void openActivity3() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity (intent);

    }
    public void saveString (String milyenneven,String mit){
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(milyenneven+"string", mit).apply();

    }
    public  String loadString (String milyenNeven){
        String mit= PreferenceManager.getDefaultSharedPreferences(this).getString(milyenNeven+"string", "error");
        return mit;
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
    public void infoDialog(String szoveg){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.info_popup);
        TextView txt = (TextView)dialog.findViewById(R.id.info_text);
        txt.setText(szoveg);
        dialog.show();

    }
}
