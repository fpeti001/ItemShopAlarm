package com.ige.itemshop4;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class KedvencAdapter extends ArrayAdapter<String> {


    private int mColorResourceId;


    public KedvencAdapter(Context context, List<String> words) {
        super(context, 0, words);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        final String currentWord = getItem(position);

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        ImageView defaultImageView=(ImageView) listItemView.findViewById(R.id.delete_image_view);

        defaultTextView.setText(currentWord);
        defaultImageView.setImageResource(R.drawable.ic_delete_black_24dp);


        return listItemView;
    }


}
