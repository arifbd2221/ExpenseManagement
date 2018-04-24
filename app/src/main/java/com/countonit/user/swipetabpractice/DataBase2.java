package com.countonit.user.swipetabpractice;

/**
 * Created by User on 6/25/2017.
 */

public class DataBase2{

    public static final String Table_Name="MONTH_RECORDS";
    public static final String ID="_id";
    public static final String List_Column="LIST_COLUMN";
    public static final String Column_Name="AMOUNT";
    public static final String Column_Income="INCOME";
    public static final String Month_Name="MONTH";
    public static final String Year="Year";
    public static final String Budget="Budget";
    public static final String Create_Table="CREATE TABLE "+Table_Name+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Month_Name+" VARCHAR(100), "+List_Column+" VARCHAR(100), "+Column_Name+" INTEGER, "+Column_Income+" INTEGER , "+Year+" NUMBER(10), "+Budget+" number(10) );" ;
    public static final String query="DROP TABLE IF EXISTS "+Table_Name;


}
