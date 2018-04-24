package com.countonit.user.swipetabpractice;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.countonit.user.swipetabpractice.statistics.XYMarkerView;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 6/22/2017.
 */

public class Fragment1 extends Fragment implements OnChartValueSelectedListener {

    String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    String[] days={"Sunday","Monday","Tuesday","Wednessday","Thrusday","Friday","Saturday"};
    com.github.mikephil.charting.charts.BarChart barChart;

    String[] expense;
    MainDB dataBase3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.bar_chart,container,false);

        TextView month=(TextView) view.findViewById(R.id.month);
        Calendar calendar=Calendar.getInstance();
        month.setText(months[calendar.get(Calendar.MONTH)]+", "+days[calendar.get(Calendar.DAY_OF_WEEK)-1]);
        barChart=(com.github.mikephil.charting.charts.BarChart) view.findViewById(R.id.barchart);
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(barChart,getActivity());
        dataBase3=new MainDB(getActivity());



        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.MONOSPACE);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.MONOSPACE);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setTypeface(Typeface.MONOSPACE);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);


        ArrayList<BarEntry> barEntries=new ArrayList<>();
        MainDB db=new MainDB(getActivity());
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        Date dateT=new Date();
        String dates=dateFormat1.format(dateT);
        //String[] exp=db.getAllExpenseByDate(dates);

        String[] cat=db.getCategoryByCurrentDate(dates);

        float f=0f;
        ArrayList<Integer> cost=new ArrayList<>();
        ArrayList<String> date=new ArrayList<>();
        date=db.getDistinctDate();
        if (date.size() !=0)
        {
           // Toast.makeText(getActivity(),""+date.get(0),Toast.LENGTH_SHORT).show();
            for (int i=0; i<cat.length; i++){
                cost.add(db.getTotalOfCategory(cat[i],dates));
                //Toast.makeText(getActivity(),"ok "+date.get(0),Toast.LENGTH_SHORT).show();
            }
        }

        int i=0;
        for (; i<cost.size(); i++){
            barEntries.add(new BarEntry(f,cost.get(i)));
            f++;
        }


        BarDataSet barDataSet=new BarDataSet(barEntries,"The Year "+Calendar.getInstance().get(Calendar.YEAR));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        ArrayList<IBarDataSet> iBarDataSets=new ArrayList<IBarDataSet>();
        iBarDataSets.add(barDataSet);


        ArrayList<String> labels=new ArrayList<>();
        int k;
        for (k=0; k<cat.length; k++){



            labels.add(cat[k].toUpperCase());

        }
        //labels.add(0,exp[0].toUpperCase());


        BarData barData=new BarData(iBarDataSets);
        barData.setValueTextSize(10f);

        XYMarkerView mv = new XYMarkerView(getActivity(), xAxisFormatter,cat);
        mv.setChartView(barChart); // For bounds control


        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        barChart.setMarker(mv);

        barChart.setData(barData);

        barChart.animateXY(3000,3000);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);



        Legend l=barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        barChart.invalidate();



        return view;
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        barChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = barChart.getPosition(e, YAxis.AxisDependency.LEFT);


        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() { }

}
