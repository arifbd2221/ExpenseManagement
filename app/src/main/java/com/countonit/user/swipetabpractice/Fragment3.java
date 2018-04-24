package com.countonit.user.swipetabpractice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/22/2017.
 */

public class Fragment3 extends Fragment {


    float[] amounts;
    String[] Expenses;
    Button first;
    PieChart chart;
    MainDB db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.layout3,container,false);

        final Spinner mCategory=(Spinner) v.findViewById(R.id.catSpinner);
        first=(Button) v.findViewById(R.id.first);
        chart=(PieChart) v.findViewById(R.id.chart);

        db=new MainDB(getActivity());


        //final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        final YearMonthPickerDialog yearMonthPickerDialog=new YearMonthPickerDialog(getActivity(), new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(final int year, int month) {

                //Toast.makeText(getActivity(),year+","+month,Toast.LENGTH_SHORT).show();


                final String m=getResources().getStringArray(R.array.month)[month+1]+"";

                ArrayAdapter<String> adapter=null;
                ArrayList<String> cat=new ArrayList<String>();
                String[] str=null;
                int l=1;
                //Toast.makeText(getActivity(),months.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();

                //Toast.makeText(getActivity(),db.getAllExpenseByCatMonthYear("Color",m,year).length+"",Toast.LENGTH_SHORT).show();
                    cat=db.getCategoryByCurrentDate(m,year+"");

                    str=new String[cat.size()+1];
                    str[0]="pick category";

                    for(String s : cat){
                        str[l]=s.substring(0,1).toUpperCase()+s.substring(1);
                        l++;

                    }

                    adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,str);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mCategory.setAdapter(adapter);




                    mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position !=0) {
                            Expenses=db.getAllExpenseByDate(mCategory.getSelectedItem().toString(),m,year+"");
                            amounts=db.getAllCostByDate(mCategory.getSelectedItem().toString(),m,year+"");

                            //setDataFromDataBase(months.getSelectedItem().toString());
                            setupPieChart();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

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

    public void setupPieChart(){

        List<PieEntry> entry=new ArrayList<>();

        for (int i=0; i<Expenses.length; i++){
            entry.add(new PieEntry(amounts[i],Expenses[i]));
        }
        PieDataSet dataSet=new PieDataSet(entry,"Expenses Figure");
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        PieData data=new PieData(dataSet);
        chart.setData(data);
        chart.animateY(700);
        chart.invalidate();

    }

    public void setDataFromDataBase(String month){

        ArrayList<String> arrayList=db.getAllExpense(month);
        ArrayList<String> arrayList2=db.getAllAmount(month);
        Expenses=new String[arrayList.size()];
        amounts=new float[arrayList2.size()];
        int i=0;
        for (String string : arrayList){
            Expenses[i]=string;
            //Toast.makeText(getActivity(),string,Toast.LENGTH_SHORT).show();
            i++;
        }

        int j=0;
        for (String str : arrayList2){
            amounts[j]=Float.parseFloat(str);
            j++;
        }
    }

}
