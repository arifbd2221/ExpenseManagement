package com.countonit.user.swipetabpractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by User on 7/19/2017.
 */

public class PayeeList extends AppCompatActivity implements View.OnLongClickListener,SearchView.OnQueryTextListener{
    boolean is_in_action_mode=false;
    TextView counterText;

    ArrayList<PP_Info> pp_infosList;
    ArrayList<PP_Info> selectionList=new ArrayList<>();
    RecyclerView recyclerView;
    Recycler_Adapter adapter;
    Toolbar toolbar;
    int counter=0;
    PP_Info pInfo;

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

        super.onCreate(savedInstanceState);
        //overridePendingTransition(0,R.anim.fadin);
        setContentView(R.layout.payee_list);
        Slidr.attach(this);

        toolbar=(Toolbar) findViewById(R.id.rtoolbar);
        counterText=(TextView) toolbar.findViewById(R.id.counter);
        counterText.setVisibility(View.GONE);

        toolbar.setTitle("Payees List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        recyclerView=(RecyclerView) findViewById(R.id.recycler);
        pp_infosList=new ArrayList<>();
        MainDB db=new MainDB(this);

        ListIterator<String> iterator=db.getName().listIterator();
        ListIterator<String> iterator1=db.getAmount().listIterator();
        ListIterator<String> iterator2=db.getEmail().listIterator();
        ListIterator<String> iterator3=db.getDate().listIterator();
        ListIterator<String> iterator4=db.getPhoneNumbers(PayeeDataBase.Table_Name).listIterator();

        while (iterator.hasNext() && iterator1.hasNext() && iterator2.hasNext() && iterator3.hasNext() && iterator4.hasNext()){
            pInfo=new PP_Info();
            pInfo.setNames(iterator.next());
            pInfo.setAmounts(iterator1.next());
            pInfo.setEmails(iterator2.next());
            pInfo.setDates(iterator3.next());
            pInfo.setPhone(iterator4.next());
            pp_infosList.add(pInfo);
        }
        /*for(int y=0; y<4; y++)
        Toast.makeText(this,pp_infosList.get(y).getNames(),Toast.LENGTH_SHORT).show();*/

        adapter=new Recycler_Adapter(this,pp_infosList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.delete){
            is_in_action_mode=false;
            adapter.updateAdapter(selectionList);
            clearActionMode();
        }
        return true;
    }


    public void clearActionMode(){
        is_in_action_mode=false;
        toolbar.getMenu().clear();
        toolbar.setTitle("Payee Infos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counterText.setVisibility(View.GONE);
        counterText.setText("0 item selected");
        counter=0;
        selectionList.clear();
    }


    @Override
    public boolean onLongClick(View v) {

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.pp_menu_context_mode);
        counterText.setVisibility(View.VISIBLE);
        is_in_action_mode=true;
        adapter.notifyDataSetChanged();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }


    public void prepareCheckBox(View view,int position){

        if (((CheckBox) view).isChecked()){
            selectionList.add(pp_infosList.get(position));
            counter++;
            updateCounter(counter);
        }
        else {
            selectionList.remove(position);
            counter--;
            updateCounter(counter);
        }
    }


    public void updateCounter(int position){

        if (position == 0){
            counterText.setText("0 item selected");
        }
        else {
            counterText.setText(counter+" items selected");
            //counter=0;
        }

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText=newText.toLowerCase();

        ArrayList<PP_Info> pp_infos=new ArrayList<>();

        for (PP_Info p : pp_infosList){
            if (p.getNames().toLowerCase().contains(newText) || p.getDates().contains(newText)){
                pp_infos.add(p);
            }
        }

        adapter.filterAdapter(pp_infos);

        return true;
    }
}
