package com.example.itemshop4;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    LinearLayout.LayoutParams layoutParams1;

    Context context;
    ArrayList arrayListAdapter = new ArrayList();
    List<String> maiItemekArray = new ArrayList<String>();
    private static LayoutInflater inflater = null;

    public CustomAdapter(MainActivity mainActivity,List<String> maiItemekArraybejovo, ArrayList bejovoArrayList, LinearLayout.LayoutParams layoutParams2) {
        // TODO Auto-generated constructor stub
        maiItemekArray=maiItemekArraybejovo;
        layoutParams1 = layoutParams2;

        context = mainActivity;
        arrayListAdapter = bejovoArrayList;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return maiItemekArray.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView os_text;
        ImageView os_img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.image_item, null);
        holder.os_text = (TextView) rowView.findViewById(R.id.item_text_view);
        holder.os_img = (ImageView) rowView.findViewById(R.id.item_image_view);

        holder.os_text.setText(maiItemekArray.get(position));
        holder.os_img.setImageDrawable(((ImageView)arrayListAdapter.get(position)).getDrawable());
        rowView.setLayoutParams(layoutParams1);


        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + maiItemekArray.get(position), Toast.LENGTH_SHORT).show();

            }
        });

        return rowView;
    }


}