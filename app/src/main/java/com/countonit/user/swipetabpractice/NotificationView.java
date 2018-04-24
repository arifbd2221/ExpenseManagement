package com.countonit.user.swipetabpractice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.countonit.user.swipetabpractice.database.MainDB;

import java.util.ArrayList;
import java.util.Calendar;

public class NotificationView extends Service {


    MainDB mainDB;
    Context context;

    public NotificationView(Context context) {
        this.context = context;
    }

    public NotificationView(){

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);




        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
