package com.countonit.user.swipetabpractice;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.r0adkll.slidr.Slidr;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 7/19/2017.
 */

public class TodayReport extends AppCompatActivity {

    float[] amount;
    String[] expense;
    PieChart pieChart;
    Spinner spinner;
    MainDB mainDB;
    String[] day={"Sunday","Monday","Tuesday","Wednessday","Thrusday","Friday","Saturday"};
    String[] str;

    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    Button button;
    Methods methods;

    int appTheme;
    int themeColor;
    int appColor;
    Constant constant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        appColor = app_preferences.getInt("color", 0);
        appTheme = app_preferences.getInt("theme", 0);
        themeColor = appColor;
        constant.color = appColor;

        if (themeColor == 0){
            setTheme(Constant.theme);
        }else if (appTheme == 0){
            setTheme(Constant.theme);
        }else{
            setTheme(appTheme);
        }
        //overridePendingTransition(0,R.anim.fadin);
        setContentView(R.layout.today_report);
        Slidr.attach(this);



        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Today's Report");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mainDB=new MainDB(this);
        //db=new DataBase3(this);
        spinner=(Spinner) findViewById(R.id.spinner);
        ArrayList<String> catList=new ArrayList<>();
        int i=1;
        catList=mainDB.getAllCategory();
        str=new String[catList.size()+1];
        str[0]="Pick A Category";
        for (String s : catList){
            str[i]=s.substring(0,1).toUpperCase()+s.substring(1);
            i++;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        TextView today=(TextView)findViewById(R.id.today);
        pieChart=(PieChart) findViewById(R.id.today_chart);
        today.setText(day[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) -1]);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDataFromDB(str[position]);
                setUpPie();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setDataFromDB(String st) {
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String dates=dateFormat1.format(date);
        ArrayList<String> exp=new ArrayList<>();
        ArrayList<Integer> costs=new ArrayList<>();
        //costs=mainDB.getCostByCategory(st);
        exp=mainDB.getExpenseTitleByCategory(st);

        for (int l=0; l<exp.size(); l++){
            costs.add(mainDB.getCostByDateExpense(dates,exp.get(l)));
        }

        int i=0;

        amount=new float[costs.size()];
        expense=new String[exp.size()];
        for(Integer str : costs){
            amount[i]=str;
            i++;
        }
        i=0;
        for (String s : exp){
            expense[i]=s;
            i++;
        }
    }


    private void setUpPie() {

        ArrayList<PieEntry> pieEntry=new ArrayList<>();

        for (int i=0; i<expense.length; i++){
            pieEntry.add(new PieEntry(amount[i],expense[i]));
        }

        PieDataSet dataSet= new PieDataSet(pieEntry,"Expense Figure");
        //dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        dataSet.setValueTextColor(Color.BLACK);
        /*ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());*/
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueLinePart1OffsetPercentage(90.f);
        dataSet.setValueLinePart1Length(.2f);
        dataSet.setValueLinePart2Length(.4f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        PieData data=new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.MONOSPACE);
        pieChart.setData(data);
        pieChart.setEntryLabelColor(Color.BLACK);

        pieChart.animateXY(500,500);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        pieChart.invalidate();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }


        return true;
    }
}
