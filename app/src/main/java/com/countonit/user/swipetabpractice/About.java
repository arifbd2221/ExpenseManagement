package com.countonit.user.swipetabpractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.countonit.user.swipetabpractice.colorpicker.Constant;
import com.countonit.user.swipetabpractice.colorpicker.Methods;
import com.r0adkll.slidr.Slidr;

/**
 * Created by User on 7/23/2017.
 */

public class About extends AppCompatActivity{


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
        //overridePendingTransition(0,R.anim.fadin);

        setContentView(R.layout.about);
        Slidr.attach(this);


        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_bar);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar5);
        collapsingToolbarLayout.setTitle("About Developer");
        toolbar.setTitle("About");


    }

}
