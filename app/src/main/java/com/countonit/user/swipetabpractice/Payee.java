package com.countonit.user.swipetabpractice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.countonit.user.swipetabpractice.database.MainDB;
import com.r0adkll.slidr.Slidr;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by User on 7/19/2017.
 */

public class Payee extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextInputLayout textInputLayoutName,textInputLayoutAmount;
    RelativeLayout getDate;
    EditText pAmount;
    TextView dateRange;
    EditText pName,pEmail;
    Vibrator vibrator;
    Animation shake;
    String date=null;
    Button save,phoneNumber;
    MainDB db;

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

        app_preferences = getSharedPreferences("com.countonit.user.swipetabpractice_preferences",MODE_PRIVATE);
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
        setContentView(R.layout.payee);
        //Slidr.attach(this);

        db=new MainDB(this);
        textInputLayoutName=(TextInputLayout) findViewById(R.id.text_input_name);
        textInputLayoutAmount=(TextInputLayout) findViewById(R.id.text_input_amount);

        getDate=findViewById(R.id.date);

        pAmount=(EditText) findViewById(R.id.payee_amount);
        pName=(EditText) findViewById(R.id.payee_name);
        pEmail=(EditText) findViewById(R.id.payee_email);
        phoneNumber=(Button) findViewById(R.id.phoneNumber);

        vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        dateRange=(TextView) findViewById(R.id.payee_date);
        save=(Button) findViewById(R.id.save);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Payee Information");
        //toolbar.setForegroundGravity(Toolbar.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog=DatePickerDialog.newInstance(Payee.this,Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.setTitle("Select Your Date");
                dialog.show(getFragmentManager(),"Date");
            }
        });



        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit();
                if (date != null && !pName.getText().toString().isEmpty() && !pAmount.getText().toString().isEmpty()) {
                    String name = pName.getText().toString();
                    String amn = pAmount.getText().toString();
                    String emal = pEmail.getText().toString();
                    String phone = phoneNumber.getText().toString();
                    if(phone.equals("Pick A Contact Number")){
                        phone="";
                    }
                    db.insertPayeeInfo(name, amn, emal, date, phone);
                }


                pName.setText("");
                pAmount.setText("");
                pEmail.setText("");
            }

        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor cursor =  managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                //contactName.setText(name);
                phoneNumber.setText(number);
                //contactEmail.setText(email);
            }
        }
    }




    private void submit() {

        boolean isValid=true;
        if ( !check(pName,textInputLayoutName)){
            pAmount.setAnimation(shake);
            vibrator.vibrate(300);
            textInputLayoutName.setError("Please Provide A Name");
            isValid=false;
        }
        if ( !check(pAmount,textInputLayoutAmount)){
            pAmount.setAnimation(shake);
            vibrator.vibrate(300);
            textInputLayoutAmount.setError("Please Provide Valid Input");
            isValid=false;
        }


        if (isValid){
            Toast.makeText(this,pName.getText().toString()+" "+phoneNumber.getText().toString()+" "+pAmount.getText().toString(),Toast.LENGTH_SHORT).show();
        }

    }

    public boolean check(View view,TextInputLayout textInputLayout){
        EditText edit=(EditText) view;
        if (edit.getText().toString().isEmpty()){
            return false;
        }
        textInputLayout.setErrorEnabled(false);
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date=year+"-"+monthOfYear+"-"+dayOfMonth;

        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(year);

        if (monthOfYear <10){
            stringBuffer.append("/0"+monthOfYear);
        }
        else
            stringBuffer.append("/"+monthOfYear);


        if (dayOfMonth<10){
            stringBuffer.append("/0"+dayOfMonth);
        }
        else
            stringBuffer.append("/"+dayOfMonth);

        dateRange.setText(stringBuffer.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.payee_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.next:
                Intent intent = new Intent(this, PayeeList.class);
                startActivity(intent);

                break;
            case android.R.id.home:
                startActivity(new Intent(this,RootScreen.class));
                finish();
                break;
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

   /*     public void sendmail(String mail,String msg){
            String[] addresses=new String[]{mail};

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Statement");
            emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
            emailIntent.putExtra(Intent.EXTRA_EMAIL,addresses);
            emailIntent.setType("text/plain");
            try {
                startActivity(Intent.createChooser(emailIntent, "Send Statement"));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
 }*/


}
