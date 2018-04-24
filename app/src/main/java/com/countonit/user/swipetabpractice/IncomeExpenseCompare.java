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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by User on 9/7/2017.
 */

public class IncomeExpenseCompare extends Fragment{

    float[] incomes;
    String[] incomeDates;
    String[] expense;

    PieChart incomeChart,expenseChart;
    Button first;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.compare,container,false);

        incomeChart=(PieChart) v.findViewById(R.id.forIncome);
        expenseChart=(PieChart) v.findViewById(R.id.expenses);
        //months=(Spinner) v.findViewById(R.id.months);
        //years=(Spinner) v.findViewById(R.id.year);
        first=(Button) v.findViewById(R.id.first);

        incomeChart.setBackgroundColor(Color.WHITE);

        incomeChart.setDrawHoleEnabled(true);
        incomeChart.setHoleColor(Color.WHITE);

        incomeChart.setTransparentCircleColor(Color.WHITE);
        incomeChart.setTransparentCircleAlpha(110);

        incomeChart.setHoleRadius(58f);
        incomeChart.setTransparentCircleRadius(61f);

        incomeChart.setDrawCenterText(true);

        incomeChart.setRotationEnabled(false);
        incomeChart.setHighlightPerTapEnabled(true);

        incomeChart.setMaxAngle(180f); // HALF CHART
        incomeChart.setRotationAngle(180f);
        incomeChart.setCenterTextOffset(0, -20);

        final YearMonthPickerDialog yearMonthPickerDialog=new YearMonthPickerDialog(getActivity(), new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                setDataToExpenseChart(month+1);
                setDataToIncomeChart(month+1,year);
            }
        });

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearMonthPickerDialog.show();
            }
        });


        return v;
    }

    private void setDataToExpenseChart(int position) {

        MainDB mainDB=new MainDB(getActivity());

        float[] cost;
        String[] expense=null;
        int i=0;

        if (position != 0) {
            cost = new float[mainDB.getAllAmount(getResources().getStringArray(R.array.month)[position]).size() + 1];
            expense = new String[cost.length];

            ArrayList<String> amountList = new ArrayList<>();
            amountList = mainDB.getAllAmount(getResources().getStringArray(R.array.month)[position]);
            ListIterator listIterator = amountList.listIterator();


            while (listIterator.hasNext()) {
                cost[i] = Float.parseFloat(listIterator.next().toString());
                i++;
            }
            i = 0;

            amountList = mainDB.getAllExpense(getResources().getStringArray(R.array.month)[position]);

            listIterator = amountList.listIterator();

            while (listIterator.hasNext()) {
                expense[i] = listIterator.next().toString();
                i++;
            }


            List<PieEntry> entry = new ArrayList<>();

            for (int k = 0; k < expense.length; k++) {
                entry.add(new PieEntry(cost[k], expense[k]));
            }
            PieDataSet dataSet = new PieDataSet(entry, "Expenses Figure");
            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);
            dataSet.setColors(getColors());


            Legend l = expenseChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);
            l.setWordWrapEnabled(true);

            PieData data = new PieData(dataSet);
            expenseChart.setData(data);
            expenseChart.animateY(700);
            expenseChart.invalidate();


        }

    }


    private void setDataToIncomeChart(int month,int year) {

        MainDB database = new MainDB(getActivity());

            ArrayList<String> sources=database.getAllIncomeSource();
            incomes=new float[sources.size()];
            incomeDates = new String[sources.size()];

            for(int i=0; i<sources.size(); i++){
                incomes[i]=database.getAllIncomeBySource(sources.get(i),month,year);
            }



        if (month != 0) {



                for(int i=0; i<sources.size(); i++){
                    incomeDates[i]=sources.get(i);
                }



            setIncomePieChart();

        }

    }

    private void setIncomePieChart() {

        List<PieEntry> entry=new ArrayList<>();

        for (int i=0; i<incomes.length; i++){
            entry.add(new PieEntry(incomes[i],incomeDates[i]));
        }
        PieDataSet dataSet=new PieDataSet(entry,"Income Records");


        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(getColors());
        dataSet.setValueLinePart1OffsetPercentage(90.f);
        dataSet.setValueLinePart1Length(.2f);
        dataSet.setValueLinePart2Length(.4f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        Legend l = incomeChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        PieData data=new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        incomeChart.setData(data);
        incomeChart.setEntryLabelColor(Color.BLACK);
        incomeChart.animateY(700);
        incomeChart.invalidate();


    }


    private ArrayList<Integer> getColors() {

        //int stacksize = incomes.length;

        // have as many colors as stack-values per entry
        /*int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            try {
                colors[i] = ColorTemplate.MATERIAL_COLORS[i];
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }*/



        ArrayList<Integer> colors = new ArrayList<Integer>();

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

        colors.add(ColorTemplate.getHoloBlue());







        return colors;
    }

}
