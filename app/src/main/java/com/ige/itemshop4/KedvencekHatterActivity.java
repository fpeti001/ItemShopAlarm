package com.ige.itemshop4;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class KedvencekHatterActivity extends AppCompatActivity {
    KedvencAdapter adapter;
    List<String> kedvencekarray = new ArrayList<String>();
    DialogInterface.OnClickListener dialogClickListener;
    DialogInterface.OnClickListener dialogClickListener2;
    int torlendoszama;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kedvencek_hatter);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




        igennemegyitem();
        igennemclear();
        IntentFilter filter = new IntentFilter("com.toxy.NOTIFY");
        this.registerReceiver(new Receiver(), filter);
        kedvencekarray=loadData("kedvencekarray");
        alsomenukeszito();
        kedvencAdapterBeallito();
        Button plusButton=findViewById(R.id.plus_button);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKedvencekActivity();
            }
        });



        Button clearButton=findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(getText(R.string.clear_warning)).setPositiveButton(getText(R.string.yes), dialogClickListener2)
                        .setNegativeButton(getText(R.string.no), dialogClickListener2).show();
            }
        });

    }




    public  void openActivity1(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity (intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    public  void openActivity2(){
        Intent intent = new Intent(this, KedvencekHatterActivity.class);
        startActivity (intent);

    }
    public  void openKedvencekActivity(){
        Intent intent = new Intent(this, KedvencekActivity.class);
        startActivity (intent);
    }
    public  void openActivity3(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity (intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void alsomenukeszito(){


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        openActivity1();
                        break;
                    case R.id.kedvenc:
                       // openActivity2();
                        break;
                    case R.id.tartalek:
                        openActivity3();
                        break;
                }
                return true;
            }
        });
    }
    private List<String> loadData(String melyiket) {
        List<String> arrayList = new ArrayList<String>();
        SharedPreferences sharedPreferences = getSharedPreferences(melyiket, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        arrayList = gson.fromJson(json, type);

        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        return arrayList;
    }
    private void saveData( List<String> arrayList,String milyenneven) {
        SharedPreferences sharedPreferences = getSharedPreferences(milyenneven, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("task list", json);
        editor.apply();
    }
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            kedvencekarray=loadData("kedvencekarray");
            kedvencAdapterBeallito();

           } }


    public void kedvencAdapterBeallito(){
        adapter = new KedvencAdapter(this, kedvencekarray);
        ListView kedvencekListview=findViewById(R.id.kivalasztott_kedvencek_listview);
        kedvencekListview.setAdapter(adapter);

        kedvencekListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positionadapter, long l) {
                torlendoszama=positionadapter;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(getText(R.string.delete_one_element_warning)+" >"+kedvencekarray.get(positionadapter)+"<").setPositiveButton(getText(R.string.yes), dialogClickListener)
                        .setNegativeButton(getText(R.string.no), dialogClickListener).show();


            }
        });
    }
    public void clearKedvencLista(){
        kedvencekarray.clear();
        saveData(kedvencekarray,"kedvencekarray");
        kedvencAdapterBeallito();
    }
    public void igennemegyitem(){
       dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        kedvencekarray.remove(torlendoszama);
                        saveData(kedvencekarray,"kedvencekarray");
                        adapter.notifyDataSetChanged();
                        //Yes button clicked

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
    }
    public void igennemclear(){
        dialogClickListener2 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                    clearKedvencLista();
                        //Yes button clicked

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
    }
}
