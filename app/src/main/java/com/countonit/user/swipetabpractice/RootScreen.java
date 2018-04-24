package com.countonit.user.swipetabpractice;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.adapter.ExpandableRecycler;
import com.countonit.user.swipetabpractice.adapterViews.Category;
import com.countonit.user.swipetabpractice.adapterViews.Expenses;
import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.countonit.user.swipetabpractice.colorpicker.Setting;

import com.github.fernandodev.easyratingdialog.library.EasyRatingDialog;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by User on 6/23/2017.
 */

public class RootScreen extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {



    SharedPreferences app_preferences,sp;
    SharedPreferences.Editor editor;

    RecyclerView recyclerView;
    ExpandableRecycler adapter;

    Button button;
    Methods methods;

    int appTheme;
    int themeColor;
    int appColor;
    Constant constant;

    BottomNavigationViewEx bottomToolbar;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    TextView month_date;
    MainDB mainDB;

    EasyRatingDialog easyRatingDialog;

    //ImageButton gotoExpense,addBalance,rate;

    String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    String[] days={"Sunday","Monday","Tuesday","Wednessday","Thrusday","Friday","Saturday"};
    DonutProgress donut;
    TextView totalBudget,balance,spent;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.root_activity);

        mainDB=new MainDB(this);

        bottomToolbar= findViewById(R.id.bottomNavViewbar);

        enableNavigation(getApplicationContext(),bottomToolbar);


        easyRatingDialog=new EasyRatingDialog(this);


        sp=getBaseContext().getSharedPreferences("CurrencyPosition",MODE_PRIVATE);



        registerFonts();

        totalBudget= findViewById(R.id.hBudget);
        balance= findViewById(R.id.hIncome);
        spent= findViewById(R.id.hExpense);
        donut=findViewById(R.id.donut);
        progressBar= findViewById(R.id.progressbar);
        progressBar.setBackgroundColor(Color.GREEN);

        recyclerView= findViewById(R.id.recycler);
        init();

        int tBal=0;
        int tBdgt=new MainDB(this).getTotalBudget();
        int tCost=new MainDB(this).getTotalCost();
        if (fileExistance("Total_Expense.txt")){
            tBal=Integer.parseInt(new Storage(this).loadData());
        }else {
            Toast.makeText(this,"Please Add Your Balance First",Toast.LENGTH_SHORT).show();
        }

        float progress=(Float.parseFloat(tCost+"") / Float.parseFloat(tBal+""))*100;
        float prgs2=(Float.parseFloat(tCost+"") / Float.parseFloat(tBdgt+""))*100;
        spent.setText(tCost+"");
        donut.setDonut_progress(""+(int) progress);
        progressBar.setProgress((int) prgs2);
        donut.setText("SAVING "+(tBal-tCost));
        donut.setTextSize(20f);
        totalBudget.setText(tBdgt+"");
        //Toast.makeText(this,""+tBdgt,Toast.LENGTH_SHORT).show();

        if (fileExistance("Total_Expense.txt")){

            Calendar calendar=Calendar.getInstance();
            ArrayList<Integer> incomes = mainDB.getAllIncome(calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.YEAR)+"");

            int sum=0;
            for (int s : incomes){
                sum=sum+s;
            }
            balance.setText(getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]+" "+sum);
        }

        else
            balance.setText("0 ");
            setUpToolbar();
            setUpNavigation();


        if ( fileExistance("Total_Expense.txt") ){



            Repeater repeater=new Repeater(this);
            repeater.recordMonth();


        }

    }

    private void registerFonts() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setUpToolbar() {
        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("Personal Financial Dairy");

    }


    private void init() {



        new loadRecyclerData().execute();


    }

    private List<Category> loadCateExpense() {


        List<String> cat=new ArrayList<>();
        cat=mainDB.getAllCategory();

        List<String> expens=new ArrayList<>();

        List<Category> categories=new ArrayList<>();

        for (int i=0; i<cat.size(); i++) {
            expens=mainDB.getExpenseTitleByCategory(cat.get(i));
            List<Expenses> list=new ArrayList<>();
            for (int j = 0; j < expens.size(); j++) {
                list.add(new Expenses(expens.get(j)));
            }
            categories.add(new Category(cat.get(i),list));
        }

        return categories;

    }


    public void setUpNavigation(){
        NavigationView navigation= findViewById(R.id.navigation);
        View v=navigation.getHeaderView(0);
        month_date= v.findViewById(R.id.month_date);
        setMonthDate();
        drawerLayout= findViewById(R.id.drawer);
        ActionBarDrawerToggle toggler=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        navigation.setNavigationItemSelectedListener(this);
        drawerLayout.setDrawerListener(toggler);
        toggler.syncState();
    }

    private void setMonthDate() {

        Calendar cal=Calendar.getInstance();
        String month=months[cal.get(Calendar.MONTH)];
        int date=cal.get(Calendar.DAY_OF_WEEK);
        //Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
        month_date.setText(month+" , "+days[date-1]);
        month_date.setMaxLines(1);
    }

    public boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.custom_eport:
               startActivity(new Intent(this,Custom_Report.class));
                break;


            case R.id.payee:
                Intent payeIntent=new Intent(this,Payee.class);
                startActivity(payeIntent);
                finish();
                break;


            case R.id.payer:
                Intent payerIntent=new Intent(this,Payer.class);
                startActivity(payerIntent);
                finish();
                break;

            case R.id.daily_report:
                Intent drepot=new Intent(this,TodayReport.class);
                startActivity(drepot);
                break;
            case R.id.create_expense:
                startActivity(new Intent(this,Create_Expense.class));
                finish();
                break;

            case  R.id.about:
                startActivity(new Intent(this,About.class));
                break;

            case R.id.tools:
                startActivity(new Intent(this,Tools.class));
                break;

            case R.id.help:
                startActivity(new Intent(this,Help.class));
                break;


            case R.id.setting:

                startActivity(new Intent(this,Setting.class));
                finish();

                break;

            case R.id.report:

                try{
                    showReports();
                }catch (NullPointerException np){
                    np.printStackTrace();
                }

                break;


            /*case R.id.priv_policy:
                WebView webView=new WebView(this);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl("https://drive.google.com/open?id=131nPqiLOCznqlLHfH3ofOKE3rQBP8nO626juq7CXjJk");

                break;*/

            case R.id.feedback:
                startActivity(new Intent(this,FeedBack.class));
                break;

            case R.id.egallery:

                startActivity(new Intent(this,ExpenseGallery.class));

                break;

            case R.id.Privacy:

                startActivity(new Intent(this,Privacy.class));

                break;

        }


        drawerLayout.closeDrawers();
        return true;
    }


    private void showReports() {

        if (fileExistance("Total_Expense.txt")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }
        else {
            Toast.makeText(this,"Please Provide Your Income First",Toast.LENGTH_SHORT).show();
        }

    }

