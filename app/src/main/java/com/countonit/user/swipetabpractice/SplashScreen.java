package com.countonit.user.swipetabpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by User on 6/22/2017.
 */

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharedPreferences,sharedPreferences2;

    //private static int SPLASH_TIME_OUT = 10000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences2=getSharedPreferences("OnBoard",MODE_PRIVATE);
        sharedPreferences =getSharedPreferences("Loggin_Credential",MODE_PRIVATE);

        if ( sharedPreferences2.getBoolean("first",true)){
            SharedPreferences.Editor editor=sharedPreferences2.edit();
            editor.putBoolean("first",false);
            editor.commit();
            startActivity(new Intent(SplashScreen.this,Help.class));
            finish();
        }
        else if (sharedPreferences.getBoolean("checker", false)) {
            startActivity(new Intent(SplashScreen.this,Loggin_Activity.class));
            finish();
        } else {
            //Toast.makeText(this,""+sharedPreferences.getBoolean("checker",false),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SplashScreen.this,RootScreen.class));
            finish();
        }


    }

       /*


        setContentView(R.layout.loggin_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen.this,RootScreen.class);
                startActivity(i);
                finish();
            }
        },SPLASH_TIME_OUT);
    }*/


}