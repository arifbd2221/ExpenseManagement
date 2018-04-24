package com.countonit.user.swipetabpractice;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by User on 7/19/2017.
 */

public class BarChartG extends AppCompatActivity {

    String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};

    String from,to;
    com.github.mikephil.charting.charts.BarChart barChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        Bundle intentData=intent.getExtras();
        from=intentData.getString("from");
        to=intentData.getString("to");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bar_chart);
        TextView month=(TextView) findViewById(R.id.month);
        Calendar calendar=Calendar.getInstance();
        month.setText(months[calendar.get(Calendar.MONTH)]);
        barChart=(com.github.mikephil.charting.charts.BarChart) findViewById(R.id.barchart);
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart,this);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(Typeface.DEFAULT);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)




        ArrayList<BarEntry> barEntries=new ArrayList<>();
        MainDB db=new MainDB(this);
        float f=44f;
        ArrayList<String> cost=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();
        date=db.getDistinctDate();
        cost=db.getCostByDate(date.get(0));
        for (int i=0; i<cost.size(); i++){
            int temp=Integer.parseInt(cost.get(i));
            barEntries.add(new BarEntry(f,temp));
            f=f+4f;
        }


        BarDataSet barDataSet=new BarDataSet(barEntries,"The Year "+Calendar.getInstance().get(Calendar.YEAR));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> iBarDataSets=new ArrayList<IBarDataSet>();
        iBarDataSets.add(barDataSet);

        barChart.getLegend().setWordWrapEnabled(true);

        BarData barData=new BarData(iBarDataSets);
        barData.setValueTextSize(10f);
        barChart.setData(barData);

        barChart.animateXY(3000,3000);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }
}
