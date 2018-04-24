package com.countonit.user.swipetabpractice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.countonit.user.swipetabpractice.R;

/**
 * Created by User on 11/29/2017.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private Integer[] imageArray;

    public SpinnerAdapter(Context context,
                          Integer[] imageArray) {
        super(context,  R.layout.cat_icon);
        this.ctx = context;
        this.imageArray = imageArray;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)  ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.cat_icon, parent, false);

        /*TextView textView = (TextView) row.findViewById(R.id.catName);
        textView.setText(contentArray[position]);
*/
        ImageView imageView = (ImageView) row.findViewById(R.id.catIconView);
        imageView.setImageResource(imageArray[position]);

        return row;
    }
}
