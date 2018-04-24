package com.countonit.user.swipetabpractice;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.util.ArrayList;

/**
 * Created by User on 11/22/2017.
 */

public class BudgetVsExpense extends Fragment {

    private BarChart chart;
    float barWidth;
    float barSpace;
    float groupSpace;
    ArrayList<Integer> budgets;
    ArrayList<String> expenses;
    ArrayList<Integer> amounts;
    int groupCount = 6;
    Button first;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        barWidth = 0.3f;
        barSpace = 0f;
        groupSpace = 0.4f;

        final MainDB mainDB=new MainDB(getActivity());
        View view=inflater.inflate(R.layout.budgetvsexpense,container,false);

       /* years=(Spinner) view.findViewById(R.id.year);
        months=(Spinner) view.findViewById(R.id.month);*/
       first=(Button) view.findViewById(R.id.first);

        final YearMonthPickerDialog yearMonthPickerDialog=new YearMonthPickerDialog(getActivity(), new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                budgets=mainDB.getBudgetsByMonth(getResources().getStringArray(R.array.month)[month+1]);
                expenses=mainDB.getExpenseByMonth(getResources().getStringArray(R.array.month)[month+1]);
                amounts=mainDB.getExpenseAmountByMonth(getResources().getStringArray(R.array.month)[month+1]);
                //Toast.makeText(getActivity(),""+months.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                setUpBarChart();
            }
        });


        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearMonthPickerDialog.show();
            }
        });

        chart = (BarChart) view.findViewById(R.id.barChart);
        chart.setDescription(null);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);


        ArrayList xVals = new ArrayList();

        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");

        ArrayList yVals1 = new ArrayList();
        ArrayList yVals2 = new ArrayList();


        return view;
    }

    private void setUpBarChart(){
        ArrayList<BarEntry> budgetExtries=new ArrayList<>();
        float k=1f;
        for (int i=0; i<budgets.size(); i++){
            budgetExtries.add(new BarEntry(k,Float.parseFloat(budgets.get(i)+"")));
            k++;
        }


        ArrayList<BarEntry> amountsExtries=new ArrayList<>();
        float j=1f;
        for (int i=0; i<amounts.size(); i++){
            try {
                amountsExtries.add(new BarEntry(j, Float.parseFloat(amounts.get(i) + "")));
            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
            j++;
        }


        BarDataSet set1, set2;
        set1 = new BarDataSet(budgetExtries, "Budget");
        set1.setColor(Color.BLUE);
        set2 = new BarDataSet(amountsExtries, "Expense");
        set2.setColor(Color.GREEN);

        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());
        chart.setData(data);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);


        //X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(expenses));
//Y-axis
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        chart.invalidate();

    }


   }
