package com.countonit.user.swipetabpractice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by User on 7/18/2017.
 */

public class Custom_Report extends AppCompatActivity{

    PieChart mChart;
    String date;
    MainDB mainDB;

    float[] costs;
    String[] expenses;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_report);

        Toolbar toolbar= findViewById(R.id.toolbar2);
        toolbar.setTitle("Get Report Of A Day");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChart=(PieChart) findViewById(R.id.customdatechart);

        MaterialCalendarView mcv=(MaterialCalendarView) findViewById(R.id.calendarView);
        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                        setCustomReportFor(date.getYear(),date.getMonth()+1,date.getDay());
            }
        });

        mainDB=new MainDB(this);


    }

    private void setCustomReportFor(int year, int month, int day) {

        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(year);

        if (month <10){
            stringBuffer.append("/0"+month);
        }
        else
            stringBuffer.append("/"+month);


        if (day<10){
            stringBuffer.append("/0"+day);
        }
        else
            stringBuffer.append("/"+day);

        ArrayList<String> list=new ArrayList<>();
        list=mainDB.getCostByDate(stringBuffer.toString());
        expenses=new String[list.size()+1];
        expenses=mainDB.getAllExpenseByDate(stringBuffer.toString());
        costs=new float[list.size()+1];
        ListIterator listIterator=list.listIterator();
        int h=0;
        while (listIterator.hasNext()){
            costs[h]=Float.parseFloat(listIterator.next().toString());
            h++;
        }
        Toast.makeText(getBaseContext(),stringBuffer.toString(),Toast.LENGTH_SHORT).show();
        setUpExpensCostChart();


    }



    private void setUpExpensCostChart() {


        List<PieEntry> entry=new ArrayList<>();

        for (int i=0; i<expenses.length; i++){
            entry.add(new PieEntry(costs[i],expenses[i]));
        }
        PieDataSet dataSet=new PieDataSet(entry,"Expenses Figure");
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueLinePart1OffsetPercentage(90.f);
        dataSet.setValueLinePart1Length(.2f);
        dataSet.setValueLinePart2Length(.4f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        PieData data=new PieData(dataSet);
        mChart.setData(data);

        mChart.setEntryLabelColor(Color.BLACK);
        mChart.animateY(700);
        mChart.invalidate();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_bottom_toolbar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this,RootScreen.class));
                finish();
                break;

        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
            startActivity(new Intent(this,RootScreen.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
