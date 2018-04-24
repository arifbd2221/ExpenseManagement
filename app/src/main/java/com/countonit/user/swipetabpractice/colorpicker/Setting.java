package com.countonit.user.swipetabpractice.colorpicker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.countonit.user.swipetabpractice.R;
import com.countonit.user.swipetabpractice.RootScreen;
import com.r0adkll.slidr.Slidr;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

/**
 * Created by User on 9/4/2017.
 */

public class Setting extends AppCompatActivity {

    SharedPreferences app_preferences;
    SharedPreferences.Editor editor;
    Button button;
    Methods methods;

    int appTheme;
    int themeColor;
    int appColor;
    public Spinner currency;
    Constant constant;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        app_preferences = getSharedPreferences("com.countonit.user.swipetabpractice_preferences",MODE_PRIVATE);
        appColor = app_preferences.getInt("color", 1);
        appTheme = app_preferences.getInt("theme", 1);
        themeColor = appColor;
        constant.color = appColor;

        if (themeColor == 1){
            setTheme(Constant.theme);
        }else if (appTheme == 1){
            setTheme(Constant.theme);
        }else{
            setTheme(appTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        //Slidr.attach(this);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Constant.color);
        currency=(Spinner) findViewById(R.id.currency);

        ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.currencySymbols1));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        currency.setAdapter(adapter);


        currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sp=getBaseContext().getSharedPreferences("CurrencyPosition",MODE_PRIVATE);
                SharedPreferences.Editor edit=sp.edit();
                if (position == 0)
                edit.putInt("position",position+1);
                else
                    edit.putInt("position",position);
                edit.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        methods = new Methods();
        button = (Button) findViewById(R.id.button_color);
        app_preferences = getSharedPreferences("com.countonit.user.swipetabpractice_preferences",MODE_PRIVATE);
        editor = app_preferences.edit();

        colorize();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ColorChooserDialog dialog=new ColorChooserDialog(Setting.this);
                dialog.setTitle("Select");
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        colorize();
                        Constant.color = color;

                        methods.setColorTheme();
                        editor.putInt("color", color);
                        editor.putInt("theme",Constant.theme);
                        editor.commit();

                        Intent intent = new Intent(Setting.this, RootScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

                dialog.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this,RootScreen.class));
                finish();
                break;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void colorize(){
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);

        d.getPaint().setStyle(Paint.Style.FILL);
        d.getPaint().setColor(Constant.color);

        button.setBackground(d);
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
