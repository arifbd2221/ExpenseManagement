package com.countonit.user.swipetabpractice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.util.ArrayList;

/**
 * Created by User on 11/11/2017.
 */

public class IncomeHistory extends Fragment {

    ListView listView;
    MainDB db;
    Button first;
    Custom_Adapter adapter2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.income_infos,container,false);
        db=new MainDB(getActivity());
        listView=(ListView) view.findViewById(R.id.income_info);
        first=(Button) view.findViewById(R.id.first);

        final YearMonthPickerDialog yearMonthPickerDialog=new YearMonthPickerDialog(getActivity(), new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                populateAdapter(month+1,year+"");
            }
        });


        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearMonthPickerDialog.show();
            }
        });


        return view;
    }

        public void populateAdapter(int position,String year){
            ArrayList<String> timeList = new ArrayList<>();
            ArrayList<String> dateList = new ArrayList<>();
            ArrayList<Integer> incomeList = new ArrayList<>();
            ArrayList<String> sourceList = new ArrayList<>();


            timeList =db.getAllIncomeTime(position,year);
            dateList =db.getAllIncomeDate(position,year);
            incomeList =db.getAllIncome(position,year);
            sourceList =db.getAllIncomeSource(position,year);

            db.close();

            adapter2=new Custom_Adapter(getContext(),incomeList,dateList,sourceList,timeList);
            listView.setAdapter(adapter2);
            registerForContextMenu(listView);

        }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select The Action");
        menu.add(0,v.getId(),0,"Edit");
        menu.add(0,v.getId(),0,"Delete");


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if (item.getTitle() == "Edit"){
            Toast.makeText(getActivity(),"This Feature Is Under Progress...",Toast.LENGTH_SHORT).show();
        }
        else if (item.getTitle() == "Delete"){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            if (adapter2.remove(db,index) ){
                Toast.makeText(getContext(),"Successfully Removed",Toast.LENGTH_SHORT).show();
                adapter2.notifyDataSetChanged();
            }

        }


        return true;
    }
}