/*    @Override
    protected void onResume() {
        super.onResume();
        init();


        int tBal=0;
        int tBdgt=mainDB.getTotalBudget();
        int tCost=mainDB.getTotalCost();
        if (fileExistance("Total_Expense.txt")){
            tBal=Integer.parseInt(new Storage(this).loadData());
        }else {
            Toast.makeText(this,"Please Provide Your Balance First",Toast.LENGTH_SHORT).show();
        }

        float progress=(Float.parseFloat(tCost+"") / Float.parseFloat(tBal+""))*100;
        float prgs2=(Float.parseFloat(tCost+"") / Float.parseFloat(tBdgt+""))*100;
        spent.setText(tCost+" "+getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]);
        donut.setDonut_progress(""+(int) progress);
        progressBar.setProgress((int) prgs2);
        donut.setText("SAVING "+(tBal-tCost));
        donut.setTextSize(20f);
        totalBudget.setText(tBdgt+" "+getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]);

        if (fileExistance("Total_Expense.txt"))
            balance.setText(getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]+" "+new Storage(this).loadData());
        else
            balance.setText("0 "+getResources().getStringArray(R.array.symbols1)[sp.getInt("position",1)-1]);


    }*/

    @Override
    protected void onStart() {
        super.onStart();
        easyRatingDialog.onStart();
    }

    public void onClickShowAnyway(View view) {
        easyRatingDialog.showAnyway();
    }

   /* public void onClickRateNow(View view) {
        easyRatingDialog.rateNow();
    }

    public void onClickNeverReminder(View view) {
        easyRatingDialog.neverReminder();
    }

    public EasyRatingDialog getEasyRatingDialog() {
        return easyRatingDialog;
    }*/


    public boolean endOfMonth(){

        /*Calendar calendar=Calendar.getInstance();
        int last=calendar.get(Calendar.DAY_OF_MONTH);
        //int month=calendar.get(Calendar.MONTH);lastDate[month]

        if (last == 1){
            return true;
        }*/

        return true;
    }




    public void enableNavigation(final Context context,BottomNavigationViewEx bottomNavigationViewEx){
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.add_expense:

                        startActivity(new Intent(getApplicationContext(),CreateExpenseInput.class));
                        finish();
                        break;

                    case R.id.balance:
                        //addIncome();
                        startActivity(new Intent(getApplicationContext(),Take_Income_Info.class));
                        finish();
                        break;

                    case R.id.love:
                        easyRatingDialog.onStart();
                        easyRatingDialog.showAnyway();
                        break;

                }


                return true;
            }
        });
    }

    boolean isAvailable(){
        File file=new File("/data/data/com.countonit.user.swipetabpractice/shared_prefs/MonthEndDetector.xml");

        if (file.exists()){
            return true;
        }
        return false;
    }


     class loadRecyclerData extends AsyncTask<Integer,Integer,Boolean>{
         List<Category> categories;
         ProgressDialog progDialog=new ProgressDialog(RootScreen.this);

         @Override
         protected void onPreExecute() {
             progDialog.show();
         }

         @Override
        protected Boolean doInBackground(Integer... params) {
            categories=loadCateExpense();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progDialog.isShowing())
                progDialog.dismiss();

            adapter = new ExpandableRecycler(RootScreen.this, categories);
            RecyclerView.LayoutManager manager=new LinearLayoutManager(RootScreen.this);
            manager.setAutoMeasureEnabled(true);

            recyclerView.setLayoutManager(manager);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);

        }
    }



}
