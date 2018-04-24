package com.countonit.user.swipetabpractice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.r0adkll.slidr.Slidr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 6/28/2017.
 */

public class Repeater {


    String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    DataBase db;
    ArrayList<String> list;
    private MainDB mainDB;


    private Context mContext;


    public Repeater(Context mContext) {
        this.mContext = mContext;
        mainDB=new MainDB(mContext);
    }



    public void recordMonth(){



            String income=new Storage(mContext).loadData();
            ArrayList<String> cate=new ArrayList<>();
            cate=mainDB.getAllCategory();
            ArrayList<String> amns=new ArrayList<>();
            ArrayList<Integer> budgets=mainDB.getAllBudget();

            for (int p=0;p<cate.size(); p++){
                amns.add(mainDB.getAllAmountOfAllCategory(cate.get(p))+"");
            }


            for (int i = 0; i < cate.size(); i++) {

                    int y=Calendar.getInstance().get(Calendar.YEAR);
                    int m=Calendar.getInstance().get(Calendar.MONTH);


                    if( !mainDB.availableThisMonth(months[m],y) ) {

                        if( !mainDB.isMothRecordEmpty() )
                            mainDB.insertData(cate.get(i), amns.get(i), months[m], income, y, budgets.get(i));

                        else {
                            mainDB.insertData(cate.get(i), amns.get(i), months[m], income, y, budgets.get(i));
                            resetAllAmount();
                            resetIncome();

                            Toast.makeText(mContext,"This Month is Finished, Check Your Monthly Statistics",Toast.LENGTH_LONG).show();

                        }

                    }else{
                        mainDB.updateMonthData(cate.get(i), amns.get(i), months[m], income, y, budgets.get(i));
                    }

            }

    }


    public void resetAllAmount() {

        mainDB.resetAllWhenMonthEnd();

    }

    public void resetIncome(){
        Storage storage=new Storage(mContext);

        storage.saveData("0");
    }

}
