package com.countonit.user.swipetabpractice;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.database.MainDB;

import java.util.ArrayList;

/**
 * Created by User on 6/23/2017.
 */

public class Custom_Adapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> income_amount_date_list,timeList;
    ArrayList<String> income_sourceList;
    ArrayList<Integer> amountList;

   public Custom_Adapter (Context c,ArrayList<Integer> amountList,ArrayList<String> income_amount_date,ArrayList<String> income_source,ArrayList<String> time){
        super(c,R.layout.income_infos_list_item,R.id.income_source,income_source);
        this.context=c;
        this.income_amount_date_list=income_amount_date;
        this.income_sourceList=income_source;
        this.timeList=time;
        this.amountList=amountList;
    }

    class ViewHolder{

        TextView income_amount_date;
        TextView income_source,time;
        ViewHolder(View view){
            income_amount_date=(TextView) view.findViewById(R.id.income_amount_date);
            income_source=(TextView) view.findViewById(R.id.income_source);
            time=(TextView) view.findViewById(R.id.time);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        ViewHolder viewHolder=null;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.income_infos_list_item, parent, false);
            viewHolder=new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) view.getTag();
        }


        try{
            SharedPreferences preferences= context.getSharedPreferences("CurrencyPosition",Context.MODE_PRIVATE);
            String symbol= context.getResources().getStringArray(R.array.symbols1)[preferences.getInt("position",1) -1];

            viewHolder.income_amount_date.setText(amountList.get(position)+symbol+" Income On "+income_amount_date_list.get(position));

        }catch (IndexOutOfBoundsException ie){
            ie.printStackTrace();
        }
        viewHolder.income_source.setText(income_sourceList.get(position));
        viewHolder.time.setText(timeList.get(position));



        return view;
    }

    public boolean remove(MainDB mainDB,int position){
        return mainDB.executeSqlForUD(income_amount_date_list.get(position));
    }






}
