package com.example.itemshop4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KedvencekActivity extends AppCompatActivity {
    //fizetni keljen a több kedvencért
    ListView listView;
    List<String> arrayList = new ArrayList<String>();
    List<String> kedvencekarray = new ArrayList<String>();
    ArrayList igaziArrayList=new ArrayList();
    List<String> osszesItemArray = new ArrayList<String>();
    final ArrayList<Word> words = new ArrayList<Word>();
     final ArrayList<Word> wordsAdapter = new ArrayList<Word>();
    WordAdapter adapter;

    EditText searchEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kedvencek);



        searchEdittext=findViewById(R.id.search_edittext);
        setResult(RESULT_OK, null);


        setResult(RESULT_OK, null);
         adapter = new WordAdapter(this, wordsAdapter, R.color.category_family);
       // arrayList= Arrays.asList("Android","PHP","Web Development","Blogger","SEO","Photoshop");
        arrayList=loadData("osszesItemArray");
        kedvencekarray=loadData("kedvencekarray");

        osszesItemArray=loadData("osszesItemArray");
        arrayravalto();
        //adapterKetto();
        adapterHarom();
      //  adapteronclicklistview();
       beszinez();
        //timerbeszinezhez();
        alsomenukeszito();



        searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            search();
            }
        });
    }
    public void adapterHarom (){




        for (int i = 0;i <osszesItemArray.size();i++){
            words.add(new Word(osszesItemArray.get(i),R.color.feher));

        }



        ListView listView = (ListView) findViewById(R.id.list);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positionadapter, long l) {

                Word word = wordsAdapter.get(positionadapter);
                String nevee=word.getDefaultTranslation();
                int position=osszesItemArray.indexOf(nevee);
                if (kedvencekarray.contains(nevee)){
                    kedvencekarray.remove(nevee);
                    word.setColorResouce(R.color.feher);
                    words.set(position,word);
                    saveData(kedvencekarray,"kedvencekarray");
                }else {
                    word.setColorResouce(R.color.category_family);
                    words.set(position, word);
                    kedvencekarray.add(word.getDefaultTranslation());
                    saveData(kedvencekarray,"kedvencekarray");
                }
                adapter.notifyDataSetChanged();


            }
        });


    }
    public void adapterKetto(){

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father",R.color.category_numbers));
        words.add(new Word("fathasdaser",R.color.category_family));



        final WordAdapter adapter = new WordAdapter(this, words, R.color.category_family);

        ListView listView = (ListView) findViewById(R.id.list);


        listView.setAdapter(adapter);


    }
    public void adapteronclicklistview(){
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, igaziArrayList);


        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                TextView textView = (TextView) listView.getChildAt(position);
                String neve = (String)textView.getText();
                if (kedvencekarray.contains(neve)) {
                    kedvencekarray.remove(neve);
                    textView.setBackgroundColor(Color.WHITE);
                }else {
                    kedvencekarray.add(neve);
                    textView.setBackgroundColor(Color.GREEN);

                }
                saveData(kedvencekarray,"kedvencekarray");





                Toast.makeText(KedvencekActivity.this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });
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
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        arrayList = gson.fromJson(json, type);

        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        return arrayList;
    }



    public void beszinez(){

        for(int i=0; i<kedvencekarray.size(); i++){
            String neve= kedvencekarray.get(i);
                int sorszam = arrayList.indexOf(neve);
                if (sorszam!=-1) {
                    Word word = words.get(sorszam);

                    word.setColorResouce(R.color.category_family);
                    words.set(sorszam, word);
                    adapter.notifyDataSetChanged();
                }else {
                    kedvencekarray.remove(i);
                    saveData(kedvencekarray,"kedvencekarray");
                }
        }
        wordsAdapter.clear();
        for (int i = 0;i <words.size();i++){
            wordsAdapter.add(words.get(i));

        }
    }
    public void timerbeszinezhez(){
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                beszinez();
            }
        }.start();
    }
    public  void openActivity1(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity (intent);
    }
    public  void openActivity2(){
        Intent intent = new Intent(this, KedvencekActivity.class);
        startActivity (intent);
    }

public void alsomenukeszito(){


    BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    openActivity1();
                    Toast.makeText(KedvencekActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.kedvenc:
                    openActivity2();
                    Toast.makeText(KedvencekActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tartalek:
                    Toast.makeText(KedvencekActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });
}
    public void arrayravalto(){
    for (int i=0;i<arrayList.size();i++){
        igaziArrayList.add(arrayList.get(i));
    }
    }
    public void search(){

        List<String> egyezok = new ArrayList<String>();
        String iras=(String) searchEdittext.getText().toString();
        if (iras!="") {
            wordsAdapter.clear();
            for (int i = 0; i < osszesItemArray.size(); i++) {
                if (osszesItemArray.get(i).toLowerCase().startsWith(iras)) {
                    wordsAdapter.add(words.get(i));
                }
            }
        }else {
            for (int i = 0;i <words.size();i++){
                wordsAdapter.add(words.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

}
