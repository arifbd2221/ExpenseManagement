package com.countonit.user.swipetabpractice;

import android.content.Context;

/**
 * Created by User on 7/18/2017.
 */

public class DataBase3{




    public Context context;
    public static final String TABLE_NAME="DATE_COST";
    public static final String ID="ID";
    public static final String DATE="DATE";
    public static final String EXPENSE="EXPENSES";
    public static final String COST="COST";
    public static final String CATEGORY="CATEGORY";
    public static final String MONTH="MONTH";
    public static final String YEAR="YEAR";
    public static final String Table="CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+DATE+" VARCHAR(15) DEFAULT '', "+EXPENSE+" VARCHAR(30), "+COST+" VARCHAR(10) DEFAULT '', "+CATEGORY+" VARCHAR(20), "+MONTH+" VARCHAR(20), "+YEAR+" NUMBER(5) );";
    public static final String query="DROP TABLE IF EXISTS "+TABLE_NAME;



}
