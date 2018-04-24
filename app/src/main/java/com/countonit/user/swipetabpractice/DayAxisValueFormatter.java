package com.countonit.user.swipetabpractice;

import android.content.Context;

import com.countonit.user.swipetabpractice.database.MainDB;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 7/24/2017.
 */

public class DayAxisValueFormatter implements IAxisValueFormatter {

    private Context context;
    private ArrayList<String> date;


    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart,Context context) {
        this.chart = chart;
        this.context=context;
        lol();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String d=dateFormat1.format(date);

        int days = (int) value;
        int c=-1;
        int year = determineYear(days);

        int month = determineMonth(days);

        String monthName = mMonths[month % mMonths.length];
        String yearName = String.valueOf(year);

        if (chart.getVisibleXRange() > 30 * 6) {

            return monthName + " " + yearName;
        } else {

            int dayOfMonth = determineDayOfMonth(days, month + 12 * (year - 2016));

            String appendix = "th";

            switch (dayOfMonth) {
                case 1:
                    appendix = "st";
                    break;
                case 2:
                    appendix = "nd";
                    break;
                case 3:
                    appendix = "rd";
                    break;
                case 21:
                    appendix = "st";
                    break;
                case 22:
                    appendix = "nd";
                    break;
                case 23:
                    appendix = "rd";
                    break;
                case 31:
                    appendix = "st";
                    break;
            }
            //    + " " + monthName

            return days+"";             //dayOfMonth == 0 ? "" : dayOfMonth + appendix;
        }
    }

    private int getDaysForMonth(int month, int year) {

        // month is 0-based

        if (month == 1) {
            boolean is29Feb = false;

            if (year < 1582)
                is29Feb = (year < 1 ? year + 1 : year) % 4 == 0;
            else if (year > 1582)
                is29Feb = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);

            return is29Feb ? 29 : 28;
        }

        if (month == 3 || month == 5 || month == 8 || month == 10)
            return 30;
        else
            return 31;
    }

    private int determineMonth(int dayOfYear) {

        int month = -1;
        int days = 0;

        while (days < dayOfYear) {
            month = month + 1;

            if (month >= 12)
                month = 0;

            int year = determineYear(days);
            days += getDaysForMonth(month, year);
        }

        return Math.max(month, 0);
    }

    private int determineDayOfMonth(int days, int month) {

        int count = 0;
        int daysForMonths = 0;

        while (count < month) {

            int year = determineYear(daysForMonths);
            daysForMonths += getDaysForMonth(count % 12, year);
            count++;
        }

        return days - daysForMonths;
    }

    private int determineYear(int days) {

        if (days <= 366)
            return 2016;
        else if (days <= 730)
            return 2017;
        else if (days <= 1094)
            return 2018;
        else if (days <= 1458)
            return 2019;
        else
            return 2020;

    }

    public void lol(){
            MainDB db=new MainDB(context);
        ArrayList<String> temp=new ArrayList<>();
        temp=db.getDBInfo();
            date=new ArrayList();
        for (int i=0; i<temp.size(); i++){
            date.add(temp.get(i).substring(8));
        }

    }
}
