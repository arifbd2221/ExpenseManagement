package com.countonit.user.swipetabpractice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.r0adkll.slidr.Slidr;

/**
 * Created by User on 8/29/2017.
 */

public class FeedBack extends AppCompatActivity {

    Button send;
    EditText msg;
    String[] addresses;

    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    Button button;
    Methods methods;

    int appTheme;
    int themeColor;
    int appColor;
    Constant constant;
    Vibrator vibrator;


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
        setContentView(R.layout.feedback);
        Slidr.attach(this);



        send=(Button) findViewById(R.id.send);
        msg=(EditText) findViewById(R.id.getFeedback);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("FeedBack");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( !msg.getText().toString().isEmpty())
                {
                    addresses=new String[]{"mohidulhoque216@yahoo.com"};

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, msg.getText().toString());
                    emailIntent.putExtra(Intent.EXTRA_EMAIL,addresses);
                    emailIntent.setType("text/plain");
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
                        finish();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(700);
                    Toast.makeText(getApplicationContext(),"You might have forget to fill the Comment Box",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                if (item.getItemId() == android.R.id.home){
                    onBackPressed();
                }


        return true;
    }
}
