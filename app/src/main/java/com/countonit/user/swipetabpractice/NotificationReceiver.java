package com.countonit.user.swipetabpractice;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.countonit.user.swipetabpractice.database.MainDB;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 6/28/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {


    final public static String ONE_TIME = "onetime";
    private Context context;
    private String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context=context;

        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
            //Make sure this intent has been sent by the one-time timer button.
            msgStr.append("One time Timer : ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context, NotificationView.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Today's Expense")
                .setContentText("Look at your cost")
                .setAutoCancel(true);


        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Expense Management Details:");


        notification.setStyle(generateReport(inboxStyle));


        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification.setSound(sound);

        notificationManager.notify(100, notification.build());

        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        context.startService(repeating_intent);

    }

    public void SetAlarm(Context context, Calendar calendar)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After 12 hours later
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }




    private NotificationCompat.InboxStyle generateReport(NotificationCompat.InboxStyle inboxStyle){

        MainDB mainDB = new MainDB(context);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String str=dateFormat.format(date);



        ArrayList<String> allCost = mainDB.getCostByDate(str);
        ArrayList<Integer> budgets = mainDB.getBudgetsByMonth(months[Calendar.getInstance().get(Calendar.MONTH)]);
        ArrayList<Integer> income = mainDB.getAllIncome(Calendar.getInstance().get(Calendar.MONTH)+1,Calendar.getInstance().get(Calendar.YEAR)+"");

        int sumOfCost=0;

        for (int i=0; i<allCost.size(); i++){
            try {
                sumOfCost = sumOfCost + Integer.parseInt(allCost.get(i));
            }catch (NumberFormatException nb){
                nb.printStackTrace();
            }
        }

        int sumOfBudget=0;

        for(int b : budgets){
            sumOfBudget=sumOfBudget+b;
        }

        int sumOfIncome=0;

        for (int in : income){
            sumOfIncome=sumOfIncome+in;
        }

        int rem=sumOfIncome-sumOfCost;


        String[] msg ={"Total Income Is "+sumOfIncome,"Total Budget Is "+sumOfBudget,"Total Cost Is "+sumOfCost,"Remaining Balance Is "+rem};


        for (String s : msg){
            inboxStyle.addLine(s);
        }


        return inboxStyle;

    }



}
