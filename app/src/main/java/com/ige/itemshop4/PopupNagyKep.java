package com.ige.itemshop4;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PopupNagyKep extends Activity {
    ImageView nagyKepImageView;
int hanyadikkep;
    List<String> nagystringBitmapArray = new ArrayList<String>();
    List<String> maiItemekArray = new ArrayList<String>();
    ImageView youtubeImageView;
    ImageView googleImageView;
    Button backButton;
    TextView nagyKepTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_nagy_kep);



        youtubeImageView=findViewById(R.id.youtube_imageview);
        googleImageView=findViewById(R.id.google_imageview);
        backButton=findViewById(R.id.back_button);
        nagyKepTextView=findViewById(R.id.kep_neve_textview);

        maiItemekArray= loadData("maiItemekArray");
        nagystringBitmapArray=loadData("nagystringBitmapArray");
        Bundle extras = getIntent().getExtras();
        hanyadikkep= extras.getInt("hanyadikkep");

       nagyKepImageView=findViewById(R.id.nagy_kep_image_view);
        nagyKepBetolto();

        youtubeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            keressYoutubeon(maiItemekArray.get(hanyadikkep));
            }
        });
        googleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            keressGoogleben(maiItemekArray.get(hanyadikkep));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void nagyKepBetolto(){
        if (nagystringBitmapArray.size()!=0) {
            if (hanyadikkep<nagystringBitmapArray.size()&&nagystringBitmapArray.get(hanyadikkep)!= null) {
                String bitmapString = nagystringBitmapArray.get(hanyadikkep);
                String nagykepNev=maiItemekArray.get(hanyadikkep);
                nagyKepTextView.setText(nagykepNev);
                Bitmap bitmap = decodeBase64(bitmapString);
                nagyKepImageView.setImageBitmap(bitmap);
            }else {
                String nagykepNev=maiItemekArray.get(hanyadikkep);
                nagyKepTextView.setText(nagykepNev);
                TextView textView=findViewById(R.id.no_image_textview);
                textView.setText("\n"+getText(R.string.no_image_text));
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
                lp.setMargins(0,0,0,25);
                textView.setLayoutParams(lp);
            }
        }

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
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

public void keressYoutubeon(String mit){

    Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + mit+" fortnite"));
    startActivity(intent);

}
public void keressGoogleben(String mit){
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q="+mit+" fortnite"));
    startActivity(browserIntent);
}
public void back(){
    super.onBackPressed();
}
}
