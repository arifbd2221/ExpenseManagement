package com.countonit.user.swipetabpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 11/11/2017.
 */

public class Take_Income_Info extends AppCompatActivity{


    EditText incomeSource,incomeAmount;
    Spinner inSource;
    Button save;
    Toolbar toolbar;

    ExpandableTextView expTv1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_balance_info);
        //Slidr.attach(this);

        init();



    }

    private void init() {



        incomeSource=(EditText) findViewById(R.id.income_source);
        incomeAmount=(EditText) findViewById(R.id.income_amount);
        expTv1 = (ExpandableTextView)findViewById(R.id.root)
                .findViewById(R.id.expand_text_view);
        //incomeNote=(EditText) findViewById(R.id.note);
        save=(Button) findViewById(R.id.save);
        inSource=(Spinner) findViewById(R.id.in_Source);


        expTv1.setText(getString(R.string.BalanceInfo));

        String[] sources=new MainDB(this).getAllIncomeSource().toArray(new String[0]);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sources);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inSource.setAdapter(adapter);


        setUpToolbar();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
            }
        });

    }

    private void setUpToolbar() {

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Income Information");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void addIncome() {

            String  incom=incomeAmount.getText().toString();
            String  source=incomeSource.getText().toString();
            //String  note=incomeNote.getText().toString();

        if (incom.isEmpty()){
            Toast.makeText(getApplicationContext(),"You Must Provide Both Income Source & Income Amount",Toast.LENGTH_LONG).show();
        }
        else if (source.isEmpty() && inSource.getSelectedItem().equals("Select Income Source")){
            Toast.makeText(getApplicationContext(),"You Must Provide Both Income Source & Income Amount",Toast.LENGTH_LONG).show();
        }
        else {

            Storage storage=new Storage(this);
            MainDB mainDB=new MainDB(getApplicationContext());
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat dateFormat2 = new SimpleDateFormat("HH:mm a");
            Date d=new Date();
            if (source.isEmpty()){
                mainDB.insertIncome(Integer.parseInt(incom),dateFormat1.format(d), Calendar.getInstance().get(Calendar.MONTH)+1,inSource.getSelectedItem().toString(),"note"
                        ,dateFormat2.format(d)
            );
                incomeAmount.setText("");
            }
            else {
                mainDB.insertIncome(Integer.parseInt(incom),dateFormat1.format(d), Calendar.getInstance().get(Calendar.MONTH)+1,source,"note"
                        ,dateFormat2.format(d)

                );
            mainDB.close();

                incomeAmount.setText("");
                incomeSource.setText("");
            }

            if (fileExistance("Total_Expense.txt")){
                String loadAmn=storage.loadData();
                String temp=String.valueOf(Integer.parseInt(incom)+Integer.parseInt(loadAmn));
                incom=temp;
            }

            storage.saveData(incom);
            Toast.makeText(this,"Balance is "+incom,Toast.LENGTH_SHORT).show();
        }

    }

    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
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
