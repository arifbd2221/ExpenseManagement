package com.countonit.user.swipetabpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

/**
 * Created by User on 11/29/2017.
 */

public class CreateExpenseInput extends AppCompatActivity {


    //Integer[] icons={R.drawable.bus,R.drawable.grocery,R.drawable.game,R.drawable.fitness,R.drawable.excercise,R.drawable.aeroplane,R.drawable.fish,R.drawable.dogfood,R.drawable.clinic};
    Toolbar toolbar;
    SharedPreferences.Editor editor;

    ExpandableTextView expandableTextView;

    MainDB mainDB;
    Spinner spinner;
    EditText exp,ct,bdg;
    Button add;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_expense_input);
        //Slidr.attach(this);

        /*sharedPreferences=getSharedPreferences("CatIcon",MODE_PRIVATE);
        editor=sharedPreferences.edit();*/

        init();


        Intent intent=getIntent();
        if (intent != null) {
            try {
                exp.setText(intent.getStringExtra("expName"));
            } catch (NullPointerException np) {
                np.printStackTrace();
            }
        }

    }

   /* private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("754DB6521943676637AE86202C5ACE52")
                .build();
        //mBannerAd.setAdUnitId(getResources().getString(R.string.ad_id_banner));
        mBannerAd.loadAd(adRequest);

    }*/


    private void init() {

        int i=1;

        mainDB=new MainDB(this);
        spinner=(Spinner) findViewById(R.id.spinner);
        //final Spinner catIcon=(Spinner) findViewById(R.id.catIcon);
        exp=(EditText) findViewById(R.id.editext);
        bdg=(EditText) findViewById(R.id.budget);
        ct=(EditText) findViewById(R.id.category_name);
        add=(Button) findViewById(R.id.add_expense);
        toolbar=(Toolbar) findViewById(R.id.toolbar);

        expandableTextView = (ExpandableTextView) findViewById(R.id.root)
                .findViewById(R.id.expand_text_view);
        expandableTextView.setText(getString(R.string.ExpenseCreation));

        toolbar.setTitle("Add Expense");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CoordinatorLayout coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinator);

        //catIcon.setAdapter(new SpinnerAdapter(this,icons));

        MainDB db=new MainDB(this);
        ArrayList<String> cat=new ArrayList<>();
        cat=db.getAllCategory();
        String[] str=new String[cat.size()+1];
        str[0]="Pick A Category";
        for (String st : cat){
            str[i]=st.substring(0,1).toUpperCase()+st.substring(1);
            i++;
        }
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,str);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cate=ct.getText().toString();
                if (exp.getText().toString().isEmpty() || exp.getText().toString().equals(" ") || exp.getText().toString().equals("  ")){
                    exp.setError("Expense Name Needed");
                    Snackbar.make(coordinatorLayout,"Please Enter Expense Name, Category Name or Pick One",Snackbar.LENGTH_SHORT).show();
                }else if (cate.isEmpty() && !spinner.getSelectedItem().toString().equals("Pick Category")){
                    int bdgt=Integer.parseInt(bdg.getText().toString());
                    //adapter.addItem(expense,category.getText().toString(),bdgt);
                    mainDB.createCategoryExpense(spinner.getSelectedItem().toString(),exp.getText().toString(),bdgt);
                    ct.setText("");
                    exp.setText("");
                    Toast.makeText(getApplicationContext(),"Expense Successfully created",Toast.LENGTH_SHORT).show();
                    /*editor.putInt(spinner.getSelectedItem().toString(),catIcon.getSelectedItemPosition());
                    editor.commit();
                    mainDB.close();*/
                }
                else if(cate.isEmpty() && spinner.getSelectedItem().toString().equals("Pick Category")){
                    ct.setError("Category Name Needed");
                    Snackbar.make(coordinatorLayout,"Please Enter Category Name or Pick One",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    int bdgt=Integer.parseInt(bdg.getText().toString());
                    mainDB.createCategoryExpense(cate,exp.getText().toString(),bdgt);

                    ct.setText("");
                    exp.setText("");

                    Toast.makeText(getApplicationContext(),"Expense Successfully created",Toast.LENGTH_SHORT).show();
                    /*editor.putInt(cate,catIcon.getSelectedItemPosition());
                    editor.commit();
                    mainDB.close();*/
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            startActivity(new Intent(this,RootScreen.class));
            finish();
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
